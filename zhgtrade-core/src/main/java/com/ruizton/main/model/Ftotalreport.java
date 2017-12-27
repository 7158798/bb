package com.ruizton.main.model;

// default package

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * Ftotalreport entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ftotalreport")
public class Ftotalreport implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Date fdate;
	private Double ftotalAmt;
	private Double ftotalCanAmt;
	private Double ftotalFrozenAmt;
	private Double ftotalRechargeAmt;
	private Double ftotalWithdrawAmt;
	private Double ftotalDou;
	private Double ftotalCanDou;
	private Double ftotalFrozenDou;
	private Double ftotalCreateDou;

	// Constructors

	/** default constructor */
	public Ftotalreport() {
	}

	/** full constructor */
	public Ftotalreport(Date fdate, Double ftotalAmt, Double ftotalCanAmt,
			Double ftotalFrozenAmt, Double ftotalRechargeAmt,
			Double ftotalWithdrawAmt, Double ftotalDou, Double ftotalCanDou,
			Double ftotalFrozenDou, Double ftotalCreateDou) {
		this.fdate = fdate;
		this.ftotalAmt = ftotalAmt;
		this.ftotalCanAmt = ftotalCanAmt;
		this.ftotalFrozenAmt = ftotalFrozenAmt;
		this.ftotalRechargeAmt = ftotalRechargeAmt;
		this.ftotalWithdrawAmt = ftotalWithdrawAmt;
		this.ftotalDou = ftotalDou;
		this.ftotalCanDou = ftotalCanDou;
		this.ftotalFrozenDou = ftotalFrozenDou;
		this.ftotalCreateDou = ftotalCreateDou;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "fdate", length = 0)
	public Date getFdate() {
		return this.fdate;
	}

	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}

	@Column(name = "ftotalAmt", precision = 16, scale = 6)
	public Double getFtotalAmt() {
		return this.ftotalAmt;
	}

	public void setFtotalAmt(Double ftotalAmt) {
		this.ftotalAmt = ftotalAmt;
	}

	@Column(name = "ftotalCanAmt", precision = 16, scale = 6)
	public Double getFtotalCanAmt() {
		return this.ftotalCanAmt;
	}

	public void setFtotalCanAmt(Double ftotalCanAmt) {
		this.ftotalCanAmt = ftotalCanAmt;
	}

	@Column(name = "ftotalFrozenAmt", precision = 16, scale = 6)
	public Double getFtotalFrozenAmt() {
		return this.ftotalFrozenAmt;
	}

	public void setFtotalFrozenAmt(Double ftotalFrozenAmt) {
		this.ftotalFrozenAmt = ftotalFrozenAmt;
	}

	@Column(name = "ftotalRechargeAmt", precision = 16, scale = 6)
	public Double getFtotalRechargeAmt() {
		return this.ftotalRechargeAmt;
	}

	public void setFtotalRechargeAmt(Double ftotalRechargeAmt) {
		this.ftotalRechargeAmt = ftotalRechargeAmt;
	}

	@Column(name = "ftotalWithdrawAmt", precision = 16, scale = 6)
	public Double getFtotalWithdrawAmt() {
		return this.ftotalWithdrawAmt;
	}

	public void setFtotalWithdrawAmt(Double ftotalWithdrawAmt) {
		this.ftotalWithdrawAmt = ftotalWithdrawAmt;
	}

	@Column(name = "ftotalDou", precision = 16, scale = 6)
	public Double getFtotalDou() {
		return this.ftotalDou;
	}

	public void setFtotalDou(Double ftotalDou) {
		this.ftotalDou = ftotalDou;
	}

	@Column(name = "ftotalCanDou", precision = 16, scale = 6)
	public Double getFtotalCanDou() {
		return this.ftotalCanDou;
	}

	public void setFtotalCanDou(Double ftotalCanDou) {
		this.ftotalCanDou = ftotalCanDou;
	}

	@Column(name = "ftotalFrozenDou", precision = 16, scale = 6)
	public Double getFtotalFrozenDou() {
		return this.ftotalFrozenDou;
	}

	public void setFtotalFrozenDou(Double ftotalFrozenDou) {
		this.ftotalFrozenDou = ftotalFrozenDou;
	}

	@Column(name = "ftotalCreateDou", precision = 16, scale = 6)
	public Double getFtotalCreateDou() {
		return this.ftotalCreateDou;
	}

	public void setFtotalCreateDou(Double ftotalCreateDou) {
		this.ftotalCreateDou = ftotalCreateDou;
	}

}