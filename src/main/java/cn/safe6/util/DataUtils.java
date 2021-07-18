package cn.safe6.util;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class DataUtils {


    //复制到剪切板
    public static void copyStringToClipboard(String str) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(str);
        clipboard.setContent(content);
    }

}
