package com.example.flutter_app.meetingboard;

public class BoardMessage {
    public static final int vcDebug = 0x00;//调试保留用命令
    public static final int vcLogin = 0x01;//登录请求
    public static final int vcCfmLogin = 0x02;//登录返回
    public static final int vcCfmOnline = 0x03;//在线确认(心跳)
    public static final int vcStatusChange = 0x04;//好友状态变化通知
    public static final int vcKickOut = 0x05;//被服务器踢出(强行离线)
    public static final int vcLogout = 0x06;//退出、注销登录
    public static final int vcAppendNote = 0x07;//添加留言请求
    public static final int vcSearchFriend = 0x0A;//搜索好友资料
    public static final int vcCfmSearchFriend = 0x0B;//搜索好友命令的返回
    public static final int vcGetTotalOnline = 0x0C;//读取在线用户总数
    public static final int vcCfmTotalOnline = 0x0D;//读取在线用户总数
    public static final int vcLocalPubic = 0x0E;//服务器返回本地公网地址
    public static final int vcUdpConnect = 0x0F;//UDP连接协议
    public static final int vcSetCallStatu = 0x10;//设置用户的呼叫状态
    public static final int vcAppendCourseware = 0x11;//添加课件记录

    public static final int vcFMRQ = 0x23;
    public static final int vcFMCF = 0x24;
    public static final int vcWCRQ = 0x25;
    public static final int vcWCCF = 0x26;
    public static final int vcWDRQ = 0x27;

    public static final int vcRegisterUser = 0x1001;//
    public static final int vcCfmRegister = 0x1002;
    public static final int vcModifyUserInfo = 0x1003;//修改用户信息
    public static final int vcCfmModifyUser = 0x1004;//
    public static final int vcModifyPwd = 0x1005;//修改用户密码
    public static final int vcCfmModifyPwd = 0x1006;//
    public static final int vcModifyOption = 0x1007;//修改用户设置
    public static final int vcCfmModifyOption = 0x1008;
    public static final int vcModifyRelation = 0x1009;//修改好友关系(添加、删除、修改)
    public static final int vcCfmModifyRelation = 0x100A;
    public static final int vcModifyTeamText = 0x100B;//修改分组信息
    public static final int vcCfmModifyTeam = 0x100C;
    public static final int vcRelationChange = 0x100D;//好友关系改变通知
    public static final int vcModifyPhoto = 0x100E;//修改形象照片
    public static final int vcCfmModifyPhoto = 0x100F;

    public static final int vcGetUserDetail = 0x1101;//查看朋友信息
    public static final int vcCfmUserDetail = 0x1102;//查看朋友信息
    public static final int vcGetFriendList = 0x1103;//读取朋友列表
    public static final int vcFriendListHead = 0x1104;//好友列表的头信息
    public static final int vcCfmFriendList = 0x1105;//读取朋友列表
    ;//public static final int  vcGetUserBase       =0x1106  ;//获取用户基本信息
    ;//public static final int  vcCfmUserBase       =0x1107  ;//获取用户基本信息
    ;//public static final int  vcGetBatUser        =0x1108  ;//获取一批用户的信息
    ;//public static final int  vcCfmBatUser        =0x1109  ;//返回一批用户的信息
    public static final int vcSendBatCommand = 0x110A;//向一批用户发送命令
    public static final int vcQueryFriendState = 0x110B;//查询用户状态信息
    public static final int vcCfmFriendState = 0x110C;//
    public static final int vcWebNotify = 0x1120;//Web上的通知信息
    public static final int vcGetNoteList = 0x1121;//向服务器请求通知列表
    public static final int vcCfmNoteList = 0x1122;//服务器返回通知列表

    public static final int vcSendCallRequire = 0x1201;//发送呼叫
    public static final int vcRcvCallRequire = 0x1202;//接收呼叫
    public static final int vcGetCallRequire = 0x1203;//读取呼叫(限教师)
    public static final int vcCfmCallRequire = 0x1204;//响应呼叫
    public static final int vcCancelCallRequire = 0x1205;//取消呼叫
    public static final int vcNoCfmCall = 0x1210;//未响应(接收)呼叫日志
    public static final int vcNtfBBSQuestion = 0x1211;//BBS问题帖的通知
    public static final int vcCfmBBSQuestion = 0x1212;//教师对BBS问题的回复

    public static final int vcUserCoinChange = 0x1301;//用户金额变化的通知

    public static final int vcCallNote = 0x1311;//通知对方，是在哪个现场呼叫你

