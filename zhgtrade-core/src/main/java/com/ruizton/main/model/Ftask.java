package com.ruizton.main.model;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 * Ftask entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ftask")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Ftask implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer flevel;
	private String fname;
	private Set<FtaskLog> ftaskLogs = new HashSet<FtaskLog>(0);

	// Constructors

	/** default constructor */
	public Ftask() {
	}

	/** full constructor */
	public Ftask(Integer flevel, String fname, Set<FtaskLog> ftaskLogs) {
		this.flevel = flevel;
		this.fname = fname;
		this.ftaskLogs = ftaskLogs;
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

	@Column(name = "flevel")
	public Integer getFlevel() {
		return this.flevel;
	}

	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}

	@Column(name = "fname", length = 65535)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ftask")
	public Set<FtaskLog> getFtaskLogs() {
		return this.ftaskLogs;
	}

	public void setFtaskLogs(Set<FtaskLog> ftaskLogs) {
		this.ftaskLogs = ftaskLogs;
	}

}