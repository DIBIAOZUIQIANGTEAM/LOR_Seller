package com.wsns.lor.seller.entity;

import java.io.Serializable;

/**
 * 商家实体类
 * @author JR
 *
 */
public class Seller implements Serializable{
	private String account;
	private String passwordHash;
	private String avatar;//头像本地相对地址
	private String name;// 店名
	private Double coin;//余额
	private String email;//邮箱，暂时没用到
	private String address;// 商家地址
	private String hotline;// 热线电话
	private Double service;// 人工服务费（每次交易最少收取费用）
	private String worktime;// 营业时间
	private String notice;// 商家公告
	private Integer turnover;// 交易量
	//private Integer tradeTypes;// 服务方式：1上门+邮寄 2邮寄 ！！！这个可能不保留 只做上门
	private String repairsTypes;// 维修类型：如空调，电脑
//	private String state;// 店铺状态 1在线状态 ,2离线状态
	private String distance;// 距离（不在数据库插入此字段）
	private String comment;// 评价数

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getCoin() {
		return coin;
	}

	public void setCoin(Double coin) {
		this.coin = coin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public Double getService() {
		return service;
	}

	public void setService(Double service) {
		this.service = service;
	}

	public String getWorktime() {
		return worktime;
	}

	public void setWorktime(String worktime) {
		this.worktime = worktime;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public Integer getTurnover() {
		return turnover;
	}

	public void setTurnover(Integer turnover) {
		this.turnover = turnover;
	}

	public String getRepairsTypes() {
		return repairsTypes;
	}

	public void setRepairsTypes(String repairsTypes) {
		this.repairsTypes = repairsTypes;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
