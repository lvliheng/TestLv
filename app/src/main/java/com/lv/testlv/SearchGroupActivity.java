package com.lv.testlv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.lv.testlv.adapter.GroupAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvliheng on 15/6/5.
 */
public class SearchGroupActivity extends Activity {

    private List<AVIMConversation> avimConversations = new ArrayList<AVIMConversation>();
    private GroupAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_group);
        initView();
    }

    private void initView() {
        TextView middle_tv= (TextView) findViewById(R.id.middle_tv);
        SearchView search_view = (SearchView) findViewById(R.id.search_view);
        ListView search_lv = (ListView) findViewById(R.id.search_lv);

        middle_tv.setText("search");

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if ("".equals(newText)) {
                    avimConversations.clear();
                    mAdapter.notifyDataSetChanged();
                } else {
                    searchGroupByName(newText);
                }
                return false;
            }
        });

        search_view.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return false;
            }
        });

        mAdapter = new GroupAdapter(SearchGroupActivity.this, mHandler);
        search_lv.setAdapter(mAdapter);
        mAdapter.setData(avimConversations);
    }

    private void searchGroupByName(String groupName) {
        AVIMClient avimClient = AVIMClient.getInstance(MyApplication.getLoginId());
        AVIMConversationQuery conversationQuery = avimClient.getQuery();
        conversationQuery.whereEqualTo("attr.type", MyApplication.group_chat);
        conversationQuery.whereEqualTo("attr.group_name", groupName);

        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> conversations, AVException e) {
                if (null != e) {
                    e.printStackTrace();
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
            Log.d("llh", "arg1 : " + msg.arg1);
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
                                GroupActivity.isNeedToRefresh=true;
                                Log.d("llh", "join success");
                                intentToChatActivity(avimConversations.get(position).getConversationId(), avimConversations.get(position).getAttribute("group_name").toString());
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
        mIntent.setClass(SearchGroupActivity.this, ChatActivity.class);
        mIntent.putExtra("type", MyApplication.group_chat + "");
        mIntent.putExtra("conversation_id", conversationId);
        mIntent.putExtra("name", groupName);
        startActivity(mIntent);
    }

}
