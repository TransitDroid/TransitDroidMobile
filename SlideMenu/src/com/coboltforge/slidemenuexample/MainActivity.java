
package com.coboltforge.slidemenuexample;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.coboltforge.datasource.PictureTDG;
import com.coboltforge.datasource.SettingsTDG;
import com.coboltforge.datasource.UserTDG;
import com.coboltforge.domain.Setting;
import com.coboltforge.domain.User;
import com.coboltforge.slidemenu.SlideMenu;
import com.coboltforge.slidemenu.SlideMenu.SlideMenuItem;
import com.coboltforge.slidemenu.SlideMenuInterface.OnSlideMenuItemClickListener;
import com.transitdroid.transitdroid_emulator.CPOApplet;
import com.transitdroid.transitdroid_emulator.OpusActivity;
import com.transitdroid.transitdroid_emulator.TagWrapper;

public class MainActivity extends Activity implements OnSlideMenuItemClickListener {

	private static int status = 0;
	//private static Bitmap photo;
	
	private SlideMenu slidemenu;
	private final static int MYOPUSID = 42;
	private final static int MYPROFILEID = 43;
	private final static int MYACCOUNTID = 44;
	private final static int MYPLANID = 45;
	private final static int MYUSAGEID = 46;
	private final static int MYSETID = 47;
	
	public static MainActivity activity;
	
	//private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    
    //String root = Environment.getExternalStorageDirectory().toString();
    static String imagePath;
    private PictureTDG picture;
    
    private static User user;
    private UserTDG userTdg;
    
    private static Setting setting;
    private SettingsTDG setTdg;
    
    /*
     * ***** NFC READER ****
     */
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] filters;
    private String[][] techLists;
    
    private static final String TECH_ISO_PCDA = "android.nfc.tech.IsoPcdA";
    
    private static final String TAG = OpusActivity.class.getSimpleName();
    
    CPOApplet cpoApplet;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		activity = this;
		/*ActionBar actionbar = getActionBar();
        actionbar.setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
		
		/*
		 * There are two ways to add the slide menu: 
		 * From code or to inflate it from XML (then you have to declare it in the activities layout XML)
		 */
		// this is from code. no XML declaration necessary, but you won't get state restored after rotation.
//		slidemenu = new SlideMenu(this, R.menu.slide, this, 333);
		// this inflates the menu from XML. open/closed state will be restored after rotation, but you'll have to call init.
		slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
		slidemenu.init(this, R.menu.slide, this, 333);
		
		// this can set the menu to initially shown instead of hidden
