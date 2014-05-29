package com.kosbrother.housefinder;

import java.util.ArrayList;
import java.util.Comparator;

import com.kosbrother.houseprice.entity.House;
import com.kosbrother.houseprice.entity.RentHouse;

public class Datas
{

	public static ArrayList<RentHouse> mRentHouses = new ArrayList<RentHouse>();
	public static ArrayList<House> mSaleHouses = new ArrayList<House>();
	

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
	
	public static class SaleTotalPriceComparator implements Comparator<House>
	{

		private int myOrder;

		public SaleTotalPriceComparator(int order)
		{
			myOrder = order;
		}

		@Override
		public int compare(House es1, House es2)
		{
			if (myOrder == 0)
			{
				return Integer.compare(es1.price, es2.price);
			} else
			{
				return -Integer.compare(es1.price, es2.price);
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
	
	public static class SaleAreaComparator implements Comparator<House>
	{

		private int myOrder;

		public SaleAreaComparator(int order)
		{
			myOrder = order;
		}

		@Override
		public int compare(House es1, House es2)
		{
			if (myOrder == 0)
			{
				return Double.compare(es1.total_area, es2.total_area);
			} else
			{
				return -Double.compare(es1.total_area, es2.total_area);
			}
		}

	}

}
