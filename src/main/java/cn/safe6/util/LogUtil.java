package cn.safe6.util;

import javafx.application.Platform;
import javafx.scene.control.TextArea;


/**
 * 日志类
 *
 */
public class LogUtil {

    private TextArea log;

    public LogUtil(TextArea log) {
        this.log = log;
    }

    public void printAbortedLog(String text){
        Platform.runLater(() -> {
            log.appendText("[-] "+text+"\r\n");
            //自动定位到最后
            log.selectPositionCaret(log.getText().length());
        });
    }

    public void printSucceedLog(String text){
        Platform.runLater(() -> {
            log.appendText("[+] "+text+"\r\n");
            log.selectPositionCaret(log.getText().length());
        });
    }

    public void printInfoLog(String text){
        Platform.runLater(() -> {
            log.appendText("[*] "+text+"\r\n");
            log.selectPositionCaret(log.getText().length());
            //log.setWrapText();
        });

    }

    public TextArea getLog() {
        return log;
    }
}
