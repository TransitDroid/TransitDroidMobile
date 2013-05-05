package com.coboltforge.domain;

import java.lang.reflect.Method;

import com.coboltforge.datasource.UserTDG;
import com.coboltforge.slidemenuexample.MainActivity;

public class User {
	private String name;
	private String id;
	private Plan plan;
	private String username;
	private String password;
	
	private static UserTDG tdg;
	
	public static User INSTANCE;
	
	private User(String name, String id, Plan plan, String username, String password, MainActivity activity){
		this.name = name;
		this.id = id;
		this.username = username;
		this.password = password;
		this.plan = new Plan(plan);
		
		tdg = new UserTDG(activity);
		tdg.initialInsertIntoTable(this);
		
	}
	
	public User(String name, String id, Plan plan, String username, String password){
		this.name = name;
		this.id = id;
		this.username = username;
		this.password = password;
		this.plan = new Plan(plan);
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		tdg.initialInsertIntoTable(this);
	}
	public String getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String user) {
		this.username = user;
		tdg.initialInsertIntoTable(this);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String pass) {
		this.password = pass;
		tdg.initialInsertIntoTable(this);
	}
	
	public void setPlan(Plan plan, MainActivity activity){
		this.plan = new Plan(plan.getId(), plan.getType(), plan.getExp(), plan.getPassCode(), activity);
		tdg.initialInsertIntoTable(this);
	}
	
	public Plan getPlan(){
		return plan;
	}
	public static User getInstance(MainActivity activity){
		if (INSTANCE == null){
			new UserTDG(activity).setUser(INSTANCE);
			if (INSTANCE != null)
				return INSTANCE;
			
			String serial = "";
	        try{
	        	Class<?> c = Class.forName("android.os.SystemProperties");
	        	Method get = c.getMethod("get", String.class);
	        	serial = (String)get.invoke(c, "ro.serialno");
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
	        Plan plan = new Plan("plan_"+serial, "", "", "CODE_HERE", activity);
			INSTANCE =  new User("", serial, plan, "", "", activity);
		}
		return INSTANCE;
	}
	public static User createNewInstance(String name, String id, Plan plan, String username, String password){
		//INSTANCE = null;
		
		INSTANCE =  new User(name, id, plan, username, password);
		
		return INSTANCE;
	}
	
}
