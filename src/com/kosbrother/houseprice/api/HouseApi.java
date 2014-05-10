package com.kosbrother.houseprice.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.kosbrother.houseprice.entity.County;
import com.kosbrother.houseprice.entity.LandMark;
import com.kosbrother.houseprice.entity.RentHouse;
import com.kosbrother.houseprice.entity.Town;

public class HouseApi
{
	final static String HOST = "http://106.186.31.71";
	public static final String TAG = "HOUSE_API";
	public static final boolean DEBUG = true;

	public static ArrayList<County> getCounties()
	{
		return County.getCounties();
	}

	public static ArrayList<Town> getCountyTowns(int county_id)
	{
		return Town.getTonwsByCounty(county_id);
	}

	public static ArrayList<RentHouse> getAroundRentsByAreas(double km_dis,
			double center_x, double center_y, String rp_min, String rp_max,
			String area_min, String area_max, String rentTypeString,
			String buildingTypeString)
	{

		String query_link = "/api/v1/house/get_rents_by_distance?" + "km_dis="
				+ km_dis + "&center_x=" + center_x + "&center_y=" + center_y;

		if (rp_min != null)
		{
			query_link = query_link + "&rp_min=" + rp_min;
		}

		if (rp_max != null)
		{
			query_link = query_link + "&rp_max=" + rp_max;
		}

		if (area_min != null)
		{
			query_link = query_link + "&a_min=" + area_min;
		}

		if (area_max != null)
		{
			query_link = query_link + "&a_max=" + area_max;
		}

		if (rentTypeString != null)
		{
			query_link = query_link + "&rent_type=" + rentTypeString;
		}

		if (buildingTypeString != null)
		{
			query_link = query_link + "&building_type=" + buildingTypeString;
		}

		String message = getMessageFromServer("GET", query_link, null, null);

		ArrayList<RentHouse> realEstates = new ArrayList<RentHouse>();
		if (message == null)
		{
			return null;
		} else
		{
			return parseRentHouseMessage(message, realEstates);
		}
	}

	public static RentHouse getHouseDetails(int estate_id)
	{
		String message = getMessageFromServer("GET",
				"/api/v1/house/get_rent_detail?house_id=" + estate_id, null,
				null);
		if (message == null)
		{
			return null;
		} else
		{
			return parseRentHouseDetailMessage(message);
		}
	}

