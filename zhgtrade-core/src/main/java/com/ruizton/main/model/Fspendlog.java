package com.ruizton.main.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.SpendLogTypeEnum;

/**
 * Fspendlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fspendlog")
public class Fspendlog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fuser fuser;
	private Integer ftype;//SpendLogTypeEnum
	private Double fgdouSpend;
	private Double fscore;
	private Timestamp fcreatetime;
	private boolean fischarge;

	
	/** default constructor */
	public Fspendlog() {
	}

	/** full constructor */
	public Fspendlog(Fuser fuser, Integer ftype, Double fgdouSpend,
			Double fscore, Timestamp fcreatetime) {
		this.fuser = fuser;
		this.ftype = ftype;
		this.fgdouSpend = fgdouSpend;
		this.fscore = fscore;
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
	@JoinColumn(name = "fuser")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "fgdouSpend", precision = 16, scale = 6)
	public Double getFgdouSpend() {
		return this.fgdouSpend;
	}

	public void setFgdouSpend(Double fgdouSpend) {
		this.fgdouSpend = fgdouSpend;
	}

	@Column(name = "fscore", precision = 16, scale = 6)
	public Double getFscore() {
		return this.fscore;
	}

	public void setFscore(Double fscore) {
		this.fscore = fscore;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}
	
	@Column(name = "fischarge")
	public boolean isFischarge() {
		return fischarge;
	}

	public void setFischarge(boolean fischarge) {
		this.fischarge = fischarge;
	}


}