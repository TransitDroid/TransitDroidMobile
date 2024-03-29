package org.montrealtransit.android.provider;

import org.montrealtransit.android.MyLog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * This database helper is used to access the user data.
 * @author Mathieu Méa
 */
public class DataDbHelper extends SQLiteOpenHelper {

	/**
	 * The log tag.
	 */
	private static final String TAG = DataDbHelper.class.getSimpleName();
	/**
	 * The database file.
	 */
	private static final String DATABASE_NAME = "data.db";
	/**
	 * The database version use to manage database changes.
	 */
	public static final int DATABASE_VERSION = 5;

	/**
	 * The favorites table.
	 */
	public static final String T_FAVS = "favs";
	/**
	 * The favorite ID field.
	 */
	public static final String T_FAVS_K_ID = BaseColumns._ID;
	/**
	 * The favorite FK_ID field.
	 */
	public static final String T_FAVS_K_FK_ID = "fk_id";
	/**
	 * The favorite FK_ID2 field.
	 */
	public static final String T_FAVS_K_FK_ID_2 = "fk_id2";
	/**
	 * The favorite title field.
	 */
	public static final String T_FAVS_K_TITLE = "title";
	/**
	 * The favorite type field.
	 */
	public static final String T_FAVS_K_TYPE = "type";

	/**
	 * The favorite type value for bus stops.
	 */
	public static final int KEY_FAVS_TYPE_VALUE_BUS_STOP = 1;
	/**
	 * The favorite type value for subway stations.
	 */
	public static final int KEY_FAVS_TYPE_VALUE_SUBWAY_STATION = 2;
	/**
	 * The favorite type value for bike stations.
	 */
	public static final int KEY_FAVS_TYPE_VALUE_BIKE_STATION = 3;

	/**
	 * The history table.
	 */
	public static final String T_HISTORY = "history";
	/**
	 * The history ID field.
	 */
	public static final String T_HISTORY_K_ID = BaseColumns._ID;
	/**
	 * The history value field.
	 */
	public static final String T_HISTORY_K_VALUE = "value";

	/**
	 * The Twitter API table.
	 */
	public static final String T_TWITTER_API = "twitter_api";
	/**
	 * The Twitter API ID field.
	 */
	public static final String T_TWITTER_API_K_ID = BaseColumns._ID;
	/**
	 * The Twitter API OAuth token field.
	 */
	public static final String T_TWITTER_API_K_TOKEN = "oauth_token";
	/**
	 * The Twitter API OAuth token secret field.
	 */
	public static final String T_TWITTER_API_K_TOKEN_SECRET = "oauth_token_secret";

	/**
	 * The service status table.
	 */
	public static final String T_SERVICE_STATUS = "service_status";
	/**
	 * The service status ID field.
	 */
	public static final String T_SERVICE_STATUS_K_ID = BaseColumns._ID;
	/**
	 * The service status message field.
	 */
	public static final String T_SERVICE_STATUS_K_MESSAGE = "message";
	/**
	 * The service status message publish date.
	 */
	public static final String T_SERVICE_STATUS_K_PUB_DATE = "pub_date";
	/**
	 * The service status message read date.
	 */
	public static final String T_SERVICE_STATUS_K_READ_DATE = "read_date";
	/**
	 * The service status message language.
	 */
	public static final String T_SERVICE_STATUS_K_LANGUAGE = "lang";
	/**
	 * The service status message source.
	 */
	public static final String T_SERVICE_STATUS_K_SOURCE = "source";
	/**
	 * The service status message link.
	 */
	public static final String T_SERVICE_STATUS_K_LINK = "link";
	/**
	 * The service status message type.
	 */
	public static final String T_SERVICE_STATUS_K_TYPE = "type";

	public static final int SERVICE_STATUS_TYPE_DEFAULT = 0;
	public static final int SERVICE_STATUS_TYPE_GREEN = 1;
	public static final int SERVICE_STATUS_TYPE_YELLOW = 2;
	public static final int SERVICE_STATUS_TYPE_RED = 3;

	public static final String SERVICE_STATUS_LANG_UNKNOWN = "";
	public static final String SERVICE_STATUS_LANG_ENGLISH = "en";
	public static final String SERVICE_STATUS_LANG_FRENCH = "fr";

	/**
	 * The cache table.
	 */
	public static final String T_CACHE = "cache";
	/**
	 * The cache ID field.
	 */
	public static final String T_CACHE_K_ID = BaseColumns._ID;
	/**
	 * The cache date field.
	 */
	public static final String T_CACHE_K_DATE = "date";
	/**
	 * The cache type field.
	 */
	public static final String T_CACHE_K_TYPE = "type";

