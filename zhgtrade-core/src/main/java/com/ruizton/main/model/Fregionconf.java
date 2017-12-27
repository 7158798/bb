package com.ruizton.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "fregionconf")
public class Fregionconf {
	
	private int id;
	private int pid;
	private String name;
	private int regionLevel;
	private String py;
	private int isHot;
	
	public Fregionconf(){}
	
	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "pid")
	public int getPid(){
		return this.pid;
	}
	
	public void setPid(int pid){
		this.pid = pid;
	}
	
	@Column(name = "name", length = 128)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "region_level")
	public int getRegionLevel(){
		return this.regionLevel;
	}
	
	public void setRegionLevel(int regionLevel){
		this.regionLevel = regionLevel;
	}
	
	@Column(name = "py", length = 128)
	public String getPy() {
		return this.py;
	}

	public void setPy(String py) {
		this.py = py;
	}
	
	@Column(name = "is_hot")
	public int getIsHot() {
		return this.isHot;
	}

	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}
	
}
