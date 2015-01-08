package org.lychie.beanutil;

/**
 * Bean 包装器
 * 
 * @author Lychie Fan
 */
public class BeanWrapper extends Wrapper {

	private Object bean;

	private BeanWrapper(Object bean) {
		super(bean.getClass());
		this.bean = bean;
	}

	/**
	 * 包装Bean对象
	 * 
	 * @param bean
	 *            被包装的对象
	 * @return BeanWrapper
	 */
	public static BeanWrapper wrap(Object bean) {
		return new BeanWrapper(bean);
	}

	/**
	 * 设置属性的值
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param propertyValue
	 *            属性的值
	 */
	public void setPropertyValue(String propertyName, Object propertyValue) {
		this.setPropertyValue(bean, propertyName, propertyValue);
	}

	/**
	 * 获取属性的值
	 * 
	 * @param propertyName
	 *            属性名称
	 * @return
	 */
	public <E> E getPropertyValue(String propertyName) {
		return this.getPropertyValue(bean, propertyName);
	}

}