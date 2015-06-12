package com.lv.testlv;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.lv.testlv.adapter.InviteAdapter;
import com.lv.testlv.model.FriendModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lvliheng on 15/6/8.
 */
public class CreateGroupActivity extends Activity implements OnClickListener{

    private List<FriendModel> allContacts = new ArrayList<FriendModel>();

    private List<String> createMembers = new ArrayList<String>();

    private EditText create_name_et;
    private Button right_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        initView();
    }

    private void initView() {
        Button left_btn= (Button) findViewById(R.id.left_btn);
        TextView middle_tv= (TextView) findViewById(R.id.middle_tv);
        right_btn= (Button) findViewById(R.id.right_btn);

        middle_tv.setText("create group");
        right_btn.setText("commit");
        right_btn.setEnabled(false);

        right_btn.setOnClickListener(this);

        create_name_et= (EditText) findViewById(R.id.create_name_et);

        create_name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    right_btn.setEnabled(true);
                }else{
                    right_btn.setEnabled(false);
                }
            }
        });

        ListView create_group_member_lv= (ListView) findViewById(R.id.create_group_member_lv);

        FriendModel model = new FriendModel();
        if (MyApplication.getLoginId().equals("0")) {
            model.setIsInGroup("1");
            createMembers.add("0");
        }else{
            model.setIsInGroup("0");
        }
        model.setName("0");
        allContacts.add(model);

        FriendModel model1 = new FriendModel();
        if (MyApplication.getLoginId().equals("1")) {
            model1.setIsInGroup("1");
            createMembers.add("1");
        }else{
            model1.setIsInGroup("0");
        }
        model1.setName("1");
        allContacts.add(model1);

        FriendModel model2 = new FriendModel();
        if (MyApplication.getLoginId().equals("2")) {
            model2.setIsInGroup("1");
            createMembers.add("2");
        }else{
            model2.setIsInGroup("0");
        }
        model2.setName("2");
        allContacts.add(model2);

        FriendModel model3 = new FriendModel();
        if (MyApplication.getLoginId().equals("3")) {
            model3.setIsInGroup("1");
            createMembers.add("3");
        }else{
            model3.setIsInGroup("0");
        }
        model3.setName("3");
        allContacts.add(model3);

        InviteAdapter mAdapter = new InviteAdapter(CreateGroupActivity.this, mHandler);
        create_group_member_lv.setAdapter(mAdapter);
        mAdapter.setData(allContacts);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    createMembers.remove(allContacts.get(msg.arg1).getName());
                    break;
                case 1:
                    createMembers.add(allContacts.get(msg.arg1).getName());
                    break;
                default:
                    break;
            }
        }
    };

    private void createGroup() {
        final Map<String, Object> attr = new HashMap<String, Object>();
        attr.put("type", MyApplication.group_chat);
        attr.put("group_name", create_name_et.getText().toString());

        AVIMClient avimClient = AVIMClient.getInstance(MyApplication.getLoginId());
        avimClient.createConversation(createMembers, attr, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation, AVException e) {
                if (null != conversation && null == e) {
                    GroupActivity.isNeedToRefresh=true;
                    finish();
                    Toast.makeText(CreateGroupActivity.this, "create group success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_btn:
                createGroup();
                break;
            default:
                break;
        }
    }
}
