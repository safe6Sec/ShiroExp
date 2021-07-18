package cn.safe6.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class Serializer {
    // 对传进来的 Object 进行转换，转成byte
    // 首先将对象进行序列化
    public static byte[] serialize(Object object) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outObj = new ObjectOutputStream(byteArrayOutputStream);
        outObj.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }
}
