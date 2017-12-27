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
import javax.persistence.Version;
import org.hibernate.annotations.GenericGenerator;

/**
 * FbankinfoWithdraw entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fbankinfo_withdraw")
public class FbankinfoWithdraw implements java.io.Serializable {

	// Fields

	private int fid;
	private int version;
	private Fuser fuser;
	private String fname;
	private String fbankNumber;
	private String fprovince;
	private String fcity;
	private String fbranch;
	private int fbankType;
	private Timestamp fcreateTime;
	private int fstatus;
	private boolean init ;//是否初始化
	// Constructors

	/** default constructor */
	public FbankinfoWithdraw() {
	}

	/** full constructor */
	public FbankinfoWithdraw(Fuser fuser, String fname, String fbankNumber,String fprovince,String fcity,String fbranch,
			int fbankType, Timestamp fcreateTime, int fstatus) {
		this.fuser = fuser;
		this.fname = fname;
		this.fbankNumber = fbankNumber;
		this.fprovince = fprovince;
		this.fcity = fcity;
		this.fbranch = fbranch;
		this.fbankType = fbankType;
		this.fcreateTime = fcreateTime;
		this.fstatus = fstatus;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fId", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@Version
	@Column(name = "version")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FUs_fId")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fName", length = 128)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fBankNumber", length = 128)
	public String getFbankNumber() {
		return this.fbankNumber;
	}

	public void setFbankNumber(String fbankNumber) {
		this.fbankNumber = fbankNumber;
	}
	
	@Column(name = "fProvince", length = 128)
	public String getFprovince() {
		return this.fprovince;
	}

	public void setFprovince(String fprovince) {
		this.fprovince = fprovince;
	}
	
	@Column(name = "fCity", length = 128)
	public String getFcity() {
		return this.fcity;
	}

	public void setFcity(String fcity) {
		this.fcity = fcity;
	}
	
	@Column(name = "fBranch", length = 128)
	public String getFbranch() {
		return this.fbranch;
	}

	public void setFbranch(String fbranch) {
		this.fbranch = fbranch;
	}

	@Column(name = "fBankType")
	public int getFbankType() {
		return this.fbankType;
	}

	public void setFbankType(int fbankType) {
		this.fbankType = fbankType;
	}

	@Column(name = "fCreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fStatus")
	public int getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name="init")
	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}
}