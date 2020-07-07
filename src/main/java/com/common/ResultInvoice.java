package com.common;

import java.io.Serializable;
import java.util.List;

/**
 * 响应发票信息
 */
public class ResultInvoice implements Serializable {
	/**
	 * 发票状态代码
	 */
	public interface Status {
		/**
		 * 正常
		 */
		String NORMAL = "1";
		/**
		 * 红冲
		 */
		String RED = "3";
		/**
		 * 被红冲
		 */
		String REDED = "4";
	}

	/**
	 * 电子发票编号
	 */
	private String code;

	/**
	 * 付款方纳税人识别号
	 */
	private String customerCode;

	/**
	 * 付款方名称
	 */
	private String customerName;

	/**
	 * 收款方纳税人识别号
	 */
	private String taxpayerCode;

	/**
	 * 收款方名称
	 */
	private String taxpayerName;

	/**
	 * DownloadUrl 下载URL：下载XML格式文件便于导入电商平台或财务系统中
	 */
	private String downloadUrl;

	/**
	 * 查看URL
	 */
	private String viewUrl;

	/**
	 * 未签名PDF下载URL
	 */
	private String pdfUnsignedUrl;

	/**
	 * 开票人
	 */
	private String drawer;

	/**
	 * 税控码
	 */
	private String fiscalCode;

	/**
	 * 生成时间
	 */
	private String generateTime;

	/**
	 * 发票状态 正常：1 红冲：3 被红冲：4
	 */
	private String status;

	/**
	 * 不含税金额
	 */
	private String noTaxAmount;

	/**
	 * 税额
	 */
	private String taxAmount;

	/**
	 * 发票总金额
	 */
	private String amount;

	/**
	 * 作废/红冲原因
	 */
	private String validReason;

	/**
	 * 作废/红冲时间
	 */
	private String validTime;

	/**
	 * 关联发票编号
	 */
	private String relatedCode;

	/**
	 * 项目明细
	 */
	private List<ResultInvoiceItem> items;

	/**
	 * 备注
	 */
	private String remark;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getTaxpayerCode() {
		return taxpayerCode;
	}

	public void setTaxpayerCode(String taxpayerCode) {
		this.taxpayerCode = taxpayerCode;
	}

	public String getTaxpayerName() {
		return taxpayerName;
	}

	public void setTaxpayerName(String taxpayerName) {
		this.taxpayerName = taxpayerName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public String getPdfUnsignedUrl() {
		return pdfUnsignedUrl;
	}

	public void setPdfUnsignedUrl(String pdfUnsignedUrl) {
		this.pdfUnsignedUrl = pdfUnsignedUrl;
	}

	public String getDrawer() {
		return drawer;
	}

	public void setDrawer(String drawer) {
		this.drawer = drawer;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public String getGenerateTime() {
		return generateTime;
	}

	public void setGenerateTime(String generateTime) {
		this.generateTime = generateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNoTaxAmount() {
		return noTaxAmount;
	}

	public void setNoTaxAmount(String noTaxAmount) {
		this.noTaxAmount = noTaxAmount;
	}

	public String getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(String taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getValidReason() {
		return validReason;
	}

	public void setValidReason(String validReason) {
		this.validReason = validReason;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getRelatedCode() {
		return relatedCode;
	}

	public void setRelatedCode(String relatedCode) {
		this.relatedCode = relatedCode;
	}

	public List<ResultInvoiceItem> getItems() {
		return items;
	}

	public void setItems(List<ResultInvoiceItem> items) {
		this.items = items;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
