package com.kosbrother.houseprice.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class GroundType
{
	static String groundTypeMessage = "[{\"id\":1,\"name\":\"\u4F4F\u5B85\"},{\"id\":2,\"name\":\"\u5957\u623F\"},{\"id\":3,\"name\":\"\u5E97\u9762\"},{\"id\":4,\"name\":\"\u6524\u4F4D\"},{\"id\":5,\"name\":\"\u8FA6\u516C\"},{\"id\":6,\"name\":\"\u4F4F\u8FA6\"},{\"id\":7,\"name\":\"\u5EE0\u623F\"},{\"id\":8,\"name\":\"\u8ECA\u4F4D\"},{\"id\":9,\"name\":\"\u571F\u5730\"},{\"id\":10,\"name\":\"\u5176\u4ED6\"}]";
	
	public int id;
	public String name;
	
	public GroundType(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public static ArrayList<GroundType> getGroundTypes() {
        ArrayList<GroundType> types = new ArrayList<GroundType>();
        JSONArray typeArray;
        try {
        	typeArray = new JSONArray(groundTypeMessage);
            for (int i = 0; i < typeArray.length(); i++) {
                int type_id = typeArray.getJSONObject(i).getInt("id");
                String name = typeArray.getJSONObject(i).getString("name");
                GroundType theType = new GroundType(type_id, name);
                types.add(theType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return types;
    }
	
	
}
