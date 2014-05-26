package com.kosbrother.housefinder;

import java.util.ArrayList;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosbrother.houseprice.adapter.ListCheckBoxAdapter;
import com.kosbrother.houseprice.api.HouseApi;
import com.kosbrother.houseprice.entity.GroundType;
import com.kosbrother.houseprice.entity.RentType;

public class FilterNewActivity extends FragmentActivity
{

	private ActionBar mActionBar;

	private LinearLayout filterMoreLayout;
	private LinearLayout filterLinearSale;
	private LinearLayout filterLinearRent;
	private LinearLayout filterLinearSaleType;
	private LinearLayout filterLinearRentType;
	private LinearLayout filterLinearArea;
	private LinearLayout filterLinearAge;
	private CheckBox rentCheckBox;
	private CheckBox saleCheckBox;
	private TextView moreTextView;
	private TextView salePriceTextView;
	private TextView rentPriceTextView;
	private TextView saleTypeTextView;
	private TextView rentTypeTextView;
	private TextView areaTextView;
	private TextView ageTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter_new);

		mActionBar = getActionBar();
		mActionBar.setTitle("看屋高手---搜索設定");
		if (Build.VERSION.SDK_INT >= 14)
		{
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(true);
		}

		filterMoreLayout = (LinearLayout) findViewById(R.id.filter_linear_more);
		filterLinearSale = (LinearLayout) findViewById(R.id.filter_linear_sale);
		filterLinearRent = (LinearLayout) findViewById(R.id.filter_linear_rent);
		filterLinearSaleType = (LinearLayout) findViewById(R.id.filter_linear_sale_type);
		filterLinearRentType = (LinearLayout) findViewById(R.id.filter_linear_rent_type);
		filterLinearArea = (LinearLayout) findViewById(R.id.filter_linear_area);
		filterLinearAge = (LinearLayout) findViewById(R.id.filter_linear_age);
		salePriceTextView = (TextView) findViewById(R.id.filter_text_sale_price);
		rentPriceTextView = (TextView) findViewById(R.id.filter_text_rent_price);
		saleTypeTextView = (TextView) findViewById(R.id.filter_text_sale_type);
		rentTypeTextView = (TextView) findViewById(R.id.filter_text_rent_type);
		areaTextView = (TextView) findViewById(R.id.filter_text_area);
		ageTextView = (TextView) findViewById(R.id.filter_text_age);

		String hp_min = Setting.getSetting(Setting.keyHousePriceMin,
				FilterNewActivity.this);
		String hp_max = Setting.getSetting(Setting.keyHousePriceMax,
				FilterNewActivity.this);
		if (hp_min.equals("0") && hp_max.equals("0"))
		{
			salePriceTextView.setText("任何出售價錢");
		} else
		{
			if (hp_max.equals("0"))
			{
				setRangeText(salePriceTextView, "$" + hp_min + "萬", "$" + "Max"
						+ "萬");
			} else
			{
				setRangeText(salePriceTextView, "$" + hp_min + "萬", "$"
						+ hp_max + "萬");
			}
		}

		String rpMinString = Setting.getSetting(Setting.keyRentHousePriceMin,
				FilterNewActivity.this);
		String rpMaxString = Setting.getSetting(Setting.keyRentHousePriceMax,
				FilterNewActivity.this);
		if (rpMinString.equals("0") && rpMaxString.equals("0"))
		{
			rentPriceTextView.setText("任何出租價錢");
		} else
		{
			if (rpMaxString.equals("0"))
			{
				setRangeText(rentPriceTextView, "$" + rpMinString + "元", "$"
						+ "Max" + "元");
			} else
			{
				setRangeText(rentPriceTextView, "$" + rpMinString + "元", "$"
						+ rpMaxString + "元");
			}
		}

		String sale_type = Setting.getSetting(Setting.keySaleType,
				FilterNewActivity.this);
		if (sale_type.equals("0"))
		{
			saleTypeTextView.setText("任何出售型態");
		} else
		{
			saleTypeTextView.setText(setSaleTypeText(sale_type));
		}

		String rent_type = Setting.getSetting(Setting.keyRentType,
				FilterNewActivity.this);
		if (rent_type.equals("0"))
		{
			rentTypeTextView.setText("任何出租型態");
		} else
		{
			rentTypeTextView.setText(setRentTypeText(rent_type));
		}

		String areaMinString = Setting.getSetting(Setting.keyAreaMin,
				FilterNewActivity.this);
		String areaMaxString = Setting.getSetting(Setting.keyAreaMax,
				FilterNewActivity.this);
		if (areaMinString.equals("0") && areaMaxString.equals("0"))
		{
			areaTextView.setText("任何坪數");
		} else
		{
			if (areaMaxString.equals("0"))
			{
				setRangeText(areaTextView, areaMinString + "坪", "Max" + "坪");
			} else
			{
				setRangeText(areaTextView, areaMinString + "坪", areaMaxString
						+ "坪");
			}

		}

		String age_min = Setting.getSetting(Setting.keyAgeMin,
				FilterNewActivity.this);
		String age_max = Setting.getSetting(Setting.keyAgeMax,
				FilterNewActivity.this);
		if (age_min.equals("0") && age_max.equals("0"))
		{
			ageTextView.setText("任何屋齡");
		} else
		{
			if (age_max.equals("0"))
			{
				setRangeText(ageTextView, age_min + "年", "Max" + "年");
			} else
			{
				setRangeText(ageTextView, age_min + "年", age_max + "年");
			}
		}

		boolean is_rent_show = Setting.getBooleanSetting(Setting.KeyIsShowRent,
				FilterNewActivity.this);
		rentCheckBox = (CheckBox) findViewById(R.id.filter_checkbox_rent);
		if (is_rent_show)
		{
			rentCheckBox.setChecked(true);
		} else
		{
			rentCheckBox.setChecked(false);
		}

		boolean is_sale_show = Setting.getBooleanSetting(Setting.KeyIsShowSale,
				FilterNewActivity.this);
		saleCheckBox = (CheckBox) findViewById(R.id.filter_checkbox_sale);
		if (is_sale_show)
		{
			saleCheckBox.setChecked(true);
		} else
		{
			saleCheckBox.setChecked(false);
		}

		filterLinearSale.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showEditDialog("選擇出售價格", "0", "Max", "萬元", salePriceTextView,
						Setting.keyHousePriceMin, Setting.keyHousePriceMax,
						"$", "萬", "任何出售價錢");
			}
		});

		filterLinearRent.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showEditDialog("選擇出租價格", "0", "Max", "元", rentPriceTextView,
						Setting.keyRentHousePriceMin,
						Setting.keyRentHousePriceMax, "$", "元", "任何出租價格");
			}
		});

		filterLinearSaleType.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showListDialog("選擇出售形態", 1);

			}
		});

		filterLinearRentType.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showListDialog("選擇出租形態", 2);
			}
		});

		filterLinearArea.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showEditDialog("選擇坪數", "0", "Max", "坪", areaTextView,
						Setting.keyAreaMin, Setting.keyAreaMax, "", "坪", "任何坪數");
			}
		});

		filterLinearAge.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showEditDialog("選擇屋齡", "0", "Max", "年", ageTextView,
						Setting.keyAgeMin, Setting.keyAgeMax, "", "年", "任何屋齡");
			}
		});

		moreTextView = (TextView) findViewById(R.id.filter_more);
		moreTextView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (filterMoreLayout.getVisibility() == View.VISIBLE)
				{
					filterMoreLayout.setVisibility(View.GONE);
					moreTextView.setText("更多選項");
				} else
				{
					filterMoreLayout.setVisibility(View.VISIBLE);
					moreTextView.setText("較少選項");
				}

			}
		});

	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		saveRentSaleBoolean();
		MainActivity.isReSearch = true;
		MainActivity.isBackFromFilter = true;
	}

	private void saveRentSaleBoolean()
	{
		Setting.saveBooleanSetting(Setting.KeyIsShowRent,
				rentCheckBox.isChecked(), this);
		Setting.saveBooleanSetting(Setting.KeyIsShowSale,
				saleCheckBox.isChecked(), this);
	}

	private void setRangeText(TextView textView, String minString,
			String highString)
	{
		textView.setText(minString + "~" + highString);
	}

	public void setTypeText(int typeId)
	{
		if (typeId == 1)
		{
			String sale_type = Setting
					.getSetting(Setting.keySaleType, FilterNewActivity.this);
			if (sale_type.equals("0"))
			{
				saleTypeTextView.setText("任何出售型態");
			} else
			{
				saleTypeTextView.setText(setSaleTypeText(sale_type));
			}

		} else if (typeId == 2)
		{

			String rent_type = Setting
					.getSetting(Setting.keyRentType, FilterNewActivity.this);
			if (rent_type.equals("0"))
			{
				rentTypeTextView.setText("任何出租型態");
			} else
			{
				rentTypeTextView.setText(setRentTypeText(rent_type));
			}
		}
	}

	private String setRentTypeText(String rent_type)
	{
		String rentString = "";
		if (rent_type.indexOf("a") != -1)
		{
			rentString = rentString + "整層住家,";
		}
		if (rent_type.indexOf("b") != -1)
		{
			rentString = rentString + "獨立套房,";
		}
		if (rent_type.indexOf("c") != -1)
		{
			rentString = rentString + "分租套房,";
		}
		if (rent_type.indexOf("d") != -1)
		{
			rentString = rentString + "雅房,";
		}
		if (rent_type.indexOf("e") != -1)
		{
			rentString = rentString + "店面,";
		}
		if (rent_type.indexOf("f") != -1)
		{
			rentString = rentString + "攤位,";
		}
		if (rent_type.indexOf("g") != -1)
		{
			rentString = rentString + "辦公,";
		}
		if (rent_type.indexOf("h") != -1)
		{
			rentString = rentString + "住辦,";
		}
		if (rent_type.indexOf("i") != -1)
		{
			rentString = rentString + "廠房,";
		}
		if (rent_type.indexOf("j") != -1)
		{
			rentString = rentString + "車位,";
		}
		if (rent_type.indexOf("k") != -1)
		{
			rentString = rentString + "土地,";
		}
		if (rent_type.indexOf("l") != -1)
		{
			rentString = rentString + "場地,";
		}
		rentString = rentString.substring(0, rentString.length() - 1);
		return rentString;
	}

	private String setSaleTypeText(String sale_type)
	{
		String saleString = "";
		if (sale_type.indexOf("a") != -1)
		{
			saleString = saleString + "住宅,";
		}

		if (sale_type.indexOf("b") != -1)
		{
			saleString = saleString + "套房,";
		}

		if (sale_type.indexOf("c") != -1)
		{
			saleString = saleString + "店面,";
		}

		if (sale_type.indexOf("d") != -1)
		{
			saleString = saleString + "攤位,";
		}

		if (sale_type.indexOf("e") != -1)
		{
			saleString = saleString + "辦公,";
		}

		if (sale_type.indexOf("f") != -1)
		{
			saleString = saleString + "住辦,";
		}

		if (sale_type.indexOf("g") != -1)
		{
			saleString = saleString + "廠房,";
		}

		if (sale_type.indexOf("h") != -1)
		{
			saleString = saleString + "車位,";
		}

		if (sale_type.indexOf("i") != -1)
		{
			saleString = saleString + "土地,";
		}

		if (sale_type.indexOf("j") != -1)
		{
			saleString = saleString + "其他,";
		}
		saleString = saleString.substring(0, saleString.length() - 1);
		return saleString;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			saveRentSaleBoolean();
			MainActivity.isReSearch = true;
			MainActivity.isBackFromFilter = true;
			finish();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// typeId 1 for sale, 2 for rent
	private void showListDialog(String title, int typeId)
	{
		ListView mListView = new ListView(FilterNewActivity.this);
		if (typeId == 1)
		{
			final ArrayList<GroundType> mTypes = HouseApi.getGroundType();
			final ArrayList<String> ListStr = new ArrayList<String>();
			for (int i = 0; i < mTypes.size(); i++)
			{
				ListStr.add(mTypes.get(i).name);
			}
			ListCheckBoxAdapter mAdapter = new ListCheckBoxAdapter(
					FilterNewActivity.this, ListStr, typeId);
			mListView.setAdapter(mAdapter);
		} else
		{
			final ArrayList<RentType> mTypes = HouseApi.getRentTypes();
			final ArrayList<String> ListStr = new ArrayList<String>();
			for (int i = 0; i < mTypes.size(); i++)
			{
				ListStr.add(mTypes.get(i).name);
			}
			ListCheckBoxAdapter mAdapter = new ListCheckBoxAdapter(
					FilterNewActivity.this, ListStr, typeId);
			mListView.setAdapter(mAdapter);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				FilterNewActivity.this);
		builder.setTitle(title);
		builder.setView(mListView);
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void showEditDialog(String title, String lowHint, String highHint,
			String dialogUnit, final TextView textView, final String keyMin,
			final String keyMax, final String forwardMark,
			final String backwardMark, final String initialString)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(
				FilterNewActivity.this);
		LayoutInflater inflater = FilterNewActivity.this.getLayoutInflater();

		LinearLayout vLayout = (LinearLayout) inflater.inflate(
				R.layout.dialog_two_edit, null);
		final EditText lowEditText = (EditText) vLayout
				.findViewById(R.id.dialog_low_edit);
		final EditText highEditText = (EditText) vLayout
				.findViewById(R.id.dialog_high_edit);
		TextView uniTextView = (TextView) vLayout
				.findViewById(R.id.dialog_unit);
		lowEditText.setHint(lowHint);
		highEditText.setHint(highHint);
		uniTextView.setText(dialogUnit);

		String lowString = Setting.getSetting(keyMin, FilterNewActivity.this);
		String highString = Setting.getSetting(keyMax, FilterNewActivity.this);
		if (!lowString.equals("0") && !highString.equals("0"))
		{
			lowEditText.setText(lowString);
			highEditText.setText(highString);
		}

		builder.setView(vLayout)
				.setTitle(title)
				.setPositiveButton("確定", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						String lowString = lowEditText.getText().toString();
						String highString = highEditText.getText().toString();
						if (lowString.equals(""))
						{
							lowString = "0";
						}
						if (highString.equals(""))
						{
							highString = "0";
						}

						boolean isLowBiggerHigh = false;
						if (highString.equals("0"))
						{
							isLowBiggerHigh = false;
						} else
						{
							isLowBiggerHigh = Double.valueOf(lowString) > Double
									.valueOf(highString);
						}

						if (isLowBiggerHigh)
						{
							Toast.makeText(FilterNewActivity.this, "區間設定錯誤",
									Toast.LENGTH_SHORT).show();
						} else
						{

							if (lowString.equals("0") && highString.equals("0"))
							{
								textView.setText(initialString);
								Setting.saveSetting(keyMin, "0",
										FilterNewActivity.this);
								Setting.saveSetting(keyMax, "0",
										FilterNewActivity.this);
							} else
							{
								if (highString.equals("0"))
								{
									setRangeText(textView, forwardMark
											+ lowString + backwardMark,
											forwardMark + "Max" + backwardMark);
								} else
								{
									setRangeText(textView, forwardMark
											+ lowString + backwardMark,
											forwardMark + highString
													+ backwardMark);
								}
								Setting.saveSetting(keyMin, lowString,
										FilterNewActivity.this);
								Setting.saveSetting(keyMax, highString,
										FilterNewActivity.this);
							}
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{

					}
				}).show();
	}
}
