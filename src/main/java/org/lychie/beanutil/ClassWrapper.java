package org.lychie.beanutil;

import java.util.Map;
import java.lang.reflect.Field;

/**
 * Bean class 包装器
 * 
 * @author Lychie Fan
 */
public class ClassWrapper extends Wrapper {

	private ClassWrapper(Class<?> beanClass) {
		super(beanClass);
	}

	/**
	 * 包装类对象
	 * 
	 * @param beanClass
	 *            被包装的类
	 * @return ClassWrapper
	 */
	public static ClassWrapper wrap(Class<?> beanClass) {
		return new ClassWrapper(beanClass);
	}

	/**
	 * 设置属性的值
	 * 
	 * @param bean
	 *            被操纵的对象
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性值
	 */
	public void setPropertyValue(Object bean, String propertyName,
			Object propertyValue) {

		super.setPropertyValue(bean, propertyName, propertyValue);
	}

	/**
	 * 获取属性的值
	 * 
	 * @param bean
	 *            被操纵的对象
	 * @param propertyName
	 *            属性名称
	 * @return
	 */
	public <E> E getPropertyValue(Object bean, String propertyName) {
		return super.getPropertyValue(bean, propertyName);
	}

	/**
	 * 获取包装的类的属性
	 * 
	 * @return
	 */
	public Map<String, Field> getProperties() {
		return properties;
	}

}