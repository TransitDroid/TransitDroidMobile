package com.coboltforge.domain;

import java.lang.reflect.Method;

import org.shipp.activity.PrincipalActivity;

import com.coboltforge.datasource.UserTDG;

/**
 * User class
 * @author Austin Takam
 *
 */
public class User {
	private String name;
	private String id;
	private String username;
	private String password;
	private byte[] key1;
	private byte[] key2;
	private byte[] key3;
	private byte[] key4;
	private byte[] passCode;
	
	private static UserTDG tdg;
	
	public static User INSTANCE;
	
	private User(String name, String id, String username, String password, PrincipalActivity activity, byte[] k1, byte[] k2, byte[] k3, byte[] k4, byte[] passCode){
		this.name = name;
		this.id = id;
		this.username = username;
		this.password = password;
		
		this.key1 = k1;
		this.key2 = k2;
		this.key3 = k3;
		this.key4 = k4;
		this.passCode = passCode;
		
		tdg = new UserTDG(activity);
		tdg.insertIntoTable(this);
		
	}
	
	public User(String name, String id, String username, String password, byte[] k1, byte[] k2, byte[] k3, byte[] k4, byte[] passCode){
		this.name = name;
		this.id = id;
		this.username = username;
		this.password = password;
		
		this.key1 = k1;
		this.key2 = k2;
		this.key3 = k3;
		this.key4 = k4;
		this.passCode = passCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		tdg.insertIntoTable(this);
	}
	public String getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String user) {
		this.username = user;
		tdg.insertIntoTable(this);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String pass) {
		this.password = pass;
		tdg.insertIntoTable(this);
	}
	
	public static User getInstance(PrincipalActivity activity){
		if (INSTANCE == null){
			
			//get from database
			INSTANCE = new UserTDG(activity).getUser();
			if (INSTANCE != null)
				return INSTANCE;
			
			//if nothing in database
			String serial = "";
	        try{
	        	Class<?> c = Class.forName("android.os.SystemProperties");
	        	Method get = c.getMethod("get", String.class);
	        	serial = (String)get.invoke(c, "ro.serialno");
	        }
	        catch(Exception e){
	        	e.printStackTrace();
	        }
			INSTANCE =  new User("", serial, "", "", activity, "KEY1".getBytes(), "KEY2".getBytes(), "KEY3".getBytes(), "KEY4".getBytes(), "PASSCODE".getBytes());
		}
		return INSTANCE;
	}

	public byte[] getKey1() {
		return key1;
	}

	public void setKey1(byte[] key1) {
		this.key1 = key1;
		tdg.insertIntoTable(this);
	}

	public byte[] getKey2() {
		return key2;
	}

	public void setKey2(byte[] key2) {
		this.key2 = key2;
		tdg.insertIntoTable(this);
	}

	public byte[] getKey3() {
		return key3;
	}

	public void setKey3(byte[] key3) {
		this.key3 = key3;
		tdg.insertIntoTable(this);
	}

	public byte[] getKey4() {
		return key4;
	}

	public void setKey4(byte[] key4) {
		this.key4 = key4;
		tdg.insertIntoTable(this);
	}

	public byte[] getPassCode() {
		return passCode;
	}

	public void setPassCode(byte[] passCode) {
		this.passCode = passCode;
		tdg.insertIntoTable(this);
	}
}
