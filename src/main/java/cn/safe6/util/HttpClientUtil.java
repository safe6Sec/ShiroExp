package cn.safe6.util;

import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;


/**
 * @author safe6
 */
public class HttpClientUtil {
	private static PoolingHttpClientConnectionManager cm;
    private static String EMPTY_STR = "none";
    private static String UTF_8 = "UTF-8";
    public static String CURRENT = "";
    public static int timeOut = 10;
    private static SSLConnectionSocketFactory sslsf;

    private static void init() {
        if (cm == null) {
            try {
                SSLContext sslContext = getSSLContext(true, null, null);
                /*SSLContext sslContext = SSLContextBuilder
                        .create()
                        .loadTrustMaterial(new TrustSelfSignedStrategy())
                        .build();*/

                HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

                sslsf = new SSLConnectionSocketFactory(sslContext,
                        allowAllHosts);

            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                throw new RuntimeException(e);
            } catch (CertificateException | IOException e) {
                e.printStackTrace();
            }
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslsf)
                    .build();
/*            final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", sslsf)
                    .build();*/
            cm = new PoolingHttpClientConnectionManager(registry);
            //cm = new PoolingHttpClientConnectionManager();
            // 整个连接池最大连接数
            cm.setMaxTotal(5000);
            // 每路由最大连接数，默认值是2
            cm.setDefaultMaxPerRoute(1000);
        }
    }


    /**
     * 获取SSL上下文对象,用来构建SSL Socket连接
     *
     * @param isDeceive 是否绕过SSL
     * @param creFile 整数文件,isDeceive为true 可传null
     * @param crePwd 整数密码,isDeceive为true 可传null, 空字符为没有密码
     * @return SSL上下文对象
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws CertificateException
     */
    private static SSLContext getSSLContext(boolean isDeceive, File creFile, String crePwd) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {

        SSLContext sslContext = null;

        if (isDeceive) {
            sslContext = SSLContext.getInstance("SSLv3");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[] {x509TrustManager}, null);
        } else {
            if (null != creFile && creFile.length() > 0) {
                if (null != crePwd) {
                    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    keyStore.load(new FileInputStream(creFile), crePwd.toCharArray());
                    sslContext = SSLContexts.custom().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy()).build();
                } else {
                    throw new SSLHandshakeException("整数密码为空");
                }
            }
        }

        return sslContext;

    }

    /**
     * 通过连接池获取HttpClient
     * 
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        init();
        // 配置超时回调机制
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {// 如果已经重试了3次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return true;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
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
        HttpClientBuilder builder = HttpClients
                .custom()
                .setConnectionManager(cm)
                .setSSLSocketFactory(sslsf)
                .setRetryHandler(retryHandler);
        return builder.build();
    }

    /**
     * 
     * @param url
     * @return
     */
    public static String httpGetRequest(String url) {
    	
    	
        HttpGet httpGet = new HttpGet(url);
        
        return getResult(httpGet);
    }

    public static CloseableHttpResponse httpGetRequest3(String url,Map<String, Object> headers) {
        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        try {
            return getHttpClient().execute(httpGet);
        } catch (IOException e) {
           // System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 获取get请求响应
     * @param url
     * @return
     * @throws IOException
     */

    public static CloseableHttpResponse get(String url) {
        HttpGet httpGet = new HttpGet(url);
/*        SocketConfig socketConfig = SocketConfig.custom()
                .setSoKeepAlive(false)
                .setSoLinger(1)
                .setSoReuseAddress(true)
                .setSoTimeout(timeOut*100)
                .setTcpNoDelay(true).build();*/
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeOut*1000).build();
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36" );
        httpGet.setConfig(requestConfig);
        try {
            return getHttpClient().execute(httpGet);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    /**
     * 获取head请求响应
     * @param url
     * @return
     * @throws IOException
     */


    public static CloseableHttpResponse head(String url) {


        HttpHead httpHead = new HttpHead(url);
        httpHead.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36" );
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeOut*1000).build();
        httpHead.setConfig(requestConfig);
        // response.getStatusLine().getStatusCode();
        try {
            return getHttpClient().execute(httpHead);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;

    }
    
    public static String httpGetRequest1(String url,Map<String, Object> headers) {
        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }
    
    public static String httpHeadRequest(String url) {
        HttpHead httpHead = new HttpHead(url);
        return getResult(httpHead);
    }


    public static String httpGetRequest(String url, Map<String, Object> headers)
            throws URISyntaxException {
        //URIBuilder ub = new URIBuilder();
        //ub.setPath(url);

        HttpGet httpGet = new HttpGet(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpGet);
    }

    public static String httpPostRequest(String url) {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }
    
    public static String httpPostRequest1(String url,Map<String, Object> headers) {
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> params) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
        return getResult(httpPost);
    }

    public static String httpPostRequest(String url, Map<String, Object> headers, Map<String, Object> params)
            throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }

        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));

        return getResult(httpPost);
    }

    public static CloseableHttpResponse httpPostRequest2(String url, Map<String, Object> headers, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(url);

        for (Map.Entry<String, Object> param : headers.entrySet()) {
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));
        }


        try {
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));
            return getHttpClient().execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        
        
        for (Map.Entry<String, Object> param : params.entrySet()) {
        	if(param.getKey().equals("avars[1][]")) {
        		pairs.add(new BasicNameValuePair("vars[1][]", String.valueOf(param.getValue())));
        	}else {
        		pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        	}
            
        }

        return pairs;
    }

    /**
     * 处理Http请求
     * 
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request) {
        // CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = getHttpClient();
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
            // response.getStatusLine().getStatusCode();
            
            //响应实例
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                // httpClient.close();
                //System.out.println(result);
                return EntityUtils.toString(entity,"utf-8");
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }finally {
            try {
                assert response != null;
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return EMPTY_STR;
    }

    /**
     * 解析数据
     *
     * @param response
     * @return
     */
    public static String getResult(CloseableHttpResponse response) {
        // CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            // response.getStatusLine().getStatusCode();
            //响应实例
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // long len = entity.getContentLength();// -1 表示长度未知
                String result = EntityUtils.toString(entity,"utf-8");
                response.close();
                // httpClient.close();
                //System.out.println(result);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return EMPTY_STR;
    }


    

}
