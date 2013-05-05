package com.coboltforge.domain;

import com.coboltforge.datasource.SettingsTDG;
import com.coboltforge.slidemenuexample.MainActivity;

public class Setting {
	
	private String opus_theme;
	private boolean showPicture;
	private SettingsTDG tdg;
	
	public Setting(String theme, boolean show, MainActivity activity){
		this.opus_theme = theme;
		this.showPicture = show;
		
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

	public boolean isShowPicture() {
		return showPicture;
	}

	public void setShowPicture(boolean showPicture) {
		this.showPicture = showPicture;
		tdg.setSettings(this);
	}
	
}
