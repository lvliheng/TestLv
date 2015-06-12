package com.lv.testlv;

import android.app.Activity;
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
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.lv.testlv.adapter.InviteAdapter;
import com.lv.testlv.model.FriendModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvliheng on 15/6/5.
 */
public class InviteActivity extends Activity implements View.OnClickListener {

    private AVIMConversation avimConversation;

    private List<FriendModel> friends = new ArrayList<FriendModel>();
    private List<String> allContacts = new ArrayList<String>();
    private ArrayList members = new ArrayList();
    private List<String> invitedFriends = new ArrayList<String>();

    private InviteAdapter mAdapter;

    private Button right_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite);
        initView();
    }

    private void initView() {
        Button left_btn= (Button) findViewById(R.id.left_btn);
        TextView middle_tv = (TextView) findViewById(R.id.middle_tv);
        right_btn= (Button) findViewById(R.id.right_btn);

        left_btn.setOnClickListener(this);
        right_btn.setOnClickListener(this);
        middle_tv.setText("invite");
        right_btn.setText("commit");
        right_btn.setEnabled(false);

        ListView invite_lv= (ListView) findViewById(R.id.invite_lv);

        mAdapter = new InviteAdapter(InviteActivity.this, mHandler);
        invite_lv.setAdapter(mAdapter);
        mAdapter.setData(friends);

        allContacts.add("0");
        allContacts.add("1");
        allContacts.add("2");
        allContacts.add("3");

//        members=getIntent().getStringArrayListExtra("members");

        searchGroupConversation(getIntent().getStringExtra("group_name"));

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

                    for (int i = 0; i < allContacts.size(); i++) {
                        FriendModel model1=new FriendModel();
                        for (int j = 0; j < avimConversation.getMembers().size(); j++) {
                            if (avimConversation.getMembers().get(j).equals(allContacts.get(i))) {
                                model1.setIsInGroup("1");
                                break;
                            }else{
                                model1.setIsInGroup("0");
                            }
                        }
                        model1.setName(allContacts.get(i));
                        friends.add(model1);
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
                    invitedFriends.remove(friends.get(msg.arg1).getName());
                    checkCommitBtn();
                    break;
                case 1:
                    invitedFriends.add(friends.get(msg.arg1).getName());
                    checkCommitBtn();
                    break;
                default:
                    break;
            }
        }
    };

    private void checkCommitBtn() {
        if (invitedFriends.size() > 0) {
            right_btn.setEnabled(true);
        }else{
            right_btn.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:
                avimConversation.addMembers(invitedFriends, new AVIMConversationCallback() {
                    @Override
                    public void done(AVException e) {
                        if (null == e) {
                            GroupMemberActivity.isNeedToRefresh=true;
                            finish();
                            Log.d("llh", "invite success");
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

}
