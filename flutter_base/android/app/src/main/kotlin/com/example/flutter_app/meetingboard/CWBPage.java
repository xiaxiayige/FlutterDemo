package com.example.flutter_app.meetingboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.OutputStream;
import java.util.ArrayList;

import static com.example.flutter_app.meetingboard.BoardConstants.WBMainType.wbmtImage;
import static com.example.flutter_app.meetingboard.BoardConstants.WBMainType.wbmtShape;
import static com.example.flutter_app.meetingboard.BoardConstants.WBMainType.wbmtText;


public class CWBPage {
    private boolean Visible;
    private short PageID;
    private int Creator;
    private ArrayList<CWBObject> m_ObjList;
    public int Width;
    public int Height;
    public int Rev0;
    public int Direct_Lock;//0:nolocked ,1:locked
    public int Direction;// 1:v  ,2:h
    public int PageBackColor;
    public int TextListCount;
    public boolean bPptPage;
    public int DocType;
    public ArrayList<CWBObject> TextListItems;
    public boolean Locked;

    public CWBPage(int id) {
        Visible = true;
        PageID = (short) id;
        TextListCount = 0;
        TextListItems = new ArrayList<CWBObject>();
        m_ObjList = new ArrayList<CWBObject>();
        Direct_Lock = 0;
        Direction = 1;
        PageBackColor = Color.rgb(255, 255, 255);
        bPptPage = false;
        DocType = 0;
        Locked = false;

    }

    public boolean IsLocked()
    {
        return  Locked;
    }

    //上移一个对象
    public boolean Up(CWBObject Obj) {
        if (Locked) return false;
        if (Obj == null) return false;
        long lPos = FindByObjectID(Obj.ObjectID);
        if (lPos < 0) return false;
        long lIns = GetLastByOrder(Obj.Z_Order + 1) - 1;
        if (lIns == lPos) return false;
        m_ObjList.remove(lPos);
        Obj.Z_Order = Obj.Z_Order + 1;
        m_ObjList.add((int) lIns, Obj);

        return true;

    }

    //下移一下对象
    public boolean Down(CWBObject Obj) {
        if (Locked) return false;
        if (Obj == null) return false;
        long lPos = FindByObject(Obj);
        if (lPos < 0) return false;
        long lIns = GetFirstByOrder(Obj.Z_Order - 1);
        if (lIns == lPos) return false;
        m_ObjList.remove(lPos);
        Obj.Z_Order = Obj.Z_Order - 1;
        m_ObjList.add((int) lIns, Obj);
        return true;
    }

    //将对象置于顶层
    public boolean GoTop(CWBObject Obj) {
        long lPos = FindByObject(Obj);
        if (lPos < 0) return false;
        else if (lPos == (m_ObjList.size() - 1)) return true;
        CWBObject LastObj = m_ObjList.get(m_ObjList.size() - 1);
        if (LastObj == null) return false;
        Obj.Z_Order = LastObj.Z_Order + 1;
        m_ObjList.remove(lPos);
        m_ObjList.add(Obj);
        return true;
    }

    //将对象置于底层
    public boolean GoBottom(CWBObject Obj) {
        long lPos = FindByObject(Obj);
        if (lPos < 0) return false;
        else if (lPos == 0) return true;
        CWBObject FirstObj = m_ObjList.get(0);
        if (FirstObj == null) return false;
        Obj.Z_Order = FirstObj.Z_Order - 1;
        m_ObjList.remove(lPos);
        m_ObjList.add(0, Obj);
        return true;
    }

    //向层中添加一个对象
    public boolean Append(CWBObject Obj) {
        if (Obj == null) return false;
        long lPos = GetLastByOrder(Obj.Z_Order);
        m_ObjList.add((int) lPos, Obj);
        return true;
    }

