package com.kosbrother.housefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import at.bartinger.list.item.EntryAdapter;
import at.bartinger.list.item.EntryItem;
import at.bartinger.list.item.Item;
import at.bartinger.list.item.SectionItem;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosbrother.houseprice.api.HouseApi;
import com.kosbrother.houseprice.api.InfoParserApi;
import com.kosbrother.houseprice.entity.County;
import com.kosbrother.houseprice.entity.RentHouse;
import com.kosbrother.houseprice.entity.Town;
import com.kosbrother.houseprice.fragment.TransparentSupportMapFragment;
import com.kosbrother.imageloader.ImageLoader;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, OnMapClickListener
{

	private LocationClient mLocationClient;
	private GoogleMap mGoogleMap;

	public static boolean isReSearch = true;
	public static boolean isBackFromFilter = false;
	public static boolean isBackFromListFilterButton = false;
	public static boolean isBackFromListGetLocationButton = false;

	private MarkerOptions loacationMarker;

	private float mapSize;

	private MenuItem itemSearch;
	private static final int ID_SEARCH = 5;

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;
	private LinearLayout leftDrawer;

	private boolean isRentHouseTaskRunning;
	private ArrayList<MarkerOptions> mMarkers = new ArrayList<MarkerOptions>();
	private LayoutInflater inflater;

	private TextView titleRentTextView;
	private LinearLayout linearProgressLayout;
	// private LinearLayout linearTitlecontentLayout;

	private LinearLayout locationsButtonLayout;
	private LinearLayout linearFilter;

	private ImageButton btnFocusButton;
	private ImageButton btnLayerButton;
	private int currentMapTypePosition = 0;

	private LinearLayout mainRentLayout;
	private ImageLoader imageLoader;
	private ArrayList<RentHouse> aroundRentHouses = new ArrayList<RentHouse>();
	private ArrayList<MarkerOptions> aroudMarkers = new ArrayList<MarkerOptions>();
	private LinearLayout linearInfoSwitch;
	private ImageButton infoBackButton;
	private ImageButton infoForwardbButton;
	private TextView infoNumsTextView;
	private int currentPosition = 0;
	private LinearLayout linearInfoContent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_layout);

		boolean isFirstOpen = Setting.getBooleanSetting(Setting.keyFirstOpenV2,
				this);
		if (isFirstOpen)
		{
			final LinearLayout firstLinearLayout = (LinearLayout) findViewById(R.id.first_teach_layout);
			firstLinearLayout.setVisibility(View.VISIBLE);
			Button firstConfirButton = (Button) findViewById(R.id.first_confrim_button);
			firstConfirButton.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					Setting.saveBooleanSetting(Setting.keyFirstOpenV2, false,
							MainActivity.this);
					firstLinearLayout.setVisibility(View.GONE);
				}
			});
		}

		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_list_view);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		titleRentTextView = (TextView) findViewById(R.id.title_rent_text);
		linearProgressLayout = (LinearLayout) findViewById(R.id.linear_title_progress);
		locationsButtonLayout = (LinearLayout) findViewById(R.id.linear_location_button);
		linearFilter = (LinearLayout) findViewById(R.id.linear_filter);
		mainRentLayout = (LinearLayout) findViewById(R.id.main_rent_layout);
		linearInfoSwitch = (LinearLayout) findViewById(R.id.linear_info_switch);
		infoBackButton = (ImageButton) findViewById(R.id.button_info_back);
		infoForwardbButton = (ImageButton) findViewById(R.id.button_info_forward);
		infoNumsTextView = (TextView) findViewById(R.id.text_info_nums);
		linearInfoContent = (LinearLayout) findViewById(R.id.linear_info_contact);

		imageLoader = new ImageLoader(this, 100);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		if (Build.VERSION.SDK_INT >= 14)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		}

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close)
		{
			public void onDrawerClosed(View view)
			{
				// getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView)
			{
				// getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		setDrawerLayout();

		btnFocusButton = (ImageButton) findViewById(R.id.image_btn_focus);
		btnFocusButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EasyTracker easyTracker = EasyTracker
						.getInstance(MainActivity.this);
				easyTracker.send(MapBuilder.createEvent("Button",
						"button_press", "focus_button", null).build());
				getLocation(true, 1);
			}
		});

		btnLayerButton = (ImageButton) findViewById(R.id.image_btn_layers);
		btnLayerButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				EasyTracker easyTracker = EasyTracker
						.getInstance(MainActivity.this);
				easyTracker.send(MapBuilder.createEvent("Button",
						"button_press", "layer_button", null).build());

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				// Set the dialog title
				builder.setTitle("顯示地圖").setSingleChoiceItems(R.array.map_type,
						currentMapTypePosition,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int position)
							{
								setMapTypeByPosition(position);
								currentMapTypePosition = position;
								dialog.cancel();
							}

							private void setMapTypeByPosition(int position)
							{
								switch (position)
								{
								case 0:
									mGoogleMap
											.setMapType(GoogleMap.MAP_TYPE_NORMAL);
									break;
								case 1:
									mGoogleMap
											.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
									break;
								case 2:
									mGoogleMap
											.setMapType(GoogleMap.MAP_TYPE_HYBRID);
									break;
								default:
									break;
								}

							}
						});
				builder.show();

			}
		});

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

		locationsButtonLayout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				EasyTracker easyTracker = EasyTracker
						.getInstance(MainActivity.this);
				easyTracker.send(MapBuilder.createEvent("Button",
						"button_press", "area locations button", null).build());
				showCountyDialog();

			}

			private void showCountyDialog()
			{
				final ArrayList<County> mCounties = HouseApi.getCounties();
				final String[] ListStr = new String[mCounties.size()];
				for (int i = 0; i < mCounties.size(); i++)
				{
					ListStr[i] = mCounties.get(i).name;
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("選擇地區");
				builder.setItems(ListStr, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int position)
					{
						showTownDialog(mCounties.get(position).id);
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

			private void showTownDialog(int county_id)
			{
				final ArrayList<Town> mTowns = HouseApi
						.getCountyTowns(county_id);
				final String[] ListStr = new String[mTowns.size()];
				for (int i = 0; i < mTowns.size(); i++)
				{
					ListStr[i] = mTowns.get(i).name;
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setTitle("選擇鄉鎮");
				builder.setItems(ListStr, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int position)
					{
						AppConstants.currentLatLng = new LatLng(mTowns
								.get(position).y_lat,
								mTowns.get(position).x_long);
						getLocation(false, 1);
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});

		linearFilter.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				EasyTracker easyTracker = EasyTracker
						.getInstance(MainActivity.this);
				easyTracker.send(MapBuilder.createEvent("Button",
						"button_press", "filter_button", null).build());
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, FilterActivity.class);
				startActivity(intent);
			}
		});

		infoBackButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (currentPosition != 0)
				{
					currentPosition = currentPosition - 1;
					setInfoWindow(currentPosition, aroundRentHouses.size());
				} else
				{
					currentPosition = aroundRentHouses.size() - 1;
					setInfoWindow(currentPosition, aroundRentHouses.size());
				}

			}

		});

		infoForwardbButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (currentPosition != aroundRentHouses.size() - 1)
				{
					currentPosition = currentPosition + 1;
					setInfoWindow(currentPosition, aroundRentHouses.size());
				} else
				{
					currentPosition = 0;
					setInfoWindow(currentPosition, aroundRentHouses.size());
				}

			}
		});

		linearInfoContent.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				int position = Integer.valueOf(aroudMarkers
						.get(currentPosition).getTitle());

				Intent intent = new Intent(MainActivity.this,
						DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("ItemPosition", position);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	private void setInfoWindow(int currentPosition, int total_size)
	{

		infoNumsTextView.setText(Integer.toString(currentPosition + 1) + " / "
				+ Integer.toString(total_size));

		TextView textTitle = (TextView) findViewById(R.id.rent_list_title);
		TextView textAddress = (TextView) findViewById(R.id.rent_list_address_text);
		TextView textMoney = (TextView) findViewById(R.id.rent_list_money_text);

		TextView textRentType = (TextView) findViewById(R.id.rent_list_type_text);

		ImageView imageView = (ImageView) findViewById(R.id.rent_list_image);

		imageLoader.DisplayImage(
				aroundRentHouses.get(currentPosition).promote_pic, imageView);

		textAddress.setText(aroundRentHouses.get(currentPosition).address);
		textTitle.setText(aroundRentHouses.get(currentPosition).title);

		String moneyString = "<font size=\"3\" color=\"red\">"
				+ Integer.toString(aroundRentHouses.get(currentPosition).price)
				+ "元/月"
				+ "</font>"
				+ ",&nbsp;"
				+ "<font size=\"3\" color=\"black\">"
				+ InfoParserApi.parseRentArea(aroundRentHouses
						.get(currentPosition).rent_area) + "坪" + "</font>";
		textMoney.setText(Html.fromHtml(moneyString));

		String typeString = "<font size=\"3\" color=\"black\">"
				+ InfoParserApi.parseRentType(aroundRentHouses
						.get(currentPosition).rent_type_id);

		if (InfoParserApi.parseRoomArrangement(
				aroundRentHouses.get(currentPosition).rooms, 0,
				aroundRentHouses.get(currentPosition).rest_rooms, 0) != "")
		{
			typeString = typeString
					+ ",&nbsp;"
					+ InfoParserApi
							.parseRoomArrangement(
									aroundRentHouses.get(currentPosition).rooms,
									0,
									aroundRentHouses.get(currentPosition).rest_rooms,
									0);
		}

		if (InfoParserApi.parseLayers(
				aroundRentHouses.get(currentPosition).layer,
				aroundRentHouses.get(currentPosition).total_layer) != "")
		{
			typeString = typeString
					+ ",&nbsp;"
					+ InfoParserApi.parseLayers(
							aroundRentHouses.get(currentPosition).layer,
							aroundRentHouses.get(currentPosition).total_layer);
		}

		typeString = typeString + "</font>";

		textRentType.setText(Html.fromHtml(typeString));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		itemSearch = menu
				.add(0, ID_SEARCH, 0, "搜索")
				.setIcon(R.drawable.icon_search_white)
				.setOnActionExpandListener(
						new MenuItem.OnActionExpandListener()
						{
							private EditText search;

							@Override
							public boolean onMenuItemActionExpand(MenuItem item)
							{
								search = (EditText) item.getActionView();
								search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
								search.setInputType(InputType.TYPE_CLASS_TEXT);
								search.requestFocus();
								search.setOnEditorActionListener(new TextView.OnEditorActionListener()
								{
									@Override
									public boolean onEditorAction(TextView v,
											int actionId, KeyEvent event)
									{
										if (actionId == EditorInfo.IME_ACTION_SEARCH
												|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
										{

											EasyTracker easyTracker = EasyTracker
													.getInstance(MainActivity.this);
											easyTracker.send(MapBuilder
													.createEvent("Button",
															"button_press",
															"search_button",
															null).build());

											String inputString = v.getText()
													.toString();
											Geocoder geocoder = new Geocoder(
													MainActivity.this);
											List<Address> addresses = null;
											Address address = null;
											InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
											imm.hideSoftInputFromWindow(
													v.getWindowToken(), 0);
											try
											{
												addresses = geocoder
														.getFromLocationName(
																inputString, 1);
											} catch (Exception e)
											{
												Log.e("MainActivity",
														e.toString());
											}
											if (addresses == null
													|| addresses.isEmpty())
											{
												Toast.makeText(
														MainActivity.this,
														"無此地點",
														Toast.LENGTH_SHORT)
														.show();
											} else
											{
												address = addresses.get(0);
												double geoLat = address
														.getLatitude();
												double geoLong = address
														.getLongitude();
												AppConstants.currentLatLng = new LatLng(
														geoLat, geoLong);
												getLocation(false, 1);
											}
											return true;
										}
										return false;
									}
								});
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.showSoftInput(search,
										InputMethodManager.SHOW_IMPLICIT);
								return true;
							}

							@Override
							public boolean onMenuItemActionCollapse(
									MenuItem item)
							{

								search.setText("");
								return true;
							}
						}).setActionView(R.layout.collapsible_edittext);

		itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

			mGoogleMap.setOnMapClickListener(this);

			if (mGoogleMap == null)
			{
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public void onLocationChanged(Location arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng arg0)
	{
		// TODO Auto-generated method stub
		mainRentLayout.setVisibility(View.GONE);

		mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
		AppConstants.currentLatLng = arg0;
		mGoogleMap.clear();
		addCurrentLocationMarker();
		getLocation(false, 1);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0)
	{
		if (isReSearch)
		{

			if (isBackFromFilter)
			{
				getLocation(false, 0);
				isBackFromFilter = false;
			} else
			{
				getLocation(true, 0);
			}

			isReSearch = false;

		}

		if (isBackFromListGetLocationButton)
		{
			getLocation(true, 0);
			isBackFromListGetLocationButton = false;
		}

	}

	@Override
	public void onDisconnected()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);

		if (isBackFromListFilterButton)
		{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, FilterActivity.class);
			startActivity(intent);
			isBackFromListFilterButton = false;
		} else
		{
			if (!mLocationClient.isConnected())
			{
				mLocationClient.connect();
			}
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

	private void getLocation(Boolean isReGetLoc, int aniParam)
	{

		// If Google Play Services is available
		if (servicesConnected())
		{
			boolean isNeedChangeMap = false;

			if (isReGetLoc)
			{

				try
				{
					Location currentLocation = mLocationClient
							.getLastLocation();
					if (currentLocation != null)
					{
						AppConstants.currentLatLng = new LatLng(
								currentLocation.getLatitude(),
								currentLocation.getLongitude());
					} else
					{

						AppConstants.currentLatLng = new LatLng(25.0478,
								121.5172);

					}
					// add location marker
					addCurrentLocationMarker();
				} catch (Exception e)
				{
					isNeedChangeMap = true;
				}

			}

			if (!isNeedChangeMap)
			{
				// center_x = AppConstants.currentLatLng.longitude;
				// center_y = AppConstants.currentLatLng.latitude;

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
							new LatLng(AppConstants.currentLatLng.latitude,
									AppConstants.currentLatLng.longitude),
							mapSize));
				} else
				{
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(AppConstants.currentLatLng).zoom(mapSize)
							.build();
					mGoogleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}
			}

			mGoogleMap.setOnMarkerClickListener(new OnMarkerClickListener()
			{

				@Override
				public boolean onMarkerClick(Marker marker)
				{
					if (marker == null || marker.getTitle() == null)
					{
						Toast.makeText(MainActivity.this, "marker null",
								Toast.LENGTH_SHORT).show();
						return true;
					}

					int position = Integer.valueOf(marker.getTitle());

					mainRentLayout.setVisibility(View.VISIBLE);

					TextView textTitle = (TextView) findViewById(R.id.rent_list_title);
					TextView textAddress = (TextView) findViewById(R.id.rent_list_address_text);
					TextView textMoney = (TextView) findViewById(R.id.rent_list_money_text);

					TextView textRentType = (TextView) findViewById(R.id.rent_list_type_text);

					ImageView imageView = (ImageView) findViewById(R.id.rent_list_image);

					imageLoader.DisplayImage(
							Datas.mRentHouses.get(position).promote_pic,
							imageView);

					textAddress.setText(Datas.mRentHouses.get(position).address);
					textTitle.setText(Datas.mRentHouses.get(position).title);

					String moneyString = "<font size=\"3\" color=\"red\">"
							+ Integer.toString(Datas.mRentHouses.get(position).price)
							+ "元/月"
							+ "</font>"
							+ ",&nbsp;"
							+ "<font size=\"3\" color=\"black\">"
							+ InfoParserApi.parseRentArea(Datas.mRentHouses
									.get(position).rent_area) + "坪" + "</font>";
					textMoney.setText(Html.fromHtml(moneyString));

					String typeString = "<font size=\"3\" color=\"black\">"
							+ InfoParserApi.parseRentType(Datas.mRentHouses
									.get(position).rent_type_id);

					if (InfoParserApi.parseRoomArrangement(
							Datas.mRentHouses.get(position).rooms, 0,
							Datas.mRentHouses.get(position).rest_rooms, 0) != "")
					{
						typeString = typeString
								+ ",&nbsp;"
								+ InfoParserApi.parseRoomArrangement(
										Datas.mRentHouses.get(position).rooms,
										0,
										Datas.mRentHouses.get(position).rest_rooms,
										0);
					}

					if (InfoParserApi.parseLayers(
							Datas.mRentHouses.get(position).layer,
							Datas.mRentHouses.get(position).total_layer) != "")
					{
						typeString = typeString
								+ ",&nbsp;"
								+ InfoParserApi.parseLayers(
										Datas.mRentHouses.get(position).layer,
										Datas.mRentHouses.get(position).total_layer);
					}

					typeString = typeString + "</font>";

					textRentType.setText(Html.fromHtml(typeString));

					setAroundRentHouses(position);

					// Toast.makeText(
					// MainActivity.this,
					// "附近" + Integer.toString(aroundRentHouses.size())
					// + "間", Toast.LENGTH_SHORT).show();

					if (aroundRentHouses.size() > 1)
					{
						linearInfoSwitch.setVisibility(View.VISIBLE);
					} else
					{
						linearInfoSwitch.setVisibility(View.GONE);
					}

					return true;
				}

				private void setAroundRentHouses(int position)
				{
					aroundRentHouses.clear();
					aroudMarkers.clear();
					RentHouse pickedRentHouse = Datas.mRentHouses.get(position);
					Location pickedLocation = new Location("");
					pickedLocation.setLatitude(pickedRentHouse.y_lat);
					pickedLocation.setLongitude(pickedRentHouse.x_long);

					for (int i = 0; i < Datas.mRentHouses.size(); i++)
					{
						RentHouse item = Datas.mRentHouses.get(i);
						Location itemLocation = new Location("");
						itemLocation.setLatitude(item.y_lat);
						itemLocation.setLongitude(item.x_long);
						float dis_meters = itemLocation
								.distanceTo(pickedLocation);
						if (dis_meters < 10)
						{
							if (item.title.equals(pickedRentHouse.title))
							{
								currentPosition = aroundRentHouses.size();
							}
							aroundRentHouses.add(item);
							aroudMarkers.add(mMarkers.get(i));
						}
					}

					infoNumsTextView.setText(Integer
							.toString(currentPosition + 1)
							+ " / "
							+ Integer.toString(aroundRentHouses.size()));
				}
			});

			mGoogleMap.setOnCameraChangeListener(new OnCameraChangeListener()
			{

				@Override
				public void onCameraChange(CameraPosition arg0)
				{
					// TODO Auto-generated method stub
					mainRentLayout.setVisibility(View.GONE);
				}
			});

			if (NetworkUtil.getConnectivityStatus(MainActivity.this) == 0)
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						MainActivity.this);
				dialog.setTitle("無網路");
				dialog.setMessage("偵測不到網路");
				dialog.setPositiveButton("確定",
						new DialogInterface.OnClickListener()
						{
							public void onClick(
									DialogInterface dialoginterface, int i)
							{
								getLocation(true, 0);
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
					new GetRentHouseTask().execute();
				}

			}

		}
	}

	protected class GetRentHouseTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			// linearTitleLayout.setVisibility(View.VISIBLE);
			isRentHouseTaskRunning = true;
			showProgress();
		}

		@Override
		protected Void doInBackground(Void... Void)
		{

			HouseApi.getAroundAmenities("111", 25.05535, 121.4588);

			try
			{
				Datas.mRentHouses.clear();
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String rpMinString = Setting.getSetting(
					Setting.keyRentHousePriceMin, MainActivity.this);
			if (rpMinString.equals("0"))
			{
				rpMinString = null;
			}
			String rpMaxString = Setting.getSetting(
					Setting.keyRentHousePriceMax, MainActivity.this);
			if (rpMaxString.equals("0"))
			{
				rpMaxString = null;
			}
			String areaMinString = Setting.getSetting(Setting.keyAreaMin,
					MainActivity.this);
			if (areaMinString.equals("0"))
			{
				areaMinString = null;
			}
			String areaMaxString = Setting.getSetting(Setting.keyAreaMax,
					MainActivity.this);
			if (areaMaxString.equals("0"))
			{
				areaMaxString = null;
			}
			String rentTypeString = Setting.getSetting(Setting.keyRentType,
					MainActivity.this);
			if (rentTypeString.equals("0"))
			{
				rentTypeString = null;
			}
			String buildingTypeString = Setting.getSetting(
					Setting.keyBuildingType, MainActivity.this);
			if (buildingTypeString.equals("0"))
			{
				buildingTypeString = null;
			}

			Datas.mRentHouses = HouseApi.getAroundRentsByAreas(
					AppConstants.km_dis, AppConstants.currentLatLng.longitude,
					AppConstants.currentLatLng.latitude, rpMinString,
					rpMaxString, areaMinString, areaMaxString, rentTypeString,
					buildingTypeString);

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			endProgress();
			isRentHouseTaskRunning = false;
			// linearTitleLayout.setVisibility(View.INVISIBLE);
			if (Datas.mRentHouses != null && Datas.mRentHouses.size() != 0)
			{

				Datas.mRentHouseMap = getRentHouseMap(Datas.mRentHouses);
				titleRentTextView.setText("出租 x "
						+ Integer.toString(Datas.mRentHouses.size()));
				new addMarkerTask().execute();

			} else
			{
				// Datas.mEstates = new ArrayList<RealEstate>();
				// Datas.mEstatesMap = getRealEstatesMap(Datas.mEstates);

				new addMarkerTask().execute();

				Toast.makeText(MainActivity.this, "無資料~", Toast.LENGTH_SHORT)
						.show();
				titleRentTextView.setText("出租 x 0");
				// titleTextView.setText("無資料~");
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
			addCurrentLocationMarker();
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

	private TreeMap<String, ArrayList<RentHouse>> getRentHouseMap(
			ArrayList<RentHouse> rentHouses)
	{

		TreeMap<String, ArrayList<RentHouse>> rentHouseMap = new TreeMap<String, ArrayList<RentHouse>>();
		for (int i = 0; i < rentHouses.size(); i++)
		{
			RentHouse rentHouse = rentHouses.get(i);
			String realEstateKey = Integer.toString(rentHouse.rent_type_id);
			// 先確認key是否存在
			if (rentHouseMap.containsKey(realEstateKey))
			{
				// 已經有的話就把movie加進去
				((ArrayList<RentHouse>) rentHouseMap.get(realEstateKey))
						.add(rentHouse);
			} else
			{
				// 沒有的話就建一個加進去
				ArrayList<RentHouse> newRealEstateList = new ArrayList<RentHouse>(
						10);
				newRealEstateList.add(rentHouse);
				rentHouseMap.put(realEstateKey, newRealEstateList);
			}
		}
		return rentHouseMap;
	}

	private void showProgress()
	{
		linearProgressLayout.setVisibility(View.VISIBLE);
	}

	private void endProgress()
	{
		linearProgressLayout.setVisibility(View.GONE);
	}

	private void setMapMark()
	{

		mMarkers.clear();

		for (int i = 0; i < Datas.mRentHouses.size(); i++)
		{
			LatLng newLatLng = new LatLng(Datas.mRentHouses.get(i).y_lat,
					Datas.mRentHouses.get(i).x_long);

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

			String rentType = InfoParserApi.parseRentType(Datas.mRentHouses
					.get(i).rent_type_id);
			rentType = rentType.substring(0, 1);
			markerTypeText.setText(rentType);

			// for later marker info window use
			MarkerOptions marker = new MarkerOptions().position(newLatLng)
					.title(Integer.toString(i));

			double rentPrice = ((double) Datas.mRentHouses.get(i).price) / 1000;
			String rentPriceString = Double.toString(rentPrice);
			if (rentPriceString.indexOf(".0") != -1)
			{
				rentPriceString = rentPriceString.substring(0, rentPriceString.indexOf(".0"));
			}
			markerText.setText(rentPriceString + "k");

			markerView.setImageResource(R.drawable.marker_rent);

			Bitmap bm = loadBitmapFromView(layout);

			// Changing marker icon
			marker.icon(BitmapDescriptorFactory.fromBitmap(bm));

			mMarkers.add(marker);
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

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub

		if (mainRentLayout.getVisibility() == View.VISIBLE)
		{
			mainRentLayout.setVisibility(View.GONE);
		} else
		{
			super.onBackPressed();
			isReSearch = true;
			// finish();
		}
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

	public static class ErrorDialogFragment extends DialogFragment
	{

		// Global field to contain the error dialog
		private Dialog mDialog;

		/**
		 * Default constructor. Sets the dialog field to null
		 */
		public ErrorDialogFragment()
		{
			super();
			mDialog = null;
		}

		/**
		 * Set the dialog to display
		 * 
		 * @param dialog
		 *            An error dialog
		 */
		public void setDialog(Dialog dialog)
		{
			mDialog = dialog;
		}

		/*
		 * This method must return a Dialog to the DialogFragment.
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			return mDialog;
		}
	}

	private void addCurrentLocationMarker()
	{
		// TODO Auto-generated method stub
		mGoogleMap.clear();
		loacationMarker = new MarkerOptions().position(
				AppConstants.currentLatLng).draggable(true);
		loacationMarker.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.pin_red));
		mGoogleMap.addMarker(loacationMarker);
	}

	private void setDrawerLayout()
	{

		items.add(new SectionItem("房屋搜尋"));
		items.add(new EntryItem("位置附近", R.drawable.icon_access_location));
		items.add(new EntryItem("條件篩選", R.drawable.icon_filter));
		items.add(new EntryItem("我的最愛", R.drawable.icon_favorite));
		items.add(new SectionItem("房貸計算"));
		items.add(new EntryItem("房貸計算機", R.drawable.icon_calculator));
		items.add(new SectionItem("其他"));
		items.add(new EntryItem("分享", R.drawable.icon_recommend));
		items.add(new EntryItem("給評(星星)", R.drawable.icon_star3));
		items.add(new EntryItem("關於我們", R.drawable.icon_about));

		mDrawerAdapter = new EntryAdapter(MainActivity.this, items);
		mDrawerListView.setAdapter(mDrawerAdapter);
		mDrawerListView.setOnItemClickListener((new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id)
			{
				if (!items.get(position).isSection())
				{
					// EntryItem item = (EntryItem) items.get(position);

					switch (position)
					{
					case 1:
						EasyTracker easyTracker = EasyTracker
								.getInstance(MainActivity.this);
						easyTracker.send(MapBuilder.createEvent("Button",
								"button_press", "focus_button2", null).build());
						getLocation(true, 1);
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 2:
						EasyTracker easyTracker2 = EasyTracker
								.getInstance(MainActivity.this);
						easyTracker2
								.send(MapBuilder.createEvent("Button",
										"button_press", "filter_button2", null)
										.build());
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, FilterActivity.class);
						startActivity(intent);
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 3:
						EasyTracker easyTrackerF = EasyTracker
								.getInstance(MainActivity.this);
						easyTrackerF.send(MapBuilder.createEvent("Button",
								"button_press", "Favorite_button", null)
								.build());
						Intent intentFavorite = new Intent();
						intentFavorite.setClass(MainActivity.this,
								FavoriteActivity.class);
						startActivity(intentFavorite);
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 5:
						EasyTracker easyTracker3 = EasyTracker
								.getInstance(MainActivity.this);
						easyTracker3.send(MapBuilder.createEvent("Button",
								"button_press", "calculator_button", null)
								.build());

						Intent intent2 = new Intent(MainActivity.this,
								CalculatorActivity.class);
						startActivity(intent2);
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 7:
						Intent intent3 = new Intent(Intent.ACTION_SEND);
						intent3.setType("text/plain");
						intent3.putExtra(Intent.EXTRA_TEXT,
								"找屋高手 https://play.google.com/store/apps/details?id=com.kosbrother.housefinder");
						startActivity(Intent.createChooser(intent3, "Share..."));

						EasyTracker easyTracker4 = EasyTracker
								.getInstance(MainActivity.this);
						easyTracker4.send(MapBuilder.createEvent("Button",
								"button_press", "share_button", null).build());
						break;
					case 8:
						EasyTracker easyTracker5 = EasyTracker
								.getInstance(MainActivity.this);
						easyTracker5.send(MapBuilder.createEvent("Button",
								"button_press", "star_button", null).build());

						Uri uri = Uri
								.parse("https://play.google.com/store/apps/details?id=com.kosbrother.housefinder");
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
						// Setting.saveBooleanSetting(Setting.KeyGiveStar, true,
						// MainActivity.this);
						// Setting.saveBooleanSetting(Setting.KeyPushStarDialog,
						// false, MainActivity.this);
						break;
					case 9:
						// about us
						EasyTracker easyTracker6 = EasyTracker
								.getInstance(MainActivity.this);
						easyTracker6.send(MapBuilder.createEvent("Button",
								"button_press", "about_button", null).build());

						Intent intent5 = new Intent(MainActivity.this,
								AboutUsActivity.class);
						startActivity(intent5);
						break;
					default:
						break;
					}
				}
			}
		}));

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(getMenuItem(item)))
		{
			return true;
		} else
		{
			switch (item.getItemId())
			{
			case ID_SEARCH:
				// Toast.makeText(MainActivity.this, "search",
				// Toast.LENGTH_SHORT).show();
				break;
			case R.id.menu_list:
				// Toast.makeText(MainActivity.this, "list",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ListActivity.class);
				// intent.putExtras(bundle);
				startActivity(intent);
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private android.view.MenuItem getMenuItem(final MenuItem item)
	{
		return new android.view.MenuItem()
		{
			@Override
			public int getItemId()
			{
				return item.getItemId();
			}

			public boolean isEnabled()
			{
				return true;
			}

			@Override
			public boolean collapseActionView()
			{
				return false;
			}

			@Override
			public boolean expandActionView()
			{
				return false;
			}

			@Override
			public ActionProvider getActionProvider()
			{
				return null;
			}

			@Override
			public View getActionView()
			{
				return null;
			}

			@Override
			public char getAlphabeticShortcut()
			{
				return 0;
			}

			@Override
			public int getGroupId()
			{
				return 0;
			}

			@Override
			public Drawable getIcon()
			{
				return null;
			}

			@Override
			public Intent getIntent()
			{
				return null;
			}

			@Override
			public ContextMenuInfo getMenuInfo()
			{
				return null;
			}

			@Override
			public char getNumericShortcut()
			{
				return 0;
			}

			@Override
			public int getOrder()
			{
				return 0;
			}

			@Override
			public SubMenu getSubMenu()
			{
				return null;
			}

			@Override
			public CharSequence getTitle()
			{
				return null;
			}

			@Override
			public CharSequence getTitleCondensed()
			{
				return null;
			}

			@Override
			public boolean hasSubMenu()
			{
				return false;
			}

			@Override
			public boolean isActionViewExpanded()
			{
				return false;
			}

			@Override
			public boolean isCheckable()
			{
				return false;
			}

			@Override
			public boolean isChecked()
			{

				return false;
			}

			@Override
			public boolean isVisible()
			{
				return false;
			}

			@Override
			public android.view.MenuItem setActionProvider(
					ActionProvider actionProvider)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(View view)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(int resId)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setAlphabeticShortcut(char alphaChar)
			{

				return null;
			}

			@Override
			public android.view.MenuItem setCheckable(boolean checkable)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setChecked(boolean checked)
			{

				return null;
			}

			@Override
			public android.view.MenuItem setEnabled(boolean enabled)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(Drawable icon)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(int iconRes)
			{

				return null;
			}

			@Override
			public android.view.MenuItem setIntent(Intent intent)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setNumericShortcut(char numericChar)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setOnActionExpandListener(
					OnActionExpandListener listener)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setOnMenuItemClickListener(
					OnMenuItemClickListener menuItemClickListener)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setShortcut(char numericChar,
					char alphaChar)
			{
				return null;
			}

			@Override
			public void setShowAsAction(int actionEnum)
			{

			}

			@Override
			public android.view.MenuItem setShowAsActionFlags(int actionEnum)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(CharSequence title)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(int title)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setTitleCondensed(CharSequence title)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setVisible(boolean visible)
			{
				return null;
			}
		};
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

}
