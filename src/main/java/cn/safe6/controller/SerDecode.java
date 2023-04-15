package cn.safe6.controller;

import cn.safe6.core.Constants;
import cn.safe6.core.ControllersFactory;
import cn.safe6.payload.JRMPClient;
import cn.safe6.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Map;

public class SerDecode {


    final private Map<String, Object> paramsContext = ControllersFactory.paramsContext;
    @FXML
    public TextField server;
    public TextArea mTextArea27;
    public TextArea mTextArea28;

    @FXML
    public void send(ActionEvent actionEvent) throws Exception {
        Controller controller = (Controller) ControllersFactory.controllers.get("Controller");
        controller.validAllDataAndSetConfig();
        String url = paramsContext.get("url").toString();
        if (paramsContext.get("key")==null){
            Tools.alert("AES key 不能为空", "请输入AES key");
            return;
        }
        String key = paramsContext.get("key").toString();
        String aes = paramsContext.get("AES").toString();
        String method = paramsContext.get("method").toString();
        String rmeValue = paramsContext.get("rmeValue").toString();
        Map<String, Object> params = (Map<String, Object>) paramsContext.get("params");

        byte[] payload = JRMPClient.getPayload(server.getText());

        Map<String, Object> header = ShiroTool.getShiroHeader((Map<String, Object>) paramsContext.get("header"), rmeValue);
        String encryptData;
        if (Constants.AES_GCM.equals(aes)) {
            encryptData = PayloadEncryptTool.AesGcmEncrypt(payload, key);
        } else {
            encryptData = PayloadEncryptTool.AesCbcEncrypt(payload, key);
        }
        header.put("cookie", rmeValue + "=" + encryptData);

        try {
            if (method.equals(Constants.METHOD_GET)) {
                // data = HttpClientUtil.httpGetRequest(url, header);
                 HttpTool.get(url, header);
            } else {
                HttpClientUtil.httpPostRequest(url, header, params);
            }
            Tools.info("发送成功");
        }catch (Exception e){
            Tools.alert("发送失败",e.getMessage());
        }

    }

    public void onButton29Click(ActionEvent actionEvent) {

    }

}
