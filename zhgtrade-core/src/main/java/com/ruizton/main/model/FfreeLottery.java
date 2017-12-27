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
import javax.persistence.Version;
import org.hibernate.annotations.GenericGenerator;

/**
 * FfreeLottery entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ffree_lottery")
public class FfreeLottery implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer version;
	private Fuser fuser;
	private Fvirtualcointype fvirtualcointype;
	private Double freward;
	private Boolean fiswin;
	private Timestamp fcreatetime;
	private String remark;

	// Constructors

	/** default constructor */
	public FfreeLottery() {
	}

	/** full constructor */
	public FfreeLottery(Fuser fuser, Fvirtualcointype fvirtualcointype,
			Double freward, Boolean fiswin, Timestamp fcreatetime, String remark) {
		this.fuser = fuser;
		this.fvirtualcointype = fvirtualcointype;
		this.freward = freward;
		this.fiswin = fiswin;
		this.fcreatetime = fcreatetime;
		this.remark = remark;
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

	@Version
	@Column(name = "version")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuser")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fvirtualcointype")
	public Fvirtualcointype getFvirtualcointype() {
		return this.fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@Column(name = "freward", precision = 16, scale = 6)
	public Double getFreward() {
		return this.freward;
	}

	public void setFreward(Double freward) {
		this.freward = freward;
	}

	@Column(name = "fiswin")
	public Boolean getFiswin() {
		return this.fiswin;
	}

	public void setFiswin(Boolean fiswin) {
		this.fiswin = fiswin;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "remark", length = 65535)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}