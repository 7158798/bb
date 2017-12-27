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
import javax.persistence.Version;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.ApplyinfoStatusEnum;
import com.ruizton.main.Enum.UserGradeEnum;

/**
 * Fapplyinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fapplyinfo")
public class Fapplyinfo implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer version;
	private Fuser fuser;
	private Integer fstatus;
	private String fstatus_s;
	private String freason;
	private Integer fgrade;
	private String fgrade_s;
	private String frealname;
	private String fphone;
	private Timestamp fcreatetime;
	private String frejectReason;

	// Constructors

	/** default constructor */
	public Fapplyinfo() {
	}

	/** full constructor */
	public Fapplyinfo(Fuser fuser, Integer fstatus, String freason,
			Integer fgrade, String frealname, String fphone,
			Timestamp fcreatetime) {
		this.fuser = fuser;
		this.fstatus = fstatus;
		this.freason = freason;
		this.fgrade = fgrade;
		this.frealname = frealname;
		this.fphone = fphone;
		this.fcreatetime = fcreatetime;
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
	@JoinColumn(name = "fuserid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "freason", length = 1000)
	public String getFreason() {
		return this.freason;
	}

	public void setFreason(String freason) {
		this.freason = freason;
	}

	@Column(name = "fgrade")
	public Integer getFgrade() {
		return this.fgrade;
	}

	public void setFgrade(Integer fgrade) {
		this.fgrade = fgrade;
	}

	@Column(name = "frealname", length = 100)
	public String getFrealname() {
		return this.frealname;
	}

	public void setFrealname(String frealname) {
		this.frealname = frealname;
	}

	@Column(name = "fphone", length = 100)
	public String getFphone() {
		return this.fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}
	
	@Column(name = "frejectReason")
	public String getFrejectReason() {
		return frejectReason;
	}

	public void setFrejectReason(String frejectReason) {
		this.frejectReason = frejectReason;
	}
	
	
	@Transient
	public String getFstatus_s() {
		return ApplyinfoStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	
	@Transient
	public String getFgrade_s() {
		return UserGradeEnum.getEnumString(this.getFgrade());
	}

	public void setFgrade_s(String fgrade_s) {
		this.fgrade_s = fgrade_s;
	}
}