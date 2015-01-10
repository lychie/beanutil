package org.lychie.beanutil;

import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import org.lychie.beanutil.exception.BeanException;
import org.lychie.beanutil.exception.AbnormalException;

/**
 * Bean class 工具类
 * 
 * @author Lychie Fan
 */
public class BeanClass {

	/**
	 * 创建类的一个实例
	 * 
	 * @param beanClass
	 *            类
	 */
	public static <E> E newInstance(Class<E> beanClass) {
		try {
			return beanClass.newInstance();
		} catch (Throwable e) {
			throw new AbnormalException(e);
		}
	}

	/**
	 * 通过调用构造方法创建类的实例。要求构造方法的参数类型不能存在基本数据类型
	 * 
	 * @param beanClass
	 *            类
	 * @param argValues
	 *            构造方法的参数值
	 */
	public static <E> E newInstance(Class<E> beanClass, Object... argValues) {
		return BeanMethod.invokeConstructor(beanClass, argValues);
	}

	/**
	 * 通过调用构造方法创建类的实例。
	 * 
	 * @param beanClass
	 *            类
	 * @param argValues
	 *            构造方法的参数值
	 * @param argTypes
	 *            构造方法的参数类型
	 */
	public static <E> E newInstance(Class<E> beanClass, Object[] argValues,
			Class<?>[] argTypes) {
		return BeanMethod.invokeConstructor(beanClass, argValues, argTypes);
	}

	/**
	 * 根据类全名获取类的Class实例
	 * 
	 * @param className
	 *            类的全路径
	 */
	public static Class<?> forName(String className) {
		try {
			return Class.forName(className);
		} catch (Throwable e) {
			throw new AbnormalException(e);
		}
	}

	/**
	 * 判断参数是否是一个Class
	 * 
	 * @param testObject
	 *            被测试的对象
	 */
	public static boolean isClass(Object testObject) {
		return testObject instanceof Class;
	}

	/**
	 * 若测试对象为一个Class, 则, 判断测试对象是否派生自被测试的类;<br>
	 * 若测试对象为一个Object, 则, 判断测试对象是否是被测试类的一个实例
	 * 
	 * @param testObject
	 *            测试的对象
	 * @param targetClass
	 *            被测试的类
	 */
	public static boolean isFrom(Object testObject, Class<?> targetClass) {
		if (testObject instanceof Class) {
			Class<?> testClass = (Class<?>) testObject;
			return targetClass.isAssignableFrom(testClass);
		}
		return targetClass.isInstance(testObject);
	}

	/**
	 * 是否为内部类
	 * 
	 * @param beanClass
	 *            类
	 */
	public static boolean isInnerClass(Class<?> beanClass) {
		return beanClass.getName().indexOf("$") != -1;
	}

	/**
	 * 获取类的超类列表
	 * 
	 * @param beanClass
	 *            类
	 */
	public static List<Class<?>> getSuperclasses(Class<?> beanClass) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		while ((beanClass = beanClass.getSuperclass()) != null) {
			classes.add(beanClass);
		}
		return classes;
	}

	/**
	 * 获取接口列表
	 * 
	 * @param beanClass
	 *            类/接口
	 * @return 如果没有, 则返回空的集合
	 */
	public static List<Class<?>> getInterfaces(Class<?> beanClass) {
		Set<Class<?>> set = new HashSet<Class<?>>();
		do {
			set.addAll(Arrays.asList(beanClass.getInterfaces()));
		} while ((beanClass = beanClass.getSuperclass()) != null);
		return new ArrayList<Class<?>>(set);
	}

	/**
	 * 获取含泛型类型的超类中, 泛型参数列表的第一个参数的类型
	 * 
	 * @param beanClass
	 *            泛型超类的子类
	 */
	public static Class<?> getGenericSuperclass(Class<?> beanClass) {
		return getGenericSuperclass(beanClass, 0);
	}

	/**
	 * 获取含泛型类型的超类中, 泛型参数列表的第N个参数的类型
	 * 
	 * @param beanClass
	 *            泛型超类的子类
	 * @param index
	 *            泛型列表中参数的索引值
	 */
	public static Class<?> getGenericSuperclass(Class<?> beanClass, int index) {
		try {
			Type type = beanClass.getGenericSuperclass();
			ParameterizedType ptype = (ParameterizedType) type;
			Type[] types = ptype.getActualTypeArguments();
			return (Class<?>) types[index];
		} catch (ClassCastException e) {
			throw new BeanException(beanClass.getSimpleName()
					+ " class must have a supper class with a generic type");
		} catch (Throwable e) {
			throw new AbnormalException(e);
		}
	}

	/**
	 * 获取参数对象的类型
	 * 
	 * @param args
	 *            参数对象
	 */
	public static Class<?>[] getArgumentsActualType(Object... args) {
		if (args == null || args.length == 0) {
			return null;
		}
		int length = args.length;
		Class<?>[] argsType = new Class<?>[length];
		for (int i = 0; i < length; i++) {
			argsType[i] = args[i].getClass();
		}
		return argsType;
	}

	/**
	 * 获取参数对象的类名
	 * 
	 * @param obj
	 *            普通对象或类
	 */
	public static String getClassName(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Class) {
			return ((Class<?>) obj).getName();
		}
		return obj.getClass().getName();
	}

	/**
	 * 获取参数对象的简单类名
	 * 
	 * @param obj
	 *            普通对象或类
	 */
	public static String getSimpleClassName(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof Class) {
			return ((Class<?>) obj).getSimpleName();
		}
		return obj.getClass().getSimpleName();
	}

	/**
	 * 获取参数的Class对象
	 * 
	 * @param obj
	 *            普通对象或类
	 */
	public static Class<?> getClass(Object obj) {
		return obj instanceof Class ? (Class<?>) obj : obj.getClass();
	}

}