package com.etnetera.hr.data;

import java.util.Date;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Simple data entity describing basic properties of every JavaScript framework.
 * 
 * @author Etnetera
 *
 */
@Entity
public class JavaScriptFramework {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;
	
	private TreeSet<String> version = new TreeSet<>();
	private Date deprecationDate;
	private String hypeLevel;

	public JavaScriptFramework() {
	}
	
	public JavaScriptFramework(String name) {
		this.name = name;
	}
	
	public JavaScriptFramework(String name, String version, Date deprecationDate, String hypeLevel) {
		this.name = name;
		this.version.add(version);
		this.deprecationDate = deprecationDate;
		this.hypeLevel = hypeLevel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TreeSet<String> getVersion() {
		return version;
	}

	public void setVersion(TreeSet<String> version) {
		this.version = version;
	}

	public String getLastVersion() {
		if (version.isEmpty())
			return "NoVersion";
		return version.last();
	}

	public void addVersion(String version) {
		this.version.add(version);
	}
	public Date getDeprecationDate() {
		return deprecationDate;
	}

	public void setDeprecationDate(Date deprecationDate) {
		this.deprecationDate = deprecationDate;
	}

	public String getHypeLevel() {
		return hypeLevel;
	}

	public void setHypeLevel(String hypeLevel) {
		this.hypeLevel = hypeLevel;
	}
	
	@Override
	public String toString() {
		return "JavaScriptFramework [id=" + id + ", name=" + name + "]";
	}

}
