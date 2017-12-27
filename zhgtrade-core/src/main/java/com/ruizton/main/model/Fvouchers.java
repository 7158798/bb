package com.ruizton.main.model;
// default package

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.VouchersStatusEnum;

/**
 * Fvouchers entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fvouchers")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fvouchers implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fuser fuser;
	private Double famount;
	private Date fendate;
	private Integer fstatus;
	private String fstatus_s;
	private Double flastamount;
	private Timestamp fcreatetime;

	// Constructors

	/** default constructor */
	public Fvouchers() {
	}

	/** full constructor */
	public Fvouchers(Fuser fuser, Double famount, Date fendate,
			Integer fstatus, Double flastamount, Timestamp fcreatetime) {
		this.fuser = fuser;
		this.famount = famount;
		this.fendate = fendate;
		this.fstatus = fstatus;
		this.flastamount = flastamount;
		this.fcreatetime = fcreatetime;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "identity")
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
	@JoinColumn(name = "fuserid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "famount", precision = 16, scale = 6)
	public Double getFamount() {
		return this.famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fendate", length = 0)
	public Date getFendate() {
		return this.fendate;
	}

	public void setFendate(Date fendate) {
		this.fendate = fendate;
	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "flastamount", precision = 16, scale = 6)
	public Double getFlastamount() {
		return this.flastamount;
	}

	public void setFlastamount(Double flastamount) {
		this.flastamount = flastamount;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}
	
	
	@Transient
	public String getFstatus_s() {
		return VouchersStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

}