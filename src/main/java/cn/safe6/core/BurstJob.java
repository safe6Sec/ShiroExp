package cn.safe6.core;

import cn.safe6.Controller;
import cn.safe6.util.HttpClientUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.*;
import javafx.application.Platform;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 *  爆破key
 */

public class BurstJob implements Callable<String> {
    private String url;
    private String method;
    private Map<String,String> params;
    private List<String> keys;
    final private Controller controller = (Controller) ControllersFactory.controllers.get(Controller.class.getSimpleName());
    final private Map<String,Object> paramsContext = ControllersFactory.paramsContext;


    public BurstJob(String url, String method, Map<String,String> params, List<String> keys) {
        this.url = url;
        this.method = method;
        this.params = params;
        this.keys = keys;
    }

    @Override
    public String call() {

        try {
            Controller.logUtil.printInfoLog("开始爆破默认key");
            //错误包长度

            Map<String, Object> header = (Map<String, Object>)paramsContext.get("header");
            if (header==null){
                header = new HashMap<>();
            }
            //在原有cookie后面追加
            header.put("cookie",header.get("cookie")+";"+paramsContext.get("rmeValue")+"=123456");
            int errLen= HttpClientUtil.httpGetRequest(url,header).length();
            for (String key : keys) {
                Controller.logUtil.printInfoLog("检测"+key);
                if(paramsContext.get("method").equals(Constants.METHOD_GET)){
                    header.put("cookie",header.get("cookie")+";"+paramsContext.get("rmeValue")+"=");
                    String data = HttpClientUtil.httpGetRequest(url,header);
                   // if (errLen==data.length())



                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Controller.logUtil.getLog().selectPositionCaret(controller.log.getText().length());
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

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }


}
