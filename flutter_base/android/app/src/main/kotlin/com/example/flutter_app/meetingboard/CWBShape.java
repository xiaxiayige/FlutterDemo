package com.example.flutter_app.meetingboard;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.OutputStream;
import java.util.ArrayList;

public class CWBShape extends CWBObject {
    //线型选项
    public CWBImage.WBLineOption LineOption;
    //填充选项
    public CWBImage.WBFillOption FillOption;
    public int PenColorIndex;
    public boolean bShapeDrawing;
    public boolean bTransparent; //wbstCover是否透明显示，默认为false
    public int VertexCount;
    //图形顶点数据
    public ArrayList<Point> Vertexs;

    public CWBShape() {
        Reset(0);
    }

    public CWBShape(int iCount) {
        Reset(iCount);
    }

    private void Reset(int iCount) {
        MainType= BoardConstants.WBMainType.wbmtShape;
        LineOption=new CWBImage.WBLineOption();
        LineOption.Style = 0;//PS_SOLID;
        LineOption.Color = Color.BLACK;
        LineOption.Width = 1;
        LineOption.Alpha = 255;
        FillOption=new CWBImage.WBFillOption();
        VertexCount = iCount;
        Vertexs = new ArrayList<Point>(iCount);
        PenColorIndex = -1;
        bShapeDrawing = false;
        bTransparent = false;
    }

    //更新对象的客户区域
    public void UpdateRect() {
        int iW, iH, nW, nH, nM, i;

        iW = nW = Vertexs.get(0).x;
        for (i = 1; i < Vertexs.size(); i++) {
            nM = Vertexs.get(i).x;
            iW = Math.min(iW, nM);
            nW = Math.max(nW, nM);
        }
        iH = nH = Vertexs.get(0).y;
        for (i = 1; i < Vertexs.size(); i++) {
            nM = Vertexs.get(i).y;
            iH = Math.min(iH, nM);
            nH = Math.max(nH, nM);
        }
        ClientRect.left = iW;
        ClientRect.right = nW;
        ClientRect.top = iH;
        ClientRect.bottom = nH;
    }

    public void Move(int xof, int yof)
    {
        super.Move(xof,yof);
        for(int i=0;i<Vertexs.size();i++)
        {
            Vertexs.get(i).x+=xof;
            Vertexs.get(i).y+=yof;
        }
    }

    //根据一个基准点和一个新的点放大对象的客户矩形区域，并更新对象的数据
    public void Resize(Rect rt){

        // TODO: Add your specialized code here.
        int ow = ClientRect.right - ClientRect.left;
        int oh = ClientRect.bottom - ClientRect.top;
        Rect rect = ClientRect;
        super.Resize(rt);
        int nw = ClientRect.right - ClientRect.left;
        int nh = ClientRect.bottom - ClientRect.top;
        if((ow == 0) || (oh == 0)) return;
        double dx = ((double)nw) / ow;
        double dy = ((double)nh) / oh;
        double dv;

        for(int id = 0; id < VertexCount; id++)
        {
            dv = dx * (Vertexs.get(id).x - rect.left);
            Vertexs.get(id).x =( int)(dv + rt.left + 0.5);
            dv = dy * (Vertexs.get(id).y - rect.top);
            Vertexs.get(id).y = (int)(dv + rt.top + 0.5);

        }
    }

    //旋转对象
    public void Rotation(Point ptBase, int iDegree)
    {
        Rect rect = ClientRect;
        Point pt;
        int xof, yof;
        if(iDegree == 90)       //顺时针旋转90度
        {
            for(int i = 0; i < Vertexs.size(); i++)
            {
                pt =  Vertexs.get(i);
                xof = pt.x - rect.left;
                yof = pt.y - rect.top;
                Vertexs.get(i).x = rect.right - yof;
                Vertexs.get(i).y = rect.top + xof;
            }
        }
        UpdateRect();
    }

