package com.example.flutter_app.meetingboard;

public class TcpBuffer {
  public int Capacity;
  public int RcvLen;
  public  byte[] Buffer;
  public  TcpBuffer(int iCap)
    {
        Capacity=iCap;
        RcvLen=0;
        Buffer=new byte[iCap];
    }

    //追加数据
    public boolean AddBuf(byte[] dataBuf,int ilen)
    {
      if (ilen+RcvLen>Capacity) return false;
      else {
        System.arraycopy(dataBuf,0, Buffer, RcvLen, ilen);
        RcvLen=RcvLen+ilen;
        return true;
      }
    }

    public SeriallyPacket  ReadPacket()
    {
      SeriallyPacket retPacket;
        byte[] ret=null;
        BoardByteBuf boardByteBuf=new BoardByteBuf();
        boardByteBuf.AssignReadBuf(Buffer);
        int ilen=boardByteBuf.ReadInt();
        if (ilen+4<=RcvLen)
        {
            byte[] databuf=boardByteBuf.ReadByteArr(ilen);
            retPacket=new SeriallyPacket();
            retPacket.Assign(databuf,ilen);
            RcvLen=RcvLen-4-ilen;
            //将缓冲区剩余内容前移到开头
            if (RcvLen>0)
            System.arraycopy(Buffer,4+ilen,Buffer,0,RcvLen);
            return retPacket;
        }
        else return null;
    }

}
