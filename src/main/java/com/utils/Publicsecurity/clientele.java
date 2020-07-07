package com.utils.Publicsecurity;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 境内外旅客
 * @author fjn
 */
@ToString
@Data
@XmlRootElement(name="DATA")
public class clientele {

	private String FLAG;//国籍
	private String CREATE_TIME;//创建时间(当前时间)
	private String ID;//UUID
	private String OPERATETYPE;//"0" 对该旅客的操作类型 只有入住
	private String ROOM_NO;//房间号
	private String EXIT_TIME;//入住时间
	private String ENTER_TIME;//离店时间
	private String NAME;//姓名
	private String SEX;//性别
	private String NATION;//国家或者民族
	private String BIRTHDAY;//出生年月日
	private String CARD_TYPE;// 证件类型
	private String CARD_NO;
	private String REASON;//理由
	private String OCCUPATION;//职业
	private String ADDRDETAIL;
	private String CREDITCARD_TYPE;
	private String CREDITCARD_NO;
	private String PHOTO;
	//private String ADDRESS;

	
}
