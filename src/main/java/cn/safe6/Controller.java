package cn.safe6;

import cn.safe6.core.*;
import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.Hashing;
import cn.safe6.tools.HttpTool;
import cn.safe6.tools.Tools;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// JavaFX图形化界面的控制类
public class Controller {

    @FXML
    public TextField keys;

    @FXML
    public DatePicker startTime;

    @FXML
    public DatePicker endTime;

    @FXML
    public TabPane tab;
    @FXML
    public ToggleButton scan;


    @FXML
    private Label tool_name;
    @FXML
    private Label proxyStatusLabel;
    @FXML
    private Label author;
    @FXML
    private ChoiceBox tpVer;
    @FXML
    private ChoiceBox thread;
    @FXML
    private ChoiceBox fofa_size;
    @FXML
    private Text time;
    @FXML
    public  TextArea log;
    @FXML
    private TextArea fofa_result_info;

    @FXML
    private TextField fofa_info;

    @FXML
    private TableView<VulInfo> table_view;
    @FXML
    private TableColumn<VulInfo, String> id;
    @FXML
    private TableColumn<VulInfo, String> url;
    @FXML
    private TableColumn<VulInfo, String> isVul;
    @FXML
    public TableColumn<VulInfo, String> length;

    public static final ObservableList<VulInfo> datas = FXCollections.observableArrayList();

    ExecutorService pool = null;

    @FXML
    private TextField target;


    @FXML
    private MenuItem proxySetupBtn;


    @FXML
    private MenuItem fofa_setting;

    // 设置相关信息保存
    public static Map<String, Object> settingInfo = new HashMap();
    // fofa 结果
    public static HashSet<String> fofa_result = new HashSet<String>();


    // 监听菜单关于事件
    @FXML
    public void about() {
        Alert alert = new Alert(AlertType.NONE);

        // 点 x 退出
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

        ButtonType confirm = new ButtonType("确认");
        dialogPane.getButtonTypes().setAll(confirm);
        alert.setDialogPane(dialogPane);

        alert.showAndWait();


    }

