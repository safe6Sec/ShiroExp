package cn.safe6.util;


import cn.safe6.payload.TomcatEchoAll;
import cn.safe6.payload.memshell.Behinder2;
import cn.safe6.payload.memshell.Behinder3;
import net.bytebuddy.ByteBuddy;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

public class GetByteCodeUtil {

    public static void main(String[] args) throws Exception {
       decodeData();
       //encodeData();

        //getFileByBytes(Behinder3.getMemBehinder3Payload("shell@2021"),"load.class");
    }

    public static void decodeData() throws Exception {

        String data ="";
        System.out.println(data);
        System.out.println("oldLen:"+data.length());
        getFileByBytes(Base64.getDecoder().decode(data),"load.class");

        //对比生成前后，长度差距
        //String newData = Base64.getEncoder().encodeToString(getBytesForFile("load.class"));
        //System.out.println(newData);
        //System.out.println("newLen:"+ newData.length());

    }

    public static void encodeData(){
        byte[] code= new ByteBuddy()
                .redefine(cn.safe6.payload.memshell.BehinderLoader2.class)
                .name("cn.safe6.payload.memshell.BehinderLoader2")
                .make()
                .getBytes();
        System.out.println(Base64.getEncoder().encodeToString(code));
    }

    public static String getEncodeData(Class className){
        byte[] code= new ByteBuddy()
                .redefine(className)
                .name(className.getName())
                .make()
                .getBytes();
        return Base64.getEncoder().encodeToString(code);
    }

    public static byte[] getDataBytes(Class className){
        byte[] code= new ByteBuddy()
                .redefine(className)
                .name(className.getName())
                .make()
                .getBytes();
        return code;
    }


    //将Byte数组转换成文件
    public static void getFileByBytes(byte[] bytes, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static byte[] getBytesForFile(String fileName) {
        BufferedInputStream bos = null;
        FileInputStream fos = null;
        File file = new File(fileName);
        byte[] b = new byte[(int)file.length()];
        try {
            fos = new FileInputStream(file);
            bos = new BufferedInputStream(fos);
            bos.read(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return b;
    }

    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }




}
