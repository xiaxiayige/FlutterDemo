package com.example.flutter_app.meetingboard;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.OutputStream;

public class WBFillOption {
    public int  FillType; //填充类别,可选值包括：无填充，纯颜色，图案，图片
    public int  FillStyle;    //填充样式,只有填充类别为图案或图片时才有效，可选值包括：居中，平铺，拉伸，
    public long  FillData;  //填充数据，无填充时值为NULL，颜色填充时值为颜色值，图案填充时值为图案编号，图片填充时为图片编号

    public int BytesCount()
    {
        return 8;
    }
    public void WriteToStream( OutputStream outputStream){
        BoardUtils boardUtils=new BoardUtils();
        boardUtils.WriteUShortToStream(FillType,outputStream);
        boardUtils.WriteUShortToStream(FillStyle,outputStream);
         boardUtils.WriteUIntToStream(FillData,outputStream);
    }
    public long ReadFromBuffer(byte[] buffer,int startpos)
    {
            BoardByteBuf boardtool=new BoardByteBuf();
            boardtool.AssignReadBuf(buffer,startpos);
            FillType=boardtool.ReadUShort();
            FillStyle=boardtool.ReadUShort();
            FillData=boardtool.ReadUInt();
            return boardtool.GetReadPos();
    }
    public long WriteToBuffer(byte[] buffer,int startpos)
    {
        BoardByteBuf boardtool=new BoardByteBuf();
        boardtool.AssignWriteBuf(buffer,startpos);
        boardtool.WriteUShort(FillType);
        boardtool.WriteUShort(FillStyle);
        boardtool.WriteUint(FillData);
        return boardtool.GetWritePos();
    }
    public void ReadFromStream(DocumentInputStream inputStream){

        FillType=inputStream.readUShort();
        FillStyle=inputStream.readUShort();
        FillData=inputStream.readUInt();
    }
}
