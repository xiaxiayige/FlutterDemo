package com.example.flutter_app.meetingboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentInputStream;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import static com.example.flutter_app.meetingboard.BoardConstants.Thumb_Height;
import static com.example.flutter_app.meetingboard.BoardConstants.Thumb_Width;
import static com.example.flutter_app.meetingboard.BoardConstants.WBControlType.wbctMove;
import static com.example.flutter_app.meetingboard.BoardConstants.WBMainType.wbmtImage;
import static com.example.flutter_app.meetingboard.BoardConstants.WBMainType.wbmtShape;
import static com.example.flutter_app.meetingboard.BoardConstants.WBMainType.wbmtText;
import static com.example.flutter_app.meetingboard.BoardConstants.WBShapeType.wbstDrag;
import static com.example.flutter_app.meetingboard.BoardConstants.*;
import static com.example.flutter_app.meetingboard.BoardConstants.WBShapeType.wbstErase;
import static com.example.flutter_app.meetingboard.BoardConstants.WBShapeType.wbstHandline;
import static com.example.flutter_app.meetingboard.BoardConstants.WBShapeType.wbstLight;
import static com.example.flutter_app.meetingboard.BoardConstants.WBShapeType.wbstSelect;
import static com.example.flutter_app.meetingboard.BoardMessage.*;


//import com.ugee.pentabletinterfacelibrary.*;
//import com.ble.support.UgBleFactory;


public class BoardView extends View {

    private static String TAG = "BoardMainActivity";

    private CWBShape TmpLine;
    private CWBFileHead FileHead;
    private CWBObjectList wbList;
    private ArrayList<CWBPage> pageList;
    private ArrayList<CWBMark> markList;
    private CWBImage.WBLineOption DefLineOption;
    private String rootTmpPath = "";
    private String tmpPath = "";
    private Bitmap memBmp;
    private Bitmap selectBmp;
    private int FUser;
    private Canvas DrawCanvas;
    private int mColor = Color.RED;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean m_bCanDraw;    //是否允许组件输出到显示设备
    CWBObject ObjSelect;  //用户选择的对象
    private boolean m_bSaved = true;  //白板文件是否已保存过，如果为false，文件被改动后未保存
    private boolean bpenDrawing;
    private float fZoom = 1;
    private ArrayList<Bitmap> thumbList = new ArrayList<Bitmap>();

    //点阵笔校正参数
    public CWBPenParam penParam = new CWBPenParam();
    private CWBAdjustParam adjustParam;
    //View当前是否在校正状态
    public boolean ViewAdjusting = false;
    public NotifyMsg notifyMsg;
    /**
     * 初始化状态常量
     */
    public static final int STATUS_INIT = 1;

    /**
     * 图片放大状态常量
     */
    public static final int STATUS_ZOOM_OUT = 2;

    /**
     * 图片缩小状态常量
     */
    public static final int STATUS_ZOOM_IN = 3;

    /**
     * 图片拖动状态常量
     */
    public static final int STATUS_MOVE = 4;

    /**
     * 用于对图片进行移动和缩放变换的矩阵
     */
    private Matrix matrix = new Matrix();

    /**
     * 记录当前操作的状态，可选值为STATUS_INIT、STATUS_ZOOM_OUT、STATUS_ZOOM_IN和STATUS_MOVE
     */
    private int currentStatus;

    /**
     * ZoomImageView控件的宽度
     */
    private int width;

    /**
     * ZoomImageView控件的高度
     */
    private int height;

    /**
     * 记录两指同时放在屏幕上时，中心点的横坐标值
     */
    private float centerPointX;

    /**
     * 记录两指同时放在屏幕上时，中心点的纵坐标值
     */
    private float centerPointY;

    /**
     * 记录当前图片的宽度，图片被缩放时，这个值会一起变动
     */
    private float currentBitmapWidth;

    /**
     * 记录当前图片的高度，图片被缩放时，这个值会一起变动
     */
    private float currentBitmapHeight;

    /**
     * 记录上次手指移动时的横坐标
     */
    private float lastXMove = -1;

    /**
     * 记录上次手指移动时的纵坐标
     */
    private float lastYMove = -1;

    /**
     * 记录手指在横坐标方向上的移动距离
     */
    private float movedDistanceX;

    /**
     * 记录手指在纵坐标方向上的移动距离
     */
    private float movedDistanceY;

    /**
     * 记录图片在矩阵上的横向偏移值
     */
    private float totalTranslateX;

    /**
     * 记录图片在矩阵上的纵向偏移值
     */
    private float totalTranslateY;

    /**
     * 记录图片在矩阵上的总缩放比例
     */
    private float totalRatio = 1.0f;

    /**
     * 记录手指移动的距离所造成的缩放比例
     */
    private float scaledRatio = 1.0f;

    /**
     * 记录图片初始化时的缩放比例
     */
    private float initRatio = 1.0f;

    /**
     * 记录上次两指之间的距离
     */
    private double lastFingerDis;

    private Point ptLastErase = new Point(0, 0);

    private float pageThumbScalex = 0.2f;
    private float pageThumbScaley = 0.2f;
    public int DefPageWidth = Width_V_A4;
    public int DefPageHeight = Height_V_A4;
    public int BoardId;
    public CWBPage CurrentPage;


    public int WritingDirection;
    public int PageBackColor;
    public boolean bLoadCacheMsg = true;

    public BoardView(Context context) {
        super(context);
        init();
    }

    //手写板设备发送的书写坐标
    public void HandlePenEvent(int x, int y, int press) {
        //已经根据page的宽高做了转换
        //保留原先的触屏工作模式，用于恢复

        if (!ViewAdjusting) {
            x = (int) (penParam.xSlope * x + penParam.xOffset);
            y = (int) (penParam.ySlope * y + penParam.yOffset);
        }
        BoardConstants.WBShapeType oldtouchtype = TmpLine.SubType;
        if (TmpLine.SubType != wbstHandline) {
            PrepareDraw(wbstHandline);
        }
        if (press > 0) {
            if (bpenDrawing == false) {
                bpenDrawing = true;
                if (TmpLine.SubType == wbstHandline) {
                    ArrayList<Point> pointlist = TmpLine.GetPointList();
                    pointlist.clear();
                    Point pt = new Point((int) x, (int) y);
                    pointlist.add(pt);
                }
            } else {
                if (bpenDrawing == false) return;
                if (TmpLine.SubType == wbstHandline) {
                    Point pt = new Point((int) x, (int) y);
                    ArrayList<Point> pointlist = TmpLine.GetPointList();
                    pointlist.add(pt);
                    TmpLine.UpdateRect();
                    //  postInvalidate(TmpLine.ClientRect.left, TmpLine.ClientRect.top, TmpLine.ClientRect.right, TmpLine.ClientRect.bottom);
                    postInvalidate();
                }
            }
        } else {
            if (bpenDrawing == false) return;
            if (TmpLine.SubType == wbstHandline) {
                if (ViewAdjusting) {
                    HandleAdjustPoint((int) x, (int) y);
                } else {
                    CWBShape ObjShape = AppendShape(true);
                    ObjShape.LineOption.Alpha = 255;
                    UpdateBack(ObjShape.ClientRect);
                }
                bpenDrawing = false;
                TmpLine.Vertexs.clear();
            }
        }
        TmpLine.SubType = oldtouchtype;
    }

    private boolean isPointInPage(Point pt) {
        if ((pt.x < 0) || (pt.x > CurrentPage.Width)) return false;
        if ((pt.y < 0) || (pt.y > CurrentPage.Height)) return false;
        return true;
    }

