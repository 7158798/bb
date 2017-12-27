package com.ruizton.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;

import com.ruizton.util.HTMLSpirit;

/**
 * Flendsystemargs entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "flendsystemargs", uniqueConstraints = @UniqueConstraint(columnNames = "fkey"))
public class Flendsystemargs implements java.io.Serializable {

	// Fields

	private Integer fid;
	private String fkey;
	private String fvalue;
	private String fvalue_s;
	private String fdesc;

	// Constructors

	/** default constructor */
	public Flendsystemargs() {
	}

	/** full constructor */
	public Flendsystemargs(String fkey, String fvalue, String fdesc) {
		this.fkey = fkey;
		this.fvalue = fvalue;
		this.fdesc = fdesc;
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

	@Column(name = "fkey", unique = true, length = 128)
	public String getFkey() {
		return this.fkey;
	}

	public void setFkey(String fkey) {
		this.fkey = fkey;
	}

	@Column(name = "fvalue", length = 65535)
	public String getFvalue() {
		return this.fvalue;
	}

	public void setFvalue(String fvalue) {
		this.fvalue = fvalue;
	}

	@Column(name = "fdesc", length = 65535)
	public String getFdesc() {
		return this.fdesc;
	}

	public void setFdesc(String fdesc) {
		this.fdesc = fdesc;
	}

	@Transient
	public String getFvalue_s() {
		return HTMLSpirit.delHTMLTag(getFvalue());
	}

	public void setFvalue_s(String fvalue_s) {
		this.fvalue_s = fvalue_s;
	}

	@Transient
	public boolean getBooleanValue() {
		boolean flag = false;
		if (this.getFvalue() != null
				&& "true".equalsIgnoreCase(this.getFvalue().trim())) {
			flag = true;
		}
		return flag;
	}

	@Transient
	public Integer getIntegerValue() {
		int flag = 0;
		if (this.getFvalue() != null) {
			try {
				flag = Integer.parseInt(this.getFvalue().trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	@Transient
	public double getDoubleValue() {
		double flag = 0d;
		if (this.getFvalue() != null) {
			try {
				flag = Double.parseDouble(this.getFvalue().trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

}