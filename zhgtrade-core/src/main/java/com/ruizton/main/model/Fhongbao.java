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
import javax.persistence.Transient;
import javax.persistence.Version;
import org.hibernate.annotations.GenericGenerator;

/**
 * Fhongbao entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fhongbao")
public class Fhongbao implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Integer version;
	private Fuser fuser;
	private Fvirtualcointype fvirtualcointype;
	private Integer ftotalcount;
	private Integer fleftcount;
	private Double foneAmount;
	private Double ftotalAmount;
	private String fremark;
	private Timestamp fcreatetime;
	private Timestamp flastupdatetime;
	
	private String fuser_get ;
	private boolean finclude ;

	// Constructors

	/** default constructor */
	public Fhongbao() {
	}

	/** full constructor */
	public Fhongbao(Fuser fuser, Fvirtualcointype fvirtualcointype,
			Integer ftotalcount, Integer fleftcount, Double foneAmount,
			Double ftotalAmount, String fremark, Timestamp fcreatetime,
			Timestamp flastupdatetime) {
		this.fuser = fuser;
		this.fvirtualcointype = fvirtualcointype;
		this.ftotalcount = ftotalcount;
		this.fleftcount = fleftcount;
		this.foneAmount = foneAmount;
		this.ftotalAmount = ftotalAmount;
		this.fremark = fremark;
		this.fcreatetime = fcreatetime;
		this.flastupdatetime = flastupdatetime;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuser")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fvirtualcointype")
	public Fvirtualcointype getFvirtualcointype() {
		return this.fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@Column(name = "ftotalcount")
	public Integer getFtotalcount() {
		return this.ftotalcount;
	}

	public void setFtotalcount(Integer ftotalcount) {
		this.ftotalcount = ftotalcount;
	}

	@Column(name = "fleftcount")
	public Integer getFleftcount() {
		return this.fleftcount;
	}

	public void setFleftcount(Integer fleftcount) {
		this.fleftcount = fleftcount;
	}

	@Column(name = "foneAmount", precision = 16, scale = 6)
	public Double getFoneAmount() {
		return this.foneAmount;
	}

	public void setFoneAmount(Double foneAmount) {
		this.foneAmount = foneAmount;
	}

	@Column(name = "ftotalAmount", precision = 16, scale = 6)
	public Double getFtotalAmount() {
		return this.ftotalAmount;
	}

	public void setFtotalAmount(Double ftotalAmount) {
		this.ftotalAmount = ftotalAmount;
	}

	@Column(name = "fremark", length = 128)
	public String getFremark() {
		return this.fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Column(name = "flastupdatetime", length = 0)
	public Timestamp getFlastupdatetime() {
		return this.flastupdatetime;
	}

	public void setFlastupdatetime(Timestamp flastupdatetime) {
		this.flastupdatetime = flastupdatetime;
	}

	@Column(name="fuser_get")
	public String getFuser_get() {
		if(fuser_get==null){
			return "" ;
		}else{
			return fuser_get ;
		}
	}

	public void setFuser_get(String fuser_get) {
		this.fuser_get = fuser_get;
	}

	@Transient
	public boolean isFinclude() {
		return finclude ;
	}

	public void setFinclude(boolean finclude) {
		this.finclude = finclude;
	}
	
	public boolean include(String userId){

		boolean flag = false ;
		String userGet = this.getFuser_get() ;
		if(userGet!=null){
			String[] s = userGet.split("_") ;
			for (String string : s) {
				System.out.println(userId+"--"+string);
				if(userId.equals(string)==true){
					flag = true ;
				}
			}
		}
		setFinclude(flag) ;
		return flag ;
	
	}
	
	

}