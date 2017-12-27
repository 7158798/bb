package com.ruizton.main.model;
// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.PopcornResult1Enum;

/**
 * Fbetlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fbetlog")
public class Fbetlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Double fbetqty;
	private Double fwinqty;
	private Timestamp fcreatetime;
	private int fpopcornlogId;
	private int fbetResult;
	private String fbetResult_s;

	/** default constructor */
	public Fbetlog() {
	}

	/** full constructor */
	public Fbetlog(Double fbetqty, Timestamp fcreatetime) {
		this.fbetqty = fbetqty;
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

	@Column(name = "fbetqty", precision = 16, scale = 6)
	public Double getFbetqty() {
		return this.fbetqty;
	}

	public void setFbetqty(Double fbetqty) {
		this.fbetqty = fbetqty;
	}
	
	@Column(name = "fwinqty", precision = 16, scale = 6)
	public Double getFwinqty() {
		return fwinqty;
	}

	public void setFwinqty(Double fwinqty) {
		this.fwinqty = fwinqty;
	}
	
	@Column(name = "fpopcornlogId")
	public int getFpopcornlogId() {
		return fpopcornlogId;
	}

	public void setFpopcornlogId(int fpopcornlogId) {
		this.fpopcornlogId = fpopcornlogId;
	}
	
	@Column(name = "fbetResult")
	public int getFbetResult() {
		return fbetResult;
	}

	public void setFbetResult(int fbetResult) {
		this.fbetResult = fbetResult;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

    @Transient
	public String getFbetResult_s() {
		return PopcornResult1Enum.getEnumString(this.getFbetResult());
	}

	public void setFbetResult_s(String fbetResult_s) {
		this.fbetResult_s = fbetResult_s;
	}
}