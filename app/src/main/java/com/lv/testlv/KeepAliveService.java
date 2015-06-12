package com.lv.testlv;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

/**
 * Created by lvliheng on 15/6/9.
 */
public class KeepAliveService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("llh", "KeepAliveService : onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("llh", "KeepAliveService : onStartCommand");
        AVIMClient avimClient = AVIMClient.getInstance(MyApplication.getLoginId());

        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVException e) {
                if (null == e) {
                    Log.d("llh", "login success");
                }else{
                    e.printStackTrace();
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
