package com.kosbrother.housefinder;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import at.bartinger.list.item.EntryAdapter;
import at.bartinger.list.item.EntryItem;
import at.bartinger.list.item.Item;
import at.bartinger.list.item.SectionItem;

import com.google.android.gms.ads.AdView;
import com.kosbrother.houseprice.adapter.ListRentHouseAdapter;

@SuppressLint("NewApi")
public class ListActivity extends FragmentActivity
{

	// private ActionBarHelper mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;

	// private MenuItem itemSearch;
	// private static final int ID_SEARCH = 5;

	private ListRentHouseAdapter mAdapter;
	private ListView mainListView;

	private int currentSortPosition;
	private LinearLayout leftDrawer;
	private LayoutInflater inflater;

	private ActionBar mActionBar;

	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_list_layout);
		mActionBar = getActionBar();
		mActionBar.setTitle("找屋高手: " + Integer.toString(Datas.mRentHouses.size()) + "筆");

		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_list_view);

		// mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
				R.string.drawer_open, R.string.drawer_close)
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

		mainListView = (ListView) findViewById(R.id.list_estates);
		mAdapter = new ListRentHouseAdapter(ListActivity.this, Datas.mRentHouses);
		mainListView.setAdapter(mAdapter);
		
//		CallAds();
		
	}

	private void setDrawerLayout()
	{

		items.add(new SectionItem("實價登錄搜尋"));
		items.add(new EntryItem("位置附近", R.drawable.icon_access_location));
		items.add(new EntryItem("條件篩選", R.drawable.icon_filter));
		items.add(new SectionItem("房貸計算"));
		items.add(new EntryItem("房貸計算機", R.drawable.icon_calculator));
		items.add(new SectionItem("其他"));
		items.add(new EntryItem("推薦", R.drawable.icon_recommend));
		items.add(new EntryItem("給評(星星)", R.drawable.icon_star3));
		items.add(new EntryItem("關於我們", R.drawable.icon_about));

		mDrawerAdapter = new EntryAdapter(ListActivity.this, items);
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
						// EasyTracker easyTracker = EasyTracker
						// .getInstance(MainActivity.this);
						// easyTracker.send(MapBuilder.createEvent("Button",
						// "button_press", "focus_button2", null).build());
//						getLocation(true, 1);
//						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 2:
						// EasyTracker easyTracker2 = EasyTracker
						// .getInstance(MainActivity.this);
						// easyTracker2
						// .send(MapBuilder.createEvent("Button",
						// "button_press", "filter_button2", null)
						// .build());
						Intent intent = new Intent();
						intent.setClass(ListActivity.this, FilterActivity.class);
						startActivity(intent);
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 4:
						// EasyTracker easyTracker3 = EasyTracker
						// .getInstance(MainActivity.this);
						// easyTracker3.send(MapBuilder.createEvent("Button",
						// "button_press", "calculator_button", null)
						// .build());

						Intent intent2 = new Intent(ListActivity.this,
								CalculatorActivity.class);
						startActivity(intent2);
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 6:
						Intent intent3 = new Intent(Intent.ACTION_SEND);
						intent3.setType("text/plain");
						intent3.putExtra(Intent.EXTRA_TEXT,
								"看屋高手 https://play.google.com/store/apps/details?id=com.kosbrother.houseprice");
						startActivity(Intent.createChooser(intent3, "Share..."));

						// EasyTracker easyTracker4 = EasyTracker
						// .getInstance(MainActivity.this);
						// easyTracker4.send(MapBuilder.createEvent("Button",
						// "button_press", "share_button", null).build());
						break;
					case 7:
						// EasyTracker easyTracker5 = EasyTracker
						// .getInstance(MainActivity.this);
						// easyTracker5.send(MapBuilder.createEvent("Button",
						// "button_press", "star_button", null).build());

						Uri uri = Uri
								.parse("https://play.google.com/store/apps/details?id=com.kosbrother.houseprice");
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
						Setting.saveBooleanSetting(Setting.KeyGiveStar, true,
								ListActivity.this);
						Setting.saveBooleanSetting(Setting.KeyPushStarDialog,
								false, ListActivity.this);
						break;
					case 8:
						// about us
						// EasyTracker easyTracker6 = EasyTracker
						// .getInstance(MainActivity.this);
						// easyTracker6.send(MapBuilder.createEvent("Button",
						// "button_press", "about_button", null).build());

						Intent intent5 = new Intent(ListActivity.this,
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
	protected void onResume()
	{
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.menu_list, menu);

		return super.onCreateOptionsMenu(menu);
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
			case R.id.menu_map:
				// Toast.makeText(ListActivity.this, "Map", Toast.LENGTH_SHORT)
				// .show();
				finish();
				break;
			case R.id.menu_sorting:

				AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
				// Set the dialog title
				builder.setTitle("排序").setSingleChoiceItems(R.array.list_sort,
						currentSortPosition, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int position)
							{
								sortingByPosition(position);
								currentSortPosition = position;
								dialog.cancel();
							}
						});
				builder.show();

				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void sortingByPosition(int position)
	{
		switch (position)
		{
//		case 0:
//			Collections.sort(Datas.mEstates, new Datas.BuyPerSquareComparator(0));
//			break;
//		case 1:
//			Collections.sort(Datas.mEstates, new Datas.BuyPerSquareComparator(1));
//			break;
//		case 2:
//			Collections.sort(Datas.mEstates, new Datas.BuyTotalPriceComparator(0));
//			break;
//		case 3:
//			Collections.sort(Datas.mEstates, new Datas.BuyTotalPriceComparator(1));
//			break;
//		case 4:
//			Collections.sort(Datas.mEstates, new Datas.BuildingExchangeAreaComparator(0));
//			break;
//		case 5:
//			Collections.sort(Datas.mEstates, new Datas.BuildingExchangeAreaComparator(1));
//			break;
//		case 6:
//			Collections.sort(Datas.mEstates, new Datas.BuiltDateComparator());
//			break;
		}
		mAdapter.notifyDataSetChanged();
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
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean expandActionView()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public ActionProvider getActionProvider()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View getActionView()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public char getAlphabeticShortcut()
			{
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getGroupId()
			{
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Drawable getIcon()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Intent getIntent()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ContextMenuInfo getMenuInfo()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public char getNumericShortcut()
			{
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getOrder()
			{
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public SubMenu getSubMenu()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CharSequence getTitle()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CharSequence getTitleCondensed()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasSubMenu()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isActionViewExpanded()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isCheckable()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isChecked()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isVisible()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public android.view.MenuItem setActionProvider(ActionProvider actionProvider)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(View view)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(int resId)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setAlphabeticShortcut(char alphaChar)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setCheckable(boolean checkable)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setChecked(boolean checked)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setEnabled(boolean enabled)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(Drawable icon)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(int iconRes)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIntent(Intent intent)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setNumericShortcut(char numericChar)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setOnMenuItemClickListener(
					OnMenuItemClickListener menuItemClickListener)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setShortcut(char numericChar, char alphaChar)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setShowAsAction(int actionEnum)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public android.view.MenuItem setShowAsActionFlags(int actionEnum)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(CharSequence title)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(int title)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitleCondensed(CharSequence title)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setVisible(boolean visible)
			{
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	

	// private ActionBarHelper createActionBarHelper() {
	// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	// return new ActionBarHelperICS();
	// } else {
	// return new ActionBarHelper();
	// }
	// }
	//
	// private class ActionBarHelper {
	// public void init() {}
	// public void onDrawerClosed() {}
	// public void onDrawerOpened() {}
	// public void setTitle(CharSequence title) {}
	// }
	//
	// private class ActionBarHelperICS extends ActionBarHelper {
	// private final ActionBar mActionBar;
	// private CharSequence mDrawerTitle;
	// private CharSequence mTitle;
	//
	// ActionBarHelperICS() {
	// mActionBar = getActionBar();
	// }
	//
	// @Override
	// public void init() {
	// mActionBar.setDisplayHomeAsUpEnabled(true);
	// mActionBar.setHomeButtonEnabled(true);
	// mTitle = mDrawerTitle = getTitle();
	// }
	//
	// @Override
	// public void onDrawerClosed() {
	// super.onDrawerClosed();
	// mActionBar.setTitle(mTitle);
	// }
	//
	// @Override
	// public void onDrawerOpened() {
	// super.onDrawerOpened();
	// mActionBar.setTitle(mDrawerTitle);
	// }
	//
	// @Override
	// public void setTitle(CharSequence title) {
	// mTitle = title;
	// }
	// }

	// private class DemoDrawerListener implements DrawerLayout.DrawerListener {
	// @Override
	// public void onDrawerOpened(View drawerView) {
	// mDrawerToggle.onDrawerOpened(drawerView);
	// mActionBar.onDrawerOpened();
	// }
	//
	// @Override
	// public void onDrawerClosed(View drawerView) {
	// mDrawerToggle.onDrawerClosed(drawerView);
	// mActionBar.onDrawerClosed();
	// }
	//
	// @Override
	// public void onDrawerSlide(View drawerView, float slideOffset) {
	// mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
	// }
	//
	// @Override
	// public void onDrawerStateChanged(int newState) {
	// mDrawerToggle.onDrawerStateChanged(newState);
	// }
	// }

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		// The rest of your onStart() code.
//		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop()
	{
		super.onStop();
		// The rest of your onStop() code.
//		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}
	
//	private void CallAds()
//	{
//
//		adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
//		final AdRequest adReq = new AdRequest.Builder().build();
//
//		// 12-18 17:01:12.438: I/Ads(8252): Use
//		// AdRequest.Builder.addTestDevice("A25819A64B56C65500038B8A9E7C19DD")
//		// to get test ads on this device.
//
//		adMobAdView = new AdView(ListActivity.this);
//		adMobAdView.setAdSize(AdSize.SMART_BANNER);
//		adMobAdView.setAdUnitId(Constants.MEDIATION_KEY);
//
//		adMobAdView.loadAd(adReq);
//		adMobAdView.setAdListener(new AdListener()
//		{
//			@Override
//			public void onAdLoaded() {
//				adBannerLayout.setVisibility(View.VISIBLE);
//				if (adBannerLayout.getChildAt(0)!=null)
//				{
//					adBannerLayout.removeViewAt(0);
//				}
//				adBannerLayout.addView(adMobAdView);
//			}
//			
//			public void onAdFailedToLoad(int errorCode) {
//				adBannerLayout.setVisibility(View.GONE);
//			}
//			
//		});	
//	}

}