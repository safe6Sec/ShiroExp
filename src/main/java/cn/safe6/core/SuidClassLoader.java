package cn.safe6.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SuidClassLoader extends ClassLoader{
    private Map<String, byte[]> classByteMap = new HashMap<>();
    private Map<String, Class> cacheClass = new HashMap<>();

    public void addClass(String className,byte[] classByte){
        classByteMap.put(className,classByte);
    }

    private void readJar(JarFile jar) throws IOException {
        Enumeration<JarEntry> en = jar.entries();
        // 遍历jar文件所有实体
        while (en.hasMoreElements()){
            JarEntry je = en.nextElement();
            String name = je.getName();
            // 只class文件进行处理
            if (name.endsWith(".class")){
                String clss = name.replace(".class", "").replaceAll("/", ".");
                if(this.findLoadedClass(clss) != null) continue;
                // 读取class的byte内容
                InputStream input = jar.getInputStream(je);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];
                int bytesNumRead = 0;
                while ((bytesNumRead = input.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesNumRead);
                }
                byte[] cc = baos.toByteArray();
                input.close();
                // 将class name 和class byte存储到classByteMap
                classByteMap.put(clss, cc);
            }
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // 1. 检测自定ClassLoader缓存中有没有，有的话直接返回
            Class clazz = cacheClass.get(name);
            if (null != clazz) {
                return clazz;
            }

            try {
                // 2. 若缓存中没有，就从当前ClassLoader可加载的所有Class中找
                clazz = findClass(name);
                if (null != clazz) {
                    cacheClass.put(name, clazz);
                }else{
                    clazz = super.loadClass(name, resolve);
                }
            } catch (ClassNotFoundException ex) {
                // 3.当自定义ClassLoader中没有找到目标class，再调用系统默认的加载机制,走双亲委派模式
                clazz = super.loadClass(name, resolve);
            }

            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        }
    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        // 从classByteMap中获取
        byte[] result = classByteMap.get(name);
        if(result == null){
            // 没有找到则抛出对应异常
            throw new ClassNotFoundException();
        }else{
            // 将一个字节数组转为Class对象
            return defineClass(name, result, 0, result.length);
        }
    }


}
