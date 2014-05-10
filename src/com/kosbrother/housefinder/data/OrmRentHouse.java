package com.kosbrother.housefinder.data;

import com.j256.ormlite.field.DatabaseField;

public class OrmRentHouse
{
	
	public final static String Column_Rent_ID_NAME = "rent_id";
	
	
//	public RentHouse(int rent_id, String title, String promote_pic, int price,
//			String address, double rent_area, int layer, int total_layer,
//			int rooms, int rest_rooms, double x_long, double y_lat,
//			int rent_type_id)
	
	@DatabaseField(generatedId = true)
	public int id;

	@DatabaseField(columnName = Column_Rent_ID_NAME)
	public int rent_id;
	
	@DatabaseField
	public String title;
	
	@DatabaseField
	public String promote_pic;
	
	@DatabaseField
	public int price;
	
	@DatabaseField
	public String address;
	
	@DatabaseField
	public double rent_area;
	
	@DatabaseField
	public int layer;
	
	@DatabaseField
	public int total_layer;
	
	@DatabaseField
	public int rooms;
	
	@DatabaseField
	public int rest_rooms;
	
	@DatabaseField
	public double x_long;
	@DatabaseField
	public double y_lat;

	@DatabaseField
	public int rent_type_id;


	public OrmRentHouse()
	{
		// needed by ormlite
	}

}
