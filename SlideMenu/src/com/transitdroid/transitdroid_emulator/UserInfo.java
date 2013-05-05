package com.transitdroid.transitdroid_emulator;

import static com.transitdroid.transitdroid_emulator.ISO7816.SW_COMMAND_NOT_ALLOWED;
import static com.transitdroid.transitdroid_emulator.ISO7816.SW_SUCCESS;

import java.util.ArrayList;

import android.widget.Toast;

import com.coboltforge.datasource.UserTDG;
import com.coboltforge.domain.User;
import com.coboltforge.slidemenuexample.MainActivity;

public class UserInfo {
	
	private static ArrayList passC = new ArrayList();
	    
	    private byte[] card;
	    
		public UserInfo(){
			card = new byte[64];
			//User user = new UserTDG(MainActivity.activity).getUser();
			User user = User.getInstance(MainActivity.activity);
			String username = user.getUsername();
			String password = ":"+user.getPassword();
			String id = ":"+user.getId();
			String code = ":"+user.getPlan().getPassCode();
			
			if (username != null && password != null){
				byte[] array1 = mergeByteArrays(username.getBytes(), password.getBytes());
				byte[] array2 = mergeByteArrays(id.getBytes(), code.getBytes());
				byte[] array = mergeByteArrays(array1, array2);
				System.arraycopy(array, 0, card, 0, array.length);
			}
			//System.arraycopy(INIT_DATA, 0, card, 0, 64);
		}
		
		public byte[] readInfo(int i, int bytesToRead)
		{
			byte[] response = new byte[bytesToRead+2];
			
			System.arraycopy(card, i*4,response,0,bytesToRead);
			
			System.arraycopy(toBytes(SW_SUCCESS),0,response,bytesToRead,2);
			
			return response;
		}
		
		public byte[] mergeByteArrays(byte[] a, byte[] b){
			byte[] c = new byte[a.length + b.length];
			for (int i=0; i<a.length; i++){
				c[i] = a[i];
			}
			int j = a.length;
			for (int i=0; i<b.length; i++){
				c[j] = b[i];
				j++;
			}
			return c;
		}
		
		public byte[] updateBinary(byte data)
		{
			passC.add(data);
			//System.out.println(new String(data).trim());
			
			return "OK".getBytes();
			/*byte[] response;
			
			//pages 0,1 are reserved for UID
			if(pageNumber<2)
			{
				response = new byte[2];
				System.arraycopy(toBytes(SW_COMMAND_NOT_ALLOWED),0,response,0,2);
				
			}
			else
			{	
				
				response = new byte[bytesToWrite+2];
				
				System.arraycopy(data, 0, card, pageNumber*4, bytesToWrite);
				
				System.arraycopy(toBytes(SW_SUCCESS),0,response,bytesToWrite,2);
			}
			return response;*/
		}

		   private static byte[] toBytes(short s) {
		        return new byte[] { (byte) ((s & 0xff00) >> 8), (byte) (s & 0xff) };
		    }

		public byte[] endConnection() {
			byte[] b = new byte[passC.size()];
			int i=0;
			for (Object by : passC){
				b[i] = (Byte) by;
				i++;
			}
			User user = User.getInstance(MainActivity.activity);
			user.getPlan().setPassCode(new String(b).trim());
			//Toast.makeText(MainActivity.activity, new String(b).trim(), Toast.LENGTH_SHORT).show();
			return "OK".getBytes();
		}
}
