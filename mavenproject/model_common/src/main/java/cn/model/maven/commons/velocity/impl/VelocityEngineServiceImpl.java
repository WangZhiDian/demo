package cn.model.maven.commons.velocity.impl;

import cn.model.maven.commons.cache.service.CacheService;
import cn.model.maven.commons.velocity.VelocityEngineService;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.log.SystemLogChute;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * @Description TODO
 * @author wanghl80
 * @date 2017年5月1日 上午11:35:55
 * @version V2.0
 */
@Slf4j
@Component
public class VelocityEngineServiceImpl implements VelocityEngineService
{
    private static final Logger logger = LoggerFactory.getLogger(VelocityEngineServiceImpl.class);
    @Autowired
    private CacheService redis;

    static {
        Velocity.setProperty(Velocity.RESOURCE_LOADER, "string");
        Velocity.addProperty("string.resource.loader.class", StringResourceLoader.class.getName());
        Velocity.addProperty("string.resource.loader.modificationCheckInterval", "1");
        Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, SystemLogChute.class.getName());
        Velocity.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        Velocity.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        Velocity.init();
    }

    /**
     * 替换模板
     * 
     * @param context
     *            待替换数据
     * @param templateContent
     *            模板内容
     * @return
     */
    public String transferString(VelocityContext context, String templateKey, String templateContent) {
        Template tp = null;
        StringResourceRepository repo = StringResourceLoader.getRepository();
        if (templateContent != null) {
            repo.putStringResource(templateKey, templateContent);
            tp = Velocity.getTemplate(templateKey);
        }
        if (tp != null) {
            StringWriter writer = new StringWriter();
            tp.merge(context, writer);
            return writer.toString();
        }
        else {
            return null;
        }
    }

    /**
     * 替换模板
     * 
     * @param context
     *            待替换数据
     * @param templateContent
     *            模板内容
     * @return
     */
    public String transferString(VelocityContext context, String templateContent) {
        int hashCode = templateContent.hashCode();
        StringWriter sw = null;
        try {
            String xml=transferString(context, Integer.toString(hashCode), templateContent);
            org.dom4j.Document document = DocumentHelper.parseText(xml);
            sw = new StringWriter();
            OutputFormat of = OutputFormat.createCompactFormat();
            of.setEncoding("GBK");
            XMLWriter xw = new XMLWriter(sw, of);
            xw.write(document);
            xw.flush();
            sw.close();
        } catch (Exception e) {
            logger.error("transferString Exception:",e);
        }
        return sw.toString();
    }

    public String transferEncodeString(VelocityContext context, String templateContent, String charset) {
        int hashCode = templateContent.hashCode();
        StringWriter sw = null;
        try {
            String xml=transferString(context, Integer.toString(hashCode), templateContent);
            org.dom4j.Document document = DocumentHelper.parseText(xml);
            sw = new StringWriter();
            OutputFormat of = OutputFormat.createCompactFormat();
            of.setEncoding(charset);
            XMLWriter xw = new XMLWriter(sw, of);
            xw.write(document);
            xw.flush();
            sw.close();
        } catch (Exception e) {
            logger.error("transferString Exception:",e);
        }
        return sw.toString();
    }


    public String getTemplateFile(String filePath)
    {
        try {
            String template=redis.getString("TemplateFile_"+Integer.toString(filePath.hashCode()));
            if(Strings.isNullOrEmpty(template))
            {
                template= Files.toString(new File(filePath), Charset.forName("GBK"));
                redis.setString("TemplateFile_"+Integer.toString(filePath.hashCode()),template,60*60*24*30);
            }
            return template;
        } catch (IOException e) {
            log.info("gettemplateFile IOException:{}",e);
        }
        return null;
    }

    public String getTemplateFile(String filePath,String charset)
    {
        try {
            String template=redis.getString("TemplateFile_"+Integer.toString(filePath.hashCode()));
            if(Strings.isNullOrEmpty(template))
            {
                template= Files.toString(new File(filePath), Charset.forName(charset));
                redis.setString("TemplateFile_"+Integer.toString(filePath.hashCode()),template,60*60*24*30);
            }
            return template;
        } catch (IOException e) {
            log.info("gettemplateFile IOException:{}",e);
        }
        return null;
    }
}
