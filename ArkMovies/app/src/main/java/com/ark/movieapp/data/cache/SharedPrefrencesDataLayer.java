package com.ark.movieapp.data.cache;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * @author ahmedb
 *
 */
public class SharedPrefrencesDataLayer {
	private final static String SHARED_KEY = "shared_Prefe_Data_layer_Key";

	private static SharedPreferences setUp(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SHARED_KEY, Context.MODE_PRIVATE);
		return sharedPreferences;
	}

	public static void saveStringPreferences(Context context,
			Map<String, String> values) {
		for (Map.Entry<String, String> item : values.entrySet()) {
			saveStringPreferences(context, item.getKey(), item.getValue());
		}
	}

	public static void saveStringPreferences(Context context, String key,
			String value) {
		SharedPreferences share = setUp(context);
		share.edit().putString(key, value).apply();
	}

	public static String getStringPreferences(Context context, String key,
			String DefualtValue) {
		SharedPreferences share = setUp(context);
		return share.getString(key, DefualtValue);
	}

	public static void saveIntPreferences(Context context,
			Map<String, Integer> values) {
		for (Map.Entry<String, Integer> item : values.entrySet()) {
			saveIntPreferences(context, item.getKey(), item.getValue());
		}
	}
	
	public static void saveIntPreferences(Context context, String key, int value) {
		SharedPreferences share = setUp(context);
		share.edit().putInt(key, value).apply();
	}
	
	public static int getIntPreferences(Context context, String key,
			int DefualtValue) {
		SharedPreferences share = setUp(context);
		return share.getInt(key, DefualtValue);
	}

	public static void saveFloatPreferences(Context context,
			Map<String, Float> values) {
		for (Map.Entry<String, Float> item : values.entrySet()) {
			saveFloatPreferences(context, item.getKey(), item.getValue());
		}
	}
	
	public static void saveFloatPreferences(Context context, String key,
			float value) {
		SharedPreferences share = setUp(context);
		share.edit().putFloat(key, value).apply();
	}

	public static float getFloatPreferences(Context context, String key,
			float DefualtValue) {
		SharedPreferences share = setUp(context);
		return share.getFloat(key, DefualtValue);
	}

	public static void saveBooleanPreferences(Context context,
			Map<String, Boolean> values) {
		for (Map.Entry<String, Boolean> item : values.entrySet()) {
			saveBooleanPreferences(context, item.getKey(), item.getValue());
		}
	}
	
	public static void saveBooleanPreferences(Context context, String key,
			boolean value) {
		SharedPreferences share = setUp(context);
		share.edit().putBoolean(key, value).apply();
	}

	public static boolean getBooleanPreferences(Context context, String key,
			boolean DefualtValue) {
		SharedPreferences share = setUp(context);
		return share.getBoolean(key, DefualtValue);
	}

	public static void saveLongPreferences(Context context,
			Map<String, Long> values) {
		for (Map.Entry<String, Long> item : values.entrySet()) {
			saveLongPreferences(context, item.getKey(), item.getValue());
		}
	}
	
	public static void saveLongPreferences(Context context, String key,
			long value) {
		SharedPreferences share = setUp(context);
		share.edit().putLong(key, value).apply();
	}

	public static long getLongPreferences(Context context, String key,
			long DefualtValue) {
		SharedPreferences share = setUp(context);
		return share.getLong(key, DefualtValue);
	}

}
