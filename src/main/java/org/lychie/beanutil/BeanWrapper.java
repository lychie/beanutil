package org.lychie.beanutil;
/**
 * Bean 包装器
 * @author Lychie Fan
 */
public class BeanWrapper extends Wrapper {
	
	private Object bean;

	private BeanWrapper(Object bean) {
		super(bean.getClass());
		this.bean = bean;
	}
	
	public static BeanWrapper wrap(Object bean) {
		return new BeanWrapper(bean);
	}
	
	/**
	 * 设置属性的值
	 */
	public void setPropertyValue(String propertyName, Object propertyValue) {
		this.setPropertyValue(bean, propertyName, propertyValue);
	}
	
	/**
	 * 获取属性的值
	 */
	public <E> E getPropertyValue(String propertyName) {
		return this.getPropertyValue(bean, propertyName);
	}
	
}