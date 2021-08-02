package cn.safe6.util;

/**
 * @author safe6Sec
 *
 * 某大佬之前写的不是很好用，小弟直接重构。
 * 后面使用到该工具类的大佬，麻烦留下版权。
 *
 */

// http 请求对象，取自 shack2 的Java反序列化漏洞利用工具V1.7

import cn.safe6.Controller;
import cn.safe6.core.Constants;
import cn.safe6.core.http.Request;
import cn.safe6.core.http.Response;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class HttpTool {
    private static int Timeout = 600;

    private static String DefalutEncoding = "UTF-8";

    public static String httpRequest(String requestUrl, int timeOut, String requestMethod, String contentType, String postString, String encoding) throws Exception {

        if ("".equals(encoding) || encoding == null)
            encoding = DefalutEncoding;
        URLConnection httpUrlConn = null;
        HttpsURLConnection hsc = null;
        HttpURLConnection hc = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(requestUrl);
            if (requestUrl.startsWith("https")) {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                TrustManager[] tm = { new MyCERT() };
                sslContext.init(null, tm, new SecureRandom());

                SSLSocketFactory ssf = sslContext.getSocketFactory();

                //代理
                Proxy proxy = (Proxy) Controller.settingInfo.get("proxy");

                if (proxy != null) {
                    hsc = (HttpsURLConnection)url.openConnection(proxy);
                } else {
                    hsc = (HttpsURLConnection)url.openConnection();
                }
                hsc.setSSLSocketFactory(ssf);
                hsc.setHostnameVerifier(allHostsValid);
                httpUrlConn = hsc;
            } else {
                Proxy proxy = (Proxy) Controller.settingInfo.get("proxy");
                if (proxy != null) {
                    hc = (HttpURLConnection)url.openConnection(proxy);
                } else {
                    hc = (HttpURLConnection)url.openConnection();
                }
                hc.setRequestMethod(requestMethod);
                //禁止302 跳转
                hc.setInstanceFollowRedirects(false);
                System.out.println(hc.getRequestProperties());
                httpUrlConn = hc;
            }

            httpUrlConn.setConnectTimeout(timeOut);
            httpUrlConn.setReadTimeout(timeOut);
            if (contentType != null && !"".equals(contentType))
                httpUrlConn.setRequestProperty("Content-Type", contentType);


            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
            httpUrlConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            httpUrlConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpUrlConn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
            httpUrlConn.setRequestProperty("Connection","close");

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);

            httpUrlConn.connect();

            if (null != postString && !"".equals(postString)) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(postString.getBytes(encoding));
                outputStream.flush();
                outputStream.close();
            }
            inputStream = httpUrlConn.getInputStream();
            String result = readString(inputStream, encoding);
            return result;
        } catch (IOException ie) {
            System.out.println(ie);

            if (hsc != null)
                return readString(hsc.getErrorStream(), encoding);
            if (hc != null)
                return readString(hc.getErrorStream(), encoding);
            return "";
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        } finally {
            if (hsc != null)
                hsc.disconnect();
            if (hc != null)
                hc.disconnect();
        }
    }

    public static HostnameVerifier allHostsValid = (hostname, session) -> true;

    public static String readString(InputStream inputStream, String encoding) throws IOException {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        try {
            bis = new BufferedInputStream(inputStream);
            baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] arr = new byte[1];
            while ((len = bis.read(arr)) != -1) {
                baos.write(arr, 0, len);
            }


        } catch (IOException e) {

        } finally {
            if (baos != null) {
                baos.flush();
                baos.close();
            }
            if (bis != null)
                bis.close();
            if (inputStream != null)
                inputStream.close();
            return baos.toString(encoding);
        }

    }

    public static String httpRequestAddHeader(String requestUrl, int timeOut, String requestMethod, String contentType, String postString, String encoding, Map<String, Object> headers) throws Exception {
        if ("".equals(encoding) || encoding == null)
            encoding = DefalutEncoding;
        URLConnection httpUrlConn = null;
        HttpsURLConnection hsc = null;
        HttpURLConnection hc = null;
        InputStream inputStream = null;

        String res = null;

        try {
            URL url = new URL(requestUrl);
            if (requestUrl.startsWith("https")) {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                TrustManager[] tm = { new MyCERT() };
                sslContext.init(null, tm, new SecureRandom());

                SSLSocketFactory ssf = sslContext.getSocketFactory();
                //代理
                Proxy proxy = (Proxy) Controller.settingInfo.get("proxy");

                if (proxy != null) {
                    hsc = (HttpsURLConnection)url.openConnection(proxy);
                } else {
                    hsc = (HttpsURLConnection)url.openConnection();
                }
                hsc.setSSLSocketFactory(ssf);
                hsc.setHostnameVerifier(allHostsValid);
                httpUrlConn = hsc;
            } else {

                Proxy proxy = (Proxy) Controller.settingInfo.get("proxy");
                if (proxy != null) {
                    hc = (HttpURLConnection)url.openConnection(proxy);
                } else {
                    hc = (HttpURLConnection)url.openConnection();
                }

                hc.setRequestMethod(requestMethod);
                hc.setInstanceFollowRedirects(false);
                httpUrlConn = hc;
            }
            httpUrlConn.setConnectTimeout(timeOut);
            httpUrlConn.setReadTimeout(timeOut);
            if (contentType != null && !"".equals(contentType)) {
                httpUrlConn.setRequestProperty("Content-Type", contentType);
            }

            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");

            //httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
            //httpUrlConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            //httpUrlConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            //httpUrlConn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
            //httpUrlConn.setRequestProperty("Connection","close");

            if (headers != null)
                for (String key : headers.keySet()) {
                    String val = headers.get(key).toString();
                    httpUrlConn.addRequestProperty(key, val);
                }
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            // 建立实际的连接
            httpUrlConn.connect();

            if (null != postString && !"".equals(postString)) {

                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(postString.getBytes(encoding));
                outputStream.close();
            }

            inputStream = httpUrlConn.getInputStream();
            //System.out.println(httpUrlConn.getHeaderFields().toString());

            String result = readString(inputStream, encoding);

            if (hsc != null) {
                hsc.disconnect();
            }

            if (hc != null) {
                hc.disconnect();
            }
            return result;

        } catch (Exception e) {
            if (hsc != null) {
                hsc.disconnect();
            }

            if (hc != null) {
                hc.disconnect();
            }
            throw e;
        }

    }

    public static Response httpRequestAddHeader1(String requestUrl, int timeOut, String requestMethod, String postString, String encoding, Map<String, Object> headers) throws Exception {
        if ("".equals(encoding) || encoding == null)
            encoding = DefalutEncoding;
        URLConnection httpUrlConn = null;
        HttpsURLConnection hsc = null;
        HttpURLConnection hc = null;
        InputStream inputStream = null;

        String res = null;
        Response response = new Response();

        try {
            URL url = new URL(requestUrl);
            if (requestUrl.startsWith("https")) {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                TrustManager[] tm = { new MyCERT() };
                sslContext.init(null, tm, new SecureRandom());

                SSLSocketFactory ssf = sslContext.getSocketFactory();
                //代理
                Proxy proxy = (Proxy) Controller.settingInfo.get("proxy");

                if (proxy != null) {
                    hsc = (HttpsURLConnection)url.openConnection(proxy);
                } else {
                    hsc = (HttpsURLConnection)url.openConnection();
                }
                hsc.setSSLSocketFactory(ssf);
                hsc.setHostnameVerifier(allHostsValid);
                httpUrlConn = hsc;
            } else {

                Proxy proxy = (Proxy) Controller.settingInfo.get("proxy");
                if (proxy != null) {
                    hc = (HttpURLConnection)url.openConnection(proxy);
                } else {
                    hc = (HttpURLConnection)url.openConnection();
                }

                hc.setRequestMethod(requestMethod);
                hc.setInstanceFollowRedirects(false);
                httpUrlConn = hc;
            }
            httpUrlConn.setConnectTimeout(timeOut);
            httpUrlConn.setReadTimeout(timeOut);

            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");

            //httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
            //httpUrlConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            //httpUrlConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            //httpUrlConn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
            //httpUrlConn.setRequestProperty("Connection","close");

            if (headers != null)
                for (String key : headers.keySet()) {
                    String val = headers.get(key).toString();
                    httpUrlConn.addRequestProperty(key, val);
                }
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            // 建立实际的连接
            httpUrlConn.connect();

            if (null != postString && !"".equals(postString)) {

                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(postString.getBytes(encoding));
                outputStream.close();
            }

            inputStream = httpUrlConn.getInputStream();
            response.setHeader(httpUrlConn.getHeaderFields());
            //System.out.println(httpUrlConn.getHeaderFields().toString());

            String result = readString(inputStream, encoding);
            response.setData(result);

            if (hsc != null) {
                hsc.disconnect();
                response.setCode(hsc.getResponseCode());
            }

            if (hc != null) {
                hc.disconnect();
                response.setCode(hc.getResponseCode());
            }
            return response;

        } catch (Exception e) {
            if (hsc != null) {
                hsc.disconnect();
            }

            if (hc != null) {
                hc.disconnect();
            }
            throw e;
        }

    }



    public static int codeByHttpRequest(String requestUrl, int timeOut, String requestMethod, String contentType, String postString, String encoding) throws Exception {
        if ("".equals(encoding) || encoding == null)
            encoding = DefalutEncoding;
        URLConnection httpUrlConn = null;
        HttpsURLConnection hsc = null;
        HttpURLConnection hc = null;
        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            URL url = new URL(requestUrl);
            if (requestUrl.startsWith("https")) {
                SSLContext sslContext = SSLContext.getInstance("SSL");
                TrustManager[] tm = { new MyCERT() };
                sslContext.init(null, tm, new SecureRandom());
                SSLSocketFactory ssf = sslContext.getSocketFactory();
                hsc = (HttpsURLConnection)url.openConnection();
                hsc.setSSLSocketFactory(ssf);
                hsc.setHostnameVerifier(allHostsValid);
                httpUrlConn = hsc;
            } else {
                hc = (HttpURLConnection)url.openConnection();
                hc.setRequestMethod(requestMethod);
                httpUrlConn = hc;
            }
            httpUrlConn.setReadTimeout(timeOut);
            if (contentType != null && !"".equals(contentType))
                httpUrlConn.setRequestProperty("Content-Type", contentType);
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Baiduspider/2.0; +http://www.baidu.com/search/spider.html)");
            httpUrlConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            httpUrlConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpUrlConn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
            httpUrlConn.setRequestProperty("Connection","close");

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.connect();
            if (null != postString && !"".equals(postString)) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(postString.getBytes(encoding));
                outputStream.close();
            }
            if (hsc != null)
                return hsc.getResponseCode();
            if (hc != null)
                return hc.getResponseCode();
            return 0;
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            if (br != null)
                br.close();
            if (isr != null)
                isr.close();
            if (inputStream != null)
                inputStream.close();
            if (hsc != null)
                hsc.disconnect();
            if (hc != null)
                hc.disconnect();
        }
    }

    public static String httpReuest(String requestUrl, String method, String contentType, String postString, String encoding) throws Exception {
        return httpRequest(requestUrl, Timeout, method, contentType, postString, encoding);
    }

    public static String get(String url,String encoding) throws Exception {
        return httpRequest(url, Timeout, Constants.METHOD_GET, "application/x-www-form-urlencoded", "", encoding);
    }

    public static String get(String url) throws Exception {
        return httpRequest(url, Timeout, Constants.METHOD_GET, "application/x-www-form-urlencoded", "", DefalutEncoding);
    }

    public static Response get1(String url) throws Exception {
        return httpRequestAddHeader1(url, Timeout, Constants.METHOD_GET, null, DefalutEncoding,null);
    }

    public static Response get1(String url, Map<String, Object> headers) throws Exception {
        return httpRequestAddHeader1(url, Timeout, Constants.METHOD_GET, null, DefalutEncoding,headers);
    }

    public static String get(String url, String postString, String encoding, Map<String, Object> headers, String contentType) throws Exception {
        return httpRequestAddHeader(url, Timeout, Constants.METHOD_GET, contentType, postString, encoding, headers);
    }

    public static String get(String url, String encoding, Map<String, Object> headers) throws Exception {
        return httpRequestAddHeader(url, Timeout, Constants.METHOD_GET, "application/x-www-form-urlencoded", "", encoding, headers);
    }

    public static String get(String url, Map<String, Object> headers) throws Exception {
        return httpRequestAddHeader(url, Timeout, Constants.METHOD_GET, "application/x-www-form-urlencoded", "", DefalutEncoding, headers);
    }

    public static String post(String url,String postData, Map<String, Object> headers) throws Exception {
        return httpRequestAddHeader(url, Timeout, Constants.METHOD_POST, null, postData, DefalutEncoding, headers);
    }



    public static String postHttpReuest(String requestUrl, int timeOut, String contentType, String postString, String encoding) throws Exception {
        return httpRequest(requestUrl, timeOut, "POST", contentType, postString, encoding);
    }

    public static String postHttpReuest(String requestUrl, String postString, String encoding, Map<String, Object> headers, String contentType) throws Exception {
        return httpRequestAddHeader(requestUrl, Timeout, "POST", contentType, postString, encoding, headers);
    }


    public static String postHttpReuest(String requestUrl, String contentType, String postString, String encoding) throws Exception {
        return httpRequest(requestUrl, Timeout, "POST", contentType, postString, encoding);
    }

    public static String postHttpReuest(String requestUrl, int timeOut, String postString, String encoding) throws Exception {
        return httpRequest(requestUrl, timeOut, "POST", "application/x-www-form-urlencoded", postString, encoding);
    }

    public static String postHttpReuest(String requestUrl, String postString, String encoding) throws Exception {
        return httpRequest(requestUrl, Timeout, "POST", "application/x-www-form-urlencoded", postString, encoding);
    }



    public static String postHttpReuestByXML(String requestUrl, int timeOut, String postString, String encoding) throws Exception {
        return httpRequest(requestUrl, timeOut, "POST", "text/xml", postString, encoding);
    }

    public static String postHttpReuestByXML(String requestUrl, String postString, String encoding) throws Exception {
        return httpRequest(requestUrl, Timeout, "POST", "text/xml", postString, encoding);
    }

    public static String postHttpReuestByXMLAddHeader(String requestUrl, String postString, String encoding, Map<String, Object> headers) throws Exception {
        return httpRequestAddHeader(requestUrl, Timeout, "POST", "text/xml", postString, encoding, headers);
    }

    public static int codeByHttpRequest(String requestUrl, String method, String contentType, String postString, String encoding) throws Exception {
        return codeByHttpRequest(requestUrl, Timeout, method, contentType, postString, encoding);
    }

    public static int getCodeByHttpRequest(String requestUrl, String encoding) throws Exception {
        return codeByHttpRequest(requestUrl, "GET", null, "", encoding);
    }

    public static int getCodeByHttpRequest(String requestUrl, int timeout, String encoding) throws Exception {
        return codeByHttpRequest(requestUrl, timeout, "GET", null, "", encoding);
    }
    public static String getHttpReuest(String requestUrl, String contentType, String encoding) throws Exception {
        return httpRequest(requestUrl, Timeout, "GET", contentType, "", encoding);
    }


    public static int postCodeByHttpRequest(String requestUrl, String contentType, String postString, String encoding) throws Exception {
        return codeByHttpRequest(requestUrl, Timeout, "POST", contentType, postString, encoding);
    }

    public static int postCodeByHttpRequestWithNoContenType(String requestUrl, String postString, String encoding) throws Exception {
        return codeByHttpRequest(requestUrl, Timeout, "POST", null, postString, encoding);
    }

    public static int postCodeByHttpRequest(String requestUrl, String encoding) throws Exception {
        return codeByHttpRequest(requestUrl, Timeout, "POST", null, null, encoding);
    }

    public static int postCodeByHttpRequest(String requestUrl, String postString, String encoding) throws Exception {
        return codeByHttpRequest(requestUrl, Timeout, "POST", "application/x-www-form-urlencoded", postString, encoding);
    }

    public static int postCodeByHttpRequestXML(String requestUrl, String postString, String encoding) throws Exception {
        return codeByHttpRequest(requestUrl, Timeout, "POST", "text/xml", postString, encoding);
    }

    public static boolean downloadFile(String downURL, File file) throws Exception {
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bin = null;
        OutputStream out = null;
        try {
            URL url = new URL(downURL);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            bin = new BufferedInputStream(httpURLConnection.getInputStream());
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (bin != null)
                bin.close();
            if (out != null) {
                out.flush();
                out.close();
            }
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return true;
    }

    public static boolean downloadFile(String downURL, String path) throws Exception {
        return downloadFile(downURL, new File(path));
    }


    /**
     *  改自feihong大佬代码
     * @param body
     * @return
     * @throws Exception
     */
    public static Request parseRequest(String body) throws Exception {
        Request request = new Request();
        try{
            body = body.trim();
            String[] parts = body.split("\r\n\r\n|\r\r|\n\n");
            if(parts.length < 2){
                return null;
            }

            Map<String, Object> ps = new HashMap<>();
            String[] params = parts[1].split("&");
            for(String temp : params){
                String[] temps = temp.trim().split("=");
                ps.put(temps[0],temps[1]);
            }
            request.setParams(ps);
            request.setParamsStr(parts[1]);

            //请求头部
            String[] lines = parts[0].split("\r\n|\r|\n");
            //请求起始行
            String requestLine = lines[0].trim();
            parts = requestLine.split("\\s+");
            String requestMethod = parts[0];
            System.out.println(requestMethod);
            request.setRequestMethod(requestMethod);

            String requestURI = parts[1];
            System.out.println(requestURI);
            //request.setRequestUrl(requestURI);

            Map<String, Object> headers = new HashMap<>();
            for(int i = 1; i < lines.length; i++){
                String[] pair = lines[i].trim().split(":",2);
                headers.put(pair[0].trim(), pair[1].trim());
            }
            request.setHeader(headers);

            if(headers.get("Host") != null){
                //完整请求包，否则直接返回，从url框取
                String url =  "http://"+headers.get("Host").toString().trim() + requestURI;
                request.setRequestUrl(url);
            }

            String cookie = headers.get("Cookie").toString();
            if(cookie != null){
                Map<String, String> ck = new HashMap<>();
                String[] cookies = cookie.trim().split(";");
                for(String temp : cookies){
                    String[] temps = temp.trim().split("=");
                    ck.put(temps[0],temps[1]);
                }
                request.setCookies(ck);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new Exception();
        }


        return request;
    }

}