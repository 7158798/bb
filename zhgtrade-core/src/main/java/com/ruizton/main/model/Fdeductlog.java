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

import com.ruizton.main.Enum.DeductlogTypeEnum;

/**
 * 提成明细
 */
@Entity
@Table(name = "fdeductlog")
public class Fdeductlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fdeduct fdeduct;//提成总表ID
	private Integer ftype;//DeductlogTypeEnum
	private String ftype_s;
	private Double famt;//操作金额
	private Double ffees;//操作手续费
	private Double ftakeAmt;//得到奖励
	private Boolean fisMoney;//是否是人民币
	private Timestamp fcreatetime;
	private Integer fsourceId;//来源ID
	private Fuser fuser;//fuserId
	private Fuser fsourceUserId;
	private String fkey;

	/** default constructor */
	public Fdeductlog() {
	}

	/** full constructor */
	public Fdeductlog(Fdeduct fdeduct, Integer ftype, Double famt,
			Double ffees, Double ftakeAmt, Boolean fisMoney,
			Timestamp fcreatetime, Integer fsourceId) {
		this.fdeduct = fdeduct;
		this.ftype = ftype;
		this.famt = famt;
		this.ffees = ffees;
		this.ftakeAmt = ftakeAmt;
		this.fisMoney = fisMoney;
		this.fcreatetime = fcreatetime;
		this.fsourceId = fsourceId;
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
	@JoinColumn(name = "fdeductId")
	public Fdeduct getFdeduct() {
		return this.fdeduct;
	}

	public void setFdeduct(Fdeduct fdeduct) {
		this.fdeduct = fdeduct;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "famt", precision = 16, scale = 6)
	public Double getFamt() {
		return this.famt;
	}

	public void setFamt(Double famt) {
		this.famt = famt;
	}

	@Column(name = "ffees", precision = 16, scale = 6)
	public Double getFfees() {
		return this.ffees;
	}

	public void setFfees(Double ffees) {
		this.ffees = ffees;
	}

	@Column(name = "ftakeAmt", precision = 16, scale = 6)
	public Double getFtakeAmt() {
		return this.ftakeAmt;
	}

	public void setFtakeAmt(Double ftakeAmt) {
		this.ftakeAmt = ftakeAmt;
	}

	@Column(name = "fisMoney")
	public Boolean getFisMoney() {
		return this.fisMoney;
	}

	public void setFisMoney(Boolean fisMoney) {
		this.fisMoney = fisMoney;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "fsourceId")
	public Integer getFsourceId() {
		return this.fsourceId;
	}

	public void setFsourceId(Integer fsourceId) {
		this.fsourceId = fsourceId;
	}
	
	@Column(name = "fkey")
	public String getFkey() {
		return fkey;
	}

	public void setFkey(String fkey) {
		this.fkey = fkey;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuserId")
	public Fuser getFuser() {
		return fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fsourceUserId")
	public Fuser getFsourceUserId() {
		return fsourceUserId;
	}

	public void setFsourceUserId(Fuser fsourceUserId) {
		this.fsourceUserId = fsourceUserId;
	}
	
	
	@Transient
	public String getFtype_s() {
		return DeductlogTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}

}