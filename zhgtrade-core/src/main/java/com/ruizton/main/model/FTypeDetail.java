package com.ruizton.main.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ftypedetail")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FTypeDetail implements java.io.Serializable {

	// Fields

	private int fid;
	private int fviFid;				// 币ID
	private String desc;			// 描述
	private String name;			// 中文文名
	private String ename;			// 英文名
	private String developers;		// 研发者
	private String algorithm;		// 核心算法
	private Date releaseTime;		// 发布时间
	private String blocksTime;		// 区块时间
	private String blocksReward;	// 区块奖励
	private String amount;			// 货币数量
	private String remark;			// 备注信息
	private String walletLink;		// 钱包地址
	private String sourceLink;		// 源码地址
	private String whitePaperLink;	// 白皮书下载
	private String blockBrowserLink;// 白皮书下载
	private String feature;			// 特色
	private String defects;			// 不足之处
	private Date timestamp;

	// Constructors

	/** default constructor */
	public FTypeDetail() {
	}

	// Property accessors

	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@Column
	public int getFviFid() {
		return fviFid;
	}

	public void setFviFid(int fviFid) {
		this.fviFid = fviFid;
	}

	@Column(name = "fdesc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(length = 30, nullable = false)
	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 50)
	public String getDevelopers() {
		return developers;
	}

	public void setDevelopers(String developers) {
		this.developers = developers;
	}

	@Column(name = "falgorithm", length = 255)
	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	@Column
	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	@Column
	public String getBlocksTime() {
		return blocksTime;
	}

	public void setBlocksTime(String blocksTime) {
		this.blocksTime = blocksTime;
	}

	@Column(length = 255)
	public String getBlocksReward() {
		return blocksReward;
	}

	public void setBlocksReward(String blocksReward) {
		this.blocksReward = blocksReward;
	}

	@Column(length = 20)
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Column(length = 255)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(length = 255)
	public String getWalletLink() {
		return walletLink;
	}

	public void setWalletLink(String walletLink) {
		this.walletLink = walletLink;
	}

	@Column(length = 255)
	public String getSourceLink() {
		return sourceLink;
	}

	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}

	@Column(length = 255)
	public String getWhitePaperLink() {
		return whitePaperLink;
	}

	public void setWhitePaperLink(String whitePaperLink) {
		this.whitePaperLink = whitePaperLink;
	}

	@Column(name = "blockBrowser", length = 255)
	public String getBlockBrowserLink() {
		return blockBrowserLink;
	}

	public void setBlockBrowserLink(String blockBrowserLink) {
		this.blockBrowserLink = blockBrowserLink;
	}

	@Column
	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	@Column
	public String getDefects() {
		return defects;
	}

	public void setDefects(String defects) {
		this.defects = defects;
	}

	@Version
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}