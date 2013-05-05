package com.coboltforge.domain;

import java.util.ArrayList;

import org.shipp.activity.PrincipalActivity;

import com.coboltforge.datasource.PurchaseTDG;
import com.coboltforge.datasource.UsageTDG;

/**
 * Purchase class with attributes
 * @author Austin Takam
 *
 */
public class Purchase {
	private String type;
	private String date;
	private String price;
	
	private static PurchaseTDG tdg;
	
	private static ArrayList<Purchase> purchase = new ArrayList<Purchase>();
	
	public Purchase(String t, String d, String p, PrincipalActivity activity){
		this.type = t;
		this.date = d; 
		this.price = p; 
		purchase.add(this);
		
		tdg = new PurchaseTDG(activity);
		tdg.insertIntoTable(this.toString());
	}
	
	public String toString(){
		return type + "Pass Bought on: [" + date + "]  Price: $" + price;
	}
	
	public static ArrayList<String> getPurchases(PrincipalActivity activity){
		tdg = new PurchaseTDG(activity);
		ArrayList<String> list = tdg.getPurchaseDetails();
		ArrayList<String> list2 = new ArrayList<String>(list.size());
		
		for(int i=1; i<=list.size(); i++){
			list2.add(list.get(list.size()-i));
		}
		return list2;
	}

}
