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

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.ChancelogtypeEnum;

/**
 * Fchancelog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fchancelog")
public class Fchancelog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fuser fuser;
	private Double fqty1;
	private Double fqty2;
	private Integer ftype;//ChancelogtypeEnum
	private String ftype_s;
	private Timestamp fcreatetime;

	// Constructors

	/** default constructor */
	public Fchancelog() {
	}

	/** full constructor */
	public Fchancelog(Fuser fuser, Double fqty1, Double fqty2, Integer ftype,
			Timestamp fcreatetime) {
		this.fuser = fuser;
		this.fqty1 = fqty1;
		this.fqty2 = fqty2;
		this.ftype = ftype;
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
	@JoinColumn(name = "fuserId")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fqty1", precision = 16, scale = 6)
	public Double getFqty1() {
		return this.fqty1;
	}

	public void setFqty1(Double fqty1) {
		this.fqty1 = fqty1;
	}

	@Column(name = "fqty2", precision = 16, scale = 6)
	public Double getFqty2() {
		return this.fqty2;
	}

	public void setFqty2(Double fqty2) {
		this.fqty2 = fqty2;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
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
		return ChancelogtypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}
}