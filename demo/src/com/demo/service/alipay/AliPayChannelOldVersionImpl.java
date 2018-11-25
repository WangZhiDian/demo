package com.demo.service.alipay;

/*import cn.tk.hera.pay.commons.httpclient.HttpClientUtil;
import cn.tk.hera.pay.commons.httpclient.builder.HCB;
import cn.tk.hera.pay.commons.httpclient.common.HttpConfig;
import cn.tk.hera.pay.commons.httpclient.common.HttpHeader;
import cn.tk.hera.pay.commons.httpclient.common.SSLs;
import cn.tk.hera.pay.commons.httpclient.exception.HttpProcessException;
import cn.tk.hera.pay.commons.utils.Md5Encrypt;
import cn.tk.hera.pay.dto.CallPayRequestDTO;
import cn.tk.hera.pay.dto.OrderQueryResultDTO;*/
import com.alibaba.fastjson.JSONObject;
import com.demo.bussiness.http.RequestUtil;
import com.demo.common.md5.Md5Encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by wangdian05 on 2018/1/15.
 */
@Component
@Slf4j
public class AliPayChannelOldVersionImpl {


    public String queryOrderV1(JSONObject conf) {

        log.debug("query alipay accoutn start.....");
        String paygateway = AlipayConfig.paygateway;
        String bankurlParam = createParam("222333", conf);
        String response;
        try {
            response = sendHttpRequest(bankurlParam, paygateway);
            log.debug("response:" + response);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }



        return null;
    }

    public static String createParam(String outTradeNo , JSONObject conf) {
        String parameter = "";
        try {
            String sign_type =  AlipayConfig.sign_type;
            String service = "single_trade_query";       // 快速付款交易服务
            String partner = conf.getString("v1_partner");
            String key = conf.getString("v1_key");

            Map<String, String> params = new HashMap<String, String>();
            params.put("service", service);
            params.put("partner", partner);
            params.put("out_trade_no", outTradeNo);
            params.put("_input_charset", AlipayConfig.CHARSETV1);

            String sign = Md5Encrypt.md5(getContent(params, key));

            List keys = new ArrayList(params.keySet());
            for (int i = 0; i < keys.size(); i++) {
                try {
                    parameter = parameter + keys.get(i) + "=" + URLEncoder.encode((String) params.get(keys.get(i)), "utf-8") + "&";
                } catch (Exception e) {
                    System.out.println("--payment createurl error:" + e.toString());
                    e.printStackTrace();
                }
            }

            parameter = parameter + "sign=" + sign + "&sign_type=" + sign_type;
            log.debug("----parameter:" +	parameter);
        } catch (Exception e) {
            log.debug("----parameter:" +	e.toString());
        }
        return parameter;
    }

    private static String getContent(Map params, String privateKey) {
        List keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);
            if (i == keys.size() - 1) {
                prestr = prestr + key + "=" + value;
            }else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr + privateKey;
    }

    /**
     * param xmlMsg
     * param url
     * return
     * throws ParserConfigurationException
     * throws IOException
     * throws SAXException
     */
    private String sendHttpRequest(String content, String url) throws ParserConfigurationException, IOException, SAXException {
        String responseStr = null;
        try {
			responseStr = RequestUtil.initHttp().doPost(url, content, "application/json", "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
/*        try{
            Header[] header = HttpHeader.custom().other("Content-type", "text/plain;charset=utf-8").build();
            HttpConfig config = HttpConfig.custom();
            config.xml(xmlMsg);
            config.encoding("utf-8");
            config.headers(header);
            config.url(url);
            config.client(HCB.custom().sslpv(SSLs.SSLProtocolVersion.TLSv1).timeout(5000).build());
            responseStr =  HttpClientUtil.post(config);
            log.info("response=="+responseStr);
        } catch (HttpProcessException e) {
            e.printStackTrace();
        }*/
        return responseStr;
    }

}
