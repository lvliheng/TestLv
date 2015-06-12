package com.lv.testlv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lv.testlv.R;
import com.lv.testlv.model.FriendModel;
import com.lv.testlv.utils.ViewHolder;

import java.util.List;

/**
 * Created by lvliheng on 15/6/5.
 */
public class InviteAdapter extends BaseAdapter {

    private Context mContext;
    private Handler mHandler;
    private List<FriendModel> friends;

    public InviteAdapter(Context mContext, Handler mHandler) {
        this.mContext=mContext;
        this.mHandler=mHandler;
    }

    public void setData(List<FriendModel> friends) {
        this.friends=friends;
    }

    @Override
    public int getCount() {
        return friends==null?0:friends.size();
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
            convertView = View.inflate(mContext, R.layout.invite_item, null);
        }

        TextView invite_name_tv = ViewHolder.findViewById(convertView, R.id.invite_name_tv);
        final CheckBox invite_cb=ViewHolder.findViewById(convertView, R.id.invite_cb);

        invite_name_tv.setText(friends.get(position).getName());
        if (friends.get(position).getIsInGroup().equals("1")) {
            invite_cb.setChecked(true);
            invite_cb.setEnabled(false);
            convertView.setEnabled(false);
            convertView.setBackgroundColor(Color.parseColor("#c5c5c5"));
        }else{
            invite_cb.setChecked(false);
            invite_cb.setEnabled(false);
            convertView.setEnabled(true);
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invite_cb.setChecked(!invite_cb.isChecked());
                Message msg = new Message();
                msg.arg1=position;
                if (invite_cb.isChecked()) {
                    msg.what=1;
                }else{
                    msg.what=0;
                }
                mHandler.handleMessage(msg);
            }
        });

        return convertView;
    }
}
