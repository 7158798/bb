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

import com.ruizton.main.Enum.LendflowlogTypeEnum;
import com.ruizton.main.Enum.ReturnTypeEnum;

/**
 * Flendflowlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "flendflowlog")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Flendflowlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fuser fuser;
	private Integer ftype;
	private String ftype_s;
	private boolean fiscny;
	private int version;
	private int freturnType;
	private String freturnType_s;
	private Double famount;
	private Timestamp fcreatetime;

	// Constructors

	/** default constructor */
	public Flendflowlog() {
	}

	/** full constructor */
	public Flendflowlog(Fuser fuser, Integer ftype,
			Double famount, Timestamp fcreatetime) {
		this.fuser = fuser;
		this.ftype = ftype;
		this.famount = famount;
		this.fcreatetime = fcreatetime;
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
	@JoinColumn(name = "fuserid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fiscny")
	public boolean isFiscny() {
		return fiscny;
	}

	public void setFiscny(boolean fiscny) {
		this.fiscny = fiscny;
	}

	@Column(name = "famount", precision = 16, scale = 6)
	public Double getFamount() {
		return this.famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}
	
	@Transient
	public String getFtype_s() {
		return LendflowlogTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}
	
	@Version
	@Column(name = "version")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Column(name="freturnType")
	public int getFreturnType() {
		return freturnType;
	}

	public void setFreturnType(int freturnType) {
		this.freturnType = freturnType;
	}

	@Transient
	public String getFreturnType_s() {
		return ReturnTypeEnum.getEnumString(this.getFreturnType());
	}

	public void setFreturnType_s(String freturnType_s) {
		this.freturnType_s = freturnType_s;
	}
}