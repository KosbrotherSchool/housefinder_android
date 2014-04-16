package com.kosbrother.houseprice.api;

public class InfoParserApi
{
	public static String parseBuildingType(int buildingTypeId)
	{
		String buildType = "";
		try
		{
			switch (buildingTypeId)
			{
			case 1:
				buildType = "公寓";
				break;
			case 2:
				buildType = "透天厝";
				break;
			case 3:
				buildType = "店面";
				break;
			case 4:
				buildType = "商辦";
				break;
			case 5:
				buildType = "住宅";
				break;
			case 6:
				buildType = "華廈";
				break;
			case 7:
				buildType = "套房";
				break;
			case 8:
				buildType = "工廠";
				break;
			case 9:
				buildType = "廠辦";
				break;
			case 10:
				buildType = "農舍";
				break;
			case 11:
				buildType = "倉庫";
				break;
			case 12:
				buildType = "其他";
				break;
			default:
				break;
			}
		} catch (Exception e)
		{

		}
		return buildType;
	}

	public static String parseGroundType(int groundTypeId)
	{
		String buildType = "";
		try
		{
			switch (groundTypeId)
			{
			case 1:
				buildType = "房地";
				break;
			case 2:
				buildType = "房地+車";
				break;
			case 3:
				buildType = "土地";
				break;
			case 4:
				buildType = "建物";
				break;
			case 5:
				buildType = "車位";
				break;
			default:
				break;
			}
		} catch (Exception e)
		{

		}
		return buildType;
	}

	public static String parseRoomArrangement(int rooms, int living_rooms,
			int rest_rooms, int balconies)
	{
		String arrangeString = "";
		if (rooms != 0)
		{
			arrangeString = Integer.toString(rooms) + "房";
		}
		if (living_rooms != 0)
		{
			arrangeString = arrangeString + Integer.toString(living_rooms) + "廳";
		}
		if (rest_rooms != 0)
		{
			arrangeString = arrangeString + Integer.toString(rest_rooms) + "衛";
		}
		if (balconies != 0)
		{
			arrangeString = arrangeString + Integer.toString(balconies) + "陽台";
		}
		
		return arrangeString;
	}
	
	public static String parseLayers(int layer, int total_layer)
	{
		String layerString = "";
		if (total_layer != 0)
		{
			if (layer != 0)
			{
				layerString = Integer.toString(layer) + "/";
			}else {
				layerString = "整棟/";
			}
			
			layerString = layerString + Integer.toString(total_layer) + "樓";
		}
		
		return layerString;
	}

	public static String parseRentType(int rentTypeId)
	{
		String rentType = "";
		try
		{
			switch (rentTypeId)
			{
			case 1:
				rentType = "整層住家";
				break;
			case 2:
				rentType = "獨立套房";
				break;
			case 3:
				rentType = "分租套房";
				break;
			case 4:
				rentType = "雅房";
				break;
			case 5:
				rentType = "店面";
				break;
			case 6:
				rentType = "攤位";
				break;
			case 7:
				rentType = "辦公";
				break;
			case 8:
				rentType = "住辦";
				break;
			case 9:
				rentType = "廠房";
				break;
			case 10:
				rentType = "車位";
				break;
			case 11:
				rentType = "土地";
				break;
			case 12:
				rentType = "場地";
				break;
			default:
				break;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return rentType;
	}

	public static String parseRentArea(Double rentArea)
	{
		String rentAreaString = "";
		rentAreaString = Double.toString(rentArea);
		String first_part = rentAreaString.substring(0,
				rentAreaString.indexOf("."));
		String second_part = rentAreaString.substring(
				rentAreaString.indexOf(".") + 1, rentAreaString.length());
		int second_part_int = Integer.valueOf(second_part);
		if (second_part_int == 0)
		{
			rentAreaString = first_part;
		}

		return rentAreaString;
	}

}
