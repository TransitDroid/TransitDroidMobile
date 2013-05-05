package com.coboltforge.domain;

import org.shipp.activity.PrincipalActivity;

import com.coboltforge.datasource.SettingsTDG;

/**
 * Setting class with attributes
 * @author Austin Takam
 *
 */
public class Setting {
	
	private String opus_theme;
	private boolean connected;
	private SettingsTDG tdg;
	private String username;
	private String password;
	private String displayName;
	private String disclaimer = "No";
	
	public Setting(String theme, boolean show, String username, String password, String display, PrincipalActivity activity, String claim){
		this.opus_theme = theme;
		this.connected = show;
		this.username = username;
		this.password = password;
		this.displayName = display;
		this.disclaimer = claim;
		
		tdg = new SettingsTDG(activity);
		tdg.setSettings(this);
	}

	public String getOpus_theme() {
		return opus_theme;
	}

	public void setOpus_theme(String opus_theme) {
		this.opus_theme = opus_theme;
		tdg.setSettings(this);
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connection) {
		this.connected = connection;
		tdg.setSettings(this);
	}
	
	public void setCredentials(String un, String pass) {
		this.username = un;
		this.password = pass;
		tdg.setSettings(this);
	}
	
	public void setDisplayName(String display) {
		this.displayName = display;
		tdg.setSettings(this);
	}
	
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
		tdg.setSettings(this);
	}

	public void setUsername(String username) {
		this.username = username;
		tdg.setSettings(this);
	}

	public void setPassword(String password) {
		this.password = password;
		tdg.setSettings(this);
	}

	public static Setting getSetting(PrincipalActivity activity){
		return new SettingsTDG(activity).getSetting();
	}
	
}
