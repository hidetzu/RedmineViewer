
package com.redmine.database;

import com.redmine.data.Acount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AcountDatabaseHelper extends SQLiteOpenHelper
	implements AcountDatabase {

	private static final String DB_FILE_NAME="acount.db";
	private static final int DB_VERSION=1;

	private static final String DB_TABLE="acountTable";
	private static final String DB_COLUMN_ID     ="ID";
	private static final String DB_COLUMN_SERVERURL ="SERVERURL";
	private static final String DB_COLUMN_NAME ="NAME";
	private static final String DB_COLUMN_PASSWORD ="PASSWORD";

	private static AcountDatabaseHelper sSingleton = null;
	
	private String[] mColumns = new String[] {
			DB_COLUMN_ID,
			DB_COLUMN_SERVERURL,
			DB_COLUMN_NAME,
			DB_COLUMN_PASSWORD
			};
	
	public static AcountDatabaseHelper getInstance(Context context) {
		if( sSingleton == null ) {
			sSingleton = new AcountDatabaseHelper(context);
		}
		return sSingleton;
	}
	

	private AcountDatabaseHelper(Context context) {
		super(context, DB_FILE_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "";
		sql ="CREATE TABLE ACOUNTTABLE";
		sql += "(";
		sql +=  DB_COLUMN_ID+ " KEY INTEGER PRIMARY KEY,";
		sql += DB_COLUMN_SERVERURL + " TEXT NOT NULL,";
		sql += DB_COLUMN_NAME + " TEXT NOT NULL,";
		sql += DB_COLUMN_PASSWORD + " TEXT NOT NULL";
		sql += ")";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// none
	}

	public void setAcount(int id, String serverURL, String name, String password) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = getCursor(id);
		ContentValues values = new ContentValues();

		if( cursor.getCount() > 0 ) {
 			values.put(DB_COLUMN_ID, id);
			values.put(DB_COLUMN_SERVERURL, serverURL);
			values.put(DB_COLUMN_NAME, name);
			values.put(DB_COLUMN_PASSWORD, password);
			db.update(DB_TABLE, values, DB_COLUMN_ID +" = " + id, null);
		} else {
			values.put(DB_COLUMN_ID, id);
			values.put(DB_COLUMN_SERVERURL, serverURL);
			values.put(DB_COLUMN_NAME, name);
			values.put(DB_COLUMN_PASSWORD, password);
			db.insert(DB_TABLE, null, values);
		}
		db.close();
	}

	private Cursor getCursor(int id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(DB_TABLE /* table */,
				mColumns,
				DB_COLUMN_ID + "=?"/* selection */,
				new String[] {
					String.valueOf(id)
				}/* selectionArgs */,
				null/* groupBy */,
				null/* having */,
				null/* orderBy */);
		return cursor;
	}
	
	
	public Acount getAcount(int id) {
		SQLiteDatabase db = getReadableDatabase();
		Acount acount = null;
		Cursor cursor = getCursor(id);
		
		cursor.moveToFirst();
		if(cursor.getCount() > 0) {
			acount = new Acount(cursor.getString(1), cursor.getString(2), cursor.getString(3));
		}
		cursor.close();
		db.close();

		return acount;
	}
}
