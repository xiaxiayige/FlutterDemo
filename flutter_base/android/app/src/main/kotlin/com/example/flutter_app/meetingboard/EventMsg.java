package com.example.flutter_app.meetingboard;

/**
 * Created by yzq on 2017/9/27.
 * 传递消息时使用，可以自己增加更多的参数
 */

public class EventMsg {

    public String Tag;
    public SeriallyPacket packet;
    public void  EventMsg()
    {
        Tag="";
        packet=null;
    }

    @Override
    public String toString() {
        return "EventMsg{" +
                "Tag='" + Tag + '\'' +
                ", packet=" + packet +
                '}';
    }
}