    //画出对象
    public void Draw(Canvas pDC, boolean bMask, boolean bPreview){
        BoardUtils boardtool=new BoardUtils();
        //handline
        if(this.SubType== BoardConstants.WBShapeType.wbstHandline) {
            Paint paint = new Paint();
            int linecolor = boardtool.GetARGB(LineOption.Color);
            paint.setColor(linecolor);
            paint.setStrokeWidth(LineOption.Width);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            pDC.drawPath(measurePath(), paint);
            return;
        }
        //rectangle
        if (this.SubType== BoardConstants.WBShapeType.wbstRectangle)
        {
            Paint paint = new Paint();
            int linecolor = boardtool.GetARGB(LineOption.Color);
            paint.setColor(linecolor);
            paint.setStrokeWidth(LineOption.Width);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            Rect rect=new Rect(Vertexs.get(0).x,Vertexs.get(0).y,Vertexs.get(1).x,Vertexs.get(1).y);
            pDC.drawRect(rect,paint);
            return;

        }
        //ellipse
        if (this.SubType== BoardConstants.WBShapeType.wbstEllipse)
        {
            Paint paint = new Paint();
            int linecolor = boardtool.GetARGB(LineOption.Color);
            paint.setColor(linecolor);
            paint.setStrokeWidth(LineOption.Width);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            RectF rect=new RectF(Vertexs.get(0).x,Vertexs.get(0).y,Vertexs.get(1).x,Vertexs.get(1).y);
            pDC.drawOval(rect,paint);
            return;
        }
        //line
        if (this.SubType== BoardConstants.WBShapeType.wbstLine)
        {
            Paint paint = new Paint();
            int linecolor = boardtool.GetARGB(LineOption.Color);
            paint.setColor(linecolor);
            paint.setStrokeWidth(LineOption.Width);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            pDC.drawLine(Vertexs.get(0).x,Vertexs.get(0).y,Vertexs.get(1).x,Vertexs.get(1).y,paint);
            return;
        }
        //polygon
        if (this.SubType== BoardConstants.WBShapeType.wbstPolygon)
        {
            Paint paint = new Paint();
            int linecolor = boardtool.GetARGB(LineOption.Color);
            paint.setColor(linecolor);
            paint.setStrokeWidth(LineOption.Width);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            //   pDC.drawLine(Vertexs.get(0).x,Vertexs.get(0).y,Vertexs.get(1).x,Vertexs.get(1).y,paint);

            Path path = new Path();
            path.moveTo(Vertexs.get(0).x,Vertexs.get(0).y);
            path.lineTo(Vertexs.get(1).x,Vertexs.get(1).y);
            path.lineTo(Vertexs.get(2).x,Vertexs.get(2).y);
            path.close(); // 使这些点构成封闭的多边形
            pDC.drawPath(path, paint);

            return;
        }

    }
    public void Draw(Canvas pDC, boolean bMask)
    {
        boolean bPreview=false;
        Draw(pDC,bMask,bPreview);
    }
    public void Draw(Canvas pDC)
    {
        boolean bMask=false;
        boolean bPreview=false;
        Draw(pDC,bMask,bPreview);
    }
    //画出对象的顶点(主要针对弧线与贝塞尔曲线)
    public void DrawVertexs(Canvas pDC) {
    }

    //设置顶点数据
    public void SetVertex(Point pt, int id) {
        if (id < Vertexs.size()) {
            Vertexs.get(id).x = pt.x;
            Vertexs.get(id).y = pt.y;
        }
    }

    //读取顶点数据
    public Point GetVertex(int id) {
        return Vertexs.get(id);
    }

    //设置顶点数量
    public void SetCount(int iCount) {
    }

    //读取顶点数量
    public int GetCount( ) {
        return Vertexs.size();
    }

    //从另一个对象区复制数据
    public void Assign(CWBShape SrcObj) {
        super.Assign(SrcObj);
        LineOption.Alpha = SrcObj.LineOption.Alpha;
        LineOption.Color=SrcObj.LineOption.Color;
        LineOption.Style=SrcObj.LineOption.Style;
        LineOption.Width=SrcObj.LineOption.Width;
        FillOption.FillStyle = SrcObj.FillOption.FillStyle;
        FillOption.FillData=SrcObj.FillOption.FillData;
        FillOption.FillType=SrcObj.FillOption.FillType;
        bTransparent = SrcObj.bTransparent;
        Vertexs.clear();
        for (int i = 0; i < SrcObj.Vertexs.size(); i++) {
            Point pt = (Point) (SrcObj.Vertexs.get(i));
            Point newpt = new Point(pt.x, pt.y);
            Vertexs.add(newpt);
        }
    }

    //将对象保存到流中
    public long SaveToStream(OutputStream outputStream) {
        super.SaveToStream(outputStream);
        LineOption.WriteToStream(outputStream);
        FillOption.WriteToStream(outputStream);
        BoardUtils boardutil = new BoardUtils();
        boardutil.WriteUShortToStream(VertexCount, outputStream);
        for (int i = 0; i < Vertexs.size(); i++) {
            Point pt = (Point) (Vertexs.get(i));
            boardutil.WriteUShortToStream(pt.x, outputStream);
            boardutil.WriteUShortToStream(pt.y, outputStream);
        }
        return 0;
    }

    //从流中加载对象
    public long LoadFromStream(DocumentInputStream inputStream) {
        LineOption.ReadFromStream(inputStream);
        FillOption.ReadFromStream(inputStream);
        VertexCount = inputStream.readUShort();
        Vertexs = new ArrayList<Point>();
        Point pt;
        for (int i = 0; i < VertexCount; i++) {
            pt = new Point();
            pt.x = (int) (inputStream.readUShort());
            pt.y = (int) (inputStream.readUShort());
            Vertexs.add(pt);
        }
        return 0;
    }

