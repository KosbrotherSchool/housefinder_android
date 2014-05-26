package com.kosbrother.houseprice.fragment;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.kosbrother.housefinder.AmenitiesActivity;
import com.kosbrother.housefinder.AppConstants;
import com.kosbrother.housefinder.Datas;
import com.kosbrother.housefinder.DetailActivity;
import com.kosbrother.housefinder.R;
import com.kosbrother.housefinder.data.OrmHouse;
import com.kosbrother.houseprice.api.HouseApi;
import com.kosbrother.houseprice.api.InfoParserApi;
import com.kosbrother.houseprice.entity.RentHouse;
import com.kosbrother.imageloader.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;

public class RentDetailFragment extends Fragment
{
	int mNum;
	String mRentTypeKey;
	private ImageLoader imageLoader;
	private RentHouse theRentHouse;
	private static DetailActivity mActivity;

	/**
	 * Create a new instance of CountingFragment, providing "num" as an
	 * argument.
	 */
	public static RentDetailFragment newInstance(int num,
			DetailActivity theDetailActivity)
	{
		RentDetailFragment f = new RentDetailFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);
		mActivity = theDetailActivity;

		return f;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		imageLoader = new ImageLoader(getActivity(), 100);
		theRentHouse = Datas.mRentHouses.get(mNum);
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */

	private ScrollView myScrollView;
	private ImageView mapImageView;
	private ViewPager imageViewPager;
	private CirclePageIndicator mIndicator;
	
	private ImageView typeImageView;
	private TextView titleTextView;
	private TextView addressTextView;
	private TextView moneyTextView;

	private TextView typeTextView;
	private TextView layerTextView;

	private TextView arrangeTextView;
	private TextView areaTextView;

	private TextView depositTextView;
	private TextView parkingTextView;

	private TextView featureTextView;
	private TextView otherTextView;

	private TextView furnitureTextView;
	private TextView equipmenTextView;
	private TextView livingTextView;
	private TextView communicationTextView;

	private LinearLayout linearDirect;
	private LinearLayout linearAmenities;
	private LinearLayout linearStreetView;
	private LinearLayout linearContact;
	private LinearLayout linearAddFavorite;
	private TextView detailAddFavoriteTextView;