    //画出对象列表
    public void Draw(Canvas pDC, CWBObject ObjIgnore, boolean bPreview) {
        if (Visible == false) return;
        try {
            for (int i = 0; i < m_ObjList.size(); i++) {
                CWBObject Obj = m_ObjList.get(i);
                if (Obj == null) continue;
                if ((ObjIgnore != null) && (ObjIgnore.ObjectID == Obj.ObjectID)) continue;
                ;
                if (Obj.MainType == wbmtImage) ((CWBImage) Obj).Draw(pDC, false);
                else if (Obj.MainType == wbmtShape) ((CWBShape) Obj).Draw(pDC, false);
                else if (Obj.MainType==wbmtText) ((CWBText)Obj).Draw(pDC,false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Draw(Canvas pDC) {
        CWBObject Obj = null;
        boolean bPreview = false;
        Draw(pDC, Obj, bPreview);
    }

    public void Draw(Canvas pDC, CWBObject Obj) {
        boolean bPreview = false;
        Draw(pDC, Obj, bPreview);
    }

    public void DrawMask(Canvas pDC, long dwObj) {
        CWBObject Obj = null;
        for (int i = 0; i < m_ObjList.size(); i++) {
            Obj = m_ObjList.get(i);
            if (Obj == null || Obj.ObjectID == dwObj) continue;
            Obj.Draw(pDC, true,true);
            //Application->ProcessMessages();
        }
    }

    //根据坐标检索对象
    public CWBObject GetItemAt(Point pt) {
        CWBObject Obj = null;
        CWBObject Ret = null;
        //按矩形区域索引法
        for (int i = 0; i < m_ObjList.size(); i++) {
            Obj = m_ObjList.get(i);
            if (Obj.InRegion(pt.x, pt.y)) {
                if ((Ret == null) || (Obj.Z_Order >= Obj.Z_Order)) {
                    Ret = Obj;
                }
            }
        }
        return Ret;
    }

    //根据Z顺序搜索第一个节点
    public long GetFirstByOrder(int order) {
        CWBObject Obj = null;

        long lRet = 0;
        for (int i = 0; i < m_ObjList.size(); i++) {
            Obj = m_ObjList.get(i);
            if (Obj.Z_Order >= order)
                return i;
        }
        return lRet;
    }

    //根据Z顺序搜索最后一个节点
    public long GetLastByOrder(int order) {
        CWBObject Obj = null;

        long lRet = 0;
        for (int i = m_ObjList.size(); i > 0; i--) {
            Obj = m_ObjList.get(i - 1);
            if (Obj.Z_Order <= order)
                return i;
        }
        return lRet;
    }

    //根据对象编号检索索引值
    public long FindByObjectID(long dwID) {
        for (int i = 0; i < m_ObjList.size(); i++) {
            CWBObject obj = m_ObjList.get(i);
            if (obj == null) continue;
            if (obj.ObjectID == dwID) return i;
        }
        return -1;
    }

    //根据对象编号检索索引值
    public long FindByObject(CWBObject Obj) {
        return FindByObjectID(Obj.ObjectID);
    }

    public long GetCount() {
        return m_ObjList.size();
    }

    //将对象存储到流中
    public long SaveStream(OutputStream outputStream) {
        long dwRet = 0;
        long dwCount = 0;

        BoardUtils boardtool = new BoardUtils();
        boardtool.WriteUIntToStream(GetCount(), outputStream);
        boardtool.WriteUByteToStream(PageID, outputStream);
        boardtool.WriteUShortToStream(Width, outputStream);
        boardtool.WriteUShortToStream(Height, outputStream);
        boardtool.WriteUShortToStream(Direct_Lock, outputStream);
        boardtool.WriteUShortToStream(Direction, outputStream);
        boardtool.WriteColorToStream(PageBackColor, outputStream);


        long dwID;
        for (int i = 0; i < m_ObjList.size(); i++) {
            CWBObject Obj = m_ObjList.get(i);
            if (Obj != null) {
                dwID = Obj.ObjectID;
                boardtool.WriteUIntToStream(dwID, outputStream);
            }
        }
        // 写入对象列表的顺序
        boardtool.WriteUShortToStream(TextListCount, outputStream);

        for (int i = 0; i < TextListCount; i++) {
            CWBObject Obj = TextListItems.get(i);
            if (Obj != null) {
                dwID = Obj.ObjectID;
                boardtool.WriteUIntToStream(dwID, outputStream);
            }
        }
        boardtool.WriteUIntToStream(DocType, outputStream);
        return dwRet;

    }

    //从流中加载对象
    //将ObjList中的当前页面的对象添加到页面的对象列表中
    public long LoadStream(DocumentInputStream inputStream, CWBObjectList ObjList) {
        long dwRet = 0;
        long dwCount = 0;
        int iLen;
        long mCount;
        m_ObjList.clear();
        TextListItems.clear();
        //页面的对象数量
        mCount = inputStream.readUInt();
        //页面的基本属性
        PageID = (short) (inputStream.readUByte());
        Width = inputStream.readUShort();
        Height = inputStream.readUShort();
        Direct_Lock = inputStream.readUShort();
        Direction = inputStream.readUShort();
      //  long iret=inputStream.readUInt();
        PageBackColor =(int) inputStream.readUInt();


        //页面对象 列表
        long lPos;
        for (int i = 0; i < mCount; i++) {
            long currObjId = inputStream.readUInt();
            lPos = ObjList.FindByObjectID(currObjId);
            if (lPos >= 0) m_ObjList.add(ObjList.GetItem((int) lPos));
        }

        //对象名称
        TextListCount = inputStream.readUShort();

        for (int i = 0; i < TextListCount; i++) {
            long dwID = inputStream.readUInt();
            lPos = ObjList.FindByObjectID(dwID);
            CWBObject Obj = ObjList.GetItem((int) lPos);
            TextListItems.add(Obj);
        }
        DocType = (int) (inputStream.readUInt());
        return 0;
    }


    public void Show() {
        if (Visible == false) Visible = true;
    }


    public void Hide() {
        if (Visible == true) Visible = false;
    }

    public boolean IsVisible() {
        return Visible;
    }

    public int GetPageID() {
        return PageID;
    }

    public void SetPageID(short id) {
        PageID = id;
    }

    ;

    public void SetCreator(int iUser) {
        Creator = iUser;
    }

    ;

    public long GetMaxID(int Creator) {
        long mid = ((long) Creator) << 16;
        long dwMax = mid;
        CWBObject Obj = null;

        for (int i = 0; i < m_ObjList.size(); i++) {
            Obj = m_ObjList.get(i);
            if ((Obj.ObjectID & 0xFFFF0000) == mid) {
                if (Obj.ObjectID > dwMax) dwMax = Obj.ObjectID;
            }
        }
        return dwMax;
    }

    public void ReleaseResource() {
        CWBObject Obj = null;
        CWBImage Img = null;

        for (int i = 0; i < m_ObjList.size(); i++) {
            Obj = m_ObjList.get(i);
            if (Obj == null) continue;
            try {
                if (Obj.MainType == wbmtImage) {
                    Img = (CWBImage) Obj;
                    Img.ReleaseImage();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void LoadResource(String sPath, int iTab) {
        CWBObject Obj = null;
        CWBImage Img = null;
        BoardUtils boardtool = new BoardUtils();

        for (int i = 0; i < m_ObjList.size(); i++) {
            Obj = m_ObjList.get(i);
            if (Obj == null) continue;
            try {
                if (Obj.MainType == wbmtImage) {
                    Img = (CWBImage) Obj;
                    String imgFile = sPath + "/" + Long.toString(Img.ObjectID) + "_" + Integer.toString(iTab) + boardtool.getExtensionName(((CWBImage) Obj).GetFileName());

                    if (boardtool.FileExists(imgFile)) Img.LoadImgFromFile((imgFile));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void LoadResource(String sPath) {
        int iTab = 0;
        LoadResource(sPath, iTab);
    }

    public void SetTextList(int iCount, ArrayList<CWBObject> txtList) {
        TextListItems.clear();
        TextListItems = txtList;
    }

    public boolean IncludeVideoOrFlash() {
        CWBObject Obj = null;
        for (int i = 0; i < m_ObjList.size(); i++) {
            Obj = m_ObjList.get(i);
            if (Obj.IsFlash() || Obj.IsVideo()) return true;
        }
        return false;
    }

    public CWBObject GetItem(int iPos) {
        return m_ObjList.get(iPos);
    }

    public boolean SetItem(int iPos, CWBObject Obj) {
        if (iPos < 0 || iPos >= m_ObjList.size()) return false;
        m_ObjList.remove(iPos);
        m_ObjList.add(iPos, Obj);
        return true;
    }

    public void DeleteItem(int ObjId)
    {
        int  ipos=(int)FindByObjectID(ObjId);
        if (ipos>=0) {
            m_ObjList.remove(ipos);
        }
    }
    public boolean DeleteItem(CWBObject Obj)
    {
     return        m_ObjList.remove(Obj);

    }
    public ArrayList<CWBObject> getObjList()
    {
        return m_ObjList;
    }


}