    public long ReadFromBuffer(byte[] buf) {
        int startpos = 0;
        ReadFromBuffer(buf, startpos);
        return 0;
    }

    public long ReadFromBuffer(byte[] buf, int startpos) {
        long  iSize =   super.ReadFromBuffer(buf) ;
        iSize = LineOption.ReadFromBuffer(buf, (int)iSize);
        iSize = FillOption.ReadFromBuffer(buf, (int)iSize);

        BoardByteBuf boardtool = new BoardByteBuf();
        boardtool.AssignReadBuf(buf, (int)iSize);
        VertexCount = boardtool.ReadUShort();
        Vertexs = new ArrayList<Point>();
        Point pt;
        for (int i = 0; i < VertexCount; i++) {
            pt = new Point();
            pt.x = (int)(boardtool.ReadUShort());
            pt.y = (int)(boardtool.ReadUShort());
            Vertexs.add(pt);
        }
        return boardtool.GetReadPos();
    }

    public long WriteToBuffer(byte[] buf, int startpos) {
        long  iSize = super.WriteToBuffer(buf, startpos);
        iSize = LineOption.WriteToBuffer(buf, (int )iSize);
        iSize = FillOption.WriteToBuffer(buf, (int)iSize);
        BoardByteBuf boardtool = new BoardByteBuf();
        boardtool.AssignWriteBuf(buf, (int)iSize);
        boardtool.WriteUShort(VertexCount);
        for (int i = 0; i < Vertexs.size(); i++) {
            Point pt = (Point) Vertexs.get(i);
            boardtool.WriteUShort(pt.x);
            boardtool.WriteUShort(pt.y);
        }
        return boardtool.GetWritePos();
    }

    public long WriteToBuffer(byte[] buf) {
        int startpos = 0;
        return WriteToBuffer(buf, startpos);
    }

    public int BytesCount() {
        int iSize = super.BaseBytesCount();
        iSize += LineOption.BytesCount();  // LineOption
        iSize += FillOption.BytesCount(); //FillOption
        iSize += 2;   //vertexcount
        iSize += Vertexs.size() * 4;
        return iSize;
    }

    public void Mirror(int nWay) {

    }

    public void Rotate(int iDegree) {

    }

    public void FastDraw(Canvas pDC, int iLast) {
    }

    public void SetPenColor(int iColorIndex) {
    }


    public void ReadVertex(CWBShape ObjShape) {
        Vertexs.clear();
        Vertexs = (ArrayList<Point>) (ObjShape.GetPointList().clone());
    }


    public ArrayList<Point> GetPointList() {
        return Vertexs;
    }


    private Path measurePath() {
        final float lineSmoothness = 0.1f;
        //保存曲线路径
        Path mPath = new Path();
        //保存辅助线路径
        Path mAssistPath = new Path();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX;
        float nextPointY;
        ArrayList<Point> mPointList=Vertexs;
        final int lineSize = mPointList.size();
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                Point point = mPointList.get(valueIndex);
                currentPointX = point.x;
                currentPointY = point.y;
            }
            if (Float.isNaN(previousPointX)) {
                //是否是第一个点
                if (valueIndex > 0) {
                    Point point = mPointList.get(valueIndex - 1);
                    previousPointX = point.x;
                    previousPointY = point.y;
                } else {
                    //是的话就用当前点表示上一个点
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                //是否是前两个点
                if (valueIndex > 1) {
                    Point point = mPointList.get(valueIndex - 2);
                    prePreviousPointX = point.x;
                    prePreviousPointY = point.y;
                } else {
                    //是的话就用当前点表示上上个点
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }

            // 判断是不是最后一个点了
            if (valueIndex < lineSize - 1) {
                Point point = mPointList.get(valueIndex + 1);
                nextPointX = point.x;
                nextPointY = point.y;
            } else {
                //是的话就用当前点表示下一个点
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                // 将Path移动到开始点
                mPath.moveTo(currentPointX, currentPointY);
                mAssistPath.moveTo(currentPointX, currentPointY);
            } else {
                // 求出控制点坐标
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (lineSmoothness * firstDiffX);
                final float firstControlPointY = previousPointY + (lineSmoothness * firstDiffY);
                final float secondControlPointX = currentPointX - (lineSmoothness * secondDiffX);
                final float secondControlPointY = currentPointY - (lineSmoothness * secondDiffY);
                //画出曲线
                mPath.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
                //将控制点保存到辅助路径上
                mAssistPath.lineTo(firstControlPointX, firstControlPointY);
                mAssistPath.lineTo(secondControlPointX, secondControlPointY);
                mAssistPath.lineTo(currentPointX, currentPointY);
            }

            // 更新值,
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        return mPath;
    }
}
