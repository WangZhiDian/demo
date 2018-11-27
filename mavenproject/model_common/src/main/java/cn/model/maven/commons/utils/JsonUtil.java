package cn.model.maven.commons.utils;

import cn.model.maven.commons.enumutils.DataTypeEnum;
import cn.model.maven.exception.ExceptionCode;
import cn.model.maven.exception.IllegalBusinessException;
import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Json 工具类 wangguangyu qrw:使用FastJson重新写了代码，性能有大幅度提升，并且减少了内存和CPU消耗
 */
@Slf4j
public class JsonUtil {
    private static ConcurrentHashMap<String, String> pathCache = new ConcurrentHashMap<String, String>(1024, 0.75f, 1);

    public static JsonUtil instance = new JsonUtil();

    static {
        JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
    }

    public static String getStringValue(JSONObject jsonData, String path) {
        Object val = getValue(jsonData, path, false);
        return val != null ? val.toString() : null;
    }

    public static Object getValue(JSONObject jsonData, String path) {
        return getValue(jsonData, path, false);
    }

    public static Object getValue(JSONObject jsonData, String path, boolean jsonPath_flag) {
        try {
            if (path == null) return null;
            path = jsonPath_flag ? path : transferToJsonPath(path);
            if (path.contains(".")) {
                return JSONPath.eval(jsonData, path);
            } else {
                return jsonData.get(path);
            }
        } catch (Exception e) {
            return null;
        }
    }

    // 转化成符合jsonpath格式的路径：id_policy.insurants.0.beneList.0.person.name -->
    // id_policy.insurants[0].beneList[0].person.name
    public static String transferToJsonPath(String path) {
        String path_inner = pathCache.get(path);
        if (path_inner == null) {
            path_inner = path.replaceAll("\\.([0-9]+)([\\.]?)", "[$1\\]$2");
            pathCache.putIfAbsent(path, path_inner);
        }
        return path_inner;
    }

    // will use the fastjson function to modify the json content
    // 使用FastJson的set方法来设置值,如果成功则返回true，否则 false
    public static boolean setValueByJsonPath(JSONObject rootObject, String path, Object value) {
        try {
            JSON.DEFAULT_GENERATE_FEATURE |= SerializerFeature.DisableCircularReferenceDetect.getMask();
            JSONPath jsonpath = JSONPath.compile(path);
            return jsonpath.set(rootObject, value);
        } catch (Exception e) {
            return false;
        }
    }

    // 使用Jsonpath格式设置值 path= a.b[0].c.d[0].e
    public static boolean setValue(JSONObject rootObject, String path, Object value) {
        return setValue(rootObject, path, value, true);
    }

    // 设置Json里面的内容，如果Json里面没有这个值，则生成这个key
    public static boolean setValue(JSONObject rootObject, String path, Object value, boolean jsonPath_format) {
        String parentPath = jsonPath_format ? path : transferToJsonPath(path);

        String currentPath = "";
        String key = "";
        Object valueObj = value;

        boolean result = false;
        boolean lastNode = false;

        while (!lastNode && !(result = setValueByJsonPath(rootObject, parentPath, valueObj))) {

            int indexPos = parentPath.lastIndexOf(".");

            currentPath = parentPath.substring(indexPos + 1);

            if (indexPos >= 0) {
                parentPath = parentPath.substring(0, indexPos);
            } else {
                lastNode = true;
                parentPath = null;
            }

            int indexPos2 = currentPath.indexOf('[');
            if (indexPos2 > 0) {
                key = currentPath.substring(0, indexPos2);

                ArrayList tempObj = null;
                if (parentPath != null) {
                    Object obj = getValue(rootObject, parentPath + "." + key, true);
                    if (!"".equals(obj)) {
                        tempObj = (ArrayList) obj;
                    }
                } else {
                    Object obj = getValue(rootObject, key, true);
                    if (!"".equals(obj)) {
                        tempObj = (ArrayList) obj;
                    }
                }

                if (tempObj == null) {
                    tempObj = new ArrayList();
                }

                tempObj.add(valueObj);
                valueObj = tempObj;
            } else {
                key = currentPath;
            }

            if (lastNode) {
                rootObject.put(key, valueObj);
            } else {
                JSONObject tempObj = null;
                Object obj = getValue(rootObject, parentPath, true);
                if (!"".equals(obj)) {
                    tempObj = (JSONObject) obj;
                }

                if (tempObj == null) {
                    tempObj = new JSONObject();
                }
                tempObj.put(key, valueObj);
                valueObj = tempObj;
            }
        }

        return result;
    }