	private ImageView contactImageView;
	private TextView contactNameTextView;
	private TextView contactPhoneTextView;
	private ImageButton contactCallButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_detail_pager, container,
				false);

		myScrollView = (ScrollView) v.findViewById(R.id.detail_scroll_view);
		mapImageView = (ImageView) v.findViewById(R.id.detail_map_image);

		imageViewPager = (ViewPager) v
				.findViewById(R.id.detail_image_view_pager);
		mIndicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
		final float density = getResources().getDisplayMetrics().density;
		// mIndicator.setBackgroundColor(0xFFCCCCCC);
		mIndicator.setRadius(4 * density);
		mIndicator.setPageColor(getActivity().getResources().getColor(
				R.color.circle_gray));
		mIndicator.setFillColor(getActivity().getResources().getColor(
				R.color.light_black));
		mIndicator.setStrokeColor(getActivity().getResources().getColor(
				R.color.white));
		mIndicator.setStrokeWidth(1 * density);
		
		typeImageView = (ImageView) v.findViewById(R.id.detail_type_image);
		typeImageView.setImageResource(R.drawable.marker_rent);
		
		titleTextView = (TextView) v.findViewById(R.id.detail_text_title);
		addressTextView = (TextView) v.findViewById(R.id.detail_text_address);
		moneyTextView = (TextView) v.findViewById(R.id.detail_money_text);
		typeTextView = (TextView) v.findViewById(R.id.detail_type_text);
		layerTextView = (TextView) v.findViewById(R.id.detail_layers_text);

		arrangeTextView = (TextView) v.findViewById(R.id.detail_arrange_text);
		areaTextView = (TextView) v.findViewById(R.id.detail_area_text);
		depositTextView = (TextView) v.findViewById(R.id.detail_deposit_text);
		parkingTextView = (TextView) v.findViewById(R.id.detail_parking_text);
		featureTextView = (TextView) v.findViewById(R.id.detail_feature_text);
		otherTextView = (TextView) v.findViewById(R.id.detail_other_text);
		furnitureTextView = (TextView) v
				.findViewById(R.id.detail_furniture_text);
		equipmenTextView = (TextView) v
				.findViewById(R.id.detail_equipment_text);
		livingTextView = (TextView) v.findViewById(R.id.detail_living_text);
		communicationTextView = (TextView) v
				.findViewById(R.id.detail_cummunication_text);

		linearDirect = (LinearLayout) v
				.findViewById(R.id.linear_location_direct);
		linearAmenities = (LinearLayout) v
				.findViewById(R.id.linear_location_amenities);
		linearStreetView = (LinearLayout) v
				.findViewById(R.id.linear_location_streetview);
		linearContact = (LinearLayout) v.findViewById(R.id.linear_contact);
		linearAddFavorite = (LinearLayout) v
				.findViewById(R.id.linear_add_favorite);

		contactImageView = (ImageView) v.findViewById(R.id.contact_image);
		contactNameTextView = (TextView) v.findViewById(R.id.contact_name);
		contactPhoneTextView = (TextView) v.findViewById(R.id.contact_phone);
		contactCallButton = (ImageButton) v.findViewById(R.id.call_imagebutton);

		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;

		// set image
		String x_long = Double.toString(theRentHouse.x_long);
		String y_lat = Double.toString(theRentHouse.y_lat);
		String url = "http://maps.google.com/maps/api/staticmap?center="
				+ y_lat + "," + x_long
				+ "&zoom=17&markers=color:red%7Clabel:%7C" + y_lat + ","
				+ x_long + "&size=" + Integer.toString(width / 2) + "x"
				+ Integer.toString(height / 2 / 6)
				+ "&language=zh-TW&scale=2&sensor=false";

		// mapImageView.setAdjustViewBounds(true);
		imageLoader.DisplayImage(url, mapImageView, width);

		detailAddFavoriteTextView = (TextView) v
				.findViewById(R.id.detail_add_favorite_text);
		if (getIsAddFavorite(theRentHouse.rent_id))
		{
			detailAddFavoriteTextView.setText("從最愛刪除");
		} else
		{
			detailAddFavoriteTextView.setText("加入最愛");
		}

		new GetEstatesTask().execute();

		return v;
	}

	private class GetEstatesTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{

		}

		@Override
		protected Void doInBackground(Void... arg)
		{

			RentHouse newRentHouse = HouseApi
					.getHouseDetails(theRentHouse.rent_id);
			if (newRentHouse != null)
			{
				theRentHouse = newRentHouse;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void arg)
		{
			super.onPostExecute(null);

			titleTextView.setText(theRentHouse.title);
			addressTextView.setText(theRentHouse.address);

			String moneyString = "<font size=\"3\" color=\"red\">"
					+ Integer.toString(theRentHouse.price) + "元/月" + "</font>"
					+ ",&nbsp;" + "<font size=\"3\" color=\"black\">" + "押金"
					+ theRentHouse.deposit + "</font>";
			moneyTextView.setText(Html.fromHtml(moneyString));

			String buildingString = "~";
			if (theRentHouse.building_type_id != 0)
			{
				buildingString = InfoParserApi
						.parseBuildingType(theRentHouse.building_type_id);
			}
			typeTextView.setText("型態: " + buildingString);

			depositTextView.setText(InfoParserApi
					.parseRentType(theRentHouse.rent_type_id));

			String layerString = InfoParserApi.parseLayers(theRentHouse.layer,
					theRentHouse.total_layer);
			if (!layerString.equals(""))
			{
				layerTextView.setText("樓層: " + layerString);
			} else
			{
				layerTextView.setText("樓層: " + "~");
			}

			String arrangeString = InfoParserApi.parseRoomArrangement(
					theRentHouse.rooms, theRentHouse.living_rooms,
					theRentHouse.rest_rooms, theRentHouse.balconies);

			if (!arrangeString.equals(""))
			{
				arrangeTextView.setText(arrangeString);
			} else
			{
				arrangeTextView.setText("~");
			}

			areaTextView.setText("坪數 "
					+ Double.toString(theRentHouse.rent_area) + "坪");

			parkingTextView.setText("車位: " + theRentHouse.parkingString);

			featureTextView.setText(Html.fromHtml(theRentHouse.feature_html));

			String otherString = "";
			if (theRentHouse.guard_price != 0)
			{
				otherString = "*管理費: "
						+ Integer.toString(theRentHouse.guard_price) + "\n";
			}
			if (!theRentHouse.min_rent_time.equals("")
					&& !theRentHouse.min_rent_time.equals("null"))
			{
				otherString = otherString + "*最短租期: "
						+ theRentHouse.min_rent_time + "\n";
			}
			if (theRentHouse.is_cooking)
			{
				otherString = otherString + "*可開伙: 可以" + "\n";
			}
			if (theRentHouse.is_pet)
			{
				otherString = otherString + "*可養寵物: 可以" + "\n";
			}
			if (!theRentHouse.identity.equals("")
					&& !theRentHouse.identity.equals("null"))
			{
				otherString = otherString + "*身份限制:" + theRentHouse.identity
						+ "\n";
			}
			if (!theRentHouse.sexual_restriction.equals("")
					&& !theRentHouse.sexual_restriction.equals("null"))
			{
				otherString = otherString + "*性別要求:"
						+ theRentHouse.sexual_restriction + "\n";
			}
			if (!theRentHouse.orientation.equals("")
					&& !theRentHouse.sexual_restriction.equals("null"))
			{
				otherString = otherString + "*房子座向:" + theRentHouse.orientation
						+ "\n";
			}
			otherTextView.setText(otherString);

			if (!theRentHouse.furniture.equals("")
					&& !theRentHouse.furniture.equals("null"))
			{
				furnitureTextView.setText("提供傢俱: " + theRentHouse.furniture);
			} else
			{
				furnitureTextView.setVisibility(View.GONE);
			}

			if (!theRentHouse.equipment.equals("")
					&& !theRentHouse.equipment.equals("null"))
			{
				equipmenTextView.setText("提供設備: " + theRentHouse.equipment);
			} else
			{
				equipmenTextView.setVisibility(View.GONE);
			}

			if (!theRentHouse.living_explanation.equals("")
					&& !theRentHouse.living_explanation.equals("null"))
			{
				livingTextView.setText("生活機能: "
						+ theRentHouse.living_explanation);
			} else
			{
				livingTextView.setVisibility(View.GONE);
			}

			if (!theRentHouse.communication.equals("")
					&& !theRentHouse.communication.equals("null"))
			{
				communicationTextView.setText("附近交通: "
						+ theRentHouse.communication);
			} else
			{
				communicationTextView.setVisibility(View.GONE);
			}

			ImageAdapter adapter = new ImageAdapter();
			imageViewPager.setAdapter(adapter);

			mIndicator.setViewPager(imageViewPager);
			// ((CirclePageIndicator) mIndicator).setSnap(true);

			linearDirect.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri
									.parse("http://maps.google.com/maps?daddr="
											+ theRentHouse.y_lat + ","
											+ theRentHouse.x_long));
					startActivity(intent);

				}
			});

			linearAmenities.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(getActivity(),
							AmenitiesActivity.class);
					Bundle bundle = new Bundle();
					bundle.putDouble("house_x", theRentHouse.x_long);
					bundle.putDouble("house_y", theRentHouse.y_lat);
					bundle.putInt("rent_type", theRentHouse.rent_type_id);
					bundle.putInt("price", theRentHouse.price);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});

			linearStreetView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Intent streetView = new Intent(
							android.content.Intent.ACTION_VIEW, Uri
									.parse("google.streetview:cbll="
											+ theRentHouse.y_lat + ","
											+ theRentHouse.x_long
											+ "&cbp=1,99.56,,1,-5.27&mz=21"));
					startActivity(streetView);
				}
			});

			contactNameTextView.setText("聯絡人: " + theRentHouse.vender_name);
			contactPhoneTextView
					.setText("Phone: "
							+ InfoParserApi
									.parsePhoneNumber(theRentHouse.phone_number));

			if (theRentHouse.vender_name.indexOf("小姐") != -1)
			{
				contactImageView.setImageResource(R.drawable.contact_woman);
			}

			contactCallButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{

					if (AppConstants.isTablet(getActivity()))
					{
						Toast.makeText(getActivity(), "此裝置無電話",
								Toast.LENGTH_SHORT).show();
					} else
					{

						if (theRentHouse.phone_number != null
								&& !theRentHouse.phone_number.equals("")
								&& !theRentHouse.phone_number.equals("null"))
						{
							EasyTracker easyTracker = EasyTracker
									.getInstance(getActivity());
							easyTracker.send(MapBuilder.createEvent("Button",
									"button_press", "call_button", null)
									.build());
							// Intent intent = new Intent(Intent.ACTION_CALL,
							// Uri
							// .parse("tel:" + theRentHouse.phone_number));
							Intent intent = new Intent(Intent.ACTION_DIAL, Uri
									.parse("tel:" + theRentHouse.phone_number));
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						} else
						{
							Toast.makeText(getActivity(), "無電話",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			});

			mapImageView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// double latitude = theRentHouse.y_lat;
					// double longitude = theRentHouse.x_long;
					// String label = theRentHouse.title;
					// String uriBegin = "geo:" + latitude + "," + longitude;
					// String query = latitude + "," + longitude + "(" + label
					// + ")";
					// String encodedQuery = Uri.encode(query);
					// String uriString = uriBegin + "?q=" + encodedQuery
					// + "&z=16";
					// Uri uri = Uri.parse(uriString);
					// Intent intent = new Intent(
					// android.content.Intent.ACTION_VIEW, uri);
					// startActivity(intent);
					Intent intent = new Intent(getActivity(),
							AmenitiesActivity.class);
					Bundle bundle = new Bundle();
					bundle.putDouble("house_x", theRentHouse.x_long);
					bundle.putDouble("house_y", theRentHouse.y_lat);
					bundle.putInt("rent_type", theRentHouse.rent_type_id);
					bundle.putInt("price", theRentHouse.price);
					intent.putExtras(bundle);
					startActivity(intent);

				}
			});

			linearContact.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					myScrollView.fullScroll(View.FOCUS_DOWN);
				}
			});

			linearAddFavorite.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{

					try
					{
						Dao<OrmHouse, Integer> rentHouseDao = mActivity
								.getHelper().getOrmHouseDao();

						if (!getIsAddFavorite(theRentHouse.rent_id))
						{

							OrmHouse newRentHouse = new OrmHouse();
							newRentHouse.house_id = theRentHouse.rent_id;
							newRentHouse.title = theRentHouse.title;
							newRentHouse.promote_pic = theRentHouse.promote_pic;
							newRentHouse.price = theRentHouse.price;
							newRentHouse.address = theRentHouse.address;
							newRentHouse.area = theRentHouse.rent_area;
							newRentHouse.layer = theRentHouse.layer;
							newRentHouse.total_layer = theRentHouse.total_layer;
							newRentHouse.rooms = theRentHouse.rooms;
							newRentHouse.rest_rooms = theRentHouse.rest_rooms;
							newRentHouse.x_long = theRentHouse.x_long;
							newRentHouse.y_lat = theRentHouse.y_lat;
							newRentHouse.rent_type_id = theRentHouse.rent_type_id;
							newRentHouse.house_type_id = AppConstants.TYPE_ID_RENT;
							rentHouseDao.create(newRentHouse);
							Toast.makeText(getActivity(), "已加入最愛!",
									Toast.LENGTH_SHORT).show();

							detailAddFavoriteTextView.setText("已加入最愛");

						} else
						{

							DeleteBuilder<OrmHouse, Integer> deleteBuilder = rentHouseDao
									.deleteBuilder();
							deleteBuilder.where().eq(
									OrmHouse.Column_House_ID_NAME,
									theRentHouse.rent_id);
							deleteBuilder.delete();
							Toast.makeText(mActivity, "從我的最愛移除!",
									Toast.LENGTH_SHORT).show();
							detailAddFavoriteTextView.setText("加入最愛");
						}

					} catch (Exception e)
					{
						Toast.makeText(mActivity, "執行失敗!", Toast.LENGTH_SHORT)
								.show();
					}

				}

			});

		}
	}

	private boolean getIsAddFavorite(int rent_id)
	{
		Boolean isBoolean = false;
		try
		{
			Dao<OrmHouse, Integer> rentHouseDao = mActivity.getHelper()
					.getOrmHouseDao();
			QueryBuilder<OrmHouse, Integer> queryBuilder = rentHouseDao
					.queryBuilder();
			queryBuilder.where().eq(OrmHouse.Column_House_ID_NAME, rent_id);
			List<OrmHouse> list = queryBuilder.query();
			if (list.size() > 0)
			{
				isBoolean = true;
			} else
			{
				isBoolean = false;
			}
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isBoolean;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

	class ImageAdapter extends PagerAdapter
	{

		@Override
		public int getCount()
		{
			if (theRentHouse.picArrayList.size() != 0)
			{
				return theRentHouse.picArrayList.size();
			} else
			{
				return 1;
			}
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			ImageView imageView = new ImageView(getActivity());
			if (theRentHouse.picArrayList.size() != 0)
			{
				imageLoader.DisplayImage(
						theRentHouse.picArrayList.get(position), imageView);
				// imageView.setImageResource(IMAGES[position]);

			} else
			{
				imageView.setImageResource(R.drawable.icon_no_pics);
			}
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			((ViewPager) container).removeView((ImageView) object);
		}
	}

}
