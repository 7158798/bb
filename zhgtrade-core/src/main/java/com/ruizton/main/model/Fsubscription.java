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
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.SubscriptionTypeEnum;

/**
 * Fsubscription entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fsubscription")
public class Fsubscription implements java.io.Serializable {

	// Fields

	private int fid;
	private Fvirtualcointype fvirtualcointypeCost;
	private Fvirtualcointype fvirtualcointype;
	private boolean fisopen;//是否开放认购
	private double ftotal;//认购总量
	private double fAlreadyByCount ;
	private double fprice;//认购价格
	private int fbuyCount;//每人最大认购数量，0为无限
	private int fbuyTimes;//每人最多认购次数，0为无限
	private Timestamp fcreateTime;
	private Timestamp fbeginTime;//开始时间
	private Timestamp fendTime;//结束时间
	private int ftype;//SubscriptionTypeEnum
	private String ftype_s;
	private int fdays;
	private String ftitle;
    private int version;


	private Set<Fsubscriptionlog> fsubscriptionlogs = new HashSet<Fsubscriptionlog>(
			0);

	// Constructors

	/** default constructor */
	public Fsubscription() {
	}

	/** full constructor */
	public Fsubscription(Fvirtualcointype fvirtualcointype, boolean fisopen,
			Double ftotal, Double fprice, int fbuyCount, int fbuyTimes,
			Timestamp fcreateTime, Timestamp fbeginTime, Timestamp fendTime,
			Set<Fsubscriptionlog> fsubscriptionlogs) {
		this.fvirtualcointype = fvirtualcointype;
		this.fisopen = fisopen;
		this.ftotal = ftotal;
		this.fprice = fprice;
		this.fbuyCount = fbuyCount;
		this.fbuyTimes = fbuyTimes;
		this.fcreateTime = fcreateTime;
		this.fbeginTime = fbeginTime;
		this.fendTime = fendTime;
		this.fsubscriptionlogs = fsubscriptionlogs;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public int getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fvi_id")
	public Fvirtualcointype getFvirtualcointype() {
		return this.fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fcost_vi_id")
	public Fvirtualcointype getFvirtualcointypeCost() {
		return fvirtualcointypeCost;
	}

	public void setFvirtualcointypeCost(Fvirtualcointype fvirtualcointypeCost) {
		this.fvirtualcointypeCost = fvirtualcointypeCost;
	}

	@Column(name = "fisopen")
	public boolean getFisopen() {
		return this.fisopen;
	}

	public void setFisopen(boolean fisopen) {
		this.fisopen = fisopen;
	}

	@Column(name = "ftotal", precision = 16, scale = 6)
	public Double getFtotal() {
		return this.ftotal;
	}

	public void setFtotal(Double ftotal) {
		this.ftotal = ftotal;
	}

	@Column(name = "fprice", precision = 16, scale = 6)
	public Double getFprice() {
		return this.fprice;
	}

	public void setFprice(Double fprice) {
		this.fprice = fprice;
	}

	@Column(name = "fbuyCount")
	public int getFbuyCount() {
		return this.fbuyCount;
	}

	public void setFbuyCount(int fbuyCount) {
		this.fbuyCount = fbuyCount;
	}

	@Column(name = "fbuyTimes")
	public int getFbuyTimes() {
		return this.fbuyTimes;
	}

	public void setFbuyTimes(int fbuyTimes) {
		this.fbuyTimes = fbuyTimes;
	}

	@Column(name = "fcreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fBeginTime", length = 0)
	public Timestamp getFbeginTime() {
		return this.fbeginTime;
	}

	public void setFbeginTime(Timestamp fbeginTime) {
		this.fbeginTime = fbeginTime;
	}

	@Column(name = "fEndTime", length = 0)
	public Timestamp getFendTime() {
		return this.fendTime;
	}

	public void setFendTime(Timestamp fendTime) {
		this.fendTime = fendTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fsubscription")
	public Set<Fsubscriptionlog> getFsubscriptionlogs() {
		return this.fsubscriptionlogs;
	}

	public void setFsubscriptionlogs(Set<Fsubscriptionlog> fsubscriptionlogs) {
		this.fsubscriptionlogs = fsubscriptionlogs;
	}

	@Column(name="fAlreadyByCount")
	public Double getfAlreadyByCount() {
		return fAlreadyByCount;
	}

	public void setfAlreadyByCount(Double fAlreadyByCount) {
		this.fAlreadyByCount = fAlreadyByCount;
	}

	@Column(name="ftype")
	public int getFtype() {
		return ftype;
	}

	public void setFtype(int ftype) {
		this.ftype = ftype;
	}
	
	@Column(name="fdays")
	public int getFdays() {
		return fdays;
	}

	public void setFdays(int fdays) {
		this.fdays = fdays;
	}
	
	@Column(name="ftitle")
	public String getFtitle() {
		return ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Transient
	public String getFtype_s() {
		return SubscriptionTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}
	
	@Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
}