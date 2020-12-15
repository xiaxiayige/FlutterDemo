package com.example.flutter_app.meetingboard;

import java.util.Arrays;

public class SeriallyData {

    public SeriallyHead Head;
    public byte[] Data;

    public  SeriallyData() {
        Head = new SeriallyHead();
        Head.Flag = 0;
        Head.DataType = 0;
        Head.DataLen = 0;
        Data = null;
    }

    public int Length() {
        return Head.DataLen;
    }

    public void Assign(SeriallyData sourceData) {
        Assign(sourceData.Head.Flag,   sourceData.Data, sourceData.Head.DataLen);
    }

    public void Assign(short flag,   byte[] buf, int len) {
        if (len < 0 || len > 64000) return;
        Head.Flag = flag;
        Head.DataLen=len;
        Data = new byte[Head.DataLen];
        System.arraycopy(buf, 0, Data, 0, len);
    }

    public byte[] GetData() {
        return Data;
    }

    public int CopyData(byte[] Destbuf, int len) {
        if (len < Head.DataLen) return 0;
        Arrays.fill(Destbuf, (byte) 0);
        System.arraycopy(Data, 0, Destbuf, 0, len);
        return Head.DataLen;
    }

    public boolean FindFlag(byte[] buf, int buflen, int lf) {
        int iPos = 0;
        while (iPos < buflen) {
            iPos += ReadFrom(buf, iPos);
            if (Head.Flag == lf) return true;
        }
        Data = null;
        Head.DataLen = 0;
        return false;
    }

    public int ReadFrom(byte[] buf, int iPos) {
        int iLen = 0;
        if (buf == null) return 0;
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        if (boardByteBuf.AssignReadBuf(buf, iPos) == false) return 0;
        Head.Flag = boardByteBuf.ReadUByte();
        Head.DataLen = boardByteBuf.ReadUShort();
        Data = boardByteBuf.ReadByteArr(Head.DataLen);
        return boardByteBuf.GetReadPos();
    }

    public int WriteTo(byte[] buf, int iPos) {
        int iLen = 0;
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        boardByteBuf.AssignWriteBuf(buf, iPos);
        boardByteBuf.WriteUByte(Head.Flag);
        boardByteBuf.WriteUShort(Head.DataLen);
        boardByteBuf.WriteByteArr(Data);
        return boardByteBuf.GetWritePos();
    }

    public int AsInt() {
        int iRet = 0;
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        boardByteBuf.AssignReadBuf(Data);
        switch (Head.DataLen) {
            case 0:
                return 0;
            case 1:
                iRet = boardByteBuf.ReadUByte();
                break;
            case 2:
                iRet = boardByteBuf.ReadUShort();
                break;
            default:
                iRet = boardByteBuf.ReadInt();
                break;
        }
        return iRet;
    }

    public long AsInt64() {
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        boardByteBuf.AssignReadBuf(Data);
        long i64;
        if (Head.DataLen == 8) {
            i64 = boardByteBuf.ReadLong();
        } else i64 = AsInt();
        return i64;
    }

    public String AsString() {
        try {
            String sRet = "";
            BoardByteBuf boardByteBuf = new BoardByteBuf();
            boardByteBuf.AssignReadBuf(Data);
            if (Head.DataLen == 0) return "";
            byte[] buf = boardByteBuf.ReadByteArr(Head.DataLen);
            if (buf != null) {
                sRet = new String(buf, "GB2312");
            }
            return sRet;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public double AsDouble() {
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        boardByteBuf.AssignReadBuf(Data);
        if (Head.DataLen == 0) return 0;
        double dv = 0;
        if (Head.DataLen == 8) {
            dv = boardByteBuf.ReadDouble();
        }
        return dv;
    }


}
