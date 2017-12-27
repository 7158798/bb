package com.ruizton.main.model;

// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.KillHistoryStatusEnum;
import com.ruizton.main.Enum.KillHistoryTypeEnum;

/**
 * Fkillhistory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fkillhistory")
public class Fkillhistory implements java.io.Serializable {

	// Fields

	private Integer fid;
	private String fuser;
	private Integer ftype;// KillHistoryTypeEnum
	private String ftype_s;
	private Timestamp fcreatetime;
	private Integer fstatus;
	private String fstatus_s;

	// Constructors

	/** default constructor */
	public Fkillhistory() {
	}

	/** full constructor */
	public Fkillhistory(String fuser, Integer ftype, Timestamp fcreatetime,
			Integer fstatus) {
		this.fuser = fuser;
		this.ftype = ftype;
		this.fcreatetime = fcreatetime;
		this.fstatus = fstatus;
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

	@Column(name = "fuser", length = 500)
	public String getFuser() {
		return this.fuser;
	}

	public void setFuser(String fuser) {
		this.fuser = fuser;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
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

	@Transient
	public String getFtype_s() {
		return KillHistoryTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}

	@Transient
	public String getFstatus_s() {
		return KillHistoryStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}
}