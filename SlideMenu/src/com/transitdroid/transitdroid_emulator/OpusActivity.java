package com.transitdroid.transitdroid_emulator;

import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.coboltforge.slidemenuexample.R;

public class OpusActivity extends Activity {
	
	private TextView message;
	
	private NfcAdapter adapter;
	
	private PendingIntent pendingIntent;
    private IntentFilter[] filters;
    private String[][] techLists;
    
    private static final String TECH_ISO_PCDA = "android.nfc.tech.IsoPcdA";
    
    private static final String TAG = OpusActivity.class.getSimpleName();
    
    CPOApplet cpoApplet;
    
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        message = (TextView) findViewById(R.id.message);
        
        adapter = NfcAdapter.getDefaultAdapter(this);
        
        adapter.setNdefPushMessage(null, this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            adapter.setBeamPushUris(null, this);
        }
        
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        filters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };
        techLists = new String[][] { { "android.nfc.tech.IsoPcdA" } };

        cpoApplet = new CPOApplet(someHandler);

        Intent intent = getIntent();
        String action = intent.getAction();
        Log.d(TAG, "Intent: " + intent);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            handleTag(intent);
        } else {
            message.setText("Place on reader");
        }
        
    }

    private void handleTag(Intent intent) {
        Log.d(TAG, "TECH_DISCOVERED: " + intent);
       message.setText("Discovered tag  with intent: " + intent);
       
       try {
           Tag tag = null;
           if (intent.getExtras() != null) {
               tag = (Tag) intent.getExtras().get(NfcAdapter.EXTRA_TAG);
           }
           if (tag == null) {
               return;
           }

           message.append("\n\n Tag: " + tag);
           List<String> techList = Arrays.asList(tag.getTechList());
           message.append("\n\n Tech list: " + techList);
           if (!techList.contains(TECH_ISO_PCDA)) {
               Log.e(TAG, "IsoPcdA not found in tech list");
               return;
           }

           TagWrapper tw = new TagWrapper(tag, TECH_ISO_PCDA);
           Log.d(TAG, "isConnected() " + tw.isConnected());
           if (!tw.isConnected()) {
               tw.connect();
           }
           
           message.append("Max length: " + tw.getMaxTransceiveLength());
           message.append("\n");
           message.append("Staring PKI applet thread...");

           // stop and start a fresh thread for each new connection
           // shouldn't be needed since onNewIntent() is called only
           // after we enter the reader field again, and exiting it
           // should kill the previous thread
           if (cpoApplet != null) {
               Log.d(TAG, "Applet running: " + cpoApplet.isRunning());
               if (cpoApplet.isRunning()) {
                   Log.d(TAG, "Applet thread alredy running, stopping");
                   cpoApplet.stop();
               }
           }
           cpoApplet.start(tw);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        
        if (adapter != null) {
            adapter.enableForegroundDispatch(this, pendingIntent, filters,
                    techLists);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
        
        if (adapter != null) {
            Log.d(TAG, "disabling foreground dispatch");
            adapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent()");
        handleTag(intent);

    }
    
    public void appendMessage(final String s)
    {

    	 runOnUiThread(new Runnable() {
    	        public void run() {
    	    		message.append("\n\n" + s);
    	       }
    	   });
    }
   

    final Handler someHandler=new Handler(){
    	public void handleMessage(Message msg)
    	{    
    		String data = (String) msg.obj;
    		appendMessage(data);
    	}
    };
}