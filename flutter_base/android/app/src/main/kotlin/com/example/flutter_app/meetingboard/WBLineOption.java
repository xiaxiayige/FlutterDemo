package com.example.flutter_app.meetingboard;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.OutputStream;

public class WBLineOption {
    public  int  Color; //线的颜色
    public short Width;//线的宽度
    public short Style;   //线的样式
    public int  Alpha; //线的透明度
    public int BytesCount()
    {
        return 8;
    }
    public  void WriteToStream(OutputStream outputStream){
        BoardUtils boardUtils=new BoardUtils();
        boardUtils.WriteColorToStream(Color,outputStream);
        boardUtils.WriteUByteToStream(Width,outputStream);
        boardUtils.WriteUByteToStream(Style,outputStream);
        boardUtils.WriteUShortToStream(Alpha,outputStream);
    }
    public long ReadFromBuffer(byte[] buffer,int startpos)
    {
            BoardByteBuf boardtool=new BoardByteBuf();
            boardtool.AssignReadBuf(buffer,startpos);
            Color =boardtool.ReadInt();
            Width=boardtool.ReadUByte();
            Style=boardtool.ReadUByte();
            Alpha=boardtool.ReadUShort();
            return boardtool.GetReadPos();
    }
    public long WriteToBuffer(byte[] buffer,int startpos)
    {
            BoardByteBuf boardtool=new BoardByteBuf();
            boardtool.AssignWriteBuf(buffer,startpos);
            boardtool.WriteInt(Color);
            boardtool.WriteUByte(Width);
            boardtool.WriteUByte(Style);
            boardtool.WriteUShort(Alpha);
            return  boardtool.GetWritePos();
    }
    public void ReadFromStream(DocumentInputStream inputStream)
    {

        Color=inputStream.readInt();
        Width=(short) inputStream.readUByte();
        Style=(short) inputStream.readUByte();
        Alpha=inputStream.readUShort();
    }
}
