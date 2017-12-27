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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fbalancetype entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fbalancetype")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fbalancetype implements java.io.Serializable {

	// Fields

	private Integer fid;
	private String fname;
	private Double frate;
	private Integer fday;
	private Timestamp fcreatetime;
	private Fvirtualcointype fvirtualcointype;

	// Constructors

	/** default constructor */
	public Fbalancetype() {
	}

	/** full constructor */
	public Fbalancetype(String fname, Double frate, Timestamp fcreatetime) {
		this.fname = fname;
		this.frate = frate;
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

	@Column(name = "fname", length = 50)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "frate", precision = 16, scale = 6)
	public Double getFrate() {
		return this.frate;
	}

	public void setFrate(Double frate) {
		this.frate = frate;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "fday")
	public Integer getFday() {
		return fday;
	}

	public void setFday(Integer fday) {
		this.fday = fday;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fvid")
	public Fvirtualcointype getFvirtualcointype() {
		return fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}
}