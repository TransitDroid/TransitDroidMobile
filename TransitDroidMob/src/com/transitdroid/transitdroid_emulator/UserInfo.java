package com.transitdroid.transitdroid_emulator;

import static com.transitdroid.transitdroid_emulator.ISO7816.SW_SUCCESS;

import java.util.ArrayList;

import org.shipp.activity.PrincipalActivity;

import com.coboltforge.domain.Setting;
import com.coboltforge.domain.User;

/**
 * Class with specific phone/user details to be transferred to the reader
 * @author Austin Takam
 *
 */
public class UserInfo {
	
	private static ArrayList passCArr = new ArrayList();
	private static ArrayList key1Arr = new ArrayList();
	private static ArrayList key2Arr = new ArrayList();
	private static ArrayList key3Arr = new ArrayList();
	private static ArrayList key4Arr = new ArrayList();
	    
	    static User user = User.getInstance(PrincipalActivity.activity);
	    PrincipalActivity activity;
	    static Setting setting = Setting.getSetting(PrincipalActivity.activity);
	    
	    /**
	     * Constructor
	     */
		public UserInfo(){
		}
		
		/**
		 * Obtain the response to send to the reader according to the commeand provided
		 * @param i
		 * @param bytesToRead
		 * @return
		 */
		public byte[] readPhoneId()
		{
			byte[] response = new byte[user.getId().getBytes().length+2];
			
			System.arraycopy(user.getId().getBytes(), 0,response,0,user.getId().getBytes().length);
			
			System.arraycopy(toBytes(SW_SUCCESS),0,response,user.getId().getBytes().length,2);
			
			return response;
		}
		
		public byte[] readKey1()
		{
			byte[] response = new byte[user.getKey1().length+2];
			
			System.arraycopy(user.getKey1(), 0,response,0,user.getKey1().length);
			
			System.arraycopy(toBytes(SW_SUCCESS),0,response,user.getKey1().length,2);
			
			return response;
		}
		
		public byte[] readKey2()
		{
			byte[] response = new byte[user.getKey2().length+2];
			
			System.arraycopy(user.getKey2(), 0,response,0,user.getKey2().length);
			
			System.arraycopy(toBytes(SW_SUCCESS),0,response,user.getKey2().length,2);
			
			return response;
		}
		
		public byte[] readKey3()
		{
			byte[] response = new byte[user.getKey3().length+2];
			
			System.arraycopy(user.getKey3(), 0,response,0,user.getKey3().length);
			
			System.arraycopy(toBytes(SW_SUCCESS),0,response,user.getKey3().length,2);
			
			return response;
		}
		
		public byte[] readKey4()
		{
			byte[] response = new byte[user.getKey4().length+2];
			
			System.arraycopy(user.getKey4(), 0,response,0,user.getKey4().length);
			
			System.arraycopy(toBytes(SW_SUCCESS),0,response,user.getKey4().length,2);
			
			return response;
		}
		
		public byte[] readPassCode()
		{
			byte[] response = new byte[user.getPassCode().length+2];
			
			System.arraycopy(user.getPassCode(), 0,response,0,user.getPassCode().length);
			
			System.arraycopy(toBytes(SW_SUCCESS),0,response,user.getPassCode().length,2);
			
			return response;
		}
		
		/**
		 * Obtain the new pass code provided from the reader and responding OK to the reader
		 * @param data
		 * @return
		 */
		public byte[] updatePassCodeBinary(byte data)
		{
			passCArr.add(data);
			//System.out.println(new String(data).trim());
			
			return "OK".getBytes();
		}
		
		public byte[] updateKey1Binary(byte data)
		{
			key1Arr.add(data);
			//System.out.println(new String(data).trim());
			
			return "OK".getBytes();
		}
		
		public byte[] updateKey2Binary(byte data)
		{
			key2Arr.add(data);
			//System.out.println(new String(data).trim());
			
			return "OK".getBytes();
		}
		
		public byte[] updateKey3Binary(byte data)
		{
			key3Arr.add(data);
			//System.out.println(new String(data).trim());
			
			return "OK".getBytes();
		}
		
		public byte[] updateKey4Binary(byte data)
		{
			key4Arr.add(data);
			//System.out.println(new String(data).trim());
			
			return "OK".getBytes();
		}

	   private static byte[] toBytes(short s) {
	        return new byte[] { (byte) ((s & 0xff00) >> 8), (byte) (s & 0xff) };
	    }
	   
	   public void flushData(){
		   
		   byte[] pass = new byte[passCArr.size()];
			int i=0;
			for (Object by : passCArr){
				pass[i] = (Byte) by;
				i++;
			}
			
			byte[] key1 = new byte[key1Arr.size()];
			int j=0;
			for (Object by : key1Arr){
				key1[j] = (Byte) by;
				j++;
			}
			
			byte[] key2 = new byte[key2Arr.size()];
			int k=0;
			for (Object by : key2Arr){
				key2[k] = (Byte) by;
				k++;
			}
			byte[] key3 = new byte[key3Arr.size()];
			int l=0;
			for (Object by : key3Arr){
				key3[l] = (Byte) by;
				l++;
			}
			byte[] key4 = new byte[key4Arr.size()];
			int m=0;
			for (Object by : key4Arr){
				key4[m] = (Byte) by;
				m++;
			}
			user.setPassCode(pass);
			user.setKey1(key1);
			user.setKey2(key2);
			user.setKey3(key3);
			user.setKey4(key4);
			
			passCArr.clear();
			key1Arr.clear();
			key2Arr.clear();
			key3Arr.clear();
			key4Arr.clear();
			passCArr.clear();
	   }

	   /**
	    * End NFC connection 
	    * @return
	    */
		public byte[] endConnection() {
			
			
			//Toast.makeText(MainActivity.activity, new String(b).trim(), Toast.LENGTH_SHORT).show();
			return "OK".getBytes();
		}
		
		public byte[] mergeByteArrays(byte[] a, byte[] b){
			byte[] v = ":".getBytes();
			
			byte[] c = new byte[a.length + b.length + v.length];
			for (int i=0; i<a.length; i++){
				c[i] = a[i];
			}
			int x = a.length;
			for (int i=0; i<v.length; i++){
				c[x] = v[i];
				x++;
			}
			int j = a.length + v.length;
			for (int i=0; i<b.length; i++){
				c[j] = b[i];
				j++;
			}
			return c;
		}
}
