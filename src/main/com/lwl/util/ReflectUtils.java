package main.com.lwl.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lwl
 * @date 2018/7/6 16:09
 * @description
 */
public class ReflectUtils {
    private static final int TYPE_GET = 0;
    private static final int TYPE_SET = 1;
    private static final String GET = "get";
    private static final String SET = "set";

    //将
    public static <E, M> List<M> listModuleFromEnum(Class<E> enumClass, Class<M> moduleClass){
        Field[] enumFields = enumClass.getDeclaredFields();
        List<String> privateEnumFieldStringList = getPrivateFieldStrings(enumFields);
        Method[] enumGetMethods = getDeclaredMethods(enumClass, privateEnumFieldStringList, TYPE_GET);
        E[] enumList = enumClass.getEnumConstants();

        Field[] moduleFields = moduleClass.getDeclaredFields();
        List<String> privateModuleFieldStringList = getPrivateFieldStrings(moduleFields);
        Method[] moduleSetMethods = getDeclaredMethods(moduleClass, privateModuleFieldStringList, TYPE_SET);
        List<M> moduleList = new ArrayList<>();

        for (E anEnumList : enumList) {
            M moduleInstance = null;
            try {
                moduleInstance = moduleClass.newInstance();
                for (int j = 0; j < enumGetMethods.length; j++) {
                    Object object;
                    object = enumGetMethods[j].invoke(anEnumList);
                    moduleSetMethods[j].invoke(moduleInstance, object);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            moduleList.add(moduleInstance);
        }
        return moduleList;
    }

    private static List<String> getPrivateFieldStrings(Field[] enumFields) {
        List<String> privateFieldStringList = new ArrayList<>();
        for (Field field: enumFields){
            //Enum类型中有个默认的$VALUE字段
            if (Modifier.isPrivate(field.getModifiers()) && !field.getName().contains("$")){
                field.setAccessible(true);
                privateFieldStringList.add(field.getName());
            }
        }
        return privateFieldStringList;
    }

    private static Method[] getDeclaredMethods(Class objectClass, List<String> fieldNames, int type) {
        int length = fieldNames.size();
        Method[] methods = new Method[length];
        for (int i = 0; i < length; i++){
            if (type == TYPE_GET){
                methods[i] = getGetMethod(objectClass, fieldNames.get(i));
            }else {
                methods[i] = getSetMethod(objectClass, fieldNames.get(i));
            }
        }
        return methods;
    }

    //获取get方法
    private static Method getGetMethod(Class objectClass, String fieldName){
        StringBuffer sb = new StringBuffer();
        sb.append(GET);
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getDeclaredMethod(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取set方法
    public static Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append(SET);
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            return objectClass.getDeclaredMethod(sb.toString(), parameterTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}