    public static final int vcGetSSPInfo = 0x1403;
    public static final int vcCfmSSPInfo = 0x1404;
    public static final int vcSearchSSP = 0x1414;//查找问答服务商
    public static final int vcCfmSearchSSP = 0x1415;//返回查找结果

    public static final int vcAskCanCall = 0x1501;//向服务器询问是否可以发起一对一的呼叫
    public static final int vcCfmCanCall = 0x1502;//服务器返回请求结果
    public static final int vcCallOnline = 0x1503;//通知服务器呼叫正在进行中
    public static final int vcCfmCallOnline = 0x1504;//服务器对通知的处理返回
    public static final int vcSearchCmdZX = 0x1510;//吉林征信通征信查询

    public static final int vcGetServerInfo = 0x1600;//读取服务器信息
    public static final int vcCfmServerInfo = 0x1601;//返回服务器信息

    public static final int vcUdpHole = 0x2000;//UDP 打洞消息
    public static final int vcInviteVerify = 0x2003;//请求将对方加入好友
    public static final int vcConfirmVerify = 0x2004;//好友验证返回
    public static final int vcUdpConnectRequire = 0x2005;//与用户建立UDP连接
    public static final int vcUdpConnectConfirm = 0x2006;//连接返回信息
    public static final int vcDebugGetEv = 0x2007;//读取用户环境信息
    public static final int vcDebugReturnEv = 0x2008;//返回用户环境信息
    public static final int vcAppendFriendTip = 0x2009;//添加好友的提示信息

    public static final int vcPopupAffiche = 0x2021;//公告
    public static final int vcOnineAffiche = 0x2022;//在线公告信息

    public static final int vcChatCommand = 0x2051;//聊天命令(需聊天窗口处理)
    public static final int vcSendText = 0x2052;//发送文字信息
    public static final int vcAskVoiceChat = 0x2061;//发送语音交谈邀请
    public static final int vcConfirmTalk = 0x2062;//语音邀请确认
    public static final int vcStopTalk = 0x2063;//中止语音交谈
    public static final int vcAudioData = 0x2064;//声音数据
    public static final int vcAudioReply = 0x2065;//声音数据返回
    public static final int vcMeetingFile = 0x2066;//会议情况下的文件
    public static final int vcSpeakerAudio = 0x2067;//会议情况下向主持人向发言人发送的音频数据
    public static final int vcScreenShare = 0x2068;//屏幕共享请求
    public static final int vcShareCtrl = 0x2069;//屏幕控制数据
    public static final int vcStopScreenShare = 0x2070;//屏幕共享的停止

    public static final int vcAskFileTransfer = 0x2071;//文件传输申请
    public static final int vcConfirmTransfer = 0x2072;//文件传输确认
    public static final int vcStopFileTrans = 0x2073;//停止文件传输
    public static final int vcCompleteFileTrans = 0x2074;//停止文件传输
    public static final int vcFileData = 0x2075;//文件传输数据
    public static final int vcFileReply = 0x2076;//文件传输回执
    public static final int vcViewScreen = 0x207A;//教师查看学生屏幕的请求, 原编号，=0x2077
    public static final int vcDeleteFileTask = 0x2078;//停止文件传输任务
    public static final int vcShowScreen = 0x2079;//显示接收到的对方屏幕

    public static final int vcAskVideoChat = 0x2081;//视频邀请
    public static final int vcConfirmVideo = 0x2082;//视频确认
    public static final int vcStopVideo = 0x2083;//停止视频
    public static final int vcVideoData = 0x2084;//视频数据
    public static final int vcVideoReply = 0x2085;//视频数据回执
    public static final int vcScreenData = 0x2086;//屏幕数据
    public static final int vcScrCtrlData = 0x2087;//屏幕控制数据
    public static final int vcGetScreen = 0x2088;//获取全屏幕数据
    public static final int vcResendVideo = 0x2089;//请求重发视频包
    public static final int vcResendVideoEx = 0x208A;//
    public static final int vcStartMtSS = 0x208B;//开始共享会议屏幕
    public static final int vcSSMousePos = 0x208C;//停止共享
    public static final int vcEndMtSS = 0x208D;//停止共享会议屏幕
    public static final int vcSetMtSSUser = 0x208E;//设置共享视频用户

