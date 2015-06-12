package com.lv.testlv.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDB extends SQLiteOpenHelper {
	public final static String DATABASE_NAME = "LeanCloud.db";
	public final static int DATABASE_VERSION = 1;

	public ToDoDB(Context context) {
		super(context, "LeanCloud", null, DATABASE_VERSION);
	}

	private static ToDoDB mInstance;

	public synchronized static ToDoDB getInstance(Context context) {

		if (mInstance == null) {
			mInstance = new ToDoDB(context);
		}
		return mInstance;
	};

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String createLeanCloudSQL = "CREATE TABLE "+LeanCloudDB.LEANCLOUD+" ("
				+ "[id] INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ LeanCloudDB.CONVERSATION_ID+" VARCHAR(50),"
				+ LeanCloudDB.CONVERSATION_TYPE+" VARCHAR(50),"
				+ LeanCloudDB.CONVERSATION_NAME+" VARCHAR(50),"
				+ LeanCloudDB.CONVERSATION_DATE+" VARCHAR(50),"
				+ LeanCloudDB.CONVERSATION_CONTENT+" VARCHAR(50),"
				+ LeanCloudDB.CONVERSATION_SEND_ID+" VARCHAR(50),"
				+ LeanCloudDB.LOGIN_ID+" VARCHAR(50)"
				+ ")";
		db.execSQL(createLeanCloudSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.beginTransaction();
//        try {
//        	db.execSQL("ALTER TABLE "+LeanCloudDB.LEANCLOUD+" ADD COLUMN addnew VARCHAR(50) NOT NULL default testnew");
//            db.setTransactionSuccessful();
//        } catch (Throwable ex) {
//            Log.e("", ex.getMessage(), ex);
//        } finally {
//            db.endTransaction();
//        }
	}

	// 查询
	public Cursor query(String sql, String[] args) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, args);
		return cursor;
	}

	// 插入 修改 删除
	public void execSQL(String sql, Object[] argsObjects) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql, argsObjects);
	}
	
}
