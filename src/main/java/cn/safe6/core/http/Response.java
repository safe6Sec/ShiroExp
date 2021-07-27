package cn.safe6.core.http;

import java.util.List;
import java.util.Map;

public class Response {

    private Integer code;
    private Map<String, List<String>> header;
    private String data;
    private Integer length;

    public Response() {
    }

    public Response(Integer code, Map<String, List<String>> header, String data, Integer length) {
        this.code = code;
        this.header = header;
        this.data = data;
        this.length = length;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Map<String, List<String>> getHeader() {
        return header;
    }

    public void setHeader(Map<String, List<String>> header) {
        this.header = header;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
