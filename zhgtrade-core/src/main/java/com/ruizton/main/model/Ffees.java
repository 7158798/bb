package com.ruizton.main.model;

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
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

/**
 * Ffees entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ffees")
public class Ffees implements java.io.Serializable {

	// Fields

	private int fid;
	private double ffee;// 交易手续费
	private double withdraw;// 提现手续费
	private int flevel ;
	private Fvirtualcointype fvirtualcointype ;
	private int version;

	// Constructors

	/** default constructor */
	public Ffees() {
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public int getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@Column(name = "ffee", precision = 15, scale = 0)
	public double getFfee() {
		return this.ffee;
	}

	public void setFfee(double ffee) {
		this.ffee = ffee;
	}

	public double getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(double withdraw) {
		this.withdraw = withdraw;
	}

	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fvir_id")
	public Fvirtualcointype getFvirtualcointype() {
		return fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@Column(name="flevel")
	public int getFlevel() {
		return flevel;
	}

	public void setFlevel(int flevel) {
		this.flevel = flevel;
	}
	
	

}