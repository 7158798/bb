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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.ruizton.main.Enum.VirtualCoinTypeStatusEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.GenericGenerator;


/**
 * Fvirtualcointype entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fvirtualcointype")
public class Fvirtualcointype implements java.io.Serializable {

	private int fid;
	private int fid_s;
	private boolean fisShare;// 是否可以交易
	private boolean FIsWithDraw;// 是否可以提现
	private boolean FIsRecharge;// 是否可以充值
	private String fname;
	private String fShortName;
	private String fdescription;
	private Timestamp faddTime;
	private int fstatus;// VirtualCoinTypeStatusEnum
	private String fstatus_s;
	private String fSymbol;
	private String faccess_key;
	private String fsecrt_key;
	private String fip;
	private String fport;
	private double lastDealPrize;// fake,最新成交价格
	private double higestBuyPrize;
	private double lowestSellPrize;
	private double volumn;
	private boolean canLend;// 是否可以借贷
	private Set<Ffees> ffees = new HashSet<Ffees>(0);
	private Set<Fentrustplan> fentrustplans = new HashSet<Fentrustplan>(0);
	private Set<Fentrust> fentrusts = new HashSet<Fentrust>(0);
	private Set<Fvirtualcaptualoperation> fvirtualcaptualoperations = new HashSet<Fvirtualcaptualoperation>(
			0);
	private Set<Fvirtualwallet> fvirtualwallets = new HashSet<Fvirtualwallet>(0);
	private int version;
	private String furl;
	private String fintroUrl;//新增字段：项目简介url
	private double fopenPrice;//新增字段：开盘价格
	private double ftotalamount;//总量

	private String fname_sn;//新增
	private double fupanddown;//日涨跌

	private double fupanddownweek;//周涨跌
	private double fmarketValue;//总市值
	private double fentrustValue;//日成交额

	private String openTrade;
	// Constructors

	private int coinTradeType;			// 币币交易类型 1币主板/2币三板
	private int equityType;				// 权益交易 0不是/1是
	private boolean homeShow;			// true首页展示

	private int homeOrder;	//首页次序
	private int typeOrder; //板块次序
	private int totalOrder; //所有币的排序，用于充币提币
	private int confirmTimes;	// 充值确认次数
	private int isOtcActive;  //是否启用otc
	private boolean isIntroSend;   //是否开启推广送手续费
	private double introRate;  //推广送手续费费率
	public int getIsOtcActive() {
		return isOtcActive;
	}
	public void setIsOtcActive(int isOtcActive) {
		this.isOtcActive = isOtcActive;
	}
	public int getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}

	@Column(name="openTrade")
	public String getOpenTrade() {
		return openTrade;
	}

	public void setOpenTrade(String openTrade) {
		this.openTrade = openTrade;
	}

	/** default constructor */
	public Fvirtualcointype() {
	}

	/** full constructor */
	public Fvirtualcointype(String fname, String fdescription,
							Timestamp faddTime, Set<Fentrustplan> fentrustplans,
							Set<Fentrust> fentrusts,
							Set<Fvirtualcaptualoperation> fvirtualcaptualoperations,
							Set<Fvirtualwallet> fvirtualwallets) {
		this.fname = fname;
		this.fdescription = fdescription;
		this.faddTime = faddTime;
		this.fentrustplans = fentrustplans;
		this.fentrusts = fentrusts;
		this.fvirtualcaptualoperations = fvirtualcaptualoperations;
		this.fvirtualwallets = fvirtualwallets;
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

	@Column(name = "fName", length = 32)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fDescription", length = 32)
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	@Column(name = "fAddTime", length = 0)
	public Timestamp getFaddTime() {
		return this.faddTime;
	}

	public void setFaddTime(Timestamp faddTime) {
		this.faddTime = faddTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fvirtualcointype")
	public Set<Fentrustplan> getFentrustplans() {
		return this.fentrustplans;
	}

	public void setFentrustplans(Set<Fentrustplan> fentrustplans) {
		this.fentrustplans = fentrustplans;
	}

	@Transient
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fvirtualcointype")
	public Set<Fentrust> getFentrusts() {
		return this.fentrusts;
	}

	public void setFentrusts(Set<Fentrust> fentrusts) {
		this.fentrusts = fentrusts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fvirtualcointype")
	public Set<Fvirtualcaptualoperation> getFvirtualcaptualoperations() {
		return this.fvirtualcaptualoperations;
	}

	public void setFvirtualcaptualoperations(
			Set<Fvirtualcaptualoperation> fvirtualcaptualoperations) {
		this.fvirtualcaptualoperations = fvirtualcaptualoperations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fvirtualcointype")
	public Set<Ffees> getFfees() {
		return ffees;
	}

	public void setFfees(Set<Ffees> ffees) {
		this.ffees = ffees;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fvirtualcointype")
	public Set<Fvirtualwallet> getFvirtualwallets() {
		return this.fvirtualwallets;
	}

	public void setFvirtualwallets(Set<Fvirtualwallet> fvirtualwallets) {
		this.fvirtualwallets = fvirtualwallets;
	}

	@Column(name = "fstatus")
	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fShortName")
	public String getfShortName() {
		return fShortName;
	}

	public void setfShortName(String fShortName) {
		this.fShortName = fShortName;
	}

	@Transient
	public String getFstatus_s() {
		return VirtualCoinTypeStatusEnum.getEnumString(this.getFstatus());
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

	@Column(name = "fSymbol")
	public String getfSymbol() {
		return fSymbol;
	}

	public void setfSymbol(String fSymbol) {
		this.fSymbol = fSymbol;
	}

	@Column(name = "faccess_key")
	public String getFaccess_key() {
		return faccess_key;
	}

	public void setFaccess_key(String faccess_key) {
		this.faccess_key = faccess_key;
	}

	@Column(name = "fsecrt_key")
	public String getFsecrt_key() {
		return fsecrt_key;
	}

	public void setFsecrt_key(String fsecrt_key) {
		this.fsecrt_key = fsecrt_key;
	}

	@Column(name = "fip")
	public String getFip() {
		return fip;
	}

	public void setFip(String fip) {
		this.fip = fip;
	}

	@Column(name = "fport")
	public String getFport() {
		return fport;
	}

	public void setFport(String fport) {
		this.fport = fport;
	}

	@Transient
	public double getLastDealPrize() {
		return lastDealPrize;
	}

	public void setLastDealPrize(double lastDealPrize) {
		this.lastDealPrize = lastDealPrize;
	}

	@Transient
	public double getHigestBuyPrize() {
		return higestBuyPrize;
	}

	public void setHigestBuyPrize(double higestBuyPrize) {
		this.higestBuyPrize = higestBuyPrize;
	}

	@Transient
	public double getLowestSellPrize() {
		return lowestSellPrize;
	}

	public void setLowestSellPrize(double lowestSellPrize) {
		this.lowestSellPrize = lowestSellPrize;
	}

	@Transient
	public String getFid_s() {
		Integer id = this.getFid();
		if (id != null) {
			return String.valueOf(id);
		}
		return String.valueOf(0);
	}

	public void setFid_s(int fid_s) {
		this.fid_s = fid_s;
	}

	@Column(name = "fisShare")
	public boolean isFisShare() {
		return fisShare;
	}

	public void setFisShare(boolean fisShare) {
		this.fisShare = fisShare;
	}

	@Column(name = "FIsWithDraw")
	public boolean isFIsWithDraw() {
		return FIsWithDraw;
	}

	public void setFIsWithDraw(boolean fIsWithDraw) {
		FIsWithDraw = fIsWithDraw;
	}

	@Column(name = "furl")
	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}

	@Transient
	public double getVolumn() {
		return volumn;
	}

	public void setVolumn(double volumn) {
		this.volumn = volumn;
	}

	@Transient
	public boolean isCanLend() {
		return true;
	}

	public void setCanLend(boolean canLend) {
		this.canLend = canLend;
	}

	@Transient
	public String getFname_sn(){
		return this.fname_sn;
	}

	public void setFname_sn(String name_sn){
		if (name_sn!=null && name_sn.length()>10){
			name_sn = name_sn.substring(0,7)+"...";
		}
		this.fname_sn = name_sn;
	}

	@Column(name = "fintroUrl", length = 255)
	public String getFintroUrl(){
		return this.fintroUrl;
	}

	public void setFintroUrl(String fintroUrl){
		this.fintroUrl = fintroUrl;
	}

	@Transient
	public double getFupanddown(){
		return this.fupanddown;
	}

	public void setFupanddown(double fupanddown){
		this.fupanddown = fupanddown;
	}

	@Transient
	public double getFupanddownweek(){
		return this.fupanddownweek;
	}

	public void setFupanddownweek(double fupanddownweek){
		this.fupanddownweek = fupanddownweek;
	}

	@Transient
	public double getFmarketValue(){
		return this.fmarketValue;
	}

	public void setFmarketValue(double fmarketValue){
		this.fmarketValue = fmarketValue;
	}

	@Transient
	public double getFentrustValue(){
		return this.fentrustValue;
	}

	public void setFentrustValue(double fentrustValue){
		this.fentrustValue = fentrustValue;
	}


	@Column(name = "fopenPrice", precision = 12, scale = 0)
	public double getFopenPrice(){
		return this.fopenPrice;
	}

	public void setFopenPrice(double fopenPrice){
		this.fopenPrice = fopenPrice;
	}

	@Column(name = "ftotalAmount", precision = 12, scale = 0)
	public double getFtotalamount(){
		return this.ftotalamount;
	}

	public void setFtotalamount(double ftotalamount){
		this.ftotalamount = ftotalamount;
	}

	@Column(name = "coinTradeType")
	public int getCoinTradeType() {
		return coinTradeType;
	}

	public void setCoinTradeType(int coinTradeType) {
		this.coinTradeType = coinTradeType;
	}

	@Column(name = "equityType")
	public int getEquityType() {
		return equityType;
	}

	public void setEquityType(int equityType) {
		this.equityType = equityType;
	}

	@Column(name = "homeShow")
	public boolean isHomeShow() {
		return homeShow;
	}

	public void setHomeShow(boolean homeShow) {
		this.homeShow = homeShow;
	}

	public void setHomeOrder(int homeOrder) {
		this.homeOrder = homeOrder;
	}

	@Column(name = "homeOrder")
	public int getHomeOrder() {
		return homeOrder;
	}

	@Column(name = "typeOrder")
	public int getTypeOrder() {
		return typeOrder;
	}

	public void setTypeOrder(int typeOrder) {
		this.typeOrder = typeOrder;
	}

	@Column(name = "confirm_times")
	public int getConfirmTimes() {
		return confirmTimes;
	}

	public void setConfirmTimes(int confirmTimes) {
		this.confirmTimes = confirmTimes;
	}

	@Column(name = "FIsRecharge")
	public boolean isFIsRecharge() {
		return FIsRecharge;
	}

	public void setFIsRecharge(boolean FIsRecharge) {
		this.FIsRecharge = FIsRecharge;
	}

	public boolean isIntroSend() {
		return isIntroSend;
	}

	public void setIntroSend(boolean introSend) {
		isIntroSend = introSend;
	}

	public double getIntroRate() {
		return introRate;
	}

	public void setIntroRate(double introRate) {
		this.introRate = introRate;
	}

	//通过ID判断两个币种是否为同一个币种
	@Override
	public boolean equals(Object obj) {

		if(obj == null){
			return false;
		}
		if(obj instanceof Fvirtualcointype){
			Fvirtualcointype fvirtualcointype = (Fvirtualcointype) obj;
			if(fid == fvirtualcointype.getFid()){
				return true;
			}
		}
		return false;
	}
}