    private static JSONObject merge(JSONObject target, Object source) {
        if (source == null) return target;
        if (source instanceof JSONObject) return merge(target, (JSONObject) source);
        throw new RuntimeException("JSON megre can not merge JSONObject with " + source.getClass());
    }

    private static JSONArray merge(JSONArray target, Object source) {
        if (source == null) return target;
        if (target instanceof JSONArray) return merge(target, (JSONArray) source);
        target.add(source);
        return target;
    }

    private static JSONArray merge(JSONArray target, JSONArray source) {
        target.addAll(source);
        return target;
    }

    // 合并另个json
    public static JSONObject merge(JSONObject target, JSONObject source) {
        return merge(target, source, true, false);
    }

    public static JSONObject merge(JSONObject target, JSONObject source, boolean overwrite) {
        return merge(target, source, true, true);
    }

    /*
     * overwrite: 当处理数组时，如果为true使用新的数组完全覆盖老的数组。 当为false时，在老的数组尾部添加新的元素
     * appendItemFlag: 当为true时，source里面的可以如果没有在target存在，则添加到target里面。
     * 当为false时，则不添加，这样意味着只会使用source里面的值去替换target对应的key
     */
    public static JSONObject merge(JSONObject target, JSONObject source, boolean overwrite, boolean appendItemFlag) {
        if (source == null) return target;

        for (String key : target.keySet()) {
            Object value1 = target.get(key);
            Object value2 = source.get(key);
            if (value2 == null) continue;
            if (value1 instanceof JSONArray) {
                if (overwrite)
                    target.put(key, value2);
                    // target.put(key, mergeItemContent((JSONArray) value1,
                    // (JSONArray) value2));
                else
                    target.put(key, merge((JSONArray) value1, value2));
                continue;
            }

            if (value1 instanceof JSONObject) {
                target.put(key, merge((JSONObject) value1, value2));
                continue;
            }

            if (value1.equals(value2)) {
                continue;
            } else if (overwrite) {
                target.put(key, value2);
                continue;
            }

            if (value1.getClass().equals(value2.getClass())) throw new RuntimeException(
                    "JSON merge can not merge two " + value1.getClass().getName() + " Object together");
            throw new RuntimeException(
                    "JSON merge can not merge " + value1.getClass().getName() + " with " + value2.getClass().getName());
        }

        if (appendItemFlag) {
            for (String key : source.keySet()) {
                if (target.containsKey(key)) continue;
                target.put(key, source.get(key));
            }
        }
        return target;
    }

    /**
     * 支持多级添加
     *
     * @param jsonData
     * @param path
     * @param jsonValue
     * @wangguangyu
     */
    public static void setStringValue(JSONObject jsonData, String path, String jsonValue) {
        setValue(jsonData, path, jsonValue, false);
    }

    /**
     * Convert a JSONObject into a well-formed, element-normal XML string.
     *
     * @param object A JSONObject.
     * @return A string.
     * @throws JSONException
     */
    public static String toXMLString(Object object) throws JSONException {
        return toXMLString(object, null);
    }

