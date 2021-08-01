package javax.servlet.jsp;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Writer;

public class PageContext extends ClassLoader {
    ServletRequest request;
    ServletResponse response;


    public Writer getOut(){
        return null;
    }

    public PageContext(ServletRequest request, ServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public PageContext(){

    }

    public void setRequest(ServletRequest request) {
        this.request = request;
    }

    public void setResponse(ServletResponse response){
        this.response = response;
    }

    public HttpSession getSession() {
        HttpServletRequest test = (HttpServletRequest) this.request;
        return test.getSession();
    }

    public ServletRequest getRequest() {
        return  this.request;
    }

    public ServletResponse getResponse() {
        return this.response;
    }

    public Class test(byte[] b){
        return super.defineClass(b, 0, b.length);
    }

}
