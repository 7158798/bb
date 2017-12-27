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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.util.HTMLSpirit;
import com.ruizton.util.Utils;

/**
 * Farticle entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "farticle")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Farticle implements java.io.Serializable {

	// Fields

	private int fid;
	private Farticletype farticletype;
	private Fadmin fadminByFcreateAdmin;
	private Fadmin fadminByFmodifyAdmin;
	private String ftitle;
	private String ftitle_short ;
	private String fkeyword;
	private boolean isTop;				// 置顶
	private String fdescription;
	private String fcontent;
	private String fcontent_short;
	private String fabstract ;
	private Timestamp fcreateDate;
	private Timestamp flastModifyDate;
	private int version ;
	private String fdocurl;

	private Fvirtualcointype fvirtualcointype;
	// Constructors


	/** default constructor */
	public Farticle() {
	}

	/** full constructor */
	public Farticle(Farticletype farticletype, Fadmin fadminByFcreateAdmin,
			Fadmin fadminByFmodifyAdmin, String ftitle, String fkeyword,
			String fdescription, String fcontent, Timestamp fcreateDate,
			Timestamp flastModifyDate) {
		this.farticletype = farticletype;
		this.fadminByFcreateAdmin = fadminByFcreateAdmin;
		this.fadminByFmodifyAdmin = fadminByFmodifyAdmin;
		this.ftitle = ftitle;
		this.fkeyword = fkeyword;
		this.fdescription = fdescription;
		this.fcontent = fcontent;
		this.fcreateDate = fcreateDate;
		this.flastModifyDate = flastModifyDate;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fId", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fArticleType")
	public Farticletype getFarticletype() {
		return this.farticletype;
	}

	public void setFarticletype(Farticletype farticletype) {
		this.farticletype = farticletype;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fCreateAdmin")
	public Fadmin getFadminByFcreateAdmin() {
		return this.fadminByFcreateAdmin;
	}

	public void setFadminByFcreateAdmin(Fadmin fadminByFcreateAdmin) {
		this.fadminByFcreateAdmin = fadminByFcreateAdmin;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fModifyAdmin")
	public Fadmin getFadminByFmodifyAdmin() {
		return this.fadminByFmodifyAdmin;
	}

	public void setFadminByFmodifyAdmin(Fadmin fadminByFmodifyAdmin) {
		this.fadminByFmodifyAdmin = fadminByFmodifyAdmin;
	}

	@Column(name = "fTitle", length = 1024)
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fKeyword", length = 1024)
	public String getFkeyword() {
		return this.fkeyword;
	}

	public void setFkeyword(String fkeyword) {
		this.fkeyword = fkeyword;
	}

	@Column(name = "fDescription", length = 1024)
	public String getFdescription() {
		return this.fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	@Column(name = "fContent", length = 65535)
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "fCreateDate", length = 0)
	public Timestamp getFcreateDate() {
		return this.fcreateDate;
	}

	public void setFcreateDate(Timestamp fcreateDate) {
		this.fcreateDate = fcreateDate;
	}

	@Column(name = "fLastModifyDate", length = 0)
	public Timestamp getFlastModifyDate() {
		return this.flastModifyDate;
	}

	public void setFlastModifyDate(Timestamp flastModifyDate) {
		this.flastModifyDate = flastModifyDate;
	}
	@Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "isTop")
	public boolean isTop() {
		return isTop;
	}

	public void setTop(boolean top) {
		isTop = top;
	}

	@Transient
	public String getFtitle_short() {
		String t = this.getFtitle() ;
		if(t==null){
			return t ;
		}
		if(t.length()>22){
			t = t.substring(0,22) ;
			t+="..." ;
		}
		
		return t;
	}

	public void setFtitle_short(String ftitle_short) {
		this.ftitle_short = ftitle_short;
	}

	@Transient
	public String getFabstract() {
		String content = HTMLSpirit.delHTMLTag(this.getFcontent()) ;
		String ret = null ;
		if(content!=null){
			if(content.length()>75){
				ret = content.substring(0,75) ;
			}else{
				ret = content ;
			}
		}
		return ret ;
	}

	public void setFabstract(String fabstract) {
		this.fabstract = fabstract;
	}

	@Transient
	public String getFcontent_short() {
		String content = this.getFcontent() ;
		String retf = "" ;
		if(content!=null){
			content = HTMLSpirit.delHTMLTag(content) ;
			if(content.length()<300){
				retf = content+"..." ;
			}else{
				retf = content.substring(0,300)+"..." ;
			}
		}
		
		return retf ;
	}

	public void setFcontent_short(String fcontent_short) {
		this.fcontent_short = fcontent_short;
	}
	
	@Column(name="fdocurl")
	public String getFdocurl() {
		return fdocurl;
	}

	public void setFdocurl(String fdocurl) {
		this.fdocurl = fdocurl;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "referCoinId")
	public Fvirtualcointype getFvirtualcointype() {
		return fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}
}