	private static RentHouse parseRentHouseDetailMessage(String message)
	{
		try
		{
			JSONArray jArray;
			jArray = new JSONArray(message.toString());

			// for estate data
			int rent_id = jArray.getJSONObject(0).getInt("id");

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

			String deposit = "";
			try
			{
				deposit = jArray.getJSONObject(0).getString("deposit");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			double rent_area = 0;
			try
			{
				rent_area = jArray.getJSONObject(0).getDouble("rent_area");
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

			String min_rent_time = "";
			try
			{
				min_rent_time = jArray.getJSONObject(0).getString(
						"mint_rent_time");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			boolean is_cooking = false;
			try
			{
				is_cooking = jArray.getJSONObject(0).getBoolean("is_cooking");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			boolean is_pet = false;
			try
			{
				is_pet = jArray.getJSONObject(0).getBoolean("is_pet");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String identity = "";
			try
			{
				identity = jArray.getJSONObject(0).getString("identity");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String sexual_restriction = "";
			try
			{
				sexual_restriction = jArray.getJSONObject(0).getString(
						"sexual_restriction");
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

			String furniture = "";
			try
			{
				furniture = jArray.getJSONObject(0).getString("furniture");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String equipment = "";
			try
			{
				equipment = jArray.getJSONObject(0).getString("equipment");
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

			String communication = "";
			try
			{
				communication = jArray.getJSONObject(0).getString(
						"communication");
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

			int rent_type_id = 0;
			try
			{
				rent_type_id = jArray.getJSONObject(0).getInt("rent_type_id");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int building_type_id = 0;
			try
			{
				building_type_id = jArray.getJSONObject(0).getInt(
						"building_type_id");
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

			RentHouse newHouse = new RentHouse(rent_id, title, promote_pic,
					price, address, deposit, rent_area, layer, total_layer,
					rooms, living_rooms, rest_rooms, balconies, parkingString,
					x_long, y_lat, guard_price, min_rent_time, is_cooking,
					is_pet, identity, sexual_restriction, orientation,
					furniture, equipment, living_explanation, communication,
					feature_html, vender_name, phone_number, county_id,
					town_id, rent_type_id, building_type_id, is_show);

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

	private static ArrayList<RentHouse> parseRentHouseMessage(String message,
			ArrayList<RentHouse> rentHouses)
	{

		Log.i(TAG, "Start Parse Json:" + System.currentTimeMillis());

		try
		{
			JSONArray jArray;
			jArray = new JSONArray(message.toString());
			for (int i = 0; i < jArray.length(); i++)
			{

				int rent_id = 0;
				try
				{
					rent_id = jArray.getJSONObject(i).getInt("id");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String title = "";
				try
				{
					title = jArray.getJSONObject(i).getString("title");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String promote_pic = "";
				try
				{
					promote_pic = jArray.getJSONObject(i).getString(
							"promote_pic_link");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int price = 0;
				try
				{
					price = jArray.getJSONObject(i).getInt("price");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String address = "";
				try
				{
					address = jArray.getJSONObject(i).getString("address");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double rent_area = 0;
				try
				{
					rent_area = jArray.getJSONObject(i).getDouble("rent_area");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int layer = 0;
				try
				{
					layer = jArray.getJSONObject(i).getInt("layer");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int total_layer = 0;
				try
				{
					total_layer = jArray.getJSONObject(i)
							.getInt("total_lyaers");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int rooms = 0;
				try
				{
					rooms = jArray.getJSONObject(i).getInt("rooms");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int rest_rooms = 0;
				try
				{
					rest_rooms = jArray.getJSONObject(i).getInt("rest_rooms");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int rent_type_id = 0;
				try
				{
					rent_type_id = jArray.getJSONObject(i).getInt(
							"rent_type_id");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double x_long = jArray.getJSONObject(i).getDouble("x_long");
				double y_lat = jArray.getJSONObject(i).getDouble("y_lat");

				// public RentHouse(int rent_id, String title, String
				// promote_pic, int price,
				// String address, double rent_area, int layer, int total_layer,
				// int rooms, int rest_rooms, double x_long, double y_lat,
				// int rent_type_id)

				RentHouse newHouse = new RentHouse(rent_id, title, promote_pic,
						price, address, rent_area, layer, total_layer, rooms,
						rest_rooms, x_long, y_lat, rent_type_id);
				rentHouses.add(newHouse);

			}

		} catch (Exception e1)
		{
			e1.printStackTrace();
		}

		Log.i(TAG, "End   Parse Json:" + System.currentTimeMillis() + " array ");
		return rentHouses;

	}

	// HouseApi.getAroundAmenities("111", 25.05535, 121.4588);
	public static ArrayList<LandMark> getAroundAmenities(String unique_id,
			double x_lat, double y_lng)
	{
		String landMarks_url = "http://api.housebook.tw/api/landmarks?";
		landMarks_url = landMarks_url + "unique_id=" + unique_id + "&lat="
				+ Double.toString(x_lat) + "&lng=" + Double.toString(y_lng);
		String message = httpPOST(landMarks_url);
		ArrayList<LandMark> landMarks = new ArrayList<LandMark>();
		if (message == null)
		{
			return null;
		} else
		{
			return parseLandMarksMessage(message, landMarks);
		}

	}

	private static ArrayList<LandMark> parseLandMarksMessage(String message,
			ArrayList<LandMark> landMarks)
	{
		try
		{
			JSONArray jArray;
			JSONObject jObject = new JSONObject(message.toString());
			jArray = jObject.getJSONArray("ApiResult");
			// jArray = new JSONArray(message.toString());
			for (int i = 0; i < jArray.length(); i++)
			{
				int landMark_id = 0;
				try
				{
					landMark_id = jArray.getJSONObject(i).getInt("id");
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				int type_id = 0;
				try
				{
					 String type = jArray.getJSONObject(i).getString("type");
					 type_id = LandMark.parseType(type);
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				String title = "";
				try
				{
					title = jArray.getJSONObject(i).getString("title");
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				double y_lat = 0;
				try
				{
					y_lat = jArray.getJSONObject(i).getDouble("lat");
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				double x_lng = 0;
				try
				{
					x_lng = jArray.getJSONObject(i).getDouble("lng");
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				int dis_meter = 0;
				try
				{
					dis_meter = jArray.getJSONObject(i).getInt("distance");
				} catch (Exception e)
				{
					// TODO: handle exception
				}
				
				LandMark newLandMark = new LandMark(landMark_id, type_id,
						title, x_lng, y_lat, dis_meter);
				landMarks.add(newLandMark);
			}
			
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return landMarks;
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

	private static String httpPOST(String url)
	{
		HttpPost post = new HttpPost(url);
		try
		{
			// 送出HTTP request
			// post.setEntity(new UrlEncodedFormEntity(null, HTTP.UTF_8));
			// 取得HTTP response
			HttpResponse httpResponse = new DefaultHttpClient().execute(post);
			// 檢查狀態碼，200表示OK
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{
				// 取出回應字串
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				return strResult;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
