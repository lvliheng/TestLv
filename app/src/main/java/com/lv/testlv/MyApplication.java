package com.lv.testlv;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;

/**
 * Created by lvliheng on 15/6/4.
 */
public class MyApplication extends Application{

    public static String loginIdIndex="login_id";

    private static String loginId;

    public static int friend_chat=0;
    public static int group_chat=1;

    public static AVIMClient avimClient;

    @Override
    public void onCreate() {
        super.onCreate();
//        AVOSCloud.setNetworkTimeout(10000);

        AVOSCloud.initialize(this, "flxoaxy4a4xnr99vayzdh9nrr4q2u2h7n4cj85ucji28btof",
                "7mwnh3d12mbm1170xjathcmq5uib87q8nomjbwsbtvqvaupv");

//        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler());

        MySharePreferences mySharePreferences = new MySharePreferences(getApplicationContext());

        if (!"-1".equals(mySharePreferences.getLoginId())) {
            setLoginId(mySharePreferences.getLoginId());
            setAvimClient(mySharePreferences.getLoginId());
        }

    }

    public static String getLoginId() {
        return loginId;
    }

    public static void setLoginId(String login_d) {
        loginId=login_d;
    }

    public static AVIMClient getAvimClient() {
        return avimClient;
    }

    public static void setAvimClient(String loginId) {
        avimClient = AVIMClient.getInstance(loginId);
    }


    private class CustomMessageHandler extends AVIMMessageHandler {

        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            super.onMessage(message, conversation, client);
            Log.d("llh", "MyApplication onMessage : " + message.getContent());
            Toast.makeText(getApplicationContext(), message.getFrom()+" : "+message.getContent(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            super.onMessageReceipt(message, conversation, client);
            Log.d("llh", "MyApplication onMessageReceipt : "+message);
        }

    }
}


