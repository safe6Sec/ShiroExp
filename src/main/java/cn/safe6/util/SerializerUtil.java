package cn.safe6.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class SerializerUtil {

    /**
     * 把对象序列化为byte
     * @param object
     * @return
     * @throws Exception
     */
    public static byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outObj = new ObjectOutputStream(byteArrayOutputStream);
        outObj.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }
}
