package com.ruizton.main.model;

// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fpopcorn entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fpopcorn")
public class Fpopcorn implements java.io.Serializable {

	// Fields

	private Integer fid;
	private String ftitle;//简介
	private Integer ftime;//多少分钟开一次奖
	private double frate;
	private Timestamp fcreatetime;//创建时间
	private Integer fspantime;
	private double fbetRate;

	/** default constructor */
	public Fpopcorn() {
	}

	/** full constructor */
	public Fpopcorn(String ftitle, Integer ftime, Timestamp fcreatetime) {
		this.ftitle = ftitle;
		this.ftime = ftime;
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

	@Column(name = "ftitle", length = 500)
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "ftime")
	public Integer getFtime() {
		return this.ftime;
	}

	public void setFtime(Integer ftime) {
		this.ftime = ftime;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "frate")
	public double getFrate() {
		return frate;
	}

	public void setFrate(double frate) {
		this.frate = frate;
	}
	
	@Column(name = "fspantime")
	public Integer getFspantime() {
		return fspantime;
	}

	public void setFspantime(Integer fspantime) {
		this.fspantime = fspantime;
	}
	
	@Column(name = "fbetRate")
	public double getFbetRate() {
		return fbetRate;
	}

	public void setFbetRate(double fbetRate) {
		this.fbetRate = fbetRate;
	}
}