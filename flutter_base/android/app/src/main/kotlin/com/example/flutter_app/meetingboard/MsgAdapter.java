package com.example.flutter_app.meetingboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;


import com.example.flutter_app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.webrtc.ali.ContextUtils.getApplicationContext;


//适配器类
public class MsgAdapter extends BaseAdapter {

    private List<Msg> datalist;
    private  LayoutInflater minflater;
    private Date lastchattime;


    //构造函数，获取到数据列表
    public MsgAdapter(Context cxt, List<Msg> datex){
        this.datalist=datex;
        this.minflater=LayoutInflater.from(cxt);
        lastchattime=new Date();
    }


    @Override
    public int getCount() {//总条数
        return datalist.size();
    }
    @Override
    public Object getItem(int position) {//根据一个索引（位置）获得该位置的对象
        return datalist.get(position);
    }
    @Override
    public long getItemId(int position) {//获取条目的id
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取该条目要显示的界面
        mViewHolder holder = null;

        if (convertView == null) {
            //无缓存时进入
            holder = new mViewHolder();
            //这里要注意有一个是上下文，一个是显示每一行的行布局文件
            convertView = minflater.inflate(R.layout.msg_item, parent, false);
            holder.tvNick = (TextView) convertView.findViewById(R.id.chat_nick);
            holder.tvMsg = (TextView) convertView.findViewById(R.id.chat_msg);
            holder.tvTime=(TextView)convertView.findViewById(R.id.chat_time);
            convertView.setTag(holder);
        } else {
            //缓存时进入
            holder = (mViewHolder) convertView.getTag();
        }
        //匹配数据


        holder.tvNick.setText(datalist.get(position).ChatName);
        holder.tvMsg.setText(datalist.get(position).ChatMsg);
        Date now=new Date();
        long diff=now.getTime()-lastchattime.getTime();
        if(diff>50000) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String strnow = format.format(now);
            holder.tvTime.setText(strnow);
            lastchattime=now;
            holder.tvTime.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvTime.setVisibility(View.GONE);
        }
        //int receiverId=datalist.get(position).ReceiverId;
        boolean isTeacherMsg=datalist.get(position).bTeacherMsg;
        if (isTeacherMsg)
        {
          int  mastercolor = ContextCompat.getColor(getApplicationContext(), R.color.mastercolor);
          holder.tvMsg.setTextColor(mastercolor);
        }
        else {
            int chatnormalcolor= ContextCompat.getColor(getApplicationContext(), R.color.chat_msg);
            holder.tvMsg.setTextColor(chatnormalcolor);
        }
        return convertView;
    }


    private static class  mViewHolder{
        TextView tvTime;
        TextView tvNick;
        TextView tvMsg;
    }
}
