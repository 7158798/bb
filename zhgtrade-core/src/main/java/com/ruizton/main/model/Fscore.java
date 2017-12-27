package com.ruizton.main.model;

import java.util.Date;
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
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fscore entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fscore")
@JsonIgnoreProperties({
		"fscore",
		"fusers",
		"version",
		"fgroupqty",
		"ffarmlevel",
		"fisopen",
		"fissend",
		"fissign",
		"feffecttime",
		"fendtime",
})
public class Fscore implements java.io.Serializable {

	// Fields

	private int fid;
	private double fscore;
	private int flevel;
	private Set<Fuser> fusers = new HashSet<Fuser>(0);
	private int version;
	private int fgroupqty;//土地数量
	private int ffarmlevel;//农场等级
	private boolean fisopen;//是否开通农场
	//	private int ftreeqty;
//	private int fkillQty;
	private boolean fissend;
	private boolean fissign;
	private Date feffecttime;
	private Date fendtime;

	// Constructors



	/** default constructor */
	public Fscore() {
	}

	/** full constructor */
	public Fscore(int fscore, int flevel, Set<Fuser> fusers) {
		this.fscore = fscore;
		this.flevel = flevel;
		this.fusers = fusers;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@Column(name = "fscore")
	public double getFscore() {
		return this.fscore;
	}

	public void setFscore(double fscore) {
		this.fscore = fscore;
	}

	@Column(name = "flevel")
	public int getFlevel() {
		return this.flevel;
	}

	public void setFlevel(int flevel) {
		this.flevel = flevel;
	}
	
	@Column(name = "fgroupqty")
	public int getFgroupqty() {
		return fgroupqty;
	}

	public void setFgroupqty(int fgroupqty) {
		this.fgroupqty = fgroupqty;
	}

	@Column(name = "ffarmlevel")
	public int getFfarmlevel() {
		return ffarmlevel;
	}

	public void setFfarmlevel(int ffarmlevel) {
		this.ffarmlevel = ffarmlevel;
	}

    @Column(name = "fisopen")
	public boolean isFisopen() {
		return fisopen;
	}

	public void setFisopen(boolean fisopen) {
		this.fisopen = fisopen;
	}

	@Column(name = "fissend")
	public boolean isFissend() {
		return fissend;
	}

	public void setFissend(boolean fissend) {
		this.fissend = fissend;
	}
	
	@Column(name = "fissign")
	public boolean isFissign() {
		return fissign;
	}

	public void setFissign(boolean fissign) {
		this.fissign = fissign;
	}
	
	@Column(name = "feffecttime")
	public Date getFeffecttime() {
		return feffecttime;
	}

	public void setFeffecttime(Date feffecttime) {
		this.feffecttime = feffecttime;
	}

	@Column(name = "fendtime")
	public Date getFendtime() {
		return fendtime;
	}

	public void setFendtime(Date fendtime) {
		this.fendtime = fendtime;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fscore")
	public Set<Fuser> getFusers() {
		return this.fusers;
	}

	public void setFusers(Set<Fuser> fusers) {
		this.fusers = fusers;
	}

	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}