    public static String escape(String string) {
        StringBuilder sb = new StringBuilder(string.length());
        for (int i = 0, length = string.length(); i < length; i++) {
            char c = string.charAt(i);
            switch (c) {
                case '&':
                    sb.append("&amp;");
                    break;
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Convert a JSONObject into a well-formed, element-normal XML string.
     *
     * @param object  A JSONObject.
     * @param tagName The optional name of the enclosing tag.
     * @return A string.
     * @throws JSONException
     */
    public static String toXMLString(Object object, String tagName) throws JSONException {
        StringBuilder sb = new StringBuilder();
        int i;
        JSONArray ja;
        JSONObject jo;
        String key;
        Iterator<String> keys;
        int length;
        String string;
        Object value;
        if (object instanceof JSONObject) {

            // Emit <tagName>

            if (tagName != null) {
                sb.append('<');
                sb.append(tagName);
                sb.append('>');
            }

            // Loop thru the keys.

            jo = (JSONObject) object;
            keys = jo.keySet().iterator();
            while (keys.hasNext()) {
                key = keys.next();
                value = jo.get(key);
                if (value == null) {
                    value = "";
                }
                string = value instanceof String ? (String) value : null;

                // Emit content in body

                if ("content".equals(key)) {
                    if (value instanceof JSONArray) {
                        ja = (JSONArray) value;
                        length = ja.size();
                        for (i = 0; i < length; i += 1) {
                            if (i > 0) {
                                sb.append('\n');
                            }
                            sb.append(escape(ja.get(i).toString()));
                        }
                    } else {
                        sb.append(escape(value.toString()));
                    }

                    // Emit an array of similar keys

                } else if (value instanceof JSONArray) {
                    ja = (JSONArray) value;
                    length = ja.size();
                    for (i = 0; i < length; i += 1) {
                        value = ja.get(i);
                        if (value instanceof JSONArray) {
                            sb.append('<');
                            sb.append(key);
                            sb.append('>');
                            sb.append(toXMLString(value));
                            sb.append("</");
                            sb.append(key);
                            sb.append('>');
                        } else {
                            sb.append(toXMLString(value, key));
                        }
                    }
                } else if ("".equals(value)) {
                    sb.append('<');
                    sb.append(key);
                    sb.append("/>");

                    // Emit a new tag <k>
                } else {
                    sb.append(toXMLString(value, key));
                }
            }
            if (tagName != null) {
                // Emit the </tagname> close tag
                sb.append("</");
                sb.append(tagName);
                sb.append('>');
            }
            return sb.toString();

            // XML does not have good support for arrays. If an array appears in
            // a place
            // where XML is lacking, synthesize an <array> element.

        } else {
            if (object instanceof JSONArray) {
                ja = (JSONArray) object;
                length = ja.size();
                for (i = 0; i < length; i += 1) {
                    sb.append(toXMLString(ja.get(i), tagName == null ? "array" : tagName));
                }
                return sb.toString();
            } else {
                string = (object == null) ? "null" : escape(object.toString());
                return (tagName == null) ? "\"" + string + "\""
                        : (string.length() == 0) ? "<" + tagName + "/>"
                        : "<" + tagName + ">" + string + "</" + tagName + ">";
            }
        }
    }

    private static JSONObject transferArrayToJSON(JSONArray value_arr, String indexKeyName) {
        JSONObject targetJson = new JSONObject();
        for (int i = 0; i < value_arr.size(); i++) {
            JSONObject json_item = (JSONObject) value_arr.get(i);
            targetJson.put(json_item.getString(indexKeyName), json_item);
        }
        return targetJson;
    }

    public static JSONArray mergerJson(JSONArray target, JSONArray source, String indexKeyName) {
        JSONObject targetJson = transferArrayToJSON(target, indexKeyName);
        JSONArray resultJson = new JSONArray();

        for (int i = 0; i < source.size(); i++) {
            JSONObject source_item = (JSONObject) source.get(i);
            if (targetJson.containsKey(source_item.getString(indexKeyName))) {
                JSONObject target_item = (JSONObject) targetJson.get(source_item.getString(indexKeyName));
                JSONObject merged_Json = merge(target_item, source_item);
                resultJson.add(merged_Json);
            }
        }

        return resultJson;
    }

    public static JSONObject mergerJson(JSONObject targetJsonObj, JSONObject sourceJsonObj, String arrayPath,
                                        String indexKeyName) {
        JSONArray target = (JSONArray) JSONPath.eval(targetJsonObj, arrayPath);
        JSONArray source = (JSONArray) JSONPath.eval(sourceJsonObj, arrayPath);

        setValue(sourceJsonObj, arrayPath, mergerJson(target, source, indexKeyName));
        return sourceJsonObj;
    }

    /**
     * jsonschema校验
     *
     * @param jsonschema 用于校验的jsonschema
     * @param jsonString 待检验json字符串
     */
    public static void validate(String jsonschema, String jsonString) {
        //log.debug("====validate(String jsonschema, String jsonString) start====");
        try {
            if (StringUtils.isNotEmpty(jsonschema)) {
                ObjectMapper oMapper = new ObjectMapper();
                final JsonNode fstabSchema = oMapper.readTree(jsonschema);
                final JsonNode reqSchema = oMapper.readTree(jsonString);

                final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
                final JsonSchema schema = null;//factory.getJsonSchema(fstabSchema);
                ProcessingReport report = schema.validate(reqSchema);

                if (!report.isSuccess()) {
                    //log.debug("====jsonSchemaValidation result:{}====",report.toString());
                    throw new IllegalBusinessException(ExceptionCode.SYSTEM_ILLEGAL_REQUEST_B);
                }
            } else {
                //log.debug("====jsonSchemaValidation jsonSchema is null====");
            }
        } catch (IllegalBusinessException e) {
            throw e;
        } catch (Exception e) {
            //log.error("====jsonSchemaValidation Exception====",e);
            throw new IllegalBusinessException(ExceptionCode.SYSTEM_ILLEGAL_REQUEST_B);
        }
        //log.debug("====validate(String jsonschema, String jsonString) end====");
    }

    /**
     * 合并json--遍历源json的key，把目标json中不存在或节点值为空的节点进行合并
     *
     * @param target 目标json 合并到该json中
     * @param source 源json
     */
    public static void mergeIfBlank(JSONObject target, JSONObject source) {
        Set<String> keys = source.keySet();
        for (String key : keys) {
            String value1 = target.getString(key);
            String value2 = source.getString(key);
            if (StringUtils.isBlank(value1) && StringUtils.isNotBlank(value2)) {
                target.put(key, source.get(key));
            }
        }
    }

    public static void mergeByNotBlank(JSONObject target, JSONObject source) {
        Set<String> keys = source.keySet();
        for (String key : keys) {
            String value2 = source.getString(key);
            if (StringUtils.isNotBlank(value2)) {
                target.put(key, source.get(key));
            }
        }
    }

    public static JSONArray mergeAllArr(JSONArray target, JSONArray source) {
        if (source == null) return target;
        int num = target.size();
        for (int i = 0; i < source.size(); i++) {
            Object value2 = source.get(i);
            if (i >= num) {
                //超出目标数组的索引，直接覆盖
                target.add(i, value2);
                continue;
            }
            Object value1 = target.get(i);

            if (value1 instanceof JSONObject && value2 instanceof JSONObject) {
                mergeAll((JSONObject) value1, (JSONObject) value2);
                continue;
            }
            if (value1 instanceof JSONArray && value2 instanceof JSONArray) {
                mergeAllArr((JSONArray) value1, (JSONArray) value2);
                continue;
            }
            //覆盖
            target.add(i, value2);
        }
        return target;
    }

    /**
     * 合并所有节点，已有节点覆盖
     *
     * @param target
     * @param source
     * @return
     */
    public static JSONObject mergeAll(JSONObject target, JSONObject source) {
        if (source == null) return target;

        for (String key : source.keySet()) {
            Object value1 = target.get(key);
            Object value2 = source.get(key);
            if (value1 == null) {
                //目标节点为空直接覆盖
                target.put(key, value2);
                continue;
            }
            if (value1 instanceof JSONObject && value2 instanceof JSONObject) {
                mergeAll((JSONObject) value1, (JSONObject) value2);
                continue;
            }
            if (value1 instanceof JSONArray && value2 instanceof JSONArray) {
                mergeAllArr((JSONArray) value1, (JSONArray) value2);
                continue;
            }
            //覆盖
            target.put(key, value2);
        }
        return target;
    }

    /**
     * JSON字符串转换成对象
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    /**
     * Java对象转换成JSON字符串
     *
     * @param obj
     * @return
     */
    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }


    /**
     * JSON 转 OBJECT
     * @param type
     * @param body
     * @param c
     * @return
     */
    public static Object getObjectFromJson(DataTypeEnum type, String body, Class<?> c) {
        if (type == DataTypeEnum.JSON) {
            return JSON.parseObject(body,c);
        } else if (type == DataTypeEnum.XML) {
            ObjectUtil binder = new ObjectUtil(c);
            return binder.fromXml(body);
        }
        return null;
    }

    /**
     * 格式化json字符串
     * @param jsonStr
     * @return
     */
    public static String formatJson(String jsonStr) {
        int level = 0;
        StringBuffer jsonFormatStr = new StringBuffer();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if(level > 0 && '\n' == jsonFormatStr.charAt(jsonFormatStr.length() - 1)) {
                jsonFormatStr.append(getLevelStr(level));
            }
            if(i == 0) {
                jsonFormatStr.append("\n");
            }
            switch(c) {
                case '{' :
                case '[' :
                    jsonFormatStr.append(c + "\n");
                    level++;
                    break;
                case ',' :
                    jsonFormatStr.append(c + "\n");
                    break;
                case '}' :
                case ']' :
                    jsonFormatStr.append("\n");
                    level--;
                    jsonFormatStr.append(getLevelStr(level));
                    jsonFormatStr.append(c);
                    break;
                default:
                    jsonFormatStr.append(c);
                    break;
            }
        }
        return jsonFormatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int i = 0; i < level; i++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

}
