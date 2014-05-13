package com.kosbrother.housefinder.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kosbrother.housefinder.R;

public class Report
{

	static Activity mActivity;
	private static AlertDialog dialog;

	public static void createReportDialog(Activity act, String novelName,
			String article)
	{
		mActivity = act;
		showDialog(novelName, article);
	}

	public static void createReportDialog(Activity act, String novelName,
			String article, String problem)
	{
		mActivity = act;
		showDialog(novelName, article, problem);
	}

	private static void showDialog(String novelName, String article,
			String problem)
	{
		LayoutInflater inflater = mActivity.getLayoutInflater();
		LinearLayout recomendLayout = (LinearLayout) inflater.inflate(
				R.layout.dialog_report, null);

		final EditText novelNameEditText = (EditText) recomendLayout
				.findViewById(R.id.house_name);
		final EditText problemEditText = (EditText) recomendLayout
				.findViewById(R.id.problem);

		novelNameEditText.setText(novelName);
		problemEditText.setText(problem);
		problemEditText.requestFocus();

		Builder a = new AlertDialog.Builder(mActivity).setTitle("物件問題回報")
				.setIcon(R.drawable.icon_info2)
				.setPositiveButton("回報", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						if (problemEditText.getText().toString().equals(""))
						{
							Toast.makeText(mActivity, "請先填寫問題內容",
									Toast.LENGTH_LONG).show();
							createReportDialog(mActivity, novelNameEditText
									.getText().toString(), problemEditText
									.getText().toString());
						} else
						{
							final Intent emailIntent2 = new Intent(
									android.content.Intent.ACTION_SEND);
							emailIntent2.setType("plain/text");
							emailIntent2
									.putExtra(
											android.content.Intent.EXTRA_EMAIL,
											new String[] { mActivity
													.getResources()
													.getString(
															R.string.respond_mail_address) });
							emailIntent2.putExtra(
									android.content.Intent.EXTRA_SUBJECT,
									"找屋高手問題回報");
							emailIntent2.putExtra(
									android.content.Intent.EXTRA_TEXT,
									"物件回報:"
											+ novelNameEditText.getText()
													.toString()
											+ "\n"
											+ "問題內容:"
											+ "\n"
											+ problemEditText.getText()
													.toString());
							mActivity.startActivity(Intent.createChooser(
									emailIntent2, "Send mail..."));

						}
					}
				}).setNegativeButton("取消", null);
		a.setView(recomendLayout);
		dialog = a.create();
		dialog.show();
	}

	private static void showDialog(String novelName, String article)
	{
		showDialog(novelName, article, "");
	}
}
