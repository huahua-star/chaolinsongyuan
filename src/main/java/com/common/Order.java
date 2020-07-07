package com.common;

/**
 * 订单信息
 */
public class Order {
	/**
	 * 订单编号
	 */
	private String orderNo;
	private String totalAmount;

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * 
	 * 购物者在平台的账户名
	 */
	private String account;

	/**
	 * 货物配送地址
	 */
	private String address;

	/**
	 * 购物者电话号码
	 */
	private String tel;

	/**
	 * 购物者Email
	 */
	private String email;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
