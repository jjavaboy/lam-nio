package lam.net;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			HttpGet request = new HttpGet("http://www.baidu.com");
			try {
				response = httpClient.execute(request);
				HttpEntity entity = response.getEntity();
				System.out.println(EntityUtils.toString(entity, Charset.forName("utf-8")));
				EntityUtils.consume(entity);
			} finally {
				//HttpClientUtils.closeQuietly(response);
				response.close();
			}
			
			HttpPost request0 = new HttpPost("http://www.baidu.com");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("a", "av"));
			nvps.add(new BasicNameValuePair("b", "bv"));
			UrlEncodedFormEntity entity0 = new UrlEncodedFormEntity(nvps);
			request0.setEntity(entity0);
			
			CloseableHttpResponse response0 = null;
			try {
				response0 = httpClient.execute(request0);
				HttpEntity entity1 = response.getEntity();
				System.out.println(EntityUtils.toString(entity1, Charset.forName("utf-8")));
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
