package cn.safe6.core.http;

import java.util.Map;

public class Request {
    private String requestUrl;
    private String requestMethod;
    private Map<String,Object> header;
    private Map<String,String> cookies;
    private Map<String,Object> params;
    private String paramsStr;

    public Request() {
    }

    public Request(String requestUrl, String requestMethod, Map<String, Object> header, Map<String,Object> params) {
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.header = header;
        this.params = params;
    }

    public Request(String requestUrl, String requestMethod, Map<String, Object> header, String params) {
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.header = header;
        this.paramsStr = params;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Map<String,Object> getParams() {
        return params;
    }

    public void setParams(Map<String,Object> params) {
        this.params = params;
    }

    public String getParamsStr() {
        return paramsStr;
    }

    public void setParamsStr(String paramsStr) {
        this.paramsStr = paramsStr;
    }
}
