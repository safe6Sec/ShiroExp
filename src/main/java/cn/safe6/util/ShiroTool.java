package cn.safe6.util;

import cn.safe6.core.Constants;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
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
        CloseableHttpResponse closeableHttpResponse1;

        if (header.get("cookie")!=null){
            if (!header.get("cookie").toString().contains(rememberMe)){
                header.put("cookie",header.get("cookie")+";"+rememberMe+"=123456");
            }
        }else {
            header.put("cookie",rememberMe+"=123456");
        }

        //此处探测，采用发送两个包，解决某些带了rememberMe无回显情况
        if (method.equals(Constants.METHOD_GET)){

            closeableHttpResponse= HttpClientUtil.httpGetRequest3(url, header);
            header.remove("cookie");
            closeableHttpResponse1= HttpClientUtil.httpGetRequest3(url, header);
        }else {
            closeableHttpResponse = HttpClientUtil.httpPostRequest2(url,header,params);
            header.remove("cookie");
            closeableHttpResponse1 = HttpClientUtil.httpPostRequest2(url,header,params);
        }

        if (closeableHttpResponse!=null){
            String text = Arrays.toString(closeableHttpResponse.getHeaders("Set-Cookie"));

            return text.contains(rememberMe);
        }else if (closeableHttpResponse1!=null){
            String text1 = Arrays.toString(closeableHttpResponse.getHeaders("Set-Cookie"));
            return text1.contains(rememberMe);
        }
        return false;
    }


    /**
     * 获取SimplePrincipalCollection探测payload，如果秘钥正确，解密成功不会返现deleteMe
     * @return
     * @throws Exception
     */
    public static byte[] getDetectText() throws Exception{
        InputStream inputStream = ShiroTool.class.getClassLoader().getResourceAsStream("detect.txt");
        // 读取字节流还是用 ByteArrayOutputStream
        // 将数据读到 byteArrayOutputStream 中
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int n;
        while ((n=inputStream.read()) != -1){
            byteArrayOutputStream.write(n);
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return bytes;
    }
}
