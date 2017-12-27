package com.ruizton.main.model;
// default package

import java.sql.Timestamp;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.DeductStatusEnum;

/**
 * Fdeduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fdeduct")
public class Fdeduct implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fadmin fadmin;//审核人
	private Fchargesection fchargesection;//结算区间
	private Fuser fuser;//业务员
	private Date fchargeDate;//结算日期
	private Double ftotalAmt;//提成总金额
	private Double ftotalQty;//提成总金豆数量
	private Integer fstatus;//DeductStatusEnum
	private String fstatus_s;
	private Timestamp fcreatetime;
	private Timestamp faudittime;//审核时间
	private Set<Fdeductlog> fdeductlogs = new HashSet<Fdeductlog>(0);

	// Constructors

	/** default constructor */
	public Fdeduct() {
	}

	/** full constructor */
	public Fdeduct(Fadmin fadmin, Fchargesection fchargesection, Fuser fuser,
			Date fchargeDate, Double ftotalAmt, Double ftotalQty,
			Integer fstatus, Timestamp fcreatetime, Timestamp faudittime,
			Set<Fdeductlog> fdeductlogs) {
		this.fadmin = fadmin;
		this.fchargesection = fchargesection;
		this.fuser = fuser;
		this.fchargeDate = fchargeDate;
		this.ftotalAmt = ftotalAmt;
		this.ftotalQty = ftotalQty;
		this.fstatus = fstatus;
		this.fcreatetime = fcreatetime;
		this.faudittime = faudittime;
		this.fdeductlogs = fdeductlogs;
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
	@JoinColumn(name = "fauditorid")
	public Fadmin getFadmin() {
		return this.fadmin;
	}

	public void setFadmin(Fadmin fadmin) {
		this.fadmin = fadmin;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fchargesectionId")
	public Fchargesection getFchargesection() {
		return this.fchargesection;
	}

	public void setFchargesection(Fchargesection fchargesection) {
		this.fchargesection = fchargesection;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fchargeDate", length = 0)
	public Date getFchargeDate() {
		return this.fchargeDate;
	}

	public void setFchargeDate(Date fchargeDate) {
		this.fchargeDate = fchargeDate;
	}

	@Column(name = "ftotalAmt", precision = 16, scale = 6)
	public Double getFtotalAmt() {
		return this.ftotalAmt;
	}

	public void setFtotalAmt(Double ftotalAmt) {
		this.ftotalAmt = ftotalAmt;
	}

	@Column(name = "ftotalQty", precision = 16, scale = 6)
	public Double getFtotalQty() {
		return this.ftotalQty;
	}

	public void setFtotalQty(Double ftotalQty) {
		this.ftotalQty = ftotalQty;
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

	@Column(name = "faudittime", length = 0)
	public Timestamp getFaudittime() {
		return this.faudittime;
	}

	public void setFaudittime(Timestamp faudittime) {
		this.faudittime = faudittime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fdeduct")
	public Set<Fdeductlog> getFdeductlogs() {
		return this.fdeductlogs;
	}

	public void setFdeductlogs(Set<Fdeductlog> fdeductlogs) {
		this.fdeductlogs = fdeductlogs;
	}
	
	@Transient
	public String getFstatus_s() {
		return DeductStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

}