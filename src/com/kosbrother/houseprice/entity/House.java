package com.kosbrother.houseprice.entity;

import java.util.ArrayList;

public class House
{
	public int house_id;

	public String title;
	public String promote_pic;
	public int price;
	public String address;
	public double square_price;
	public double total_area;

	public int layer;
	public int total_layer;

	public int building_age;

	public int rooms;
	public int living_rooms;
	public int rest_rooms;
	public int balconies;

	public String parkingString;

	public double x_long;
	public double y_lat;

	public int guard_price;
	public String orientation;
	public boolean is_renting;
	public String ground_explanation;
	public String living_explanation;

	public String feature_html;

	public String vender_name;
	public String phone_number;

	public int county_id;
	public int town_id;
	public int ground_type_id;
	public int building_type_id;

	public boolean is_show;

	public ArrayList<String> picArrayList;

	public House(int house_id, String title, String promote_pic, int price,
			String address, double total_area, int layer, int total_layer,
			int rooms, int rest_rooms, double x_long, double y_lat,
			int ground_type_id)
	{
		this.house_id = house_id;
		this.title = title;
		this.promote_pic = promote_pic;
		this.price = price;
		this.address = address;
		this.total_area = total_area;

		this.layer = layer;
		this.total_layer = total_layer;

		this.building_age = 0;

		this.rooms = rooms;
		this.living_rooms = 0;
		this.rest_rooms = rest_rooms;
		this.balconies = 0;

		this.parkingString = "";

		this.x_long = x_long;
		this.y_lat = y_lat;

		this.guard_price = 0;
		this.orientation = "";
		this.is_renting = false;
		this.ground_explanation = "";
		this.living_explanation = "";

		this.feature_html = "";

		this.vender_name = "";
		this.phone_number = "";

		this.county_id = 0;
		this.town_id = 0;
		this.ground_type_id = ground_type_id;
		this.building_type_id = 0;
		this.picArrayList = new ArrayList<String>();
	}

	public House(int house_id, String title, String promote_pic, int price,
			String address, double square_price, double total_area, int layer, int total_layer,
			int building_age, int rooms, int living_rooms, int rest_rooms,
			int balconies, String parkingString, double x_long, double y_lat,
			int guard_price, String orientation, Boolean is_renting,
			String ground_explanation, String living_explanation,
			String feature_html, String vender_name, String phone_number,
			int county_id, int town_id, int ground_type_id,
			int building_type_id, boolean is_show)
	{
		this.house_id = house_id;
		this.title = title;
		this.promote_pic = promote_pic;
		this.price = price;
		this.address = address;
		this.square_price = square_price;
		this.total_area = total_area;

		this.layer = layer;
		this.total_layer = total_layer;

		this.building_age = building_age;

		this.rooms = rooms;
		this.living_rooms = living_rooms;
		this.rest_rooms = rest_rooms;
		this.balconies = balconies;

		this.parkingString = parkingString;

		this.x_long = x_long;
		this.y_lat = y_lat;

		this.guard_price = guard_price;
		this.orientation = orientation;
		this.is_renting = is_renting;

		this.ground_explanation = ground_explanation;
		this.living_explanation = living_explanation;

		this.feature_html = feature_html;

		this.vender_name = vender_name;
		this.phone_number = phone_number;

		this.county_id = county_id;
		this.town_id = town_id;
		this.ground_type_id = ground_type_id;
		this.building_type_id = building_type_id;

		this.is_show = is_show;

		this.picArrayList = new ArrayList<String>();
	}

}
