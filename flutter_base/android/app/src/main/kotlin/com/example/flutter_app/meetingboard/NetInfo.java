package com.example.flutter_app.meetingboard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetInfo {
    public int IP;
    public int Port;

    public int ReadFrom(byte[] buf) {
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        boardByteBuf.AssignReadBuf(buf);
        IP = boardByteBuf.ReadInt();
        Port = boardByteBuf.ReadUShort();
        return boardByteBuf.GetReadPos();
    }

    public int WriteTo(byte[] buf) {
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        boardByteBuf.AssignWriteBuf(buf);
        boardByteBuf.WriteUint(IP);
        boardByteBuf.WriteUShort(Port);
        return boardByteBuf.GetWritePos();
    }

    public boolean FromString(String sv) {
        BoardUtils boardtool = new BoardUtils();
        Pattern p1 = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)");
        Matcher m1 = p1.matcher(sv);
        String sIP = "";
        try {

            sIP = m1.group(1);
            IP = boardtool.ipToInt(sIP);
            String sPort = "";
            sPort = m1.group(2);
            Port = Integer.valueOf(sPort);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }

    public boolean IsValid()
    { return IP!=0 && Port!=0;}


}
