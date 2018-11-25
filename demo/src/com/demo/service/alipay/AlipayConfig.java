package com.demo.service.alipay;

public class AlipayConfig {
/*	// 商户appid  寿险生产
	public static String APPID = "2017062907595828";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDX8OhEV9ShIKwXEi/Fq1UonwGZaYo3biPDYJVKK2x64H6ahQievROhSJSNqhHEBQIryy4hxGSyNXQ9lF8qz40Lvx08pTBk+wlpyGM3bl7mphPev/BKrrV5DdXUE985jy4SJnOOILAqi5ShzW863n3tSqsz/szQDB5iOgY77z1rs7VI/ogX25U11/TBLlRens10zpcmuih/E2QDIMm7g3vm3f5ekTMmnig8o6dd7MqUO+bVTO+q343UFvPh/GC2fnimxfvqzU9z1wPOx7wvN5U2sD3oLIlWfTM6QY5SpOOXJox1pnNSOKo/+8KcDUAU1+gyXSPbYv1JfQ0lOQFYFi+vAgMBAAECggEBAIrXsN/QgTygn0WuQd0czoJxyjtzqpT9Ui1bOmIYDYXsNjrv8pzuAWMgIbP8d7PLbJvv5dd8SSp0SCsX7SC/lnbJCOlnZPhomKLVX0XSx14RxpDgZIYAU1pkVOopZMvDjeHJq17lEaApYwWt6yi0yc76xZ2G5PWX9Sk/GacLqjhGg4OEGYTd4VJlxhNMO5ODAu3qvFBt9lmQmoiuJSLVSg7PxoQh6ez0Z2D1KI0hh/INcGcKpyFuKOZoA8U0RxN1YCKz2xrj9dGV6WHQ3cr0WO/RAmhUVq3wpXkj/7bbY6bVhmBTiLyFtHar2nminjosGiISidw4jj3HcxlefrCbGzECgYEA8Ufm+eaCOvZQRnbU0I4hChIRjP0pXjsuwzv9FfoGfKwB+aCVRfQ/KXIRMPYx1i6HQ7yKrdP2daqj/6ivUJ7dco7HSE2IXJc39TiRKWYTpjF6af20WHCSgeqBl5Gp3TONirlaAIevGjx/HLFRsT2S8N+r5KasMAYcQ5PEd8B6Lg0CgYEA5R1FgKaz5bXk32iaSMCL0Zig0APP6FD/a15QARN0DHxNp5J+oTOxiC51Y6Sao8sFSin1JIULXnxiFoR2zF3heQmYuSNnq6/Gd621+Wwp2gXwi0J1kpAENlyC2oLAOa/07jXjxu7zK067vS0avwmO/ERpUOeXjDs9NIljxPQA4asCgYBjBS81v/QSXqPzRzFvpeTJTUEGq58enQtD9IqZSlL/N1hYKh8Y5nRFfhqwDVnGnLHGisofLK4o/C7x7TiFzaz0JsSCV89usGC2stBgPRQzUHr8pLIu2YRG7QbiPVWJAm9ET20wan7iNZG1JNBx/OMhF6pj3EMuMlPHKA2pV8TMwQKBgFFP+Sm2gCQnUOoE+rSVOUoSrAgHQlNF49BcMXKgT9BakUjGkhBuAzS8oEbT1VoKJH90sp7aZPWMmVc6fdr7+ph1UnFqYlGpWXuB+BJkU9ofv4X8+TMhz4znej1IqCYRD5E3KPaiOUUGdVSnXFWuc8OOF/5xovRO1eL+W507p9BvAoGAYZgEw7WCJrALtubAYCrgU+fm57Dmme+Slf2PVa0ZkMU+s7j0GXuoGZrI9XyEQqPI8/xJduSNSXAo27kfC1bz902YD5B4q/TNn9VWKtHlN0aMELAnOtH27855vSgbwCW1+Fm7xUuHWd1WVM1gOhUB2L70NxApsoBgN+IDggwcsdg=";
	*/
	
