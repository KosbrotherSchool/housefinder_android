package com.kosbrother.housefinder;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

public class Setting
{
	public final static String keyPref = "Preference";

	public final static String keyPurpose = "Purpose";
	public final static String keyRentHousePriceMin = "RP_Min";
	public final static String keyRentHousePriceMax = "RP_Max";
	public final static String keyRentType = "RentType";
	public final static String keyBuildingType = "BuildingType";
	public final static String keyAreaMin = "Area_Min";
	public final static String keyAreaMax = "Area_Max";
	public final static String keyKmDistance = "Km_Distance";

	public final static String keyFirstOpenV2 = "First_OpenV2";
	public final static String KeyCurrentDateNum = "Current_Data_Date";
	public final static String KeyGiveStar = "Give_Star";
	public final static String KeyPushStarDialog = "Push_Star_Dialog";

	public final static String initialPurpose = "0"; // 0 for buy, 1 for sell
	public final static String initialRentHousePriceMin = "0";
	public final static String initialRentHousePriceMax = "0"; // 0 for max
	public final static String initialRentType = "0";
	public final static String initialBuildingType = "0";
	public final static String initialAreaMin = "0";
	public final static String initialAreaMax = "0"; // 0 for max
	public final static String initialKmDistance = "0.5";
	public final static boolean initialFirstOpenV2 = true;
	public final static int initialCurrentDate = 10212;
	public final static boolean initialGiveStar = false;
	public final static boolean initialPushStarDialog = true;

	private static final HashMap<String, String> initMap = new HashMap<String, String>()
	{
		{
			put(keyPurpose, initialPurpose);
			put(keyRentHousePriceMin, initialRentHousePriceMin);
			put(keyRentHousePriceMax, initialRentHousePriceMax);
			put(keyRentType, initialRentType);
			put(keyBuildingType, initialBuildingType);
			put(keyAreaMin, initialAreaMin);
			put(keyAreaMax, initialAreaMax);
			put(keyKmDistance, initialKmDistance);
		}
	};

	private static final HashMap<String, Boolean> booleanMap = new HashMap<String, Boolean>()
	{
		{
			put(KeyGiveStar, initialGiveStar);
			put(KeyPushStarDialog, initialPushStarDialog);
			put(keyFirstOpenV2, initialFirstOpenV2);
		}
	};

	public static String getSetting(String settingKey, Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		String settingValue = sharePreference.getString(settingKey,
				initMap.get(settingKey));
		return settingValue;
	}

	public static void saveSetting(String settingKey, String settingValue,
			Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		sharePreference.edit().putString(settingKey, settingValue).commit();
	}

	public static boolean getBooleanSetting(String settingkey, Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		boolean booleanValue = sharePreference.getBoolean(settingkey,
				booleanMap.get(settingkey));
		return booleanValue;
	}

	public static void saveBooleanSetting(String settingKey,
			boolean settingBooleanValue, Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		sharePreference.edit().putBoolean(settingKey, settingBooleanValue)
				.commit();
	}



}