    public static final int vcMeetingCommand = 0x2100;//会议命令　Member To Server
    public static final int vcInviteMeeting = 0x2101;//会议邀请
    public static final int vcQuitMeeting = 0x2102;//退出会议
    public static final int vcMeetingText = 0x2103;//会议聊天文本信息
    public static final int vcApplyMeeting = 0x2104;//申请创建会议(预约）
    public static final int vcCfmApplyMeeting = 0x2105;//申请创建会议的回执
    public static final int vcCreateMeeting = 0x2106;//创建会议
    public static final int vcCfmCreateMeeting = 0x2107;//创建会议的回执
    public static final int vcMeetingPatch = 0x2108;//设置会议的附件
    public static final int vcGetMeetingInfo = 0x2109;//获取会议信息
    public static final int vcCfmMeetingInfo = 0x210A;//获取会议信息的回执
    public static final int vcJoinMeeting = 0x210B;//加入会议
    public static final int vcCfmJoinMeeting = 0x210C;//加入会议的回执
    public static final int vcSetMtStatu = 0x210D;//准备工作完成
    public static final int vcRefuseMeeting = 0x210E;//拒绝参加会议
    public static final int vcRaiseHand = 0x210F;//举手申请发言
    public static final int vcKickMember = 0x2110;//踢出参会成员
    public static final int vcSetSpeaker = 0x2111;//设置发言人
    public static final int vcSetManager = 0x2112;//设置会议的管理者
    public static final int vcStartMeeting = 0x2113;//开始会议
    public static final int vcEndMeeting = 0x2114;//结束会议
    public static final int vcCfmMeetingOnline = 0x2115;//会议心跳包
    public static final int vcGetMemberList = 0x2116;//读取会议成员列表的请求
    public static final int vcCfmMemberList = 0x2117;//会议成员列表
    public static final int vcMtPrivateChat = 0x2118;//私聊
    public static final int vcMtGetWBList = 0x2119;//会议迟到者向管理员请求对象列表
    public static final int vcMtSimObject = 0x211A;//管理员向会议迟到者发送对象
    public static final int vcAskStartBoard = 0x211B;//请求开始白板
    public static final int vcCfmAskBoard = 0x211C;//请求白板的回复
    public static final int vcAskSpeak = 0x211D;//申请发言权限
    public static final int vcSetMtMode = 0x211E;//设置会议模式
    public static final int vcSetMtLayout = 0x211F;//设置会议的布局
    public static final int vcLightMove = 0x2120;//test
    public static final int vcStartBoard = 0x2121;//开始多人画板
    public static final int vcEndBoard = 0x2122;//结束多人画板
    public static final int vcCfmMtInvite = 0x2123;//回应会议邀请，仅拒绝时用到
    public static final int vcMtWBListComplete = 0x2124;//主持人通知新加入者对象列表发送完成
    public static final int vcGetHandList = 0x2125;//读取申请发言人列表
    public static final int vcCfmHandList = 0x2126;//返回申请发言人列表
    public static final int vcGetMtInfoEx = 0x2127;//
    public static final int vcCfmMtInfoEx = 0x2128;//
    public static final int vcSetMtInfoEx = 0x2129;//
    public static final int vcSetMtPwd = 0x212A;//
    public static final int vcSetMtBlack = 0x212C;//设置会议黑名单
    public static final int vcGetMtBlack = 0x212D;//读取会议黑名单列表
    public static final int vcCfmMtBlack = 0x212E;//返回黑名单列表
    public static final int vcSetMtAutoCloseMic = 0x212F;//设置会议属性

    public static final int vcSetMicTime = 0x213B;//设置麦时（每次上麦可用的时间）
    ;//IM 用，有与MT 重复
    public static final int vcGetMtServers = 0x2125;//获取可以创建会议的服务器列表
    public static final int vcCfmMtServers = 0x2126;//返回可用的会议服务器列表

