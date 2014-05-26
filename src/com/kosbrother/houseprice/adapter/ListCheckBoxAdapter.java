package com.kosbrother.houseprice.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.kosbrother.housefinder.FilterNewActivity;
import com.kosbrother.housefinder.R;
import com.kosbrother.housefinder.Setting;

public class ListCheckBoxAdapter extends BaseAdapter
{
	private FilterNewActivity activity;
	private List<String> mList;
	private int typeId;
	private String[] keyTypeArray = { "a", "b", "c", "d", "e", "f", "g", "h",
			"i", "j", "k", "l", "m" };

	private static LayoutInflater inflater = null;

	public ListCheckBoxAdapter(FilterNewActivity a, List<String> list, int typeId)
	{
		activity = a;
		mList = list;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.typeId = typeId;
	}

	public int getCount()
	{
		return mList.size();
	}

	public Object getItem(int position)
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		// View vi = convertView;
		// if (convertView == null)
		// {
		// vi = inflater.inflate(R.layout.list_item_checkbox, null);
		// }

		View vi = inflater.inflate(R.layout.list_item_checkbox, null);
		String typeString = "";
		if (typeId == 1)
		{
			typeString = Setting.getSetting(Setting.keySaleType, activity);
		} else
		{
			typeString = Setting.getSetting(Setting.keyRentType, activity);
		}

		final CheckBox mCheckBox = (CheckBox) vi
				.findViewById(R.id.list_dialog_checkbox);
		final TextView mTextView = (TextView) vi
				.findViewById(R.id.list_dialog_textview);

		mTextView.setText(mList.get(position).toString());

		if (typeString.equals("0"))
		{
			mCheckBox.setChecked(true);
		} else
		{

			if (typeString.indexOf(keyTypeArray[position]) != -1)
			{
				mCheckBox.setChecked(true);
			} else
			{
				mCheckBox.setChecked(false);
			}
		}

		mCheckBox.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String typeString = "";
				if (typeId == 1)
				{
					typeString = Setting.getSetting(Setting.keySaleType,
							activity);
					if (mCheckBox.isChecked())
					{
						if (typeString.equals("0"))
						{
							typeString = keyTypeArray[position];
						} else
						{
							typeString = typeString + keyTypeArray[position];
						}

					} else
					{
						if (typeString.equals("0"))
						{
							typeString = "";
							for (int i = 0; i < mList.size(); i++)
							{
								typeString = typeString + keyTypeArray[i];
							}
						}
						typeString = typeString.replaceAll(
								keyTypeArray[position], "");
					}

					if (typeString.equals(""))
					{
						typeString = "0";
					} else if (typeString.length() == mList.size())
					{
						typeString = "0";
					}
//					Toast.makeText(activity, typeString, Toast.LENGTH_SHORT)
//							.show();
					Setting.saveSetting(Setting.keySaleType, typeString,
							activity);
					activity.setTypeText(typeId);
				} else
				{
					typeString = Setting.getSetting(Setting.keyRentType,
							activity);
					if (mCheckBox.isChecked())
					{
						if (typeString.equals("0"))
						{
							typeString = keyTypeArray[position];
						} else
						{
							typeString = typeString + keyTypeArray[position];
						}

					} else
					{
						if (typeString.equals("0"))
						{
							typeString = "";
							for (int i = 0; i < mList.size(); i++)
							{
								typeString = typeString + keyTypeArray[i];
							}
						}
						typeString = typeString.replaceAll(
								keyTypeArray[position], "");
					}

					if (typeString.equals(""))
					{
						typeString = "0";
					} else if (typeString.length() == mList.size())
					{
						typeString = "0";
					}
//					Toast.makeText(activity, typeString, Toast.LENGTH_SHORT)
//							.show();
					Setting.saveSetting(Setting.keyRentType, typeString,
							activity);
					activity.setTypeText(typeId);
				}

			}
		});

		return vi;
	}
}