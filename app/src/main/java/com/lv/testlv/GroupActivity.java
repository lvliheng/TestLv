package com.lv.testlv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.lv.testlv.adapter.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvliheng on 15/6/4.
 */
public class GroupActivity extends Activity implements View.OnClickListener {

    private List<AVIMConversation> avimConversations = new ArrayList<AVIMConversation>();
    private GroupAdapter mAdapter;

    public static boolean isNeedToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group);
        initView();
    }

    private void initView() {
        TextView middle_tv= (TextView) findViewById(R.id.middle_tv);
        Button create_btn = (Button) findViewById(R.id.create_btn);
        Button search_btn = (Button) findViewById(R.id.search_btn);
        ListView group_lv = (ListView) findViewById(R.id.group_lv);

        mAdapter = new GroupAdapter(GroupActivity.this, mHandler);
        group_lv.setAdapter(mAdapter);
        mAdapter.setData(avimConversations);

        create_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);

        middle_tv.setText("group");

        initData();
    }

    private void initData() {
        searchMyGroup();
    }

    private void searchMyGroup() {
        List<String> clients = new ArrayList<String>();
        clients.add(MyApplication.getLoginId());
        AVIMClient avimClient=AVIMClient.getInstance(MyApplication.getLoginId());
        AVIMConversationQuery conversationQuery = avimClient.getQuery();
        conversationQuery.containsMembers(clients);
        conversationQuery.whereEqualTo("attr.type", MyApplication.group_chat);

        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (null != e) {
                    Log.e("llh", "searchMyGroup ::: " + e.getMessage());
                    MyApplication.getAvimClient().open(new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVException e) {
                            if (null == e) {
                                Log.d("llh", "open success");
                            } else {
                                Log.e("llh", "open ::: " + e.getMessage());
                            }
                            searchMyGroup();
                        }
                    });
                } else {
                    if (null != conversations) {
                        Log.d("llh", "找到了符合条件的 " + conversations.size() + " 个对话");
                        if (conversations.size() == 0) {
                            Log.d("llh", "没有找到符合条件的对话");
                        } else {
                            for (AVIMConversation avimConversation : conversations) {
                                avimConversations.add(avimConversation);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("llh", "没有找到符合条件的对话");
                    }
                }
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    intentToChatActivity(avimConversations.get(msg.arg1).getConversationId(), avimConversations.get(msg.arg1).getAttribute("group_name").toString());
                    break;
                case 1:
                    final int position=msg.arg1;
                    avimConversations.get(position).join(new AVIMConversationCallback() {
                        @Override
                        public void done(AVException e) {
                            if (null == e) {
                                Log.d("llh", "join success");
                                intentToChatActivity(avimConversations.get(position).getConversationId(), avimConversations.get(position).getAttribute("group_name").toString());
                            }else {
                                Log.e("llh", "join ::: "+e.getMessage());
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    private void intentToChatActivity(String conversationId, String groupName) {
        Intent mIntent = new Intent();
        mIntent.setClass(GroupActivity.this, ChatActivity.class);
        mIntent.putExtra("type", MyApplication.group_chat + "");
        mIntent.putExtra("conversation_id", conversationId);
        mIntent.putExtra("name", groupName);
        startActivity(mIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_btn:
                Intent mCreateIntent = new Intent();
                mCreateIntent.setClass(GroupActivity.this, CreateGroupActivity.class);
                startActivity(mCreateIntent);
                break;
            case R.id.search_btn:
                Intent mSearchIntent = new Intent();
                mSearchIntent.setClass(GroupActivity.this, SearchGroupActivity.class);
                startActivity(mSearchIntent);
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedToRefresh) {
            avimConversations.clear();
            searchMyGroup();
            isNeedToRefresh=false;
        }
    }
}
