package com.common;

import java.util.Date;

/**
 * Created by Amhong on 2015-7-11.
 */
public class ResponseData {

	// 操作流水号
	private String serialNo;
	// 响应发送时间
	private Date postTime;
	// 处理结果代码
	private int code;
	// 处理结果消息
	private String message;
	// 订单信息
	private Order order;
	// 发票信息
	private ResultInvoice[] invoices;

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public ResultInvoice[] getInvoices() {
		return invoices;
	}

	public void setInvoices(ResultInvoice[] invoices) {
		this.invoices = invoices;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