	/**
	 * The cache FK_ID field.
	 */
	public static final String T_CACHE_K_FK_ID = "fk_id";
	/**
	 * The cache serialized object.
	 */
	public static final String T_CACHE_K_OBJECT = "object";

	/**
	 * The cache type value for bus stops hours.
	 */
	public static final int KEY_CACHE_TYPE_VALUE_BUS_STOP = 1;

	/**
	 * Database creation SQL statement for the favorite table.
	 */
	private static final String DATABASE_CREATE_T_FAVS = "create table " + T_FAVS + " (" + T_FAVS_K_ID
	        + " integer primary key autoincrement, " + T_FAVS_K_TYPE + " integer, " + T_FAVS_K_FK_ID + " text, "
	        + T_FAVS_K_FK_ID_2 + " text, " + T_FAVS_K_TITLE + " text);";
	/**
	 * Database creation SQL statement for the history table.
	 */
	private static final String DATABASE_CREATE_T_HISTORY = "create table " + T_HISTORY + " (" + T_HISTORY_K_ID
	        + " integer primary key autoincrement, " + T_HISTORY_K_VALUE + " text);";

	/**
	 * Database creation SQL statement for the Twitter API table.
	 */
	private static final String DATABASE_CREATE_T_TWITTER_API = "create table " + T_TWITTER_API + " ("
	        + T_TWITTER_API_K_ID + " integer primary key autoincrement, " + T_TWITTER_API_K_TOKEN + " text, "
	        + T_TWITTER_API_K_TOKEN_SECRET + " text);";
	/**
	 * Database creation SQL statement for the service status table.
	 */
	private static final String DATABASE_CREATE_T_SERVICE_STATUS = "create table " + T_SERVICE_STATUS + " ("
	        + T_SERVICE_STATUS_K_ID + " integer primary key autoincrement, " + T_SERVICE_STATUS_K_MESSAGE + " text, "
	        + T_SERVICE_STATUS_K_PUB_DATE + " integer, " + T_SERVICE_STATUS_K_READ_DATE + " integer, "
	        + T_SERVICE_STATUS_K_TYPE + " integer, " + T_SERVICE_STATUS_K_LANGUAGE + " text, "
	        + T_SERVICE_STATUS_K_SOURCE + " text, " + T_SERVICE_STATUS_K_LINK + " text);";

	/**
	 * Database creation SQL statement for the Cache table.
	 */
	private static final String DATABASE_CREATE_T_CACHE = "create table " + T_CACHE + " (" + T_CACHE_K_ID
	        + " integer primary key autoincrement, " + T_CACHE_K_DATE + " integer, " + T_CACHE_K_TYPE + " integer, "
	        + T_CACHE_K_FK_ID + " text," + T_CACHE_K_OBJECT + " text);";

	/**
	 * Default constructor.
	 */
	public DataDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		MyLog.v(TAG, "DataDbHelper()");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		MyLog.v(TAG, "onCreate()");
		db.execSQL(DATABASE_CREATE_T_FAVS);
		db.execSQL(DATABASE_CREATE_T_HISTORY);
		db.execSQL(DATABASE_CREATE_T_TWITTER_API);
		db.execSQL(DATABASE_CREATE_T_SERVICE_STATUS);
		db.execSQL(DATABASE_CREATE_T_CACHE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MyLog.v(TAG, "onUpgrade(%s, %s)", oldVersion, newVersion);
		MyLog.d(TAG, "Upgrading database from version %s to %s, which may destroy all old data!", oldVersion,
		        newVersion);
		switch (oldVersion) {
		case 1:
			MyLog.v(TAG, "add the History table");
			// just create the History table
			db.execSQL(DATABASE_CREATE_T_HISTORY);
		case 2:
			MyLog.v(TAG, "add the Twitter API table");
			// just create the Twitter API table
			db.execSQL(DATABASE_CREATE_T_TWITTER_API);
		case 3:
			MyLog.v(TAG, "add the Service Status table");
			// just create the Service Status table
			db.execSQL(DATABASE_CREATE_T_SERVICE_STATUS);
		case 4:
			MyLog.v(TAG, "add the Cache table");
			// just create the Cache table
			db.execSQL(DATABASE_CREATE_T_CACHE);
			break;
		default:
			MyLog.w(TAG, "Old user data destroyed!");
			db.execSQL("DROP TABLE IF EXISTS " + T_FAVS);
			db.execSQL("DROP TABLE IF EXISTS " + T_HISTORY);
			db.execSQL("DROP TABLE IF EXISTS " + T_TWITTER_API);
			db.execSQL("DROP TABLE IF EXISTS " + T_SERVICE_STATUS);
			db.execSQL("DROP TABLE IF EXISTS " + T_CACHE);
			onCreate(db);
			break;
		}
	}

	@Override
	public void close() {
		MyLog.v(TAG, "close()");
		super.close();
	}
}
