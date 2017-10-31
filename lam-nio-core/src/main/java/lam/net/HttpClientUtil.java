package lam.net;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.AIMDBackoffManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultBackoffStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.DefaultServiceUnavailableRetryStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import lam.util.Strings;

/**
* <p>
* apache httpclient util class
* </p>
* @author linanmiao
* @date 2017年10月27日
* @version 1.0
*/
public class HttpClientUtil {

    static final int timeOut = 10 * 1000;

    private static volatile CloseableHttpClient httpClient = null;

    private final static Object syncLock = new Object();

    private static void config(HttpRequestBase httpRequestBase) {
        // 设置Header等
        // httpRequestBase.setHeader("User-Agent", "Mozilla/5.0");
        // httpRequestBase
        // .setHeader("Accept",
        // "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        // httpRequestBase.setHeader("Accept-Language",
        // "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");// "en-US,en;q=0.5");
        // httpRequestBase.setHeader("Accept-Charset",
        // "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7");

        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeOut)
                .setConnectTimeout(timeOut)
                .setSocketTimeout(timeOut)
                .build();
        httpRequestBase.setConfig(requestConfig);
    }

    /**
     * 获取HttpClient对象
     * 
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient getHttpClient(String url) {
        if (httpClient == null) {
            synchronized (syncLock) {
                if (httpClient == null) {
                	URI uri = URI.create(url); //new URI(url);
                    httpClient = createHttpClient(200, 40, 100, uri);
                }
            }
        }
        return httpClient;
    }

    /**
     * 创建HttpClient对象
     * 
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, int maxRoute, URI uri) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加
        connManager.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        connManager.setDefaultMaxPerRoute(maxPerRoute);
        //HttpHost httpHost = new HttpHost(hostname, port);
        HttpHost target = null;
        if (uri.isAbsolute()) {
        	target = URIUtils.extractHost(uri);
        }
        // 将目标主机的最大连接数增加
        connManager.setMaxPerRoute(new HttpRoute(target), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {

            public boolean retryRequest(IOException exception,int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                //失败重试
                .setRetryHandler(httpRequestRetryHandler)
                //重定向
                .setRedirectStrategy(DefaultRedirectStrategy.INSTANCE)
                //返回状态503，服务不可用，重试1次，间隔1秒。
                .setServiceUnavailableRetryStrategy(new DefaultServiceUnavailableRetryStrategy(1, 1000))
                //连接降级处理管理类 A (A和B一起使用)
                //.setBackoffManager(new AIMDBackoffManager(final ConnPoolControl<HttpRoute> connPerRoute))
                .setBackoffManager(new AIMDBackoffManager(connManager))
                //连接降级策略 B (A和B一起使用)
                .setConnectionBackoffStrategy(new DefaultBackoffStrategy())
                //默认是false，表示connManager不是共享的，只是专用于创建的该httpClient，那么httpClient被close掉时，connManager也会被close掉。
                .setConnectionManagerShared(Boolean.FALSE.booleanValue())
                //逐出过期/空闲连接，清除过期/空闲连接，10秒执行一次清理任务（清除过期连接，清除大于10秒没使用的空闲连接）
                .evictExpiredConnections()
                .evictIdleConnections(10L, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }

    private static void setPostParams(HttpPost httpost, Map<String, Object> params) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, Strings.trimToEmpty(params.get(key))));
        }
        httpost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
    }

    /**
     * GET请求URL获取内容
     * 
     * @param url
     * @return
     * @author SHANHY
     * @throws IOException 
     * @create 2015年12月18日
     */
    public static String post(String url, Map<String, Object> params) throws IOException {
        HttpPost httppost = new HttpPost(url);
        config(httppost);
        setPostParams(httppost, params);
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient(url).execute(httppost,
                    HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, Consts.UTF_8);
            EntityUtils.consume(entity);
            return result;
        } catch (Exception e) {
//          e.printStackTrace();
            throw e;
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * GET请求URL获取内容
     * 
     * @param url
     * @return
     * @author SHANHY
     * @create 2015年12月18日
     */
    public static String get(String url) {
        HttpGet httpget = new HttpGet(url);
        config(httpget);
        CloseableHttpResponse response = null;
        URI uri = httpget.getURI();
        HttpHost httpHost = null;
        if (uri.isAbsolute()) {
        	httpHost = URIUtils.extractHost(uri);
        }
        HttpClientContext context = HttpClientContext.create();
        try {
            response = getHttpClient(url).execute(httpHost, httpget, context);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, Consts.UTF_8);
            EntityUtils.consume(entity);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // URL列表数组
        String[] urisToGet = {
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",

                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497",
                "http://blog.csdn.net/catoop/article/details/38849497" };

        long start = System.currentTimeMillis();
        try {
            int pagecount = urisToGet.length;
            ExecutorService executors = Executors.newFixedThreadPool(pagecount);
            CountDownLatch countDownLatch = new CountDownLatch(pagecount);
            for (int i = 0; i < pagecount; i++) {
                HttpGet httpget = new HttpGet(URI.create(urisToGet[i]));
                config(httpget);
                // 启动线程抓取
                executors.execute(new GetRunnable(urisToGet[i], countDownLatch));
            }
            countDownLatch.await();
            executors.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("线程" + Thread.currentThread().getName() + ","
                    + System.currentTimeMillis() + ", 所有线程已完成，开始进入下一步！");
        }

        long end = System.currentTimeMillis();
        System.out.println("consume -> " + (end - start));
    }

    static class GetRunnable implements Runnable {
        private final CountDownLatch countDownLatch;
        private String url;

        public GetRunnable(String url, final CountDownLatch countDownLatch) {
            this.url = url;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                System.out.println(HttpClientUtil.get(url));
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
