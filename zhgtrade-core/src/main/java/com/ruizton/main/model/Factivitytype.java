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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.ActivityTypeTypeEnum;

/**
 * Factivitytype entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "factivitytype")
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
// ActivityTypeEnum,和具体虚拟币类型相关的，虚拟币充值、交易等，枚举没有，需要通过数据库查找,对应数据库ID
public class Factivitytype implements java.io.Serializable {

	// Fields

	private Integer fid;
	private String fname;
	private String fdescription;
	private int type;// ActivityTypeTypeEnum,和虚拟币相关的才需要
	private String type_s;
	private Fvirtualcointype fvirtualCoinType;

	private Timestamp fcreateTime;
	private Timestamp flastUpdateTime;
	private Set<Factivity> factivities = new HashSet<Factivity>(0);

	// Constructors

	/** default constructor */
	public Factivitytype() {
	}

	/** full constructor */
	public Factivitytype(String fname, String fdescription,
			Timestamp fcreateTime, Timestamp flastUpdateTime,
			Set<Factivity> factivities) {
		this.fname = fname;
		this.fdescription = fdescription;
		this.fcreateTime = fcreateTime;
		this.flastUpdateTime = flastUpdateTime;
		this.factivities = factivities;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy="native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	@Column(name = "fName", length = 128)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fDescription", length = 256)
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	@Column(name = "fCreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fLastUpdateTime", length = 0)
	public Timestamp getFlastUpdateTime() {
		return this.flastUpdateTime;
	}

	public void setFlastUpdateTime(Timestamp flastUpdateTime) {
		this.flastUpdateTime = flastUpdateTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "factivitytype")
	public Set<Factivity> getFactivities() {
		return this.factivities;
	}

	public void setFactivities(Set<Factivity> factivities) {
		this.factivities = factivities;
	}

	@Column(name = "ftype")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fvirtualCoinType")
	public Fvirtualcointype getFvirtualCoinType() {
		return fvirtualCoinType;
	}

	public void setFvirtualCoinType(Fvirtualcointype fvirtualCoinType) {
		this.fvirtualCoinType = fvirtualCoinType;
	}
	
	@Transient
	public String getType_s() {
		return ActivityTypeTypeEnum.getEnumString(this.getType());
	} 

	public void setType_s(String type_s) {
		this.type_s = type_s;
	}

}