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

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.ActivityRewardStatusEnum;

/**
 * Fpromotionrewardrecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fpromotionrewardrecord")
public class Fpromotionrewardrecord implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Factivity factivity;
	private Fuser fuser;
	private String frewardDetail;
	private String frewardReason;
	private Timestamp fcreateTime;
	private Timestamp flastUpdatetime;
	private int fstatus;// ActivityRewardStatusEnum
	private String fstatus_s;

	// Constructors

	/** default constructor */
	public Fpromotionrewardrecord() {
	}

	/** full constructor */
	public Fpromotionrewardrecord(Factivity factivity, Fuser fuser,
			String frewardDetail, String frewardReason, Timestamp fcreateTime) {
		this.factivity = factivity;
		this.fuser = fuser;
		this.frewardDetail = frewardDetail;
		this.frewardReason = frewardReason;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fActivity_id")
	public Factivity getFactivity() {
		return this.factivity;
	}

	public void setFactivity(Factivity factivity) {
		this.factivity = factivity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuser_id")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fRewardDetail", length = 1024)
	public String getFrewardDetail() {
		return this.frewardDetail;
	}

	public void setFrewardDetail(String frewardDetail) {
		this.frewardDetail = frewardDetail;
	}

	@Column(name = "fRewardReason", length = 1024)
	public String getFrewardReason() {
		return this.frewardReason;
	}

	public void setFrewardReason(String frewardReason) {
		this.frewardReason = frewardReason;
	}

	@Column(name = "fCreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "flastUpdatetime")
	public Timestamp getFlastUpdatetime() {
		return flastUpdatetime;
	}

	public void setFlastUpdatetime(Timestamp flastUpdatetime) {
		this.flastUpdatetime = flastUpdatetime;
	}

	@Column(name = "fstatus")
	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	
	@Transient
	public String getFstatus_s() {
		return ActivityRewardStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}
}