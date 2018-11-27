package cn.model.maven.commons.enumutils;

/**
 * 数据类型
 * Created by itw_liuyf04 on 2017/10/18.
 */
public enum DataTypeEnum {

    XML {
        public String getValue() {return  "XML";}
    },
    JSON {
        public String getValue() {return  "JSON";}
    };

    public abstract String getValue();
}
