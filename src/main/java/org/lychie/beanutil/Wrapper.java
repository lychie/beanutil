package org.lychie.beanutil;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.lang.reflect.Field;
import org.lychie.beanutil.exception.BeanException;
import org.lychie.beanutil.exception.AbnormalException;

/**
 * 抽象包装器
 * 
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
	 * 
	 * @param propertyName
	 *            属性名称
	 * @return
	 */
	public Class<?> getPropertyType(String propertyName) {
		Field field = properties.get(propertyName);
		if (field != null) {
			return field.getType();
		}
		throw new BeanException(errorOf(propertyName));
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
	protected void setPropertyValue(Object bean, String propertyName,
			Object propertyValue) {

		Field field = properties.get(propertyName);
		if (field != null) {
			try {
				field.set(bean, propertyValue);
				return;
			} catch (Throwable e) {
				throw new AbnormalException(e);
			}
		}
		throw new BeanException(errorOf(propertyName));
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
	@SuppressWarnings("unchecked")
	protected <E> E getPropertyValue(Object bean, String propertyName) {
		Field field = properties.get(propertyName);
		if (field != null) {
			try {
				return (E) field.get(bean);
			} catch (Throwable e) {
				throw new AbnormalException(e);
			}
		}
		throw new BeanException(errorOf(propertyName));
	}

	/**
	 * 初始化
	 * 
	 * @param beanClass
	 *            被包装的类
	 */
	protected void init(Class<?> beanClass) {
		this.beanClass = beanClass;
		properties = new HashMap<String, Field>();
		List<Field> list = BeanProperty
				.getReferableNonStaticFields(beanClass);
		for (Field field : list) {
			String name = field.getName();
			if (!properties.containsKey(name)) {
				properties.put(name, field);
			}
		}
	}

	/**
	 * 错误信息
	 * 
	 * @param propertyName
	 *            属性名称
	 * @return
	 */
	private String errorOf(String propertyName) {
		String message = propertyName
				+ " property can not be found in the class "
				+ beanClass.getSimpleName();
		return message;
	}

}