package com.lv.testlv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.lv.testlv.MyApplication;
import com.lv.testlv.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lvliheng on 15/6/4.
 */
public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private List<AVIMMessage> messages = new ArrayList<AVIMMessage>();

    public ChatAdapter(Context mContext) {
        this.mContext=mContext;
    }

    public void setData(List<AVIMMessage> messages) {
        this.messages=messages;
    }

    @Override
    public int getCount() {
        return messages==null?0:messages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mViewHolder;
        if (null == view) {
            view = View.inflate(mContext, R.layout.chat_item, null);
            mViewHolder=new ViewHolder();
            view.setTag(mViewHolder);
        }else{
            mViewHolder= (ViewHolder) view.getTag();
        }

        mViewHolder.msg= (TextView) view.findViewById(R.id.msg_tv);
        mViewHolder.time= (TextView) view.findViewById(R.id.time_tv);

        if (MyApplication.getLoginId().equals(messages.get(i).getFrom())) {
            mViewHolder.msg.setGravity(Gravity.RIGHT);
            mViewHolder.msg.setTextColor(Color.parseColor("#4778af"));
            mViewHolder.msg.setText(messages.get(i).getContent() + " : " + messages.get(i).getFrom());
        }else{
            mViewHolder.msg.setGravity(Gravity.LEFT);
            mViewHolder.msg.setTextColor(Color.parseColor("#477805"));
            mViewHolder.msg.setText(messages.get(i).getFrom()+" : "+messages.get(i).getContent());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        mViewHolder.time.setText(simpleDateFormat.format(new Date(messages.get(i).getTimestamp())));
        mViewHolder.time.setTextColor(Color.parseColor("#c5c5c5"));

        return view;
    }

    class ViewHolder{
        TextView msg;
        TextView time;
    }
}
