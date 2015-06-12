package com.lv.testlv.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lv.testlv.model.LeanCloudModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库方法类
 */
public class ParamDAO {

	private ToDoDB database;

	public ParamDAO() {
	}

	public void openDatabase(Context context) {
		database = new ToDoDB(context);
	}

	public void closeDatabase() {
		database.close();
	}

	public void addConversation(String loginId, List<LeanCloudModel> models) {

		try {
			String deleteTable = "delete from " + LeanCloudDB.LEANCLOUD + " where "
					+ LeanCloudDB.LOGIN_ID + " = " + loginId;
			database.execSQL(deleteTable, new Object[] {});

			String sql = "insert into " + LeanCloudDB.LEANCLOUD + "("
					+ LeanCloudDB.CONVERSATION_ID + ", " + LeanCloudDB.CONVERSATION_TYPE + ", "
					+ LeanCloudDB.CONVERSATION_NAME + ", " + LeanCloudDB.CONVERSATION_DATE + ", "
					+ LeanCloudDB.CONVERSATION_CONTENT + ", " + LeanCloudDB.CONVERSATION_SEND_ID + ", "
					+ LeanCloudDB.LOGIN_ID + ")" + " values (?,?,?,?,?,?,?)";
			for (int i = 0; i < models.size(); i++) {
				Object[] args = new Object[] { models.get(i).getConversation_id(),
						models.get(i).getConversation_type(),
						models.get(i).getConversation_name(),
						models.get(i).getConversation_date(),
						models.get(i).getConversation_content(),
						models.get(i).getConversation_send_id(),
						loginId };
				database.execSQL(sql, args);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<LeanCloudModel> getAllConversations(String LOGIN_ID) {
		List<LeanCloudModel> models = new ArrayList<LeanCloudModel>();
		try {
			String selectID = "select * from " + LeanCloudDB.LEANCLOUD + " where "
					+ LeanCloudDB.LOGIN_ID + " = " + LOGIN_ID;
			Cursor mCursor = database.query(selectID, null);
			mCursor.moveToFirst();
			while (!mCursor.isAfterLast()) {
				LeanCloudModel model = new LeanCloudModel();
				model.setConversation_id(mCursor.getString(mCursor
						.getColumnIndex(LeanCloudDB.CONVERSATION_ID)));
				model.setConversation_type(mCursor.getString(mCursor
						.getColumnIndex(LeanCloudDB.CONVERSATION_TYPE)));
				model.setConversation_name(mCursor.getString(mCursor
						.getColumnIndex(LeanCloudDB.CONVERSATION_NAME)));
				model.setConversation_date(mCursor.getString(mCursor
						.getColumnIndex(LeanCloudDB.CONVERSATION_DATE)));
				model.setConversation_content(mCursor.getString(mCursor.getColumnIndex(LeanCloudDB.CONVERSATION_CONTENT)));
				model.setConversation_send_id(mCursor.getString(mCursor.getColumnIndex(LeanCloudDB.CONVERSATION_SEND_ID)));
				models.add(model);
				mCursor.moveToNext();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return models;
	}

	public void delAllConversations(String LOGIN_ID) {
		String deleteID = "delete from " + LeanCloudDB.LEANCLOUD + " where "
				+ LeanCloudDB.LOGIN_ID + " = " + LOGIN_ID;
		Log.e("", "deleteID >>> " + deleteID);
		database.execSQL(deleteID, new Object[] {});
	}
	
	public void addColumn(String table, String columnName, String columnValue){
		SQLiteDatabase db = database.getWritableDatabase();
		db.beginTransaction();
        try {
        	db.execSQL("ALTER TABLE "+table+" ADD COLUMN "+columnName+" VARCHAR(50) NOT NULL default "+columnValue);
//        	db.execSQL("ALTER TABLE "+table+" ADD COLUMN "+columnName+" VARCHAR(50) ");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
	}
	
	public void alterColumnDefaultValue(String table, String columnName, String columnValue){
		SQLiteDatabase db = database.getWritableDatabase();
		db.beginTransaction();
        try {
        	//alter table 表名 add constraint 约束名字 DEFAULT 默认值 for 字段名称
        	/**
        	 * alter table T_ping drop constraint DF_T_ping_p_c  
			   alter table T_ping add constraint DF_T_ping_p_c DEFAULT ((2)) for p_c  
        	 */
        	db.execSQL("ALTER TABLE "+table+" DROP CONSTRAINT  default "+columnValue);
//        	db.execSQL("ALTER TABLE "+table+" ADD CONSTRAINT NOT NULL default "+"NULL" +" for "+columnName);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
	}
	
	public boolean checkColumnExists(String tableName
	       , String columnName) {
		SQLiteDatabase db = database.getReadableDatabase();
	    boolean result = false ;
	    Cursor cursor = null ;

	    try{
	        cursor = db.rawQuery( "select * from sqlite_master where name = ? and sql like ?"
	           , new String[]{tableName , "%" + columnName + "%"} );
	        result = null != cursor && cursor.moveToFirst() ;
	    }catch (Exception e){
	        Log.e("lv","checkColumnExists2..." + e.getMessage()) ;
	    }finally{
	        if(null != cursor && !cursor.isClosed()){
	            cursor.close() ;
	        }
	    }
	    return result ;
	}

}
