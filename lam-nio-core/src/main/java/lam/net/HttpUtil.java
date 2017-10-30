package lam.net;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年10月23日
* @versio 1.0
*/
public class HttpUtil {
	
	public static void main(String[] args){
		//Create an HttpClient with the ThreadSafeClientConnManager.
		//This connection manager must be used if more than one thread will be using the the HttpClient.
		/*
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(100);
		
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();
		*/
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			//method: Get
			HttpGet request = new HttpGet(URI.create("http://www.baidu.com"));
			int timeout = 1000 * 10;
			//建造者模式
			RequestConfig config = RequestConfig.custom()
					.setConnectionRequestTimeout(timeout)
					.setConnectTimeout(timeout)
					.setSocketTimeout(timeout)
					.build();
			request.setConfig(config);
			try {
				HttpHost target = null;
				final URI requestURI = request.getURI();
				if (requestURI.isAbsolute()) {
					target = URIUtils.extractHost(requestURI);
				}
				HttpContext httpContext = new BasicHttpContext();//null;
				HttpClientContext context = HttpClientContext.adapt(httpContext);
				response = httpClient.execute(target, request, /*httpContext*/context);
				HttpEntity entity = response.getEntity();
				System.out.println(EntityUtils.toString(entity, Consts.UTF_8));//HTTP.UTF_8 Deprecated =>Consts.UTF-8
				EntityUtils.consume(entity);
			} finally {
				//HttpClientUtils.closeQuietly(response);
				response.close();
			}
			
			//method: Post
			HttpPost request0 = new HttpPost(URI.create("http://www.baidu.com"));
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("a", "av"));
			nvps.add(new BasicNameValuePair("b", "bv"));
			//entity即是请求实体的意思，POST请求才有请求实体，GET请求没有请求实体。
			//entity将参数nvps转换成a=av&b=bv的格式
			//将生成http头部信息: Header(name: value)的实现对象BasicHeader：
			//Content-Type: application/x-www-form-urlencoded; charset=UTF-8 
			UrlEncodedFormEntity entity0 = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
			request0.setEntity(entity0);
			
			CloseableHttpResponse response0 = null;
			try {
				response0 = httpClient.execute(request0);
				HttpEntity entity1 = response.getEntity();
				System.out.println(EntityUtils.toString(entity1, Consts.UTF_8));
				EntityUtils.consume(entity1);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//response0.close();
				HttpClientUtils.closeQuietly(response0);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//HttpClientUtils.closeQuietly(httpClient);
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
