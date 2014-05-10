package com.kosbrother.housefinder;

import android.content.Context;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.model.LatLng;

public class AppConstants
{
//	public static boolean isSaledMarkerShow = true;
//	public static boolean isRentMarkerShow = true;
//	public static boolean isPreSaleMarkerShow = true;
//	public static int salePerSquareMin = 0;
//	// if is 0, means no need to add query string
//	public static int salePerSquareMax = 0;
//	public static int saleTotalMin = 0;
//	// if is 0, means no need to add query string
//	public static int saleTotalMax = 0;
//	public static double saleAreaMin = 0;
//	// if is 0, means no need to add query string
//	public static double saleAreaMax = 0;
	public static LatLng currentLatLng;
	
	public static final String MEDIATION_KEY = "7a1211cf15804292";
	
	public static final int PAGE_ITME_NUM = 100;
	
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
}
