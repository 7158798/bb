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

import com.ruizton.main.Enum.PaytypeEnum;
import com.ruizton.main.Enum.ShoppinglogStatusEnum;

/**
 * Fshoppinglog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fshoppinglog")
public class Fshoppinglog implements java.io.Serializable {

	private Integer fid;
	private Integer version;
	private Fgoods fgoods;//商品
	private Fuser fuser;//购买者
	private Double fpriceAmt;//消耗多少人民币
	private Double fpriceCoin;//消耗多少币
	private Integer fpaytype;
	private String fpaytype_s;
	private Integer fstatus;//
	private String fstatus_s;
	private String fpasswordA;
	private String fpasswordB;
	private Timestamp fcreatetime;
	private Timestamp fusetime;
	private Timestamp fsendtime;
	private String fphone;
	private String freceiveAddress;
	private String fexpressNo;
	private boolean fisShare;
	private Integer fqty;
	private Double fscore;

	// Constructors


	/** default constructor */
	public Fshoppinglog() {
	}

	/** full constructor */
	public Fshoppinglog(Fgoods fgoods, Fuser fuser, Double fpriceAmt,
			Double fpriceCoin, Integer fstatus, String fpasswordA,
			String fpasswordB, Timestamp fcreatetime, Timestamp fusetime,
			String fphone, String freceiveAddress, String fexpressNo) {
		this.fgoods = fgoods;
		this.fuser = fuser;
		this.fpriceAmt = fpriceAmt;
		this.fpriceCoin = fpriceCoin;
		this.fstatus = fstatus;
		this.fpasswordA = fpasswordA;
		this.fpasswordB = fpasswordB;
		this.fcreatetime = fcreatetime;
		this.fusetime = fusetime;
		this.fphone = fphone;
		this.freceiveAddress = freceiveAddress;
		this.fexpressNo = fexpressNo;
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
	@JoinColumn(name = "fgoodsid")
	public Fgoods getFgoods() {
		return this.fgoods;
	}

	public void setFgoods(Fgoods fgoods) {
		this.fgoods = fgoods;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuserid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fpriceAmt", precision = 16, scale = 6)
	public Double getFpriceAmt() {
		return this.fpriceAmt;
	}

	public void setFpriceAmt(Double fpriceAmt) {
		this.fpriceAmt = fpriceAmt;
	}

	@Column(name = "fpriceCoin", precision = 16, scale = 6)
	public Double getFpriceCoin() {
		return this.fpriceCoin;
	}

	public void setFpriceCoin(Double fpriceCoin) {
		this.fpriceCoin = fpriceCoin;
	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fpasswordA", length = 50)
	public String getFpasswordA() {
		return this.fpasswordA;
	}

	public void setFpasswordA(String fpasswordA) {
		this.fpasswordA = fpasswordA;
	}

	@Column(name = "fpasswordB", length = 50)
	public String getFpasswordB() {
		return this.fpasswordB;
	}

	public void setFpasswordB(String fpasswordB) {
		this.fpasswordB = fpasswordB;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}
	
	@Column(name = "fsendtime", length = 0)
	public Timestamp getFsendtime() {
		return fsendtime;
	}

	public void setFsendtime(Timestamp fsendtime) {
		this.fsendtime = fsendtime;
	}

	@Column(name = "fusetime", length = 0)
	public Timestamp getFusetime() {
		return this.fusetime;
	}

	public void setFusetime(Timestamp fusetime) {
		this.fusetime = fusetime;
	}

	@Column(name = "fphone", length = 50)
	public String getFphone() {
		return this.fphone;
	}

	public void setFphone(String fphone) {
		this.fphone = fphone;
	}

	@Column(name = "freceiveAddress", length = 300)
	public String getFreceiveAddress() {
		return this.freceiveAddress;
	}

	public void setFreceiveAddress(String freceiveAddress) {
		this.freceiveAddress = freceiveAddress;
	}

	@Column(name = "fexpressNo", length = 50)
	public String getFexpressNo() {
		return this.fexpressNo;
	}

	public void setFexpressNo(String fexpressNo) {
		this.fexpressNo = fexpressNo;
	}
	
	@Column(name = "fisShare")
	public boolean isFisShare() {
		return fisShare;
	}

	public void setFisShare(boolean fisShare) {
		this.fisShare = fisShare;
	}

	@Column(name = "fqty")
	public Integer getFqty() {
		return fqty;
	}

	public void setFqty(Integer fqty) {
		this.fqty = fqty;
	}
	
	@Column(name = "fpaytype")
	public Integer getFpaytype() {
		return fpaytype;
	}

	public void setFpaytype(Integer fpaytype) {
		this.fpaytype = fpaytype;
	}
	
	@Column(name = "fscore")
	public Double getFscore() {
		return fscore;
	}

	public void setFscore(Double fscore) {
		this.fscore = fscore;
	}

	@Transient
	public String getFpaytype_s() {
		return PaytypeEnum.getEnumString(this.getFpaytype());
	}

	public void setFpaytype_s(String fpaytype_s) {
		this.fpaytype_s = fpaytype_s;
	}
	
	@Transient
	public String getFstatus_s() {
		return ShoppinglogStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

}