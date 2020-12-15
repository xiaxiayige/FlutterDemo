package com.example.flutter_app.meetingboard;

public interface BoardConstants {

    public static final long rfBoard = 0x46425748;
    public static final long rfAction = 0x46414248;
    public static final long rfVideo = 0x46564248;

    public static final short WBF_VERSION = 21;

    public static int Width_V_A4 = 992;
    public static int Height_V_A4 = 1404;
    public static int Width_H_A4 = 1404;
    public static int Height_H_A4 = 992;

    //关于点阵笔的一些常量
    public static double XDIST_PERUNIT=1.524;
    public static double YDIST_PERUNIT=1.524;

    public static float IN_SIZE=25.40f;

    public static int DPI_150=150;
    public  static int DPI_120=120;


    public static int PAGE_MAXWIDTH = 2400;
    public static int PAGE_MAXHEIGHT = 9600;

    public static enum MarkType {e_Unknown, e_BookMark, e_Url, e_File}

    public static enum WBMainType {
        wbmtUnknown, wbmtShape, wbmtMedia, wbmtText,
        wbmtImage, wbmtOle, wbmtWinMedia, wbmtRealMedia, wbmtFlash, wbmtLink
    }

    public static enum WBShapeType {
        wbstSelect, wbstUnknown, wbstDot, wbstLine, wbstPLine,
        wbstRectangle, wbstPolygon, wbstCircle, wbstEllipse, wbstArc,
        wbstBezier, wbstSector, wbstRight, wbstWrong, wbstArrow,
        wbstDblArrow, wbstHandline, wbstLight, wbstRoundRect, wbstMemo,
        wbstCloud, wbstWave, wbstMask, wbstIndicator, wbstCut,
        wbstErase, wbstFlag, wbstLink, wbstBrush, wbstCover, wbstDrag
    }

    public static enum WBMediaType {
        wbmdUnknown, wbmdText, wbmdWinAudio, wbmdWinVideo, wbmdFlash,
        wbmdRealAudio, wbmdRealVideo
    }


    public static enum WBPageEvent {wbpeUnkown, wbpeNew, wbpeSelect, wbpeDelete, wbpeAppend, wbpeSaveText}


    public static enum ViewAdjustType {vaWidth, vaHeight, vaTotal}


    //http msg
    public static final int Http_DownLoad_Start = 1001;
    public static final int Http_DownLoad_Over = 1002;
    public static final int Http_DownLoad_Progress = 1003;
    //ftp msg
    public static final  int Ftp_UpLoad_Start=1101;
    public static final  int Ftp_UpLoad_Over=1102;
    public static final  int Ftp_Upload_Progress=1103;


    //about ftp
    public static final String FTP_CONNECT_SUCCESSS = "ftp连接成功";
    public static final String FTP_CONNECT_FAIL = "ftp连接失败";
    public static final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";
    public static final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";

    public static final String FTP_UPLOAD_SUCCESS = "ftp文件上传成功";
    public static final String FTP_UPLOAD_FAIL = "ftp文件上传失败";
    public static final String FTP_UPLOAD_LOADING = "ftp文件正在上传";

    public static final String FTP_DOWN_LOADING = "ftp文件正在下载";
    public static final String FTP_DOWN_SUCCESS = "ftp文件下载成功";
    public static final String FTP_DOWN_FAIL = "ftp文件下载失败";

    public static final String FTP_DELETEFILE_SUCCESS = "ftp文件删除成功";
    public static final String FTP_DELETEFILE_FAIL = "ftp文件删除失败";

    public static final int Thumb_Width = 150;
    public static final int Thumb_Height = 150;

    public static enum ByteDataType {
        bdtUnknow, bdtBool, bdtChar, bdtBytes,
        bdtInt16, bdtInt32, bdtInt64, bdtUInt16, bdtUInt32, bdtUInt64,
        bdtString, bdtDateTime, bdtIP, bdtDouble, bdtSubType
    }
    public static enum FileType {ftNormal,ftImage,ftBoard,ftResource,ftPatch,ftScreen,ftLecture}

    public static enum MeetingServerFlag {fms_UnKnown,fms_SID,fms_Name,fms_IP,fms_Port,fms_Capacity,fms_Online,fms_Circuit}

    public static enum MeetingStatu {msPrepare,msStand,msHand,msSpeaker,msManager,msJoing}

    public static enum SocketSource {ssNULL ,ssMainForm,ssDetailForm,ssChatForm,
        ssSearchForm,ssOptionForm,ssFriendList,ssNoteForm,ssClassMember, ssCallCmd,ssJoinMeeting }

    public static enum BrowserAction
    {
        baNone , baNewUrl, baNavigate, baActive, baClose, baBack, baNext, baPanel, paNew, paActive, paClose, paDelete,
        pptNavigate, pptClick, pptNextSlide, pptPreSlide, pptNextAni, pptPreAni, pptClose, pptLoad, pptHtmlNav, pptGoTo
    }

    public static  enum MediaEvent
    {
        mtUnknown,mtPlay , mtPause, mtContinue, mtClose, mtSeek
    }

   public static enum WBControlType
   {
       wbctNULL , wbctMove, wbctLeft, wbctTopLeft, wbctTop,
        wbctTopRight, wbctRight, wbctBottomRight, wbctBottom, wbctBottomLeft
    }

    /*intent tag*/
    public static final String INTENT_IP="192.168.31.140";//ip
    public static final String INTENT_PORT="8080";//port(端口号)
    public static final String md5PrivateKey="1@aD4FEa";



    /*EventBus  msg*/
    public static final String CONNET_SUCCESS="connectSucccess";
    public static final String RECTCPPACKET="rectcppacket";
    public static final String CONNECT_FAIL="connectFail";
    public static final String Meeting_Msg="meetingMsg";


}
