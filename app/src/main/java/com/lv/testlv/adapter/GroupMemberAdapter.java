package com.lv.testlv.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lv.testlv.R;
import com.lv.testlv.utils.ViewHolder;

import java.util.List;

/**
 * Created by lvliheng on 15/6/5.
 */
public class GroupMemberAdapter extends BaseAdapter {

    private Context mContext;
    private Handler mHandler;
    private List<String> members;

    public GroupMemberAdapter(Context mContext, Handler mHandler) {
        this.mContext=mContext;
        this.mHandler=mHandler;
    }

    public void setData(List<String> members) {
        this.members=members;
    }

    @Override
    public int getCount() {
        return members==null?1:(members.size()+1);
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
            convertView = View.inflate(mContext, R.layout.group_member_item, null);
        }

        TextView member_name = ViewHolder.findViewById(convertView, R.id.member_name_tv);
        Button kick_btn = ViewHolder.findViewById(convertView, R.id.kick_btn);
        if (position == getCount() - 1) {
            kick_btn.setVisibility(View.GONE);
            member_name.setText(" + ");
            member_name.setGravity(Gravity.CENTER);
            member_name.setTextSize(20);
        }else{
            kick_btn.setVisibility(View.VISIBLE);
            member_name.setText(members.get(position));
            member_name.setGravity(Gravity.LEFT);
            member_name.setTextSize(12);
        }

        kick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what=0;
                msg.obj = members.get(position);
                mHandler.handleMessage(msg);
            }
        });

        return convertView;
    }
}
