package com.kosbrother.houseprice.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kosbrother.housefinder.Datas;
import com.kosbrother.housefinder.ListActivity;
import com.kosbrother.housefinder.R;
import com.kosbrother.houseprice.adapter.ListRentHouseAdapter;

public class RentListFragment extends Fragment
{
	private static ListActivity mActivity;
	private ListRentHouseAdapter mAdapter;

	public static RentListFragment newInstance(ListActivity theListActivity)
	{
		RentListFragment f = new RentListFragment();
		mActivity = theListActivity;
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_list, container, false);
		ListView mListView = (ListView) v.findViewById(R.id.list_houses);

		LinearLayout mNoDataLayout = (LinearLayout) v.findViewById(R.id.fragment_no_data_layout);

		if (Datas.mRentHouses.size() == 0)
		{
			mNoDataLayout.setVisibility(View.VISIBLE);
		} else
		{
			mAdapter = new ListRentHouseAdapter(mActivity, Datas.mRentHouses);
			mListView.setAdapter(mAdapter);
		}

		return v;
	}

	public void nofifyListDataChanged()
	{
		mAdapter.notifyDataSetChanged();
	}
}
