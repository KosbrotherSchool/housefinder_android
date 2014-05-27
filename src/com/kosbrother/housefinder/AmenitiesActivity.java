package com.kosbrother.housefinder;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosbrother.housefinder.MainActivity.ErrorDialogFragment;
import com.kosbrother.housefinder.tool.LocationUtils;
import com.kosbrother.housefinder.tool.NetworkUtil;
import com.kosbrother.houseprice.api.HouseApi;
import com.kosbrother.houseprice.api.InfoParserApi;
import com.kosbrother.houseprice.entity.LandMark;
import com.kosbrother.houseprice.fragment.TransparentSupportMapFragment;

@SuppressLint("NewApi")
public class AmenitiesActivity extends FragmentActivity implements
		LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener
{

	private LocationClient mLocationClient;
	private GoogleMap mGoogleMap;

	private double house_x;
	private double house_y;
	private int rent_type;
	private int house_type;
	private int sale_type;
	private int rent_price;
	private int sale_price;
	private LatLng houseLatLng;
	private ArrayList<LandMark> mLandMarks = new ArrayList<LandMark>();

	private float mapSize;
	private ArrayList<MarkerOptions> mMarkers = new ArrayList<MarkerOptions>();
	private MarkerOptions houseMarker;

	private LinearLayout linearTitleLayout;
	private LayoutInflater inflater;

	private ActionBar mActionBar;

	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amenities);
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		linearTitleLayout = (LinearLayout) findViewById(R.id.linear_title);

		Bundle bundle = getIntent().getExtras();
		house_x = bundle.getDouble("house_x");
		house_y = bundle.getDouble("house_y");
		house_type = bundle.getInt("house_type");
		if (house_type == AppConstants.TYPE_ID_SALE)
		{
			sale_type = bundle.getInt("sale_type");
			sale_price = bundle.getInt("price");
		} else
		{
			rent_type = bundle.getInt("rent_type");
			rent_price = bundle.getInt("price");
		}

		houseLatLng = new LatLng(house_y, house_x);

		mActionBar = getActionBar();
		if (Build.VERSION.SDK_INT >= 14)
		{
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(true);
		}

		mLocationClient = new LocationClient(this, this, this);
		try
		{
			// Loading map
			initilizeMap();
			// mWrapperLayout.init(googleMap, getPixelsFromDp(this, 39 + 20));
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		CallAds();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initilizeMap()
	{
		if (mGoogleMap == null)
		{
			mGoogleMap = ((TransparentSupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			// mLocationManager = (LocationManager)
			// getSystemService(LOCATION_SERVICE);
			// mGoogleMap.setMyLocationEnabled(true);
			mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
			mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
			mGoogleMap.getUiSettings().setCompassEnabled(false);

			if (mGoogleMap == null)
			{
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
		if (!mLocationClient.isConnected())
		{
			mLocationClient.connect();
		}
	}

	@Override
	public void onStop()
	{
		EasyTracker.getInstance(this).activityStop(this);
		// // If the client is connected
		if (mLocationClient.isConnected())
		{
			stopPeriodicUpdates();
		}
		//
		// // After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();

		super.onStop();
	}

	private void stopPeriodicUpdates()
	{
		mLocationClient.removeLocationUpdates(this);
		// mConnectionState.setText(R.string.location_updates_stopped);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0)
	{
		getLocation(0);

	}

	private void getLocation(int aniParam)
	{

		// If Google Play Services is available
		if (servicesConnected())
		{
			boolean isNeedChangeMap = false;

			if (!isNeedChangeMap)
			{

				mapSize = 15.0f;

				if (0 < AppConstants.km_dis && AppConstants.km_dis <= 0.3)
				{
					mapSize = 16.5f;
				} else if (0.3 < AppConstants.km_dis
						&& AppConstants.km_dis <= 0.5)
				{
					mapSize = 16.0f;
				} else if (0.5 < AppConstants.km_dis
						&& AppConstants.km_dis <= 1)
				{
					mapSize = 15.0f;
				} else if (1 < AppConstants.km_dis && AppConstants.km_dis <= 2)
				{
					mapSize = 14.0f;
				} else
				{
					mapSize = 13.0f;
				}

				if (aniParam == 0)
				{
					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							houseLatLng, mapSize));
				} else
				{
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(houseLatLng).zoom(mapSize).build();
					mGoogleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}
			}

			if (NetworkUtil.getConnectivityStatus(AmenitiesActivity.this) == 0)
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						AmenitiesActivity.this);
				dialog.setTitle("無網路");
				dialog.setMessage("偵測不到網路");
				dialog.setPositiveButton("確定",
						new DialogInterface.OnClickListener()
						{
							public void onClick(
									DialogInterface dialoginterface, int i)
							{
								getLocation(0);
							}
						});
				dialog.show();
			} else
			{
				if (isNeedChangeMap)
				{
					// do nothing
				} else
				{
					new GetAmenitiesTask().execute();
				}

			}

		}
	}

	protected class GetAmenitiesTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			showProgress();
		}

		@Override
		protected Void doInBackground(Void... Void)
		{

			mLandMarks = HouseApi.getAroundAmenities("111", house_y, house_x);
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			endProgress();
			// linearTitleLayout.setVisibility(View.INVISIBLE);
			if (mLandMarks != null && mLandMarks.size() != 0)
			{
				new addMarkerTask().execute();

			} else
			{
				Toast.makeText(AmenitiesActivity.this, "無附近設施資料",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	private class addMarkerTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			mGoogleMap.clear();
			addHouseMarker();
			// setTitleText(mPage);
		}

		@Override
		protected Void doInBackground(Void... arg0)
		{
			// TODO Auto-generated method stub
			setMapMark();
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// Toast.makeText(MainActivity.this, Integer.toString(crawlDateNum),
			// Toast.LENGTH_SHORT).show();

			for (int i = 0; i < mMarkers.size(); i++)
			{
				mGoogleMap.addMarker(mMarkers.get(i));
			}

		}
	}

	private void addHouseMarker()
	{

		mGoogleMap.clear();

		View layout = inflater.inflate(R.layout.item_marker, null);
		layout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		ImageView markerView = (ImageView) layout
				.findViewById(R.id.image_marker);
		TextView markerText = (TextView) layout
				.findViewById(R.id.text_marker_price);
		TextView markerTypeText = (TextView) layout
				.findViewById(R.id.text_rent_type);

		

		// for later marker info window use
		houseMarker = new MarkerOptions().position(houseLatLng).title("房屋所在地");
		if (house_type == AppConstants.TYPE_ID_SALE)
		{
			String groundType = InfoParserApi.parseGroundType(sale_type);
			groundType = groundType.substring(0, 1);
			markerTypeText.setText(groundType);

			String salePriceString = Integer.toString(sale_price);
			markerText.setText(salePriceString + "萬");

			markerView.setImageResource(R.drawable.marker_sale);

			Bitmap bm = loadBitmapFromView(layout);

			// Changing marker icon
			houseMarker.icon(BitmapDescriptorFactory.fromBitmap(bm));

			mGoogleMap.addMarker(houseMarker);

		} else
		{
			String rentType = InfoParserApi.parseRentType(rent_type);
			rentType = rentType.substring(0, 1);
			markerTypeText.setText(rentType);
			
			markerText.setText(Integer.toString(rent_price / 100) + "k");

			markerView.setImageResource(R.drawable.marker_rent);

			Bitmap bm = loadBitmapFromView(layout);

			// Changing marker icon
			houseMarker.icon(BitmapDescriptorFactory.fromBitmap(bm));
			mGoogleMap.addMarker(houseMarker);
		}

	}

	public static Bitmap loadBitmapFromView(View v)
	{
		if (v.getMeasuredHeight() <= 0)
		{
			v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(),
					v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
			v.draw(c);
			return b;
		}

		Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width,
				v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		v.draw(c);
		return b;
	}

	private void setMapMark()
	{

		mMarkers.clear();

		for (int i = 0; i < mLandMarks.size(); i++)
		{
			LatLng newLatLng = new LatLng(mLandMarks.get(i).y_lat,
					mLandMarks.get(i).x_lng);

			// for later marker info window use
			MarkerOptions marker = new MarkerOptions().position(newLatLng)
					.title(mLandMarks.get(i).title);

			// Changing marker icon
			switch (mLandMarks.get(i).type_id)
			{
			case 1:
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.mark_school));
				break;
			case 2:
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.mark_convenience_store));
				break;
			case 3:
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.mark_parks));
				break;
			case 4:
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.mark_supermarket));
				break;
			case 5:
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.mark_hospital));
				break;
			case 6:
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.mark_station));
				break;

			}
			if (mLandMarks.get(i).type_id != 0)
			{
				mMarkers.add(marker);
			}

		}

	}

	private void showProgress()
	{
		linearTitleLayout.setVisibility(View.VISIBLE);
	}

	private void endProgress()
	{
		linearTitleLayout.setVisibility(View.GONE);
	}

	private boolean servicesConnected()
	{

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode)
		{
			// In debug mode, log the status
			Log.d(LocationUtils.APPTAG, "Google Play Service Available");

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else
		{
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, 0);
			if (dialog != null)
			{
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(),
						LocationUtils.APPTAG);
			}
			return false;
		}
	}

	@Override
	public void onDisconnected()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location arg0)
	{
		// TODO Auto-generated method stub

	}

	private void CallAds()
	{
		boolean isGivenStar = Setting.getBooleanSetting(Setting.KeyGiveStar,
				AmenitiesActivity.this);

		if (!isGivenStar)
		{
			adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
			final AdRequest adReq = new AdRequest.Builder().build();

			adMobAdView = new AdView(AmenitiesActivity.this);
			adMobAdView.setAdSize(AdSize.SMART_BANNER);
			adMobAdView.setAdUnitId(AppConstants.MEDIATION_KEY);

			adMobAdView.loadAd(adReq);
			adMobAdView.setAdListener(new AdListener()
			{
				@Override
				public void onAdLoaded()
				{
					adBannerLayout.setVisibility(View.VISIBLE);
					if (adBannerLayout.getChildAt(0) != null)
					{
						adBannerLayout.removeViewAt(0);
					}
					adBannerLayout.addView(adMobAdView);
				}

				public void onAdFailedToLoad(int errorCode)
				{
					adBannerLayout.setVisibility(View.GONE);
				}

			});
		}
	}

}
