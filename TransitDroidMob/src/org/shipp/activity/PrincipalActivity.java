package org.shipp.activity;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.montrealtransit.android.LocationUtils;
import org.montrealtransit.android.MenuUtils;
import org.montrealtransit.android.SubwayUtils;
import org.montrealtransit.android.activity.Bus;
import org.montrealtransit.android.activity.Subway;
import org.shipp.activity.disclaimer.DisclaimerActivity;
import org.shipp.util.MenuEventController;
import org.shipp.util.MenuLazyAdapter;
import org.shipp.util.MyArrayAdapter;
import org.shipp.util.OnSwipeTouchListener;
import org.transitdroid.httprequest.PurchaseRestClient;
import org.transitdroid.httprequest.RegisterRestClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.coboltforge.datasource.PictureTDG;
import com.coboltforge.domain.Purchase;
import com.coboltforge.domain.Setting;
import com.coboltforge.domain.Usage;
import com.coboltforge.domain.User;
import com.transitdroid.transitdroid_emulator.CPOApplet;
import com.transitdroid.transitdroid_emulator.TagWrapper;

/**
 * Main Activity class which controls the entire app
 * @author Austin Takam
 *
 */
public class PrincipalActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	
    
	private RelativeLayout layout;
	private MenuLazyAdapter menuAdapter;
	private boolean open = false;
    
	private final Context context = this;
    
	private ListView listMenu;
	private TextView appName;
	
	private static String imagePath;
    private PictureTDG picture;
    
    private static Setting setting;
    public static PrincipalActivity activity;
    
    public static String keys;
    

	public enum RequestMethod
	{
	    GET,
	    POST
	}
	
	public enum PurchaseType
	{
	    SINGLE,
	    DAILY,
	    NIGHTLY,
	    WEEKLY,
	    MONTHLY, 
	    YEARLY
	}
	
	boolean $purchase = false;

    
    //private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    
    //Usage attributes
    ArrayAdapter<String> aa;
    ArrayAdapter<String> pa;
    
    //Themes attributes
    private static int themeNumber = 0;
    
    static User user; 
    
    static int layout_Id;
    /*
     * ***** NFC READER ATTRIBUTES ****
     */
    private NfcAdapter adapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] filters;
    private String[][] techLists;
    private static final String TECH_ISO_PCDA = "android.nfc.tech.IsoPcdA";
    private static final String TAG = PrincipalActivity.class.getSimpleName();
    CPOApplet cpoApplet;
    
    
    Bus bus;
    Subway subway;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        bus = new Bus(this);
        subway = new Subway(this);
        
        displayContents(R.layout.opus_card);
        //Toast.makeText(PrincipalActivity.this, "Creation", Toast.LENGTH_SHORT).show();
    }
    
    /**
    * Show closest bus stops (called when user clicks gps icon).
    */
    public void showClosest(View v) {
    	bus.showClosest(v);
    }
    
    /**
    * Show bus lines (called when user clicks on grid icon).
    */
    public void showGrid(View v) {
    	bus.showGrid(v);
    }
    
    /**
    * Show the STM subway map.
    * @param v the view (not used)
    */
    public void showSTMSubwayMap(View v) {
    SubwayUtils.showSTMSubwayMap(this);
    }
    
    /**
    * Refresh or not refresh the status depending of the task status.
    * @param v
    */
    public void refreshOrStopRefreshStatus(View v) {
    	subway.refreshOrStopRefreshStatus(v);
    }
    
    /**
    * Refresh or stop refresh the closest stations depending if running.
    * @param v a view (not used)
    */
    public void refreshOrStopRefreshClosestStations(View v) {
    	subway.refreshOrStopRefreshClosestStations(v);
    }
    public void showSubwayStatusInfoDialog(View v) {
    	subway.showSubwayStatusInfoDialog(v);
    }
    /**
     * Displays the content of the specied layout id
     * @param layoutId
     */
    public void displayContents(int layoutId){
    	//disclaimer
    	activity = this;
    	setting = Setting.getSetting(activity);
    	
    	Toast.makeText(PrincipalActivity.this, setting.getDisclaimer(), Toast.LENGTH_SHORT).show();
    	
    	if (setting.getDisclaimer().trim().equalsIgnoreCase("No")){
    		startActivity(new Intent(this, DisclaimerActivity.class));
    	}
    	else{
    		/*
        	 * Common to all layouts
        	 * - The sliding menu
        	 * - The title (App Name)
        	 */
        	setContentView(layoutId);
        	layout_Id = layoutId;
            this.listMenu = (ListView) findViewById(R.id.listMenu);
            this.layout = (RelativeLayout) findViewById(R.id.layoutToMove);
            this.appName = (TextView) findViewById(R.id.appName);
            
            /* *Stop GPS**/
            LocationUtils.disableLocationUpdates(this, Subway.subway);
            LocationUtils.disableLocationUpdates(this, Bus.bus);
            
            user = User.getInstance(activity);

            this.menuAdapter = new MenuLazyAdapter(this, MenuEventController.menuArray.size() == 0 ? MenuEventController.getMenuDefault(this) : MenuEventController.menuArray);
            this.listMenu.setAdapter(menuAdapter);
            
            this.layout.setOnTouchListener(new OnSwipeTouchListener() {
                public void onSwipeRight() {
                    if(!open){
                    	open = true;
                    	MenuEventController.open(context, layout, appName);
                    	MenuEventController.closeKeyboard(context, getCurrentFocus());
                    }
                }
                public void onSwipeLeft() {
                	if(open){
                		open = false;
                		MenuEventController.close(context, layout, appName);
                		MenuEventController.closeKeyboard(context, getCurrentFocus());
                	}
                }
            });
            
            picture = new PictureTDG(this);
    		
    		if (setting.getOpus_theme().equalsIgnoreCase("Original")){
    			themeNumber = 1;
    		}
    		else if (setting.getOpus_theme().equalsIgnoreCase("BlackF")){
    			themeNumber = 2;
    		}
    		else if (setting.getOpus_theme().equalsIgnoreCase("GreenF")){
    			themeNumber = 3;
    		}
    		else if (setting.getOpus_theme().equalsIgnoreCase("Modern")){
    			themeNumber = 4;
    		}
    		else if (setting.getOpus_theme().equalsIgnoreCase("Ancient")){
    			themeNumber = 5;
    		}
    		else if (setting.getOpus_theme().equalsIgnoreCase("Classic")){
    			themeNumber = 6;
    		}
            
    		//CLICK LISTENER TO RESPOND THE ONCLICK ACTION FOR THE MENU ITEMS
            this.listMenu.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    				Intent intent = null;
    				if(position == 0){
    					//Toast.makeText(PrincipalActivity.this, "Card", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.opus_card);
    					
    				} 
    				else if(position == 1){
    					//Toast.makeText(PrincipalActivity.this, "Profile", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.profile);
    					
    				} else if(position == 2){
    					//Toast.makeText(PrincipalActivity.this, "Account", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.account);
    					
    				} else if(position == 3){
    					//Toast.makeText(PrincipalActivity.this, "Purchase", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.purchase);
    					
    				} else if(position == 4){
    					//Toast.makeText(PrincipalActivity.this, "Usage Details", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.usage);
    					
    				} else if(position == 5){
    					//Toast.makeText(PrincipalActivity.this, "Bus Schedule", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.bus_tab);
    					bus.showAll();
    				} else if(position == 6){
    					//Toast.makeText(PrincipalActivity.this, "Metro Schedule", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.subway_tab);
    					subway.showAll();					
    				}else if(position == 7){
    					//Toast.makeText(PrincipalActivity.this, "Themes", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.themes);
    				} else if(position == 8){
    					//Toast.makeText(PrincipalActivity.this, "Register", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.register);
    					
    				} else if(position == 9){
    					//Toast.makeText(PrincipalActivity.this, "About", Toast.LENGTH_SHORT).show();
    					displayContents(R.layout.about);
    					
    				}
    				if(open){
    					open = false;
    					MenuEventController.close(context, layout, appName);
    					MenuEventController.closeKeyboard(context, view);
                	}
    			}
    		});
            
            /*
             * Calling specific layout content details 
             */
            switch (layoutId){
        	case R.layout.opus_card:
        		opusCard();
        		break;
        	case R.layout.profile:
    			profile();
    			break;
        	case R.layout.account:
    			account();
    			break;
    	    case R.layout.usage:
    			usage();
    			break;
    	    case R.layout.themes:
    			themes();
    			break;
    	    case R.layout.register:
    			register();
    			break;
    	    case R.layout.purchase:
    			purchase();
    			break;
    		}
    	}
    	
    }

    /**
     * Function to open/close menu on swipe 
     * @param view
     */
	public void openCloseMenu(View view){
    	if(!this.open){
    		this.open = true;
    		MenuEventController.open(this.context, this.layout, this.appName);
    		MenuEventController.closeKeyboard(this.context, view);
    	} else {
    		this.open = false;
    		MenuEventController.close(this.context, this.layout, this.appName);
    		MenuEventController.closeKeyboard(this.context, view);
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
    
    /**
     * Function specific to the Card view of the app
     */
    public void opusCard(){
    	
		//FrameLayout rel = (FrameLayout)findViewById(R.id.frameLayout);
    	ImageView image = (ImageView) findViewById(R.id.opusImage);
        
		/*
		 * Sets the theme of the card according to the theme stored in the setting object
		 */
		if (setting.getOpus_theme().equalsIgnoreCase("Original")){
			image.setImageResource(R.drawable.card0);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("BlackF")){
			image.setImageResource(R.drawable.card5);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("GreenF")){
			image.setImageResource(R.drawable.card6);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Modern")){
			image.setImageResource(R.drawable.card2);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Ancient")){
			image.setImageResource(R.drawable.card3);
		}
		else if (setting.getOpus_theme().equalsIgnoreCase("Classic")){
			image.setImageResource(R.drawable.opus2);
		}
		
		/*
		 * Initialize the NFC attributes and ready to transmit NFC data
		 */
		adapter = NfcAdapter.getDefaultAdapter(this);
        
        adapter.setNdefPushMessage(null, this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            adapter.setBeamPushUris(null, this);
        }
        
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        filters = new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) };
        techLists = new String[][] { { "android.nfc.tech.IsoPcdA" } };

        cpoApplet = new CPOApplet(someHandler, this);

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
	
	private final Handler someHandler=new Handler(){
    	public void handleMessage(Message msg)
    	{    
    		String data = (String) msg.obj;
    		//appendMessage(data);
    	}
    };
	
    /**
     * Get picture path from selected picture
     * @param uri
     * @return
     */
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
	
	/**
	 * Get picture image from specified path
	 * @param url
	 * @return
	 */
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
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
		    	 e.printStackTrace();
		     }
	    }
    }
	
	/**
	 * Function specific to the profile view of the app
	 */
	public void profile(){
    	//Sets the display name according to the settings object
    	this.imageView = (ImageView)this.findViewById(R.id.profile_pic);
        TextView txt = (TextView)this.findViewById(R.id.display_name);
        txt.setText(setting.getDisplayName());
        
        //Sets the image if any, and the possibility to change by touching the image area on the app
        imagePath = picture.getImagePth();
        if (imagePath != null){
        	this.imageView.setImageBitmap(loadBitmap(imagePath));
        }
        this.imageView.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View arg0) {
        	      Intent intent = new Intent(Intent.ACTION_PICK,
        	        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        	      startActivityForResult(intent, 0);
        	     }});
        
        findViewById(R.id.opus_icon).setOnClickListener(this);
        findViewById(R.id.register_icon).setOnClickListener(this);
        findViewById(R.id.purchase_icon).setOnClickListener(this);
        findViewById(R.id.usage_icon).setOnClickListener(this);
        findViewById(R.id.schedule_icon).setOnClickListener(this);
        findViewById(R.id.themes_icon).setOnClickListener(this);
        
	}
	
	/**
	 * Function specific to the account view of the app
	 */
	public void account(){
		//Setting the display Name on the app
		this.imageView = (ImageView)this.findViewById(R.id.profile_pic);
        TextView txt = (TextView)this.findViewById(R.id.display_name);
        txt.setText(setting.getDisplayName());
        
        //Setting the profile picture
        imagePath = picture.getImagePth();
        if (imagePath != null){
        	this.imageView.setImageBitmap(loadBitmap(imagePath));
        }
	}
	
	/**
	 * Function specific to the usage view of the app
	 */
	public void usage(){
		//Setting the list view with the customized display adapter for displaying usage details
		ListView view = (ListView)findViewById(R.id.listView1);
		Button clear = (Button)findViewById(R.id.but_clear);
		if (Usage.getUsage(this).isEmpty()){
			clear.setEnabled(false);
		}
		else {
			clear.setEnabled(true);
		}
		clear.setOnClickListener(this);
        aa = new MyArrayAdapter(this, android.R.layout.simple_list_item_1, Usage.getUsage(this));
        this.aa.notifyDataSetChanged();
        view.setAdapter(aa);
	}
	
	/**
	 * Function specific to the purchase view of the app
	 */
	public void purchase() {
		ListView view = (ListView)findViewById(R.id.purchases);
		pa = new MyArrayAdapter(this, android.R.layout.simple_list_item_1, Purchase.getPurchases(this));
		this.pa.notifyDataSetChanged();
        view.setAdapter(pa);
        
        Spinner s = (Spinner) findViewById(R.id.spin_passes);
        s.setSelection(0);
        
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
		R.array.plan_type_array, android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter1);
		s.setOnItemSelectedListener(this);
		
		if(s.getSelectedItemPosition()==1){
			findViewById(R.id.pick_occurrence).setEnabled(true);
		}else {
			findViewById(R.id.pick_occurrence).setEnabled(false);
		}
        
        NumberPicker pick = (NumberPicker)findViewById(R.id.pick_occurrence);
        pick.setMaxValue(12);
        pick.setValue(0);
        
        findViewById(R.id.but_reset).setOnClickListener(this);
        findViewById(R.id.but_purchase).setOnClickListener(this);
	}
	
	/**
	 * Function specific to the register view of the app
	 */
	public void register(){
		TextView t2 = (TextView) findViewById(R.id.reg_url);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
        
		//setting the status message according to the setting object
		TextView status = (TextView)findViewById(R.id.status_message);
		if(setting.isConnected()){
			status.setText("Connected");
			status.setTextColor(Color.GREEN);
			Toast.makeText(PrincipalActivity.this, "Connected", Toast.LENGTH_SHORT).show();
		}
		else{
			status.setText("Disconnected");
			status.setTextColor(Color.RED);
			Toast.makeText(PrincipalActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
		}
		
		findViewById(R.id.but_connect).setOnClickListener(this);
		findViewById(R.id.but_disconnect).setOnClickListener(this);
		
		((EditText)findViewById(R.id.username_text)).setText(setting.getUsername());
		((EditText)findViewById(R.id.password_text)).setText(setting.getPassword());
	}
	
	/**
	 * Function specific to the themes view of the app
	 */
	public void themes(){
		findViewById(R.id.but_original).setOnClickListener(this);
		findViewById(R.id.but_black_flower).setOnClickListener(this);
		findViewById(R.id.but_green_flower).setOnClickListener(this);
		findViewById(R.id.but_modern).setOnClickListener(this);
		findViewById(R.id.but_ancient).setOnClickListener(this);
		findViewById(R.id.but_classic).setOnClickListener(this);
		findViewById(R.id.but_save).setOnClickListener(this);
		
		((EditText)findViewById(R.id.display_name_text)).setText(setting.getDisplayName());
		
		switch (themeNumber){
		case 1: //original
			findViewById(R.id.but_original).setEnabled(false);
			findViewById(R.id.but_black_flower).setEnabled(true);
			findViewById(R.id.but_green_flower).setEnabled(true);
			findViewById(R.id.but_modern).setEnabled(true);
			findViewById(R.id.but_ancient).setEnabled(true);
			findViewById(R.id.but_classic).setEnabled(true);
			break;
		case 2: //black flower
			findViewById(R.id.but_original).setEnabled(true);
			findViewById(R.id.but_black_flower).setEnabled(false);
			findViewById(R.id.but_green_flower).setEnabled(true);
			findViewById(R.id.but_modern).setEnabled(true);
			findViewById(R.id.but_ancient).setEnabled(true);
			findViewById(R.id.but_classic).setEnabled(true);
			break;
		case 3: //green flower
			findViewById(R.id.but_original).setEnabled(true);
			findViewById(R.id.but_black_flower).setEnabled(true);
			findViewById(R.id.but_green_flower).setEnabled(false);
			findViewById(R.id.but_modern).setEnabled(true);
			findViewById(R.id.but_ancient).setEnabled(true);
			findViewById(R.id.but_classic).setEnabled(true);
			break;
		case 4: //modern
			findViewById(R.id.but_original).setEnabled(true);
			findViewById(R.id.but_black_flower).setEnabled(true);
			findViewById(R.id.but_green_flower).setEnabled(true);
			findViewById(R.id.but_modern).setEnabled(false);
			findViewById(R.id.but_ancient).setEnabled(true);
			findViewById(R.id.but_classic).setEnabled(true);
			break;
		case 5: //ancient
			findViewById(R.id.but_original).setEnabled(true);
			findViewById(R.id.but_black_flower).setEnabled(true);
			findViewById(R.id.but_green_flower).setEnabled(true);
			findViewById(R.id.but_modern).setEnabled(true);
			findViewById(R.id.but_ancient).setEnabled(false);
			findViewById(R.id.but_classic).setEnabled(true);
			break;
		case 6: //classic
			findViewById(R.id.but_original).setEnabled(true);
			findViewById(R.id.but_black_flower).setEnabled(true);
			findViewById(R.id.but_green_flower).setEnabled(true);
			findViewById(R.id.but_modern).setEnabled(true);
			findViewById(R.id.but_ancient).setEnabled(true);
			findViewById(R.id.but_classic).setEnabled(false);
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		// Responding to all onclick events registered to this class (this class implements the onclicklistener interface)
		switch(v.getId()){
		case R.id.but_original:
			setting.setOpus_theme("Original");
			themeNumber = 1;
			Toast.makeText(PrincipalActivity.this, "Original Selected", Toast.LENGTH_SHORT).show();
			themes();
			break;
		case R.id.but_black_flower:
			setting.setOpus_theme("BlackF");
			themeNumber = 2;
			Toast.makeText(PrincipalActivity.this, "Black Flower Selected", Toast.LENGTH_SHORT).show();
			themes();
			break;
		case R.id.but_green_flower:
			setting.setOpus_theme("GreenF");
			themeNumber = 3;
			Toast.makeText(PrincipalActivity.this, "Green Flower Selected", Toast.LENGTH_SHORT).show();
			themes();
			break;
		case R.id.but_modern:
			setting.setOpus_theme("Modern");
			themeNumber = 4;
			Toast.makeText(PrincipalActivity.this, "Ginky Selected", Toast.LENGTH_SHORT).show();
			themes();
			break;
		case R.id.but_ancient:
			setting.setOpus_theme("Ancient");
			themeNumber = 5;
			Toast.makeText(PrincipalActivity.this, "Ancient Selected", Toast.LENGTH_SHORT).show();
			themes();
			break;
		case R.id.but_classic:
			setting.setOpus_theme("Classic");
			themeNumber = 6;
			Toast.makeText(PrincipalActivity.this, "Classic Selected", Toast.LENGTH_SHORT).show();
			themes();
			break;
		case R.id.but_save:
			String displayName = ((EditText)findViewById(R.id.display_name_text)).getText().toString();
			setting.setDisplayName(displayName);
			themes();
			break;
		case R.id.but_connect: //Connecting to the webservice here
			String username = ((EditText)findViewById(R.id.username_text)).getText().toString();
			String password = ((EditText)findViewById(R.id.password_text)).getText().toString();
			setting.setCredentials(username, password);
			//request connection from webservice
			boolean connected = requestConection(username, password);
			//set setting object with response from webservice
			setting.setConnected(connected);
			register();
			break;
		case R.id.but_disconnect: //disconnecting from webservice
			setting.setCredentials("", "");
			setting.setConnected(false);
			register();
			break;
		case R.id.opus_icon:
			Toast.makeText(PrincipalActivity.this, "Card", Toast.LENGTH_SHORT).show();
			displayContents(R.layout.opus_card);
			break;
		case R.id.register_icon:
			Toast.makeText(PrincipalActivity.this, "Register", Toast.LENGTH_SHORT).show();
			displayContents(R.layout.register);
			break;
		case R.id.schedule_icon:
			Toast.makeText(PrincipalActivity.this, "Bus Schedule", Toast.LENGTH_SHORT).show();
			displayContents(R.layout.bus_tab);
			bus.showAll();
			break;
		case R.id.purchase_icon:
			Toast.makeText(PrincipalActivity.this, "Purchase", Toast.LENGTH_SHORT).show();
			displayContents(R.layout.purchase);
			break;
		case R.id.themes_icon:
			Toast.makeText(PrincipalActivity.this, "Themes", Toast.LENGTH_SHORT).show();
			displayContents(R.layout.themes);
			break;
		case R.id.usage_icon:
			Toast.makeText(PrincipalActivity.this, "Usage Details", Toast.LENGTH_SHORT).show();
			displayContents(R.layout.usage);
			break;
		case R.id.but_clear:
			Toast.makeText(PrincipalActivity.this, "Clear Usage Details", Toast.LENGTH_SHORT).show();
			Usage.clearUsage(this);
			usage();
			break;
		case R.id.but_reset:
			Toast.makeText(PrincipalActivity.this, "Reset", Toast.LENGTH_SHORT).show();
			purchase();
			break;
		case R.id.but_purchase: //This will call the webservice to reques the purchase action
			Spinner spin = (Spinner)findViewById(R.id.spin_passes);
			NumberPicker pick = (NumberPicker)findViewById(R.id.pick_occurrence);
			if (spin.getSelectedItemPosition() == 0){
				Toast.makeText(PrincipalActivity.this, "You must select a pass type", Toast.LENGTH_SHORT).show();
			}
			else if(pick.getValue() == 0 && spin.getSelectedItemPosition() == 1){
				Toast.makeText(PrincipalActivity.this, "Occurence cannot be 0", Toast.LENGTH_SHORT).show();
			}
			else{
				int position = spin.getSelectedItemPosition();
				int number = pick.getValue();
				
				if (requestPurchase(position, number)){
					
					new Purchase(getPassType(position), getCurrentTimeStamp(), getPassPrice(position, number), activity);
					Toast.makeText(PrincipalActivity.this, "Purchase Successful", Toast.LENGTH_SHORT).show();
					
						new AlertDialog.Builder(this)
				        .setIcon(android.R.drawable.ic_dialog_alert)
				        .setTitle("Purchase Type: " + getPassType(position))
				        .setMessage("Purchase Successful.")
				        .setPositiveButton("OK",null)
					    .show();
				}
				else {
					Toast.makeText(PrincipalActivity.this, "Could not purchase this pass. Try later.", Toast.LENGTH_SHORT).show();
					new AlertDialog.Builder(this)
			        .setIcon(android.R.drawable.ic_dialog_alert)
			        .setTitle("Purchase Type: " + getPassType(position))
			        .setMessage("Purchase Unsuccessful. Try Later.")
			        .setPositiveButton("OK",null)
				    .show();
				}
					
				purchase();
			}
			
			break;
		case R.id.spin_passes:
			Spinner s1 = (Spinner) findViewById(R.id.spin_passes);
			if(s1.getSelectedItemPosition()==1){
				findViewById(R.id.pick_occurrence).setEnabled(true);
			}else {
				findViewById(R.id.pick_occurrence).setEnabled(false);
			}
			break;
		}
	}
	
	private boolean requestPurchase(final int i, final int qty){
		
		PurchaseRestClient client = new PurchaseRestClient(PrincipalActivity.RequestMethod.POST, getPassEnumType(i), qty){
			@Override
			protected void onPostExecute(String result) {
				//Toast.makeText(PrincipalActivity.this, "11" + result, Toast.LENGTH_SHORT).show();
				keys = result;

//				Intent intent = new Intent(Intent.ACTION_SEND);
//				intent.setType("plain/text");
//				intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
//				intent.putExtra(Intent.EXTRA_SUBJECT, "Error while purchasing, type: " + getPassType(i));
//				intent.putExtra(Intent.EXTRA_TEXT, keys);
//				startActivity(Intent.createChooser(intent, ""));
					
				
				try {
					JSONObject  pages  =  new JSONObject(keys);
					//pages.get("succeed");
					$purchase = true;
					//Toast.makeText(PrincipalActivity.this, "Success", Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
					$purchase = false;
				}
				
				//Toast.makeText(PrincipalActivity.this, "22" + keys, Toast.LENGTH_SHORT).show();
			}
		};
		try {
			
			client.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(PrincipalActivity.this, "Nothing", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return $purchase;
	}
	
	private static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	private String getPassType(int position){
		switch(position){
		case 1:
			return "Single";
		case 2:
			return "Daily";
		case 3:
			return "Weekly";
		case 4: 
			return "Nightly";
		case 5: 
			return "Monthly";
		case 6:
			return "Yearly";
		}
		return "";
	}
	
	private PurchaseType getPassEnumType(int position){
		switch(position){
		case 1:
			return PurchaseType.SINGLE;
		case 2:
			return PurchaseType.DAILY;
		case 3:
			return PurchaseType.WEEKLY;
		case 4: 
			return PurchaseType.NIGHTLY;
		case 5: 
			return PurchaseType.MONTHLY;
		case 6:
			return PurchaseType.YEARLY;
		}
		return PurchaseType.MONTHLY;
	}
	
	private String getPassPrice(int position, int number){
		double p = 0.0;
		switch(position){
		case 1:
			p = 3.0;
			return "" + (p*(double)number);
		case 2:
			p = 9.0; 
			break;
		case 3:
			p = 23.75; 
			break;
		case 4: 
			p = 4.0; 
			break;
		case 5: 
			p = 77.0; 
			break;
		case 6:
			p = 800.0; 
			break;
		}
		return "" + (p);
	}

	private boolean requestConection(String username, String password) {
		boolean $return = false;
		RegisterRestClient client = new RegisterRestClient(RequestMethod.POST, username, password){
			
			@Override
			protected void onPostExecute(String result) {
				Toast.makeText(PrincipalActivity.this, "11" + result, Toast.LENGTH_SHORT).show();
				keys = result;
				
				try {
					JSONObject  pages  =  new JSONObject(keys);
					pages.get("user");
					Toast.makeText(PrincipalActivity.this, pages.get("user").toString(), Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					Toast.makeText(PrincipalActivity.this, "Nothing", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				
				Toast.makeText(PrincipalActivity.this, "22" + keys, Toast.LENGTH_SHORT).show();
			}
		};
		try {
			
			client.execute();
			$return = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(PrincipalActivity.this, "Nothing", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		
		return $return;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	return MenuUtils.inflateMenu(this, menu, R.menu.main_menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.stm_map) {
			showSTMSubwayMap(null);
			return true;
		} else {
			return MenuUtils.handleCommonMenuActions(this, item);
		}
	}
	
	@Override
	public void onBackPressed() {
		if (layout_Id != R.layout.opus_card){
			displayContents(R.layout.opus_card);
		}
		else {
				new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Closing Activity")
		        .setMessage("Are you sure you want to exit?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            finish();    
		        }
	
		    })
		    .setNegativeButton("No", null)
		    .show();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		Spinner s1 = (Spinner) findViewById(R.id.spin_passes);
		if(s1.getSelectedItemPosition()==1){
			findViewById(R.id.pick_occurrence).setEnabled(true);
		}else {
			findViewById(R.id.pick_occurrence).setEnabled(false);
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}