package cn.safe6.util;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


/**
 * 日志类
 *
 */
public class LogUtil {

    private TextArea log;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public LogUtil(TextArea log) {
        this.log = log;
    }

    public void printAbortedLog(String text,boolean isEnd){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            log.appendText("[-] "+getNowTime()+"  "+text+"\r\n");
            //自动定位到最后
            if (isEnd){
                log.selectPositionCaret(log.getText().length());
            }

        });
    }

    public void printSucceedLog(String text){
        log.setText("");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            log.appendText("[+] "+getNowTime()+"  "+text+"\r\n");
            log.appendText("---------------------------\r\n");
            log.selectPositionCaret(log.getText().length());
        });
    }

    public void printData(String text){
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            log.setText("");
            log.appendText("\r\n---------------\r\n"+text+"\r\n\r\n");
            log.selectPositionCaret(log.getText().length());
        });
    }

    public void printInfoLog(String text,boolean isEnd){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            log.appendText("[*] "+getNowTime()+"  "+text+"\r\n");
            if (isEnd){
                log.selectPositionCaret(log.getText().length());
            }
        });

    }

    public void printWarningLog(String text,boolean isEnd){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            log.appendText("[!] "+getNowTime()+"  "+text+"\r\n");
            if (isEnd){
                log.selectPositionCaret(log.getText().length());
            }
        });

    }

    public TextArea getLog() {
        return log;
    }


    public String getNowTime(){
        //return sdf.format();
        return LocalDateTime.now().format(formatter);
    }
}
