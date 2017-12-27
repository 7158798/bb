package com.ruizton.main.model;

// default package

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

import com.ruizton.main.Enum.CanPayTypeEnum;
import com.ruizton.main.Enum.GoodsStatusEnum;

/**
 * Fgoods entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fgoods")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fgoods implements java.io.Serializable {

	// Fields
	private Integer fid;
	private Integer version;
	private String fname;
	private Double fprice;
	private Double fscore;
	private Integer ftotalQty;
	private Integer flastQty;
	private Integer fsellQty;
	private String fdescription;
	private String fprovince;
	private String fcity;
	private Timestamp fcreatetime;
	private Integer fstatus;
	private String fstatus_s;
	private String fsupplierNo;
	private boolean fisVirtual;
	private String fpictureUrl;
	private Fgoodtype fgoodtype;
	private Double fmarketPrice;
	private String fqq;
	private int fcanpaytype;
	private String fcanpaytype_s;
	private Double fcanBuyQty;


	private Set<Fshoppinglog> fshoppinglogs = new HashSet<Fshoppinglog>(0);

	// Constructors

	/** default constructor */
	public Fgoods() {
	}

	/** full constructor */
	public Fgoods(String fname, Double fprice, Double fscore,
			Integer ftotalQty, Integer flastQty, String fdescription,
			String fprovince, String fcity, Timestamp fcreatetime,
			Integer fstatus, String fsupplierNo, boolean fisVirtual,
			String fpictureUrl, Set<Fshoppinglog> fshoppinglogs) {
		this.fname = fname;
		this.fprice = fprice;
		this.fscore = fscore;
		this.ftotalQty = ftotalQty;
		this.flastQty = flastQty;
		this.fdescription = fdescription;
		this.fprovince = fprovince;
		this.fcity = fcity;
		this.fcreatetime = fcreatetime;
		this.fstatus = fstatus;
		this.fsupplierNo = fsupplierNo;
		this.fisVirtual = fisVirtual;
		this.fpictureUrl = fpictureUrl;
		this.fshoppinglogs = fshoppinglogs;
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

	@Column(name = "fname", length = 100)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fprice", precision = 16, scale = 6)
	public Double getFprice() {
		return this.fprice;
	}

	public void setFprice(Double fprice) {
		this.fprice = fprice;
	}

	@Column(name = "fscore", precision = 16, scale = 6)
	public Double getFscore() {
		return this.fscore;
	}

	public void setFscore(Double fscore) {
		this.fscore = fscore;
	}

	@Column(name = "ftotalQty")
	public Integer getFtotalQty() {
		return this.ftotalQty;
	}

	public void setFtotalQty(Integer ftotalQty) {
		this.ftotalQty = ftotalQty;
	}

	@Column(name = "flastQty")
	public Integer getFlastQty() {
		return this.flastQty;
	}

	public void setFlastQty(Integer flastQty) {
		this.flastQty = flastQty;
	}

	@Column(name = "fdescription", length = 65535)
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	@Column(name = "fprovince", length = 50)
	public String getFprovince() {
		return this.fprovince;
	}

	public void setFprovince(String fprovince) {
		this.fprovince = fprovince;
	}

	@Column(name = "fcity", length = 50)
	public String getFcity() {
		return this.fcity;
	}

	public void setFcity(String fcity) {
		this.fcity = fcity;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fsupplierNo", length = 50)
	public String getFsupplierNo() {
		return this.fsupplierNo;
	}

	public void setFsupplierNo(String fsupplierNo) {
		this.fsupplierNo = fsupplierNo;
	}

	@Column(name = "fisVirtual")
	public boolean getFisVirtual() {
		return this.fisVirtual;
	}

	public void setFisVirtual(boolean fisVirtual) {
		this.fisVirtual = fisVirtual;
	}
	
	@Column(name = "fsellQty")
	public Integer getFsellQty() {
		return fsellQty;
	}

	public void setFsellQty(Integer fsellQty) {
		this.fsellQty = fsellQty;
	}

	@Column(name = "fpictureUrl", length = 100)
	public String getFpictureUrl() {
		return this.fpictureUrl;
	}

	public void setFpictureUrl(String fpictureUrl) {
		this.fpictureUrl = fpictureUrl;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fgoods")
	public Set<Fshoppinglog> getFshoppinglogs() {
		return this.fshoppinglogs;
	}

	public void setFshoppinglogs(Set<Fshoppinglog> fshoppinglogs) {
		this.fshoppinglogs = fshoppinglogs;
	}

	@Transient
	public String getFstatus_s() {
		return GoodsStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Column(name = "fmarketPrice")
	public Double getFmarketPrice() {
		return fmarketPrice;
	}

	public void setFmarketPrice(Double fmarketPrice) {
		this.fmarketPrice = fmarketPrice;
	}
	
	@Column(name = "fqq")
	public String getFqq() {
		return fqq;
	}

	public void setFqq(String fqq) {
		this.fqq = fqq;
	}
	
	@Column(name = "fcanBuyQty")
	public Double getFcanBuyQty() {
		return fcanBuyQty;
	}

	public void setFcanBuyQty(Double fcanBuyQty) {
		this.fcanBuyQty = fcanBuyQty;
	}

	@Column(name = "fcanpaytype")
	public int getFcanpaytype() {
		return fcanpaytype;
	}

	public void setFcanpaytype(int fcanpaytype) {
		this.fcanpaytype = fcanpaytype;
	}

	@Transient
	public String getFcanpaytype_s() {
		return CanPayTypeEnum.getEnumString(this.getFcanpaytype());
	}

	public void setFcanpaytype_s(String fcanpaytype_s) {
		this.fcanpaytype_s = fcanpaytype_s;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ftypeid")
	public Fgoodtype getFgoodtype() {
		return fgoodtype;
	}

	public void setFgoodtype(Fgoodtype fgoodtype) {
		this.fgoodtype = fgoodtype;
	}
}