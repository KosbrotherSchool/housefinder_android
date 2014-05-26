package com.kosbrother.houseprice.adapter;

import java.sql.SQLException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.kosbrother.housefinder.AppConstants;
import com.kosbrother.housefinder.FavoriteActivity;
import com.kosbrother.housefinder.FavoriteDetailActivity;
import com.kosbrother.housefinder.R;
import com.kosbrother.housefinder.data.OrmHouse;
import com.kosbrother.houseprice.api.InfoParserApi;
import com.kosbrother.imageloader.ImageLoader;

public class ListOrmHouseAdapter extends BaseAdapter
{
	private final FavoriteActivity activity;
	private final ArrayList<OrmHouse> data;
	private static LayoutInflater inflater = null;
	private ImageLoader imageLoader;

	// private String mChannelTitle;

	public ListOrmHouseAdapter(FavoriteActivity a, ArrayList<OrmHouse> d)
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
		ImageView typeImageView = (ImageView) vi
				.findViewById(R.id.rent_type_image);

		imageLoader.DisplayImage(data.get(position).promote_pic, imageView);
		textAddress.setText(data.get(position).address);
		textTitle.setText(data.get(position).title);

		String typeString = "";

		if (data.get(position).house_type_id == AppConstants.TYPE_ID_SALE)
		{
			typeImageView.setImageResource(R.drawable.marker_sale);
			String moneyString = "<font size=\"3\" color=\"red\">"
					+ Integer.toString(data.get(position).price) + "萬"
					+ "</font>" + ",&nbsp;"
					+ "<font size=\"3\" color=\"black\">"
					+ InfoParserApi.parseRentArea(data.get(position).area)
					+ "坪" + "</font>";
			textMoney.setText(Html.fromHtml(moneyString));
			typeString = "<font size=\"3\" color=\"black\">"
					+ InfoParserApi
							.parseGroundType(data.get(position).ground_type_id);
		} else if (data.get(position).house_type_id == AppConstants.TYPE_ID_RENT)
		{
			typeImageView.setImageResource(R.drawable.marker_rent);
			String moneyString = "<font size=\"3\" color=\"red\">"
					+ Integer.toString(data.get(position).price) + "元/月"
					+ "</font>" + ",&nbsp;"
					+ "<font size=\"3\" color=\"black\">"
					+ InfoParserApi.parseRentArea(data.get(position).area)
					+ "坪" + "</font>";
			textMoney.setText(Html.fromHtml(moneyString));
			typeString = "<font size=\"3\" color=\"black\">"
					+ InfoParserApi
							.parseRentType(data.get(position).rent_type_id);
		}

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

				Intent intent = new Intent(activity,
						FavoriteDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("ItemPosition", position);
				intent.putExtras(bundle);
				activity.startActivity(intent);

			}
		});

		vi.setOnLongClickListener(new OnLongClickListener()
		{

			@Override
			public boolean onLongClick(View v)
			{

				final String[] ListStr = new String[2];
				ListStr[0] = "查看詳細資料";
				ListStr[1] = "刪除";

				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("查看或刪除");
				builder.setItems(ListStr, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{

						if (item == 0)
						{
							Intent intent = new Intent(activity,
									FavoriteDetailActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("ItemPosition", position);
							intent.putExtras(bundle);
							activity.startActivity(intent);
						} else
						{

							Dao<OrmHouse, Integer> rentHouseDao;
							try
							{
								rentHouseDao = activity.getHelper()
										.getOrmHouseDao();
								DeleteBuilder<OrmHouse, Integer> deleteBuilder = rentHouseDao
										.deleteBuilder();
								deleteBuilder.where().eq(
										OrmHouse.Column_House_ID_NAME,
										data.get(position).house_id);
								deleteBuilder.delete();
								Toast.makeText(activity, "從我的最愛移除!",
										Toast.LENGTH_SHORT).show();
								activity.refreshList();
							} catch (SQLException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				});
				AlertDialog alert = builder.create();
				alert.show();

				return false;
			}
		});

		return vi;
	}
}
