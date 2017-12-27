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


/**
 * Fpromotionactivityreward entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="fpromotionactivityreward"
)

public class Fpromotionactivityreward  implements java.io.Serializable {


    // Fields    

     private Integer fid;
     private Fvirtualcointype fvirtualcointype;
     private Factivity factivity;
     private Boolean fvirtualCoinOrCny;//虚拟币奖励（true）还是人民币奖励
     private Boolean frateOrReal;//奖励比率（true）还是奖励实数
     private Double famount;//奖励数量（奖励比率的时候才有用）
     private Timestamp fcreateTime;


    // Constructors

    /** default constructor */
    public Fpromotionactivityreward() {
    }

    
    /** full constructor */
    public Fpromotionactivityreward(Fvirtualcointype fvirtualcointype, Factivity factivity, Boolean fvirtualCoinOrCny, Boolean frateOrReal, Double famount, Timestamp fcreateTime) {
        this.fvirtualcointype = fvirtualcointype;
        this.factivity = factivity;
        this.fvirtualCoinOrCny = fvirtualCoinOrCny;
        this.frateOrReal = frateOrReal;
        this.famount = famount;
        this.fcreateTime = fcreateTime;
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
        @JoinColumn(name="fVirtualCoinType")

    public Fvirtualcointype getFvirtualcointype() {
        return this.fvirtualcointype;
    }
    
    public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
        this.fvirtualcointype = fvirtualcointype;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="fActivity")

    public Factivity getFactivity() {
        return this.factivity;
    }
    
    public void setFactivity(Factivity factivity) {
        this.factivity = factivity;
    }
    
    @Column(name="fVirtualCoinOrCny")

    public Boolean getFvirtualCoinOrCny() {
        return this.fvirtualCoinOrCny;
    }
    
    public void setFvirtualCoinOrCny(Boolean fvirtualCoinOrCny) {
        this.fvirtualCoinOrCny = fvirtualCoinOrCny;
    }
    
    @Column(name="fRateOrReal")

    public Boolean getFrateOrReal() {
        return this.frateOrReal;
    }
    
    public void setFrateOrReal(Boolean frateOrReal) {
        this.frateOrReal = frateOrReal;
    }
    
    @Column(name="fAmount", precision=16, scale=6)

    public Double getFamount() {
        return this.famount;
    }
    
    public void setFamount(Double famount) {
        this.famount = famount;
    }
    
    @Column(name="fCreateTime", length=0)

    public Timestamp getFcreateTime() {
        return this.fcreateTime;
    }
    
    public void setFcreateTime(Timestamp fcreateTime) {
        this.fcreateTime = fcreateTime;
    }
   








}