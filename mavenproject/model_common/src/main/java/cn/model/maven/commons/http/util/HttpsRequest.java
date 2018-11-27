package cn.model.maven.commons.http.util;


import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class HttpsRequest extends HttpConfig implements IRequest {
	private HttpSSLConfig sslConfig;
	
	/**
	 * @Description:
	 *
	 * @param certPath
	 * @param certPass
	 */
	public HttpsRequest(String certPath, String certPass) {
		init(certPath, certPass);
	}
	
	private void init(final String certPath, final String certPass) {
		sslConfig = new HttpSSLConfig() {
			
			@Override
			public String initCertPath() {
				return certPath;
			}
			
			@Override
			public String initCertPass() {
				return certPass;
			}
		};
	}
	
//	@Override
	public String doPost(String url, String content, String ctype, String charset) throws Exception {
		String useCharset = (null == charset || "".equals(charset)) ? DEFAULT_CHARSET : charset;
		byte[] reqContent = {};
		if (content != null) {
			reqContent = content.getBytes(useCharset);
		}
		return doPost(url, ctype + ";charset=" + useCharset, reqContent);
	}
	
//	@Override
	public String doPostFile(String url, String fileName, byte[] content, String charset) throws Exception {
		HttpsURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		String useCharset = (null == charset || "".equals(charset)) ? DEFAULT_CHARSET : charset;
		String boundary = "--------httppostfile";
		try {
			conn = (HttpsURLConnection) getConnection(getHttpsUrl(url), METHOD_POST, "multipart/form-data; boundary=" + boundary);
			conn.setSSLSocketFactory(sslConfig.initSSLSocketFactory());
			conn.setHostnameVerifier(new HttpSSLConfig.DefaultHostnameVerifier());
			
			out = conn.getOutputStream();
			
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("\r\n");
			strBuf.append("--").append(boundary).append("\r\n");
			strBuf.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n");
			strBuf.append("Content-Type: application/octet-stream \r\n");
			strBuf.append("\r\n");
			out.write(strBuf.toString().getBytes(useCharset));
			out.write(content);
			
			strBuf.setLength(0);
			strBuf.append("\r\n");
			strBuf.append("--" + boundary + "--" + "\r\n");
			strBuf.append("\r\n");
			out.write(strBuf.toString().getBytes());
			
			out.flush();
			
			rsp = getResponseAsString(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != out) {
				out.close();
			}
			if (null != conn) {
				conn.disconnect();
			}
		}
		return rsp;
	}
    
	private String doPost(String url, String ctype, byte[] content) throws Exception {
		HttpsURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			conn = (HttpsURLConnection) getConnection(getHttpsUrl(url), METHOD_POST, ctype);
			conn.setSSLSocketFactory(sslConfig.initSSLSocketFactory());
			conn.setHostnameVerifier(new HttpSSLConfig.DefaultHostnameVerifier());

			out = conn.getOutputStream();
			out.write(content);
			out.flush();
			
			rsp = getResponseAsString(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != out) {
				out.close();
			}
			if (null != conn) {
				conn.disconnect();
			}
		}
		return rsp;
	}
	
//	@Override
	public String doGet(String url) throws Exception {
		HttpsURLConnection conn = null;
		String rsp = null;
		try {
			conn = (HttpsURLConnection) getConnection(getHttpsUrl(url), METHOD_GET, null);
			conn.setSSLSocketFactory(sslConfig.initSSLSocketFactory());
			conn.setHostnameVerifier(new HttpSSLConfig.DefaultHostnameVerifier());

			rsp = getResponseAsString(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != conn) {
				conn.disconnect();
			}
		}
		return rsp;
	}
	
//	@Override
	public Map<String, Object> doGetFile(String url) throws Exception {
		HttpsURLConnection conn = null;
		Map<String, Object> rsp = null;
		try {
			conn = (HttpsURLConnection) getConnection(getHttpsUrl(url), METHOD_GET, null);
			conn.setSSLSocketFactory(sslConfig.initSSLSocketFactory());
			conn.setHostnameVerifier(new HttpSSLConfig.DefaultHostnameVerifier());

			rsp = getResponseAsByteArray(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != conn) {
				conn.disconnect();
			}
		}
		return rsp;
	}
	
	private static URL getHttpsUrl(String url) throws MalformedURLException {

		System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
		URL httpsURL = new URL(null, url, new sun.net.www.protocol.https.Handler());
		return httpsURL;
	}
}
