package com.kosbrother.housefinder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
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

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kosbrother.housefinder.tool.DrawerMenuItemMethoud;
import com.kosbrother.houseprice.fragment.RentListFragment;
import com.kosbrother.houseprice.fragment.SaleListFragment;

@SuppressLint("NewApi")
public class ListActivity extends FragmentActivity implements TabListener,
		OnPageChangeListener
{

	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;

	// private ListRentHouseAdapter mAdapter;
	// private ListView mainListView;
	private int currentSortPosition;
	private LinearLayout leftDrawer;
	private ActionBar mActionBar;
	MyAdapter mAdapter;
	ViewPager mPager;

	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);
		mActionBar = getActionBar();

		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mActionBar.addTab(mActionBar.newTab().setText("出售")
				.setTabListener(this));
		mActionBar.addTab(mActionBar.newTab().setText("出租")
				.setTabListener(this));
		forceTabs();

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(this);
		if (Datas.mSaleHouses.size() == 0)
		{
			mPager.setCurrentItem(1);
		} else
		{
			mPager.setCurrentItem(0);
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_list_view);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

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

		// CallAds();

	}

	public class MyAdapter extends FragmentStatePagerAdapter
	{
		public MyAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public int getCount()
		{
			return 2;
		}

		@Override
		public Fragment getItem(int position)
		{

			Fragment mFragment = null;
			if (position == 0)
			{
				mFragment = SaleListFragment.newInstance(ListActivity.this);
			} else
			{
				mFragment = RentListFragment.newInstance(ListActivity.this);
			}

			return mFragment;
		}

	}

	@Override
	public void onTabReselected(Tab mTab, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab mTab, FragmentTransaction arg1)
	{
		if (mPager != null)
		{
			mPager.setCurrentItem(mTab.getPosition());
		}
	}

	@Override
	public void onTabUnselected(Tab mTab, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub

	}

	public void forceTabs()
	{
		try
		{
			final ActionBar actionBar = getActionBar();
			final Method setHasEmbeddedTabsMethod = actionBar.getClass()
					.getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
			setHasEmbeddedTabsMethod.setAccessible(true);
			setHasEmbeddedTabsMethod.invoke(actionBar, true);
		} catch (final Exception e)
		{
			// Handle issues as needed: log, warn user, fallback etc
			// Alternatively, ignore this and default tab behaviour will apply.
		}
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
						EasyTracker easyTracker = EasyTracker
								.getInstance(ListActivity.this);
						easyTracker.send(MapBuilder.createEvent("Button",
								"button_press", "focus_button2", null).build());
						finish();
						MainActivity.isBackFromListGetLocationButton = true;
						break;
					case 2:
						EasyTracker easyTracker2 = EasyTracker
								.getInstance(ListActivity.this);
						easyTracker2
								.send(MapBuilder.createEvent("Button",
										"button_press", "filter_button2", null)
										.build());
						finish();
						MainActivity.isBackFromListFilterButton = true;
						break;
					case 3:
						EasyTracker easyTrackerF = EasyTracker
								.getInstance(ListActivity.this);
						easyTrackerF.send(MapBuilder.createEvent("Button",
								"button_press", "Favorite_button", null)
								.build());
						Intent intentFavorite = new Intent();
						intentFavorite.setClass(ListActivity.this,
								FavoriteActivity.class);
						startActivity(intentFavorite);
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 5:
						EasyTracker easyTracker3 = EasyTracker
								.getInstance(ListActivity.this);
						easyTracker3.send(MapBuilder.createEvent("Button",
								"button_press", "calculator_button", null)
								.build());

						Intent intent2 = new Intent(ListActivity.this,
								CalculatorActivity.class);
						startActivity(intent2);
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 7:
						EasyTracker easyTracker4 = EasyTracker
								.getInstance(ListActivity.this);
						easyTracker4.send(MapBuilder.createEvent("Button",
								"button_press", "share_button", null).build());

						Intent intent3 = new Intent(Intent.ACTION_SEND);
						intent3.setType("text/plain");
						intent3.putExtra(Intent.EXTRA_TEXT,
								"找屋高手 https://play.google.com/store/apps/details?id=com.kosbrother.housefinder");
						startActivity(Intent.createChooser(intent3, "Share..."));
						break;
					case 8:
						EasyTracker easyTracker5 = EasyTracker
								.getInstance(ListActivity.this);
						easyTracker5.send(MapBuilder.createEvent("Button",
								"button_press", "star_button", null).build());

						Uri uri = Uri
								.parse("https://play.google.com/store/apps/details?id=com.kosbrother.housefinder");
						Intent it = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(it);
						break;
					case 9:
						// about us
						EasyTracker easyTracker6 = EasyTracker
								.getInstance(ListActivity.this);
						easyTracker6.send(MapBuilder.createEvent("Button",
								"button_press", "about_button", null).build());

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
		if (Datas.mRentHouses.size() == 0 && Datas.mSaleHouses.size() == 0)
		{
			finish();
		}

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
		if (mDrawerToggle.onOptionsItemSelected(DrawerMenuItemMethoud
				.getMenuItem(item)))
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

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ListActivity.this);
				// Set the dialog title
				builder.setTitle("排序").setSingleChoiceItems(R.array.list_sort,
						currentSortPosition,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int position)
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
		case 0:
			Collections.sort(Datas.mRentHouses,
					new Datas.RentPriceComparator(0));
			break;
		case 1:
			Collections.sort(Datas.mRentHouses,
					new Datas.RentPriceComparator(1));
			break;
		case 2:
			Collections
					.sort(Datas.mRentHouses, new Datas.RentAreaComparator(0));
			break;
		case 3:
			Collections
					.sort(Datas.mRentHouses, new Datas.RentAreaComparator(1));
			break;
		}
		// mAdapter.notifyDataSetChanged();
	}

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
		EasyTracker.getInstance(this).activityStart(this); // Add this
		// method.
	}

	@Override
	public void onStop()
	{
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	private void CallAds()
	{
		boolean isGivenStar = Setting.getBooleanSetting(Setting.KeyGiveStar,
				ListActivity.this);

		if (!isGivenStar)
		{
			adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
			final AdRequest adReq = new AdRequest.Builder().build();

			adMobAdView = new AdView(ListActivity.this);
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

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int index)
	{
		mPager.setCurrentItem(index);
		mActionBar.selectTab(mActionBar.getTabAt(index));

	}

}