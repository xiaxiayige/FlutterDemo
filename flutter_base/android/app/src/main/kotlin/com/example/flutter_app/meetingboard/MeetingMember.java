package com.example.flutter_app.meetingboard;

public class MeetingMember {
    public long UserID;
    public long Photo;
    public int Icon;
    public int Statu;
    public boolean Camera;
    public boolean RefuseAudio;
    public boolean RefuseVideo;
    public MtPurview Purview;
    public int Type;
    public long ExternID;
    public String Nick;
    public int DeviceType;

    // TDateTime StatuTick;
    private void Reset() {
        UserID = 0;
        Icon = 0;
        Camera = false;
        Purview = new MtPurview();
        Nick = "";
        Photo = 0;
        RefuseAudio = false;
        RefuseVideo = false;
        Statu = 0;
        ExternID = 0;
        Type = 0;
        DeviceType=1;       //android hd
    }

    public MeetingMember() {
        Reset();
    }

    public void ParseFromBytes(byte[] pbData, int iLen) {
        SeriallyPacket seriallyPacket = new SeriallyPacket();
        seriallyPacket.Assign(pbData, iLen);
        UserID = seriallyPacket.AsUInt(0x01);
        Icon = seriallyPacket.AsInt(0x02);
        Statu = seriallyPacket.AsInt(0x03);
        Nick = seriallyPacket.AsString(0x04);
        Camera = seriallyPacket.AsBoolean(0x05);
        Photo = seriallyPacket.AsUInt(0x06);
        Purview = new MtPurview(seriallyPacket.AsInt(0x07));
        ExternID = seriallyPacket.AsInt(0x08);
        DeviceType=seriallyPacket.AsInt(0x0C);
        Type = seriallyPacket.AsInt(0x1B);
    }

    public void Assign(MeetingMember pm) {
        if (pm == null) return;
        UserID = pm.UserID;
        Icon = pm.Icon;
        Camera = pm.Camera;
        Purview.setValue(pm.Purview.getValue());
        Nick = pm.Nick;
        Photo = pm.Photo;
        Statu = pm.Statu;
        ExternID = pm.ExternID;
        Type = pm.Type;
        DeviceType=pm.DeviceType;
    }

    public boolean IsSpeaker() {
        return (Purview.getAudio() || Purview.getVideo() || Purview.getBoard());
    }

    public long GetExtID() {
        return (ExternID != 0 ? ExternID : UserID);
    }

    public void WriteTo(Object pv) {
        if (pv==null) return;
        try {
            SeriallyPacket pack = (SeriallyPacket) pv;
            pack.InsertData(0x01,UserID);
            pack.InsertData(0x02,Icon);
            pack.InsertData(0x03,Statu);
            pack.InsertData(0x04,Nick);
            pack.InsertData(0x05,Camera);
            pack.InsertData(0x06,Photo);
            pack.InsertData(0x07,Purview.getValue());
            pack.InsertData(0x08,ExternID);
            pack.InsertData(0x0C,DeviceType);
          //  pack.InsertData(0x20,StatuTick);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public boolean IsManager() {
        return (Purview.getManager() || (Purview.getAdvMng()));
    }

    public boolean IsVip() {
        return Type == 1 || Type == 4;
    }

    public boolean IsWatcher() {
        return Type == 128;
    }
}
