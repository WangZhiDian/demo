package test.other.autolink;

import org.apache.commons.codec.binary.Base64;

public class OauthGenerator {
	public static void main(String[] args)
	{

		String url = "http://wechat.wei.taikang.com/shortAccident/page/index.html";
//		String url = "http://wechat.wei.taikang.com/ticketsInsu/page/ticket_insu_claim.html";
//		url = "http://m.taikang.com/service/weixinbind/releaseBinding.html";
//		url = "http://m.tk.cn/service/weixinbind/releaseBinding.html";
//		url = "http://weit.tk.cn/product/child_emergency/v1/page/guide.html";
		url = "http://ecuat.tk.cn/service/weixinbind/identityBinding.html?name=wangdian&age=23&";
		String oathUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcd7143c00e5bb6f7&redirect_uri=http://wxpt.taikang.com/tkmap/wechat/oauth2/redirect/wxcd7143c00e5bb6f7?other=###&response_type=code&scope=snsapi_base&state=redict#wechat_redirect";
//		String oathUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx90b252e89d5742e3&redirect_uri=http://wxpt.taikang.com/tkmap/wechat/oauth2/redirect/wx90b252e89d5742e3?other=###&response_type=code&scope=snsapi_base&state=redict#wechat_redirect";

		
		try
		{
			url = Base64.encodeBase64URLSafeString(url.getBytes());
			System.out.println(url);
			System.out.println(oathUrl.replace("###", url));
			//System.out.println(new String(Base64.decodeBase64(aa)));
//        	System.out.println(parentOpenId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
