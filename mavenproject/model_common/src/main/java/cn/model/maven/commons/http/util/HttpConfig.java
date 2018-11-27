package cn.model.maven.commons.http.util;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class HttpConfig {
	
	protected static final String METHOD_GET = "GET";
	protected static final String METHOD_POST = "POST";
	protected static final String DEFAULT_CHARSET = "utf-8";
	public static final String CONTENT_TYPE_XML = "text/xml";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String CONTENT_TYPE_TXT = "text/plain";
	public static final String CONTENT_TYPE_HTML = "text/html";

	//连接超时时间，默认10秒
    private static int readTimeout = 100000;

  //连接超时时间，默认30秒
    private static int connectTimeout = 30000;

	protected HttpURLConnection getConnection(URL url, String method, String ctype) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setRequestProperty("User-Agent", "stargate");
		conn.setRequestProperty("Connection", "Keep-Alive");
		if (StrNotNull(ctype)) {
			conn.setRequestProperty("Content-Type", ctype);
		}
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		
		return conn;
	}

	protected String getResponseAsString(HttpURLConnection conn) throws IOException {
		InputStream es = null;
		InputStream is = null;
		try {
			es = conn.getErrorStream();
			is = conn.getInputStream();
			String charset = getResponseCharset(conn.getContentType());
			if (es == null) {
				return getStreamAsString(conn.getInputStream(), charset);
			} else {
				String msg = getStreamAsString(es, charset);
				if (!StrNotNull(msg)) {
					throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
				} else {
					throw new IOException(msg);
				}
			}
		} finally {
			if (null != es) {
				es.close();
			}
			if (null != is) {
				is.close();
			}
		}
	}
	
	protected Map<String, Object> getResponseAsByteArray(HttpURLConnection conn) throws IOException {
		InputStream is = null;
		try {
			is = conn.getInputStream();
			String ctype = conn.getContentType();
			if (!StrNotNull(ctype)) {
				throw new IOException("ָ类型为空");
			} else {
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int n = 0;
				while (-1 != (n = is.read(buffer))) {
					output.write(buffer, 0, n);
				}
				output.flush();
				byte[] fileContent = output.toByteArray();
				output.close();
				
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("data", fileContent);
				result.put("type", ctype);
				return result;
			}
		} finally {
			if (null != is) {
				is.close();
			}
		}
	}

	protected String getStreamAsString(InputStream stream, String charset) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
		StringWriter writer = new StringWriter();

		char[] chars = new char[256];
		int count = 0;
		while ((count = reader.read(chars)) > 0) {
			writer.write(chars, 0, count);
		}
		return writer.toString();
	}

	protected String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;

		if (StrNotNull(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (StrNotNull(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}
	
	private boolean StrNotNull(String str)
	{
		if(str == null || "".equals(str))
			return false;
		else
			return true;
	}
}
