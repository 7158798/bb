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
//import javax.ws.rs.core.Response.Status.Family;

import org.hibernate.annotations.GenericGenerator;


/**
 * Factivitycompletelog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="factivitycompletelo")

public class Factivitycompletelog  implements java.io.Serializable {


    // Fields    

     private Integer fid;
     private Factivity factivity;
     private double famount ;//数量，当奖励是比率是需要
     private Fuser fuser;
     private Integer ftype;//该字段弃用
     private Integer fstatus;//ActivityCompleteStatusEnum
     private Timestamp fcreateTime;
     private Timestamp flastUpdateTime;


    // Constructors

    /** default constructor */
    public Factivitycompletelog() {
    }

    
    /** full constructor */
    public Factivitycompletelog(Factivity factivity, Fuser fuser, Integer ftype, Integer fstatus, Timestamp fcreateTime, Timestamp flastUpdateTime) {
        this.factivity = factivity;
        this.fuser = fuser;
        this.ftype = ftype;
        this.fstatus = fstatus;
        this.fcreateTime = fcreateTime;
        this.flastUpdateTime = flastUpdateTime;
    }

   
    // Property accessors
    @GenericGenerator(name="generator", strategy="native")@Id @GeneratedValue(generator="generator")
    
    @Column(name="fid", unique=true, nullable=false)

    public Integer getFid() {
        return this.fid;
    }
    
    public void setFid(Integer fid) {
        this.fid = fid;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="factivity_id")

    public Factivity getFactivity() {
        return this.factivity;
    }
    
    public void setFactivity(Factivity factivity) {
        this.factivity = factivity;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="fuser_fid")

    public Fuser getFuser() {
        return this.fuser;
    }
    
    public void setFuser(Fuser fuser) {
        this.fuser = fuser;
    }
    
    @Column(name="ftype")

    public Integer getFtype() {
        return this.ftype;
    }
    
    public void setFtype(Integer ftype) {
        this.ftype = ftype;
    }
    
    @Column(name="fstatus")

    public Integer getFstatus() {
        return this.fstatus;
    }
    
    public void setFstatus(Integer fstatus) {
        this.fstatus = fstatus;
    }
    
    @Column(name="fcreateTime", length=0)

    public Timestamp getFcreateTime() {
        return this.fcreateTime;
    }
    
    public void setFcreateTime(Timestamp fcreateTime) {
        this.fcreateTime = fcreateTime;
    }
    
    @Column(name="fLastUpdateTime", length=0)

    public Timestamp getFlastUpdateTime() {
        return this.flastUpdateTime;
    }
    
    public void setFlastUpdateTime(Timestamp flastUpdateTime) {
        this.flastUpdateTime = flastUpdateTime;
    }

    @Column(name="famount")
	public double getFamount() {
		return famount;
	}


	public void setFamount(double famount) {
		this.famount = famount;
	}
   








}