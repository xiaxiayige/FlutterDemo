package com.example.flutter_app.meetingboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.OutputStream;


public class CWBImage extends CWBObject {

    //是否初始化
    private boolean m_bIni;
    //是否透明
    private boolean m_Transparent;
    //宽度
    private long m_Width;
    //高度
    private long m_Height;
    //透明色彩
    private int TransColor;
    //图片数据
    private Bitmap m_Bitmap;
    private String szExt;
    private double Scale;

    private boolean Ini(boolean bScale) {
        return true;
    }

    private boolean Ini() {
        boolean bScale = false;
        return Ini(bScale);
    }

    public CWBImage() {
        MainType = BoardConstants.WBMainType.wbmtImage;
        SubType = BoardConstants.WBShapeType.wbstUnknown;
        m_bIni = false;
        m_Transparent = false;
        TransColor = Color.rgb(0, 0, 0);
        m_Width = 0;
        m_Height = 0;
        m_Bitmap = null;
        Scale = 1.0;
    }

    //从文件中读取图像数据
    public boolean LoadImgFromFile(String szFile) {

        try {

            m_Bitmap = BitmapFactory.decodeFile(szFile);
          //  m_Bitmap = BitmapFactory.decodeFile(szFile);
            return true;
        } catch (OutOfMemoryError e) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                m_Bitmap = BitmapFactory.decodeFile(szFile,  options);
                return true;
            } catch (Exception ee) {
               e.printStackTrace();

            }
            return false;
        }
    }

    //从流中读取图像数据,保留
    public long LoadImgFromStream(DocumentInputStream inputStream) {
        return 0;
    }

    //将对象保存到流中
    public long SaveToStream(OutputStream outputStream) {
        super.SaveToStream(outputStream);
        BoardUtils boardtool = new BoardUtils();
        boardtool.WriteStringToStream(szExt, outputStream);
        boardtool.WriteUByteToStream((short) (m_Transparent ? 1 : 0), outputStream);
        boardtool.WriteColorToStream(TransColor, outputStream);
        return 0;
    }

    public long LoadFromStream(DocumentInputStream inputStream) {
        BoardUtils boardtool = new BoardUtils();
        szExt = boardtool.ReadStringFromStream(inputStream);
        int ivalue = inputStream.readUByte();
        m_Transparent = (ivalue != 0);
        TransColor = inputStream.readInt();
        return 0;
    }

    //将图像导出到文件中
    public boolean SaveToFile(String szFile) {

        return true;

    }

    //画出对象
    public void Draw(Canvas pDC) {
        boolean bMask = false;
        Draw(pDC, bMask);
    }

    public void Draw(Canvas pDC, boolean bMask) {
        if (Deleting || (Visible == false)) return;
        Paint paint = new Paint();
        if (bMask) {
            paint.setColor((int) ObjectID);
            pDC.drawRect(ClientRect, paint);
        } else {
            // if (m_Bitmap == null) LoadImgFromFile();
            if (m_Bitmap != null) {
                pDC.drawBitmap(m_Bitmap, new Rect(0,0,m_Bitmap.getWidth(),m_Bitmap.getHeight()),ClientRect, paint);

            }
        }
    }


    public void Resize(Rect rt) {
        super.Resize(rt);
    }

    public long GetWidth() {
        if (m_Bitmap != null) return m_Bitmap.getWidth();
        else return m_Width;
    }

    public long GetHeight() {
        if (m_Bitmap != null) return m_Bitmap.getHeight();
        else return m_Height;
    }

    //设置对象矩形区域
    public void SetRect(Rect rect) {
        ClientRect = rect;
    }

    public void SetTransparent(boolean bTrans) {
        if (m_Transparent != bTrans) {
            m_Transparent = bTrans;
            //if(m_Bitmap != NULL) m_Bitmap->Transparent = m_Transparent;
        }
    }

    public void SetTransColor(int cValue) {
        if (TransColor != cValue) {
            TransColor = cValue;

        }
    }

    public boolean IsTransparent() {
        return m_Transparent;
    }

    public String GetFileName() {
        return Long.toString(ObjectID) + szExt;
    }

    public void SetExt(String sz) {
        szExt = sz;
    }

    public String GetExt() {
        return szExt;
    }

    public void ReleaseImage() {
        m_Bitmap = null;
    }

    public int GetTransparentColor() {
        return TransColor;
    }


    public static class WBFileOption {
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

    public static class WBFillOption {
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

    public static class WBLineOption {
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
}
