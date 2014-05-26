package com.kosbrother.houseprice.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class RentType
{
	private final static String messageString = "[{\"id\":1,\"name\":\"\u6574\u5C64\u4F4F\u5BB6\"},{\"id\":2,\"name\":\"\u7368\u7ACB\u5957\u623F\"},{\"id\":3,\"name\":\"\u5206\u79DF\u5957\u623F\"},{\"id\":4,\"name\":\"\u96C5\u623F\"},{\"id\":5,\"name\":\"\u5E97\u9762\"},{\"id\":6,\"name\":\"\u6524\u4F4D\"},{\"id\":7,\"name\":\"\u8FA6\u516C\"},{\"id\":8,\"name\":\"\u4F4F\u8FA6\"},{\"id\":9,\"name\":\"\u5EE0\u623F\"},{\"id\":10,\"name\":\"\u8ECA\u4F4D\"},{\"id\":11,\"name\":\"\u571F\u5730\"},{\"id\":12,\"name\":\"\u5834\u5730\"}]";
	
	public int id;
	public String name;
	
	public RentType(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public static ArrayList<RentType> getRentTypes() {
        ArrayList<RentType> types = new ArrayList<RentType>();
        JSONArray typeArray;
        try {
        	typeArray = new JSONArray(messageString);
            for (int i = 0; i < typeArray.length(); i++) {
                int type_id = typeArray.getJSONObject(i).getInt("id");
                String name = typeArray.getJSONObject(i).getString("name");
                RentType theType = new RentType(type_id, name);
                types.add(theType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return types;
    }
	
}
