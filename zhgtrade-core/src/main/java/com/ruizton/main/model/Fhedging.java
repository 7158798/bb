package com.ruizton.main.model;

// default package

import java.sql.Time;
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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fhedging entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fhedging", uniqueConstraints = @UniqueConstraint(columnNames = "fvid1"))
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fhedging implements java.io.Serializable {

	// Fields
	private Integer fid;
	private Integer version;
	private Fvirtualcointype fvirtualcointypeByFvid2;//投注
	private Fvirtualcointype fvirtualcointypeByFid;//对冲
	private Double fminqty;
	private Double fmaxqty;
	private Time fstarttime;
	private Time fendtime;
	private Double frate1;
	private Double frate2;
	private Double frate3;
	private Double fupthreshold;
	private Double fdownthreshold;
	private Time fstartpricetime;
	private Time fendpricetime;
	private Timestamp fcreatetime;
	private String fpriceurl;
	private Set<Fhedginglog> fhedginglogs = new HashSet<Fhedginglog>(0);

	// Constructors

	/** default constructor */
	public Fhedging() {
	}

	/** full constructor */
	public Fhedging(Fvirtualcointype fvirtualcointypeByFvid2,
			Fvirtualcointype fvirtualcointypeByFid, Double fminqty,
			Double fmaxqty, Timestamp fstarttime, Timestamp fendtime,
			Double frate1, Double frate2, Double frate3,
			Double fupthreshold,Double fdownthreshold,
			Timestamp fstartpricetime, Timestamp fendpricetime,
			Timestamp fcreatetime, String fpriceurl,
			Set<Fhedginglog> fhedginglogs) {
		this.fvirtualcointypeByFvid2 = fvirtualcointypeByFvid2;
		this.fvirtualcointypeByFid = fvirtualcointypeByFid;
		this.fminqty = fminqty;
		this.fmaxqty = fmaxqty;
		this.frate1 = frate1;
		this.frate2 = frate2;
		this.frate3 = frate3;
		this.fupthreshold = fupthreshold;
		this.fdownthreshold = fdownthreshold;
		this.fcreatetime = fcreatetime;
		this.fpriceurl = fpriceurl;
		this.fhedginglogs = fhedginglogs;
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
	@JoinColumn(name = "fvid2")
	public Fvirtualcointype getFvirtualcointypeByFvid2() {
		return this.fvirtualcointypeByFvid2;
	}

	public void setFvirtualcointypeByFvid2(
			Fvirtualcointype fvirtualcointypeByFvid2) {
		this.fvirtualcointypeByFvid2 = fvirtualcointypeByFvid2;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fvid1")
	public Fvirtualcointype getFvirtualcointypeByFid() {
		return this.fvirtualcointypeByFid;
	}

	public void setFvirtualcointypeByFid(Fvirtualcointype fvirtualcointypeByFid) {
		this.fvirtualcointypeByFid = fvirtualcointypeByFid;
	}

	@Column(name = "fminqty", precision = 16, scale = 6)
	public Double getFminqty() {
		return this.fminqty;
	}

	public void setFminqty(Double fminqty) {
		this.fminqty = fminqty;
	}

	@Column(name = "fmaxqty", precision = 16, scale = 6)
	public Double getFmaxqty() {
		return this.fmaxqty;
	}

	public void setFmaxqty(Double fmaxqty) {
		this.fmaxqty = fmaxqty;
	}

	@Column(name = "fstarttime", length = 0)
	public Time getFstarttime() {
		return this.fstarttime;
	}

	public void setFstarttime(Time fstarttime) {
		this.fstarttime = fstarttime;
	}

	@Column(name = "fendtime", length = 0)
	public Time getFendtime() {
		return this.fendtime;
	}

	public void setFendtime(Time fendtime) {
		this.fendtime = fendtime;
	}

	@Column(name = "frate1", precision = 16, scale = 6)
	public Double getFrate1() {
		return this.frate1;
	}

	public void setFrate1(Double frate1) {
		this.frate1 = frate1;
	}

	@Column(name = "frate2", precision = 16, scale = 6)
	public Double getFrate2() {
		return this.frate2;
	}

	public void setFrate2(Double frate2) {
		this.frate2 = frate2;
	}

	@Column(name = "frate3", precision = 16, scale = 6)
	public Double getFrate3() {
		return this.frate3;
	}

	public void setFrate3(Double frate3) {
		this.frate3 = frate3;
	}
	
	@Column(name = "fupthreshold", precision = 16, scale = 6)
	public Double getFupthreshold() {
		return this.fupthreshold;
	}

	public void setFupthreshold(Double fupthreshold) {
		this.fupthreshold = fupthreshold;
	}
	
	@Column(name = "fdownthreshold", precision = 16, scale = 6)
	public Double getFdownthreshold() {
		return this.fdownthreshold;
	}

	public void setFdownthreshold(Double fdownthreshold) {
		this.fdownthreshold = fdownthreshold;
	}

	@Column(name = "fstartpricetime", length = 0)
	public Time getFstartpricetime() {
		return this.fstartpricetime;
	}

	public void setFstartpricetime(Time fstartpricetime) {
		this.fstartpricetime = fstartpricetime;
	}

	@Column(name = "fendpricetime", length = 0)
	public Time getFendpricetime() {
		return this.fendpricetime;
	}

	public void setFendpricetime(Time fendpricetime) {
		this.fendpricetime = fendpricetime;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "fpriceurl", length = 100)
	public String getFpriceurl() {
		return this.fpriceurl;
	}

	public void setFpriceurl(String fpriceurl) {
		this.fpriceurl = fpriceurl;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fhedging")
	public Set<Fhedginglog> getFhedginglogs() {
		return this.fhedginglogs;
	}

	public void setFhedginglogs(Set<Fhedginglog> fhedginglogs) {
		this.fhedginglogs = fhedginglogs;
	}

}