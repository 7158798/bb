package com.ruizton.main.model;
// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fhedginginfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fhedginginfo")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fhedginginfo implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer version;
	private String ftitle;
	private Double frate;//手续费比例
	private Timestamp fcreatetime;

	// Constructors

	/** default constructor */
	public Fhedginginfo() {
	}

	/** full constructor */
	public Fhedginginfo(String ftitle, Double frate, Timestamp fcreatetime) {
		this.ftitle = ftitle;
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

	@Version
	@Column(name = "version")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "ftitle", length = 500)
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
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

}