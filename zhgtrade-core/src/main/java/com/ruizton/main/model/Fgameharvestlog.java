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
import javax.persistence.Version;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.GameharvestlogStatusEnum;
import com.ruizton.main.Enum.GameharvestlogTypeEnum;

/**
 * Fgameharvestlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fgameharvestlog")
public class Fgameharvestlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer version;
	private Fuser fuser;
	private Fuser fintrolUser;
	private Integer fgroupqty;
	private Double fharvestqty;
	private Double ffrozenqty;
	private Integer fstatus;
	private String fstatus_s;
	private Integer ftype;
	private String ftype_s;
	private Timestamp fcreatetime;
    private boolean fissend;

	// Constructors


	/** default constructor */
	public Fgameharvestlog() {
	}

	/** full constructor */
	public Fgameharvestlog(Integer fuserid, Integer fgroupqty,
			Double fharvestqty, Integer fstatus, Integer ftype,
			Timestamp fcreatetime) {
		this.fgroupqty = fgroupqty;
		this.fharvestqty = fharvestqty;
		this.fstatus = fstatus;
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

	@Version
	@Column(name = "version")
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fuserid")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fintroluserid")
	public Fuser getFintrolUser() {
		return fintrolUser;
	}

	public void setFintrolUser(Fuser fintrolUser) {
		this.fintrolUser = fintrolUser;
	}

	@Column(name = "fgroupqty")
	public Integer getFgroupqty() {
		return this.fgroupqty;
	}

	public void setFgroupqty(Integer fgroupqty) {
		this.fgroupqty = fgroupqty;
	}

	@Column(name = "fharvestqty", precision = 16, scale = 6)
	public Double getFharvestqty() {
		return this.fharvestqty;
	}

	public void setFharvestqty(Double fharvestqty) {
		this.fharvestqty = fharvestqty;
	}

	@Column(name = "fstatus")
	public Integer getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(Integer fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}
	
	@Column(name = "ffrozenqty")
	public Double getFfrozenqty() {
		return ffrozenqty;
	}

	public void setFfrozenqty(Double ffrozenqty) {
		this.ffrozenqty = ffrozenqty;
	}
	
	@Column(name = "fissend")
	public boolean isFissend() {
		return fissend;
	}

	public void setFissend(boolean fissend) {
		this.fissend = fissend;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	
	@Transient
	public String getFstatus_s() {
		return GameharvestlogStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Transient
	public String getFtype_s() {
		return GameharvestlogTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}
}