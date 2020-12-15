package com.example.flutter_app.meetingboard;

import android.graphics.Canvas;
import android.graphics.Point;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.OutputStream;
import java.util.ArrayList;

import static com.example.flutter_app.meetingboard.BoardConstants.WBShapeType.wbstLight;


public class CWBObjectList {
    private ArrayList<CWBObject> m_list;

    public CWBObjectList() {
        m_list = new ArrayList<CWBObject>();
        StartMaxId = 0;
    }
    public void Clear()
    {
        m_list.clear();
    }
    public int GetCount()
    {
        return m_list.size();
    }
    public boolean Append(CWBObject pObj) {
        m_list.add(pObj);
        return true;
    }

    public CWBObject FindObject(long dwID)
    {
        long ipos=FindByObjectID(dwID);
        if (ipos==-1) return null;
        return  GetItem((int)ipos);
    }
    //根据对象编号查找子项所在的列表位置
    public long FindByObjectID(long dwID) {
        for (int i = 0; i < m_list.size(); i++) {
            CWBObject obj =   m_list.get(i);
            if (obj == null) continue;
            if (obj.ObjectID == dwID) return i;
        }
        return -1;
    }

    //获得从头节点起的第iPos个节点，头节点为第0个
    public CWBObject GetItem(int iPos) {
        if ((iPos < 0) || (iPos >= m_list.size())) return null;
        return m_list.get(iPos);
    }

    public boolean SetItem(int iPos,CWBObject Obj){
        if ((iPos < 0) || (iPos >= m_list.size())) return false;
            m_list.remove(iPos);
            m_list.add(iPos,Obj);
            return true;
    }

    //删除子项并释放子项的内存空间
    public boolean DeleteItem(long dwID) {
        long iPos = FindByObjectID(dwID);
        if (iPos < 0) return false;
        m_list.remove((int)iPos);
        //  if(CanRestore == false) DeleteWBObject(pObj);
        return true;
    }
    public boolean DeleteItem(CWBObject Obj)
    {
        return  m_list.remove(Obj);

    }


    //画出对象列表

    public void Draw(Canvas pDC, CWBObject Obj, boolean bPreview) {
      CWBObject currObj;
        long i;
        for (i = 0; i < m_list.size(); i++) {
            currObj = m_list.get((int )i);
            if (currObj == null) continue;
            if (currObj.ObjectID == Obj.ObjectID) continue;
            Obj.Draw(pDC,false,bPreview);

        }
    }

    public void Draw(Canvas pDc) {
        CWBObject Obj = null;
        boolean bPreview = false;
        Draw(pDc, Obj, bPreview);
    }
    //根据坐标位置搜索节点对象

    public CWBObject GetItemAt(Point pt) {
        CWBObject Obj = null;
        CWBObject Ret = null;
          for (int i = 0; i < m_list.size(); i++) {
            Obj =  m_list.get(i);
            if (Obj.InRegion(pt.x, pt.y)) {
                if ((Ret == null) || (Obj.Z_Order >= Ret.Z_Order)) {
                    Ret = Obj;
                }
            }
        }
        return Ret;
    }


    //检索对象列表中最大的ID号
    public long GetMaxID(int Creator) {
        long mid = ((long) Creator) << 16;
        long dwMax = mid;
        CWBObject Obj = null;

        for (int i = 0; i < m_list.size(); i++) {
            Obj = m_list.get(i);
            if (Obj == null) return 0;
            if ((Obj.ObjectID & 0xFFFF0000) == mid) {
                if (Obj.ObjectID > dwMax) dwMax = Obj.ObjectID;
            }
        }
        if (dwMax < StartMaxId) dwMax = StartMaxId;
        return dwMax;
    }


    public long SaveToStream(OutputStream outputStream) {
        long dwCount = 0;
        long dwRet = 0;
        BoardUtils boardtool = new BoardUtils();
        boardtool.WriteUIntToStream(m_list.size(), outputStream);
        CWBObject Obj = null;
        for (int i = 0; i < m_list.size(); i++) {
            Obj =   m_list.get(i);
            if (Obj == null) continue;
            switch (Obj.MainType) {
                case wbmtShape:
                    CWBShape ObjShape=(CWBShape)Obj;
                    dwCount = ObjShape.SaveToStream(outputStream);
                    break;
                case wbmtImage:
                    CWBImage ObjImage=(CWBImage)Obj;
                    dwCount =ObjImage.SaveToStream(outputStream);
                    break;

                case wbmtText:
                    CWBText ObjText=(CWBText)Obj;
                    dwCount = ObjText.SaveToStream(outputStream);
                    break;
                    /*
                case wbmtLink:
                    dwCount = ((CWBLink) Obj).SaveToStream(outputStream);
                    break;
                case wbmtMedia:
                    dwCount = ((CWBMediaObject) Obj).SaveToStream(outputStream);
                    break;
                case wbmtOle:
                    dwCount = pObj.SaveToStream(outputStream);
                    break;
                case wbmtFlash:
                    dwCount = pObj.SaveToStream(outputStream);
                    break;
                    */
                default:
                    dwCount = 0;
                    break;
            }
            dwRet += dwCount;
        }
        return dwRet;
    }
    //从流中加载对象

    public long LoadFromStream(final DocumentInputStream inputStream, boolean bAction) {
        long dwCount = 0;
        long dwRet = 0;
        long mCount = 0;
        mCount = inputStream.readUInt();
        CWBObject Obj = null;
        CWBImage ObjImage = null;
        CWBShape ObjShape = null;
        CWBText ObjText=null;

        for (int i = 0; i < mCount; i++) {
            Obj = new CWBObject();
            dwCount = Obj.LoadFromStream(inputStream);

            switch (Obj.MainType) {
                case wbmtShape:
                    ObjShape = new CWBShape();
                    ((CWBObject)ObjShape).Assign(Obj);
                    dwCount = ObjShape.LoadFromStream(inputStream);
                    if (ObjShape.SubType!= wbstLight || bAction) {
                        m_list.add(ObjShape);
                    }
                    break;
                case wbmtImage:
                    ObjImage = new CWBImage();
                    ((CWBObject) ObjImage).Assign(Obj);
                    dwCount = ObjImage.LoadFromStream(inputStream);
                    m_list.add(ObjImage);
                    break;
                case wbmtText:
                    ObjText = new CWBText();
                    ((CWBObject) ObjText).Assign(Obj);
                    dwCount = ObjText.LoadFromStream(inputStream);
                    m_list.add(ObjText);


                    break;


                default:
                    dwCount = 0;
                 Obj=null;
                    return 0;
            }
            dwRet += dwCount;

        }
        //delete pObj;
        return dwRet;
    }


    public long StartMaxId;
}
