package com.demo.bussiness.http.util;


import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public abstract class HttpSSLConfig {
    public abstract String initCertPath();
    public abstract String initCertPass();
    
    protected SSLSocketFactory initSSLSocketFactory() throws Exception {
    	System.setProperty("https.protocols", "TLSv1");
    	// 初始化SSL上下文
    	KeyManager[] kms = null;
    	if (StrNotNull(initCertPath()) && StrNotNull(initCertPass())) {
    		// 初始化密钥库
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
