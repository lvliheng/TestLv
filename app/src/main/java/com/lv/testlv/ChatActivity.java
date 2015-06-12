package com.lv.testlv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.lv.testlv.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvliheng on 15/6/4.
 */
public class ChatActivity extends Activity implements OnClickListener{

    private List<AVIMMessage> messages = new ArrayList<AVIMMessage>();
    private ChatAdapter mChatAdapter;
    private AVIMConversation avimConversation;

    private TextView middle_tv;
    private Button right_btn;
    private EditText content_et;
    private ListView listView;

    private String name;

    private CustomMessageHandler messageHandler;

    private boolean isTyping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        initView();
    }

    private void initView() {
        Button left_btn= (Button) findViewById(R.id.left_btn);
        middle_tv= (TextView) findViewById(R.id.middle_tv);
        right_btn= (Button) findViewById(R.id.right_btn);
        listView = (ListView) findViewById(R.id.chat_lv);
        content_et= (EditText) findViewById(R.id.content_et);
        final Button send = (Button) findViewById(R.id.send_btn);

        avimConversation = MyApplication.avimClient.getConversation(getIntent().getStringExtra("conversation_id"));
        messageHandler=new CustomMessageHandler();

        MyApplication.getAvimClient().setClientEventHandler(new CustomClientHandler());
        AVIMMessageManager.registerMessageHandler(AVIMMessage.class, messageHandler);
        AVIMMessageManager.setConversationEventHandler(new CustomConversationHandler());

        String type=getIntent().getStringExtra("type");
        name=getIntent().getStringExtra("name");
        if ((MyApplication.friend_chat+"").equals(type)) {
            initFriendChat();
        }else if ((MyApplication.group_chat+"").equals(type)) {
            initGroupChat();
        }

        middle_tv.setText(name);

        mChatAdapter=new ChatAdapter(ChatActivity.this);
        listView.setAdapter(mChatAdapter);
        mChatAdapter.setData(messages);

        initHistoryMsg();

        left_btn.setOnClickListener(this);
        right_btn.setOnClickListener(this);
        send.setOnClickListener(this);

        content_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
//                    if (!isTyping) {
//                        isTyping = true;
//                        sendMsg(1);
//                    }
                    send.setEnabled(true);
                }else{
//                    isTyping=false;
                    send.setEnabled(false);
                }
            }
        });

    }

    private void initHistoryMsg() {
        avimConversation.queryMessages(20, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVException e) {
                if (null == e) {
                    if (list.size() > 0) {
                        messages.addAll(list);
                        mChatAdapter.notifyDataSetChanged();
                        listView.setSelection(messages.size()-1);
                    }
                } else {
                    Log.e("llh", "queryMessages ::: " + e.getMessage());
                }
            }
        });
    }

    private void initFriendChat() {
        right_btn.setVisibility(View.INVISIBLE);
    }

    private void initGroupChat() {
        right_btn.setVisibility(View.VISIBLE);
        right_btn.setText("member");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_btn:
                finish();
                break;
            case R.id.right_btn:
                intentToGroupMemberActivity();
                break;
            case R.id.send_btn:
                sendMsg(0);
                break;
            default:
                break;
        }
    }

    private void intentToGroupMemberActivity() {
        Intent mIntent = new Intent();
        mIntent.setClass(ChatActivity.this, GroupMemberActivity.class);
        mIntent.putExtra("group_name", name);
        startActivity(mIntent);
    }

    private void sendMsg(final int type) {
        final AVIMMessage message = new AVIMMessage();
        message.setTimestamp(System.currentTimeMillis());
        message.setContent(content_et.getText().toString());
        switch (type) {
            case 0:
                avimConversation.sendMessage(message, new AVIMConversationCallback() {
                    @Override
                    public void done(AVException e) {
                        if (null == e) {
                            Log.d("llh", "send success");
                            content_et.setText("");
                            messages.add(message);
                            mChatAdapter.notifyDataSetChanged();
                            listView.setSelection(messages.size() - 1);
                        } else {
                            Log.e("llh", "sendMessage ::: " + e.getMessage());
                            MyApplication.avimClient.open(new AVIMClientCallback() {
                                @Override
                                public void done(AVIMClient avimClient, AVException e) {
                                    if (null == e) {
                                        Log.d("llh", "open success");
                                        middle_tv.setText(name);
                                    } else {
                                        Log.e("llh", "open ::: " + e.getMessage());
                                    }
                                    sendMsg(0);
                                }
                            });
                        }
                    }
                });
                break;
//            case 1:
//                avimConversation.sendMessage(message, AVIMConversation.TRANSIENT_MESSAGE_FLAG, new AVIMConversationCallback() {
//                    @Override
//                    public void done(AVException e) {
//                        if (null == e) {
//                            Log.d("llh", "'typing...' send success");
//                        }else {
//                            Log.e("llh", "sendMessage('typing...') ::: "+e.getMessage());
//                        }
//                    }
//                });
//                break;
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    MyApplication.avimClient.open(new AVIMClientCallback() {
                        @Override
                        public void done(AVIMClient avimClient, AVException e) {
                            if (null == e) {
                                Log.d("llh", "open success");
                            } else {
                                Log.e("llh", "open ::: " + e.getMessage());
                                mHandler.sendEmptyMessage(0);
                            }
                        }
                    });
                    break;
            }
        }
    };

    private class CustomClientHandler extends AVIMClientEventHandler{

        @Override
        public void onConnectionPaused(AVIMClient avimClient) {
            Log.d("llh", "onConnectionPaused");
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void onConnectionResume(AVIMClient avimClient) {
            Log.d("llh", "onConnectionResume");
        }
    }

    private class CustomConversationHandler extends AVIMConversationEventHandler{

        @Override
        public void onMemberLeft(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
            Log.d("llh", "onMemberLeft : ");
        }

        @Override
        public void onMemberJoined(AVIMClient avimClient, AVIMConversation avimConversation, List<String> list, String s) {
            Log.d("llh", "onMemberJoined : list >>> "+list+"\n"+" s >>> "+s);
        }

        @Override
        public void onKicked(AVIMClient avimClient, AVIMConversation avimConversation, String s) {
            Log.d("llh", "onKicked : s >>> "+s);
        }

        @Override
        public void onInvited(AVIMClient avimClient, AVIMConversation avimConversation, String s) {
            Log.d("llh", "onMemberLeft : s >>> "+s);
        }
    }

    private class CustomMessageHandler extends AVIMMessageHandler{

        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            super.onMessage(message, conversation, client);
            Log.d("llh", "onMessage : message >>> " + message.getContent());
            if (getIntent().getStringExtra("conversation_id").equals(conversation.getConversationId())) {
//                if (conversation.isTransient()) {
//                    middle_tv.setText("typing...");
//                }else{
//                    messages.add(message);
//                    mChatAdapter.notifyDataSetChanged();
//                    listView.setSelection(messages.size() - 1);
//                    middle_tv.setText(name);
//                }
                messages.add(message);
                mChatAdapter.notifyDataSetChanged();
                listView.setSelection(messages.size() - 1);
                middle_tv.setText(name);
            }
        }

        @Override
        public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            super.onMessageReceipt(message, conversation, client);
            Log.d("llh", "onMessageReceipt : message >>> "+message);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AVIMMessageManager.unregisterMessageHandler(AVIMMessage.class, messageHandler);
    }
}
