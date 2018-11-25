package com.demo.common.velocity.impl;

import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.log.SystemLogChute;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.stereotype.Service;

import com.demo.common.velocity.VelocityEngineService;

@Service
public class VelocityEngineServiceImpl implements VelocityEngineService {

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
     * @param inString
     *            模板内容
     * @return
     */
    public String transferString(VelocityContext context, String templateKey, String templeContent) {
        Template tp = null;
        StringResourceRepository repo = StringResourceLoader.getRepository();
        if (templeContent != null) {
            repo.putStringResource(templateKey, templeContent);
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
     * @param inString
     *            模板内容
     * @return
     */
    public String transferString(VelocityContext context, String templeContent) {
        int hashCode = templeContent.hashCode();
        return transferString(context, Integer.toString(hashCode), templeContent);
    }
}
