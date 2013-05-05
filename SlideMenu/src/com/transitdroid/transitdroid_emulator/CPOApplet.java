package com.transitdroid.transitdroid_emulator;

import static com.transitdroid.transitdroid_emulator.ISO7816.*;

import java.io.IOException;
import java.util.Arrays;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class CPOApplet {
	
	 private static final String TAG = CPOApplet.class.getSimpleName();
	 
	 private Thread appletThread;

	 private volatile boolean isRunning = false;

	 private TagWrapper tag;
	 
	 private Handler handler;
	 
	 UserInfo info;
	    
	 public CPOApplet(Handler h)
	 {
		 handler = h;
		 
		 info = new UserInfo();
	 }
	 
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
	                         case INS_READ_BINARY:
	                        	 blockNum = cmd[OFFSET_P2];
	                        	 numBytes = cmd[OFFSET_LC];
	                        	 
	                        	 //cmd = transceive(card.readBinary(blockNum, numBytes));
	                        	 cmd = transceive(info.readInfo(blockNum, numBytes));
	                        	 
	                             break;
	                         case INS_UPDATE_BINARY:
	                        	 byte code = cmd[OFFSET_P2];
	                        	 cmd = transceive(info.updateBinary(code));
	                        	 break;
	                         case INS_COMPLETE:
	                        	 cmd = transceive(info.endConnection());
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
	        };

	        appletThread = new Thread(r);
	        appletThread.setName("CPO applet thread#" + appletThread.getId());
	        appletThread.start();
	        isRunning = true;
	        Log.d(TAG,String.format("Started applet thread '%s'",appletThread.getName()));
	    }
	 
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
	    
	    private void logMessage(String m)
	    {
			Log.d(TAG,m);
			 
			Message msg = Message.obtain();
			msg.obj = m;
			handler.handleMessage(msg);
	    }
	    
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
