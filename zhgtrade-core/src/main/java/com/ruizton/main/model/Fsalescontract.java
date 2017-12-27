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

import com.ruizton.main.Enum.SalescontractStatusEnum;
import com.ruizton.main.Enum.UserGradeEnum;

/**
 * 业务员合同表
 */
@Entity
@Table(name = "fsalescontract")
public class Fsalescontract implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fuser fuser;//用户ID	
	private Fadmin fadmin;//审核人
	private Fsalespercent fsalespercent;//分成比例外键
//	private Double fcashQty;//冻结豆数量
	private Integer fstatus;//SalescontractStatusEnum
	private String fstatus_s;
	private Timestamp fcreateTime;
	private Integer fgrade;
	private String fgrade_s;

	// Constructors

	/** default constructor */
	public Fsalescontract() {
	}

	/** full constructor */
	public Fsalescontract(Fuser fuser, Fadmin fadmin,
			Double fcashQty, Integer fstatus, Timestamp fcreateTime) {
		this.fuser = fuser;
		this.fadmin = fadmin;
//		this.fcashQty = fcashQty;
		this.fstatus = fstatus;
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
	@JoinColumn(name = "fuserid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fadminId")
	public Fadmin getFadmin() {
		return this.fadmin;
	}

	public void setFadmin(Fadmin fadmin) {
		this.fadmin = fadmin;
	}

//	@Column(name = "fcashQty", precision = 16, scale = 6)
//	public Double getFcashQty() {
//		return this.fcashQty;
//	}
//
//	public void setFcashQty(Double fcashQty) {
//		this.fcashQty = fcashQty;
//	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fcreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fsalespercentId")
	public Fsalespercent getFsalespercent() {
		return fsalespercent;
	}

	public void setFsalespercent(Fsalespercent fsalespercent) {
		this.fsalespercent = fsalespercent;
	}
	
	@Transient
	public String getFstatus_s() {
		return SalescontractStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}
	
	@Column(name = "fgrade")
	public Integer getFgrade() {
		return fgrade;
	}

	public void setFgrade(Integer fgrade) {
		this.fgrade = fgrade;
	}

	@Transient
	public String getFgrade_s() {
		if(this.getFgrade() == null) return "";
		return UserGradeEnum.getEnumString(this.getFgrade());
	}

	public void setFgrade_s(String fgrade_s) {
		this.fgrade_s = fgrade_s;
	}

}