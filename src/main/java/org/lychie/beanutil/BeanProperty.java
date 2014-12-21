package org.lychie.beanutil;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.lychie.beanutil.exception.BeanException;
/**
 * 类属性工具类
 * @author Lychie Fan
 */
public class BeanProperty {
	
	private BeanProperty() {}
	
	/**
	 * 设置属性的值
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) {
		Field field = null;
		try {
			field = getAccessibleField(object, fieldName);
			field.set(object, value);
		} catch (Throwable e) {
			if(field != null){  // must ensure
				throw new BeanException(e);
			}
			throw new BeanException("类 " + BeanClass.getSimpleClassName(object) + " 中找不到 " + fieldName + " 属性");
		}
	}
	
	/**
	 * 获取属性的值
	 */
	@SuppressWarnings("unchecked")
	public static <E> E getFieldValue(Object object, String fieldName) {
		try {
			return (E) getAccessibleField(object, fieldName).get(object);
		} catch (ClassCastException e) {  // must catch ClassCastException first
			throw e;
		} catch (Throwable e) {
			throw new BeanException("类 " + BeanClass.getSimpleClassName(object) + " 中找不到 " + fieldName + " 属性");
		}
	}

	/**
	 * 获取属性的类型
	 */
	public static Class<?> getFieldType(Object object, String fieldName) {
		try {
			return getAccessibleField(object, fieldName).getType();
		} catch (NullPointerException e) { // only NullPointerException could be thrown
			throw new BeanException("类 " + BeanClass.getSimpleClassName(object) + " 中找不到 " + fieldName + " 属性");
		}
	}

	/**
	 * 获取可访问的属性
	 */
	public static Field getAccessibleField(Object object, String fieldName){
		Class<?> beanClass = BeanClass.getClass(object);
		while(beanClass != null){
			try {
				Field field = beanClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (Throwable e) { /* ignore */ }
			beanClass = beanClass.getSuperclass();
		}
		return null;
	}

	/**
	 * 获取声明的属性列表(当前类声明的属性, 不包括超类的属性)
	 */
	public static List<Field> getDeclaredFields(Class<?> beanClass){
		Field[] fields = beanClass.getDeclaredFields();
		if(fields.length == 0){
			return null;
		}
		for(Field field : fields){
			field.setAccessible(true);
		}
		return Arrays.asList(fields);
	}

	/**
	 * 获取声明的非静态的属性列表(当前类声明的属性, 不包括超类的属性)
	 */
	public static List<Field> getDeclaredNonStaticFields(Class<?> beanClass){
		Field[] fields = beanClass.getDeclaredFields();
		if(fields.length == 0) {
			return null;
		}
		List<Field> fieldList = new ArrayList<Field>(fields.length);
		for(Field field : fields){
			if((field.getModifiers() & Modifier.STATIC) != Modifier.STATIC){
				field.setAccessible(true);
				fieldList.add(field);
			}
		}
		return fieldList;
	}

	/**
	 * 获取声明的属性列表(包含超类的属性)
	 */
	public static List<Field> getReferableFields(Class<?> beanClass){
		List<Field> pojoClassFields;
		List<Field> fields = new ArrayList<Field>();
		while(beanClass != null){
			pojoClassFields = getDeclaredFields(beanClass);
			if(pojoClassFields != null){
				fields.addAll(pojoClassFields);
			}
			beanClass = beanClass.getSuperclass();
		}
		return fields.size() == 0 ? null : fields;
	}

	/**
	 * 获取声明的非静态的属性列表(包含超类的属性)
	 */
	public static List<Field> getReferableNonStaticFields(Class<?> beanClass){
		List<Field> fields = getReferableFields(beanClass);
		if(fields != null) {
			List<Field> fieldList = new ArrayList<Field>(fields.size());
			for(Field field : fields){
				if((field.getModifiers() & Modifier.STATIC) != Modifier.STATIC){
					fieldList.add(field);
				}
			}
			return fieldList.size() == 0 ? null : fieldList;
		}
		return null;
	}

	/**
	 * 获取声明的属性名称列表(当前类声明的属性名称, 不包括超类的属性名称)
	 */
	public static List<String> getDeclaredFieldNames(Class<?> beanClass){
		Field[] fields = beanClass.getDeclaredFields();
		if(fields.length == 0){
			return null;
		}
		List<String> fieldNames = new ArrayList<String>(fields.length);
		for(Field field : fields){
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}

	/**
	 * 获取声明的非静态的属性名称列表(当前类声明的属性名称, 不包括超类的属性名称)
	 */
	public static List<String> getDeclaredNonStaticFieldNames(Class<?> beanClass){
		List<Field> fields = getDeclaredNonStaticFields(beanClass);
		if(fields == null) {
			return null;
		}
		List<String> fieldNames = new ArrayList<String>(fields.size());
		for(Field field : fields){
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}

	/**
	 * 获取声明的属性名称列表(包含超类的属性名称)
	 */
	public static List<String> getReferableFieldNames(Class<?> beanClass){
		List<Field> fields = getReferableFields(beanClass);
		if(fields == null){
			return null;
		}
		List<String> fieldNames = new ArrayList<String>(fields.size());
		for(Field field : fields){
			fieldNames.add(field.getName());
		}
		return fieldNames;
	}

	/**
	 * 获取声明的非静态的属性名称列表(包含超类的属性名称)
	 */
	public static List<String> getReferableNonStaticFieldNames(Class<?> beanClass){
		List<Field> fields = getReferableNonStaticFields(beanClass);
		if(fields != null){
			List<String> fieldNames = new ArrayList<String>(fields.size());
			for(Field field : fields){
				fieldNames.add(field.getName());
			}
			return fieldNames;
		}
		return null;
	}
	
}