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

import com.ruizton.main.Enum.HedginglogStatusEnum;
import com.ruizton.main.Enum.HedginglogTypeEnum;

/**
 * Fhedginglog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fhedginglog")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fhedginglog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer version;
	private Fhedging fhedging;
	private Double ftotal1;
	private Double ftotal2;
	private Double ftotal3;
	private Double fstartprice;
	private Double fendprice;
	private Integer fstatus;
	private String fstatus_s;
	private Timestamp fcreatetime;
	private Integer fresult;
	private String fresult_s;
	private String fnumber;
	private Fvirtualcointype fvirtualcointypeByFvid2;//投注
	private Fvirtualcointype fvirtualcointypeByFid;//对冲
	private Set<Fhedginguserlog> fhedginguserlogs = new HashSet<Fhedginguserlog>(
			0);
	private Timestamp fstarttime;
	private Timestamp fendtime;
	private Timestamp fstartpricetime;
	private Timestamp fendpricetime;

	// Constructors

	/** default constructor */
	public Fhedginglog() {
	}

	/** full constructor */
	public Fhedginglog(Fhedging fhedging, Double ftotal1, Double ftotal2,
			Double ftotal3, Double fstartprice, Double fendprice,
			Integer fstatus, Timestamp fcreatetime, Integer fresult,
			String fnumber, Set<Fhedginguserlog> fhedginguserlogs) {
		this.fhedging = fhedging;
		this.ftotal1 = ftotal1;
		this.ftotal2 = ftotal2;
		this.ftotal3 = ftotal3;
		this.fstartprice = fstartprice;
		this.fendprice = fendprice;
		this.fstatus = fstatus;
		this.fcreatetime = fcreatetime;
		this.fresult = fresult;
		this.fnumber = fnumber;
		this.fhedginguserlogs = fhedginguserlogs;
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
	@JoinColumn(name = "fhedgingid")
	public Fhedging getFhedging() {
		return this.fhedging;
	}

	public void setFhedging(Fhedging fhedging) {
		this.fhedging = fhedging;
	}

	@Column(name = "ftotal1", precision = 16, scale = 6)
	public Double getFtotal1() {
		return this.ftotal1;
	}

	public void setFtotal1(Double ftotal1) {
		this.ftotal1 = ftotal1;
	}

	@Column(name = "ftotal2", precision = 16, scale = 6)
	public Double getFtotal2() {
		return this.ftotal2;
	}

	public void setFtotal2(Double ftotal2) {
		this.ftotal2 = ftotal2;
	}

	@Column(name = "ftotal3", precision = 16, scale = 6)
	public Double getFtotal3() {
		return this.ftotal3;
	}

	public void setFtotal3(Double ftotal3) {
		this.ftotal3 = ftotal3;
	}

	@Column(name = "fstartprice", precision = 16, scale = 6)
	public Double getFstartprice() {
		return this.fstartprice;
	}

	public void setFstartprice(Double fstartprice) {
		this.fstartprice = fstartprice;
	}

	@Column(name = "fendprice", precision = 16, scale = 6)
	public Double getFendprice() {
		return this.fendprice;
	}

	public void setFendprice(Double fendprice) {
		this.fendprice = fendprice;
	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "fresult")
	public Integer getFresult() {
		return this.fresult;
	}

	public void setFresult(Integer fresult) {
		this.fresult = fresult;
	}

	@Column(name = "fnumber", length = 50)
	public String getFnumber() {
		return this.fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fhedginglog")
	public Set<Fhedginguserlog> getFhedginguserlogs() {
		return this.fhedginguserlogs;
	}

	public void setFhedginguserlogs(Set<Fhedginguserlog> fhedginguserlogs) {
		this.fhedginguserlogs = fhedginguserlogs;
	}

	@Transient
	public String getFstatus_s() {
		return HedginglogStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Transient
	public String getFresult_s() {
		return HedginglogTypeEnum.getEnumString(this.getFresult());
	}

	public void setFresult_s(String fresult_s) {
		this.fresult_s = fresult_s;
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

	@Column(name = "fstartpricetime", length = 0)
	public Timestamp getFstartpricetime() {
		return this.fstartpricetime;
	}

	public void setFstartpricetime(Timestamp fstartpricetime) {
		this.fstartpricetime = fstartpricetime;
	}

	@Column(name = "fendpricetime", length = 0)
	public Timestamp getFendpricetime() {
		return this.fendpricetime;
	}

	public void setFendpricetime(Timestamp fendpricetime) {
		this.fendpricetime = fendpricetime;
	}

	@Column(name = "fstarttime", length = 0)
	public Timestamp getFstarttime() {
		return this.fstarttime;
	}

	public void setFstarttime(Timestamp fstarttime) {
		this.fstarttime = fstarttime;
	}

	@Column(name = "fendtime", length = 0)
	public Timestamp getFendtime() {
		return this.fendtime;
	}

	public void setFendtime(Timestamp fendtime) {
		this.fendtime = fendtime;
	}
}