    public static final int vcGetMtList = 0x2130;//读取聊天室列表
    public static final int vcCfmMtList = 0x2131;//返回聊天室列表
    public static final int vcGetRecommendMt = 0x2140;//读取推荐会议列表
    public static final int vcCfmRecommendMt = 0x2141;//返回推荐会议列表
    public static final int vcSearchMt = 0x2142;//查找会议
    public static final int vcCfmSearchMt = 0x2143;//返回查找到的会议
    public static final int vcGetMtStatus = 0x2144;//读取会议状态
    public static final int vcCfmMtStatus = 0x2145;//返回会议状态
    public static final int vcLockMeet = 0x2146;//锁定会议，03=Locked(bool)
    public static final int vcCfmLockMeet = 0x2147;//
    public static final int vcAddSequenceAct = 0x2150;//添加白板序列命令
    public static final int vcCfmSequenceAct = 0x2151;//返回白板序列命令
    public static final int vcAttachTcp = 0x2152;//将通知服务器TCP连接关联到用户
    public static final int vcCfmAttachTcp = 0x2153;
    public static final int vcLockPaint = 0x2154;//锁定白板，禁止刷新
    public static final int vcReconnectTcp = 0x2155;//重新连接TCP
    public static final int vcServerMtFile = 0x2156;//多服务器文件上传下载
    public static final int vcGetMtRmStatu = 0x2160;//读取会议室状态
    public static final int vcCfmMtRmStatu = 0x2161;//返回会议室状态
    public static final int vcResetBoard = 0x2162;//会议室白板清场
    public static final int vcAskMng = 0x2163;//申请主持人
    public static final int vcCfmAskMng = 0x2164;//申请主持人失败时的返回
    public static final int vcSetMng = 0x2165;//设置主持人
    public static final int vcMngControl = 0x2166;//主持人设置参会人员参数命令
    public static final int vcRollCall = 0x2167;//主持人点名
    public static final int vcCfmRollCall = 0x2168;//被点名人应答

    public static final int vcMtAudio = 0x2170;//会议音频数据
    public static final int vcTcpDisconnect = 0x2171;//会议TCP连接断开
    public static final int vcYuntaiControl = 0x2172;//云台控制命令
    ;//public static final int  vcQueryVideo      =0x2173  ;//视频轮循
    public static final int vcSetFullScrUser = 0x2174;//设置视频全屏用户
    public static final int vcQueryMCast = 0x2175;//询问是否存在组播服务器
    public static final int vcCfmMCast = 0x2176;//返回组播服务器(对应询问)
    //    public static final int vcAskVote = 0x2177;//发起会议投票
    public static  final  int vcSetForbiddenUser=0x2280;    //某个用户禁言
    public static final int vcStartVod = 0x2181;//开始点播
    public static final int vcStopVod = 0x2182;//停止点播
    public static final int vcViewDesktop=0x2183;//主持人查看桌面消息
    public  static  final int vcMediaEvent=0x2185;  //媒体播放器消息



    public static final int vcMobilePhoto = 0x2193;//课堂中添加了一个照片图片

    public static final int vcPageZoom = 0x21A0;//页面缩放
    public static final int vcScrollShowPoint = 0x21A1;//滚动条滚动显示指定的点
    public static final int vcRecommendList = 0x21B0;//推荐列表命令
    public static final int vcStartLessonTest = 0x21B1;//开始课堂练习

    public static final int vcStartPaperRemark = 0x21B2;//开始试卷点评

    public static  final  int vcVotePaper=0x2177;//发起问卷
    public static  final  int vcCfmVotePaper=0x21C1;//问卷回复
    public static  final  int vcShowStatis=0x2179;//问卷回复

    public static final int vcAskPPShare = 0x2201;//请求使用共享白板
    public static final int vcConfirmPPShare = 0x2202;//共享白板请求确认
    public static final int vcStopPPShare = 0x2203;//终止一个共享白板
    public static final int vcAppendWBObject = 0x2204;//添加一个白板对象
    public static final int vcDeleteWBObject = 0x2205;//删除一个白板对象
    public static final int vcResizeWBObject = 0x2206;//调整白板对象的尺寸，位置
    public static final int vcSetWBData = 0x2207;//设置白板对象的数据内容
    public static final int vcSetWBOption = 0x2208;//设置白板对象的选项
    public static final int vcTransformWBObject = 0x2209;//对白板对象进行变换操作
    public static final int vcLockWBObject = 0x220A;//锁定(解锁)一个对象
    public static final int vcShowWBObject = 0x220B;//显示(隐藏)一个对象
    public static final int vcClearWBScreen = 0x220C;//清除屏幕
    public static final int vcInsertWBPage = 0x220D;//插入一个页面
    public static final int vcDeleteWBPage = 0x220E;//删除一页面
    public static final int vcSelectWBPage = 0x220F;//选择一个页面
    public static final int vcSetWBAdmin = 0x2210;//设置白板的管理员角色
    public static final int vcCreateWBPageList = 0x2211;//创建
    public static final int vcPageListComplete = 0x2212;//页面创建完成
    public static final int vcPlayAudio = 0x2213;//播放声音
    public static final int vcPauseAudio = 0x2214;//暂停
    public static final int vcStopAudio = 0x2215;//停止播放声音
    public static final int vcCloseAudio = 0x2216;//打开声音媒体
    public static final int vcAudioEvent = 0x2217;//声音媒体媒体事件
    public static final int vcTransparentImg = 0x2218;//使用图片透明
    public static final int vcAskControl = 0x2219;//请求控制权
    public static final int vcSortPage = 0x221A;//页面排序
    public static final int vcScrollBoard = 0x221B;//白板的滚动命令
    public static final int vcNewBoard = 0x221C;//新建白板文件
    public static final int vcStartImportPack = 0x221D;//开始导入文档
    public static final int vcEndImportPack = 0x221E;//完成导入文档
    public static final int vcRestoreObject = 0x221F;//恢复对象
    public static final int vcAppendMark = 0x2220;//添加书签
    public static final int vcDeleteMark = 0x2221;//删除书签
    public static final int vcGotoMark = 0x2222;//执行书签链接
    public static final int vcAppendWBMark = 0x2223;//添加一个白板的标签对象
    public static final int vcLockBoard = 0x2224;//锁住白板
    public static final int vcShareUrlList = 0x2225;//协同浏览URL列表
    public static final int vcBoardSize = 0x2226;//更改白板尺寸
    public static final int vcSelectWBObject = 0x2227;
    public static final int vcPageBackColor = 0x2228;//更改页面颜色
    public static final int vcPageDirection = 0x2229;//更改页面的版式
    public static final int vcShareBrowser = 0x2230;//协同浏览
    public static final int vcExtendVideo = 0x2231;//扩展视频
    public static final int vcXmfEvent = 0x2232;//Xmf控件事件
    public static final int vcNewWBObject = 0x2233;//new object
    public static final int vcChangePen = 0x2234;//换笔
    public static final int vcLooseScroll = 0x2235;//失去滚屏权限
    public static final int vcHandline = 0x2236;//

