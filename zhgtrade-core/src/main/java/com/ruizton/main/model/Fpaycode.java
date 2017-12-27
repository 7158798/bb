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
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.PayCodeStatusEnum;

/**
 * 支付码列表
 */
@Entity
@Table(name = "fpaycode")
public class Fpaycode implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fuser fuser;
	private String fkey;
	private String fvalue;
	private Double famount;
	private Integer fstatus;//PayCodeStatusEnum
	private String fstatus_s;
	private Timestamp fcreatetime;
	private Timestamp fuserTime;
	private Timestamp fadminTime;
	private int version;
	// Constructors

	/** default constructor */
	public Fpaycode() {
	}

	/** full constructor */
	public Fpaycode(Fuser fuser, String fkey, String fvalue, Double famount,
			Integer fstatus, Timestamp fcreatetime, Timestamp fuserTime,
			Timestamp fadminTime) {
		this.fuser = fuser;
		this.fkey = fkey;
		this.fvalue = fvalue;
		this.famount = famount;
		this.fstatus = fstatus;
		this.fcreatetime = fcreatetime;
		this.fuserTime = fuserTime;
		this.fadminTime = fadminTime;
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
	@JoinColumn(name = "fuser")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fkey", length = 128)
	public String getFkey() {
		return this.fkey;
	}

	public void setFkey(String fkey) {
		this.fkey = fkey;
	}

	@Column(name = "fvalue", length = 128)
	public String getFvalue() {
		return this.fvalue;
	}

	public void setFvalue(String fvalue) {
		this.fvalue = fvalue;
	}

	@Column(name = "famount", precision = 16, scale = 6)
	public Double getFamount() {
		return this.famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
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

	@Column(name = "fuserTime", length = 0)
	public Timestamp getFuserTime() {
		return this.fuserTime;
	}

	public void setFuserTime(Timestamp fuserTime) {
		this.fuserTime = fuserTime;
	}

	@Column(name = "fadminTime", length = 0)
	public Timestamp getFadminTime() {
		return this.fadminTime;
	}

	public void setFadminTime(Timestamp fadminTime) {
		this.fadminTime = fadminTime;
	}

	
	@Transient
	public String getFstatus_s() {
		return PayCodeStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}
	
	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}