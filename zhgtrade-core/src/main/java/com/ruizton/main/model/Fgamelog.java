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
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.GameLogStatusEnum;
import com.ruizton.main.Enum.GameTypeEnum;

/**
 * Fgamelog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fgamelog")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fgamelog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fgamerule fgamerule;
	private Fuser fuser;
	private Fgame fgame;
	private Timestamp fstartTime;
	private Timestamp factualEndTime;
	private Timestamp fplanEndTime;
	private Double fplanHarvestQty;
	private Double factualHarvestQty;
	private Timestamp fcreateTime;
	private int fstatus ;//GameLogStatusEnum
	private String fstatus_s ;
	private int version ;
	private boolean fisOut;
	private int ftype;
	private String ftype_s;//种植类型

	/** default constructor */
	public Fgamelog() {
	}

	/** full constructor */
	public Fgamelog(Fgamerule fgamerule, Fuser fuser, Fgame fgame,
			Timestamp fstartTime, Timestamp factualEndTime,
			Timestamp fplanEndTime, Double fplanHarvestQty,
			Double factualHarvestQty, Timestamp fcreateTime) {
		this.fgamerule = fgamerule;
		this.fuser = fuser;
		this.fgame = fgame;
		this.fstartTime = fstartTime;
		this.factualEndTime = factualEndTime;
		this.fplanEndTime = fplanEndTime;
		this.fplanHarvestQty = fplanHarvestQty;
		this.factualHarvestQty = factualHarvestQty;
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
	@JoinColumn(name = "fgameRuleId")
	public Fgamerule getFgamerule() {
		return this.fgamerule;
	}

	public void setFgamerule(Fgamerule fgamerule) {
		this.fgamerule = fgamerule;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fuserId")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fgameId")
	public Fgame getFgame() {
		return this.fgame;
	}

	public void setFgame(Fgame fgame) {
		this.fgame = fgame;
	}

	@Column(name = "fstartTime", length = 19)
	public Timestamp getFstartTime() {
		return this.fstartTime;
	}

	public void setFstartTime(Timestamp fstartTime) {
		this.fstartTime = fstartTime;
	}

	@Column(name = "factualEndTime", length = 19)
	public Timestamp getFactualEndTime() {
		return this.factualEndTime;
	}

	public void setFactualEndTime(Timestamp factualEndTime) {
		this.factualEndTime = factualEndTime;
	}

	@Column(name = "fplanEndTime", length = 19)
	public Timestamp getFplanEndTime() {
		return this.fplanEndTime;
	}

	public void setFplanEndTime(Timestamp fplanEndTime) {
		this.fplanEndTime = fplanEndTime;
	}

	@Column(name = "fplanHarvestQty", precision = 16, scale = 6)
	public Double getFplanHarvestQty() {
		return this.fplanHarvestQty;
	}

	public void setFplanHarvestQty(Double fplanHarvestQty) {
		this.fplanHarvestQty = fplanHarvestQty;
	}

	@Column(name = "factualHarvestQty", precision = 16, scale = 6)
	public Double getFactualHarvestQty() {
		return this.factualHarvestQty;
	}

	public void setFactualHarvestQty(Double factualHarvestQty) {
		this.factualHarvestQty = factualHarvestQty;
	}

	@Column(name = "fcreateTime", length = 19)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}
	
	@Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name="fstatus")
	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	@Transient
	public String getFstatus_s() {
		int status = this.getFstatus() ;
		return GameLogStatusEnum.getEnumString(status) ;
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Column(name="fisOut")
	public boolean isFisOut() {
		return fisOut;
	}

	public void setFisOut(boolean fisOut) {
		this.fisOut = fisOut;
	}
	
	@Column(name = "ftype")
	public int getFtype() {
		return ftype;
	}

	public void setFtype(int ftype) {
		this.ftype = ftype;
	}

	@Transient
	public String getFtype_s() {
		return GameTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}
	
}