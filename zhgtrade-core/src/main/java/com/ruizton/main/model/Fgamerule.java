package com.ruizton.main.model;

// default package

import java.math.BigDecimal;
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
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.GameTypeEnum;
import com.ruizton.util.Utils;

/**
 * Fgamerule entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fgamerule")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fgamerule implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer flevel;//等级
	
	private Fvirtualcointype fupgradeCoinType;//升级
	private BigDecimal fupgradeNeedQty;//升级所需豆数量
	private Integer fupgradeNeedReQty;//升级所需推荐注册数
	
	
	private Fvirtualcointype fvirtualcointype;//收获与种值虚拟币类型
	private BigDecimal fexpendQty;//种土消耗数量
	private BigDecimal fcanZdtimes;//可种豆时长
	private BigDecimal fharvestQty;//每小时收获数量
	
	
	//购买土地
	private Fvirtualcointype fvirtualcointype1;//购买土地消耗
	private BigDecimal fbuyQty;//每块土地数量
	
	private BigDecimal fbadRate;//减产概率
	private BigDecimal fgoodRate;//增加概率

	private int ftype;
	private String ftype_s;//种植类型

	private int fsendqty;//赠送土地
	private int ftotalqty;//一共可以有几块土地

	private Timestamp flastUpdateTime;//最后修改时间
	private Set<Fgamelog> fgamelogs = new HashSet<Fgamelog>(0);
	private int version ;
	// Constructors
	
	/** default constructor */
	public Fgamerule() {
	}

	/** full constructor */
	public Fgamerule(Fadmin fadmin, Integer flevel,
			BigDecimal fcanZdtimes, BigDecimal fharvestQty, Integer fharvestRate,
			Integer ftimeSpan, BigDecimal fupgradeNeedQty,
			Integer fupgradeNeedReQty, Timestamp flastUpdateTime,
			Set<Fgamelog> fgamelogs) {
		this.flevel = flevel;
		this.fcanZdtimes = fcanZdtimes;
		this.fharvestQty = fharvestQty;
		this.fupgradeNeedQty = fupgradeNeedQty;
		this.fupgradeNeedReQty = fupgradeNeedReQty;
		this.flastUpdateTime = flastUpdateTime;
		this.fgamelogs = fgamelogs;
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

	@JoinColumn(name = "flevel")
	public Integer getFlevel() {
		return this.flevel;
	}

	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}

	@Column(name = "fcanZDTimes", precision = 2, scale = 1)
	public BigDecimal getFcanZdtimes() {
		return this.fcanZdtimes;
	}

	public void setFcanZdtimes(BigDecimal fcanZdtimes) {
		this.fcanZdtimes = fcanZdtimes;
	}

	@Column(name = "fharvestQty", precision = 16, scale = 6)
	public BigDecimal getFharvestQty() {
		return this.fharvestQty;
	}

	public void setFharvestQty(BigDecimal fharvestQty) {
		this.fharvestQty = fharvestQty;
	}

	
	@Column(name = "fupgradeNeedQty", precision = 16, scale = 6)
	public BigDecimal getFupgradeNeedQty() {
		return this.fupgradeNeedQty;
	}

	public void setFupgradeNeedQty(BigDecimal fupgradeNeedQty) {
		this.fupgradeNeedQty = fupgradeNeedQty;
	}

	@Column(name = "fupgradeNeedReQty")
	public Integer getFupgradeNeedReQty() {
		return this.fupgradeNeedReQty;
	}

	public void setFupgradeNeedReQty(Integer fupgradeNeedReQty) {
		this.fupgradeNeedReQty = fupgradeNeedReQty;
	}

	@Column(name = "flastUpdateTime", length = 19)
	public Timestamp getFlastUpdateTime() {
		return this.flastUpdateTime;
	}

	public void setFlastUpdateTime(Timestamp flastUpdateTime) {
		this.flastUpdateTime = flastUpdateTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fgamerule")
	public Set<Fgamelog> getFgamelogs() {
		return this.fgamelogs;
	}

	public void setFgamelogs(Set<Fgamelog> fgamelogs) {
		this.fgamelogs = fgamelogs;
	}
	
	@Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fvirtualCoinTypeId")
	public Fvirtualcointype getFvirtualcointype() {
		return this.fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fvirtualCoinTypeId1")
	public Fvirtualcointype getFvirtualcointype1() {
		return fvirtualcointype1;
	}

	public void setFvirtualcointype1(Fvirtualcointype fvirtualcointype1) {
		this.fvirtualcointype1 = fvirtualcointype1;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fupgradeCoinTypeId")
	public Fvirtualcointype getFupgradeCoinType() {
		return fupgradeCoinType;
	}

	public void setFupgradeCoinType(Fvirtualcointype fupgradeCoinType) {
		this.fupgradeCoinType = fupgradeCoinType;
	}

	
	@Column(name = "fexpendQty", precision = 16, scale = 6)
	public BigDecimal getFexpendQty() {
		return fexpendQty;
	}

	public void setFexpendQty(BigDecimal fexpendQty) {
		this.fexpendQty = fexpendQty;
	}
	
	@Column(name = "fbuyQty", precision = 16, scale = 6)
	public BigDecimal getFbuyQty() {
		return fbuyQty;
	}

	public void setFbuyQty(BigDecimal fbuyQty) {
		this.fbuyQty = fbuyQty;
	}

	@Column(name = "fbadRate", precision = 16, scale = 6)
	public BigDecimal getFbadRate() {
		return fbadRate;
	}

	public void setFbadRate(BigDecimal fbadRate) {
		this.fbadRate = fbadRate;
	}

	@Column(name = "fgoodRate", precision = 16, scale = 6)
	public BigDecimal getFgoodRate() {
		return fgoodRate;
	}

	public void setFgoodRate(BigDecimal fgoodRate) {
		this.fgoodRate = fgoodRate;
	}
	
	@Column(name = "ftype")
	public int getFtype() {
		return ftype;
	}

	public void setFtype(int ftype) {
		this.ftype = ftype;
	}
	
	@Column(name = "fsendqty")
	public int getFsendqty() {
		return fsendqty;
	}

	public void setFsendqty(int fsendqty) {
		this.fsendqty = fsendqty;
	}

	@Column(name = "ftotalqty")
	public int getFtotalqty() {
		return ftotalqty;
	}

	public void setFtotalqty(int ftotalqty) {
		this.ftotalqty = ftotalqty;
	}

	@Transient
	public String getFtype_s() {
		return GameTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}
}