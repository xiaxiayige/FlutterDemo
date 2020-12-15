package com.example.flutter_app.meetingboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.example.flutter_app.R;

import java.util.List;

//适配器类
public class ThumbAdapter extends BaseAdapter {

    private List<Bitmap> datalist;
    private  LayoutInflater minflater;
    private  int iclick=-1;


    //构造函数，获取到数据列表
    public ThumbAdapter(Context cxt, List<Bitmap> datex){
        this.datalist=datex;
        this.minflater=LayoutInflater.from(cxt);
    }

    public void  SetClickItem(int pos)
    {
        iclick=pos;
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
            convertView = minflater.inflate(R.layout.thumb_simple_item, parent, false);
            holder.himg = (ImageView) convertView.findViewById(R.id.header);
            convertView.setTag(holder);
        } else {
            //缓存时进入
            holder = (mViewHolder) convertView.getTag();
        }
        //匹配数据

        holder.himg.setImageBitmap(datalist.get(position));
        if (position == iclick) {
            holder.himg.setSelected(true);
        }
        else {
            holder.himg.setSelected(false);
            holder.himg.setPressed(false);
            holder.himg.setActivated(false);
        }
        return convertView;
    }


    private static class  mViewHolder{
        ImageView himg;
    }
}
