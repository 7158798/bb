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

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.ActivityStatusEnum;

/**
 * Factivity entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "factivity")
public class Factivity implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Factivitytype factivitytype;

	private String ftitle;
	private String fcontent;
	private String fbannerImage;
	private Integer fstatus;// ActivityStatusEnum
	private String fstatus_s;
	private Timestamp fcreateTime;
	private Timestamp flastUpdateTime;
	private Boolean fisMultiple;// 一人是否可以多次完成获得多次奖励
	private Long ftimeInterval;// 如果可以多次完成，两次获奖的间隔
	private Integer frewardPerCount;// 完成多少次可以获得奖励，一般是一次
	private Integer frewardPromotionPerCount;// 完成多少次可以获得推广奖励

	private boolean fisActiveForever;// 是否永久有限
	private Timestamp fBeginTime;// 非永久有效情况，开始时间
	private Timestamp fEndTime;// 结束时间

	private Set<Frewardrecord> frewardrecords = new HashSet<Frewardrecord>(0);
	private Set<Fpromotionactivityreward> fpromotionactivityrewards = new HashSet<Fpromotionactivityreward>(
			0);
	private Set<Factivitypromotioncompletelog> factivitypromotioncompletelogs = new HashSet<Factivitypromotioncompletelog>(
			0);
	private Set<Factivitycompletelog> factivitycompletelogs = new HashSet<Factivitycompletelog>(
			0);
	private Set<Fpromotionrewardrecord> fpromotionrewardrecords = new HashSet<Fpromotionrewardrecord>(
			0);
	private Set<Factivityreward> factivityrewards = new HashSet<Factivityreward>(
			0);

	// Constructors

	/** default constructor */
	public Factivity() {
	}

	/** full constructor */
	public Factivity(Factivitytype factivitytype, String ftitle,
			String fcontent, String fbannerImage, Integer fstatus,
			Timestamp fcreateTime, Timestamp flastUpdateTime,
			Boolean fisMultiple, Long ftimeInterval, Integer frewardPerCount,
			Integer frewardPromotionPerCount,
			Set<Frewardrecord> frewardrecords,
			Set<Fpromotionactivityreward> fpromotionactivityrewards,
			Set<Factivitypromotioncompletelog> factivitypromotioncompletelogs,
			Set<Factivitycompletelog> factivitycompletelogs,
			Set<Fpromotionrewardrecord> fpromotionrewardrecords,
			Set<Factivityreward> factivityrewards) {
		this.factivitytype = factivitytype;
		this.ftitle = ftitle;
		this.fcontent = fcontent;
		this.fbannerImage = fbannerImage;
		this.fstatus = fstatus;
		this.fcreateTime = fcreateTime;
		this.flastUpdateTime = flastUpdateTime;
		this.fisMultiple = fisMultiple;
		this.ftimeInterval = ftimeInterval;
		this.frewardPerCount = frewardPerCount;
		this.frewardPromotionPerCount = frewardPromotionPerCount;
		this.frewardrecords = frewardrecords;
		this.fpromotionactivityrewards = fpromotionactivityrewards;
		this.factivitypromotioncompletelogs = factivitypromotioncompletelogs;
		this.factivitycompletelogs = factivitycompletelogs;
		this.fpromotionrewardrecords = fpromotionrewardrecords;
		this.factivityrewards = factivityrewards;
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
	@JoinColumn(name = "factivityType")
	public Factivitytype getFactivitytype() {
		return this.factivitytype;
	}

	public void setFactivitytype(Factivitytype factivitytype) {
		this.factivitytype = factivitytype;
	}

	@Column(name = "fTitle", length = 512)
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fContent", length = 65535)
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "fBannerImage", length = 1024)
	public String getFbannerImage() {
		return this.fbannerImage;
	}

	public void setFbannerImage(String fbannerImage) {
		this.fbannerImage = fbannerImage;
	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fcreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "flastUpdateTime", length = 0)
	public Timestamp getFlastUpdateTime() {
		return this.flastUpdateTime;
	}

	public void setFlastUpdateTime(Timestamp flastUpdateTime) {
		this.flastUpdateTime = flastUpdateTime;
	}

	@Column(name = "fisMultiple")
	public Boolean getFisMultiple() {
		return this.fisMultiple;
	}

	public void setFisMultiple(Boolean fisMultiple) {
		this.fisMultiple = fisMultiple;
	}

	@Column(name = "ftimeInterval")
	public Long getFtimeInterval() {
		return this.ftimeInterval;
	}

	public void setFtimeInterval(Long ftimeInterval) {
		this.ftimeInterval = ftimeInterval;
	}

	@Column(name = "frewardPerCount")
	public Integer getFrewardPerCount() {
		return this.frewardPerCount;
	}

	public void setFrewardPerCount(Integer frewardPerCount) {
		this.frewardPerCount = frewardPerCount;
	}

	@Column(name = "frewardPromotionPerCount")
	public Integer getFrewardPromotionPerCount() {
		return this.frewardPromotionPerCount;
	}

	public void setFrewardPromotionPerCount(Integer frewardPromotionPerCount) {
		this.frewardPromotionPerCount = frewardPromotionPerCount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "factivity")
	public Set<Frewardrecord> getFrewardrecords() {
		return this.frewardrecords;
	}

	public void setFrewardrecords(Set<Frewardrecord> frewardrecords) {
		this.frewardrecords = frewardrecords;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "factivity")
	public Set<Fpromotionactivityreward> getFpromotionactivityrewards() {
		return this.fpromotionactivityrewards;
	}

	public void setFpromotionactivityrewards(
			Set<Fpromotionactivityreward> fpromotionactivityrewards) {
		this.fpromotionactivityrewards = fpromotionactivityrewards;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "factivity")
	public Set<Factivitypromotioncompletelog> getFactivitypromotioncompletelogs() {
		return this.factivitypromotioncompletelogs;
	}

	public void setFactivitypromotioncompletelogs(
			Set<Factivitypromotioncompletelog> factivitypromotioncompletelogs) {
		this.factivitypromotioncompletelogs = factivitypromotioncompletelogs;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "factivity")
	public Set<Factivitycompletelog> getFactivitycompletelogs() {
		return this.factivitycompletelogs;
	}

	public void setFactivitycompletelogs(
			Set<Factivitycompletelog> factivitycompletelogs) {
		this.factivitycompletelogs = factivitycompletelogs;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "factivity")
	public Set<Fpromotionrewardrecord> getFpromotionrewardrecords() {
		return this.fpromotionrewardrecords;
	}

	public void setFpromotionrewardrecords(
			Set<Fpromotionrewardrecord> fpromotionrewardrecords) {
		this.fpromotionrewardrecords = fpromotionrewardrecords;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "factivity")
	public Set<Factivityreward> getFactivityrewards() {
		return this.factivityrewards;
	}

	public void setFactivityrewards(Set<Factivityreward> factivityrewards) {
		this.factivityrewards = factivityrewards;
	}

	@Column(name = "fisActiveForever")
	public boolean isFisActiveForever() {
		return fisActiveForever;
	}

	public void setFisActiveForever(boolean fisActiveForever) {
		this.fisActiveForever = fisActiveForever;
	}

	@Column(name = "fBeginTime")
	public Timestamp getfBeginTime() {
		return fBeginTime;
	}

	public void setfBeginTime(Timestamp fBeginTime) {
		this.fBeginTime = fBeginTime;
	}

	@Column(name = "fEndTime")
	public Timestamp getfEndTime() {
		return fEndTime;
	}

	public void setfEndTime(Timestamp fEndTime) {
		this.fEndTime = fEndTime;
	}
	
	@Transient
	public String getFstatus_s() {
		return ActivityStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}


}