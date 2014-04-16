package com.kosbrother.houseprice.entity;

import android.R.integer;

public class RentHouse
{

	public int rent_id;

	public String title;
	public String promote_pic;
	public int price;
	public String address;
	public String deposit;
	public double rent_area;

	public int layer;
	public int total_layer;

	public int rooms;
	public int living_rooms;
	public int rest_rooms;
	public int balconies;

	public String parkingString;

	public double x_long;
	public double y_lat;

	public int guard_price;
	public String min_rent_time;
	public boolean is_cooking;
	public boolean is_pet;
	public String identity;
	public String sexual_restriction;
	public String orientation;
	public String furniture;
	public String equipment;
	public String living_explanation;
	public String communication;

	public String feature_html;

	public String vender_name;
	public String phone_number;

	public int county_id;
	public int town_id;
	public int rent_type_id;
	public int building_type_id;

	public RentHouse(int rent_id, String title, String promote_pic, int price,
			String address, double rent_area, int layer, int total_layer,
			int rooms, int rest_rooms, double x_long, double y_lat,
			int rent_type_id)
	{
		this.rent_id = rent_id;
		this.title = title;
		this.promote_pic = promote_pic;
		this.price = price;
		this.address = address;
		this.rent_area = rent_area;

		this.layer = layer;
		this.total_layer = total_layer;

		this.rooms = rooms;
		this.living_rooms = 0;
		this.rest_rooms = rest_rooms;
		this.balconies = 0;

		this.parkingString = "";

		this.x_long = x_long;
		this.y_lat = y_lat;

		this.guard_price = 0;
		this.min_rent_time = "";
		this.is_cooking = false;
		this.is_pet = false;
		this.identity = "";
		this.sexual_restriction = "";
		this.orientation = "";
		this.furniture = "";
		this.equipment = "";
		this.living_explanation = "";
		this.communication = "";

		this.feature_html = "";

		this.vender_name = "";
		this.phone_number = "";

		this.county_id = 0;
		this.town_id = 0;
		this.rent_type_id = rent_type_id;
		this.building_type_id = 0;
	}

	public RentHouse(int rent_id, String title, String promote_pic, int price,
			String address, String deposit, double rent_area, int layer,
			int total_layer, int rooms, int living_rooms, int rest_rooms,
			int balconies, String parkingString, double x_long, double y_lat,
			int guard_price, String min_rent_time, boolean is_cooking,
			boolean is_pet, String identity, String sexual_restriction,
			String orientation, String furniture, String equipment,
			String living_explanation, String communication,
			String feature_html, String vender_name, String phone_number,
			int county_id, int town_id, int rent_type_id, int building_type_id)
	{
		this.rent_id = rent_id;
		this.title = title;
		this.promote_pic = promote_pic;
		this.price = price;
		this.address = address;
		this.deposit = deposit;
		this.rent_area = rent_area;

		this.layer = layer;
		this.total_layer = total_layer;

		this.rooms = rooms;
		this.living_rooms = living_rooms;
		this.rest_rooms = rest_rooms;
		this.balconies = balconies;

		this.parkingString = parkingString;

		this.x_long = x_long;
		this.y_lat = y_lat;

		this.guard_price = guard_price;
		this.min_rent_time = min_rent_time;
		this.is_cooking = is_cooking;
		this.is_pet = is_pet;
		this.identity = identity;
		this.sexual_restriction = sexual_restriction;
		this.orientation = orientation;
		this.furniture = furniture;
		this.equipment = equipment;
		this.living_explanation = living_explanation;
		this.communication = communication;

		this.feature_html = feature_html;

		this.vender_name = vender_name;
		this.phone_number = phone_number;

		this.county_id = county_id;
		this.town_id = town_id;
		this.rent_type_id = rent_type_id;
		this.building_type_id = building_type_id;
	}

}