    public void HandleTouchWrite(MotionEvent event) {
        float x = (event.getX() - totalTranslateX) / totalRatio;
        float y = (event.getY() - totalTranslateY) / totalRatio;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (TmpLine.SubType == wbstHandline) {
                    Point pt = new Point((int) x, (int) y);
                    if (!isPointInPage(pt)) return;
                    ArrayList<Point> pointlist = TmpLine.GetPointList();
                    pointlist.clear();
                    pointlist.add(pt);
                    bpenDrawing = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (TmpLine.SubType == wbstHandline) {
                    ArrayList<Point> pointlist = TmpLine.GetPointList();
                    if (bpenDrawing == false) {
                        Point pt = new Point((int) x, (int) y);
                        pointlist.clear();
                        pointlist.add(pt);
                        bpenDrawing = true;
                    }

                    int historySize = event.getHistorySize();
                    if (historySize == 0) {
                        Point pt = new Point((int) x, (int) y);
                        if (isPointInPage(pt)) pointlist.add(pt);
                        else {
                            bpenDrawing = false;
                            CWBShape ObjShape = AppendShape(true);
                            UpdateBack(ObjShape.ClientRect);
                            TmpLine.Vertexs.clear();
                            return;
                        }
                    } else {
                        for (int i = 0; i < historySize; i++) {
                            float historicalX = (event.getHistoricalX(i) - totalTranslateX) / totalRatio;
                            float historicalY = (event.getHistoricalY(i) - totalTranslateY) / totalRatio;
                            Point pt = new Point((int) historicalX, (int) historicalY);
                            if (isPointInPage(pt)) pointlist.add(pt);
                            else {
                                bpenDrawing = false;
                                CWBShape ObjShape = AppendShape(true);
                                UpdateBack(ObjShape.ClientRect);
                                TmpLine.Vertexs.clear();
                                return;
                            }
                        }
                    }
                    TmpLine.UpdateRect();
                    postInvalidate();//(TmpLine.ClientRect.left, TmpLine.ClientRect.top, TmpLine.ClientRect.right, TmpLine.ClientRect.bottom);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (TmpLine.SubType == wbstHandline) {
                    CWBShape ObjShape = AppendShape(true);
                    ObjShape.LineOption.Alpha = 255;
                    UpdateBack(ObjShape.ClientRect);
                    TmpLine.Vertexs.clear();
                }
                break;
            default:
                break;
        }
    }

    public CWBShape AppendShape(boolean bMode) {
        int iCount = TmpLine.GetCount();
        CWBShape ObjShape = new CWBShape(iCount);
        ObjShape.Assign(TmpLine);
        ObjShape.ReviseClientRect();
        AppendObject(ObjShape, bMode);
        m_bSaved = false;
        return ObjShape;

    }

