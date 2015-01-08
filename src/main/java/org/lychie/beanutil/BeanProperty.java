package org.lychie.beanutil;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.lychie.beanutil.exception.BeanException;

/**
 * Bean property 工具类
 * 
 * @author Lychie Fan
 */
public class BeanProperty {

	/**
	 * 获取可访问的属性
	 * 
	 * @param beanClass
	 *            类
	 * @param fieldName
	 *            属性名称
	 * @return
	 */
	public static Field getAccessibleField(Class<?> beanClass, String fieldName) {
		if (fieldName == null) {
			throw new NullPointerException(
					"the argument fieldName can not be null");
		}
		if (beanClass == null) {
			throw new NullPointerException(
					"the argument beanClass can not be null");
		}
		String beanClassName = beanClass.getSimpleName();
		while (beanClass != null) {
			try {
				Field field = beanClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (Throwable e) {
				/* ignore */
			}
			beanClass = beanClass.getSuperclass();
		}
		throw new BeanException(fieldName
				+ " property can not be found in the class " + beanClassName);
	}

	/**
	 * 获取声明的属性列表(当前类声明的属性, 不包括超类的属性)
	 * 
	 * @param beanClass
	 *            类
	 * @return
	 */
	public static List<Field> getDeclaredFields(Class<?> beanClass) {
		Field[] fields = beanClass.getDeclaredFields();
		if (fields.length == 0) {
			return null;
		}
		for (Field field : fields) {
			field.setAccessible(true);
		}
		return Arrays.asList(fields);
	}

	/**
	 * 获取声明的非静态的属性列表(当前类声明的属性, 不包括超类的属性)
	 * 
	 * @param beanClass
	 *            类
	 * @return
	 */
	public static List<Field> getDeclaredNonStaticFields(Class<?> beanClass) {
		List<Field> fieldList = getDeclaredFields(beanClass);
		return dropStaticFields(fieldList);
	}

	/**
	 * 获取声明的属性列表(包含超类的属性)
	 * 
	 * @param beanClass
	 *            类
	 * @return
	 */
	public static List<Field> getReferableFields(Class<?> beanClass) {
		List<Field> list = new ArrayList<Field>();
		while (beanClass != null) {
			List<Field> pojoClassFields = getDeclaredFields(beanClass);
			if (pojoClassFields != null) {
				list.addAll(pojoClassFields);
			}
			beanClass = beanClass.getSuperclass();
		}
		return list;
	}

	/**
	 * 获取声明的非静态的属性列表(包含超类的属性)
	 * 
	 * @param beanClass
	 *            类
	 * @return
	 */
	public static List<Field> getReferableNonStaticFields(Class<?> beanClass) {
		List<Field> fieldList = getReferableFields(beanClass);
		return dropStaticFields(fieldList);
	}

	/**
	 * 获取声明的属性名称列表(当前类声明的属性名称, 不包括超类的属性名称)
	 * 
	 * @param beanClass
	 *            类
	 * @return
	 */
	public static List<String> getDeclaredFieldNames(Class<?> beanClass) {
		List<Field> fieldList = getDeclaredFields(beanClass);
		return obtainFieldNames(fieldList);
	}

	/**
	 * 获取声明的非静态的属性名称列表(当前类声明的属性名称, 不包括超类的属性名称)
	 * 
	 * @param beanClass
	 *            类
	 * @return
	 */
	public static List<String> getDeclaredNonStaticFieldNames(Class<?> beanClass) {
		List<Field> fieldList = getDeclaredNonStaticFields(beanClass);
		return obtainFieldNames(fieldList);
	}

	/**
	 * 获取声明的属性名称列表(包含超类的属性名称)
	 * 
	 * @param beanClass
	 *            类
	 * @return
	 */
	public static List<String> getReferableFieldNames(Class<?> beanClass) {
		List<Field> fieldList = getReferableFields(beanClass);
		return obtainFieldNames(fieldList);
	}

	/**
	 * 获取声明的非静态的属性名称列表(包含超类的属性名称)
	 * 
	 * @param beanClass
	 *            类
	 * @return
	 */
	public static List<String> getReferableNonStaticFieldNames(
			Class<?> beanClass) {
		
		List<Field> fieldList = getReferableNonStaticFields(beanClass);
		return obtainFieldNames(fieldList);
	}

	/**
	 * 去除静态属性
	 * 
	 * @param origin
	 *            源集合
	 * @return
	 */
	private static List<Field> dropStaticFields(List<Field> origin) {
		List<Field> list = new ArrayList<Field>();
		if (origin != null) {
			for (Field field : origin) {
				if ((field.getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
					list.add(field);
				}
			}
		}
		return list;
	}

	/**
	 * 获取属性名称列表
	 * 
	 * @param fieldList
	 *            属性集合
	 * @return
	 */
	private static List<String> obtainFieldNames(List<Field> fieldList) {
		List<String> list = new ArrayList<String>();
		if (fieldList != null) {
			for (Field field : fieldList) {
				list.add(field.getName());
			}
		}
		return list;
	}

}