package com.example.flutter_app.meetingboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.flutter_app.meetingboard.BoardConstants.ByteDataType.*;


public class SeriallyPacket {

    private SeriallyData sd;
    private int BufLen;
    private int Capacity;
    private byte[] Buffer;

    public HashMap<Integer, SeriallyData> packetMap;

    public SeriallyPacket(int icapacity) {
        Init(icapacity);
    }

    public SeriallyPacket() {
        packetMap = new HashMap<Integer, SeriallyData>();
        Capacity = 2048;
        Init(Capacity);
    }

    public void Init(int icapacity) {
        Capacity = icapacity;
        Buffer = new byte[Capacity];
        Arrays.fill(Buffer, (byte) 0);
    }

    public int GetLen() {
        // return BufLen;
        int iret = 0;
        Iterator iter = packetMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int ikey = (int) entry.getKey();
            SeriallyData obj = (SeriallyData) entry.getValue();
            iret = iret + obj.Head.DataLen + 3; //包括flag 和 datalen字段
        }
        return iret;
    }

    public byte[] Data() {
        // return Buffer;
        byte[] retarr = new byte[GetLen()];
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        boardByteBuf.AssignWriteBuf(retarr);
        int ipos = 0;
        Iterator iter = packetMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int ikey = (int) entry.getKey();
            SeriallyData obj = (SeriallyData) entry.getValue();
            boardByteBuf.WriteUByte(obj.Head.Flag);
            boardByteBuf.WriteUShort(obj.Head.DataLen);
            boardByteBuf.WriteByteArr(obj.Data);
        }
        return retarr;
    }


    public boolean InsertData(SeriallyData psd) {
        if (psd == null) return false;
        if ((BufLen + 3 + psd.Head.DataLen) > Capacity) return false;
        SeriallyData pData = packetMap.get(psd.Head.Flag);
        if (pData != null) {
            pData = null;
            SeriallyData newData = new SeriallyData();
            newData.Assign(psd);
            pData = newData;
        } else {
            SeriallyData newData = new SeriallyData();
            newData.Assign(psd);
            packetMap.put((int) psd.Head.Flag, newData);
        }
        return true;
    }

    private boolean WriteDataU(int iFlag, Object obj) {
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        short itype = 0;
        int iDatalen = 0;
        byte[] Buf = null;
        if (obj instanceof Integer) {
            itype = (short) bdtUInt16.ordinal();
            iDatalen = 2;
            Buf = new byte[iDatalen];
            boardByteBuf.AssignWriteBuf(Buf);
            boardByteBuf.WriteUShort((int) obj);

        } else if (obj instanceof Long) {
            itype = (short) bdtUInt32.ordinal();
            iDatalen = 4;
            Buf = new byte[iDatalen];
            boardByteBuf.AssignWriteBuf(Buf);
            boardByteBuf.WriteUint((Long) obj);

        }
        if (Buf == null) return false;
        SeriallyData newData = new SeriallyData();
        newData.Assign((short) iFlag, Buf, iDatalen);
        SeriallyData data = packetMap.get(iFlag);
        if (data != null) {
            data = null;
            data = newData;
        } else {
            packetMap.put(iFlag, newData);
        }
        return true;
    }

    private boolean WriteData(int iFlag, byte[] buf, int ilen) {
        SeriallyData newData = new SeriallyData();
        newData.Assign((short) iFlag, buf, ilen);
        SeriallyData data = packetMap.get(iFlag);
        if (data != null) {
            data = null;
            data = newData;
        } else {
            packetMap.put(iFlag, newData);
        }
        return true;
    }

    private boolean WriteData(int iFlag, Object obj) {
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        byte[] Buf = null;
        short itype = 0;
        int iDatalen = 0;
        if (obj instanceof Boolean) {
            itype = (short) bdtBool.ordinal();
            iDatalen = 1;
            Buf = new byte[iDatalen];
            boardByteBuf.AssignWriteBuf(Buf);
            boardByteBuf.WriteBool((Boolean) obj);

        } else if (obj instanceof Byte) {
            itype = (short) bdtChar.ordinal();
            iDatalen = 1;
            Buf = new byte[iDatalen];
            boardByteBuf.AssignWriteBuf(Buf);
            boardByteBuf.WriteUByte((Short) obj);

        } else if (obj instanceof Short) {
            itype = (short) bdtInt16.ordinal();
            iDatalen = 2;
            Buf = new byte[iDatalen];
            boardByteBuf.AssignWriteBuf(Buf);
            boardByteBuf.WriteShort((Short) obj);

        } else if (obj instanceof Integer) {
            itype = (short) bdtInt32.ordinal();
            iDatalen = 4;
            Buf = new byte[iDatalen];
            boardByteBuf.AssignWriteBuf(Buf);
            boardByteBuf.WriteInt((Integer) obj);

        } else if (obj instanceof Long) {
            itype = (short) bdtInt64.ordinal();
            iDatalen = 8;
            Buf = new byte[iDatalen];
            boardByteBuf.AssignWriteBuf(Buf);
            boardByteBuf.WriteLong((Long) obj);

        } else if (obj instanceof Double) {
            itype = (short) bdtDouble.ordinal();
            iDatalen = 8;
            Buf = new byte[iDatalen];
            boardByteBuf.AssignWriteBuf(Buf);
            boardByteBuf.WriteDouble((Double) obj);

        } else if (obj instanceof NetInfo) {
            itype = (short) bdtIP.ordinal();
            iDatalen = 6;
            Buf = new byte[iDatalen];
            ((NetInfo) obj).WriteTo(Buf);
        } else if (obj instanceof String) {
            try {
                itype = (short) bdtString.ordinal();
                String str = (String) obj;
                iDatalen = str.getBytes("GB2312").length + 1;

                Buf = new byte[iDatalen];
                boardByteBuf.AssignWriteBuf(Buf);
                boardByteBuf.WriteString((String) obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Buf == null) return false;

        SeriallyData newData = new SeriallyData();
        newData.Assign((short) iFlag, Buf, iDatalen);
        SeriallyData data = packetMap.get(iFlag);
        if (data != null) {
            data = null;
            data = newData;
        } else {
            packetMap.put(iFlag, newData);
        }
        return true;
    }

    public boolean InsertData(int iFlag, boolean bv) {
        return WriteData(iFlag, bv);
    }

    public boolean InsertData(int iFlag, int lvalue) {
        return WriteData(iFlag, lvalue);
    }

    public boolean InsertData(int iFlag, short svalue) {
        return WriteData(iFlag, svalue);
    }

    public boolean InsertDataU(int iFlag, long lvalue) {
        return WriteDataU(iFlag, lvalue);
    }

    public boolean InsertDataU(int iFlag, int ivalue) {
        return WriteDataU(iFlag, ivalue);
    }

    public boolean InsertDataU(int iFlag, short svalue) {
        return WriteDataU(iFlag, svalue);
    }

    public boolean InsertData(int iFlag, byte[] buffer) {
        return WriteData(iFlag, buffer);
    }

    public boolean InsertData(int iFlag, String str) {
        return WriteData(iFlag, str);
    }

    public boolean InsertData(int iFlag, byte[] pb, int iLen) {
        return WriteData(iFlag, pb, iLen);
    }

    public boolean InsertData(int iFlag, NetInfo netInfo) {
        return WriteData(iFlag, netInfo);
    }

    public boolean InsertData(int iFlag, long i64) {
        return WriteData(iFlag, i64);
    }

    public boolean InsertData(int iFlag, double dv) {
        return WriteData(iFlag, dv);
    }

    public boolean InsertSubData(int iFlag, SeriallyPacket pack) {
        return InsertData(iFlag, pack.Data(), pack.GetLen());
    }

    public int AsInt(int iFlag) {
        Object obj = packetMap.get(iFlag);
        if (obj == null) return 0;
        else {
            byte[] buf = ((SeriallyData) obj).Data;
            BoardByteBuf boardByteBuf = new BoardByteBuf();
            boardByteBuf.AssignReadBuf(buf);
            int iret = boardByteBuf.ReadInt();
            return iret;
        }
    }


    public long  AsUInt(int iFlag) {
        Object obj = packetMap.get(iFlag);
        if (obj == null) return 0;
        else {
            byte[] buf = ((SeriallyData) obj).Data;
            BoardByteBuf boardByteBuf = new BoardByteBuf();
            boardByteBuf.AssignReadBuf(buf);
            long  iret = boardByteBuf.ReadUInt();
            return iret;
        }
    }

    public boolean AsBoolean(int iFlag)
    {
        Object obj=packetMap.get(iFlag);
        if (obj==null) return false;
        else {
            byte[] buf = ((SeriallyData) obj).Data;
            BoardByteBuf boardByteBuf = new BoardByteBuf();
            boardByteBuf.AssignReadBuf(buf);
            short iret = boardByteBuf.ReadUByte();
            if (iret==1) return true;
            else return  false;
        }

    }


    public String AsString(int iFlag) {
        try {
            Object obj = packetMap.get(iFlag);
            String sret = "";
            if (obj == null) return "";
            else {
                byte[] buf = ((SeriallyData) obj).Data;
                sret = new String(buf, "GB2312");
                //删除字符串末尾的/0字符
                sret = sret.replaceAll("\0", "");
                return sret;
            }
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public byte[] AsBytes(int iFlag) {
        Object obj = packetMap.get(iFlag);
        if (obj == null) return null;
        else {
            byte[] buf = ((SeriallyData) obj).Data;
            return buf;
        }

    }


    public void Assign(SeriallyPacket sourcePacket) {
        packetMap.clear();
        Iterator iter = packetMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int ikey = (int) entry.getKey();
            SeriallyData obj = (SeriallyData) entry.getValue();
            SeriallyData newobj = new SeriallyData();
            newobj.Assign(obj);
            packetMap.put(ikey, newobj);
        }
    }


    public void Assign(byte[] buf, int iLen) {
        try {
            packetMap.clear();
            int iPos = 0;
            int iFlag = 0;
            int iDatalen = 0;
            byte[] databuf = null;
            BoardByteBuf boardByteBuf = new BoardByteBuf();
            boardByteBuf.AssignReadBuf(buf);
            while (boardByteBuf.GetReadPos() < iLen) {
                iFlag = boardByteBuf.ReadUByte();
                iDatalen = boardByteBuf.ReadUShort();
                databuf = boardByteBuf.ReadByteArr(iDatalen);
                SeriallyData Obj = new SeriallyData();
                Obj.Assign((short) iFlag, databuf, iDatalen);
                packetMap.put(iFlag, Obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int Command() {
        return AsInt(0xFF);
    }

    public void SetCommand(int iCmd) {
        InsertData(0xFF, iCmd);
    }

    public SeriallyPacket GetSubData(int iFlag) {
        byte[] buf = AsBytes(iFlag);
        SeriallyPacket retPacket = new SeriallyPacket();
        retPacket.Assign(buf, buf.length);
        return retPacket;
    }


    public NetInfo AsNetInfo(int iFlag) {
        NetInfo net = new NetInfo();
        boolean bRet = sd.FindFlag(Buffer, BufLen, iFlag);
        if (bRet) {
            net.ReadFrom(sd.Data);
        }
        return net;
    }

    public long AsInt64(int iFlag) {
        Object obj = packetMap.get(iFlag);
        if (obj == null) return 0;
        else {
            byte[] buf = ((SeriallyData) obj).Data;
            BoardByteBuf boardByteBuf = new BoardByteBuf();
            boardByteBuf.AssignReadBuf(buf);
            long lret = boardByteBuf.ReadLong();
            return lret;
        }
    }



    public double AsDouble(int iFlag) {
        Object obj = packetMap.get(iFlag);
        if (obj == null) return 0;
        else {
            byte[] buf = ((SeriallyData) obj).Data;
            BoardByteBuf boardByteBuf = new BoardByteBuf();
            boardByteBuf.AssignReadBuf(buf);
            double dret = boardByteBuf.ReadDouble();
            return dret;
        }
    }

    public short AsUShort(int iFlag) {
        Object obj = packetMap.get(iFlag);
        if (obj == null) return 0;
        else {
            byte[] buf = ((SeriallyData) obj).Data;
            BoardByteBuf boardByteBuf = new BoardByteBuf();
            boardByteBuf.AssignReadBuf(buf);
            short dret = (short) boardByteBuf.ReadUShort();
            return dret;
        }
    }

    public short AsShort(int iFlag) {
        Object obj = packetMap.get(iFlag);
        if (obj == null) return 0;
        else {
            byte[] buf = ((SeriallyData) obj).Data;
            BoardByteBuf boardByteBuf = new BoardByteBuf();
            boardByteBuf.AssignReadBuf(buf);
            short dret =  boardByteBuf.ReadShort();
            return dret;
        }
    }

    public void Delete(int iFlag) {
        Object obj = packetMap.get(iFlag);
        if (obj != null) {
            packetMap.remove(iFlag);
        }
    }
}
