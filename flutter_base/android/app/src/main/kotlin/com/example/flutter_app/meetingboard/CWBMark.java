package com.example.flutter_app.meetingboard;


import static com.example.flutter_app.meetingboard.BoardConstants.MarkType.e_BookMark;
import static com.example.flutter_app.meetingboard.BoardConstants.MarkType.e_File;

public class CWBMark {
    private String szCaption;
    private String szValue;
    private short XPos;
    private short YPos;
    private int Page;
    private BoardConstants.MarkType Type;
    private long Flag;

    public CWBMark() {
        XPos = 0;
        YPos = 0;
        Page = 0;
        Flag = 0;
        szCaption = "";
        szValue = "";
        SetCaption("");
        SetValue("");
        Type = e_BookMark;
    }

    public String GetFileName() {
        BoardUtils boardtool = new BoardUtils();
        if (Type != e_File) return "";
        String sv = "LK" + String.valueOf(Flag) + boardtool.getExtensionName(szValue);
        return sv;
    }

    public void Assign(CWBMark originMark) {
        if (originMark == null) return;
        Type = originMark.Type;
        if (Type == e_BookMark) {
            XPos = originMark.GetXPos();
            YPos = originMark.GetYPos();
            Page = originMark.GetPage();
        }
        Flag = originMark.GetFlag();
        szCaption = "" + originMark.GetCaption();
        if (Type != e_BookMark) {
            szValue = "" + originMark.GetValue();
        }

    }


    public void SetMark(short x, short y, int iv) {
        XPos = x;
        YPos = y;
        Page = iv;
    }

    public void SetCaption(String sz) {
        szCaption = sz;
    }

    public int BytesCount() {
        try {
            int iLen = szCaption.getBytes("GB2312").length + 4 + 1 + 2;
            if (Type == e_BookMark) iLen += (2 * 3);
            else {
                iLen += szValue.getBytes("GB2312").length;
                iLen += 2;
            }
            return iLen;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long ReadFromBuffer(byte[] buf) {
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        boardByteBuf.AssignReadBuf(buf);
        int iPos = 0;
        Type = BoardConstants.MarkType.values()[boardByteBuf.ReadUByte()];

        if (Type == e_BookMark) {
            Page = boardByteBuf.ReadUShort();
            XPos = (short) boardByteBuf.ReadUShort();
            YPos = (short) boardByteBuf.ReadUShort();

        }
        Flag = boardByteBuf.ReadUInt();
        szCaption = boardByteBuf.ReadString();

        if (Type != e_BookMark) {
            szValue = boardByteBuf.ReadString();

        }
        return boardByteBuf.GetReadPos();
    }

    public long WriteToBuffer(byte[] buf) {
        BoardByteBuf boardByteBuf = new BoardByteBuf();
        boardByteBuf.AssignWriteBuf(buf);
        boardByteBuf.WriteUByte((short) (Type.ordinal()));
        if (Type == e_BookMark) {
            boardByteBuf.WriteUShort(Page);
            boardByteBuf.WriteUShort(XPos);
            boardByteBuf.WriteUShort(YPos);
        }
        boardByteBuf.WriteUint(Flag);
        boardByteBuf.WriteString(szCaption);

        if (Type != e_BookMark) {
            boardByteBuf.WriteString(szValue);
        }
        return boardByteBuf.GetWritePos();
    }

    public void SetFlag(long iv) {
        if (Flag != iv) Flag = iv;
    }

    public short GetXPos() {
        return XPos;
    }

    public short GetYPos() {
        return YPos;
    }

    public int GetPage() {
        return Page;
    }

    public long GetFlag() {
        return Flag;
    }

    public String GetCaption() {
        return szCaption;
    }

    public String GetValue() {
        return szValue;
    }

    public BoardConstants.MarkType GetType() {
        return Type;
    }

    public void SetType(BoardConstants.MarkType mt) {
        if (mt != Type) Type = mt;
    }

    public void SetValue(String ss) {
        szValue = ss;
    }
}
