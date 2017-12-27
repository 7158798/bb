package com.ruizton.main.model;

// default package

import java.math.BigDecimal;
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
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fgame entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fgame")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fgame implements java.io.Serializable {

	// Fields

	private int fid;
	private String fdescription;// 游戏简介
	private BigDecimal fminHarvestTime;// 最小种豆时间
	private BigDecimal fgrassTime;//变草时长
	private Timestamp flastUpdateTime;// 最后修改时间
	private int version ;
	private Fvirtualcointype fvirtualcointype;//开启消耗类型
	private Double fqty;//开启消耗数量
	
	private Fvirtualcointype ffrozenvirtualcointype;//开启消耗类型
	private Double ffrozenqty;//开启消耗数量
	
	private Fvirtualcointype fgrassvirtualcointype;//除草消耗类型
	private Double fgrassqty;//除草消耗数量


	private int fdays;
	// Constructors



	/** default constructor */
	public Fgame() {
	}

	/** full constructor */
	public Fgame(Fadmin fadmin, String fname, String fdescription,
			String fnotice, String fmusicUrl, BigDecimal fminHarvestTime,
			Timestamp flastUpdateTime, Set<Fgamelog> fgamelogs) {
		this.fdescription = fdescription;
		this.fminHarvestTime = fminHarvestTime;
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

	@Column(name = "fdescription", length = 500)
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	@Column(name = "fminHarvestTime", precision = 2, scale = 1)
	public BigDecimal getFminHarvestTime() {
		return this.fminHarvestTime;
	}

	public void setFminHarvestTime(BigDecimal fminHarvestTime) {
		this.fminHarvestTime = fminHarvestTime;
	}
	
	@Column(name = "fgrassTime", precision = 3, scale = 1)
	public BigDecimal getFgrassTime() {
		return this.fgrassTime;
	}

	public void setFgrassTime(BigDecimal fgrassTime) {
		this.fgrassTime = fgrassTime;
	}
	

	@Column(name = "flastUpdateTime", length = 19)
	public Timestamp getFlastUpdateTime() {
		return this.flastUpdateTime;
	}

	public void setFlastUpdateTime(Timestamp flastUpdateTime) {
		this.flastUpdateTime = flastUpdateTime;
	}
	
	@Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fopenvid")
	public Fvirtualcointype getFvirtualcointype() {
		return this.fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fopenfrozenvid")
	public Fvirtualcointype getFfrozenvirtualcointype() {
		return ffrozenvirtualcointype;
	}

	public void setFfrozenvirtualcointype(Fvirtualcointype ffrozenvirtualcointype) {
		this.ffrozenvirtualcointype = ffrozenvirtualcointype;
	}
	
	@Column(name = "ffrozenqty", precision = 16, scale = 6)
	public Double getFfrozenqty() {
		return ffrozenqty;
	}

	public void setFfrozenqty(Double ffrozenqty) {
		this.ffrozenqty = ffrozenqty;
	}

	@Column(name = "fqty", precision = 16, scale = 6)
	public Double getFqty() {
		return this.fqty;
	}

	public void setFqty(Double fqty) {
		this.fqty = fqty;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fgrassvid")
	public Fvirtualcointype getFgrassvirtualcointype() {
		return fgrassvirtualcointype;
	}

	public void setFgrassvirtualcointype(Fvirtualcointype fgrassvirtualcointype) {
		this.fgrassvirtualcointype = fgrassvirtualcointype;
	}

	@Column(name = "fgrassqty", precision = 16, scale = 6)
	public Double getFgrassqty() {
		return fgrassqty;
	}

	public void setFgrassqty(Double fgrassqty) {
		this.fgrassqty = fgrassqty;
	}
	
	@Column(name = "fdays")
	public int getFdays() {
		return fdays;
	}

	public void setFdays(int fdays) {
		this.fdays = fdays;
	}

}