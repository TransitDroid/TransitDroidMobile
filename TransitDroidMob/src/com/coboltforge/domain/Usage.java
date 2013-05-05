package com.coboltforge.domain;

import java.util.ArrayList;

import org.shipp.activity.PrincipalActivity;

import com.coboltforge.datasource.UsageTDG;

/**
 * Usage class
 * @author Austin Takam
 *
 */
public class Usage {
	private String machine;
	private String date;
	private static UsageTDG tdg;
	
	private static ArrayList<Usage> usage = new ArrayList<Usage>();
	
	public Usage(String m, String d, PrincipalActivity activity){
		this.machine = m;
		this.date = d; 
		usage.add(this);
		
		tdg = new UsageTDG(activity);
		tdg.insertIntoTable(this.toString());
	}
	
	public String toString(){
		return "[" + date + "] at " + machine;
	}
	
	public static ArrayList<String> getUsage(PrincipalActivity activity){
		tdg = new UsageTDG(activity);
		ArrayList<String> list = tdg.getUsageDetails();
		ArrayList<String> list2 = new ArrayList<String>(list.size());
		
		for(int i=1; i<=list.size(); i++){
			list2.add(list.get(list.size()-i));
		}
		return list2;
	}
	
	public static void clearUsage(PrincipalActivity activity){
		tdg = new UsageTDG(activity);
		tdg.deleteValues();
	}

}
