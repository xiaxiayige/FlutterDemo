package com.example.flutter_app.meetingboard;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.OutputStream;

public class CWBText extends CWBObject {
    private String textContent;
    private int textColor;
    private  int backColor;
    private  int bkMode;
    private  byte boardStyle;
    private byte[] lgFont;


    public CWBText() {
        textContent="";
        textColor=0;
        backColor=0;
        bkMode=0;
        boardStyle=0;
        lgFont=new byte[60];
    }
   /*     DWORD LoadFromStream(IStream* pStream);

        DWORD SaveToStream(IStream* pStream);
    DWORD ReadFromBuffer(BYTE* buf);
    DWORD WriteToBuffer(BYTE* buf,const int nCount);
    virtual int BytesCount();

    virtual void Draw(HDC pDC,bool bMask=false,bool bPreview=false);

    void SetText(char* st);
    void SetBorder(WBBorder style);

    virtual void Move(int xof, int yof);

    virtual void Resize(RECT& rt);
    RECT GetTextRect();
    RECT CalcClientRect(RECT rtText);

    int BkMode;

    char BorderStyle;

    LOGFONT lgFont;

    COLORREF TextColor;
    COLORREF BackColor;

    char* Text;
    CWBShape* Border;
    void ReviseBorder();
*/

    public int BytesCount() {
        int iSize = super.BaseBytesCount();
        iSize += 4;  //BKMode
        iSize += 1; //BoarderStyle
        iSize += 60; //lgFont
        iSize += 4;   //TextColor
        iSize += 4;      //BackColor
        iSize +=2;      //text len
        iSize += textContent.getBytes().length;    //text content
        return iSize;
    }

    public long ReadFromBuffer(byte[] buf, int startpos) {
        try {
            long iSize = super.ReadFromBuffer(buf);

            BoardByteBuf boardtool = new BoardByteBuf();
            boardtool.AssignReadBuf(buf, (int) (iSize + 4 + 1 + 60));
            textColor = boardtool.ReadInt();
            backColor = boardtool.ReadInt();
            int txtlen = boardtool.ReadUShort();
            byte[] txtbuffer = boardtool.ReadByteArr(txtlen);

            textContent = new String(txtbuffer, "GB2312");
            return boardtool.GetReadPos();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    public long WriteToBuffer(byte[] buf, int startpos) {
        long  iSize = super.WriteToBuffer(buf, startpos);

        BoardByteBuf boardtool = new BoardByteBuf();
        boardtool.AssignWriteBuf(buf, (int)(iSize+4+1+60));

        boardtool.WriteInt(textColor);
        boardtool.WriteInt(backColor);
        boardtool.WriteUShort(textContent.getBytes().length);
        boardtool.WriteByteArr(textContent.getBytes());

        return boardtool.GetWritePos();
    }


    //将对象保存到流中
    public long SaveToStream(OutputStream outputStream) {
        super.SaveToStream(outputStream);
        BoardUtils boardutil = new BoardUtils();
        boardutil.WriteUIntToStream(bkMode,outputStream);
        boardutil.WriteUByteToStream(boardStyle,outputStream);
        boardutil.WriteBytesToStream(lgFont,outputStream);
        boardutil.WriteColorToStream(textColor,outputStream);
        boardutil.WriteColorToStream(backColor,outputStream);
        boardutil.WriteStringToStream(textContent,outputStream);
        return 0;
    }

    //从流中加载对象
    public long LoadFromStream(DocumentInputStream inputStream) {
        try {
            bkMode = (int) inputStream.readUInt();
            boardStyle = inputStream.readByte();
            inputStream.read(lgFont);
            textColor = inputStream.readInt();
            backColor = inputStream.readInt();
            int txtlen = inputStream.readUShort();
            byte[] buffer = new byte[txtlen];
            inputStream.read(buffer);
            textContent = new String(buffer, "GB2312");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    public void setText(String szText)
    {
        textContent=szText;
    }
    public void Draw(Canvas pDC, boolean bMask, boolean bPreview)
    {


        BoardUtils boardtool=new BoardUtils();
        boardtool.GetARGB(textColor);

        Paint paint = new Paint();
        paint.setTextSize(20);
        String familyName="宋体";
        Typeface font= Typeface.create(familyName, Typeface.NORMAL);
        paint.setTypeface(font);
        pDC.drawText(textContent,this.ClientRect.left,this.ClientRect.bottom,paint);
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
}
