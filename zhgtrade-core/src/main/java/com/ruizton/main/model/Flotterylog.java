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

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.LotterylogStatusEnum;

/**
 * Flotterylog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "flotterylog")
public class Flotterylog implements java.io.Serializable {

	private Integer fid;
	private Fuser fuser;
	private String ftitle;
	private Timestamp fcreateTime;
	private int fstatus;//LotterylogStatusEnum
	private String fstatus_s;
	private boolean fflag ;//实物true，虚拟币false
	private double fqty;

	/** default constructor */
	public Flotterylog() {
		
	}

	/** full constructor */
	public Flotterylog(Fuser fuser, String ftitle, Timestamp fcreateTime) {
		this.fuser = fuser;
		this.ftitle = ftitle;
		this.fcreateTime = fcreateTime;
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
	@JoinColumn(name = "fuserId")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "ftitle", length = 200)
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fcreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}
	
	@Column(name = "fstatus")
	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	@Transient
	public String getFstatus_s() {
		return LotterylogStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Column(name="fflag")
	public boolean isFflag() {
		return fflag;
	}

	public void setFflag(boolean fflag) {
		this.fflag = fflag;
	}
	
	@Column(name="fqty")
	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}
	
}