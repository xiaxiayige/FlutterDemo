package com.example.flutter_app.meetingboard;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class CWBFileVerify {

    public byte[] Md5;;
    public UUID FileID;
    public byte  Verify;
    public byte  DataLen;
    public  byte  Rev;
    public byte[]  Data;
    public CWBFileVerify()
    {
        Md5=new byte[16];
        Data=new byte[61];

    }

    public  void Create()
    {};

    public void setVerify( byte AVerify,byte ALen,byte[] AData) {
    };
    public void Update(){};
    public boolean VerifyIntegrality()
    {
        return true;
    };
    public int getsize()
    {return 96;}

    public void ReadFromStream(DocumentInputStream inputStream)
    {
        try {
            byte[] buffer = new byte[getsize()];
            inputStream.read(buffer);
            //通过buffer读取各个成员的值
        }catch (IOException e)
        {
        }
    };

    public void WriteToStream( OutputStream outputStream)
    {
        try{
            byte[] buffer=new byte[getsize()];
            outputStream.write(buffer);
        }
        catch (IOException e)
        {}
    };
}
