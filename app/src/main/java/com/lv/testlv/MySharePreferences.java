package com.lv.testlv;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lvliheng on 15/6/9.
 */
public class MySharePreferences {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    public MySharePreferences(Context mContext) {
        mSharedPreferences=mContext.getSharedPreferences("testLv", 0);
        mEditor=mSharedPreferences.edit();
    }

    public String getLoginId() {
        return mSharedPreferences.getString(MyApplication.loginIdIndex, "-1");
    }

    public void setLoginId(String loginId) {
        mEditor.putString(MyApplication.loginIdIndex, loginId);
        mEditor.commit();
    }
}


