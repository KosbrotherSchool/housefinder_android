package com.kosbrother.housefinder.data;

import com.j256.ormlite.field.DatabaseField;

public class OrmHouse
{
	public final static String Column_House_ID_NAME = "house_id";

	@DatabaseField(generatedId = true)
	public int id;
	
	@DatabaseField
	public int house_type_id;
	
	@DatabaseField(columnName = Column_House_ID_NAME)
	public int house_id;

	@DatabaseField
	public String title;

	@DatabaseField
	public String promote_pic;

	@DatabaseField
	public int price;

	@DatabaseField
	public String address;

	@DatabaseField
	public double area;

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
	
	@DatabaseField
	public int ground_type_id;
	
	public OrmHouse()
	{
		// needed by ormlite
	}
}
