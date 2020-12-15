package com.example.flutter_app.meetingboard;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.IOException;
import java.io.OutputStream;

public class CWBObject {
    protected boolean Visible;
    protected boolean Locked;
    protected int Creator;
    protected int Mark;
    protected boolean Deleting;
    protected boolean Active;
    public long ObjectID;
    public BoardConstants.WBMainType MainType;
    public BoardConstants.WBShapeType SubType;
    public int Z_Order;
    public int Page;
    public Rect ClientRect;
    public String Caption;

    public void Move(int xof, int yof) {
        ClientRect.left = ClientRect.left + xof;
        ClientRect.right = ClientRect.right + xof;
        ClientRect.top = ClientRect.top + yof;
        ClientRect.bottom = ClientRect.bottom + yof;
    }

    public void Resize(Rect rt) {
        BoardUtils boardtool = new BoardUtils();
        ClientRect = boardtool.ReviseRect(rt);
    }


    public void CWBObject(short iMain, short iSub) {
        MainType = BoardConstants.WBMainType.values()[iMain];
        SubType = BoardConstants.WBShapeType.values()[iSub];
        ObjectID = 0;
        Z_Order = 32000;
        Visible = true;
        Locked = false;
        Creator = 0;
        Page = 0;
        Mark = 0;
        Caption = "";
        Deleting = false;
        Active = false;
        ClientRect = new Rect();
    }

    public CWBObject() {
        short smaintype = (short) (BoardConstants.WBMainType.wbmtUnknown.ordinal());
        short ssubtype = (short) (BoardConstants.WBShapeType.wbstUnknown.ordinal());
        CWBObject(smaintype, ssubtype);
    }


    public void Assign(CWBObject SrcObj) {
        Visible = SrcObj.Visible;
        Locked = SrcObj.Locked;
        Creator = SrcObj.Creator;
        ObjectID = SrcObj.ObjectID;
        MainType = SrcObj.MainType;
        SubType = SrcObj.SubType;
        Z_Order = SrcObj.Z_Order;
        Page = SrcObj.Page;
        ClientRect = SrcObj.ClientRect;
        Mark = SrcObj.GetMark();
        Caption = SrcObj.Caption;
    }

    public boolean InRegion(Point pt) {
        return InRegion(pt.x, pt.y);
    }

    public boolean InRegion(int x, int y) {
        BoardUtils boardtool = new BoardUtils();

        return boardtool.InRect(ClientRect, x, y);
    }

    public boolean Overlapped(Rect rect) {
        boolean ret = false;
        ret = InRegion(rect.left, rect.top) ||
                InRegion(rect.right, rect.top) ||
                InRegion(rect.right, rect.bottom) ||
                InRegion(rect.left, rect.bottom);
        if (!ret) {
            ret = (rect.left < ClientRect.left) &&
                    (rect.top < ClientRect.top) &&
                    (rect.bottom > ClientRect.bottom) &&
                    (rect.right > ClientRect.right);
        }
        return ret;
    }


    public void Draw(Canvas pDc, boolean bMask, boolean bPreview) {

    }

    public void Draw(Canvas pDc) {
        boolean bMask=false;
        boolean bPreview = false;
        Draw(pDc, false, bPreview);
    }

    public long ReadFromBuffer(byte[] buf) {
        BoardByteBuf buftool = new BoardByteBuf();
        buftool.AssignReadBuf(buf);
        int iv = buftool.ReadUShort();
        Visible = (iv & 0x8000) > 0;
        Locked = (iv & 0x4000) > 0;
        Mark = (iv & 0x3FFF);
        Creator = buftool.ReadUShort();
        ObjectID = buftool.ReadUInt();
        MainType = BoardConstants.WBMainType.values()[buftool.ReadUByte()];
        SubType = BoardConstants.WBShapeType.values()[buftool.ReadUByte()];
        Z_Order = buftool.ReadUShort();
        Page = buftool.ReadUShort();
        ClientRect = buftool.ReadRect();
        //读取Caption属性
        Caption = buftool.ReadString();
        return buftool.GetReadPos();
    }


    public long WriteToBuffer(byte[] buf, int startPos) {

        return local_WriteToBuffer(buf, startPos);

    }

