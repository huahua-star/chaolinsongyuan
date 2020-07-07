package com.common;

/**
 * 响应发票信息的项目明细
 */
public class ResultInvoiceItem {
	/**
	 * 商品编码
	 */
	private String itemCode;

	/**
	 * 商品名称
	 */
	private String itemName;

	/**
	 * 商品单价
	 */
	private String price;

	/**
	 * 数量
	 */
	private String quantity;

	/**
	 * 单位
	 */
	private String uom;

	/**
	 * 税率
	 */
	private String taxRate;

	/**
	 * 税额
	 */
	private String taxAmount;

	/**
	 * 不含税金额
	 */
	private String noTaxAmount;

	/**
	 * 金额
	 */
	private String amount;

	/**
	 * 备注
	 */
	private String itemRemark;

	/**
	 * 商品IMIE号
	 */
	private String imei;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getNoTaxAmount() {
		return noTaxAmount;
	}

	public void setNoTaxAmount(String noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getItemRemark() {
		return itemRemark;
	}

	public void setItemRemark(String itemRemark) {
		this.itemRemark = itemRemark;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}
}