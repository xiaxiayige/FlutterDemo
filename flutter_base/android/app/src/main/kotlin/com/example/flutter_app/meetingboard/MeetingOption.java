package com.example.flutter_app.meetingboard;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.flutter_app.meetingboard.BoardConstants.MeetingStatu.msPrepare;

public class MeetingOption {

    public long MeetingID;
    public short Count;
    public short Capacity;

    public boolean HasBoard;
    public boolean Locked;
    public int  CtrlMode;
    public int  Statu;

    public byte Type;
    public boolean HasPatch;
    public boolean AutoCloseMic;
    public MtLayout Layout;

    public int SpkCapacity;
    public MeetingServer DataServer;
    public MeetingServer MtServer;

    public long ExtendUser;
    public long Manager;
    // public TDateTime StartTime;
    public String Name;
    public String Description;
    public String Password;
    public String UserGuid;
    public int BindWidth;
    public boolean AllowRecord;
    /*   void *TcpSock;
       void *VideoTcp;
       TTcpFile TcpFile;
       void *ScreenTcp;
       */
    NetInfo ScreenHost;

    public TcpFile tcpFile;
    public long LectureSize;
    public String LectureUrl;
    public long FullScrUser;
    public long ShareScreenUser;

    public String Subject;
    public String Welcome;
    public String AdUrl;
    public String PubBackImg;
    public ArrayList<String> LinkText;
    public ArrayList<String> LinkUrl;


      public MeetingOption() {
        Reset();
    }

    public void Reset() {
        MtServer=new MeetingServer();
        AutoCloseMic = false;

        ShareScreenUser = 0;
       /* TcpSock=NULL;
        VideoTcp=NULL;
        ScreenTcp=NULL;
        */
        MeetingID = 0;
        Count = 0;
        Capacity = 0;
        Manager = 0;
        SpkCapacity = 0;
        HasPatch = false;
        LectureUrl = "";
        Name = "";
        Description = "";
        Password = "";
        UserGuid = "";
        LectureSize = 0;
        Locked = false;
        HasBoard = false;
        Statu = (byte) msPrepare.ordinal();
        AllowRecord = false;
        ExtendUser = 0;
        Layout=new MtLayout();
        Layout.setValue(6);
        Type = 0;
        BindWidth = 0;
        FullScrUser = 0;
        CtrlMode = 1;
        tcpFile=new TcpFile();
    }

    public void ParseFromBytes(byte[] pb,int iLen) {
          SeriallyPacket packet=new SeriallyPacket();
          packet.Assign(pb,iLen);
        MeetingID = packet.AsUInt(0x01);
        //TcpFile.SetRoot(TcpFile.GetRoot()+IntToStr(MeetingID));
        Name = packet.AsString(0x02);
        Capacity = (short) packet.AsInt(0x03);
        Type = (byte) packet.AsInt(0x04);
        Manager = packet.AsInt(0x06);
        if (Password.equals("")) Password = packet.AsString(0x0B);
        Statu = (byte)packet.AsInt(0x0E);
        Count =(short) packet.AsInt(0x0F);
        Locked = packet.AsBoolean(0x10);
        LectureUrl = packet.AsString(0x16);
        AllowRecord = packet.AsBoolean(0x18);
        SpkCapacity = packet.AsInt(0x19);
        LectureSize = packet.AsInt(0x1A);
        CtrlMode =  packet.AsShort(0x24);

        int ilayout=packet.AsInt(0x25);
        if (Layout.getLock()) {
            MtLayout tmplayout=new MtLayout();
            tmplayout.setValue(ilayout);
            Layout.setValue(tmplayout.getValue());
        }
        ExtendUser = packet.AsUInt(0x26);
        //tcpFile.SetRoot(packet.AsString(0x27));

        String sValue= packet.AsString(0x27);
        /*if (TcpFile.HttpPre.Length() > 0) {
            ch =*(TcpFile.HttpPre.AnsiLastChar());
            if (ch != '\\' && ch != '/') TcpFile.HttpPre = TcpFile.HttpPre + "/";
        }*/
        tcpFile.SetRoot(sValue);


        BindWidth = packet.AsInt(0x30);
        FullScrUser = packet.AsUInt(0x31);
        ShareScreenUser = packet.AsUInt(0x32);

        AutoCloseMic = packet.AsBoolean(0x34);

        byte[] buffer=packet.AsBytes(0x81);
        MtServer.ParseFromBytes(buffer,buffer.length);

    }

    void Assign( MeetingOption  pm) {
        DataServer = pm.DataServer;
        MtServer = pm. MtServer;
        MeetingID = pm.MeetingID;
        Count = pm.Count;
        Capacity = pm.Capacity;
        HasBoard = pm.HasBoard;
        Statu = pm.Statu;
        Manager = pm.Manager;
        SpkCapacity = pm.SpkCapacity;
        //StartTime = pm -> StartTime;
        Name = pm.Name;
        Description = pm.Description;
        Password = pm.Password;
        Locked = pm.Locked;
        AllowRecord = pm.AllowRecord;
        LectureSize = pm.LectureSize;
        LectureUrl = pm.LectureUrl;
        AutoCloseMic = pm.AutoCloseMic;
    }

 //   boolean  ConnectTcpServer();

//    void CloseTcpSock();

  //  void __fastcall    TcpSockError(TObject *Sender,  TCustomWinSocket *Socket, TErrorEvent ErrorEvent, int &ErrorCode);

    public void SetServer(String sHost){
        Pattern p1 = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)");
        Matcher m1 = p1.matcher(sHost);
        String sIP="";
        String sPort="";
        BoardUtils boardtool=new BoardUtils();
        // 将符合规则的提取出来
        while (m1.find()) {
            //ip地址
            sIP=m1.group(1);
            //端口
            sPort=m1.group(2);

          //  TcpFile.Host=sHost.SubString(1,iv-1);
            MtServer.IP=boardtool.ipToInt(sIP);
            MtServer.Port= Integer.parseInt(sPort);
            tcpFile.Host=sIP;
            tcpFile.Port=MtServer.Port;
        }

    }

 //   String GetServer();

 //   void *   ConnectTcp(TNetInfo host, bool bSend);

//    void CloseTcp(void**ppskt);

//    void ParseExtInfo(TSeriallyPacket*pack);




}
