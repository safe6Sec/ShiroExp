package cn.safe6.core;

import cn.safe6.Controller;
import cn.safe6.util.HttpTool;
import javafx.application.Platform;


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


    public BurstJob(String url, String method, Map<String,String> params, List<String> keys) {
        this.url = url;
        this.method = method;
        this.params = params;
        this.keys = keys;
    }

    @Override
    public String call() throws Exception {

        try {
            Controller.logUtil.printInfoLog("开始爆破默认key");
            for (String key : keys) {
                Controller.logUtil.printInfoLog("检测"+key);
                String res = HttpTool.getHttpReuest(url, "application/x-www-form-urlencoded", "UTF-8");
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
