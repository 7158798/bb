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

import com.ruizton.main.Enum.HedgingTypeEnum;
import com.ruizton.main.Enum.HedginguserlogStatusEnum;

/**
 * Fhedginguserlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fhedginguserlog")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fhedginguserlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer version;
	private Fuser fuser;
	private Fhedginglog fhedginglog;
	private Double fqty;
	private Integer fstatus;
	private String fstatus_s;
	private Integer ftaketype;
	private String ftaketype_s;
	private Timestamp fcreatetime;
	private Double fwinqty;

	// Constructors

	/** default constructor */
	public Fhedginguserlog() {
	}

	/** full constructor */
	public Fhedginguserlog(Fuser fuser, Fhedginglog fhedginglog, Double fqty,
			Integer fstatus, Integer ftaketype, Timestamp fcreatetime,
			Double fwinqty) {
		this.fuser = fuser;
		this.fhedginglog = fhedginglog;
		this.fqty = fqty;
		this.fstatus = fstatus;
		this.ftaketype = ftaketype;
		this.fcreatetime = fcreatetime;
		this.fwinqty = fwinqty;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fuserid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fhedginglogid")
	public Fhedginglog getFhedginglog() {
		return this.fhedginglog;
	}

	public void setFhedginglog(Fhedginglog fhedginglog) {
		this.fhedginglog = fhedginglog;
	}

	@Column(name = "fqty", precision = 16, scale = 6)
	public Double getFqty() {
		return this.fqty;
	}

	public void setFqty(Double fqty) {
		this.fqty = fqty;
	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "ftaketype")
	public Integer getFtaketype() {
		return this.ftaketype;
	}

	public void setFtaketype(Integer ftaketype) {
		this.ftaketype = ftaketype;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "fwinqty", precision = 16, scale = 6)
	public Double getFwinqty() {
		return this.fwinqty;
	}

	public void setFwinqty(Double fwinqty) {
		this.fwinqty = fwinqty;
	}
	
	@Transient
	public String getFstatus_s() {
		return HedginguserlogStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Transient
	public String getFtaketype_s() {
		return HedgingTypeEnum.getEnumString(this.getFtaketype());
	}

	public void setFtaketype_s(String ftaketype_s) {
		this.ftaketype_s = ftaketype_s;
	}


}