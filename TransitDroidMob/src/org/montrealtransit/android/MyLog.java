package org.montrealtransit.android;

import android.util.Log;

/**
 * This class is used to customize and optimize the log.
 * @author Mathieu Méa
 */
public class MyLog {

	/**
	 * Use this boolean to enable full LOG (VERBOSE)
	 */
	public static final boolean DEBUG = false;

	/**
	 * Use this to enable location display.
	 */
	public static final boolean SHOW_LOCATION = false;
	
	/**
	 * The log tag for all the logs from the app.
	 */
	public static final String MAIN_TAG = "MonTransit";

	/**
	 * @param level the level to check
	 * @return true if the level is loggable
	 */
	public static boolean isLoggable(int level) {
		return DEBUG || Log.isLoggable(MAIN_TAG, level);
	}

	/**
	 * @see Log#v(String, String)
	 * @param tag the class tag
	 * @param msg the message
	 */
	public static void v(String tag, String msg) {
		if (isLoggable(Log.VERBOSE)) {
			Log.v(MAIN_TAG, String.format("%s>%s", tag, msg));
		}
	}

	/**
	 * @see Log#v(String, String)
	 * @param tag the class tag
	 * @param msg the message
	 * @param args the message arguments
	 */
	public static void v(String tag, String msg, Object... args) {
		if (isLoggable(Log.VERBOSE)) {
			Log.v(MAIN_TAG, String.format("%s>%s", tag, String.format(msg, args)));
		}
	}

	/**
	 * @see Log#d(String, String)
	 * @param tag the class tag
	 * @param msg the message
	 */
	public static void d(String tag, String msg) {
		if (isLoggable(Log.DEBUG)) {
			Log.d(MAIN_TAG, String.format("%s>%s", tag, msg));
		}
	}

	/**
	 * @see Log#d(String, String)
	 * @param tag the class tag
	 * @param msg the message
	 * @param args the message arguments
	 */
	public static void d(String tag, String msg, Object... args) {
		if (isLoggable(Log.DEBUG)) {
			Log.d(MAIN_TAG, String.format("%s>%s", tag, String.format(msg, args)));
		}
	}

	/**
	 * @see Log#d(String, String, Throwable)
	 * @param tag the class tag
	 * @param t the error
	 * @param msg the message
	 * @param args the message arguments
	 */
	public static void d(String tag, Throwable t, String msg, Object... args) {
		if (isLoggable(Log.DEBUG)) {
			Log.d(MAIN_TAG, String.format("%s>%s", tag, String.format(msg, args)), t);
		}
	}

	/**
	 * @see Log#i(String, String)
	 * @param tag the class log
	 * @param msg the message
	 */
	public static void i(String tag, String msg) {
		if (isLoggable(Log.INFO)) {
			Log.i(MAIN_TAG, String.format("%s>%s", tag, msg));
		}
	}

	/**
	 * @see Log#i(String, String)
	 * @param tag the class log
	 * @param msg the message
	 * @param args the message arguments
	 */
	public static void i(String tag, String msg, Object... args) {
		if (isLoggable(Log.INFO)) {
			Log.i(MAIN_TAG, String.format("%s>%s", tag, String.format(msg, args)));
		}
	}

	/**
	 * @see Log#w(String, String)
	 * @param tag the class tag
	 * @param msg the message
	 * @param args the message arguments
	 */
	public static void w(String tag, String msg, Object... args) {
		if (isLoggable(Log.WARN)) {
			Log.w(MAIN_TAG, String.format("%s>%s", tag, String.format(msg, args)));
		}
	}

	/**
	 * @see Log#w(String, String, Throwable)
	 * @param tag the class tag
	 * @param t the error
	 * @param msg the message
	 * @param args the message arguments
	 */
	public static void w(String tag, Throwable t, String msg, Object... args) {
		if (isLoggable(Log.WARN)) {
			Log.w(MAIN_TAG, String.format("%s>%s", tag, String.format(msg, args)), t);
		}
	}

	/**
	 * @see Log#e(String, String, Throwable)
	 * @param tag the class tag
	 * @param t the error
	 * @param msg the message
	 */
	public static void e(String tag, Throwable t, String msg) {
		if (isLoggable(Log.ERROR)) {
			Log.e(MAIN_TAG, String.format("%s>%s", tag, msg), t);
		}
	}
}
