package com.coboltforge.datasource;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coboltforge.domain.Plan;
import com.coboltforge.slidemenuexample.MainActivity;

public class PlanTDG {
	private static SQLiteDatabase mydb;
    private static String DBNAME = "PLANSDB.db";    // THIS IS THE SQLITE DATABASE FILE NAME.
    private static String TABLE = "PLAN";
    private MainActivity activity;
    
    public PlanTDG(MainActivity activity){
    	this.activity = activity;
    	createTable();
    }
    
 // CREATE TABLE IF NOT EXISTS 
    public void createTable(){
        try{
        mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF  NOT EXISTS "+ TABLE +" (ID INTEGER PRIMARY KEY, PLAN_ID TEXT, TYPE TEXT, EXP TEXT, PASS_CODE TEXT);");
        mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in creating table", Toast.LENGTH_LONG);
        }
    }
    
    // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void insertIntoTable(Plan plan){
    	deleteValues();
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("INSERT INTO " + TABLE + "(PLAN_ID, TYPE, EXP, PASS_CODE) VALUES('" + plan.getId() + "', " + "'" + plan.getType() + "', " + "'" + plan.getExp() + "', " + "'" + plan.getPassCode() + "')");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error in inserting into table", Toast.LENGTH_LONG);
        }
    }
    
 /*// THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void updatePlanExp(Plan plan){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET EXP = '" + plan.getExp() + "' WHERE PLAN_ID = '" + plan.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }
    
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void updatePlanType(Plan plan){
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("UPDATE " + TABLE + " SET TYPE = '" + plan.getType() + "' WHERE PLAN_ID = '" + plan.getId() + "'");
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered", Toast.LENGTH_LONG);
        }
    }
    */
 // THIS FUNCTION DELETES VALUES FROM THE DATABASE ACCORDING TO THE CONDITION
    public void deleteValues(){
        try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            mydb.execSQL("DELETE FROM " + TABLE );
            mydb.close();
        }catch(Exception e){
            //Toast.makeText(activity.getApplicationContext(), "Error encountered while deleting.", Toast.LENGTH_LONG);
        }
    }
    
    public Plan getPlan(String pid){
    	Plan plan = null;
    	try{
            mydb = activity.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,null);
            Cursor allrows = mydb.rawQuery("SELECT * FROM "+  TABLE, null);
            String id, exp, type, code;
            
            if(allrows.moveToFirst()){
                do{
                    id = allrows.getString(1);
                    type = allrows.getString(2);
                    exp = allrows.getString(3);
                    code = allrows.getString(4);
                }
                while(allrows.moveToNext());
                plan = new Plan(id, type, exp, code, activity);
            }
            mydb.close();
         }catch(Exception e){
        	 e.printStackTrace();
            //Toast.makeText(getApplicationContext(), "Error encountered.", Toast.LENGTH_LONG);
        	 plan = new Plan(pid, "", "", "PASSCODE", activity);
         }
    	return plan;
    }
}
