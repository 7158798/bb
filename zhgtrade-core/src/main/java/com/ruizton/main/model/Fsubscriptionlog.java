package com.ruizton.main.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

/**
 * Fsubscriptionlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fsubscriptionlog")
public class Fsubscriptionlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fsubscription fsubscription;
	private Fuser fuser;//用户
	private Double fcount;//认购数量
	private Double fprice;//认购价格
	private Double ftotalCost;//总消费
	private Timestamp fcreatetime;
	private Double flastQty;
	private int version;

	// Constructors


	/** default constructor */
	public Fsubscriptionlog() {
	}

	/** full constructor */
	public Fsubscriptionlog(Fsubscription fsubscription, Fuser fuser,
			Double fcount, Double fprice, Double ftotalCost,
			Timestamp fcreatetime) {
		this.fsubscription = fsubscription;
		this.fuser = fuser;
		this.fcount = fcount;
		this.fprice = fprice;
		this.ftotalCost = ftotalCost;
		this.fcreatetime = fcreatetime;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fsub_id")
	public Fsubscription getFsubscription() {
		return this.fsubscription;
	}

	public void setFsubscription(Fsubscription fsubscription) {
		this.fsubscription = fsubscription;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuser_fid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fcount", precision = 16, scale = 6)
	public Double getFcount() {
		return this.fcount;
	}

	public void setFcount(Double fcount) {
		this.fcount = fcount;
	}

	@Column(name = "fprice", precision = 16, scale = 6)
	public Double getFprice() {
		return this.fprice;
	}

	public void setFprice(Double fprice) {
		this.fprice = fprice;
	}

	@Column(name = "ftotalCost", precision = 16, scale = 6)
	public Double getFtotalCost() {
		return this.ftotalCost;
	}

	public void setFtotalCost(Double ftotalCost) {
		this.ftotalCost = ftotalCost;
	}
	
	@Column(name = "flastQty", precision = 16, scale = 6)
	public Double getFlastQty() {
		return flastQty;
	}

	public void setFlastQty(Double flastQty) {
		this.flastQty = flastQty;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}
	
	@Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	

}