package com.ruizton.main.model;

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
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.LendEntrustFinishLogStatusEnum;
import com.ruizton.main.Enum.LendEntrustFinishLogTypeEnum;
import com.ruizton.main.Enum.ReturnTypeEnum;

/*
 * 还款记录表
 * */

/**
 * Flendentrustfinishlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "flendentrustfinishlog")
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Flendentrustfinishlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Flendentrustlog flendentrustlog;
	private Double famount;//应还款
	private Double ffees ;//手续费
	private Timestamp fcreateTime;
	private int version;
	private boolean fischarge;//是否结算
	private boolean fissend;//是否处理放款
	private int freturnType;
	private String freturnType_s;
	private Double fowerFees;
	private Double fintrolFees;
	private Double fleaderFees;


	/** default constructor */
	public Flendentrustfinishlog() {
	}

	/** full constructor */
	public Flendentrustfinishlog(Flendentrustlog flendentrustlog, Fuser fuser,
			Double famount, Integer ftype, Integer fstatus,
			Timestamp fcreateTime) {
		this.flendentrustlog = flendentrustlog;
		this.famount = famount;
		this.fcreateTime = fcreateTime;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "flendEntrustId")
	public Flendentrustlog getFlendentrustlog() {
		return this.flendentrustlog;
	}

	public void setFlendentrustlog(Flendentrustlog flendentrustlog) {
		this.flendentrustlog = flendentrustlog;
	}

	@Column(name = "fAmount", precision = 16, scale = 6)
	public Double getFamount() {
		return this.famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
	}


	@Column(name = "fCreateTime", length = 19)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name="ffees")
	public Double getFfees() {
		return ffees;
	}

	public void setFfees(Double ffees) {
		this.ffees = ffees;
	}

	
	@Version
	@Column(name = "version")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Column(name="fischarge")
	public boolean isFischarge() {
		return fischarge;
	}

	public void setFischarge(boolean fischarge) {
		this.fischarge = fischarge;
	}

	@Column(name="fissend")
	public boolean isFissend() {
		return fissend;
	}

	public void setFissend(boolean fissend) {
		this.fissend = fissend;
	}
	
	@Column(name="freturnType")
	public int getFreturnType() {
		return freturnType;
	}

	public void setFreturnType(int freturnType) {
		this.freturnType = freturnType;
	}

	@Column(name="fowerFees")
	public Double getFowerFees() {
		return fowerFees;
	}

	public void setFowerFees(Double fowerFees) {
		this.fowerFees = fowerFees;
	}
	
	@Column(name="fintrolFees")
	public Double getFintrolFees() {
		return fintrolFees;
	}

	public void setFintrolFees(Double fintrolFees) {
		this.fintrolFees = fintrolFees;
	}

	@Column(name="fleaderFees")
	public Double getFleaderFees() {
		return fleaderFees;
	}

	public void setFleaderFees(Double fleaderFees) {
		this.fleaderFees = fleaderFees;
	}
	
	@Transient
	public String getFreturnType_s() {
		return ReturnTypeEnum.getEnumString(this.getFreturnType());
	}

	public void setFreturnType_s(String freturnType_s) {
		this.freturnType_s = freturnType_s;
	}
}