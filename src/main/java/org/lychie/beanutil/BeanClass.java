package org.lychie.beanutil;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import org.lychie.beanutil.exception.AbnormalException;
import org.lychie.beanutil.exception.BeanException;

/**
 * Bean class 工具类
 * @author Lychie Fan
 */
public class BeanClass {

	/**
	 * 创建类的一个实例
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
	 */
	public static <E> E newInstance(Class<E> beanClass, Object... argValues) {
		return BeanMethod.invokeConstructor(beanClass, argValues);
	}

	/**
	 * 通过调用构造方法创建类的实例。
	 */
	public static <E> E newInstance(Class<E> beanClass, Object[] argValues,
			Class<?>[] argTypes) {
		return BeanMethod.invokeConstructor(beanClass, argValues, argTypes);
	}

	/**
	 * 根据类全名获取类的Class实例
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
	 */
	public static boolean isClass(Object testObject) {
		return testObject instanceof Class;
	}

	/**
	 * 第一个参数若为 Class, 判断第一个参数类是否派生自第二个参数类<br>
	 * 第一个参数若为 Object, 判断第一个参数对象是否为第二个参数类的实例
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
	 */
	public static boolean isInnerClass(Class<?> beanClass) {
		return beanClass.getName().indexOf("$") != -1;
	}

	/**
	 * 获取超类列表
	 */
	public static List<Class<?>> getSuperclasses(Class<?> beanClass) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		while ((beanClass = beanClass.getSuperclass()) != null) {
			classes.add(beanClass);
		}
		return classes;
	}

	/**
	 * 获取含泛型类型的超类中, 泛型参数列表的第一个参数的类型
	 */
	public static Class<?> getGenericSuperclass(Class<?> beanClass) {
		return getGenericSuperclass(beanClass, 0);
	}

	/**
	 * 获取含泛型类型的超类中, 泛型参数列表的第N个参数的类型
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
	 * 获取参数的类型
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
	 * 获取参数的类名
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
	 * 获取参数的简单类名
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
	 * 获取Class对象
	 */
	public static Class<?> getClass(Object obj) {
		return obj instanceof Class ? (Class<?>) obj : obj.getClass();
	}

}