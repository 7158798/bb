package com.ruizton.main.model;

// default package

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.UserGradeEnum;

/**
 * 业务员等级及分成对应表
 */
@Entity
@Table(name = "fsalespercent")
public class Fsalespercent implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer flevel;//等级
//	private Double fqty;//保证金，金豆数量
	private Double ftotalpercent;//总分成比例
	private Double feggpercent;//砸金蛋分成比便
	private Double fleaderpercent;//领导奖比例
	private Timestamp fcreatetime;
	private Integer fgrade;
	private String fgrade_s;

	// Constructors

	/** default constructor */
	public Fsalespercent() {
	}

	/** full constructor */
	public Fsalespercent(Integer flevel, Double fqty, Double ftotalpercent,
			Double feggpercent, Timestamp fcreatetime) {
		this.flevel = flevel;
//		this.fqty = fqty;
		this.ftotalpercent = ftotalpercent;
		this.feggpercent = feggpercent;
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

	@Column(name = "flevel")
	public Integer getFlevel() {
		return this.flevel;
	}

	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}
//
//	@Column(name = "fqty", precision = 16, scale = 6)
//	public Double getFqty() {
//		return this.fqty;
//	}
//
//	public void setFqty(Double fqty) {
//		this.fqty = fqty;
//	}

	@Column(name = "ftotalpercent", precision = 16, scale = 6)
	public Double getFtotalpercent() {
		return this.ftotalpercent;
	}

	public void setFtotalpercent(Double ftotalpercent) {
		this.ftotalpercent = ftotalpercent;
	}

	@Column(name = "feggpercent", precision = 16, scale = 6)
	public Double getFeggpercent() {
		return this.feggpercent;
	}

	public void setFeggpercent(Double feggpercent) {
		this.feggpercent = feggpercent;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "fleaderpercent", precision = 16, scale = 6)
	public Double getFleaderpercent() {
		return fleaderpercent;
	}

	public void setFleaderpercent(Double fleaderpercent) {
		this.fleaderpercent = fleaderpercent;
	}
	
	@Column(name = "fgrade")
	public Integer getFgrade() {
		return fgrade;
	}

	public void setFgrade(Integer fgrade) {
		this.fgrade = fgrade;
	}

	@Transient
	public String getFgrade_s() {
		if(this.getFgrade() == null) return "";
		return UserGradeEnum.getEnumString(this.getFgrade());
	}

	public void setFgrade_s(String fgrade_s) {
		this.fgrade_s = fgrade_s;
	}
}