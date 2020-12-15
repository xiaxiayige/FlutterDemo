package com.example.flutter_app.meetingboard;


import static com.example.flutter_app.meetingboard.BoardConstants.MeetingServerFlag.*;

public class MeetingServer {

    public long ServerID;
    public int IP;
    public int Port;
    public int Capacity;
    public int Online;
    public String Name;

    public MeetingServer() {
        ServerID = 0;
        IP = 0;
        Port = 0;
        Capacity = 0;
        Online = 0;
        Name = "";
    }

    public void ParseFromBytes(byte[] pbData, int iLen) {
        SeriallyPacket packet = new SeriallyPacket();
        packet.Assign(pbData, iLen);
        ServerID = packet.AsInt(fms_SID.ordinal());
        IP = packet.AsInt(fms_IP.ordinal());
        Port = packet.AsInt(fms_Port.ordinal());
        Capacity = packet.AsInt(fms_Capacity.ordinal());
        Online = packet.AsInt(fms_Online.ordinal());
        Name = packet.AsString(fms_Name.ordinal());
    }

   /*
    bool TMeetingServer::GetAddress(void* pv)
    {
        if(pv==NULL) return false;
        sockaddr_in ad=(sockaddr_in*)pv;
        ad->sin_family=AF_INET;
        ad->sin_addr.S_un.S_addr=IP;
        ad->sin_port=htons(Port);
        return true;
    }
    */

}
