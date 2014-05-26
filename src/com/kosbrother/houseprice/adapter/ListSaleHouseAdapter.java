package com.kosbrother.houseprice.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosbrother.housefinder.Datas;
import com.kosbrother.housefinder.DetailActivity;
import com.kosbrother.housefinder.R;
import com.kosbrother.houseprice.api.InfoParserApi;
import com.kosbrother.houseprice.entity.House;
import com.kosbrother.imageloader.ImageLoader;

public class ListSaleHouseAdapter extends BaseAdapter
{

	private final Activity activity;
	private final ArrayList<House> data;
	private static LayoutInflater inflater = null;
	private ImageLoader imageLoader;

	// private String mChannelTitle;

	public ListSaleHouseAdapter(Activity a, ArrayList<House> d)
	{
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity, 100);
	}

	public int getCount()
	{
		return data.size();
	}

	public Object getItem(int position)
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.item_rent_list, null);

		TextView textTitle = (TextView) vi.findViewById(R.id.rent_list_title);
		TextView textAddress = (TextView) vi
				.findViewById(R.id.rent_list_address_text);
		TextView textMoney = (TextView) vi
				.findViewById(R.id.rent_list_money_text);

		TextView textRentType = (TextView) vi
				.findViewById(R.id.rent_list_type_text);

		ImageView imageView = (ImageView) vi.findViewById(R.id.rent_list_image);
		ImageView typeImageView = (ImageView)  vi.findViewById(R.id.rent_type_image);
		
		imageLoader.DisplayImage(data.get(position).promote_pic, imageView);
		typeImageView.setImageResource(R.drawable.marker_sale);
		
		textAddress.setText(data.get(position).address);
		textTitle.setText(data.get(position).title);

		String moneyString = "<font size=\"3\" color=\"red\">"
				+ Integer.toString(data.get(position).price) + "萬" + "</font>"
				+ ",&nbsp;" + "<font size=\"3\" color=\"black\">"
				+ InfoParserApi.parseRentArea(data.get(position).total_area)
				+ "坪" + "</font>";
		textMoney.setText(Html.fromHtml(moneyString));

		String typeString = "<font size=\"3\" color=\"black\">"
				+ InfoParserApi
						.parseGroundType(data.get(position).ground_type_id);

		if (InfoParserApi.parseRoomArrangement(data.get(position).rooms, 0,
				data.get(position).rest_rooms, 0) != "")
		{
			typeString = typeString
					+ ",&nbsp;"
					+ InfoParserApi.parseRoomArrangement(
							data.get(position).rooms, 0,
							data.get(position).rest_rooms, 0);
		}

		if (InfoParserApi.parseLayers(data.get(position).layer,
				data.get(position).total_layer) != "")
		{
			typeString = typeString
					+ ",&nbsp;"
					+ InfoParserApi.parseLayers(data.get(position).layer,
							data.get(position).total_layer);
		}

		typeString = typeString + "</font>";

		textRentType.setText(Html.fromHtml(typeString));

		vi.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				Intent intent = new Intent(activity, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("type_num", "sale_"+Integer.toString(position));
				intent.putExtras(bundle);
				activity.startActivity(intent);

			}
		});

		return vi;
	}
}
