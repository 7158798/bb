package com.ruizton.main.model;

// default package

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.PopcornBetStatusEnum;
import com.ruizton.main.Enum.PopcornResult1Enum;
import com.ruizton.main.Enum.PopcornResult2Enum;

/**
 * Fpopcornbetlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fpopcornbetlog")
public class Fpopcornbetlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fuser fuser;//投注用户 
	private Fpopcornlog fpopcornlog;//期数
	private Fvirtualcointype fvirtualcointypeId;//投注虚拟 币类型
	private Integer fbetresult1;//下注：单双
	private String fbetresult1_s;//PopcornResult1Enum
	private Integer fbetresult2;//下注：大小
	private String fbetresult2_s;//PopcornResult2Enum
	private Integer factualResult1;//开奖结果单双
	private String factualResult1_s;
	private Integer factualResult2;//开奖结果大小
	private String factualResult2_s;
	private Timestamp fcreatetime;
	private Double fbetQty1;//下注数量
	private Double fbetQty2;//下注数量
	private Double fwinQty;//赢得数量
	private Integer fstatus;//状态PopcornBetStatusEnum
	private String fstatus_s;
	private boolean fischarge;
	private Double fallWinQty;
	private Double fallLostQty;
	private Double ffees;
	private Double factualWinQty;


	/** default constructor */
	public Fpopcornbetlog() {
	}

	/** full constructor */
	public Fpopcornbetlog(Fuser fuser, Fpopcornlog fpopcornlog,
			Integer fbetresult1, Integer fbetresult2, Integer factualResult1,
			Timestamp fcreatetime,Double fwinQty) {
		this.fuser = fuser;
		this.fpopcornlog = fpopcornlog;
		this.fbetresult1 = fbetresult1;
		this.fbetresult2 = fbetresult2;
		this.factualResult1 = factualResult1;
		this.fcreatetime = fcreatetime;
		this.fwinQty = fwinQty;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuserId")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fpopcornlogId")
	public Fpopcornlog getFpopcornlog() {
		return this.fpopcornlog;
	}

	public void setFpopcornlog(Fpopcornlog fpopcornlog) {
		this.fpopcornlog = fpopcornlog;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fvirtualcointypeId")
	public Fvirtualcointype getFvirtualcointypeId() {
		return fvirtualcointypeId;
	}

	public void setFvirtualcointypeId(Fvirtualcointype fvirtualcointypeId) {
		this.fvirtualcointypeId = fvirtualcointypeId;
	}
	
	@Column(name = "fbetresult1")
	public Integer getFbetresult1() {
		return this.fbetresult1;
	}

	public void setFbetresult1(Integer fbetresult1) {
		this.fbetresult1 = fbetresult1;
	}

	@Column(name = "fbetresult2")
	public Integer getFbetresult2() {
		return this.fbetresult2;
	}

	public void setFbetresult2(Integer fbetresult2) {
		this.fbetresult2 = fbetresult2;
	}
	
	@Column(name = "factualResult1")
	public Integer getFactualResult1() {
		return factualResult1;
	}

	public void setFactualResult1(Integer factualResult1) {
		this.factualResult1 = factualResult1;
	}

	@Column(name = "factualResult2")
	public Integer getFactualResult2() {
		return factualResult2;
	}

	public void setFactualResult2(Integer factualResult2) {
		this.factualResult2 = factualResult2;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "fbetQty2", precision = 16, scale = 6)
	public Double getFbetQty2() {
		return this.fbetQty2;
	}

	public void setFbetQty2(Double fbetQty2) {
		this.fbetQty2 = fbetQty2;
	}

	@Column(name = "fbetQty1", precision = 16, scale = 6)
	public Double getFbetQty1() {
		return this.fbetQty1;
	}

	public void setFbetQty1(Double fbetQty1) {
		this.fbetQty1 = fbetQty1;
	}

	@Column(name = "fwinQty", precision = 16, scale = 6)
	public Double getFwinQty() {
		return this.fwinQty;
	}

	public void setFwinQty(Double fwinQty) {
		this.fwinQty = fwinQty;
	}
	
	@Transient
	public String getFbetresult1_s() {
		if(this.getFbetresult1() == null) return "";
		return PopcornResult1Enum.getEnumString(this.getFbetresult1());
	}

	public void setFbetresult1_s(String fbetresult1_s) {
		this.fbetresult1_s = fbetresult1_s;
	}
	
	@Transient
	public String getFbetresult2_s() {
		if(this.getFbetresult2() == null) return "";
		return PopcornResult2Enum.getEnumString(this.getFbetresult2());
	}

	public void setFbetresult2_s(String fbetresult2_s) {
		this.fbetresult2_s = fbetresult2_s;
	}

	@Transient
	public String getFactualResult1_s() {
		if(this.getFactualResult1() == null) return "";
		return PopcornResult1Enum.getEnumString(this.getFactualResult1());
	}

	public void setFactualResult1_s(String factualResult1_s) {
		this.factualResult1_s = factualResult1_s;
	}

	@Transient
	public String getFactualResult2_s() {
		if(this.getFactualResult2() == null) return "";
		return PopcornResult2Enum.getEnumString(this.getFactualResult2());
	}

	public void setFactualResult2_s(String factualResult2_s) {
		this.factualResult2_s = factualResult2_s;
	}
	
	@Column(name = "fstatus")
	public Integer getFstatus() {
		return fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fischarge")
	public boolean isFischarge() {
		return fischarge;
	}

	public void setFischarge(boolean fischarge) {
		this.fischarge = fischarge;
	}

	@Column(name = "fallWinQty")
	public Double getFallWinQty() {
		return fallWinQty;
	}

	public void setFallWinQty(Double fallWinQty) {
		this.fallWinQty = fallWinQty;
	}

	@Column(name = "fallLostQty")
	public Double getFallLostQty() {
		return fallLostQty;
	}

	public void setFallLostQty(Double fallLostQty) {
		this.fallLostQty = fallLostQty;
	}

	@Column(name = "ffees")
	public Double getFfees() {
		return ffees;
	}

	public void setFfees(Double ffees) {
		this.ffees = ffees;
	}

	@Column(name = "factualWinQty")
	public Double getFactualWinQty() {
		return factualWinQty;
	}

	public void setFactualWinQty(Double factualWinQty) {
		this.factualWinQty = factualWinQty;
	}
	
	@Transient
	public String getFstatus_s() {
		return PopcornBetStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

}