    public  long WriteToBuffer(byte[] buf ) {
       int startPos=0;
        return local_WriteToBuffer(buf, startPos);

    }
    private long local_WriteToBuffer(byte[] buf)
    {
        int startPos=0;
        return local_WriteToBuffer(buf,startPos);
    }
    private long local_WriteToBuffer(byte[] buf, int startPos) {
        BoardByteBuf buftool = new BoardByteBuf();
        buftool.AssignWriteBuf(buf);
        int iv = Mark;
        if (Visible) iv = (iv | 0x8000);
        if (Locked) iv = (iv | 0x4000);
        buftool.WriteUShort(iv);
        buftool.WriteUShort(Creator);
        buftool.WriteUint(ObjectID);
        buftool.WriteUByte((short) (MainType.ordinal()));
        buftool.WriteUByte((short) (SubType.ordinal()));
        buftool.WriteUShort(Z_Order);
        buftool.WriteUShort(Page);
        buftool.WriteRect(ClientRect);
        buftool.WriteStringZeroTail(Caption);
        return buftool.GetWritePos();
    }

    public void ReviseClientRect() {
        BoardUtils boardtool = new BoardUtils();
        ClientRect = boardtool.ReviseRect(ClientRect);
    }

    public boolean isVisible() {
        return Visible;
    }

    public boolean IsLocked() {
        return Locked;
    }

    public boolean Show(boolean bShow) {
        if (Visible != bShow) Visible = bShow;
        return Visible;
    }

    public boolean Lock(boolean bLock) {
        if (Locked != bLock) Locked = bLock;
        return Locked;
    }

    public int Width() {
        return ClientRect.right - ClientRect.left + 1;
    }

    public int Height() {
        return ClientRect.bottom - ClientRect.top + 1;
    }

    public int Left() {
        return ClientRect.left;
    }

    public int Top() {
        return ClientRect.top;
    }

    public int BaseBytesCount() {
        int iLen = 0;
        try {
            iLen++; //visible
            iLen++; //locked
            iLen += 2; //creator
            iLen += 4; //object id
            iLen++; //main type
            iLen++; //subtype
            iLen += 2; //Z_Order
            iLen += 2;   //Page
            iLen += 16;   //client rect
            iLen += Caption.getBytes("GB2312").length + 2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iLen;
    }

    public boolean CanControl(int user, int ad) {
        return true;
    }

    public int GetCreator() {
        return Creator;
    }

    public void SetCreator(int iUser) {
        Creator = iUser;
    }

    public boolean HasMark() {
        return (Mark != 0);
    }

    public int GetMark() {
        return Mark;
    }

    public void SetMark(int ww) {
        Mark = ww;
    }

    public boolean IsAudio() {
        if (MainType == BoardConstants.WBMainType.wbmtMedia) {
            if (SubType.ordinal() == BoardConstants.WBMediaType.wbmdWinAudio.ordinal() ||
                    SubType.ordinal() == BoardConstants.WBMediaType.wbmdRealAudio.ordinal())
                return true;
        }
        return false;

    }

    public boolean IsVideo() {
        if (MainType == BoardConstants.WBMainType.wbmtMedia) {
            if (SubType.ordinal() == BoardConstants.WBMediaType.wbmdWinVideo.ordinal() ||
                    SubType.ordinal() == BoardConstants.WBMediaType.wbmdRealVideo.ordinal())
                return true;
        }
        return false;
    }

    public boolean IsFlash() {
        if (MainType == BoardConstants.WBMainType.wbmtMedia && SubType.ordinal() == BoardConstants.WBMediaType.wbmdFlash.ordinal())
            return true;
        else return false;
    }

    public boolean CanRestore() {
        if (MainType == BoardConstants.WBMainType.wbmtMedia) return false;
        if (MainType == BoardConstants.WBMainType.wbmtShape) {
            if (SubType == BoardConstants.WBShapeType.wbstIndicator || SubType == BoardConstants.WBShapeType.wbstLight)
                return false;
        }
        return true;
    }


    public boolean IsDeleted() {
        return Deleting;
    }

    public void Delete() {
        Deleting = true;
    }

    public void Restore(Object parent) {
        Deleting = false;
    }

    public long LoadFromStream(DocumentInputStream inputStream) {
        int iSize = BaseBytesCount();
        byte[] buf = new byte[300];
        try {
            inputStream.read(buf, 0, iSize);
            int icaptionlen = (int) (buf[iSize - 2] & 0xff);
            inputStream.read(buf, iSize, icaptionlen - 1);
            ReadFromBuffer(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long SaveToStream(OutputStream outputStream) {
        try {
            int iSize = BaseBytesCount();
            byte[] buf = new byte[iSize];
            local_WriteToBuffer(buf);
            outputStream.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
