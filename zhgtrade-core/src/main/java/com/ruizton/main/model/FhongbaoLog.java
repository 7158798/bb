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

import com.ruizton.util.Utils;

/**
 * FhongbaoLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fhongbao_log")
public class FhongbaoLog implements java.io.Serializable {

	// Fields

	private Integer fid;
	private Fhongbao fhongbao ;
	private Integer version;
	private Fuser fuser;
	private Fvirtualcointype fvirtualcointype;
	private Double famount;
	private Timestamp fcreatetime;
	
	private String ftime ;

	// Constructors

	/** default constructor */
	public FhongbaoLog() {
	}

	/** full constructor */
	public FhongbaoLog(Fuser fuser, Fvirtualcointype fvirtualcointype,
			Double famount, Timestamp fcreatetime) {
		this.fuser = fuser;
		this.fvirtualcointype = fvirtualcointype;
		this.famount = famount;
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

	@Column(name = "famount", precision = 16, scale = 6)
	public Double getFamount() {
		return this.famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
	}

	@Column(name = "fcreatetime", length = 0)
	public Timestamp getFcreatetime() {
		return this.fcreatetime;
	}

	public void setFcreatetime(Timestamp fcreatetime) {
		this.fcreatetime = fcreatetime;
	}

	@Transient
	public String getFtime() {
		Timestamp time =  this.getFcreatetime();
		Timestamp now = Utils.getTimestamp() ;
		long interval = (now.getTime() - time.getTime())/(1000*60*60) ;//小时
		if(interval==0){
			return "刚刚" ;
		}else if(interval<24){
			return interval+"小时前" ;
		}else{
			return (interval/24)+"天前" ;
		}
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="fhongbao")
	public Fhongbao getFhongbao() {
		return fhongbao;
	}

	public void setFhongbao(Fhongbao fhongbao) {
		this.fhongbao = fhongbao;
	}

	
	
	
}