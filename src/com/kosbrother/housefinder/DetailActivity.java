package com.kosbrother.housefinder;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.kosbrother.housefinder.data.DatabaseHelper;
import com.kosbrother.housefinder.tool.ProblemReport;
import com.kosbrother.houseprice.api.InfoParserApi;
import com.kosbrother.houseprice.fragment.RentDetailFragment;
import com.kosbrother.houseprice.fragment.SaleDetailFragment;

public class DetailActivity extends FragmentActivity
{
	int NUM_ITEMS;
	MyAdapter mAdapter;
	ViewPager mPager;
	private ActionBar mActionBar;
	private DatabaseHelper databaseHelper = null;

	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;
	private int typeId;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.non_scrollable_fragment_pager);
		Bundle bundle = getIntent().getExtras();

		String paramsString = bundle.getString("type_num");
		String positionString = paramsString.substring(paramsString.indexOf("_") + 1);
		int position = Integer.valueOf(positionString);
		String type = paramsString.substring(0, paramsString.indexOf("_"));
		typeId = InfoParserApi.parseTypeId(type);

		// int position = bundle.getInt("ItemPosition");

		if (typeId == 1)
		{
			NUM_ITEMS = Datas.mSaleHouses.size();
		} else if (typeId == 2)
		{
			NUM_ITEMS = Datas.mRentHouses.size();
		}

		setPagers(position);

		CallAds();
	}

	private void setPagers(int position)
	{
		// TODO Auto-generated method stub
		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mPager.setCurrentItem(position);

		mActionBar = getActionBar();

		// enable ActionBar app icon to behave as action to toggle nav drawer
		if (Build.VERSION.SDK_INT >= 14)
		{
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(true);
		}
		mActionBar.setTitle("第" + Integer.toString(position + 1) + "/" + Integer.toString(NUM_ITEMS) + "筆資料");

		mPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int current_position)
			{
				// TODO Auto-generated method stub
				Log.i("DetailActivity", "current_position = " + current_position);
				mActionBar.setTitle("第" + Integer.toString(current_position + 1) + "/" + Integer.toString(NUM_ITEMS)
						+ "筆資料");
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub

			}

		});
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
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position)
		{
			Fragment mFragment = null;

			if (typeId == 1)
			{
				mFragment = SaleDetailFragment.newInstance(position, DetailActivity.this);
			} else if (typeId == 2)
			{
				mFragment = RentDetailFragment.newInstance(position, DetailActivity.this);
			}

			return mFragment;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_report:
			int currentNum = mPager.getCurrentItem();
			String title = "";
			if (typeId == AppConstants.TYPE_ID_SALE)
			{
				title = Datas.mSaleHouses.get(currentNum).title + "(出售編號:"
						+ Integer.toString(Datas.mSaleHouses.get(currentNum).house_id) + ")";
			} else if (typeId == AppConstants.TYPE_ID_RENT)
			{
				title = Datas.mRentHouses.get(currentNum).title + "(出租編號:"
						+ Integer.toString(Datas.mRentHouses.get(currentNum).rent_id) + ")";
			}
			ProblemReport.createReportDialog(this, title, "");
			return true;
		case R.id.menu_up:
			if (mPager.getCurrentItem() > 0)
			{
				mPager.setCurrentItem(mPager.getCurrentItem() - 1);
			} else
			{
				mPager.setCurrentItem(NUM_ITEMS - 1);
			}
			return true;
		case R.id.menu_down:
			if (mPager.getCurrentItem() < NUM_ITEMS - 1)
			{
				mPager.setCurrentItem(mPager.getCurrentItem() + 1);
			} else
			{
				mPager.setCurrentItem(0);
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.detail, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
		if (databaseHelper != null)
		{
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	/**
	 * You'll need this in your class to get the helper from the manager once
	 * per class.
	 */
	public DatabaseHelper getHelper()
	{
		if (databaseHelper == null)
		{
			databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		return databaseHelper;
	}

	private void CallAds()
	{
		boolean isGivenStar = Setting.getBooleanSetting(Setting.KeyGiveStar, DetailActivity.this);

		if (!isGivenStar)
		{
			adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
			final AdRequest adReq = new AdRequest.Builder().build();

			adMobAdView = new AdView(DetailActivity.this);
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

	@Override
	protected void onResume()
	{
		super.onResume();
		if ((typeId == 2 && Datas.mRentHouses.size() == 0) || (typeId == 1 && Datas.mSaleHouses.size() == 0))
		{
			finish();
		}

	}

}
