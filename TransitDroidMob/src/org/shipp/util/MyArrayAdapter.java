package org.shipp.util;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter class to display the List View of the app
 * @author Austin Takam
 *
 */
public class MyArrayAdapter extends ArrayAdapter {

	public MyArrayAdapter(Context context, int textViewResourceId, ArrayList<String> s) {
		super(context, textViewResourceId, s);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {  
		View view = super.getView(position, convertView, parent);  
		
		// List view with alternating colors
		if (position % 2 == 1) {
		    view.setBackgroundColor(Color.parseColor("#C8C8C8"));  
		} else {
		    view.setBackgroundColor(Color.parseColor("#E8E8E8"));  
		}
		final TextView tv = (TextView) view;
		tv.setTextSize(14);
		tv.setTypeface(Typeface.create("Segoe UI Light", Typeface.NORMAL));
		return view;  
	}
	
}
