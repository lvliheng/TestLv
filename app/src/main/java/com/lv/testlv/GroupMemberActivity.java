package com.lv.testlv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.lv.testlv.adapter.GroupMemberAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvliheng on 15/6/5.
 */
public class GroupMemberActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ArrayList<String> members = new ArrayList<String>();

    private AVIMConversation avimConversation;

    private GroupMemberAdapter mAdapter;

    private String groupName;

    public static boolean isNeedToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member);
        initView();
    }

    private void initView() {
        Button left_btn= (Button) findViewById(R.id.left_btn);
        TextView middle_tv = (TextView) findViewById(R.id.middle_tv);
        Button right_btn= (Button) findViewById(R.id.right_btn);
        ListView member_lv= (ListView) findViewById(R.id.member_lv);
        Button exit_btn= (Button) findViewById(R.id.exit_btn);

        left_btn.setOnClickListener(this);
        middle_tv.setText("member");
        right_btn.setVisibility(View.INVISIBLE);
        exit_btn.setOnClickListener(this);
        member_lv.setOnItemClickListener(this);

        mAdapter = new GroupMemberAdapter(GroupMemberActivity.this, mHandler);
        member_lv.setAdapter(mAdapter);
        mAdapter.setData(members);

        groupName=getIntent().getStringExtra("group_name");
        initData();
    }

    private void initData() {
        searchGroupConversation(groupName);
    }

    private void searchGroupConversation(String groupName) {
        AVIMClient avimClient=AVIMClient.getInstance(MyApplication.getLoginId());
        AVIMConversationQuery conversationQuery = avimClient.getQuery();
        conversationQuery.whereEqualTo("attr.type", MyApplication.group_chat);
        conversationQuery.whereEqualTo("attr.group_name", groupName);

        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (null != e) {
                    Log.e("llh", "avimConversation == null");
                    e.printStackTrace();
                } else {
                    Log.d("llh", "find " + conversations.size()+" conversations");
                    avimConversation = conversations.get(0);
                    for (int i = 0; i < avimConversation.getMembers().size(); i++) {
                        members.add(avimConversation.getMembers().get(i));
                    }
                    mAdapter.notifyDataSetChanged();
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
                    final List<String> kickIds = new ArrayList<String>();
                    kickIds.add((String)msg.obj);
                    avimConversation.kickMembers(kickIds, new AVIMConversationCallback() {
                        @Override
                        public void done(AVException e) {
                            if (null == e) {
                                members.remove(kickIds.get(0));
                                mAdapter.notifyDataSetChanged();
                                Log.d("llh", "kick " + kickIds.get(0) + "success");
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_btn:
                avimConversation.quit(new AVIMConversationCallback() {
                    @Override
                    public void done(AVException e) {
                        if (null == e) {
                            GroupActivity.isNeedToRefresh=true;
                            finish();
                        }else{
                            e.printStackTrace();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == members.size()) {
            Intent mIntent = new Intent();
            mIntent.setClass(GroupMemberActivity.this, InviteActivity.class);
            mIntent.putStringArrayListExtra("members", members);
            mIntent.putExtra("group_name", getIntent().getStringExtra("group_name"));
            startActivity(mIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedToRefresh) {
            members.clear();
            searchGroupConversation(groupName);
            isNeedToRefresh=false;
        }
    }
}
