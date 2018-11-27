package cn.model.maven.commons.http.util;


import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public abstract class HttpSSLConfig {
    public abstract String initCertPath();
    public abstract String initCertPass();
    
    protected SSLSocketFactory initSSLSocketFactory() throws Exception {
    	System.setProperty("https.protocols", "TLSv1");
    	// ��ʼ��SSL������
    	KeyManager[] kms = null;
    	if (StrNotNull(initCertPath()) && StrNotNull(initCertPass())) {
    		// ��ʼ����Կ��
    		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    		KeyStore clientStore = KeyStore.getInstance("PKCS12");
    		clientStore.load(new FileInputStream(initCertPath()), initCertPass().toCharArray());
    		kmf.init(clientStore, initCertPass().toCharArray());
    		kms = kmf.getKeyManagers();
    	}
    	
//		SSLContext sslContext = SSLContext.getInstance("TLS");
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(kms, new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
		return sslContext.getSocketFactory();
    }

	private static class DefaultTrustManager implements X509TrustManager {

//		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

//		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {
		}

//		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

	}
	
	protected static class DefaultHostnameVerifier implements HostnameVerifier {

//		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
		
	}
	private boolean StrNotNull(String str)
	{
		if(str == null || "".equals(str))
			return false;
		else
			return true;
	}
}
