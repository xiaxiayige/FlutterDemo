package com.example.flutter_app.meetingboard;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.OutputStream;

public class WBFileOption {
    public int WindowWidth;
    public int WindowHeight;
    public int ValidWidth;
    public int ValidHeight;
    public WBLineOption LineOption;
    public WBFillOption FillOption;
    public WBFileOption()
    {
        LineOption=new WBLineOption();
        FillOption=new WBFillOption();
    }

    public void WriteToStream(OutputStream outputStream) {

            BoardUtils butils=new BoardUtils();
            butils.WriteUShortToStream(WindowWidth,outputStream);
            butils.WriteUShortToStream(WindowHeight,outputStream);
            butils.WriteUShortToStream(ValidWidth,outputStream);
            butils.WriteUShortToStream(ValidHeight,outputStream);
            LineOption.WriteToStream(outputStream);
            FillOption.WriteToStream(outputStream);

    }

    public void ReadFromStream(DocumentInputStream inputStream)
    {
//通过buffer读取各个成员的值
           WindowWidth=inputStream.readUShort();
           WindowHeight= inputStream.readUShort();
           ValidWidth=inputStream.readUShort();
           ValidHeight=inputStream.readUShort();
           LineOption.ReadFromStream(inputStream);
           FillOption.ReadFromStream(inputStream);


    };

    ;
}
