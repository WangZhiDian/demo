package cn.model.maven.commons.velocity;

import org.apache.velocity.VelocityContext;

/**
 * @Description TODO
 * @author wanghl80
 * @date 2017年5月1日 上午11:34:27
 * @version V2.0
 */
public interface VelocityEngineService 
{
	/**
     * 替换模板
     * 
     * @param context
     *            待替换数据
     *            模板内容
     * @return
     */
    public String transferString(VelocityContext context, String templateKey, String templateContent);

    public String transferString(VelocityContext context, String templateContent);

    public String getTemplateFile(String filePath);

    public String getTemplateFile(String filePath, String charset);

    public String transferEncodeString(VelocityContext context, String templateContent, String charset);

}
