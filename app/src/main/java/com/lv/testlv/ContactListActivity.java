package com.lv.testlv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lvliheng on 15/6/4.
 */
public class ContactListActivity extends Activity implements View.OnClickListener{

    private AVIMClient avimClient;

    private String loginId;

    private String friendId;

    private List<String> allFriends = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        loginId = MyApplication.getLoginId();

        avimClient=MyApplication.avimClient;

        initView();
    }

    private void initView() {
        TextView left_btn= (TextView) findViewById(R.id.left_btn);
        TextView middle_tv = (TextView) findViewById(R.id.middle_tv);
        Button contact0 = (Button) findViewById(R.id.contact0);
        Button contact1 = (Button) findViewById(R.id.contact1);
        Button group = (Button) findViewById(R.id.group);
        Button logout = (Button) findViewById(R.id.logout);

        contact0.setOnClickListener(this);
        contact1.setOnClickListener(this);
        group.setOnClickListener(this);
        logout.setOnClickListener(this);

        left_btn.setVisibility(View.INVISIBLE);
        middle_tv.setText("contacts");

        allFriends.add("0");
        allFriends.add("1");
        allFriends.add("2");

        allFriends.remove(loginId);

        contact0.setText(allFriends.get(0));
        contact1.setText(allFriends.get(1));
    }

    private void searchFriend(final String friendId) {
        List<String> clients = new ArrayList<String>();
        clients.add(loginId);
        clients.add(friendId);
        AVIMConversationQuery conversationQuery = avimClient.getQuery();
        conversationQuery.containsMembers(clients);
        conversationQuery.whereEqualTo("attr.type", MyApplication.friend_chat);

        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (null != e) {
                    Log.e("llh", "searchFriend ::: " + e.getMessage());
                    MyApplication.avimClient.open(new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVException e) {
                            if (null == e) {
                                Log.d("llh", "open success");
                            } else {
                                Log.e("llh", "open ::: " + e.getMessage());
                            }
                            searchFriend(friendId);
                        }
                    });
                } else {
                    if (null != conversations) {
                        Log.d("llh", "找到了符合条件的 " + conversations.size() + " 个对话");
                        if (conversations.size() == 0) {
                            createFriendConversation(friendId);
                        } else {
                            intentToChatActivity(conversations.get(0).getConversationId(), friendId);
                        }
                    } else {
                        Log.d("llh", "没有找到符合条件的对话");
                        createFriendConversation(friendId);
                    }
                }
            }
        });
    }

    private void createFriendConversation(final String friendId) {
        final List<String> clientIds = new ArrayList<String>();
        clientIds.add(loginId);
        clientIds.add(friendId);

        final Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("type", MyApplication.friend_chat);

        avimClient.createConversation(clientIds, attr, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation, AVException e) {
                if (null != conversation && null == e) {
                    Log.d("llh", "createConversation done");
                    intentToChatActivity(conversation.getConversationId(), friendId);
                }else {
                    Log.e("llh", "createConversation ::: "+e.getMessage());
                }
            }
        });
    }

    private void intentToChatActivity(String conversationId, String friendId) {
        Intent mIntent = new Intent();
        mIntent.setClass(ContactListActivity.this, ChatActivity.class);
        mIntent.putExtra("conversation_id", conversationId);
        mIntent.putExtra("friend_id", friendId);
        mIntent.putExtra("type", MyApplication.friend_chat+"");
        mIntent.putExtra("name", friendId);
        startActivity(mIntent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contact0:
            case R.id.contact1:
                friendId=((Button)view).getText().toString();
                searchFriend(friendId);
                break;
            case R.id.group:
                Intent mIntent = new Intent();
                mIntent.setClass(ContactListActivity.this, GroupActivity.class);
                startActivity(mIntent);
                break;
            case R.id.logout:
                avimClient.close(new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVException e) {
                        if (null == e) {
                            MySharePreferences mySharePreferences = new MySharePreferences(ContactListActivity.this);
                            mySharePreferences.setLoginId("-1");
                            MyApplication.setLoginId("-1");
                            Intent mIntent = new Intent();
                            mIntent.setClass(ContactListActivity.this, MainActivity.class);
                            startActivity(mIntent);
                            finish();
                            Log.d("llh", "logout success");
                        } else {
                            Log.e("llh", "close ::: " + e.getMessage());
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
