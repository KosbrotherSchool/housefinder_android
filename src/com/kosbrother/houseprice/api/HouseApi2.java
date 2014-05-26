package com.kosbrother.houseprice.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.kosbrother.housefinder.Datas;
import com.kosbrother.houseprice.entity.House;
import com.kosbrother.houseprice.entity.RentHouse;

public class HouseApi2
{
	final static String HOST = "http://106.187.39.88";
	public static final String TAG = "HOUSE_API2";
	public static final boolean DEBUG = true;

	public static House getHouseDetails(int house_id)
	{
		String message = getMessageFromServer("GET",
				"/api/v2/house/get_sale_detail?house_id=" + house_id, null,
				null);
		if (message == null)
		{
			return null;
		} else
		{
			return parseSaleHouseDetailMessage(message);
		}
	}

	private static House parseSaleHouseDetailMessage(String message)
	{
		try
		{
			JSONArray jArray;
			jArray = new JSONArray(message.toString());

			// for estate data
			int house_id = jArray.getJSONObject(0).getInt("id");

			String title = "";
			try
			{
				title = jArray.getJSONObject(0).getString("title");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String promote_pic = "";
			try
			{
				promote_pic = jArray.getJSONObject(0).getString(
						"promote_pic_link");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int price = 0;
			try
			{
				price = jArray.getJSONObject(0).getInt("price");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String address = "";
			try
			{
				address = jArray.getJSONObject(0).getString("address");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			double square_price = 0;
			try
			{
				square_price = jArray.getJSONObject(0)
						.getDouble("square_price");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			double total_area = 0;
			try
			{
				total_area = jArray.getJSONObject(0).getDouble("total_area");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int layer = 0;
			try
			{
				layer = jArray.getJSONObject(0).getInt("layer");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int total_layer = 0;
			try
			{
				total_layer = jArray.getJSONObject(0).getInt("total_layers");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int building_age = 0;
			try
			{
				building_age = jArray.getJSONObject(0).getInt("building_age");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int rooms = 0;
			try
			{
				rooms = jArray.getJSONObject(0).getInt("rooms");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int living_rooms = 0;
			try
			{
				living_rooms = jArray.getJSONObject(0).getInt("living_rooms");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int rest_rooms = 0;
			try
			{
				rest_rooms = jArray.getJSONObject(0).getInt("rest_rooms");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int balconies = 0;
			try
			{
				balconies = jArray.getJSONObject(0).getInt("balconies");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String parkingString = "";
			try
			{
				parkingString = jArray.getJSONObject(0).getString(
						"parking_type");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			Double x_long = jArray.getJSONObject(0).getDouble("x_long");
			Double y_lat = jArray.getJSONObject(0).getDouble("y_lat");

			int guard_price = 0;
			try
			{
				guard_price = jArray.getJSONObject(0).getInt("guard_price");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String orientation = "";
			try
			{
				orientation = jArray.getJSONObject(0).getString("orientation");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			boolean is_renting = false;
			try
			{
				is_renting = jArray.getJSONObject(0).getBoolean("is_renting");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String ground_explanation = "";
			try
			{
				ground_explanation = jArray.getJSONObject(0).getString(
						"ground_explanation");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String living_explanation = "";
			try
			{
				living_explanation = jArray.getJSONObject(0).getString(
						"living_explanation");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String feature_html = "";
			try
			{
				feature_html = jArray.getJSONObject(0)
						.getString("feature_html");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String vender_name = "";
			try
			{
				vender_name = jArray.getJSONObject(0).getString("verder_name");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String phone_number = "";
			try
			{
				phone_number = jArray.getJSONObject(0)
						.getString("phone_number");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int county_id = jArray.getJSONObject(0).getInt("county_id");
			int town_id = jArray.getJSONObject(0).getInt("town_id");

			int building_type_id = 0;
			try
			{
				building_type_id = jArray.getJSONObject(0).getInt(
						"building_type_id");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int ground_type_id = 0;
			try
			{
				ground_type_id = jArray.getJSONObject(0).getInt(
						"ground_type_id");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			boolean is_show = true;
			try
			{
				is_show = jArray.getJSONObject(0).getBoolean("is_show");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			House newHouse = new House(house_id, title, promote_pic, price,
					address, square_price, total_area, layer, total_layer,
					building_age, rooms, living_rooms, rest_rooms, balconies,
					parkingString, x_long, y_lat, guard_price, orientation,
					is_renting, ground_explanation, living_explanation,
					feature_html, vender_name, phone_number, county_id,
					town_id, ground_type_id, building_type_id, is_show);

			JSONArray jArrayPics = jArray.getJSONArray(1);
			for (int i = 0; i < jArrayPics.length(); i++)
			{
				String picLink = jArrayPics.getJSONObject(i).getString(
						"picture_link");
				newHouse.picArrayList.add(picLink);
			}

			return newHouse;
		} catch (Exception e)
		{
			return null;
		}
	}

	public static boolean getAroundRentsAndHouses(Boolean is_rent_show,
			Boolean is_sale_show, double km_dis, double center_x,
			double center_y, String rp_min, String rp_max, String hp_min,
			String hp_max, String area_min, String area_max, String age_min,
			String age_max, String rentTypeString, String saleTypeString)
	{
		int int_rent = 0;
		if (is_rent_show)
		{
			int_rent = 1;
		}

		int int_sale = 0;
		if (is_sale_show)
		{
			int_sale = 1;
		}

		String query_link = "/api/v2/house/get_houses_by_distance?"
				+ "is_show_rent=" + Integer.toString(int_rent)
				+ "&is_show_sale=" + Integer.toString(int_sale) + "&km_dis="
				+ km_dis + "&center_x=" + center_x + "&center_y=" + center_y;

		if (rp_min != null)
		{
			query_link = query_link + "&rp_min=" + rp_min;
		}

		if (rp_max != null)
		{
			query_link = query_link + "&rp_max=" + rp_max;
		}

		if (hp_min != null)
		{
			query_link = query_link + "&hp_min=" + hp_min;
		}

		if (hp_max != null)
		{
			query_link = query_link + "&hp_max=" + hp_max;
		}

		if (area_min != null)
		{
			query_link = query_link + "&area_min=" + area_min;
		}

		if (area_max != null)
		{
			query_link = query_link + "&area_max=" + area_max;
		}

		if (age_min != null)
		{
			query_link = query_link + "&age_min=" + age_min;
		}

		if (age_max != null)
		{
			query_link = query_link + "&age_max=" + age_max;
		}

		if (rentTypeString != null)
		{
			query_link = query_link + "&rent_type=" + rentTypeString;
		}

		if (saleTypeString != null)
		{
			query_link = query_link + "&ground_type=" + saleTypeString;
		}

		String message = getMessageFromServer("GET", query_link, null, null);

		if (message == null)
		{
			return false;
		} else
		{
			return parseRentHouseMessage(message);
		}
	}

	private static boolean parseRentHouseMessage(String message)
	{

		Log.i(TAG, "Start Parse Json:" + System.currentTimeMillis());

		try
		{
			JSONArray jArray;
			jArray = new JSONArray(message.toString());

			JSONArray jRentsArray = jArray.getJSONArray(0);

			for (int i = 0; i < jRentsArray.length(); i++)
			{

				int rent_id = 0;
				try
				{
					rent_id = jRentsArray.getJSONObject(i).getInt("id");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String title = "";
				try
				{
					title = jRentsArray.getJSONObject(i).getString("title");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String promote_pic = "";
				try
				{
					promote_pic = jRentsArray.getJSONObject(i).getString(
							"promote_pic_link");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int price = 0;
				try
				{
					price = jRentsArray.getJSONObject(i).getInt("price");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String address = "";
				try
				{
					address = jRentsArray.getJSONObject(i).getString("address");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double rent_area = 0;
				try
				{
					rent_area = jRentsArray.getJSONObject(i).getDouble(
							"rent_area");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int layer = 0;
				try
				{
					layer = jRentsArray.getJSONObject(i).getInt("layer");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int total_layer = 0;
				try
				{
					total_layer = jRentsArray.getJSONObject(i).getInt(
							"total_lyaers");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int rooms = 0;
				try
				{
					rooms = jRentsArray.getJSONObject(i).getInt("rooms");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int rest_rooms = 0;
				try
				{
					rest_rooms = jRentsArray.getJSONObject(i).getInt(
							"rest_rooms");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int rent_type_id = 0;
				try
				{
					rent_type_id = jRentsArray.getJSONObject(i).getInt(
							"rent_type_id");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double x_long = jRentsArray.getJSONObject(i)
						.getDouble("x_long");
				double y_lat = jRentsArray.getJSONObject(i).getDouble("y_lat");

				RentHouse newHouse = new RentHouse(rent_id, title, promote_pic,
						price, address, rent_area, layer, total_layer, rooms,
						rest_rooms, x_long, y_lat, rent_type_id);
				Datas.mRentHouses.add(newHouse);

			}

			JSONArray jHousesArray = jArray.getJSONArray(1);
			for (int i = 0; i < jHousesArray.length(); i++)
			{
				int house_id = 0;
				try
				{
					house_id = jHousesArray.getJSONObject(i).getInt("id");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String title = "";
				try
				{
					title = jHousesArray.getJSONObject(i).getString("title");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String promote_pic = "";
				try
				{
					promote_pic = jHousesArray.getJSONObject(i).getString(
							"promote_pic_link");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int price = 0;
				try
				{
					price = jHousesArray.getJSONObject(i).getInt("price");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String address = "";
				try
				{
					address = jHousesArray.getJSONObject(i)
							.getString("address");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double total_area = 0;
				try
				{
					total_area = jHousesArray.getJSONObject(i).getDouble(
							"total_area");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int layer = 0;
				try
				{
					layer = jHousesArray.getJSONObject(i).getInt("layer");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int total_layer = 0;
				try
				{
					total_layer = jHousesArray.getJSONObject(i).getInt(
							"total_lyaers");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int rooms = 0;
				try
				{
					rooms = jHousesArray.getJSONObject(i).getInt("rooms");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int rest_rooms = 0;
				try
				{
					rest_rooms = jHousesArray.getJSONObject(i).getInt(
							"rest_rooms");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int ground_type_id = 0;
				try
				{
					ground_type_id = jHousesArray.getJSONObject(i).getInt(
							"ground_type_id");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double x_long = jHousesArray.getJSONObject(i).getDouble(
						"x_long");
				double y_lat = jHousesArray.getJSONObject(i).getDouble("y_lat");

				House newHouse = new House(house_id, title, promote_pic, price,
						address, total_area, layer, total_layer, rooms,
						rest_rooms, x_long, y_lat, ground_type_id);
				Datas.mSaleHouses.add(newHouse);

			}

		} catch (Exception e1)
		{
			e1.printStackTrace();
			return false;
		}

		Log.i(TAG, "End   Parse Json:" + System.currentTimeMillis() + " array ");
		return true;

	}

	public static String getMessageFromServer(String requestMethod,
			String apiPath, JSONObject json, String apiUrl)
	{
		Log.i(TAG, "Start Load from server:" + System.currentTimeMillis());
		URL url;
		try
		{
			if (apiUrl != null)
				url = new URL(apiUrl);
			else
				url = new URL(HOST + apiPath);

			if (DEBUG)
				Log.d(TAG, "URL: " + url);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod(requestMethod);

			connection.setRequestProperty("Content-Type",
					"application/json;charset=utf-8");
			if (requestMethod.equalsIgnoreCase("POST"))
				connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();

			if (requestMethod.equalsIgnoreCase("POST"))
			{
				OutputStream outputStream;

				outputStream = connection.getOutputStream();
				if (DEBUG)
					Log.d("post message", json.toString());

				outputStream.write(json.toString().getBytes());
				outputStream.flush();
				outputStream.close();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			StringBuilder lines = new StringBuilder();
			;
			String tempStr;

			while ((tempStr = reader.readLine()) != null)
			{
				lines = lines.append(tempStr);
			}
			if (DEBUG)
				Log.d("MOVIE_API", lines.toString());

			reader.close();
			connection.disconnect();

			Log.i(TAG, "End Load from server:" + System.currentTimeMillis());

			return lines.toString();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