    public BoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       /* TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mColor = a.getColor(R.styleable.CircleView_circle_color, Color.RED);
        a.recycle();
        */
        init();
    }

    private void init() {
        mPaint.setColor(mColor);
        TmpLine = new CWBShape();
        TmpLine.LineOption.Width = 2;
        FileHead = new CWBFileHead();
        wbList = new CWBObjectList();
        pageList = new ArrayList<CWBPage>();
        markList = new ArrayList<CWBMark>();
        memBmp = Bitmap.createBitmap(Width_V_A4, Height_V_A4, Bitmap.Config.ARGB_8888);
        selectBmp = Bitmap.createBitmap(Width_V_A4, Height_V_A4, Bitmap.Config.ARGB_8888);
        m_bCanDraw = true;
        PageBackColor = Color.WHITE;//Color.argb(255, 255, 255, 255);
    }

    public void ResetBoard() {
        mPaint.setColor(mColor);
        if (TmpLine != null) TmpLine.Vertexs.clear();
        TmpLine = new CWBShape();
        TmpLine.LineOption.Width = 2;
        TmpLine.SubType = wbstDrag;
        FileHead = new CWBFileHead();
        if (wbList != null) wbList.Clear();
        if (markList != null) markList.clear();
        if (pageList != null) pageList.clear();
        CurrentPage = null;
        m_bCanDraw = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST
                && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, 200);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 200);
        }
    }

    public void setTmpPath(String svalue) {
        tmpPath = svalue;

    }

    public String getTmpPath() {
        return tmpPath;
    }


    public boolean LoadFile(String sFile) throws IOException {
        ResetTmpPath();
        BoardUtils boardtool = new BoardUtils();
        if ((sFile == "") || (!boardtool.FileExists((sFile)))) return false;
        if (!boardtool.getExtensionName(sFile).equals(".eic")) return false;

        FileInputStream fis = new FileInputStream(sFile);
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        DirectoryEntry root = fs.getRoot();

        DocumentEntry objectEntry = (DocumentEntry) (root.getEntry("#"));
        DocumentInputStream inputStream = new DocumentInputStream(objectEntry);
        LoadWBObject(inputStream);

        Iterator<Entry> iterator = root.getEntries();

        String sName;
        while (iterator.hasNext()) {
            DocumentEntry myDoc = (DocumentEntry) (iterator.next());
            sName = myDoc.getName();
            inputStream = new DocumentInputStream(myDoc);

            if (sName.equals("%")) {
                //  LoadUserInfo(InputStream);
            } else if ((sName.getBytes()[0] >= '0') && (sName.getBytes()[0] <= '9')) {
                String szSaveFile = "";
                long dwID = Integer.parseInt(sName);
                long iPos = wbList.FindByObjectID(dwID);
                CWBObject Obj = wbList.GetItem((int) iPos);
                if (Obj != null) {
                    switch (Obj.MainType) {
                        case wbmtImage:
                            CWBImage objImg = (CWBImage) Obj;
                            szSaveFile = tmpPath + "/" + GetNewResName(objImg.GetFileName());
                            break;

                        default:
                            break;
                    }
                    boardtool.SaveStreamToFile(inputStream, szSaveFile);
                }
            }
        }
        CreateThumbList();
        inputStream.close();
        fis.close();

        m_bSaved = true;
        return true;
    }

    public boolean SaveFile(String sFile) {
        try {
            File saveFile = new File(sFile);
            if (saveFile.exists()) saveFile.delete();
            saveFile.createNewFile();
            POIFSFileSystem fs = new POIFSFileSystem();
            DirectoryEntry root = fs.getRoot();
            SaveWBObject(root);
            SaveResfiles(root);
            FileOutputStream outputStream = new FileOutputStream(saveFile);
            fs.writeFilesystem(outputStream);
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void NewFile() {
        ResetTmpPath();
        wbList.Clear();
        pageList.clear();
        CWBPage page = AddPage(DefPageWidth, DefPageHeight, 1, false);
        if (page != null) page.PageBackColor = Color.argb(255, 255, 255, 255);
        CurrentPage = page;
        CurrentPage.Direction = 1;
        FileHead.Reset();
        DefLineOption = FileHead.FileOption.LineOption;
        TmpLine.FillOption = FileHead.FileOption.FillOption;
        TmpLine.LineOption.Color = Color.BLACK;
        TmpLine.SubType = wbstDrag;
        m_bSaved = true;
        CreateThumbList();
        RefreshCurrentPage();


    }

    public boolean SaveResfiles(DirectoryEntry root) {
        try {


            int iCount = wbList.GetCount();
            BoardUtils boardtool = new BoardUtils();
            for (int i = 0; i < iCount; i++) {
                CWBObject Obj = wbList.GetItem(i);
                if (Obj.MainType == wbmtImage) {
                    CWBImage ObjImage = (CWBImage) Obj;
                    String soldfullname = ObjImage.GetFileName();
                    String soldname = boardtool.getFileNameNoEx(soldfullname);
                    String sext = boardtool.getExtensionName(soldfullname);
                    String snewfullname = tmpPath + "/" + soldname + "_" + String.valueOf(BoardId) + sext;
                    //将资源流写入到临时文件中
                    String swbBlockFile = tmpPath + "/wb.dat";
                    File wbBlockFile = new File(swbBlockFile);
                    if (wbBlockFile.exists()) wbBlockFile.delete();
                    wbBlockFile.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(wbBlockFile);
                    boardtool.SaveFileToStream(snewfullname, outputStream);
                    //将资源量写入到复合文件中
                    outputStream.close();
                    FileInputStream fi = new FileInputStream(wbBlockFile);
                    root.createDocument(String.valueOf(ObjImage.ObjectID), fi);
                    fi.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean SaveWBObject(DirectoryEntry root) {
        try {
            String swbBlockFile = tmpPath + "/wb.dat";
            File wbBlockFile = new File(swbBlockFile);
            if (wbBlockFile.exists()) wbBlockFile.delete();
            wbBlockFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(wbBlockFile);

            //存储头信息
            FileHead.Version = WBF_VERSION;
            FileHead.Tag = 0x100;
            FileHead.FileOption.ValidWidth = 0;             //rect.Width();
            FileHead.FileOption.ValidHeight = 0;            //rect.Height();
            FileHead.FileOption.WindowWidth = DefPageWidth;
            FileHead.FileOption.WindowHeight = DefPageHeight;
            FileHead.FileOption.LineOption = DefLineOption;
            // FileHead.FileOption.FillOption = tmpline->FillOption;
            FileHead.SaveToStream(outputStream);

            //存储标签
            SaveMarkList(outputStream);
            //存储对象
            wbList.SaveToStream(outputStream);

            //存储页数据
            int iPageCount = pageList.size();
            int iIndex = 1;//CurrentPage.GetPageID();
            BoardUtils boardtool = new BoardUtils();
            boardtool.WriteUShortToStream(iPageCount, outputStream);
            boardtool.WriteUShortToStream(iIndex, outputStream);
            for (int i = 0; i < iPageCount; i++) {
                CWBPage page = pageList.get(i);
                page.SaveStream(outputStream);
            }

            outputStream.close();
            FileInputStream fi = new FileInputStream(wbBlockFile);
            root.createDocument("#", fi);
            fi.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean LoadWBObject(DocumentInputStream inputStream) {
        FileHead.LoadFromStream(inputStream);
        DefPageWidth = FileHead.FileOption.WindowWidth;
        DefPageHeight = FileHead.FileOption.WindowHeight;
        if (DefPageWidth == 0) DefPageWidth = Width_V_A4;
        if (DefPageHeight == 0) DefPageHeight = BoardConstants.Height_V_A4;
        DefLineOption = FileHead.FileOption.LineOption;
        TmpLine.FillOption = FileHead.FileOption.FillOption;
        LoadMarkList(inputStream);
        wbList.Clear();
        wbList.LoadFromStream(inputStream, true);

        int pageCount = inputStream.readUShort();
        int pageIndex = inputStream.readUShort();
        for (int i = 0; i < pageCount; i++) {
            CWBPage page = new CWBPage(i + 1);
            page.LoadStream(inputStream, wbList);
            if (page.Width == 0) page.Width = DefPageWidth;
            if (page.Height == 0) page.Height = DefPageHeight;
            pageList.add(page);
        }
        return true;
    }

    String GetNewResName(String sOldName) {
        BoardUtils boardtool = new BoardUtils();
        String sExt = boardtool.getExtensionName(sOldName);
        String sNameOffExt = boardtool.getFileNameNoEx(sOldName);
        int iPos = sNameOffExt.indexOf("_");
        String sRet;
        if (iPos >= 0) sNameOffExt = sNameOffExt.substring(0, iPos - 1);
        sRet = sNameOffExt + "_" + String.valueOf(BoardId) + sExt;
        return sRet;
    }

    long SaveMarkList(FileOutputStream outputStream) {
        try {
            long mCount = markList.size();
            BoardUtils boardtool = new BoardUtils();
            boardtool.WriteUIntToStream(mCount, outputStream);
            for (int i = 0; i < markList.size(); i++) {
                CWBMark objMark = markList.get(i);
                int icount = objMark.BytesCount();
                boardtool.WriteUShortToStream(icount, outputStream);
                byte[] buf = new byte[icount];
                objMark.WriteToBuffer(buf);
                outputStream.write(buf);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    long LoadMarkList(DocumentInputStream inputStream) {
        try {
            markList.clear();

            long mCount = inputStream.readUInt();
            for (int i = 0; i < mCount; i++) {
                int isize = inputStream.readUShort();

                byte[] buffer = new byte[isize];
                inputStream.read(buffer);
                CWBMark pm = new CWBMark();
                pm.ReadFromBuffer(buffer);
                markList.add(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    public boolean SelectPage(long pos, boolean bMode) {

        if (pageList == null) return false;
        CWBPage page = pageList.get((int) pos);
        if (page != null && CurrentPage != page) {
            CWBPage oldPage = CurrentPage;

            page.LoadResource(tmpPath, BoardId);
            CurrentPage = page;
            ClosePage(oldPage);
            // notify message
            // if (FOnPageNotify != NULL)
            //     FOnPageNotify(CurrentPage, wbpeSelect);


            if (oldPage != null) oldPage.ReleaseResource();

            //   this->Width = CurrentPage.Width;
            //   this->Height = CurrentPage.Height;
            SetPageSize(CurrentPage.GetPageID(), CurrentPage.Width, CurrentPage.Height, false);
            SaveBack(null);
            Canvas drawCanvas = new Canvas(memBmp);
            CurrentPage.Draw(drawCanvas);
            invalidate();
            if ((notifyMsg != null) && bMode) notifyMsg.onSelectPage((int) pos, bMode);
            ScrollBoard(0, 0,getWidth(),getHeight());
            return true;
        } else
            return false;
    }


    private void ClosePage(CWBPage page) {
        return;
    }

    public int GetCurrPageId() {
        return CurrentPage.GetPageID();
    }

    public int GetCurrPageIndex() {
        return GetPageIndex(CurrentPage.GetPageID());

    }

    public int GetPageCount() {
        return pageList.size();
    }

    CWBPage InsertPage() {
        int currPos = GetPageIndex(CurrentPage);
        return InsertPage(currPos);
    }

    CWBPage InsertPage(int pos) {
        int id = GetMaxPageId();
        CWBPage page = AddPage(DefPageWidth, DefPageHeight, id, true, true, pos);
        Bitmap bitmap = MakePageThumb(GetPageIndex(page), Thumb_Width, Thumb_Height);
        m_bSaved = false;
        return page;
    }

    private int GetMaxPageId() {
        int id = 1;
        for (int i = 0; i < pageList.size(); i++) {
            CWBPage page = pageList.get(i);
            if (page.GetPageID() > id) id = page.GetPageID();
        }
        id++;
        return id;

    }

    //<!-- addpage
    CWBPage AddPage(int ipw, int iph, int id) {
        boolean bMode = true;
        boolean bIns = true;
        int pos = -1;
        boolean bLock = false;
        return AddPage(ipw, iph, id, bMode, bIns, pos, bLock);
    }

    CWBPage AddPage(int ipw, int iph, int id, boolean bMode) {
        boolean bIns = true;
        int pos = -1;
        boolean bLock = false;
        return AddPage(ipw, iph, id, bMode, bIns, pos, bLock);
    }

    CWBPage AddPage(int ipw, int iph, int id, boolean bMode, boolean bIns) {
        int pos = -1;
        boolean bLock = false;
        return AddPage(ipw, iph, id, bMode, bIns, pos, bLock);
    }

    CWBPage AddPage(int ipw, int iph, int id, boolean bMode, boolean bIns, int pos) {
        boolean bLock = false;
        return AddPage(ipw, iph, id, bMode, bIns, pos, bLock);
    }

    CWBPage AddPage(int ipw, int iph, int id, boolean bMode, boolean bIns, int iPos, boolean bLock) {
        if (pageList == null) return null;
        if (id > 255) return null;

        if (ipw == 0) ipw = DefPageWidth;

        if (iph == 0) iph = DefPageHeight;

        CWBPage page = GetPage(id);

        if (page != null) return page;
        else
            page = new CWBPage(id);

        page.Width = ipw;
        page.Height = iph;
        page.Direction = WritingDirection;

       /* if (bLock)
            page.PageBackColor = Color.WHITE;
        else
            page.PageBackColor = PageBackColor;*/
        page.PageBackColor = Color.WHITE;
        page.Direct_Lock = bLock ? 1 : 0;
        if (iPos < 0)
            pageList.add((page));
        else
            pageList.add(iPos, page);


        int iEvent = BoardConstants.WBPageEvent.wbpeAppend.ordinal();
        if (bIns) iEvent = BoardConstants.WBPageEvent.wbpeNew.ordinal();
/*
        if (FOnPageNotify != null)
            FOnPageNotify(pPage, iEvent);
*/
        if (bMode)
            SelectPage((long) GetPageIndex((int) page.GetPageID()), true);
        Bitmap bitmap = MakePageThumb(GetPageIndex(page), Thumb_Width, Thumb_Height);
        if (iPos < 0) thumbList.add(bitmap);
        else thumbList.add(iPos, bitmap);
        if ((notifyMsg != null)) notifyMsg.onAddPage(GetPageIndex(page), bMode);
        m_bSaved = false;
        return page;
    }

    public void DeletePage() {
        int currPos = GetPageIndex(CurrentPage);
        DeletePage(currPos);

    }

    public void DeletePage(long pos) {
        boolean bMod = true;
        DeletePage(pos, bMod);
    }

    public void DeletePage(long pos, boolean bMode) {
        if (pageList == null) return;

        if (pageList.size() == 1) {
            Log.i("info", "不能删除最后一个页面");
            return;
        }

        CWBPage page = pageList.get((int) pos);
        CWBObject Obj = null;
        if (page != null) {
            int iCount = (int) page.GetCount();

            for (int i = 0; i < iCount; i++) {
                Obj = page.GetItem(i);
                if (Obj == null) continue;
                wbList.DeleteItem(Obj.ObjectID);
            }

        /*    if (m_bRecordAction && bMod)
            {
                TSeriallyPacket packet;

                packet.SetCommand(vcDeleteWBPage);
                packet.InsertData(0x03, page->GetPageID());
                AppendActionCommand((LPBYTE) (packet.Data()), packet.GetLen());
            }
*/


         /*   if (FOnPageNotify != NULL)
                FOnPageNotify(page, wbpeDelete);
*/
            long iNext = pos;
            CWBPage currpage = pageList.get((int) pos);
            // currpage=null;
            if (currpage != null) {
                if (notifyMsg != null) notifyMsg.onDelPage((int) pos, bMode);
            }
            pageList.remove(currpage);
            if (pos == pageList.size())
                iNext = pos - 1;
            CurrentPage = null;
            thumbList.remove((int) pos);
            SelectPage(iNext, true);

            m_bSaved = false;
        }
    }


    public int GetPageIndex(int iPageid) {
        if (pageList == null) return -1;
        CWBPage page;
        for (int i = 0; i < pageList.size(); i++) {
            page = pageList.get(i);
            if ((page != null) && (page.GetPageID() == iPageid))
                return i;
        }
        return -1;
    }

    public int GetPageIndex(CWBPage page) {
        if (pageList == null) return -1;
        return pageList.indexOf(page);

    }

    public CWBPage GetPage(int id) {
        CWBPage page = null;
        int iValue;
        int iCount = pageList.size();
        for (int k = 0; k < iCount; k++) {
            page = pageList.get(k);
            if (page == null)
                continue;
            iValue = page.GetPageID();
            if (iValue == id) {
                return page;
            }
        }
        return null;
    }

    public int GetPageID(int pos) {
        if (pos < 0 || pos >= pageList.size())
            return 0;
        return (pageList.get(pos).GetPageID());
    }


    public void InsertObject(CWBObject Obj, boolean bMod) {
        if (Obj == null) return;
        m_bSaved = false;
        boolean bDraw = m_bCanDraw;
        BoardUtils boardtool = new BoardUtils();
        try {
            if (Obj.MainType == wbmtImage) {
                CWBImage objImage = (CWBImage) Obj;
                String sName = tmpPath + "/" + String.valueOf(Obj.ObjectID);
                String sFile = tmpPath + "/" + String.valueOf(Obj.ObjectID) + "_" + String.valueOf(BoardId) + boardtool.getExtensionName(objImage.GetFileName());

                if (boardtool.FileExists(sName)) {
                    boardtool.RenameFile(sName, sFile);
                    objImage.LoadImgFromFile(sFile);
                }
            }


            if (bMod) {
                Obj.SetCreator(FUser);
                wbList.Append(Obj);
                CurrentPage.Append(Obj);
                Obj.Page = CurrentPage.GetPageID();


          /*
            if ((pObj->MainType == wbmtShape) && (pObj->SubType == wbstMask))
                pObj->Lock(true);

            if ((pObj->MainType == wbmtShape) && (pObj->SubType == wbstCover))
            {
                CWBShape *      pshape = (CWBShape *)  pObj;
                pshape->FillOption.FillType = BS_SOLID;
                pshape->FillOption.FillData = this->BackColor;

            }
          */


                if (Obj.MainType == wbmtShape) {
                    ObjSelect = null;
                } else if ((Obj.MainType != wbmtText) && !(Obj.IsAudio())) {
                    ObjSelect = Obj;
                 /*   ResizeControl.Detach();
                    ResizeControl.Attach(pSelect);

                    if (m_bCanDraw)
                        ResizeControl.Draw(Canvas -> Handle);
                        */

                    TmpLine.SubType = wbstSelect;
                }
            } else {
                long iPos = wbList.FindByObjectID(Obj.ObjectID);
                CWBObject po = wbList.GetItem((int) iPos);
                if (po != null) //如果对象不为新对象
                {
                    wbList.SetItem((int) iPos, Obj);
                    CWBPage page = GetPage(po.Page);
                    if (po.Page != Obj.Page) return;
                    if (page != null) {
                        iPos = page.FindByObjectID(po.ObjectID);
                        page.SetItem((int) iPos, Obj);
                    }
                    if (ObjSelect != po) {
                        if (Obj.SubType == wbstHandline && ((CWBShape) Obj).LineOption.Alpha < 255) {
                            SaveBack(ObjSelect);
                        } else if (m_bCanDraw) {
                            if ((!bLoadCacheMsg) && (Obj.Page == CurrentPage.GetPageID())) {
                                Obj.Draw(new Canvas(memBmp), false, false);
                                Obj.Draw(new Canvas(selectBmp), true, false);
                            }
                        }
                    } else {
                        ObjSelect = Obj;
                        //if (m_bCanDraw) ResizeControl.Attach(pSelect);
                    }

                    po = null;
                    if (m_bCanDraw) postInvalidate();
                    if ((!bLoadCacheMsg) && (Obj.Page == CurrentPage.GetPageID())) {
                        int ioff = 2;
                        ioff = 8 + ((CWBShape) Obj).LineOption.Width / 2;
                        Rect rect = boardtool.OutlineRect(Obj.ClientRect, ioff);

                        //UpdateBack(rect);
                        Rect rs = rect;
                        Rect rect1 = boardtool.ReviseRect(rs);

                        //    OutlineRect( & rect1, 2);

                        //恢复原来的背景图
                        //if (ViewShowMode == 0)
                        UpdateBack(rs);
                        //  else if (ViewShowMode == 1)
                        //     BitBlt(Canvas -> Handle, rs.left, rs.top, rs.right - rs.left, rs.bottom - rs.top,
                        //             memBmp -> Canvas -> Handle, rs.left, rs.top, NOTSRCCOPY);
                        // if (m_bCanDraw) ResizeControl.Draw(Canvas -> Handle);
                    }
                    return;
                }
                //如果对象是新的对象
                wbList.Append(Obj);
                if ((Obj.MainType == wbmtShape) && (Obj.SubType == wbstLight)) {
                    if (m_bCanDraw) postInvalidate();
                    return;
                }
                CWBPage page = GetPage(Obj.Page);
                if (page == null) {
                    if (Obj.Page > 0)
                        page = AddPage(DefPageWidth, DefPageHeight, Obj.Page, false);
                }
                if (page != null) {
                    page.Append(Obj);
                    if (page != CurrentPage)
                        bDraw = false;
                }
            }


            Rect rp = Obj.ClientRect;
            if ((rp.right > CurrentPage.Width) || (rp.bottom > CurrentPage.Height)) {
                int iw, ih;

                iw = Math.max(CurrentPage.Width, rp.right);
                iw = Math.min(PAGE_MAXWIDTH, iw);
                ih = Math.max(CurrentPage.Height, rp.bottom);
                ih = Math.min(PAGE_MAXHEIGHT, ih);
                SetPageSize(Obj.Page, iw, ih, bMod);
                //SaveBoardSize(Width,Height);
            }
            if ((bLoadCacheMsg) || (Obj.Page != CurrentPage.GetPageID()))
                return;
            if (ObjSelect != Obj && bDraw) {
                //  FOnLogEvent(String(pObj->ObjectID) +"   draw");
                Obj.Draw(new Canvas(memBmp), false, true);
                Obj.Draw(new Canvas(selectBmp), true, true);
                //绘制到thumb
                updateThumb(CurrentPage.GetPageID(), memBmp);
            }
            int ioff = 2;
            if (Obj.MainType == wbmtShape)
                ioff = ioff + 12 + ((CWBShape) Obj).LineOption.Width / 2;
            Rect rect = boardtool.OutlineRect(Obj.ClientRect, ioff);
            UpdateBack(rect);
            //  if (m_bCanDraw) ResizeControl.Draw(Canvas -> Handle);
            //  DrawLight(Canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //##ModelId=41E8D05103AE
    void UpdateBack(Rect rs) {
        BoardUtils boardtool = new BoardUtils();
        if (m_bCanDraw == false) return;
        //   Rect rect = boardtool.ReviseRect(rs);
        //  rect = boardtool.OutlineRect(rect, 2);
        //恢复原来的背景图
        postInvalidate(rs.left, rs.top, rs.right, rs.bottom);
        //Canvas->CopyRect(rect,memBmp->Canvas,rect);
    }

    public void SaveBack(CWBObject Obj) {
        BoardUtils boardtool = new BoardUtils();
        if (wbList == null || CurrentPage == null || m_bCanDraw == false) return;
        long dwObj = 0;
        if (Obj != null) dwObj = Obj.ObjectID;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(boardtool.GetARGB(CurrentPage.PageBackColor));
        Rect rect = new Rect(0, 0, memBmp.getWidth(), memBmp.getHeight());
        Canvas drawcanvas = new Canvas(memBmp);
        drawcanvas.drawRect(rect, paint);
        if (!bLoadCacheMsg) CurrentPage.Draw(drawcanvas, Obj);

        if (selectBmp != null) {
            Canvas selectcanvas = new Canvas(selectBmp);
            Paint selectpaint = new Paint();
            selectpaint.setColor(Color.WHITE);
            selectpaint.setStyle(Paint.Style.STROKE);
            selectcanvas.drawRect(rect, selectpaint);
            if (!bLoadCacheMsg) CurrentPage.DrawMask(selectcanvas, dwObj);
        }

        postInvalidate();
    }


    public void SetPageSize(int ipage, int iw, int ih, boolean bEvent) {
        CWBPage page = GetPage(ipage);

        if (page == null) return;

        page.Width = iw;
        page.Height = ih;

        if (page == CurrentPage) {
            //    Width         = iw;
            //    Height        = ih;
            //   WMSize();
            if ((page.Width != memBmp.getWidth()) || (page.Height != memBmp.getHeight())) {
                memBmp = Bitmap.createBitmap(page.Width, page.Height, Bitmap.Config.ARGB_8888);
                selectBmp = Bitmap.createBitmap(page.Width, page.Height, Bitmap.Config.ARGB_8888);

            }
        }
        m_bSaved = false;
       /* if (bEvent)
        {
        SendPacket(vcBoardSize, NULL, iw, ih);
        AppendActionCommand((CWBObject *) page, vcBoardSize, iw, ih);
        }*/

    }


    public CWBImage InsertImg(String szFile, int xPos, int yPos, boolean bSel, boolean bMode) {
        try {

            BoardUtils boardtool = new BoardUtils();
            if (!boardtool.FileExists(szFile)) return null;
            bSel = false;
            String sExt = boardtool.getExtensionName(szFile).toLowerCase();
            boolean NeedDel = false;
        /*
        String szPng = boardtool.getFileNameNoEx(szFile) + ".png";
        if (sExt == ".bmp") {
            String szPng = ChangeFileExt(szFile, ".png");

            SaveBmpFileAs(szFile, szPng, L"image/png");
            szFile = szPng;
            NeedDel = true;
        }
*/
            CWBImage ObjImage = new CWBImage();
            ObjImage.LoadImgFromFile(szFile);
            if (ObjImage.GetWidth() == 0 || ObjImage.GetHeight() == 0) return null;
            Rect rect = new Rect();
            rect.left = xPos;
            rect.top = yPos;
            rect.right = rect.left + (int) ObjImage.GetWidth();
            rect.bottom = rect.top + (int) ObjImage.GetHeight();

            //调整image的宽高适应page

            if ((rect.right > CurrentPage.Width) || (rect.bottom > CurrentPage.Height)) {
                float fzoom = 1;
                float fzoom1 = 1;
                float ftemp = (float) ((CurrentPage.Width - rect.left) * 1.0f / ObjImage.GetWidth());
                if (fzoom1 > ftemp) fzoom1 = ftemp;

                float fzoom2 = 1;
                ftemp = (float) ((CurrentPage.Height - rect.top) * 1.0f / ObjImage.GetHeight());
                if (fzoom2 > ftemp) fzoom2 = ftemp;

                if (fzoom1 < fzoom2) fzoom = fzoom1;
                else fzoom = fzoom2;
                rect.right = rect.left + (int) (ObjImage.GetWidth() * fzoom);
                rect.bottom = rect.top + (int) (ObjImage.GetHeight() * fzoom);
            }

            ObjImage.SetRect(rect);
            //ResizeControl.Detach();
            ObjImage.ObjectID = GetMaxObjectID(FUser) + 1;

            if (bSel) {
                ObjSelect = ObjImage;
                SaveBack(null);
                // ResizeControl.Attach(pSelect);
                // UpdateBack(ResizeControl.GetClientRect());
//                if (m_bCanDraw)
                //                  ResizeControl.Draw(this->Canvas -> Handle);
            }

            TmpLine.SubType = wbstSelect;
            sExt = boardtool.getExtensionName(szFile);
            ObjImage.SetExt(sExt);

            String szNewFile = tmpPath + "/" + String.valueOf(ObjImage.ObjectID) + "_" + String.valueOf(BoardId) + sExt;
            boardtool.CopyFile(szFile, szNewFile);

            ObjImage.Caption = "";
            AppendObject(ObjImage, bMode);

            if (ObjImage.Page != CurrentPage.GetPageID())
                ObjImage.ReleaseImage();

            if (bSel == false) {
                //     ResizeControl.Detach();
                ObjSelect = null;
                SaveBack(null);
                updateThumb(CurrentPage.GetPageID(), memBmp);
                if (m_bCanDraw) postInvalidate();
            }

            if (NeedDel) boardtool.DeleteFile(szFile);

            String simg = tmpPath + "/" + ObjImage.GetFileName();

            if (boardtool.FileExists(szNewFile)) ObjImage.LoadImgFromFile(szNewFile);

            //FSelectMode = false;
            return ObjImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long GetMaxObjectID(int iUser) {
        long dwObj = wbList.GetMaxID(iUser);
        return dwObj;
    }


    void AppendObject(CWBObject Obj, boolean bMode) {
        long dwObj = GetMaxObjectID(FUser) + 1;

        if (dwObj != Obj.ObjectID) Obj.ObjectID = dwObj;
        else if (Obj.SubType == wbstHandline) {
            CWBShape objShape = (CWBShape) Obj;
            if (objShape.LineOption.Alpha < 255)
                objShape.Z_Order = objShape.Z_Order - 5;
        }

        m_bSaved = false;
        InsertObject(Obj, bMode);
        if (Obj.MainType == wbmtShape) {
            if ((notifyMsg != null) && (bMode))
                notifyMsg.onAddObject((int) Obj.ObjectID);
        } else if (Obj.MainType == wbmtImage) {
            if ((notifyMsg != null) && (bMode))
                notifyMsg.onAddObject((int) Obj.ObjectID);
        }
    }


    public void PrepareDraw(BoardConstants.WBShapeType iType) {
        try {
         /*   if (iType != TmpLine.SubType) {
                TmpLine.LineOption.Color = Color.BLACK;
                TmpLine.LineOption.Width = 2;
            }
           */
            //    UpdateSelect();
            TmpLine.SubType = iType;
            if (CurrentPage != null) return;
            if (CurrentPage.IsLocked()) {
                Log.d("debug", "层被锁定，无法绘画");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void ClearScreen(boolean bMode) {
        try {
            ArrayList<CWBObject> objList = CurrentPage.getObjList();
            Iterator<CWBObject> iterator = objList.iterator();
            while (iterator.hasNext()) {
                CWBObject Obj = iterator.next();
                if (Obj.MainType != wbmtImage) {
                    wbList.DeleteItem(Obj);
                    iterator.remove();
                }
            }
            SaveBack(null);
            if (m_bCanDraw) postInvalidate();
            int ipageindex = GetPageIndex(CurrentPage.GetPageID());
            if ((notifyMsg != null) && bMode) notifyMsg.onClearScreen(ipageindex, bMode);

            //绘制到thumb
            updateThumb(CurrentPage.GetPageID(), memBmp);
            m_bSaved = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setfZoom(float fz) {
        fZoom = fz;
        invalidate();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {

            width = getWidth();
            height = getHeight();

            AdjustView(BoardConstants.ViewAdjustType.vaWidth);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (TmpLine.SubType == wbstHandline) HandleTouchWrite(event);
        else if (TmpLine.SubType == wbstDrag) HandleTouchDrag(event);
        else if (TmpLine.SubType == wbstErase) HandleTouchErase(event);

        return true;
    }

    private void HandleTouchErase(MotionEvent event) {
        float x = (event.getX() - totalTranslateX) / totalRatio;
        float y = (event.getY() - totalTranslateY) / totalRatio;
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                ptLastErase.x = (int) x;
                ptLastErase.y = (int) y;
                break;
            case MotionEvent.ACTION_MOVE:
                ScanToDeleteObj((int) x, (int) y);
                ptLastErase.x = (int) x;
                ptLastErase.y = (int) y;
                break;
            default:
        }
    }

    private void ScanToDeleteObj(int x, int y) {
        boolean bDeleted = false;
        BoardUtils boardtool = new BoardUtils();
        Iterator<CWBObject> iterator = CurrentPage.getObjList().iterator();
        while (iterator.hasNext()) {
            CWBObject Obj = iterator.next();
            if ((Obj.MainType == wbmtShape)) {
                CWBShape ObjShape = (CWBShape) Obj;
                for (int i = 0; i < ObjShape.Vertexs.size() - 1; i++) {
                    Point pt1 = ObjShape.Vertexs.get(i);
                    Point pt2 = ObjShape.Vertexs.get(i + 1);
                    Point pt3 = ptLastErase;
                    Point pt4 = new Point(x, y);
                    boolean bret = boardtool.intersect3(pt1, pt2, pt3, pt4);
                    if (bret) {
                        iterator.remove();
                        wbList.DeleteItem(ObjShape);
                        SaveBack(ObjShape);
                        postInvalidate(ObjShape.ClientRect.left, ObjShape.ClientRect.top, ObjShape.ClientRect.right, ObjShape.ClientRect.bottom);
                        updateThumb(CurrentPage.GetPageID(), memBmp);
                        m_bSaved = false;
                        if (notifyMsg != null) notifyMsg.onDelObject((int) ObjShape.ObjectID);
                        break;

                    }
                }
            }
        }

    }

    private void HandleTouchDrag(MotionEvent event) {
        if (initRatio == totalRatio) {
            getParent().requestDisallowInterceptTouchEvent(false);
        } else {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    // 当有两个手指按在屏幕上时，计算两指之间的距离
                    lastFingerDis = distanceBetweenFingers(event);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    // 只有单指按在屏幕上移动时，为拖动状态
                    float xMove = event.getX();
                    float yMove = event.getY();
                    if (lastXMove == -1 && lastYMove == -1) {
                        lastXMove = xMove;
                        lastYMove = yMove;
                    }
                    currentStatus = STATUS_MOVE;
                    movedDistanceX = xMove - lastXMove;
                    movedDistanceY = yMove - lastYMove;
                    // 进行边界检查，不允许将图片拖出边界
                    /*
                    if (totalTranslateX + movedDistanceX > 0) {
                        movedDistanceX = 0;
                    } else if (width - (totalTranslateX + movedDistanceX) > currentBitmapWidth) {
                        movedDistanceX = 0;
                    }
                    if (totalTranslateY + movedDistanceY > 0) {
                        movedDistanceY = 0;
                    } else if (height - (totalTranslateY + movedDistanceY) > currentBitmapHeight) {
                        movedDistanceY = 0;
                    }*/
                    // 调用onDraw()方法绘制图片
                    invalidate();
                    lastXMove = xMove;
                    lastYMove = yMove;
                } else if (event.getPointerCount() == 2) {
                    // 有两个手指按在屏幕上移动时，为缩放状态
                    centerPointBetweenFingers(event);
                    double fingerDis = distanceBetweenFingers(event);
                    if (fingerDis > lastFingerDis) {
                        currentStatus = STATUS_ZOOM_OUT;
                    } else {
                        currentStatus = STATUS_ZOOM_IN;
                    }
                    // 进行缩放倍数检查，最大只允许将图片放大4倍，最小可以缩小到初始化比例
                    if ((currentStatus == STATUS_ZOOM_OUT && totalRatio < 4 * initRatio)
                            || (currentStatus == STATUS_ZOOM_IN && totalRatio > initRatio)) {
                        scaledRatio = (float) (fingerDis / lastFingerDis);
                        totalRatio = totalRatio * scaledRatio;
                        if (totalRatio > 4 * initRatio) {
                            totalRatio = 4 * initRatio;
                        } else if (totalRatio < initRatio) {
                            totalRatio = initRatio;
                        }
                        // 调用onDraw()方法绘制图片
                        invalidate();
                        lastFingerDis = fingerDis;
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2) {
                    // 手指离开屏幕时将临时值还原
                    lastXMove = -1;
                    lastYMove = -1;
                    currentStatus = STATUS_INIT;
                }
                break;
            case MotionEvent.ACTION_UP:
                // 手指离开屏幕时将临时值还原
                lastXMove = -1;
                lastYMove = -1;
                currentStatus = STATUS_INIT;
                break;
            default:
                break;
        }

    }

    /**
     * 根据currentStatus的值来决定对图片进行什么样的绘制操作。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TmpLine.SubType == wbstDrag) {
            switch (currentStatus) {
                case STATUS_ZOOM_OUT:
                case STATUS_ZOOM_IN:
                    zoom(canvas);
                    break;
                case STATUS_MOVE:
                    move(canvas);
                    break;
                case STATUS_INIT:
                    //    initBitmap(canvas);
                default:
                    if (memBmp != null) {
                        Paint paint = new Paint();
                        canvas.drawBitmap(memBmp, matrix, null);
                    }
                    break;
            }
        } else {
            if (memBmp != null) {
                canvas.drawBitmap(memBmp, matrix, null);
                canvas.setMatrix(matrix);
                TmpLine.Draw(canvas);
            }
            if (ViewAdjusting) {
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(3);
                int istep = adjustParam.GetAdjustStep();
                canvas.drawCircle(adjustParam.standPoint[istep].x, adjustParam.standPoint[istep].y, 5, paint);
            }
        }

    }

    /**
     * 对图片进行缩放处理。
     *
     * @param canvas
     */
    private void zoom(Canvas canvas) {
        matrix.reset();
        // 将图片按总缩放比例进行缩放
        matrix.postScale(totalRatio, totalRatio);
        float scaledWidth = memBmp.getWidth() * totalRatio;
        float scaledHeight = memBmp.getHeight() * totalRatio;
        float translateX = 0f;
        float translateY = 0f;
        // 如果当前图片宽度小于屏幕宽度，则按屏幕中心的横坐标进行水平缩放。否则按两指的中心点的横坐标进行水平缩放
        if (currentBitmapWidth < width) {
            translateX = (width - scaledWidth) / 2f;
        } else {
            translateX = totalTranslateX * scaledRatio + centerPointX
                    * (1 - scaledRatio);
            // 进行边界检查，保证图片缩放后在水平方向上不会偏移出屏幕
            if (translateX > 0) {
                translateX = 0;
            } else if (width - translateX > scaledWidth) {
                translateX = width - scaledWidth;
            }
        }
        // 如果当前图片高度小于屏幕高度，则按屏幕中心的纵坐标进行垂直缩放。否则按两指的中心点的纵坐标进行垂直缩放
        if (currentBitmapHeight < height) {
            translateY = (height - scaledHeight) / 2f;
        } else {
            translateY = totalTranslateY * scaledRatio + centerPointY
                    * (1 - scaledRatio);
            // 进行边界检查，保证图片缩放后在垂直方向上不会偏移出屏幕
            if (translateY > 0) {
                translateY = 0;
            } else if (height - translateY > scaledHeight) {
                translateY = height - scaledHeight;
            }
        }
        // 缩放后对图片进行偏移，以保证缩放后中心点位置不变
        matrix.postTranslate(translateX, translateY);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        currentBitmapWidth = scaledWidth;
        currentBitmapHeight = scaledHeight;
        canvas.drawBitmap(memBmp, matrix, null);
    }

    public void ScrollBoard(int ileft, int itop,int ishowwidth,int ishowheight) {
        TmpLine.SubType = wbstDrag;
        currentStatus = STATUS_MOVE;
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int bmpWidth = memBmp.getWidth();
        float ratio = viewWidth * 1.0f / bmpWidth;
        matrix.reset();
        float f1=ishowwidth*1.f/(ishowheight*1.f);
        float f2=viewWidth*1.f/(viewHeight*1.f);
        if (f2>f1){
            totalRatio= ratio*f1/f2;
        }
        if (viewWidth > memBmp.getWidth() * totalRatio) totalTranslateX = 0;
        else if (viewWidth < (memBmp.getWidth() + ileft) * totalRatio)
            totalTranslateX = ileft * totalRatio;
        else totalTranslateX = viewWidth - memBmp.getWidth() * totalRatio;
        if (viewHeight > memBmp.getHeight() * totalRatio) totalTranslateY = 0;
        else if (viewHeight < (memBmp.getHeight() + itop) * totalRatio )
            totalTranslateY = itop * totalRatio;
        else totalTranslateY = viewHeight - memBmp.getHeight() * totalRatio;
        invalidate();
    }

    /**
     * 对图片进行平移处理
     *
     * @param canvas
     */
    private void move(Canvas canvas) {
        matrix.reset();
        // 根据手指移动的距离计算出总偏移值
        float translateX = totalTranslateX + movedDistanceX;
        float translateY = totalTranslateY + movedDistanceY;
        // 先按照已有的缩放比例对图片进行缩放
        matrix.postScale(totalRatio, totalRatio);
        // 再根据移动距离进行偏移
        matrix.postTranslate(translateX, translateY);
        totalTranslateX = translateX;
        totalTranslateY = translateY;
        canvas.drawBitmap(memBmp, matrix, null);
    }

    /**
     * 对图片进行初始化操作，包括让图片居中，以及当图片大于屏幕宽高时对图片进行压缩。
     *
     * @param canvas
     */
    private void initBitmap(Canvas canvas) {
        if (memBmp != null) {
            matrix.reset();
            int bitmapWidth = memBmp.getWidth();
            int bitmapHeight = memBmp.getHeight();
            if (bitmapWidth > width || bitmapHeight > height) {
                if (bitmapWidth - width > bitmapHeight - height) {
                    // 当图片宽度大于屏幕宽度时，将图片等比例压缩，使它可以完全显示出来
                    float ratio = width / (bitmapWidth * 1.0f);
                    matrix.postScale(ratio, ratio);
                    float translateY = (height - (bitmapHeight * ratio)) / 2f;
                    // 在纵坐标方向上进行偏移，以保证图片居中显示
                    matrix.postTranslate(0, translateY);
                    totalTranslateY = translateY;
                    totalRatio = initRatio = ratio;
                } else {
                    // 当图片高度大于屏幕高度时，将图片等比例压缩，使它可以完全显示出来
                    float ratio = height / (bitmapHeight * 1.0f);
                    matrix.postScale(ratio, ratio);
                    float translateX = (width - (bitmapWidth * ratio)) / 2f;
                    // 在横坐标方向上进行偏移，以保证图片居中显示
                    matrix.postTranslate(translateX, 0);
                    totalTranslateX = translateX;
                    totalRatio = initRatio = ratio;
                }
                currentBitmapWidth = bitmapWidth * initRatio;
                currentBitmapHeight = bitmapHeight * initRatio;
            } else {
                // 当图片的宽高都小于屏幕宽高时，直接让图片居中显示
                float translateX = (width - memBmp.getWidth()) / 2f;
                float translateY = (height - memBmp.getHeight()) / 2f;
                matrix.postTranslate(translateX, translateY);
                totalTranslateX = translateX;
                totalTranslateY = translateY;
                totalRatio = initRatio = 1f;
                currentBitmapWidth = bitmapWidth;
                currentBitmapHeight = bitmapHeight;
            }
            canvas.drawBitmap(memBmp, matrix, null);
        }
    }

    /**
     * 计算两个手指之间的距离。
     *
     * @param event
     * @return 两个手指之间的距离
     */
    private double distanceBetweenFingers(MotionEvent event) {
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

    /**
     * 计算两个手指之间中心点的坐标。
     *
     * @param event
     */
    private void centerPointBetweenFingers(MotionEvent event) {
        float xPoint0 = event.getX(0);
        float yPoint0 = event.getY(0);
        float xPoint1 = event.getX(1);
        float yPoint1 = event.getY(1);
        centerPointX = (xPoint0 + xPoint1) / 2;
        centerPointY = (yPoint0 + yPoint1) / 2;
    }

    public void AdjustView(BoardConstants.ViewAdjustType type) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int bmpWidth = memBmp.getWidth();
        int bmpHeight = memBmp.getHeight();
        switch (type) {
            case vaWidth:
                totalRatio = viewWidth * 1.0f / bmpWidth;
                matrix.reset();
                totalTranslateX = 0;
                if (viewHeight < bmpHeight) totalTranslateY = 0;
                else totalTranslateY = (viewHeight - bmpHeight * totalRatio) / 2.0f;
                // 先按照已有的缩放比例对图片进行缩放
                matrix.postScale(totalRatio, totalRatio);
                // 再根据移动距离进行偏移
                matrix.postTranslate(totalTranslateX, totalTranslateY);
                invalidate();


                break;
            case vaHeight:
                totalRatio = viewHeight * 1.0f / bmpHeight;
                matrix.reset();
                totalTranslateY = 0;
                if (viewWidth < bmpWidth) totalTranslateX = 0;
                else totalTranslateX = (viewWidth - bmpWidth) / 2.0f;
                // 先按照已有的缩放比例对图片进行缩放
                matrix.postScale(totalRatio, totalRatio);
                // 再根据移动距离进行偏移
                matrix.postTranslate(totalTranslateX, totalTranslateY);
                invalidate();
                break;
            case vaTotal:
                float fzoom1 = viewWidth * 1.0f / bmpWidth;
                float fzoom2 = viewHeight * 1.0f / bmpHeight;
                matrix.reset();
                totalTranslateX = 0;
                totalTranslateY = 0;
                if (fzoom1 > fzoom2) {
                    if (viewHeight < bmpHeight) totalTranslateY = 0;
                    else totalTranslateY = (viewHeight - bmpHeight) / 2.0f;
                    // 先按照已有的缩放比例对图片进行缩放
                    totalRatio = fzoom1;
                    matrix.postScale(totalRatio, totalRatio);
                    // 再根据移动距离进行偏移
                    matrix.postTranslate(totalTranslateX, totalTranslateY);
                } else {
                    if (viewWidth < bmpWidth) totalTranslateX = 0;
                    else totalTranslateX = (viewWidth - bmpWidth) / 2.0f;
                    // 先按照已有的缩放比例对图片进行缩放
                    totalRatio = fzoom2;
                    matrix.postScale(totalRatio, totalRatio);
                    // 再根据移动距离进行偏移
                    matrix.postTranslate(totalTranslateX, totalTranslateY);
                }
                invalidate();
                break;
            default:

        }

    }

    private void updateThumb(int pageId, Bitmap sourceBmp) {
        /*
        int ipageindex = GetPageIndex(pageId);
        Bitmap thumbBitmap = thumbList.get(ipageindex);
        Canvas canvas = new Canvas(thumbBitmap);
        Matrix matrix = new Matrix();
        matrix.setScale(pageThumbScalex, pageThumbScaley);
        //canvas.setMatrix(matrix);;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawBitmap(sourceBmp, matrix, null);
*/

    }

    public void CreateThumbList() {
        if (thumbList == null) thumbList = new ArrayList<Bitmap>();
        else thumbList.clear();
        for (int i = 0; i < pageList.size(); i++) {
            Bitmap bitmap = MakePageThumb(i, Thumb_Width, Thumb_Height);
            thumbList.add(bitmap);
        }
    }

    private Bitmap MakePageThumb(int pageindex, int width, int height) {
        BoardUtils boardtool = new BoardUtils();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
        matrix.setScale(pageThumbScalex, pageThumbScaley);
        canvas.setMatrix(matrix);
        CWBPage page = pageList.get(pageindex);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(boardtool.GetARGB(page.PageBackColor));
        canvas.drawRect(0, 0, page.Width, page.Height, paint);
        if (page != CurrentPage) {
            page.LoadResource(tmpPath, BoardId);
            page.Draw(canvas);
            page.ReleaseResource();
        } else page.Draw(canvas);
        //测试是否缩略图是否生成
       /* String sfile = tmpPath + "/" + String.valueOf(pageindex) + ".jpg";
        File file = new File(sfile);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return bitmap;
    }

    public ArrayList<Bitmap> GetThumbList() {
        return thumbList;
    }

    public void RunActionCommand(SeriallyPacket pack) {
        int iCommand = pack.Command();
        int dwObject = pack.AsInt(0x03);
        byte[] pb;
        int iType;
        int iSubType;
        switch (iCommand) {
            case vcPageBackColor:
                Log.d(TAG, "vcPageBackColor\tdwObject:" + dwObject);
                SetPageBackColor(pack.AsInt(0x04), pack.AsInt(0x05), false);

                break;
            case vcInsertWBPage:
                Log.d(TAG, "vcInsertWBPage\tdwObject:" + dwObject);
                iType = pack.AsInt(0x04);
                CWBPage page = AddPage(pack.AsInt(0x05), pack.AsInt(0x06), dwObject, false, true, iType);
                page.Direction = pack.AsInt(0x07);
                page.PageBackColor = (int) (pack.AsInt64(0x08));
                SelectPage(GetPageIndex(dwObject), false);
                break;
            case vcDeleteWBPage:
                Log.d(TAG, "vcDeleteWBPage\tdwObject:" + dwObject);
                DeletePage(GetPageIndex(dwObject), false);
                break;
            case vcSelectWBPage:
                Log.d(TAG, "vcSelectWBPage\tdwObject:" + dwObject);
                SelectPage(GetPageIndex(dwObject), false);
                break;
            case vcAppendWBObject:
                Log.d(TAG, "vcAppendWBObject\tdwObject:" + dwObject);
                CWBObject Obj = new CWBObject();
                pb = pack.AsBytes(0x05);
                Obj.ReadFromBuffer(pb);
                iType = Obj.MainType.ordinal();
                iSubType = Obj.SubType.ordinal();
                if (iType == wbmtShape.ordinal()) {
                    CWBShape objShape = new CWBShape();
                    objShape.ReadFromBuffer(pb);
                    InsertObject(objShape, false);
                } else if (iType == wbmtImage.ordinal()) {
                    CWBImage imageobj = new CWBImage();
                    imageobj.ReadFromBuffer(pb);
                    imageobj.SetExt(pack.AsString(0x06));
                    //sValue              = GetPath() +pImage->GetFileName();
                    BoardUtils boardtool = new BoardUtils();
                    String sValue = getTmpPath() + imageobj.ObjectID + "_" + BoardId + boardtool.getExtensionName(imageobj.GetFileName());
                    if (boardtool.FileExists(sValue))
                        imageobj.LoadImgFromFile(sValue);
                    InsertObject(imageobj, false);
                    if (imageobj.Page != CurrentPage.GetPageID())
                        imageobj.ReleaseImage();
                } else if (iType == wbmtText.ordinal()) {
                    CWBText objText = new CWBText();
                    objText.ReadFromBuffer(pb);
                    InsertObject(objText, false);

                }
                break;
            case vcDeleteWBObject:
                Log.d(TAG, "vcDeleteWBObject\tdwObject:" + dwObject);
                DeleteObject(dwObject, false);
                break;
            case vcClearWBScreen:
                Log.d(TAG, "vcClearWBScreen\tdwObject:" + dwObject);
                ClearScreen(false);
                break;
            case vcClearOjbRect:
                Log.d(TAG, "vcClearOjbRect");
                ClearObjRect(pack);
                break;
            case vcResizeWBObject:
                int imovetype = pack.AsInt(0x04);
                pb = pack.AsBytes(0x05);
                BoardByteBuf boardByteBuf = new BoardByteBuf();
                boardByteBuf.AssignReadBuf(pb);
                Rect rect = boardByteBuf.ReadRect();
                if (imovetype == wbctMove.ordinal()) {
                    //moveobject();
                } else {
                    ResizeObject(dwObject, rect, imovetype);
                }
                break;
            case vcSetWBData:
                SetTextData(dwObject, pack.AsString(0x05));
                break;
        }
    }

    private void ClearObjRect(SeriallyPacket packet) {
        Rect rd = new Rect(packet.AsInt(0x03), packet.AsInt(0x04), packet.AsInt(0x05), packet.AsInt(0x06));
        ArrayList<CWBObject> objList = CurrentPage.getObjList();
        Iterator<CWBObject> iterator = objList.iterator();
        while (iterator.hasNext()) {
            CWBObject Obj = iterator.next();
            if (Obj.MainType == wbmtShape) {
                if (Obj.ClientRect.left >= rd.left && Obj.ClientRect.top >= rd.top &&
                        Obj.ClientRect.right <= rd.right && Obj.ClientRect.bottom <= rd.bottom) {

                    wbList.DeleteItem(Obj);
                    iterator.remove();
                    m_bSaved = false;
                }
            }
        }
        RefreshCurrentPage();
    }

    private void ResizeObject(int dwObj, Rect rect, int itype) {
        CWBObject Obj = wbList.FindObject(dwObj);
        Obj.Resize(rect);


    }

    private void SetTextData(int dwObj, String szText) {
        CWBObject Obj = wbList.FindObject(dwObj);

        if (Obj == null) return;
        if (Obj.MainType == wbmtText) {
            CWBText textObj = (CWBText) Obj;
            textObj.setText(szText);
            SaveBack(textObj);
            Canvas canvas = new Canvas(memBmp);
            textObj.Draw(canvas);
            UpdateBack(textObj.ClientRect);
        }

    }


    public void DeleteObject(int objectid, boolean bMode) {
        ArrayList<CWBObject> objList = CurrentPage.getObjList();
        Iterator<CWBObject> iterator = objList.iterator();
        while (iterator.hasNext()) {
            CWBObject Obj = iterator.next();
            if (Obj.ObjectID == objectid) {
                wbList.DeleteItem(Obj);
                iterator.remove();
                m_bSaved = false;
                SaveBack(Obj);
                if (m_bCanDraw)
                    postInvalidate();//(Obj.ClientRect.left,Obj.ClientRect.top, Obj.ClientRect.right, Obj.ClientRect.bottom);
                if (notifyMsg != null && bMode) notifyMsg.onDelObject((int) Obj.ObjectID);
                break;
            }
        }
    }

    public void SetPageBackColor(int ipage, int lColor, boolean bEvent) {
        CWBPage page = GetPage(ipage);
        if (page == null) return;
        page.PageBackColor = lColor;
        if (page == CurrentPage) {
            SetBackColor(lColor);
            m_bSaved = false;
        }
/*
        if (bEvent)
        {
            SendPacket(vcPageBackColor, null, ipage, lColor);
        }
        if (OnHotKeyEvent)    FOnHotKeyEvent(HK_PageBackColorChange_ID);*/
    }

    private void SetBackColor(int cv) {
        PageBackColor = cv;
        SaveBack(null);
        if (m_bCanDraw) postInvalidate();
    }

    public void SetUser(int iUser) {
        if (iUser == FUser) return;
        if (wbList != null) {
            CWBObject Obj;
            int iCount = wbList.GetCount();
            for (int i = 0; i < iCount; i++) {
                Obj = wbList.GetItem(i);
                if ((Obj != null) && (Obj.GetCreator() == FUser)) {
                    if (!(Obj.MainType == wbmtShape && Obj.SubType == wbstLight))
                        Obj.SetCreator(iUser);
                    m_bSaved = false;
                }
            }
        }
        FUser = iUser;
    }

    public int GetUser() {
        return FUser;
    }

    public CWBObject getObj(int ObjectId) {
        return wbList.FindObject(ObjectId);
    }

    public void SetLineColor(int icolor) {
        CWBShape shape = TmpLine;
        if (shape.LineOption.Color == icolor) return;
        shape.LineOption.Color = icolor;
    }

    //设置wbobjectlist的objectid的最大值
    public void SetMaxObjectId(int maxid) {
        wbList.StartMaxId = maxid;
    }

    public void StartPenAdjust() {
        ViewAdjusting = true;
        adjustParam = new CWBAdjustParam();
        invalidate();

    }

    public void HandleAdjustPoint(int x, int y) {
        Point pt = new Point(x, y);
        adjustParam.AddAdjustPoint(pt);
        if (adjustParam.GetAdjustStep() == 4) {
            ViewAdjusting = false;
            penParam = adjustParam.GetAdjustPenParam();
            if (notifyMsg != null) notifyMsg.onAdjustOver();
        }
        invalidate();
    }

    public boolean isSaved() {
        return m_bSaved;
    }

    //白板关闭时，清理缓冲区
    public void CloseBoardView() {
        try {
            File path = new File(tmpPath);
            if (path.exists()) path.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ResetTmpPath() {
        try {
            BoardUtils boardtool = new BoardUtils();
            File path = new File(tmpPath);
            if (path.exists()) {
                boardtool.deleteDir(tmpPath);
            }
            tmpPath = CreateNewTmpDir();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void SetRootTmpPath(String sRootPath) {
        rootTmpPath = sRootPath;
    }

    //创建一个随机的临时目录
    public String CreateNewTmpDir() {
        String sTmpPath;
        Random r = new Random(1);
        while (true) {
            int ran = r.nextInt(10000);
            sTmpPath = rootTmpPath + "/" + String.valueOf(ran);
            File sPath = new File(sTmpPath);
            if (sPath.exists()) continue;
            if (sPath.mkdir()) return sPath.getPath();
        }
    }


    public void RefreshCurrentPage() {
        if (CurrentPage == null)
            return;

        CurrentPage.LoadResource(tmpPath, BoardId);
        SaveBack(null);


    }


    public void SetDefLineColor(int icolor) {
        if (TmpLine != null) {
            TmpLine.LineOption.Color = icolor;
        }

    }

    public void SetDefLineWidth(int iwidth) {
        if (TmpLine != null) {
            TmpLine.LineOption.Width = (short) iwidth;
        }
    }
}

