package com.ruizton.main.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.LendEntrustLogStatusEnum;

/*
 * 借款明细
 * */

/**
 * Flendentrustlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "flendentrustlog")
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Flendentrustlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Flendentrust fLendEntrustLendId;//借入款的订单
	private Flendentrust fLendEntrustBorrowId;//借出款的订单
	private Double famount;//借款总金额
	private Double freturnAmount;//已掉的钱
	private Double fdailyRate ;//日利率
	private Integer fstatus;//LendEntrustLogStatusEnum
	private String fstatus_s;
	private Timestamp fcreateTime;//借款时间
	private Timestamp fMustReturnTime ;//最迟还款时间
	private Timestamp flastUpdateTime;
	private Double ftodayfees;
	private Double ftotalfees;
	private int version;
	private boolean fisSend;
	private Fuser fuser;


	/** default constructor */
	public Flendentrustlog() {
	}

	/** full constructor */
	public Flendentrustlog(Flendentrust flendentrust, Integer fuser,
			Double famount, Double freturnAmount, Integer ftype,
			Integer fstatus, Timestamp fcreateTime, Timestamp flastUpdateTime,
			Set<Flendentrustfinishlog> flendentrustfinishlogs) {
		this.famount = famount;
		this.freturnAmount = freturnAmount;
		this.fstatus = fstatus;
		this.fcreateTime = fcreateTime;
		this.flastUpdateTime = flastUpdateTime;
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
	@JoinColumn(name = "fLendEntrustLendId")
	public Flendentrust getfLendEntrustLendId() {
		return fLendEntrustLendId;
	}

	public void setfLendEntrustLendId(Flendentrust fLendEntrustLendId) {
		this.fLendEntrustLendId = fLendEntrustLendId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fLendEntrustBorrowId")
	public Flendentrust getfLendEntrustBorrowId() {
		return fLendEntrustBorrowId;
	}

	public void setfLendEntrustBorrowId(Flendentrust fLendEntrustBorrowId) {
		this.fLendEntrustBorrowId = fLendEntrustBorrowId;
	}
	
	@Column(name = "famount", precision = 16, scale = 6)
	public Double getFamount() {
		return this.famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
	}

	@Column(name = "fReturnAmount", precision = 16, scale = 6)
	public Double getFreturnAmount() {
		return this.freturnAmount;
	}

	public void setFreturnAmount(Double freturnAmount) {
		this.freturnAmount = freturnAmount;
	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fcreateTime", length = 19)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "flastUpdateTime", length = 19)
	public Timestamp getFlastUpdateTime() {
		return this.flastUpdateTime;
	}

	public void setFlastUpdateTime(Timestamp flastUpdateTime) {
		this.flastUpdateTime = flastUpdateTime;
	}

	@Transient
	public String getFstatus_s() {
		return LendEntrustLogStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Column(name="fdailyRate")
	public Double getFdailyRate() {
		return fdailyRate;
	}

	public void setFdailyRate(Double fdailyRate) {
		this.fdailyRate = fdailyRate;
	}

	@Column(name="fMustReturnTime")
	public Timestamp getfMustReturnTime() {
		return fMustReturnTime;
	}

	public void setfMustReturnTime(Timestamp fMustReturnTime) {
		this.fMustReturnTime = fMustReturnTime;
	}
	
	@Column(name="ftodayfees")
	public Double getFtodayfees() {
		return ftodayfees;
	}

	public void setFtodayfees(Double ftodayfees) {
		this.ftodayfees = ftodayfees;
	}

	@Column(name="ftotalfees")
	public Double getFtotalfees() {
		return ftotalfees;
	}

	public void setFtotalfees(Double ftotalfees) {
		this.ftotalfees = ftotalfees;
	}
	
	@Column(name="fisSend")
	public boolean isFisSend() {
		return fisSend;
	}

	public void setFisSend(boolean fisSend) {
		this.fisSend = fisSend;
	}
	
	@Version
	@Column(name = "version")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	// Constructors
    @Transient
	public Fuser getFuser() {
		return fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}
}