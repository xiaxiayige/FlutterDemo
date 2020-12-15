package com.example.flutter_app.meetingboard;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.OutputStream;

public class CWBFileHead {
    public long Flag; //文件格式标志,白板文件"HWBF",白板录制课件"HBAF"，白板录制视频"HBVF"
    public int Version;//文件版本
    public int Tag;  //解释文件所需的程序版本
    public CWBImage.WBFileOption FileOption;
    public String Author;  //文件作者
    public String CopyRight;//文件版权标志
    public String Comment;//文件备注
    public String Subject;
    public String ReadPwd;
    public String WritePwd;
    public CWBFileVerify Verify;
    public long StartOff;
    public long EndTick;

    public CWBFileHead() {
        Author = "";
        CopyRight = "";
        Comment = "";
        Subject = "";
        ReadPwd = "";
        WritePwd = "";
        Reset();
    }

    ;


    public void Reset() {
        Flag = BoardConstants.rfBoard;
        Version = BoardConstants.WBF_VERSION;
        Tag = 0;
        Tag = 0;
        FileOption = new CWBImage.WBFileOption();

       /* char sz[ 255];
        DWORD iLen = 255;
        GetComputerName(sz, & iLen);
        */

        SetAuthor("");

        SetCopyRight("");
        SetComment("");
        SetSubject("");
        SetReadPwd("");
        SetWritePwd("");
        Verify = new CWBFileVerify();
        Verify.Create();
        StartOff = 0;
        EndTick = 0;
    }

    ;


    long SaveToStream(OutputStream outputstream) {
        long dwRet = 0;

        BoardUtils butils = new BoardUtils();
        butils.WriteUIntToStream(Flag, outputstream);
        butils.WriteUShortToStream(Version, outputstream);
        butils.WriteUShortToStream(Tag, outputstream);
        Verify.WriteToStream(outputstream);
        FileOption.WriteToStream(outputstream);
        Author="test1";
        butils.WriteStringToStream(Author, outputstream);
        CopyRight="test2";
        butils.WriteStringToStream(CopyRight, outputstream);
        butils.WriteStringToStream(Comment, outputstream);
        Subject="test4";
        butils.WriteStringToStream(Subject, outputstream);

        //test
        ReadPwd="read1";
        WritePwd="write1";
        butils.WriteStringToStream(ReadPwd, outputstream);
        butils.WriteStringToStream(WritePwd, outputstream);
        butils.WriteUIntToStream(StartOff, outputstream);
        butils.WriteUIntToStream(EndTick, outputstream);

        return dwRet;
    }

    ;

    long LoadFromStream(DocumentInputStream inputStream) {

        long dwCount;
        int iLen;
        long dwRet = 0;

        BoardUtils boardUtils = new BoardUtils();
        Flag = inputStream.readUInt();
        Version = inputStream.readUShort();
        Tag = inputStream.readUShort();
        if (Version > 10) {
            Verify.ReadFromStream(inputStream);
        }
        FileOption.ReadFromStream(inputStream);
        Author = boardUtils.ReadStringFromStream(inputStream);
        CopyRight = boardUtils.ReadStringFromStream(inputStream);
        Comment = boardUtils.ReadStringFromStream(inputStream);
        Subject = boardUtils.ReadStringFromStream(inputStream);
        ReadPwd = boardUtils.ReadStringFromStream(inputStream);
        WritePwd = boardUtils.ReadStringFromStream(inputStream);
        if (Version > 12) {
            StartOff = inputStream.readUInt();
            EndTick = inputStream.readUInt();
        }

        return dwRet;
    }


    boolean SetAuthor(String sz) {
        Author = sz;
        return true;
    }

    ;

    boolean SetCopyRight(String sz) {
        CopyRight = sz;
        return true;
    }

    ;

    boolean SetComment(String sz) {

        Comment = sz;
        return true;
    }

    ;

    boolean SetSubject(String sz) {

        Subject = sz;
        return true;
    }

    ;

    boolean SetReadPwd(String sz) {

        ReadPwd = sz;
        return true;
    }

    ;

    boolean SetWritePwd(String sz) {

        WritePwd = sz;
        return true;
    }

    ;

    String GetSubject() {
        return Subject;
    }

    ;

    String GetAuthor() {
        return Author;
    }

    ;

    String GetCopyRight() {
        return CopyRight;
    }

    ;

    String GetComment() {
        return Comment;
    }

    ;

    String GetReadPwd() {
        return ReadPwd;
    }

    ;

    String GetWritePwd() {
        return WritePwd;
    }

    ;
}
