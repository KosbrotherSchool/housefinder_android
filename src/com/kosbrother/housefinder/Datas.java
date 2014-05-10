package com.kosbrother.housefinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

import com.kosbrother.houseprice.entity.RentHouse;

public class Datas
{

	public static ArrayList<RentHouse> mRentHouses = new ArrayList<RentHouse>();
	public static TreeMap<String, ArrayList<RentHouse>> mRentHouseMap;
	public static ArrayList<String> mRentTypeArrayKey = new ArrayList<String>();


	public static String getKeyByPosition(int position)
	{
		int num = (mRentTypeArrayKey.size() - 1) - position;
		return mRentTypeArrayKey.get(num);
	};

	public static class RentPriceComparator implements Comparator<RentHouse>
	{

		private int myOrder;

		public RentPriceComparator(int order)
		{
			myOrder = order;
		}

		@Override
		public int compare(RentHouse es1, RentHouse es2)
		{
			if (myOrder == 0)
			{
				return Double.compare(es1.price, es2.price);
			} else
			{
				return -Double.compare(es1.price, es2.price);
			}
		}

	}
	
	public static class RentAreaComparator implements Comparator<RentHouse>
	{

		private int myOrder;

		public RentAreaComparator(int order)
		{
			myOrder = order;
		}

		@Override
		public int compare(RentHouse es1, RentHouse es2)
		{
			if (myOrder == 0)
			{
				return Double.compare(es1.rent_area, es2.rent_area);
			} else
			{
				return -Double.compare(es1.rent_area, es2.rent_area);
			}
		}

	}

}
