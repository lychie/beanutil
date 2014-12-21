package org.lychie.beanutil;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import org.lychie.beanutil.exception.BeanException;
/**
 * 类方法工具类
 * @author Lychie Fan
 */
public class BeanMethod {

	private BeanMethod() {}
	
	/**
	 * 调用构造方法, 方法的参数类型不能存在基本数据类型
	 */
	public static <E> E invokeConstructor(Class<E> beanClass, Object... argValues) {
		return invokeConstructor(beanClass, argValues, BeanClass.getArgumentsActualType(argValues));
	}
	
	/**
	 * 调用构造方法
	 */
	public static <E> E invokeConstructor(Class<E> beanClass, Object[] argValues, Class<?>[] argTypes) {
		try {
			Constructor<E> constructor = beanClass.getDeclaredConstructor(argTypes);
			constructor.setAccessible(true);
			return constructor.newInstance(argValues);
		} catch (Throwable e) {
			throw new BeanException(e);
		}
	}
	
	/**
	 * 调用方法, 方法不能存在基本数据类型
	 */
	public static <E> E invokeMethod(Object object, String methodName, Object... argValues) {
		return invokeMethod(object, methodName, argValues, BeanClass.getArgumentsActualType(argValues));
	}
	
	/**
	 * 调用方法
	 */
	@SuppressWarnings("unchecked")
	public static <E> E invokeMethod(Object object, String methodName, Object[] argValues, Class<?>[] argTypes) {
		try {
			return (E) getAccessibleMethod(object, methodName, argTypes).invoke(object, argValues);
		} catch (ClassCastException e) { // must catch ClassCastException
			throw e;
		}  catch (NullPointerException e) { // must catch NullPointerException
			throw new BeanException(new NoSuchMethodException("类 " + BeanClass.getSimpleClassName(object) + " 中找不到 " + methodName + " 的方法"));
		} catch (Throwable e) {
			throw new BeanException(e);
		}
	}
	
	/**
	 * 获取可方法的方法
	 */
	public static Method getAccessibleMethod(Object object, String methodName, Class<?>... types){
		Class<?> pojoClass = BeanClass.getClass(object);
		while(pojoClass != null){
			try {
				Method target = pojoClass.getDeclaredMethod(methodName, types);
				target.setAccessible(true);
				return target;
			} catch (Throwable e) { /* ignore */ }
			pojoClass = pojoClass.getSuperclass();
		}
		return null;
	}
	
}