	// 商户appid  财险生产（非车）
	public static String APPID = "2016031401209561";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjb/7csbAkqYwYBc8FCY+W3u/gaB0UhSguOEJPGr9t7TWgWjACcS7GhCeAka+p+5fZhWBAuMjGgIoqOLoPqmgGPLaj1N04zM0DV/BOfVEWz29LNDUqC/6yrWDRJzLDuTo0E+LdpK8//zju5uoH3d1Jt8nd8rpgu8PwfafM7VcRiN3sKIaS4JIDlV6gQKM5mHswX0PyhDlrq+xkWCA6sXT5qqBwt9NUou+p3Axjsb5tVHsAl4LIIuEJale41jyenBwWPtIoIkFfb4p6y/gM1RKWE5yBEvguAa+2yKYztKUjpFot7cb1H6Gqam2eo0RPSLz8Rw/rZ3djDtyl/gciaXHvwIDAQAB";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCWn2t1jlquqz/VHOc8eHOcF/83p87ErqfHHG8IwnJoRv8laqcRdV51+QI6rWh0LbRPg+58K6EAjT23Ql2RdihKigH4gn6GZCvC9WCMaD1xwjfRnSn0ukpas5UpDoPEhIF/LVxEtLFqDZ+utsf8RAP/8cAkn+9RffI6B8ALLj7Xchb06yKWdYPCl+XDMQMjTc6BLZIAlrSS5IJfL5mh80XbaYIjvq51R3WoAGBID8DUZeJsHjY/nKmWXcKieygJqMFCZwhDexz6g9VOGrGUxtolJBuVoFbOF2kY1lWsK8qDplLJ6uLzTmycOFfe8YSOYGsTKgu0r2VilbaEWvYpdNLHAgMBAAECggEADWpDeuPj5byw4b1CRxt/SH7Gp2FVxCS+IrjAluoioHD6Vo3KNX7bm+xCq5E4RWiwr4hTnFGSdGcgAQtzI/p/Gixygny04ilX0HRkVf0Ow+wFZrD1xKs25h8qgM6fI8iddANJPYFOb+CowTncU/aG2KvibiCkgwCRq2N3UkebJzsJTaHbYKuYYYrjIKODYx8Cf8FIqUcs+5RA/1bCvUuSaRQhWVe7up3J86nrUnmPbB9CUn9o5D65OzQ7MFRXbl/hok7lo9g1ZPhAAE3mhftDlMp1Vu7C3OINQe7bjls+lDQBeX0qzwX4bHSv2Oj0RT7V5aDV5fw0xhTBzlAU4YwjQQKBgQDzxRqC+xiDlu6sPplnWMIUheWIWJ8ZU9L2lrJ6QLyB4cTGkm9OXN5n3hrf0nzpsoex2MKyet5M8fuCMJDuQlB/zldyXJguzTXLy7ce3HYfRALgsaq1a0IsaGN7/m597MVLU7ztzVn2HhWZu+aKM/3sgPxhC92dnXGxgRv0AsBbYQKBgQCeLfdDFkFHsngeNxEYMF30iuHq7kdqtMX/PFQn14eQrSzORjinWXkf+E3XR/deJEAPx8ebpHsbQLfCBW40knAocibVKBwy2du+fNLC1mlPSwBrHgX0YZvSEh2cy07/r7iP7S6wrWYJLfVK2fviAMckgohi0+IxoYxoLEIoXi9HJwKBgCT4CGxCzlXuhuXCXJtqv4xDPisuXXqG1uCkvA6EUNJVvfTrckvtE6Gm8mZtUppSg/e5ytUsgxgiPqVg3in0DqdDUMm8Nywp3PW6QFxCzPsy4kjXSO9Yg+GeNzAvxuv8A3PIEEf0M6d6qoX1FCJzfhfm+99v4D0uCncEAIxB+pAhAoGAXk+jxKEpTPlQ+xr0G0sDSgHgj3WAqVOdVf//YT31e0J1m4tszgLf4DrpFnm3OK8+buo7zkX0jJw6s/kO61OAge0F0EO5BGy2w02jUD4MzhU266cb/8iO4mKgu1LRCEwaK95L31WTp9d6ECYGM4spMVCOxvX0QIlEsPvqei0l3TMCgYAOsoTxrLpEen+Ts/pXGfnGmfoHrjO2ACN+QJG7H47zysrljmNNkwCLTeXNJnTTEU44Aeu7iikkfEl1Cz9+jBRkFs2LBNrcrOTS4JEmWrsJxllEwfCfDC4Zq1iEMRpmsyItIKsLTDKWcJCtpqvPtCin+uxWysd02CASUolPQxOocg==";
	
	
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://ecuat.tk.cn/service/weixinbind/releaseBinding.html";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String return_url = "http://ecuat.tk.cn/service/weixinbind/releaseBinding.html";
	// 请求网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA2";
	
	//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	//支付宝接口版本V1，为支付宝对账提供查询接口（没有SFTP下载文件对账方式，所以高版本接口下单支付，出版本接口查询对账 2018.01.15）
	static String sign_type 	= "MD5";
	// V1接口 请求网关地址
	static String paygateway 	= "https://mapi.alipay.com/gateway.do";

	static String CHARSETV1	    = "GBK";
}