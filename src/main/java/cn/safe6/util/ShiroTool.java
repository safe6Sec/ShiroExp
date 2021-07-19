package cn.safe6.util;

import cn.safe6.core.Constants;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ShiroTool {


    /**
     * 探测是否是shiro
     * @param url
     * @param method
     * @param params
     * @param rememberMe
     * @return
     */
    public static boolean shiroDetect(String url, String method,Map<String,Object> header, Map<String,Object> params, String rememberMe){
        CloseableHttpResponse closeableHttpResponse;

        if (method.equals(Constants.METHOD_GET)){
            closeableHttpResponse= HttpClientUtil.httpGetRequest3(url, header);
        }else {
            closeableHttpResponse = HttpClientUtil.httpPostRequest2(url,header,params);
        }

        assert closeableHttpResponse != null;
        String text = closeableHttpResponse.getHeaders("Set-Cookie").toString();
        return text.contains(rememberMe);
    }
}
