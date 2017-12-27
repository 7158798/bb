package com.ruizton.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fandroidpushment entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fandroidpushment")
public class Fandroidpushment implements java.io.Serializable {

	// Fields

	private Integer fid;
	private String registrationId;
	private String pushment;

	// Constructors

	/** default constructor */
	public Fandroidpushment() {
	}

	/** full constructor */
	public Fandroidpushment(String registrationId, String pushment) {
		this.registrationId = registrationId;
		this.pushment = pushment;
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

	@Column(name = "RegistrationID", length = 128)
	public String getRegistrationId() {
		return this.registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	@Column(name = "pushment", length = 65535)
	public String getPushment() {
		return this.pushment;
	}

	public void setPushment(String pushment) {
		this.pushment = pushment;
	}

}