package cn.safe6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Parent root = FXMLLoader.load(classLoader.getResource("sample.fxml"));

            primaryStage.setTitle(" Safe6Sec ShiroExp v1.3          [build:20220625]");
            //primaryStage.setAlwaysOnTop(true);
           // primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root));
            // 退出程序的时候，子线程也一起退出
            primaryStage.setOnCloseRequest(event -> System.exit(0));
            //设置窗口不可拉伸
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}

