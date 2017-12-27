package com.ruizton.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fproxy entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fproxy")
public class Fproxy implements java.io.Serializable {

	// Fields

	private Integer fid;
	private String name;
	private String amount;
	private String qq;
	private String realname;
	private String account;

	// Constructors

	/** default constructor */
	public Fproxy() {
	}

	/** full constructor */
	public Fproxy(String name, String amount, String qq, String realname,
			String account) {
		this.name = name;
		this.amount = amount;
		this.qq = qq;
		this.realname = realname;
		this.account = account;
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

	@Column(name = "name", length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "amount", length = 256)
	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Column(name = "qq", length = 256)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "realname", length = 256)
	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@Column(name = "account", length = 256)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}