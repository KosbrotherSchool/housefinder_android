package com.kosbrother.houseprice.entity;

public class LandMark
{
	public int landMark_id;
	public int type_id;
	public String title;
	public double x_lng;
	public double y_lat;
	public int dis_meter;

	public LandMark(int landMark_id, int type_id, String title, double x_lng,
			double y_lat, int dis_meter)
	{
		this.landMark_id = landMark_id;
		this.type_id = type_id;
		this.title = title;
		this.x_lng = x_lng;
		this.y_lat = y_lat;
	}

	public static int parseType(String type)
	{
		int type_int = 0;
		if (type.equals("school"))
		{
			type_int = 1;
		} else if (type.equals("convenience_store"))
		{
			type_int = 2;
		} else if (type.equals("park"))
		{
			type_int = 3;
		} else if (type.equals("grocery_or_supermarket"))
		{
			type_int = 4;
		} else if (type.equals("hospital"))
		{
			type_int = 5;
		} else if (type.equals("subway_station"))
		{
			type_int = 6;
		}
		return type_int;
	}

}
