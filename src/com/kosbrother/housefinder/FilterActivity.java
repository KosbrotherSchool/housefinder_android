package com.kosbrother.housefinder;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class FilterActivity extends FragmentActivity
{
	private ActionBar mActionBar;

	private String minHousePriceString = "";
	private String maxHousePriceString = "";
	private String stringRentType = "";
	private String areaMinString = "";
	private String areaMaxString = "";

	private EditText lowHousePriceEditText;
	private EditText highHousePriceEditText;

	private CheckBox rent_type_0;
	private CheckBox rent_type_1;
	private CheckBox rent_type_2;
	private CheckBox rent_type_3;
	private CheckBox rent_type_4;
	private CheckBox rent_type_5;
	private CheckBox rent_type_6;
	private CheckBox rent_type_7;
	private CheckBox rent_type_8;
	private CheckBox rent_type_9;
	private CheckBox rent_type_10;
	private CheckBox rent_type_11;
	private CheckBox rent_type_12;

	private EditText areaMinEditText;
	private EditText areaMaxEditText;

	private Button buttonSearch;
	// private Button buttonSetOften;

	private ImageView rentInfoImageView;

	 private RelativeLayout adBannerLayout;
	 private AdView adMobAdView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);

		// boolean isFirstOpen = Setting.getFirstBoolean(FilterActivity.this);
		// if (isFirstOpen)
		// {
		// // Toast.makeText(this, "請先設定搜索條件", Toast.LENGTH_SHORT).show();
		// Setting.setFirstBoolean(FilterActivity.this);
		// }

		lowHousePriceEditText = (EditText) findViewById(R.id.low_house_price_edit);
		highHousePriceEditText = (EditText) findViewById(R.id.high_house_price_edit);
		buttonSearch = (Button) findViewById(R.id.button_search);

		rentInfoImageView = (ImageView) findViewById(R.id.ground_info_image);

		rentInfoImageView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						FilterActivity.this);

				LayoutInflater inflater = FilterActivity.this
						.getLayoutInflater();

				dialog.setTitle("出租型態(可覆選)");
				dialog.setView(inflater.inflate(R.layout.dialog_rent_type_info,
						null));
				dialog.setPositiveButton("確定",
						new DialogInterface.OnClickListener()
						{
							public void onClick(
									DialogInterface dialoginterface, int i)
							{

							}
						});
				dialog.show();

			}
		});

		buttonSearch.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				MainActivity.isReSearch = true;
				MainActivity.isBackFromFilter = true;

				// low house price
				if (!lowHousePriceEditText.getText().toString().equals(""))
				{
					Setting.saveSetting(Setting.keyRentHousePriceMin,
							lowHousePriceEditText.getText().toString(),
							FilterActivity.this);
				} else
				{
					Setting.saveSetting(Setting.keyRentHousePriceMin,
							Setting.initialRentHousePriceMin,
							FilterActivity.this);
				}

				// high house price
				if (!highHousePriceEditText.getText().toString().equals(""))
				{
					Setting.saveSetting(Setting.keyRentHousePriceMax,
							highHousePriceEditText.getText().toString(),
							FilterActivity.this);
				} else
				{
					Setting.saveSetting(Setting.keyRentHousePriceMax,
							Setting.initialRentHousePriceMax,
							FilterActivity.this);
				}

				// ground type
				String valueGroundType = "";
				if (rent_type_0.isChecked())
				{
					valueGroundType = "0";
				} else
				{
					if (rent_type_1.isChecked())
					{
						valueGroundType = valueGroundType + "a";
					}
					if (rent_type_2.isChecked())
					{
						valueGroundType = valueGroundType + "b";
					}
					if (rent_type_3.isChecked())
					{
						valueGroundType = valueGroundType + "c";
					}
					if (rent_type_4.isChecked())
					{
						valueGroundType = valueGroundType + "d";
					}
					if (rent_type_5.isChecked())
					{
						valueGroundType = valueGroundType + "e";
					}
					if (rent_type_6.isChecked())
					{
						valueGroundType = valueGroundType + "f";
					}
					if (rent_type_7.isChecked())
					{
						valueGroundType = valueGroundType + "g";
					}
					if (rent_type_8.isChecked())
					{
						valueGroundType = valueGroundType + "h";
					}
					if (rent_type_9.isChecked())
					{
						valueGroundType = valueGroundType + "i";
					}
					if (rent_type_10.isChecked())
					{
						valueGroundType = valueGroundType + "j";
					}
					if (rent_type_11.isChecked())
					{
						valueGroundType = valueGroundType + "k";
					}
					if (rent_type_12.isChecked())
					{
						valueGroundType = valueGroundType + "l";
					}
				}

				if (valueGroundType.equals(""))
				{
					Toast.makeText(FilterActivity.this, "請選擇房地形態",
							Toast.LENGTH_SHORT).show();
				} else
				{
					Setting.saveSetting(Setting.keyRentType, valueGroundType,
							FilterActivity.this);
				}

				// min square price
				if (areaMinEditText.getText().toString().equals(""))
				{
					Setting.saveSetting(Setting.keyAreaMin,
							Setting.initialAreaMin, FilterActivity.this);
				} else
				{
					Setting.saveSetting(Setting.keyAreaMin, areaMinEditText
							.getText().toString(), FilterActivity.this);
				}

				// max square price
				if (areaMaxEditText.getText().toString().equals(""))
				{
					Setting.saveSetting(Setting.keyAreaMin,
							Setting.initialAreaMax, FilterActivity.this);
				} else
				{
					Setting.saveSetting(Setting.keyAreaMax, areaMaxEditText
							.getText().toString(), FilterActivity.this);
				}

				finish();
			}
		});

		rent_type_0 = (CheckBox) findViewById(R.id.rent_type_0);
		rent_type_1 = (CheckBox) findViewById(R.id.rent_type_1);
		rent_type_2 = (CheckBox) findViewById(R.id.rent_type_2);
		rent_type_3 = (CheckBox) findViewById(R.id.rent_type_3);
		rent_type_4 = (CheckBox) findViewById(R.id.rent_type_4);
		rent_type_5 = (CheckBox) findViewById(R.id.rent_type_5);
		rent_type_6 = (CheckBox) findViewById(R.id.rent_type_6);
		rent_type_7 = (CheckBox) findViewById(R.id.rent_type_7);
		rent_type_8 = (CheckBox) findViewById(R.id.rent_type_8);
		rent_type_9 = (CheckBox) findViewById(R.id.rent_type_9);
		rent_type_10 = (CheckBox) findViewById(R.id.rent_type_10);
		rent_type_11 = (CheckBox) findViewById(R.id.rent_type_11);
		rent_type_12 = (CheckBox) findViewById(R.id.rent_type_12);

		setCheckAllRentType(rent_type_1);
		setCheckAllRentType(rent_type_2);
		setCheckAllRentType(rent_type_3);
		setCheckAllRentType(rent_type_4);
		setCheckAllRentType(rent_type_5);
		setCheckAllRentType(rent_type_6);
		setCheckAllRentType(rent_type_7);
		setCheckAllRentType(rent_type_8);
		setCheckAllRentType(rent_type_9);
		setCheckAllRentType(rent_type_10);
		setCheckAllRentType(rent_type_11);
		setCheckAllRentType(rent_type_12);

		areaMinEditText = (EditText) findViewById(R.id.area_min);
		areaMaxEditText = (EditText) findViewById(R.id.area_max);

		mActionBar = getActionBar();
		mActionBar.setTitle("看屋高手---搜索設定");
		if (Build.VERSION.SDK_INT >= 14)
		{
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(true);
		}

		minHousePriceString = Setting.getSetting(Setting.keyRentHousePriceMin,
				this);
		maxHousePriceString = Setting.getSetting(Setting.keyRentHousePriceMax,
				this);

		stringRentType = Setting.getSetting(Setting.keyRentType, this);
		areaMinString = Setting.getSetting(Setting.keyAreaMin, this);
		areaMaxString = Setting.getSetting(Setting.keyAreaMax, this);

		if (minHousePriceString.equals("0"))
		{
			// do nothing
		} else
		{
			lowHousePriceEditText.setText(minHousePriceString);
		}

		if (maxHousePriceString.equals("0"))
		{
			// do nothing
		} else
		{
			highHousePriceEditText.setText(maxHousePriceString);
		}

		if (stringRentType.equals("0"))
		{
			rent_type_0.setChecked(true);
			rent_type_1.setChecked(true);
			rent_type_2.setChecked(true);
			rent_type_3.setChecked(true);
			rent_type_4.setChecked(true);
			rent_type_5.setChecked(true);
			rent_type_6.setChecked(true);
			rent_type_7.setChecked(true);
			rent_type_8.setChecked(true);
			rent_type_9.setChecked(true);
			rent_type_10.setChecked(true);
			rent_type_11.setChecked(true);
			rent_type_12.setChecked(true);

		} else
		{

			rent_type_0.setChecked(false);
			setChecked(stringRentType, rent_type_1, "a");
			setChecked(stringRentType, rent_type_2, "b");
			setChecked(stringRentType, rent_type_3, "c");
			setChecked(stringRentType, rent_type_4, "d");
			setChecked(stringRentType, rent_type_5, "e");
			setChecked(stringRentType, rent_type_6, "f");
			setChecked(stringRentType, rent_type_7, "g");
			setChecked(stringRentType, rent_type_8, "h");
			setChecked(stringRentType, rent_type_9, "i");
			setChecked(stringRentType, rent_type_10, "j");
			setChecked(stringRentType, rent_type_11, "k");
			setChecked(stringRentType, rent_type_12, "l");
		}

		rent_type_0.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (rent_type_0.isChecked())
				{
					rent_type_1.setChecked(true);
					rent_type_2.setChecked(true);
					rent_type_3.setChecked(true);
					rent_type_4.setChecked(true);
					rent_type_5.setChecked(true);
					rent_type_6.setChecked(true);
					rent_type_7.setChecked(true);
					rent_type_8.setChecked(true);
					rent_type_9.setChecked(true);
					rent_type_10.setChecked(true);
					rent_type_11.setChecked(true);
					rent_type_12.setChecked(true);
				} else
				{
					rent_type_1.setChecked(false);
					rent_type_2.setChecked(false);
					rent_type_3.setChecked(false);
					rent_type_4.setChecked(false);
					rent_type_5.setChecked(false);
					rent_type_6.setChecked(false);
					rent_type_7.setChecked(false);
					rent_type_8.setChecked(false);
					rent_type_9.setChecked(false);
					rent_type_10.setChecked(false);
					rent_type_11.setChecked(false);
					rent_type_12.setChecked(false);
				}

			}
		});

		if (areaMinString.equals("0"))
		{
			// do nothing
		} else
		{
			areaMinEditText.setText(areaMinString);
		}

		if (areaMaxString.equals("0"))
		{
			// do nothing
		} else
		{
			areaMaxEditText.setText(areaMaxString);
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
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setCheckAllRentType(CheckBox rent_check_box)
	{
		rent_check_box.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked)
			{
				if (isChecked)
				{
					if (rent_type_1.isChecked() && rent_type_2.isChecked()
							&& rent_type_3.isChecked()
							&& rent_type_4.isChecked()
							&& rent_type_5.isChecked()
							&& rent_type_6.isChecked()
							&& rent_type_7.isChecked()
							&& rent_type_8.isChecked()
							&& rent_type_9.isChecked()
							&& rent_type_10.isChecked()
							&& rent_type_11.isChecked()
							&& rent_type_12.isChecked())
					{
						rent_type_0.setChecked(true);
					}
				} else
				{
					rent_type_0.setChecked(false);
				}

			}
		});

	}

	private void setChecked(String content, CheckBox theCheckBox, String theKey)
	{
		if (content.indexOf(theKey) != -1)
		{
			theCheckBox.setChecked(true);
		} else
		{
			theCheckBox.setChecked(false);
		}
	}

	/**
	 * Hides the soft keyboard
	 */
	public void hideSoftKeyboard()
	{
		if (getCurrentFocus() != null)
		{
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}
		// InputMethodManager imm = (InputMethodManager)
		// getSystemService(INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(editHousePrice.getWindowToken(), 0);
	}

	/**
	 * Shows the soft keyboard
	 */
	public void showSoftKeyboard(View view)
	{
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		view.requestFocus();
		inputMethodManager.showSoftInput(view, 0);
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		// hideSoftKeyboard();
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		// MainActivity.isReSearch = true;
	}

	private void CallAds()
	{
		boolean isGivenStar = Setting.getBooleanSetting(Setting.KeyGiveStar,
				FilterActivity.this);

		if (!isGivenStar)
		{
			adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
			final AdRequest adReq = new AdRequest.Builder().build();

			// 12-18 17:01:12.438: I/Ads(8252): Use
			// AdRequest.Builder.addTestDevice("A25819A64B56C65500038B8A9E7C19DD")
			// to get test ads on this device.

			adMobAdView = new AdView(FilterActivity.this);
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
		 EasyTracker.getInstance(this).activityStart(this); // Add this
		// method.
	}

	@Override
	public void onStop()
	{
		super.onStop();
		 EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

}
