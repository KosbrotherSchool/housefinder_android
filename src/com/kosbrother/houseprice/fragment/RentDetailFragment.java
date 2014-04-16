package com.kosbrother.houseprice.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosbrother.housefinder.Datas;
import com.kosbrother.housefinder.DetailActivity;
import com.kosbrother.housefinder.R;
import com.kosbrother.houseprice.api.HouseApi;
import com.kosbrother.houseprice.api.InfoParserApi;
import com.kosbrother.houseprice.entity.RentHouse;
import com.kosbrother.imageloader.ImageLoader;

public class RentDetailFragment extends Fragment
{
	int mNum;
	String mRentTypeKey;
	private ImageLoader imageLoader;
	private RentHouse theRentHouse;
	private int text_size = 13;

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

	private ImageView image;

	// trade detail
	private TextView text_address;
	private TextView text_date;
	private TextView text_estate_type;
	private TextView text_content_buy;
	private TextView text_ground_exchange_area;
	private TextView text_building_rooms;
	private TextView text_buy_per_square_feet;
	private TextView text_buy_total_price;
	private TextView text_buiding_type;
	private LinearLayout buildingRoomsLayout;
	private LinearLayout detailEstateContentLayout;

	// ground detail
	private LinearLayout groundDetailLayout;

	// building detail
	private LinearLayout buildingDetailLayout;

	// parking detail
	private LinearLayout parkingDetailLayout;

	private ProgressDialog mProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_detail_pager, container,
				false);

		image = (ImageView) v.findViewById(R.id.imageview_detail);

		// trade detail
		text_address = (TextView) v.findViewById(R.id.text_detail_address);
		text_date = (TextView) v.findViewById(R.id.text_detail_date);
		text_estate_type = (TextView) v
				.findViewById(R.id.text_detail_estate_type);
		text_content_buy = (TextView) v
				.findViewById(R.id.text_detail_content_buy);
		text_ground_exchange_area = (TextView) v
				.findViewById(R.id.text_detail_ground_exchange_area);
		text_building_rooms = (TextView) v
				.findViewById(R.id.text_detail_building_rooms);
		text_buy_per_square_feet = (TextView) v
				.findViewById(R.id.text_detail_buy_per_square_feet);
		text_buy_total_price = (TextView) v
				.findViewById(R.id.text_detail_buy_total_price);
		text_buiding_type = (TextView) v
				.findViewById(R.id.text_detail_building_type);
		buildingRoomsLayout = (LinearLayout) v
				.findViewById(R.id.rooms_linear_layout);
		detailEstateContentLayout = (LinearLayout) v
				.findViewById(R.id.detail_estate_content_layout);

		groundDetailLayout = (LinearLayout) v.findViewById(R.id.layout_ground);
		buildingDetailLayout = (LinearLayout) v
				.findViewById(R.id.layout_building);
		parkingDetailLayout = (LinearLayout) v
				.findViewById(R.id.layout_parking);

		// set image
		String x_long = Double.toString(theRentHouse.x_long);
		String y_lat = Double.toString(theRentHouse.y_lat);
		String url = "http://maps.google.com/maps/api/staticmap?center="
				+ y_lat + "," + x_long
				+ "&zoom=17&markers=color:red%7Clabel:%7C" + y_lat + ","
				+ x_long + "&size=400x150&language=zh-TW&sensor=false";
		imageLoader.DisplayImage(url, image);
		
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
			

			return null;
		}

		@Override
		protected void onPostExecute(Void arg)
		{
			super.onPostExecute(null);


		}
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

}
