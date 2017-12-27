package com.ruizton.main.model;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * FtaskLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ftask_log")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FtaskLog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Ftask ftask;
	private Fuser fuser;
	private String remark;
	private Timestamp fcreatetime;

	private boolean fisFinish ;
	private boolean fisReach ;
	// Constructors

	/** default constructor */
	public FtaskLog() {
	}

	/** full constructor */
	public FtaskLog(Ftask ftask, Fuser fuser, String remark,
			Timestamp fcreatetime) {
		this.ftask = ftask;
		this.fuser = fuser;
		this.remark = remark;
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
	@JoinColumn(name = "ftask")
	public Ftask getFtask() {
		return this.ftask;
	}

	public void setFtask(Ftask ftask) {
		this.ftask = ftask;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuser")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "remark", length = 65535)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Transient
	public boolean isFisFinish() {
		return fisFinish;
	}

	public void setFisFinish(boolean fisFinish) {
		this.fisFinish = fisFinish;
	}

	@Transient
	public boolean isFisReach() {
		return fisReach;
	}

	public void setFisReach(boolean fisReach) {
		this.fisReach = fisReach;
	}

	

	
	
}