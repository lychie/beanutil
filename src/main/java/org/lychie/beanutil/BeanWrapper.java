package org.lychie.beanutil;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.lang.reflect.Field;
import org.lychie.beanutil.exception.BeanException;
/**
 * 实例包装器
 * @author Lychie Fan
 */
public class BeanWrapper {
	
	private Object bean;
	private Map<String, Field> properties;

	private BeanWrapper(Object bean) {
		this.bean = bean;
		init();
	}
	
	public static BeanWrapper wrap(Object bean) {
		return new BeanWrapper(bean);
	}
	
	/**
	 * 设置属性的值
	 */
	public void setPropertyValue(String propertyName, Object propertyValue) {
		Field field = properties.get(propertyName);
		if(field != null) {
			try {
				field.set(bean, propertyValue);
				return ;
			} catch (Throwable e) {
				throw new BeanException(e);
			}
		}
		throw new BeanException("类 " + BeanClass.getSimpleClassName(bean) + " 中找不到 " + propertyName + " 属性");
	}
	
	/**
	 * 获取属性的值
	 */
	@SuppressWarnings("unchecked")
	public <E> E getPropertyValue(String propertyName) {
		Field field = properties.get(propertyName);
		if(field != null) {
			try {
				return (E) field.get(bean);
			} catch (Throwable e) {
				throw new BeanException(e);
			}
		}
		throw new BeanException("类 " + BeanClass.getSimpleClassName(bean) + " 中找不到 " + propertyName + " 属性");
	}
	
	/**
	 * 获取属性的类型
	 */
	public Class<?> getPropertyType(String propertyName) {
		Field field = properties.get(propertyName);
		if(field != null) {
			return field.getType();
		}
		throw new BeanException("类 " + BeanClass.getSimpleClassName(bean) + " 中找不到 " + propertyName + " 属性");
	}
	
	private void init() {
		List<Field> list = BeanProperty.getReferableFields(bean.getClass());
		properties = new HashMap<String, Field>();
		String name;
		for(Field field : list) {
			name = field.getName();
			if(!properties.containsKey(name)) {
				properties.put(name, field);
			}
		}
	}
	
}