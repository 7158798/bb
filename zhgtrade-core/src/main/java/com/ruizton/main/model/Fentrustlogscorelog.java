package com.ruizton.main.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fentrustlogscorelog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fentrustlogscorelog")
public class Fentrustlogscorelog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer fentrustlogId;
	private Integer fuserId;
	private Double fscore;
	private Timestamp fcreateTime;

	// Constructors

	/** default constructor */
	public Fentrustlogscorelog() {
	}

	/** full constructor */
	public Fentrustlogscorelog(Integer fentrustlogId, Integer fuserId,
			Double fscore, Timestamp fcreateTime) {
		this.fentrustlogId = fentrustlogId;
		this.fuserId = fuserId;
		this.fscore = fscore;
		this.fcreateTime = fcreateTime;
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

	@Column(name = "fentrustlogId")
	public Integer getFentrustlogId() {
		return this.fentrustlogId;
	}

	public void setFentrustlogId(Integer fentrustlogId) {
		this.fentrustlogId = fentrustlogId;
	}

	@Column(name = "fuserId")
	public Integer getFuserId() {
		return this.fuserId;
	}

	public void setFuserId(Integer fuserId) {
		this.fuserId = fuserId;
	}

	@Column(name = "fscore", precision = 16, scale = 6)
	public Double getFscore() {
		return this.fscore;
	}

	public void setFscore(Double fscore) {
		this.fscore = fscore;
	}

	@Column(name = "fcreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

}