package com.coboltforge.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coboltforge.domain.Setting;
import com.coboltforge.slidemenuexample.MainActivity;

public class SettingsTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "SETTINGSDB.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "SETTING";
    private MainActivity activity;
    
    public SettingsTDG(MainActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
 // CREATE TABLE IF NOT EXISTS 
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, OPUS_THEME TEXT, SHOW_PICTURE TEXT);");
        mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    
    // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void setSettings(Setting set){
    	deleteValues();
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            String show = "no";
            if (set.isShowPicture()){
            	show = "yes";
            }
            mydb.execSQL("INSERT INTO " + TABLE + "(OPUS_THEME, SHOW_PICTURE) VALUES('" + set.getOpus_theme() + "', '" + show + "')");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    /*public void updateUsername(User user){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET USERNAME = '" + user.getUsername() + "' WHERE TRANSIT_ID = '" + user.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void updateName(User user){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET NAME = '" + user.getName() + "' WHERE TRANSIT_ID = '" + user.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void updatePassword(User user){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET PASSWORD = '" + user.getPassword() + "' WHERE TRANSIT_ID = '" + user.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void updatePlan(User user){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET PLAN_ID = '" + user.getPlan().getId() + "' WHERE TRANSIT_ID = '" + user.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }*/
    
 // THIS FUNCTION DELETES VALUES FROM THE DATABASE ACCORDING TO THE CONDITION
    public void deleteValues(){
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("DELETE FROM " + TABLE);
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered while deleting.", Toast.LENGTH_LONG);
        }
    }
    
    public Setting getSetting(){
    	Setting set = null;
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM "+  TABLE, null);
            String theme, show;
            boolean showP = false;
            
            if(allrows.moveToFirst()){
                do{
                    theme = allrows.getString(1);
                    show = allrows.getString(2);
                }
                while(allrows.moveToNext());
                
                if (show != null && show.equalsIgnoreCase("yes")){
                	showP = true;
                }
                set = new Setting(theme, showP, activity);
            }
            else {
            	set = new Setting("Classic", true, activity);
            }
            mydb.close();
         }catch(Exception e){
            //Toast.makeText(getApplicationContext(), "Error encountered.", Toast.LENGTH_LONG);
        	 //user = User.getInstance(activity);
        }
    	return set;
    }
}
