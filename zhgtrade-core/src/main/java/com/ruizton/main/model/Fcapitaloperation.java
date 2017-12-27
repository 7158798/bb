package com.ruizton.main.model;

import java.sql.Timestamp;
import javax.persistence.*;

import com.ruizton.main.Enum.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Fcapitaloperation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fcapitaloperation")
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Fcapitaloperation implements java.io.Serializable {

	// Fields

	private int fid;
	private Systembankinfo systembankinfo;// 用户向这个账号汇款,只有充值用
	private Fuser fuser;
	private Timestamp fcreateTime;
	private Timestamp fLastUpdateTime;

	private double famount;
	private int ftype;// CapitalOperationTypeEnum，汇款or提现
	private String ftype_s;
	private int fstatus;// CapitalOperationInStatus||CapitalOperationOutStatus
	private String fstatus_s;
	private int fremittanceType;// RemittanceTypeEnum汇款方式
	private ChargePayTypeEnum payType;		// 充值类型

	private String outTradeNo;		// 外部交易号
	private double actualAmount;	// 实际到账金额

	private String fremark;// 用不着
	private String fBank;
	private String fProvince;//省份
	private String fCity;//市区
	private String fBranch;//支行
	private String fAccount;
	private String faccount_s;
	private String fPayee;// 汇款人or收款人
	private String fPhone;// 汇款人手机账号
	private Fadmin fAuditee_id ;
	private double ffees ;//手续费
	private int version ;
	private boolean fischarge;
	
	/** default constructor */
	public Fcapitaloperation() {
	}

     
	/** full constructor */
	public Fcapitaloperation(Fbankinfo fbankinfo,
			Systembankinfo systembankinfo, Fuser fuser, Timestamp fcreateTime,
			double famount, int ftype, int fstatus,
			int fremittanceType, String fremark) {
		this.systembankinfo = systembankinfo;
		this.fuser = fuser;
		this.fcreateTime = fcreateTime;
		this.famount = famount;
		this.ftype = ftype;
		this.fstatus = fstatus;
		this.fremittanceType = fremittanceType;
		this.fremark = fremark;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fId", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Sys_fId")
	public Systembankinfo getSystembankinfo() {
		return this.systembankinfo;
	}

	public void setSystembankinfo(Systembankinfo systembankinfo) {
		this.systembankinfo = systembankinfo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FUs_fId")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fCreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fAmount", precision = 12, scale = 0)
	public double getFamount() {
		return this.famount;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	@Column(name = "fType")
	public int getFtype() {
		return this.ftype;
	}

	public void setFtype(int ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fStatus")
	public int getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "chargePayType")
	public ChargePayTypeEnum getPayType() {
		return payType;
	}

	public void setPayType(ChargePayTypeEnum payType) {
		this.payType = payType;
	}

	@Transient
	public String getPayType_s(){
		return this.payType.getName();
	}

	@Column(name = "fRemittanceType")
	public int getFremittanceType() {
		return this.fremittanceType;
	}

	public void setFremittanceType(int fremittanceType) {
		this.fremittanceType = fremittanceType;
	}

	@Column(name = "fRemark", length = 256)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Transient
	public String getFstatus_s() {
		int status = this.getFstatus();
		if (this.getFtype() == CapitalOperationTypeEnum.RMB_IN) {
			return CapitalOperationInStatus.getEnumString(status);
		} else {
			return CapitalOperationOutStatus.getEnumString(status);
		}
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Column(name = "fBank", length = 256)
	public String getfBank() {
		return fBank;
	}

	public void setfBank(String fBank) {
		this.fBank = fBank;
	}
	
	@Column(name = "fProvince", length = 128)
	public String getFProvince() {
		return fProvince;
	}

	public void setFProvince(String fProvince) {
		this.fProvince = fProvince;
	}
	
	@Column(name = "fCity", length = 128)
	public String getFCity() {
		return fCity;
	}

	public void setFCity(String fCity) {
		this.fCity = fCity;
	}
	
	@Column(name = "fBranch", length = 128)
	public String getFBranch() {
		return fBranch;
	}

	public void setFBranch(String fBranch) {
		this.fBranch = fBranch;
	}

	@Column(name = "fAccount", length = 256)
	public String getfAccount() {
		return fAccount;
	}

	public void setfAccount(String fAccount) {
		this.fAccount = fAccount;
	}

	@Column(name = "fPayee", length = 256)
	public String getfPayee() {
		return fPayee;
	}

	public void setfPayee(String fPayee) {
		this.fPayee = fPayee;
	}

	@Column(name = "fPhone", length = 256)
	public String getfPhone() {
		return fPhone;
	}

	public void setfPhone(String fPhone) {
		this.fPhone = fPhone;
	}

	@Transient
	public String getFtype_s() {
		return CapitalOperationTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}

	// @Transient
	// public String getFremittanceType_s() {
	// return RemittanceTypeEnum.getEnumString(this.getFremittanceType());
	// }
	//
	// public void setFremittanceType_s(String fremittanceType_s) {
	// this.fremittanceType_s = fremittanceType_s;
	// }


	@Column( name="fLastUpdateTime" )
	public Timestamp getfLastUpdateTime() {
		return fLastUpdateTime;
	}


	public void setfLastUpdateTime(Timestamp fLastUpdateTime) {
		this.fLastUpdateTime = fLastUpdateTime;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fAuditee_id")
	public Fadmin getfAuditee_id() {
		return fAuditee_id;
	}


	public void setfAuditee_id(Fadmin fAuditee_id) {
		this.fAuditee_id = fAuditee_id;
	}


	@Column(name="ffees")
	public double getFfees() {
		return ffees;
	}


	public void setFfees(double ffees) {
		this.ffees = ffees;
	}
	
	@Column(name="fischarge")
	public boolean isFischarge() {
		return fischarge;
	}

	public void setFischarge(boolean fischarge) {
		this.fischarge = fischarge;
	}
	
	@Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(length = 128)
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@Column
	public double getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}

	@Transient
	public String getFaccount_s() {
		String account = this.getfAccount();
		if(account == null || account.trim().length() == 0) return "";
		return account.replaceAll(" ", "");
	}

	public void setFaccount_s(String faccount_s) {
		this.faccount_s = faccount_s;
	}
}