    // 监听菜单事件
    private void initToolbar() {
        //代理 设置
        this.proxySetupBtn.setOnAction((event) -> {
            Alert inputDialog = new Alert(AlertType.NONE);
            Window window = inputDialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest((e) -> {
                window.hide();
            });
            ToggleGroup statusGroup = new ToggleGroup();
            RadioButton enableRadio = new RadioButton("启用");
            RadioButton disableRadio = new RadioButton("禁用");
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
            Label typeLabel = new Label("类型：");
            ComboBox typeCombo = new ComboBox();
            typeCombo.setItems(FXCollections.observableArrayList(new String[]{"HTTP", "SOCKS"}));
            typeCombo.getSelectionModel().select(0);
            Label IPLabel = new Label("IP地址：");
            TextField IPText = new TextField();
            Label PortLabel = new Label("端口：");
            TextField PortText = new TextField();
            Label userNameLabel = new Label("用户名：");
            TextField userNameText = new TextField();
            Label passwordLabel = new Label("密码：");
            TextField passwordText = new TextField();
            Button cancelBtn = new Button("取消");
            Button saveBtn = new Button("保存");


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
                this.proxyStatusLabel.setText("代理服务器配置加载失败。");
                var28.printStackTrace();
            }


            saveBtn.setOnAction((e) -> {
                if (disableRadio.isSelected()) {
                    this.settingInfo.put("proxy", null);
                    this.proxyStatusLabel.setText("");
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

                    this.proxyStatusLabel.setText("代理生效中");
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

        //fofa 设置
        this.fofa_setting.setOnAction((event) -> {
            Alert inputDialog = new Alert(AlertType.NONE);
            Window window = inputDialog.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest((e) -> {
                window.hide();
            });

            HBox statusHbox = new HBox();
            statusHbox.setSpacing(10.0D);

            GridPane proxyGridPane = new GridPane();
            proxyGridPane.setVgap(15.0D);
            proxyGridPane.setPadding(new Insets(20.0D, 20.0D, 0.0D, 10.0D));
            Label fofaEmailLabel = new Label("fofa_email：");
            TextField fofaEmailText = new TextField();
            Label fofaKeyLabel = new Label("fofa_key：");
            TextField fofaKeyText = new TextField();

            Button cancelBtn = new Button("取消");

            Button saveBtn = new Button("保存");
            File file = new File(Constants.FOFAPATH);
            try {
                if (file.exists()) {
                    String values = Tools.read(Constants.FOFAPATH, "UTF-8", false).toString();
                    values = values.substring(1, values.length() - 1);


                    System.out.println(values);
                    String[] EmaliKey = values.split(":");
                    if (EmaliKey.length == 2) {
                        String email = EmaliKey[0];
                        String key = EmaliKey[1];
                        fofaEmailText.setText(email);
                        fofaKeyText.setText(key);
                        this.settingInfo.put("fofa_email", email);
                        this.settingInfo.put("fofa_key", key);
                    } else {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("提示");
                        alert.setHeaderText(null);
                        alert.setContentText("fofa 配置错误\n");
                        alert.showAndWait();
                    }
                }


            } catch (Exception var28) {
                this.proxyStatusLabel.setText("fofa配置加载失败。");
                var28.printStackTrace();
            }


            saveBtn.setOnAction((e) -> {
                this.settingInfo.put("fofa_email", fofaEmailText.getText());
                this.settingInfo.put("fofa_key", fofaKeyText.getText());
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                        Tools.write(Constants.FOFAPATH, fofaEmailText.getText() + ":" + fofaKeyText.getText());
                        System.out.println("fofa配置已保存");
                    } else {
                        Tools.write(Constants.FOFAPATH, fofaEmailText.getText() + ":" + fofaKeyText.getText());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                this.proxyStatusLabel.setText("fofa配置已保存");
                inputDialog.getDialogPane().getScene().getWindow().hide();

            });

            cancelBtn.setOnAction((e) -> {
                inputDialog.getDialogPane().getScene().getWindow().hide();
            });
            proxyGridPane.add(statusHbox, 1, 0);
            proxyGridPane.add(fofaEmailLabel, 0, 2);
            proxyGridPane.add(fofaEmailText, 1, 2);
            proxyGridPane.add(fofaKeyLabel, 0, 3);
            proxyGridPane.add(fofaKeyText, 1, 3);
            HBox buttonBox = new HBox();
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.setSpacing(20.0D);
            buttonBox.getChildren().add(cancelBtn);
            buttonBox.getChildren().add(saveBtn);
            GridPane.setColumnSpan(buttonBox, 2);
            proxyGridPane.add(buttonBox, 0, 6);
            inputDialog.getDialogPane().setContent(proxyGridPane);
            inputDialog.showAndWait();
        });
    }


    // 界面显示  一些默认的基本信息，漏洞列表、编码选项、线程、shell、页脚
    public void defaultInformation() {

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfThisMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        this.startTime.setValue(firstDayOfThisMonth);
        this.endTime.setValue(today);

        this.tpVer.setValue(Constants.VER[0]);
        for (String ver : Constants.VER) {
            this.tpVer.getItems().add(ver);
        }

/*        this.encoding.setValue(Constants.ENCODING[0]);
        for (String coding : Constants.ENCODING) {
            this.encoding.getItems().add(coding);
        }*/

        this.thread.setValue(8);
        for (int i = 4; i <= 36; i += 4) {
            this.thread.getItems().add(i);
        }


        this.fofa_size.setValue(100);
        for (int i : Constants.SIZE) {
            this.fofa_size.getItems().add(i);
        }
        // 默认为冰蝎3 的shell
        // this.upload_info.setText(Constants.SHELL);
        // this.upload_info.setWrapText(true);

        // 命令执行
        //this.cmd_info.setText(" ");
        // this.cmd_info.setEditable(false);
        //  this.cmd_info.setWrapText(true);

        // this.upload_msg.setText("默认为 冰蝎3 的shell.jspx , 密码：rebeyond");


        // this.platform.setValue("Linux");
        //  this.platform.getItems().add("Linux");
        //   this.platform.getItems().add("Windows");

        this.time.setText((String.format(this.time.getText(), 0)));

        this.fofa_result_info.setText("\r\n\r\n\r\n\t\t在 设置 -> FOFA 中设置fofa邮箱和key，之后保存（保存后，会在当前目录下生成fofa.conf文件，供以后使用加载）\r\n\r\n" +
                "\t\tFOFA:\t查询\r\n\r\n\t\tCheck:\t一键导入到批量检查中进行漏洞检测\r\n\r\n" +
                "\t\tICON:\t通过输入icon的url，计算hash值，供fofa高级会员查询icon hash");

        // 页脚
        this.tool_name.setText(String.format(this.tool_name.getText(), Constants.NAME, Constants.VERSION));
        this.author.setText(String.format(this.author.getText(), Constants.AUTHOR));

    }

    // 基本信息
    public void basic() {
        this.log.setText(Constants.BASICINFO);
        this.log.setEditable(false);
        this.log.setWrapText(true);

    }

    @FXML
    public void startScan() {
        String url = this.target.getText().trim();
        LocalDate d1 = this.startTime.getValue();
        LocalDate d2 = this.endTime.getValue();
        long startTime = System.currentTimeMillis(); //程序开始记录时间

        if (this.scan.isSelected()) {
            if (!Tools.checkTheURL(url)) {
                Tools.alert("URL检查", "URL格式不符合要求，示例：http://127.0.0.1:7001/");
                this.scan.setSelected(false);
                return;
            }
            this.scan.setText("停   止");

            //tab.getSelectionModel().select(1);

            if (d1 == null || d2 == null || "".equals(d1.toString()) || "".equals(d2.toString())) {
                Platform.runLater(() -> {
                    //用Platform.runLater来运行需要高频调用的方法
                    Tools.alert("日期检查", "日期范围不选，你扫什么？");
                });
                this.scan.setSelected(false);
                return;
            }
            long s = d1.atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long s1 = d2.atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            if (s >= s1) {
                Platform.runLater(() -> {
                    //用Platform.runLater来运行需要高频调用的方法
                    Tools.alert("日期检查", "瞎选日期,也想扫？");
                    this.scan.setSelected(false);
                });
                return;
            }

            //通过字典合成url
            List<String> urls = this.getUrls(this.genLogDict());
            // 获取用户选择的线程池数量， 创建对应容量的线程池。
            pool = Executors.newFixedThreadPool(Integer.parseInt(this.thread.getValue().toString()));


            try {
                // 读取每行的目标
                for (int i = 0; i < urls.size(); i++) {
                    //提交线程
                    pool.submit(new UrlJob(urls.get(i), Constants.METHOD_GET, keys.getText(),log));
                }
                new Thread(
                        () -> {
                            while (true) {
                                long totalTime = System.currentTimeMillis() - startTime;       //总消耗时间 ,毫秒
                                this.time.setText("用时 " + totalTime / 1000 + "s");
                                if (pool.isTerminated()) {
                                    this.scan.setText("开   始");
                                    break;
                                }
                            }
                        }).start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            this.scan.setText("开   始");
            if (pool != null) {
                pool.shutdown();
                try {
                    pool.awaitTermination(5, TimeUnit.MICROSECONDS);
                    pool.shutdownNow();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    // 双击时复制url
    private void copyString(String str) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(str);
        clipboard.setContent(content);
    }

    // 数据映射到图形化界面表格中
    public void initTableView() {


        //datas.addAll(values);


        //String ver = this.tpVer.getValue().toString().trim();


        //映射数据进每列
        this.id.setCellValueFactory(new PropertyValueFactory("id"));
        this.id.setSortable(false);

        this.url.setCellValueFactory(new PropertyValueFactory("target"));
        this.url.setSortable(false);

        this.length.setCellValueFactory(new PropertyValueFactory("length"));

        this.isVul.setCellValueFactory(new PropertyValueFactory("isVul"));

        // 所有项目添加进datas
        this.table_view.setItems(this.datas);

        //双击浏览器打开
        this.table_view.setRowFactory(tv -> {
            TableRow<VulInfo> row = new TableRow<VulInfo>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    VulInfo url = row.getItem();
                    //this.copyString(url.getTarget());
                    try {
                        Tools.browse2(url.getTarget());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

    }


    /**
     * 获取对应版本生成的urls
     *
     * @return
     */
    public List<String> getUrls(List<String> dict) {
        String tg = this.target.getText().trim();
        List<String> urls = new ArrayList<>();
        if (Constants.VER[0].equals(tpVer.getValue().toString())) {
            for (int j = 0; j < Constants.TP5PATH.length; j++) {
                for (String dir : dict) {
                    urls.add(tg + Constants.TP5PATH[j] + dir);
                }
            }
        } else {
            for (int j = 0; j < Constants.TP3PATH.length; j++) {
                for (String dir : dict) {
                    urls.add(tg + Constants.TP3PATH[j] + dir);
                }
            }
        }
        return urls;
    }


    /**
     * 生成日志字典
     * <p>
     * tp3
     * 19_01_01.log
     * 1550651608-19_02_20.log
     * tp5
     * /runtime/log/201808/07.log
     * /runtime/log/201808/07_cli.log
     */
    public List<String> genLogDict() {
        List<String> dict = new ArrayList<>();

        //此处直接转成字符串进行处理，也可以直接掉用方法取年月日
        String start = startTime.getValue().toString();
        String end = endTime.getValue().toString();
        String[] startDate = start.split("-");
        String[] endDate = end.split("-");
        //年
        for (int i = Integer.parseInt(startDate[0]); i <= Integer.parseInt(endDate[0]); i++) {
            //月
            for (int j = Integer.parseInt(startDate[1]); j <= 12; j++) {
                //生成的年月大于结束日期年月直接跳出
                if (Integer.parseInt(i + "" + j) > Integer.parseInt(endDate[0] + endDate[1])) {
                    break;
                }
                //日，所有月份全按照31天算
                for (int k = Integer.parseInt(startDate[2]); k <= 31; k++) {
                    //数字
                    String jj = j + "";
                    String kk = k + "";
                    if (j < 10) {
                        jj = "0" + j;
                    }
                    if (k < 10) {
                        kk = "0" + k;
                    }
                    //生成的年月日大于结束日期年月日直接跳出
                    //System.out.println(i + "" + j + k);
                    if (Integer.parseInt(i + "" + jj + kk) > Integer.parseInt(endDate[0] + endDate[1] + endDate[2])) {
                        break;
                    }
                    if (Constants.VER[0].equals(tpVer.getValue().toString())) {
                        //tp5
                        dict.add(i + jj + "/" + kk + ".log");
                        dict.add(i + jj + "/" + kk + "_cli.log");
                    } else {
                        //tp3
                        String ii = String.valueOf(i);
                        dict.add(ii.substring(ii.length() - 2) + "_" + jj + "_" + kk + ".log");
                    }

                }

            }
        }

        return dict;
    }

    @FXML
    // url批量导入
    public void batch_test() {
        this.datas.clear();
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage); // 文件路径

        //this.file_path.setText(file.toString());

        HashSet<String> values = Tools.read(file.toString(), "UTF-8", true);

        //table_view(values);
    }


    @FXML
    // 导出漏洞存在的url
    public void export() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage s = new Stage();
        File file = fileChooser.showSaveDialog(s);
        if (file == null)
            return;
        if (file.exists()) { //文件已存在，则删除覆盖文件
            file.delete();
        }
        String exportFilePath = file.getAbsolutePath();

        StringBuilder sBuilder = new StringBuilder();
        if (this.datas.size() > 0) {
            for (VulInfo vulInfo : this.datas) {
                if (vulInfo.getIsVul().equals("存在")) {
                    System.out.println(vulInfo.getTarget());
                    sBuilder.append(vulInfo.getTarget() + "\r\n");
                }

            }
        }
        if (Tools.write(exportFilePath, sBuilder.toString())) {
            System.out.println("文件创建成功！");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("导出成功!保存路径:\n" + exportFilePath);

            alert.showAndWait();
        }


    }


    @FXML
    // fofa 搜索
    public void fofa_search() {
        String result = "";
        try {
            int page = (int) this.fofa_size.getValue();

            String fofa_info = this.fofa_info.getText();

            if (fofa_info.length() == 0) {
                fofa_info = "app=\"Solr\"";
            }

            File file = new File(Constants.FOFAPATH);


            if (file.exists()) {
                String values = Tools.read(Constants.FOFAPATH, "UTF-8", false).toString();
                values = values.substring(1, values.length() - 1);
                ;

                System.out.println(values);
                String[] EmaliKey = values.split(":");
                if (EmaliKey.length == 2) {
                    String email = EmaliKey[0];
                    String key = EmaliKey[1];


                    String fResult = Tools.fofaHTTP(email, key, fofa_info, page, fofa_result_info);

                    // 不清楚为啥生成的jar文件，这里json不能解析，也不报错，在IDEA中运行就可以
                    JSONObject object = (JSONObject) JSONObject.parse(fResult);
                    List<String> listStr = object.parseArray(object.getJSONArray("results").toJSONString(), String.class);

                    for (String s : listStr) {
                        s = s.replace("\"", "").replace("\\r\\n", "").replace("\\t", "");
                        String host = s.split(",", 2)[0].replace("[", "");
                        String title = s.split(",", 2)[1].replace("]", "");
                        result += host + "\t\t\t" + title + "\r\n";
                        this.fofa_result.add(host);
                    }

                    this.proxyStatusLabel.setText("fofa查询完成");

                } else {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("提示");
                    alert.setHeaderText(null);
                    alert.setContentText("fofa 配置错误\n");

                    alert.showAndWait();

                    this.proxyStatusLabel.setText("asasdadas配置错误");
                }
            } else {
                this.fofa_result_info.setText("fofa.conf文件没找到！！！！！\r\n");
            }


        } catch (Exception e) {
            e.printStackTrace();
            result = e.getStackTrace().toString();

        }

        this.fofa_result_info.setText(result);


    }

    // fofa icon 计算
    public void fofa_icon() {
        Alert inputDialog = new Alert(AlertType.NONE);
        inputDialog.setTitle("ICON Hash 计算");
        Window window = inputDialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((e) -> {
            window.hide();
        });

        HBox statusHbox = new HBox();
        statusHbox.setSpacing(20.0D);

        GridPane proxyGridPane = new GridPane();
        proxyGridPane.setVgap(15.0D);
        proxyGridPane.setPadding(new Insets(20.0D, 20.0D, 0.0D, 10.0D));
        Label iconUrlLabel = new Label("icon url：");
        TextField iconUrlText = new TextField();
        Label iconHashLabel = new Label("iconHash：");
        TextField iconHashText = new TextField();

        Button iconHash = new Button("iconHash");


        iconHash.setOnAction((e) -> {
            String ste = HttpTool.ImageToBase64ByOnline(iconUrlText.getText());
            int hashcode = Hashing.murmur3_32().hashString(ste.replaceAll("\r", "") + "\n", StandardCharsets.UTF_8).asInt();
            iconHashText.setText("icon_hash=\"" + hashcode + "\"");

        });


        proxyGridPane.add(statusHbox, 1, 0);
        proxyGridPane.add(iconUrlLabel, 0, 2);
        proxyGridPane.add(iconUrlText, 1, 2);
        proxyGridPane.add(iconHashLabel, 0, 3);
        proxyGridPane.add(iconHashText, 1, 3);
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20.0D);
        buttonBox.getChildren().add(iconHash);
        GridPane.setColumnSpan(buttonBox, 2);
        proxyGridPane.add(buttonBox, 0, 5);
        inputDialog.getDialogPane().setContent(proxyGridPane);
        inputDialog.showAndWait();
    }


    // 加载
    public void initialize() {
        try {
            this.initToolbar();
            this.defaultInformation();
            this.basic();
            this.initTableView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
