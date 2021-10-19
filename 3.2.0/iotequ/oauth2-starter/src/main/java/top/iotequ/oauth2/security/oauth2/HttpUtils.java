package top.iotequ.oauth2.security.oauth2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

public class HttpUtils {

	public static String  toString(Object o) {
		if (o==null) return null;
		else return o.toString();
	}
	public static boolean isEmpty(Object o) {
		return (o==null || o.toString().isEmpty());
	}


	public static String getHttpString(HttpResponse response) throws Exception {

		if (response==null) return null;
		HttpEntity entity = response.getEntity();
		if (entity.getContentLength() == 0 || !entity.isStreaming()) {
			return "{\"code\":" + response.getStatusLine().getStatusCode() + "}";
		}
		String line = null;
		StringBuilder entityStringBuilder = new StringBuilder();
		BufferedReader b = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8 * 1024);
		while ((line = b.readLine()) != null) {
			entityStringBuilder.append(line + "\n");
		}
		return entityStringBuilder.toString();

	}	

	/*
	 * Post String
	 * 
	 * @param host
	 * @param headers
	 * @param querys
	 * @param body
	 * @return
	 * @throws Exception 出错抛出异常
	 */
	public static HttpResponse doPost(String host, Map<String, Object> headers,Map<String, Object> querys,String body)    throws Exception {    	

		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, querys));
		if (headers != null)
			for (Map.Entry<String, Object> e : headers.entrySet()) {
				request.addHeader(e.getKey(), toString(e.getValue()));
			}

		if (!isEmpty(body)) {
			request.setEntity(new StringEntity(body, "utf-8"));
		}
		return httpClient.execute(request);
    }

	private static String buildUrl(String host, Map<String, Object> querys) throws UnsupportedEncodingException {
    	StringBuilder sbUrl = new StringBuilder();
    	sbUrl.append(host);
    	if (null != querys) {
    		StringBuilder sbQuery = new StringBuilder();
        	for (Map.Entry<String, Object> query : querys.entrySet()) {
        		if (0 < sbQuery.length()) {
        			sbQuery.append("&");
        		}
        		if (isEmpty(query.getKey()) && !isEmpty(query.getValue())) {
        			sbQuery.append(query.getValue());
                }
        		if (!isEmpty(query.getKey())) {
        			sbQuery.append(query.getKey());
        			if (!isEmpty(query.getValue())) {
        				sbQuery.append("=");
        				sbQuery.append(URLEncoder.encode(toString(query.getValue()), "utf-8"));
        			}        			
                }
        	}
        	if (0 < sbQuery.length()) {
        		sbUrl.append(host.indexOf("?")>=0 ? "&":"?").append(sbQuery);
        	}
        }
    	
    	return sbUrl.toString();
    }
	
	private static HttpClient wrapClient(String host) {
		if (host.toLowerCase().startsWith("https://")) {
			return sslClient();
		} else return HttpClients.createDefault(); 
	}
	
	private static HttpClient sslClient() {        
        try {
            // 在调用SSL之前需要重写验证方法，取消检测SSL
            X509TrustManager trustManager = new X509TrustManager() {
                @Override public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override public void checkClientTrusted(X509Certificate[] xcs, String str) {}
                @Override public void checkServerTrusted(X509Certificate[] xcs, String str) {}
            };
            SSLContext ctx = SSLContext.getInstance(SSLConnectionSocketFactory.TLS);
            ctx.init(null, new TrustManager[] { trustManager }, null);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
            // 创建Registry
            RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(Boolean.TRUE).setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https",socketFactory).build();
            // 创建ConnectionManager，添加Connection配置信息
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            CloseableHttpClient closeableHttpClient = HttpClients.custom().setConnectionManager(connectionManager).setSSLSocketFactory(socketFactory)
                    .setDefaultRequestConfig(requestConfig).build();
            return closeableHttpClient;
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
     }
}