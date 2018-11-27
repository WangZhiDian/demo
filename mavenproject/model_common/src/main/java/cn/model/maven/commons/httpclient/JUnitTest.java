package cn.model.maven.commons.httpclient;

import cn.model.maven.commons.httpclient.builder.HCB;
import cn.model.maven.commons.httpclient.common.HttpConfig;
import cn.model.maven.commons.httpclient.common.HttpHeader;
import cn.model.maven.commons.httpclient.exception.HttpProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.junit.Test;

/**
 * Created by wangdian05 on 2018/6/29.
 */
@Slf4j
public class JUnitTest {

    @Test
    public void testHttp()
    {
        String msg = "", url = "";
        String response = sendXmlHttpRequest(msg, url);
        log.info("response:{}", response);
    }

    public static String sendXmlHttpRequest(String xmlMsg, String url){
        log.info("request=="+xmlMsg);
        String responseStr = null;
        try{
            Header[] header = HttpHeader.custom().other("Content-type", "text/xml;charset=utf-8").build();
            HttpConfig config = HttpConfig.custom();
            config.xml(xmlMsg);
            config.encoding("utf-8");
            config.headers(header);
            config.url(url);
            config.client(HCB.custom().timeout(5000).build());
            responseStr =  HttpClientUtil.post(config);
            log.info("response=="+responseStr);
        } catch (HttpProcessException e){
            log.error("wechat sendHttpRequest:" , e);
        }
        return responseStr;
    }



}
