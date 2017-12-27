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
import org.hibernate.annotations.GenericGenerator;

/**
 * Flotteryrule entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "flotteryrule")
public class Flotteryrule implements java.io.Serializable {

	// Fields

	private Integer fid;
	private String ftitle;
	private Fvirtualcointype fvirtualCoinTypeId;
	private Double fqty;//虚拟币数量
	private double fscore ;//积分
	private int fplayEggTimes ;//可以兑换多少次砸金蛋的机会

	private Timestamp fcreateTime;

	// Constructors

	/** default constructor */
	public Flotteryrule() {
	}

	/** full constructor */
	public Flotteryrule(String ftitle, Double fqty,
			Timestamp fcreateTime) {
		this.ftitle = ftitle;
		this.fqty = fqty;
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

	@Column(name = "ftitle", length = 100)
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fqty", precision = 16, scale = 6)
	public Double getFqty() {
		return this.fqty;
	}

	public void setFqty(Double fqty) {
		this.fqty = fqty;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fvirtualCoinTypeId")
	public Fvirtualcointype getFvirtualCoinTypeId() {
		return fvirtualCoinTypeId;
	}

	public void setFvirtualCoinTypeId(Fvirtualcointype fvirtualCoinTypeId) {
		this.fvirtualCoinTypeId = fvirtualCoinTypeId;
	}

	@Column(name = "fcreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name="fplayEggTimes")
	public int getFplayEggTimes() {
		return fplayEggTimes;
	}

	public void setFplayEggTimes(int fplayEggTimes) {
		this.fplayEggTimes = fplayEggTimes;
	}

	@Column(name="fscore")
	public double getFscore() {
		return fscore;
	}

	public void setFscore(double fscore) {
		this.fscore = fscore;
	}

	
	
}