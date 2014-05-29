package com.kosbrother.housefinder;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.model.LatLng;

public class AppConstants
{
	public static LatLng currentLatLng;

	public static final String MEDIATION_KEY = "7a1211cf15804292";

	public static final int TYPE_ID_SALE = 1;
	public static final int TYPE_ID_RENT = 2;

	public static double km_dis = 0.3;
	public static int startDate;
	public static int endDate;

	public static float convertDpToPixel(float dp, Context context)
	{
		float px = dp * getDensity(context);
		return px;
	}

	public static float getDensity(Context context)
	{
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.density;
	}

	public static boolean isTablet(Context context)
	{
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static float getMapSizeFromDistance(double km_dis)
	{
		float mapSize = 15.0f;
		if (0 < km_dis && km_dis <= 0.3)
		{
			mapSize = 16.5f;
		} else if (0.3 < km_dis && km_dis <= 0.5)
		{
			mapSize = 16.0f;
		} else if (0.5 < km_dis && km_dis <= 1)
		{
			mapSize = 15.0f;
		} else if (1 < km_dis && km_dis <= 2)
		{
			mapSize = 14.0f;
		} else
		{
			mapSize = 13.0f;
		}
		return mapSize;
	}
}
