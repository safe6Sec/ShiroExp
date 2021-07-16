package cn.safe6.core;

import cn.safe6.Controller;
import cn.safe6.tools.HttpTool;
import cn.safe6.tools.Tools;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Callable;


public class UrlJob implements Callable<String> {
    private String url;
    private String header;
    private String method;
    private String postData;
    private String keys;
    private TextArea log;

    public UrlJob(String url, String method, String header, String postData) {
        this.url = url;
        this.header = header;
        this.method = method;
        this.postData = postData;
    }

    public UrlJob(String url, String method, String keys,TextArea log) {
        this.url = url;
        this.keys = keys;
        this.method = method;
        this.log = log;
    }

    public UrlJob(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getPostData() {
        return postData;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String call() throws Exception {

        //System.out.println("线程:" + this.url + " -> 运行...");

        String res = null;
        int code = 0;
        try {
            //System.out.println(url);

            Platform.runLater(() -> {
                this.log.appendText("开始请求:"+url);
                this.log.appendText("\r\n");
                this.log.appendText("\r\n");
            });
            if (method.equals(Constants.METHOD_GET)) {
                code = HttpTool.getCodeByHttpRequest(url, "UTF-8");
                // System.out.println("状态码："+code);
                if (code == 200) {
                    res = HttpTool.getHttpReuest(url, "application/x-www-form-urlencoded", "UTF-8");
                }
            } else {
                code = HttpTool.getCodeByHttpRequest(url, "UTF-8");
                //System.out.println("状态码："+code);
                if (code == 200) {
                    res = HttpTool.postHttpReuest(url, postData, "UTF-8", "application/x-www-form-urlencoded");
                }
            }
            this.log.appendText("线程:" + this.url + " -> 结束|状态码：" + code + "\r\n");
            this.log.appendText("\r\n");
           // System.out.println("线程:" + this.url + " -> 结束|\r\n 状态码：" + code + "|\r\n响应包：\r\n");
            if (res != null && !"".equals(res)) {
                int length = res.length();
                //System.out.println(this.url + "响应包长度:" + length);
/*                Platform.runLater(() -> {
                    this.log.appendText(this.url + "响应包长度:" + length);
                    this.log.appendText("\r\n");
                    this.log.appendText("\r\n");
                });*/

                // System.out.println(this.url+"内容:"+res);

                if (keys != null && !"".equals(keys.trim()) && res.contains(keys)) {
                    Controller.datas.add(new VulInfo(String.valueOf(Controller.datas.size() + 1), url, String.valueOf(length), "存在"));
                }
                Controller.datas.add(new VulInfo(String.valueOf(Controller.datas.size() + 1), url, String.valueOf(length), ""));

            }

            //System.out.println("result ");
            //System.out.println(res);

            // boolean flag = result.contains(uuid);


        } catch (Exception e) {
            System.out.println(e.getMessage());
            this.log.appendText("线程:" + this.url + "错误:" + e.getMessage() + "\r\n");
            //e.printStackTrace();
            return "0";
        }
        //System.out.println(11111);
        return "1";
    }
}
