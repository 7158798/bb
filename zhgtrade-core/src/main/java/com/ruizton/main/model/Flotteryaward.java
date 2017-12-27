package com.ruizton.main.model;

// default package

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.LotteryRewardTypeEnum;

/**
 * Flotteryaward entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "flotteryaward")
public class Flotteryaward implements java.io.Serializable {

	// Fields

	private Integer fid;
	private int type; // LotteryRewardTypeEnum
	private String ftype_s;
	private double count;// 数量
	private String fname;
	private BigDecimal fchance;
	private double ftotal ;
	private int version ;

	// Constructors

	/** default constructor */
	public Flotteryaward() {
	}

	/** full constructor */
	public Flotteryaward(String fname, BigDecimal fchance) {
		this.fname = fname;
		this.fchance = fchance;
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

	@Column(name = "fname", length = 200)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fchance", precision = 16, scale = 6)
	public BigDecimal getFchance() {
		return this.fchance;
	}

	public void setFchance(BigDecimal fchance) {
		this.fchance = fchance;
	}

	@Column(name = "type")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Column(name = "count")
	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	@Transient
	public String getFtype_s() {
		return LotteryRewardTypeEnum.getEnumString(this.getType());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}

	@Column(name="ftotal")
	public double getFtotal() {
		return ftotal;
	}

	public void setFtotal(double ftotal) {
		this.ftotal = ftotal;
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