    public static final int vcLockImg = 0x2237;//页面插入了文档转化的图片，需要将页面的
    ;//page->Direct_Lock = 1;  page->Direction 赋值

    public static final int vcClearLight = 0x2238;//清除某个用户的光标

    public static final int vcRemarkZy = 0x2239;//同步作业
    public static final int vcZySubmit = 0x223A;//提交作业，带上姓名

    public static final int vcRemarkPractice = 0x223B;//同步练习
    public static final int vcDownCloudFile = 0x223C;//下载云端的文件


    public static final int vcSetMtData = 0x2240;//设置会议属性
    public static final int vcGetMtData = 0x2241;//读取会议属性
    public static final int vcCfmMtData = 0x2242;//返回会议属性


    public static final int vcLoadCloudFile = 0x2244;//下载云文件
    public static final int vcClearOjbRect  = 0x2245;//清空当前页面的指定区域的所有对象


    public static final int vcPadLogin = 0x2250;//平板登录到教师机
    public static final int vcPadLogout = 0x2251;//平板退出教师机
    public static final int vcPadSeqCmd = 0x2252;//平板发送的白板命令
    public static final int vcPadRemark = 0x2253;//平板现场同步
    public static final int vcPadCfmLogin = 0x2254;//教师机返回给平板登录确认，返回信息包括meetingid
    public static final int vcPadOnline = 0x2255;//平板发送心跳包
    public static final int vcPadCloseRoom = 0x2265;//教师机返回给平板课堂关闭
    public static final int vcStartPadLoadPaper = 0x2256;//调用学生试卷
    public static final int vcDebugKey = 0x2257;//remote debug key

    public static final int vcFullBlackBoard = 0x2258;//切换黑板书写模式
    public static final int vcXorView = 0x2259;//切换黑板Xor模式

    public static final int vcShareBoardTab = 0x2260;//多白板页消息

    public static final int vcNewBoardSession = 0x2280;//添加新的白板会话
    public static final int vcCloseBoardSession = 0x2281;//结束已经存在的白板会话
    public static final  int vcLoadBoardFile=0x2282; //加载互动资料

    public static final int vcAppBarClear = 0x2290;//AppBar 透明窗体清空笔画的消息
    public static final int vcAppBarDeleteDraw = 0x2291;//AppBar 透明窗体删除一个笔画的消息
    public static final int vcAppBarAddDraw = 0x2292;//AppBar 透明窗体添加一个笔画的消息
    public static final int vcAppBarInitData = 0x2293;//MBC   通知AppBar自己的初始化信息

    public static final int vcAppBarShowBoard = 0x2294;//MBC 显示，接管AppBar的按钮消息
    public static final int vcAppBarBoardCmd = 0x2295;//AppBar 发送到MBC的按钮消息

    public static final  int vcUploadStart=0x5003;
    public static  final  int vcUploadData=0x5004;



}

