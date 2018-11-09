package cn.com.agree.hook.classloader.loadapk;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/11/8.
 */

public class RefInvoke {
    /**
     * 无参
     *
     * @param className
     * @return
     */
    public static Object createObject(String className) {
        Class[] parameterTypes = new Class[]{};
        Object[] parameterValues = new Object[]{};

        try {
            Class r = Class.forName(className);
            return createObject(r, parameterTypes, parameterValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 无参
     *
     * @param clazz
     * @return
     */
    public static Object createObject(Class clazz) {
        Class[] parameterTypes = new Class[]{};
        Object[] parameterValues = new Object[]{};

        return createObject(clazz, parameterTypes, parameterValues);
    }

    /**
     * 一个参数
     *
     * @param className
     * @param parameterType
     * @param parameterValue
     * @return
     */
    public static Object createObject(String className, Class parameterType, Object parameterValue) {
        Class[] parameterTypes = new Class[]{parameterType};
        Object[] parameterValues = new Object[]{parameterValue};

        try {
            Class r = Class.forName(className);
            return createObject(r, parameterTypes, parameterValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 一个参数
     *
     * @param clazz
     * @param parameterType
     * @param parameterValue
     * @return
     */
    public static Object createObject(Class clazz, Class parameterType, Object parameterValue) {
        Class[] parameterTypes = new Class[]{parameterType};
        Object[] parameterValues = new Object[]{parameterValue};

        return createObject(clazz, parameterTypes, parameterValues);
    }

    /**
     * 多个参数
     *
     * @param className
     * @param parameterTypes
     * @param parameterValues
     * @return
     */
    public static Object createObject(String className, Class[] parameterTypes, Object[] parameterValues) {
        try {
            Class r = Class.forName(className);
            return createObject(r, parameterTypes, parameterValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 多个参数
     *
     * @param clazz
     * @param parameterTypes
     * @param parameterValues
     * @return
     */
    public static Object createObject(Class clazz, Class[] parameterTypes, Object[] parameterValues) {
        try {
            Constructor constructor = clazz.getConstructor(parameterTypes);
            return constructor.newInstance(parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 多个参数
     *
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @param parameterValues
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class[] parameterTypes, Object[] parameterValues) {
        if (obj == null) {
            return null;
        }
        try {
            // 在指定类中获取指定的方法, 调用一个private方法
            Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(obj, parameterValues);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 执行类实例方法(一个参数)
     *
     * @param obj
     * @param methodName
     * @param parameterType
     * @param parameterValue
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class parameterType, Object parameterValue) {
        Class[] parameterTypes = {parameterType};
        Object[] parameterValues = {parameterValue};

        return invokeInstanceMethod(obj, methodName, parameterTypes, parameterValues);
    }

    /**
     * 执行类实例方法(无参)
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName) {
        Class[] parameterTypes = new Class[]{};
        Object[] parameterValues = new Object[]{};

        return invokeInstanceMethod(obj, methodName, parameterTypes, parameterValues);
    }


    /**
     * 执行类静态方法(无参)
     *
     * @param className
     * @param methodName
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName) {
        Class[] parameterTypes = new Class[]{};
        Object[] parameterValues = new Object[]{};

        return invokeStaticMethod(className, methodName, parameterTypes, parameterValues);
    }

    /**
     * 执行类静态方法(一个参数)
     *
     * @param className
     * @param methodName
     * @param parameterType
     * @param parameterValue
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName, Class parameterType, Object parameterValue) {
        Class[] parameterTypes = new Class[]{parameterType};
        Object[] parameterValues = new Object[]{parameterValue};

        return invokeStaticMethod(className, methodName, parameterTypes, parameterValues);
    }

    /**
     * 执行类静态方法(多个参数)
     *
     * @param className
     * @param methodName
     * @param parameterTypes
     * @param parameterValues
     * @return
     */
    public static Object invokeStaticMethod(String className, String methodName, Class[] parameterTypes, Object[] parameterValues) {
        try {
            Class obj_class = Class.forName(className);
            return invokeStaticMethod(obj_class, methodName, parameterTypes, parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 执行类静态方法(无参)
     *
     * @param clazz
     * @param methodName
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String methodName) {
        Class[] parameterTypes = new Class[]{};
        Object[] parameterValues = new Object[]{};

        return invokeStaticMethod(clazz, methodName, parameterTypes, parameterValues);
    }

    /**
     * 执行类静态方法(一个参数)
     *
     * @param clazz
     * @param methodName
     * @param parameterType
     * @param parameterValue
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String methodName, Class parameterType, Object parameterValue) {
        Class[] parameterTypes = new Class[]{parameterType};
        Object[] parameterValues = new Object[]{parameterValue};

        return invokeStaticMethod(clazz, methodName, parameterTypes, parameterValues);
    }

    /**
     * 执行类静态方法(多个参数)
     *
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @param parameterValues
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String methodName, Class[] parameterTypes, Object[] parameterValues) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(null, parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 获取类属性(简写版本)
     *
     * @param obj
     * @param filedName
     * @return
     */
    public static Object getFieldObject(Object obj, String filedName) {
        return getFieldObject(obj.getClass(), obj, filedName);
    }

    /**
     * 获取类属性
     *
     * @param className
     * @param obj
     * @param filedName
     * @return
     */
    public static Object getFieldObject(String className, Object obj, String filedName) {
        try {
            Class clazz = Class.forName(className);
            return getFieldObject(clazz, obj, filedName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取类属性
     *
     * @param clazz
     * @param obj
     * @param filedName
     * @return
     */
    public static Object getFieldObject(Class clazz, Object obj, String filedName) {
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 设置类属性(简写版本)
     *
     * @param obj
     * @param filedName
     * @param filedValue
     */
    public static void setFieldObject(Object obj, String filedName, Object filedValue) {
        setFieldObject(obj.getClass(), obj, filedName, filedValue);
    }

    /**
     * 设置类属性
     *
     * @param clazz
     * @param obj
     * @param filedName
     * @param filedValue
     */
    public static void setFieldObject(Class clazz, Object obj, String filedName, Object filedValue) {
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置类属性
     *
     * @param className
     * @param obj
     * @param filedName
     * @param filedValue
     */
    public static void setFieldObject(String className, Object obj, String filedName, Object filedValue) {
        try {
            Class clazz = Class.forName(className);
            setFieldObject(clazz, obj, filedName, filedValue);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置类静态属性
     *
     * @param className
     * @param filedName
     * @param filedValue
     */
    public static void setStaticFieldObject(String className, String filedName, Object filedValue) {
        setFieldObject(className, null, filedName, filedValue);
    }

    /**
     * 设置类静态属性
     *
     * @param clazz
     * @param filedName
     * @param filedValue
     */
    public static void setStaticFieldObject(Class clazz, String filedName, Object filedValue) {
        setFieldObject(clazz, null, filedName, filedValue);
    }

    /**
     * 获取静态属性
     *
     * @param className
     * @param filedName
     * @return
     */
    public static Object getStaticFieldObject(String className, String filedName) {
        return getFieldObject(className, null, filedName);
    }

    /**
     * 获取静态属性
     *
     * @param clazz
     * @param filedName
     * @return
     */
    public static Object getStaticFieldObject(Class clazz, String filedName) {
        return getFieldObject(clazz, null, filedName);
    }
}