//		slidemenu.setAsShown(); 
		
		// set optional header image
		//slidemenu.setHeaderImage(getResources().getDrawable(R.drawable.ic_launcher));
		
		slidemenu.clearMenuItems();
		slidemenu.setBackgroundColor(android.R.color.darker_gray);
		
		// this demonstrates how to dynamically add menu items
		SlideMenuItem item1 = new SlideMenuItem();
		item1.id = MYOPUSID;
		item1.icon = getResources().getDrawable(R.drawable.opus);
		item1.label = "My OPUS Card";
		slidemenu.addMenuItem(item1);
		
		SlideMenuItem item2 = new SlideMenuItem();
		item2.id = MYPROFILEID;
		item2.icon = getResources().getDrawable(R.drawable.profile);
		item2.label = "My Profile";
		slidemenu.addMenuItem(item2);
		
		SlideMenuItem item3 = new SlideMenuItem();
		item3.id = MYACCOUNTID;
		item3.icon = getResources().getDrawable(R.drawable.logo_stm);
		item3.label = "My Account";
		slidemenu.addMenuItem(item3);
		
		SlideMenuItem item4 = new SlideMenuItem();
		item4.id = MYPLANID;
		item4.icon = getResources().getDrawable(R.drawable.plan);
		item4.label = "My OPUS Plan";
		slidemenu.addMenuItem(item4);
		
		SlideMenuItem item5 = new SlideMenuItem();
		item5.id = MYUSAGEID;
		item5.icon = getResources().getDrawable(R.drawable.usage);
		item5.label = "My Usage";
		slidemenu.addMenuItem(item5);
		
		SlideMenuItem item6 = new SlideMenuItem();
		item6.id = MYSETID;
		item6.icon = getResources().getDrawable(R.drawable.settings);
		item6.label = "Settings";
		slidemenu.addMenuItem(item6);
		
		SlideMenuItem item7 = new SlideMenuItem();
		item7.label = "";
		slidemenu.addMenuItem(item7);
		
		// connect the fallback button in case there is no ActionBar
		Button b = (Button) findViewById(R.id.buttonMenu);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				slidemenu.show();
			}
		});
		
		picture = new PictureTDG(this);
		userTdg = new UserTDG(this);
		user = User.getInstance(this);
		setTdg = new SettingsTDG(this);
		setting = setTdg.getSetting();
		
		setContentView();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
	
	public void setContentView(){
		switch(status){
		case 1: profile(); break;
		case 2: account(); break;
		case 3: setContentView(R.layout.plan); break;
		case 4: setContentView(R.layout.usage); break;
		case 5: settings(); break;
		default: opusCard();
		}
	}

	@Override
	public void onSlideMenuItemClick(int itemId) {
		ImageView i = (ImageView)findViewById(android.R.id.home);
		//TextView t = (TextView)findViewById(android.R.id.title);
		switch(itemId) {
		
		/*case R.id.item_one:
			Toast.makeText(this, "Item one selected", Toast.LENGTH_SHORT).show();
			break;
		case R.id.item_two:
			Toast.makeText(this, "Item two selected", Toast.LENGTH_SHORT).show();
			break;
		case R.id.item_three:
			Toast.makeText(this, "Item three selected", Toast.LENGTH_SHORT).show();
			break;
		case R.id.item_four:
			Toast.makeText(this, "Item four selected", Toast.LENGTH_SHORT).show();
			break;*/
		case MYOPUSID:
			Toast.makeText(this, "Opus Card selected", Toast.LENGTH_SHORT).show();
			opusCard();
			i.setImageResource(R.drawable.opus);
			setTitle("TransitDroid Mobile");
			status = 0;
			break;
		case MYPROFILEID:
			Toast.makeText(this, "Profile selected", Toast.LENGTH_SHORT).show();
			profile();
			i.setImageResource(R.drawable.profile);
			setTitle("TransitDroid Profile");
			status = 1;
			break;
		case MYACCOUNTID:
			Toast.makeText(this, "Account selected", Toast.LENGTH_SHORT).show();
			account();
			i.setImageResource(R.drawable.logo_stm);
			setTitle("TransitDroid Account");
			status = 2;
			break;
		case MYPLANID:
			Toast.makeText(this, "Plan selected", Toast.LENGTH_SHORT).show();
			setContentView(R.layout.plan);
			i.setImageResource(R.drawable.plan);
			setTitle("TransitDroid Plan");
			status = 3;
			break;
		case MYUSAGEID:
			Toast.makeText(this, "Usage selected", Toast.LENGTH_SHORT).show();
			setContentView(R.layout.usage);
			i.setImageResource(R.drawable.usage);
			setTitle("TransitDroid Usage");
			status = 4;
			break;
		case MYSETID:
			Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
			settings();
			i.setImageResource(R.drawable.settings);
			setTitle("TransitDroid Settings");
			status = 5;
			break;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home: // this is the app icon of the actionbar
			slidemenu.show();
			break;
		case R.id.settingItem: 
			status = 5;
			setContentView();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void opusCard(){
		setContentView(R.layout.opus_card);
		RelativeLayout rel = (RelativeLayout)findViewById(R.id.rootRL);
		
		if (setting.getOpus_theme().equalsIgnoreCase("Classic")){
			rel.setBackgroundResource(R.drawable.opus2);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Dark")){
			rel.setBackgroundResource(R.drawable.opus2_dark);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Ginky")){
			rel.setBackgroundResource(R.drawable.ginky);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Green")){
			rel.setBackgroundResource(R.drawable.green);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Grey")){
			rel.setBackgroundResource(R.drawable.grey);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Indigo")){
			rel.setBackgroundResource(R.drawable.indigo);
		}
		
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
        	Toast t = Toast.makeText(this, "Place your phone on the reader.", Toast.LENGTH_LONG);
    		t.setDuration(10000);
    		t.show();
        }
	}
	
	@Override
    public void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent()");
        handleTag(intent);

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
	
	private void handleTag(Intent intent) {
        Log.d(TAG, "TECH_DISCOVERED: " + intent);
       //message.setText("Discovered tag  with intent: " + intent);
       
       try {
           Tag tag = null;
           if (intent.getExtras() != null) {
               tag = (Tag) intent.getExtras().get(NfcAdapter.EXTRA_TAG);
           }
           if (tag == null) {
               return;
           }

           //message.append("\n\n Tag: " + tag);
           List<String> techList = Arrays.asList(tag.getTechList());
           //message.append("\n\n Tech list: " + techList);
           if (!techList.contains(TECH_ISO_PCDA)) {
               Log.e(TAG, "IsoPcdA not found in tech list");
               return;
           }

           TagWrapper tw = new TagWrapper(tag, TECH_ISO_PCDA);
           Log.d(TAG, "isConnected() " + tw.isConnected());
           if (!tw.isConnected()) {
               tw.connect();
           }
           
           //message.append("Max length: " + tw.getMaxTransceiveLength());
           //message.append("\n");
           //message.append("Staring PKI applet thread...");

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
	
	final Handler someHandler=new Handler(){
    	public void handleMessage(Message msg)
    	{    
    		String data = (String) msg.obj;
    		//appendMessage(data);
    	}
    };
	
	public void profile(){
		setContentView(R.layout.profile);
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        
        //TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        
        TextView txt = (TextView)this.findViewById(R.id.textView1);
        txt.setText(user.getName());
        
        TextView txt2 = (TextView)this.findViewById(R.id.TextView2);
        txt2.setText(user.getId());
        
        TextView txt3 = (TextView)this.findViewById(R.id.textView3);
        txt3.setText("Exp: " + user.getPlan().getExp());
        
        TextView txt4 = (TextView)this.findViewById(R.id.textView4);
        txt4.setText("Plan: " + user.getPlan().getType());
        
        imagePath = picture.getImagePth();
        
        if (imagePath != null && setting.isShowPicture()){
        	this.imageView.setImageBitmap(loadBitmap(imagePath));
        	//Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
        }
        
        this.imageView.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        	      // TODO Auto-generated method stub
        	      Intent intent = new Intent(Intent.ACTION_PICK,
        	        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        	      startActivityForResult(intent, 0);
        	     }});
        
	}
	
	public void account(){
		
		setContentView(R.layout.account);
		
		TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        
        TabSpec spec1 = tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("PURCHASE");
        
        TabSpec spec2 = tabHost.newTabSpec("Tab 2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Mobile Accounts");
        
        TabSpec spec3 = tabHost.newTabSpec("Tab 3");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("Info");
        
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);
	}
	
	public void settings(){
		setContentView(R.layout.settings);
		
		String arry[] = new String[6];
		arry[0] = "Classic";
		arry[1] = "Dark";
		arry[2] = "Ginky";
		arry[3] = "Green";
		arry[4] = "Grey";
		arry[5] = "Indigo";
		Spinner s = (Spinner)findViewById(R.id.spinner1);
		ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arry);
		s.setAdapter(adapter);
		
		if (setting.getOpus_theme().equalsIgnoreCase("Classic")){
			s.setSelection(0);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Dark")){
			s.setSelection(1);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Ginky")){
			s.setSelection(2);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Green")){
			s.setSelection(3);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Grey")){
			s.setSelection(4);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Indigo")){
			s.setSelection(5);
		}
		
		ToggleButton tog = (ToggleButton)findViewById(R.id.toggleButton1);
		tog.setChecked(setting.isShowPicture());
		
		((EditText)findViewById(R.id.EditText01)).setText(user.getName());
		((EditText)findViewById(R.id.editText1)).setText(user.getUsername());
		((EditText)findViewById(R.id.editText2)).setText(user.getPassword());
		
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // TODO Auto-generated method stub
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK){
	     Uri targetUri = data.getData();
	     //textTargetUri.setText(targetUri.toString());
	     picture.insertIntoTable(getPath(targetUri));
	     Bitmap bitmap;
	     try {
		      bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
		      imageView.setImageBitmap(bitmap);
		      
	     } catch (FileNotFoundException e) {
	      // TODO Auto-generated catch block
	    	 e.printStackTrace();
	     }
    }
    }
	
	public String getPath(Uri uri) {

	    try{
	    String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = managedQuery(uri, projection, null, null, null);
	    if(cursor==null)
	    {
	        return null;

	    }
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	    }
	    catch(Exception e)
	    {
	        return null;
	    }

	}
	
	public Bitmap loadBitmap(String url)
	{
	    Bitmap bm = null;
	    InputStream is = null;
	    BufferedInputStream bis = null;
	    try 
	    {
	        URLConnection conn = new URL("file://" + url).openConnection();
	        conn.connect();
	        is = conn.getInputStream();
	        bis = new BufferedInputStream(is, 8192);
	        bm = BitmapFactory.decodeStream(bis);
	    }
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	    finally {
	        if (bis != null) 
	        {
	            try 
	            {
	                bis.close();
	            }
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	        if (is != null) 
	        {
	            try 
	            {
	                is.close();
	            }
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	    }
	    return bm;
	}
	
	public void saveSettings(View v){
		String displayName = ((EditText)findViewById(R.id.EditText01)).getText().toString();
		user.setName(displayName);
		String username = ((EditText)findViewById(R.id.editText1)).getText().toString();
		user.setUsername(username);
		String password = ((EditText)findViewById(R.id.editText2)).getText().toString();
		user.setPassword(password);
		
		int pos = ((Spinner)findViewById(R.id.spinner1)).getSelectedItemPosition();
		String theme;
		switch(pos){
			case 0: theme="Classic"; break;
			case 1: theme="Dark";break;
			case 2: theme="Ginky";break;
			case 3: theme="Green";break;
			case 4: theme="Grey";break;
			case 5: theme="Indigo";break;
			default: theme="Default";
		}
		setting.setOpus_theme(theme);
		setting.setShowPicture(((ToggleButton)findViewById(R.id.toggleButton1)).isChecked());
		
		Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show();
	}
	
	/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST) {  
            photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
            saveImage(photo);
        }  
    } 
	
	public void saveImage(Bitmap bmp) {
		int size = bmp.getWidth() * bmp.getHeight();
		ByteArrayOutputStream out = new ByteArrayOutputStream(size);
		bmp.compress(Bitmap.CompressFormat.PNG, 100, out);   
		byte[] bytSig = out.toByteArray();
		
		insertIntoTable(bytSig);
	}
	
	public Bitmap getImage(byte[] bytes){
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}*/
	
}