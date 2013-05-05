package com.coboltforge.domain;

import org.shipp.activity.PrincipalActivity;

import com.coboltforge.datasource.PlanTDG;

/**
 * Plan class with attributes
 * @author Austin Takam
 *
 */
public class Plan {
	
	private String id;
	private String type;
	private String exp;
	private String passCode;
	private static PlanTDG tdg;
	
	public Plan(Plan p){
		this.id = p.id;
		this.type = p.type;
		this.exp = p.exp;
		this.passCode = p.passCode;
	}
	
	public Plan(String id, String type, String exp, String passCode, PrincipalActivity activity){
		this.id = id;
		this.type = type;
		this.exp = exp;
		this.passCode = passCode;
		
		tdg = new PlanTDG(activity);
		tdg.insertIntoTable(this);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		tdg.insertIntoTable(this);
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
		tdg.insertIntoTable(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		tdg.insertIntoTable(this);
	}

	public String getPassCode() {
		return passCode;
	}
	
	public void setPassCode(String p) {
		this.passCode = p;
		tdg.insertIntoTable(this);
	}
}
