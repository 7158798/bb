package com.ruizton.main.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.main.Enum.LendRuleTypeEnum;
import com.ruizton.main.Enum.UserGradeEnum;

/**
 * Flendrule entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "flendrule")
//@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Flendrule implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer ftype;//LendRuleTypeEnum
	private String ftype_s;
	private Integer flevel;
	private String flevel_s;
	private Double famount;
	private Double frate;
	private Timestamp fcreate;
	private int version;


	// Constructors

	/** default constructor */
	public Flendrule() {
	}

	/** full constructor */
	public Flendrule(Integer ftype, Integer flevel, Double famount,
			Double frate, Timestamp fcreate) {
		this.ftype = ftype;
		this.flevel = flevel;
		this.famount = famount;
		this.frate = frate;
		this.fcreate = fcreate;
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

	@Column(name = "ftype")
	public Integer getFtype() {
		return this.ftype;
	}

	public void setFtype(Integer ftype) {
		this.ftype = ftype;
	}

	@Column(name = "flevel")
	public Integer getFlevel() {
		return this.flevel;
	}

	public void setFlevel(Integer flevel) {
		this.flevel = flevel;
	}

	@Column(name = "famount", precision = 16, scale = 6)
	public Double getFamount() {
		return this.famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
	}

	@Column(name = "frate", precision = 16, scale = 6)
	public Double getFrate() {
		return this.frate;
	}

	public void setFrate(Double frate) {
		this.frate = frate;
	}

	@Column(name = "fcreate", length = 19)
	public Timestamp getFcreate() {
		return this.fcreate;
	}

	public void setFcreate(Timestamp fcreate) {
		this.fcreate = fcreate;
	}
	
	@Transient
	public String getFtype_s() {
		return LendRuleTypeEnum.getEnumString(this.getFtype());
	}

	public void setFtype_s(String ftype_s) {
		this.ftype_s = ftype_s;
	}

	
	@Version
	@Column(name = "version")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	
	@Transient
	public String getFlevel_s() {
		if(this.getFtype() == LendRuleTypeEnum.LEND_TIME){
			return this.getFlevel()+"";
		}else{
			return UserGradeEnum.getEnumString(this.getFlevel());
		}
	}

	public void setFlevel_s(String flevel_s) {
		this.flevel_s = flevel_s;
	}
}