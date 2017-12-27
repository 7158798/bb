package com.ruizton.main.model;
// default package

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
import org.hibernate.annotations.GenericGenerator;

/**
 * 结算区间表
 */
@Entity
@Table(name = "fchargesection")
public class Fchargesection implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer fstartHour;//开始小时
	private Integer fendHour;//结束小时
	private Timestamp fcreatetime;
	private Set<Fdeduct> fdeducts = new HashSet<Fdeduct>(0);

	// Constructors

	/** default constructor */
	public Fchargesection() {
	}

	/** full constructor */
	public Fchargesection(Integer fstartHour, Integer fendHour,
			Timestamp fcreatetime, Set<Fdeduct> fdeducts) {
		this.fstartHour = fstartHour;
		this.fendHour = fendHour;
		this.fcreatetime = fcreatetime;
		this.fdeducts = fdeducts;
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

	@Column(name = "fstartHour")
	public Integer getFstartHour() {
		return this.fstartHour;
	}

	public void setFstartHour(Integer fstartHour) {
		this.fstartHour = fstartHour;
	}

	@Column(name = "fendHour")
	public Integer getFendHour() {
		return this.fendHour;
	}

	public void setFendHour(Integer fendHour) {
		this.fendHour = fendHour;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fchargesection")
	public Set<Fdeduct> getFdeducts() {
		return this.fdeducts;
	}

	public void setFdeducts(Set<Fdeduct> fdeducts) {
		this.fdeducts = fdeducts;
	}

}