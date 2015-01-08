package org.lychie.beanutil;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import org.lychie.beanutil.exception.BeanException;
import org.lychie.beanutil.exception.AbnormalException;

/**
 * Bean method 工具类
 * 
 * @author Lychie Fan
 */
public class BeanMethod {

	/**
	 * 调用方法, 方法不能存在基本数据类型
	 * 
	 * @param object
	 *            调用类方法时, 该参数可为Class对象; 调用对象方法时, 该参数必须为类的一个实例
	 * @param methodName
	 *            方法名称
	 * @param argValues
	 *            方法参数的值
	 */
	public static <E> E invoke(Object object, String methodName,
			Object... argValues) {

		return invoke(object, methodName, argValues,
				BeanClass.getArgumentsActualType(argValues));
	}

	/**
	 * 调用方法
	 * 
	 * @param object
	 *            调用类方法时, 该参数可为Class对象; 调用对象方法时, 该参数必须为类的一个实例
	 * @param methodName
	 *            方法名称
	 * @param argValues
	 *            方法参数的值
	 * @param argTypes
	 *            方法参数的类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> E invoke(Object object, String methodName,
			Object[] argValues, Class<?>[] argTypes) {

		try {
			Class<?> beanClass = BeanClass.getClass(object);
			return (E) getAccessibleMethod(beanClass, methodName, argTypes)
					.invoke(object, argValues);
		} catch (BeanException e) { // must catch BeanException
			throw e;
		} catch (ClassCastException e) { // must catch ClassCastException
			throw e;
		} catch (Throwable e) {
			throw new AbnormalException(e);
		}
	}

	/**
	 * 获取可访问的方法
	 * 
	 * @param beanClass
	 *            类
	 * @param methodName
	 *            方法名称
	 * @param types
	 *            方法参数类型
	 * @return
	 */
	public static Method getAccessibleMethod(Class<?> beanClass,
			String methodName, Class<?>... types) {

		if (methodName == null) {
			throw new NullPointerException(
					"the argument methodName can not be null");
		}
		if (beanClass == null) {
			throw new NullPointerException(
					"the argument beanClass can not be null");
		}
		String beanClassName = beanClass.getSimpleName();
		while (beanClass != null) {
			try {
				Method target = beanClass.getDeclaredMethod(methodName, types);
				target.setAccessible(true);
				return target;
			} catch (Throwable e) {
				/* ignore */
			}
			beanClass = beanClass.getSuperclass();
		}
		String argsType = "";
		if (types != null) {
			StringBuilder builder = new StringBuilder();
			for (Class<?> type : types) {
				builder.append(type.getSimpleName()).append(", ");
			}
			int length = builder.length();
			if (length > 0) {
				argsType = builder.substring(0, length - 2);
			}
		}
		throw new BeanException(methodName + "(" + argsType
				+ ") method can not be found in the class " + beanClassName);
	}

	/**
	 * 调用构造方法, 方法的参数类型不能存在基本数据类型
	 * 
	 * @param beanClass
	 *            类
	 * @param argValues
	 *            构造方法参数的值
	 */
	static <E> E invokeConstructor(Class<E> beanClass, Object... argValues) {

		return invokeConstructor(beanClass, argValues,
				BeanClass.getArgumentsActualType(argValues));
	}

	/**
	 * 调用构造方法
	 * 
	 * @param beanClass
	 *            类
	 * @param argValues
	 *            构造方法参数的值
	 * @param argTypes
	 *            构造方法参数的类型
	 */
	static <E> E invokeConstructor(Class<E> beanClass, Object[] argValues,
			Class<?>[] argTypes) {

		try {
			Constructor<E> constructor = beanClass
					.getDeclaredConstructor(argTypes);
			constructor.setAccessible(true);
			return constructor.newInstance(argValues);
		} catch (Throwable e) {
			throw new AbnormalException(e);
		}
	}

}