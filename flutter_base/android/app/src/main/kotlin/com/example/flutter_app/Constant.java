package com.example.flutter_app;

/**
 * Time: 2019/10/18 16:34
 * <p>
 * Author: 橘子丶
 */
public class Constant {
    //线上
    public static String BASE_URL = "http://web1.guanxinqiao.com/api/cloud/";
    //测试
    //public static String BASE_URL   = "http://web1.guanxinqiao.com/gxq/";

    //记录直播学习时长
    public static String STUDYDURATION_URL   = "http://web1.guanxinqiao.com/api/soft/";
    public static String BEIYONG   = "http://www.guanxinqiao.com/api/";

    //文件上传
    public static String UpdateheadIcon = "common.php?action=upload";
    public static String UpdateheadIcon_URL = BASE_URL + UpdateheadIcon;
    public static String WXPAYAPPID = "wx1f74e0ac3a0e03aa";

    //zjb
    public static  String RtcServiceUrl= "http://soft1.guanxinqiao.com:8091/app/v1/login";
    public static  String GgwApiUrl="http://web1.guanxinqiao.com/api/soft/";
    public static  String GgwLessonsUrl="http://soft3.guanxinqiao.com:8080/session.aspx";
    public static  String ServerInfoUrl="http://soft1.guanxinqiao.com:7789/CallGXQMeeting/GetMeetingLocateInfo";
    //public static String ServerInfoUrl="http://192.168.31.22:7788/CallGXQMeeting/GetMeetingLocateInfo";
    /*
     * 护眼模式状态
     */
    public static Boolean IS_EYE_CARE_OPEN = false;
    public static Boolean IS_EYE_CARE_OPEN_BASE = false;
}
