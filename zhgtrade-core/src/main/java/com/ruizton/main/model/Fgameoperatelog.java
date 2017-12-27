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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.GameOperateLogTypeEnum;

/**
 * Fgameoperatelog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fgameoperatelog")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fgameoperatelog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fgamerule fgamerule;
	private Fgame fgame;
	private Integer ftype;//GameOperateLogTypeEnum
	private String ftype_s;
	private Double fqty;
	private Timestamp fcreateTime;
	private Fuser fuser;

	// Constructors

	/** default constructor */
	public Fgameoperatelog() {
	}

	/** full constructor */
	public Fgameoperatelog(Fgamerule fgamerule, Fgame fgame, Integer ftype,
			Double fqty, Timestamp fcreateTime) {
		this.fgamerule = fgamerule;
		this.fgame = fgame;
		this.ftype = ftype;
		this.fqty = fqty;
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
	@JoinColumn(name = "fgameRuleid")
	public Fgamerule getFgamerule() {
		return this.fgamerule;
	}

	public void setFgamerule(Fgamerule fgamerule) {
		this.fgamerule = fgamerule;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fgameid")
	public Fgame getFgame() {
		return this.fgame;
	}

	public void setFgame(Fgame fgame) {
		this.fgame = fgame;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fqty", precision = 16, scale = 6)
	public Double getFqty() {
		return this.fqty;
	}

	public void setFqty(Double fqty) {
		this.fqty = fqty;
	}

	@Column(name = "fcreateTime", length = 19)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}
	
	@Transient
	public String getFtype_s() {
		return GameOperateLogTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuserId")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

}