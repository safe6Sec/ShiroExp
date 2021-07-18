package cn.safe6.util;

import java.lang.reflect.Field;

// 反射类，解耦
public class Reflections {

    public static Object getFieldValue(Object object,String field) throws Exception{
        Field f0 = getField(object,field);
        return f0.get(object);
    }

    public static void setAccessible(Field field) throws Exception {
        field.setAccessible(true);
    }

    // 进行属性的设置
    public static void setFieldValue(Object object,String fieldName,Object fieldValue) throws Exception {
        Field field =  getField(object,fieldName);
        field.set(object,fieldValue);
    }

    public static Field getField(Object object,String fieldName) throws Exception{
        Class clas = object.getClass();
        Field field = null;
        // 我们这里必须要确保，这里写一个 while 循环
        while (clas != Object.class){
            try {
                field = clas.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e){
                clas = clas.getSuperclass();
            }
        }

        if (field != null){
            field.setAccessible(true);
//            return field.get(object);
            return field;
        }else {
            return null;
        }
    }

//    public static void main(String[] args) {
//        Reflections.
//    }
}
