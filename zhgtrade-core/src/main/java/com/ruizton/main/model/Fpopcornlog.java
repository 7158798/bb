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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.PopcornResult1Enum;
import com.ruizton.main.Enum.PopcornResult2Enum;
import com.ruizton.main.Enum.PopcornStatusEnum;

/**
 * Fpopcornlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fpopcornlog")
public class Fpopcornlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer fstatus;//状态PopcornStatusEnum
	private String fstatus_s;
	private Integer fresultQty;//爆米花数量
	private Integer fresult1;//单双PopcornResult1Enum
	private String fresult1_s;
	private Integer fresult2;//大小PopcornResult2Enum
	private String fresult2_s;
	private Timestamp fcreatetime;
	private Set<Fpopcornbetlog> fpopcornbetlogs = new HashSet<Fpopcornbetlog>(0);

	// Constructors

	/** default constructor */
	public Fpopcornlog() {
	}

	/** full constructor */
	public Fpopcornlog(Integer fstatus, Integer fresultQty, Integer fresult1,
			Integer fresult2, Timestamp fcreatetime,
			Set<Fpopcornbetlog> fpopcornbetlogs) {
		this.fstatus = fstatus;
		this.fresultQty = fresultQty;
		this.fresult1 = fresult1;
		this.fresult2 = fresult2;
		this.fcreatetime = fcreatetime;
		this.fpopcornbetlogs = fpopcornbetlogs;
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

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fresultQty")
	public Integer getFresultQty() {
		return this.fresultQty;
	}

	public void setFresultQty(Integer fresultQty) {
		this.fresultQty = fresultQty;
	}

	@Column(name = "fresult1")
	public Integer getFresult1() {
		return this.fresult1;
	}

	public void setFresult1(Integer fresult1) {
		this.fresult1 = fresult1;
	}

	@Column(name = "fresult2")
	public Integer getFresult2() {
		return this.fresult2;
	}

	public void setFresult2(Integer fresult2) {
		this.fresult2 = fresult2;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fpopcornlog")
	public Set<Fpopcornbetlog> getFpopcornbetlogs() {
		return this.fpopcornbetlogs;
	}

	public void setFpopcornbetlogs(Set<Fpopcornbetlog> fpopcornbetlogs) {
		this.fpopcornbetlogs = fpopcornbetlogs;
	}
	
	@Transient
	public String getFresult1_s() {
		if(this.getFresult1() == null) return "";
		return PopcornResult1Enum.getEnumString(this.getFresult1());
	}

	public void setFresult1_s(String fresult1_s) {
		this.fresult1_s = fresult1_s;
	}
	
	@Transient
	public String getFresult2_s() {
		if(this.getFresult2() == null) return "";
		return PopcornResult2Enum.getEnumString(this.getFresult2());
	}

	public void setFresult2_s(String fresult2_s) {
		this.fresult2_s = fresult2_s;
	}
	
	
	@Transient
	public String getFstatus_s() {
		return PopcornStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

}