package cn.model.maven.commons.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * description: 自定义属性配置类,在SPRING的XML中配置加载所有的属性文件
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {

    private static Properties properties;

    public static Integer getIntProperty(String name) {
        return Integer.parseInt(properties.getProperty(name, "0"));
    }

    public static String getStringProperty(String name) {
        return properties.getProperty(name, "");
    }

    public static String getProperty(String name,String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    public static Object get(String key){
        return properties.getProperty(key);
    }


    public static Properties getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Properties properties) {
        PropertyConfigurer.properties = properties;
    }
}
