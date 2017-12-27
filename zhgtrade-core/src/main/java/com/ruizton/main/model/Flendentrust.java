package com.ruizton.main.model;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.CnyOrCoinEnum;
import com.ruizton.main.Enum.LendEntrustStatus1Enum;
import com.ruizton.main.Enum.LendEntrustStatus2Enum;
import com.ruizton.main.Enum.LendEntrustStatus3Enum;
import com.ruizton.main.Enum.LendEntrustSubTypeEnum;
import com.ruizton.main.Enum.LendEntrustTypeEnum;
import com.ruizton.main.Enum.ReturnTypeEnum;

/*
 * 借款记录
 * */

/**
 * Flendentrust entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "flendentrust")
public class Flendentrust implements java.io.Serializable {

	// Fields

	private int fid;
	private Fuser fuser;
	private Fvirtualcointype fvirtualcointype;
	private int fcnyOrCoin;//接人民币还是虚拟币//CnyOrCoinEnum
	private String fcnyOrCoin_s;
	private double famount;//委托总金额
	private double fsuccessAmount;//已成交金额
	private double fdailyRate;//日利率
	private int ftimeLength;//借款、放款天数
	private boolean fisBuyInsurance;// 是否购买保险，借出有用
	private int ftype;//借入、借出//LendEntrustTypeEnum
	private String ftype_s;
	private int fsubType;//预约，马上借//LendEntrustSubTypeEnum
	private String fsubType_s;
	private int fstatus1;//LendEntrustStatus1Enum//成交状态
	private int fstatus2;//LendEntrustStatus2Enum//还款状态
	private String fstatus1_s;
	private String fstatus2_s;

	private Timestamp fcreateTime;
	private Timestamp flastUpdateTime;
	private int version;
	private int freturnType;
	private String freturnType_s;
	private Integer flendentrustlogId;

	// Constructors

	/** default constructor */
	public Flendentrust() {
	}

	/** full constructor */
	public Flendentrust(Fuser fuser, Fvirtualcointype fvirtualcointype,
			int fcnyOrCoin, long famount, double fsuccessAmount,
			double fdailyRate, int ftimeLength, boolean fisBuyInsurance,
			int ftype, int fsubType, int fstatus,
			Timestamp fcreateTime, Timestamp flastUpdateTime,
			Set<Flendentrustlog> flendentrustlogs) {
		this.fuser = fuser;
		this.fvirtualcointype = fvirtualcointype;
		this.fcnyOrCoin = fcnyOrCoin;
		this.famount = famount;
		this.fsuccessAmount = fsuccessAmount;
		this.fdailyRate = fdailyRate;
		this.ftimeLength = ftimeLength;
		this.fisBuyInsurance = fisBuyInsurance;
		this.ftype = ftype;
		this.fsubType = fsubType;
		this.fcreateTime = fcreateTime;
		this.flastUpdateTime = flastUpdateTime;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public int getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@Version
	@Column(name = "version")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuserid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FvirtualCointypeId")
	public Fvirtualcointype getFvirtualcointype() {
		return this.fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@Column(name = "fcnyOrCoin")
	public int getFcnyOrCoin() {
		return this.fcnyOrCoin;
	}

	public void setFcnyOrCoin(int fcnyOrCoin) {
		this.fcnyOrCoin = fcnyOrCoin;
	}

	@Column(name = "fAmount")
	public double getFamount() {
		return this.famount;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	@Column(name = "fSuccessAmount", precision = 16, scale = 6)
	public double getFsuccessAmount() {
		return this.fsuccessAmount;
	}

	public void setFsuccessAmount(double fsuccessAmount) {
		this.fsuccessAmount = fsuccessAmount;
	}

	@Column(name = "fDailyRate")
	public double getFdailyRate() {
		return this.fdailyRate;
	}

	public void setFdailyRate(double fdailyRate) {
		this.fdailyRate = fdailyRate;
	}

	@Column(name = "fTimeLength")
	public int getFtimeLength() {
		return this.ftimeLength;
	}

	public void setFtimeLength(int ftimeLength) {
		this.ftimeLength = ftimeLength;
	}

	@Column(name = "fisBuyInsurance")
	public boolean getFisBuyInsurance() {
		return this.fisBuyInsurance;
	}

	public void setFisBuyInsurance(boolean fisBuyInsurance) {
		this.fisBuyInsurance = fisBuyInsurance;
	}

	@Column(name = "ftype")
	public int getFtype() {
		return this.ftype;
	}

	public void setFtype(int ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fsubType")
	public int getFsubType() {
		return this.fsubType;
	}

	public void setFsubType(int fsubType) {
		this.fsubType = fsubType;
	}

	@Column(name = "fCreateTime", length = 19)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fLastUpdateTime", length = 19)
	public Timestamp getFlastUpdateTime() {
		return this.flastUpdateTime;
	}

	public void setFlastUpdateTime(Timestamp flastUpdateTime) {
		this.flastUpdateTime = flastUpdateTime;
	}

	@Transient
	public String getFtype_s() {
		return LendEntrustTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}

	@Transient
	public String getFsubType_s() {
		return LendEntrustSubTypeEnum.getEnumString(this.getFsubType());
	}

	public void setFsubType_s(String fsubType_s) {
		this.fsubType_s = fsubType_s;
	}

	@Column(name="fstatus1")
	public int getFstatus1() {
		return fstatus1;
	}

	public void setFstatus1(int fstatus1) {
		this.fstatus1 = fstatus1;
	}

	@Column(name="fstatus2")
	public int getFstatus2() {
		return fstatus2;
	}

	public void setFstatus2(int fstatus2) {
		this.fstatus2 = fstatus2;
	}

	@Transient
	public String getFstatus1_s() {
		return LendEntrustStatus1Enum.getEnumString(this.getFstatus1()) ;
	}

	public void setFstatus1_s(String fstatus1_s) {
		this.fstatus1_s = fstatus1_s;
	}

	@Transient
	public String getFstatus2_s() {
		if(this.getFtype() == LendEntrustTypeEnum.BORROW){
			return LendEntrustStatus2Enum.getEnumString(this.getFstatus2()) ;
		}else{
			return LendEntrustStatus3Enum.getEnumString(this.getFstatus2()) ;
		}
	}

	public void setFstatus2_s(String fstatus2_s) {
		this.fstatus2_s = fstatus2_s;
	}

	@Transient
	public String getFcnyOrCoin_s() {
		return CnyOrCoinEnum.getEnumString(this.getFcnyOrCoin());
	}

	public void setFcnyOrCoin_s(String fcnyOrCoin_s) {
		this.fcnyOrCoin_s = fcnyOrCoin_s;
	}
	
	@Column(name="freturnType")
	public int getFreturnType() {
		return freturnType;
	}

	public void setFreturnType(int freturnType) {
		this.freturnType = freturnType;
	}

	@Column(name="flendentrustlogId")
	public Integer getFlendentrustlogId() {
		return flendentrustlogId;
	}

	public void setFlendentrustlogId(Integer flendentrustlogId) {
		this.flendentrustlogId = flendentrustlogId;
	}
	
	@Transient
	public String getFreturnType_s() {
		return ReturnTypeEnum.getEnumString(this.getFreturnType());
	}

	public void setFreturnType_s(String freturnType_s) {
		this.freturnType_s = freturnType_s;
	}
}