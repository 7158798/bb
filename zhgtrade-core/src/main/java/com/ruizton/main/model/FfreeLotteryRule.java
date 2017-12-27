package com.ruizton.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * FfreeLotteryRule entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ffree_lottery_rule")
public class FfreeLotteryRule implements java.io.Serializable {

	// Fields

	private Integer fid;
	private String fname;
	private Integer ffrom;
	private Integer fto;
	private Double freward;

	// Constructors

	/** default constructor */
	public FfreeLotteryRule() {
	}

	/** full constructor */
	public FfreeLotteryRule(String fname, Integer ffrom, Integer fto,
			Double freward) {
		this.fname = fname;
		this.ffrom = ffrom;
		this.fto = fto;
		this.freward = freward;
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

	@Column(name = "fname", length = 32)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "ffrom")
	public Integer getFfrom() {
		return this.ffrom;
	}

	public void setFfrom(Integer ffrom) {
		this.ffrom = ffrom;
	}

	@Column(name = "fto")
	public Integer getFto() {
		return this.fto;
	}

	public void setFto(Integer fto) {
		this.fto = fto;
	}

	@Column(name = "freward", precision = 16, scale = 6)
	public Double getFreward() {
		return this.freward;
	}

	public void setFreward(Double freward) {
		this.freward = freward;
	}

}