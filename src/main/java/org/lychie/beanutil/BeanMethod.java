package org.lychie.beanutil;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import org.lychie.beanutil.exception.BeanException;
import org.lychie.beanutil.exception.AbnormalException;

/**
 * Bean method 工具类
 * @author Lychie Fan
 */
public class BeanMethod {

	/**
	 * 调用构造方法, 方法的参数类型不能存在基本数据类型
	 */
	public static <E> E invokeConstructor(Class<E> beanClass, Object... argValues) {
		return invokeConstructor(beanClass, argValues,
				BeanClass.getArgumentsActualType(argValues));
	}

	/**
	 * 调用构造方法
	 */
	public static <E> E invokeConstructor(Class<E> beanClass,
			Object[] argValues, Class<?>[] argTypes) {
		try {
			Constructor<E> constructor = beanClass
					.getDeclaredConstructor(argTypes);
			constructor.setAccessible(true);
			return constructor.newInstance(argValues);
		} catch (Throwable e) {
			throw new AbnormalException(e);
		}
	}

	/**
	 * 调用方法, 方法不能存在基本数据类型
	 */
	public static <E> E invokeMethod(Object object, String methodName,
			Object... argValues) {
		return invokeMethod(object, methodName, argValues,
				BeanClass.getArgumentsActualType(argValues));
	}

	/**
	 * 调用方法
	 */
	@SuppressWarnings("unchecked")
	public static <E> E invokeMethod(Object object, String methodName,
			Object[] argValues, Class<?>[] argTypes) {
		try {
			Class<?> beanClass = BeanClass.getClass(object);
			return (E) getAccessibleMethod(beanClass, methodName,
					argTypes).invoke(object, argValues);
		} catch (BeanException e) { // must catch BeanException
			throw e;
		}  catch (ClassCastException e) { // must catch ClassCastException
			throw e;
		} catch (Throwable e) {
			throw new AbnormalException(e);
		}
	}

	/**
	 * 获取可方法的方法
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

}