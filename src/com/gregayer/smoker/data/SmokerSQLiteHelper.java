package com.gregayer.smoker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SmokerSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_SMOKER = "smoker";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "date";
	
	private static final String DATABASE_NAME = "smoker.db";
	private static final int DATABASE_VERSION = 2;
	
	// Create statement
	private static final String DATABASE_CREATE = "create table "
		+ TABLE_SMOKER + "(" + COLUMN_ID
		+ " integer primary key autoincrement, " + COLUMN_DATE
		+ " text not null);";
	
	public SmokerSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SmokerSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SMOKER);
		onCreate(db);
	}
}
