package com.kosbrother.housefinder.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kosbrother.housefinder.R;

/**
 * Database helper which creates and upgrades the database and provides the DAOs
 * for the app.
 * 
 * @author kevingalligan
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper
{

	/************************************************
	 * Suggested Copy/Paste code. Everything from here to the done block.
	 ************************************************/

	private static final String DATABASE_NAME = "housefinder.db";
	private static final int DATABASE_VERSION = 4;

	private Dao<OrmHouse, Integer> HouseDao;
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION,
				R.raw.ormlite_config);
//		 super(context, DATABASE_NAME, null, DATABASE_VERSION, 0);
	}

	/************************************************
	 * Suggested Copy/Paste Done
	 ************************************************/

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource)
	{
		try
		{
			TableUtils.createTable(connectionSource, OrmHouse.class);
		} catch (SQLException e)
		{
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases",
					e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int oldVer, int newVer)
	{
		try
		{
			TableUtils.dropTable(connectionSource, OrmHouse.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e)
		{
			Log.e(DatabaseHelper.class.getName(),
					"Unable to upgrade database from version " + oldVer
							+ " to new " + newVer, e);
		}
	}

	public Dao<OrmHouse, Integer> getOrmHouseDao() throws SQLException
	{
		if (HouseDao == null)
		{
			HouseDao = getDao(OrmHouse.class);
		}
		return HouseDao;
	}

}
