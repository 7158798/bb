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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.BalanceflowTypeEnum;
import com.ruizton.main.Enum.BalancelogTypeEnum;

/**
 * 小米钱袋流水帐
 */
@Entity
@Table(name = "fbalanceflow")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fbalanceflow implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fuser fuser;
	private Integer ftype;
	private String ftype_s;// BalanceflowTypeEnum类型
	private Double fqty;// 数量
	private Timestamp fcreatetime;
	private Fvirtualcointype fvirtualcointype;
	private Integer fflowtype;
	private String fflowtype_s;
	private int version;

	// Constructors

	/** default constructor */
	public Fbalanceflow() {
	}

	/** full constructor */
	public Fbalanceflow(Fuser fuser, Integer ftype, Double fqty,
			Timestamp fcreatetime) {
		this.fuser = fuser;
		this.ftype = ftype;
		this.fqty = fqty;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuserid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fqty", precision = 16, scale = 6)
	public Double getFqty() {
		return this.fqty;
	}

	public void setFqty(Double fqty) {
		this.fqty = fqty;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Transient
	public String getFtype_s() {
		return BalanceflowTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fvid")
	public Fvirtualcointype getFvirtualcointype() {
		return fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "fflowtype")
	public Integer getFflowtype() {
		return fflowtype;
	}

	public void setFflowtype(Integer fflowtype) {
		this.fflowtype = fflowtype;
	}

	@Transient
	public String getFflowtype_s() {
		return BalancelogTypeEnum.getEnumString(this.getFflowtype());
	}

	public void setFflowtype_s(String fflowtype_s) {
		this.fflowtype_s = fflowtype_s;
	}
}