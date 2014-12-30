package org.lychie.beanutil;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.lang.reflect.Field;
import org.lychie.beanutil.exception.BeanException;
/**
 * 包装器
 * @author Lychie Fan
 */
public abstract class Wrapper {

	protected Class<?> beanClass;
	protected Map<String, Field> properties;
	
	public Wrapper(Class<?> beanClass) {
		init(beanClass);
	}
	
	/**
	 * 获取属性的类型
	 */
	public Class<?> getPropertyType(String propertyName) {
		Field field = properties.get(propertyName);
		if(field != null) {
			return field.getType();
		}
		throw new BeanException("类 " + beanClass.getSimpleName() + " 中找不到 " + propertyName + " 属性");
	}
	
	/**
	 * 设置属性的值
	 */
	protected void setPropertyValue(Object bean, String propertyName, Object propertyValue) {
		Field field = properties.get(propertyName);
		if(field != null) {
			try {
				field.set(bean, propertyValue);
				return ;
			} catch (Throwable e) {
				throw new BeanException(e);
			}
		}
		throw new BeanException("类 " + beanClass.getSimpleName() + " 中找不到 " + propertyName + " 属性");
	}
	
	/**
	 * 获取属性的值
	 */
	@SuppressWarnings("unchecked")
	protected <E> E getPropertyValue(Object bean, String propertyName) {
		Field field = properties.get(propertyName);
		if(field != null) {
			try {
				return (E) field.get(bean);
			} catch (Throwable e) {
				throw new BeanException(e);
			}
		}
		throw new BeanException("类 " + beanClass.getSimpleName() + " 中找不到 " + propertyName + " 属性");
	}
	
	/**
	 * 初始化
	 */
	protected void init(Class<?> beanClass) {
		this.beanClass = beanClass;
		properties = new HashMap<String, Field>();
		List<Field> list = BeanProperty.getReferableFields(beanClass);
		for(Field field : list) {
			String name = field.getName();
			if(!properties.containsKey(name)) {
				properties.put(name, field);
			}
		}
	}
	
}