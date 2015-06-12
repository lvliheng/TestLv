package com.lv.testlv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

public class MainActivity extends Activity {

    private AVIMClient avimClient;

    private String loginId="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        if (null!=MyApplication.getLoginId()&&!"-1".equals(MyApplication.getLoginId())) {
            Intent mIntent = new Intent();
            mIntent.setClass(MainActivity.this, ContactListActivity.class);
            startActivity(mIntent);
            finish();
        }

        Button left_btn= (Button) findViewById(R.id.left_btn);
        TextView middle_tv= (TextView) findViewById(R.id.middle_tv);
        left_btn.setVisibility(View.INVISIBLE);
        middle_tv.setText("login");

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.login_rg);
        final Button login = (Button) findViewById(R.id.login);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb0) {
                    loginId = "0";
                } else if (i == R.id.rb1) {
                    loginId = "1";
                } else if (i == R.id.rb2) {
                    loginId = "2";
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MySharePreferences mySharePreferences = new MySharePreferences(getApplicationContext());
                mySharePreferences.setLoginId(loginId);
                MyApplication.setLoginId(loginId);

                avimClient = AVIMClient.getInstance(loginId);
                avimClient.open(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVException e) {
                        if (null == e) {
                            Log.d("llh", "login success");
                            Intent mIntent1 = new Intent();
                            mIntent1.setClass(MainActivity.this, ContactListActivity.class);
                            startActivity(mIntent1);
                        }else {
                            Log.d("llh", "open : " + e.getMessage());
                        }
                    }
                });

            }
        });

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                default:
                    break;
            }
        }
    };

}
