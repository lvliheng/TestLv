package com.lv.testlv.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.lv.testlv.MyApplication;
import com.lv.testlv.R;
import com.lv.testlv.utils.ViewHolder;

import java.util.List;

/**
 * Created by lvliheng on 15/6/5.
 */
public class GroupAdapter extends BaseAdapter {

    private Context mContext;
    private Handler mHandler;
    private List<AVIMConversation> avimConversations;

    public GroupAdapter(Context mContext, Handler mHandler) {
        this.mContext=mContext;
        this.mHandler=mHandler;
    }

    public void setData(List<AVIMConversation> avimConversations) {
        this.avimConversations=avimConversations;
    }

    @Override
    public int getCount() {
        return avimConversations==null?0:avimConversations.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.group_item, null);
        }
        TextView group_name = ViewHolder.findViewById(convertView, R.id.group_name_tv);
        final Button enter_or_apply_btn = ViewHolder.findViewById(convertView, R.id.enter_or_apply_btn);

        group_name.setText(avimConversations.get(position).getAttribute("group_name").toString());

        for (int i = 0; i < avimConversations.get(position).getMembers().size(); i++) {
            if (MyApplication.getLoginId().equals(avimConversations.get(position).getMembers().get(i))) {
                enter_or_apply_btn.setText("enter");
                break;
            } else {
                enter_or_apply_btn.setText("apply");
            }
        }

        enter_or_apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if ("enter".equals(enter_or_apply_btn.getText().toString())) {
                    msg.what = 0;
                } else if ("apply".equals(enter_or_apply_btn.getText().toString())) {
                    msg.what = 1;
                }
                msg.arg1 = position;
                mHandler.sendMessage(msg);
            }
        });

        return convertView;
    }
}
