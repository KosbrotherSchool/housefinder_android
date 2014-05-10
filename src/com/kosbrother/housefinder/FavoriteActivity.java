package com.kosbrother.housefinder;

import java.sql.SQLException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.kosbrother.housefinder.data.DatabaseHelper;
import com.kosbrother.housefinder.data.OrmRentHouse;
import com.kosbrother.houseprice.adapter.ListOrmRentHouseAdapter;

@SuppressLint("NewApi")
public class FavoriteActivity extends FragmentActivity
{

	private ListOrmRentHouseAdapter mAdapter;
	private ListView mainListView;
	private DatabaseHelper databaseHelper = null;
	// private ActionBar mActionBar;
	private LinearLayout favoriteNoDataLayout;

	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);

		if (Build.VERSION.SDK_INT >= 14)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
		}

		mainListView = (ListView) findViewById(R.id.list_estates);
		favoriteNoDataLayout = (LinearLayout) findViewById(R.id.favorite_no_data_layout);

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
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		refreshList();
	}

	public void refreshList()
	{
		try
		{
			Dao<OrmRentHouse, Integer> rentDao = getHelper()
					.getOrmRentHouseDao();
			ArrayList<OrmRentHouse> lists = new ArrayList<OrmRentHouse>(
					rentDao.queryForAll());
			mAdapter = new ListOrmRentHouseAdapter(FavoriteActivity.this, lists);
			mainListView.setAdapter(mAdapter);

			if (lists.size() == 0)
			{
				favoriteNoDataLayout.setVisibility(View.VISIBLE);
			} else
			{
				favoriteNoDataLayout.setVisibility(View.GONE);
			}

			getActionBar().setTitle(
					"我的最愛: " + Integer.toString(lists.size()) + "筆資料");
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		return databaseHelper;
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
				FavoriteActivity.this);

		if (!isGivenStar)
		{
			adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
			final AdRequest adReq = new AdRequest.Builder().build();

			adMobAdView = new AdView(FavoriteActivity.this);
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