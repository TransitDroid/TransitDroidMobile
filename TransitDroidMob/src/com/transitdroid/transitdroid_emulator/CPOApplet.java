package com.transitdroid.transitdroid_emulator;

import static com.transitdroid.transitdroid_emulator.ISO7816.INS_COMPLETE;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_READ_KEY_1_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_READ_KEY_2_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_READ_KEY_3_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_READ_KEY_4_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_READ_PASS_CODE_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_READ_PHONE_ID_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_UPDATE_KEY1_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_UPDATE_KEY2_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_UPDATE_KEY3_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_UPDATE_KEY4_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.INS_UPDATE_PASSCODE_BINARY;
import static com.transitdroid.transitdroid_emulator.ISO7816.OFFSET_INS;
import static com.transitdroid.transitdroid_emulator.ISO7816.OFFSET_P2;
import static com.transitdroid.transitdroid_emulator.ISO7816.SW_SUCCESS;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.shipp.activity.PrincipalActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.coboltforge.domain.Usage;

/**
 * Java Applet to start the communication between the phone and the reader
 * @author Austin Takam
 *
 */
public class CPOApplet {
	
	 private static final String TAG = CPOApplet.class.getSimpleName();
	 
	 private Thread appletThread;

	 private volatile boolean isRunning = false;

	 private TagWrapper tag;
	 
	 private Handler handler;
	 
	 private static PrincipalActivity activity;
	 
	 private static UserInfo info;
	    
	 /**
	  * Constructor
	  * @param h
	  * @param activity
	  */
	 public CPOApplet(Handler h, PrincipalActivity activity)
	 {
		 handler = h;
		 this.activity = activity;
		 info = new UserInfo();
	 }
	 
	 /**
	  * Open the communication and start accepting/receiving bytes
	  * @param tag
	  * @throws IOException
	  */
	 public void start(TagWrapper tag) throws IOException {
	        this.tag = tag;

	        Runnable r = new Runnable() {
	            public void run() {
	            	
	                try {
	                	  // send dummy data to get first command APDU
	                    // at least two bytes to keep smartcardio happy
	                    byte[] cmd = transceive(new byte[] { (byte) 0x90, 0x00 });
	                    int blockNum;
	                    int numBytes;
	                    do {	                        
	                    	
	                    	 byte ins = cmd[OFFSET_INS];
	                    	 
	                         switch (ins) {
	                         case INS_READ_PHONE_ID_BINARY:
	                        	 cmd = transceive(info.readPhoneId());
	                             break;
	                         case INS_READ_PASS_CODE_BINARY:
	                        	 cmd = transceive(info.readPassCode());
	                             break;
	                         case INS_READ_KEY_1_BINARY:
	                        	 cmd = transceive(info.readKey1());
	                             break;
	                         case INS_READ_KEY_2_BINARY:
	                        	 cmd = transceive(info.readKey2());
	                             break;
	                         case INS_READ_KEY_3_BINARY:
	                        	 cmd = transceive(info.readKey3());
	                             break;
	                         case INS_READ_KEY_4_BINARY:
	                        	 cmd = transceive(info.readKey4());
	                             break;
	                         case INS_UPDATE_PASSCODE_BINARY:
	                        	 byte code0 = cmd[OFFSET_P2];
	                        	 cmd = transceive(info.updatePassCodeBinary(code0));
	                        	 break;
	                         case INS_UPDATE_KEY1_BINARY:
	                        	 byte code1 = cmd[OFFSET_P2];
	                        	 cmd = transceive(info.updateKey1Binary(code1));
	                        	 break;
	                         case INS_UPDATE_KEY2_BINARY:
	                        	 byte code2 = cmd[OFFSET_P2];
	                        	 cmd = transceive(info.updateKey2Binary(code2));
	                        	 break;
	                         case INS_UPDATE_KEY3_BINARY:
	                        	 byte code3 = cmd[OFFSET_P2];
	                        	 cmd = transceive(info.updateKey3Binary(code3));
	                        	 break;
	                         case INS_UPDATE_KEY4_BINARY:
	                        	 byte code4 = cmd[OFFSET_P2];
	                        	 cmd = transceive(info.updateKey4Binary(code4));
	                        	 break;
	                         case INS_COMPLETE:
	                        	 byte machine = cmd[OFFSET_P2];
	                        	 log(machine);
	                        	 //cmd = transceive(info.endConnection());
	                        	 info.flushData();
	                        	 
	                        	 break;
	                         default:
	                        	 logMessage(String.format("[%s] Unknown INS %s", appletThread.getName(),ins));
	                        	 cmd = transceive(toBytes(SW_SUCCESS));
	                        	 break;
	                         }
	                         
	                         
	                
	                    } while (cmd != null && !Thread.interrupted());	                    
	                }
	                	catch (Exception e) {	              
	                    Log.e(TAG, "SE error: " + e.getMessage(), e);
	                    Log.d(TAG, String.format("Stopping applet thread '%s'",appletThread.getName()));

	                    resetState();
	                    // a bit more explicit
	                    isRunning = false;
	                    return;
	                }
	                // if interrupted
	                isRunning = false;
	            }

				private void log(byte machine) {
					String machineName = null;
					switch(machine){
						case 0x01:
							machineName = "Test Lab 1";
							break;
						default:
							machineName = "Unknown Test Lab";
					}
					String date = getCurrentTimeStamp();
					new Usage(machineName, date, activity);
				}
				
				public String getCurrentTimeStamp() {
				    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
				    Date now = new Date();
				    String strDate = sdfDate.format(now);
				    return strDate;
				}
	        };

	        appletThread = new Thread(r);
	        appletThread.setName("CPO applet thread#" + appletThread.getId());
	        appletThread.start();
	        isRunning = true;
	        Log.d(TAG,String.format("Started applet thread '%s'",appletThread.getName()));
	    }
	 
	 /**
	  * Stop the communication action
	  */
	 public synchronized void stop() {
	        Log.d(TAG, "stopping applet thread");
	        if (appletThread != null) {
	            appletThread.interrupt();
	            Log.d(TAG, "applet thread running: " + isRunning);
	        }

	        Log.d(TAG, "Resetting applet state");
	        resetState();
	}
	 
    public boolean isRunning() {
        return isRunning;
    }
    
    /**
     * Reset the state of the tag
     */
    private void resetState() {
        if (tag != null) {
            try {
                if (tag.isConnected()) {
                    tag.close();
                }
                tag = null;
            } catch (IOException e) {
                Log.w(TAG, "Error closing tag: " + e.getMessage(), e);
            }
        }
    }
    
    /**
     * Logging message string
     * @param m
     */
    private void logMessage(String m)
    {
		Log.d(TAG,m);
		 
		Message msg = Message.obtain();
		msg.obj = m;
		handler.handleMessage(msg);
    }
    
    /**
     * Method to transfer bytes from the phone to the reader
     * @param cmd
     * @return byteArray
     * @throws IOException
     */
    private byte[] transceive(byte[] cmd) throws IOException {
		
    	//log what we send
	    logMessage(String.format("[%s] --> %s", appletThread.getName(),toHex(cmd)));
	    
	     //send
        byte[] response = tag.transceive(cmd);	        
        
        //log what we  received
        logMessage(String.format("[%s] <-- %s", appletThread.getName(),toHex(response)));
	     
        return response;
    }

    private static String toHex(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (byte b : bytes) {
            buff.append(String.format("%02X", b));
        }

        return buff.toString();
    }
    
    private static byte[] toBytes(short s) {
        return new byte[] { (byte) ((s & 0xff00) >> 8), (byte) (s & 0xff) };
    }
}
