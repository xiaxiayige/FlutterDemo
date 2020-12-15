package com.example.flutter_app.activity

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.flutter_app.AliRtcImpl
import com.example.flutter_app.R
import com.example.flutter_app.meetingboard.*
import com.example.flutter_app.meetingboard.BoardConstants.*
import com.example.flutter_app.meetingboard.BoardMessage.*
import io.flutter.embedding.android.FlutterActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.webrtc.sdk.SophonSurfaceView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AliRtcPlayerActivity : FlutterActivity() {
    private val PERMISSION_REQ_ID = 0x0002
    private var courseid: String? = null
    private var userId: Long? = null
    private var userName: String? = null
    private var coursename: String? = null

    /**
     * 本地流播放view
     */
    private var mLocalView: SophonSurfaceView? = null
    private var drawView: BoardView? = null
    private var appTmpPath: String? = null
    private val mAliRtc: AliRtcImpl by lazy { AliRtcImpl() }
    var boardnotify: NotifyMsg? = null
    private var playerView: PlaySurfaceView? = null

    private val REQUESTED_PERMISSIONS = arrayOf<String>(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alirtc_video_layout)
        //需要修改的参数值
        userId = 52969
        userName = "大狗狗"
        courseid = "20695"
        coursename = "11.25"
        EventBus.getDefault().register(this)
        initView()
        initData()
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            mAliRtc.preInit(this, courseid!!, coursename!!, userName!!, userId!!,
                    object : AliRtcImpl.IPreInitResultCallBack {
                        override fun preInitResult(initStatus: Int, errorMessage: String?) {
                            if (initStatus == AliRtcImpl.IPreInitResultCallBack.STATUS_SUCCESS) {
                                Log.i("AAA", "Activity ---> 预初始化完成")
                                initRTCEngineAndStartPreview()
                            } else {
                                Toast.makeText(this@AliRtcPlayerActivity, "$errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        }

                    })
        }

    }

    private var bFileDowning = false

    private fun AddMediaCmd(mediacmdpacket: SeriallyPacket) {
        val iEvent = mediacmdpacket.AsInt(0x03)
        if (iEvent == BoardConstants.MediaEvent.mtClose.ordinal) {
            CacheMediaCmdList.clear()
            return
        } else if (iEvent == BoardConstants.MediaEvent.mtSeek.ordinal || iEvent == BoardConstants.MediaEvent.mtContinue.ordinal || iEvent == BoardConstants.MediaEvent.mtPause.ordinal) {
            val iterator = CacheMediaCmdList.iterator()
            while (iterator.hasNext()) {
                val packet = iterator.next()
                val currevent = packet.AsInt(0x03)
                if (currevent == BoardConstants.MediaEvent.mtSeek.ordinal || currevent
                        == BoardConstants.MediaEvent.mtContinue.ordinal || currevent == BoardConstants.MediaEvent.mtPause.ordinal) {
                    iterator.remove()
                }
            }
        } else if (iEvent == BoardConstants.MediaEvent.mtPlay.ordinal) {
            CacheMediaCmdList.clear()
        }
        CacheMediaCmdList.add(mediacmdpacket)
    }

    private val CacheMsgList = ArrayList<SeriallyPacket>()

    private fun HandleCacheTcpMsg() {
        //执行缓存的tcp命令
        if (CacheMsgList.size == 0) return
        Log.d("AAABBB", "process cachemsglist")
        val iterator = CacheMsgList.iterator()
        var icounter = 0
        drawView!!.bLoadCacheMsg = true
        while (iterator.hasNext() && !bFileDowning) {
            icounter++
            Log.d("AAABBB", "icounter:$icounter")
            val cachepacket = iterator.next()
            iterator.remove()
            RunBoardSequenceMsg(cachepacket)
        }
        drawView!!.bLoadCacheMsg = false
        drawView!!.RefreshCurrentPage()
    }

    /***
     * 绘制白板
     */
    private fun ReceiveBoardSeqenceMsg(packet: SeriallyPacket) {

        val iMax = packet.AsInt(0x03)
        val iIndex = packet.AsInt(0x04)
        val subbuffer = packet.AsBytes(0x05)
        Log.i("AAA", "绘制白板 iMax = $iMax , iIndex = $iIndex subbuffer = $subbuffer")
        if (subbuffer == null || subbuffer.size == 0) return
        val boardCmdMsg = SeriallyPacket()
        boardCmdMsg.Assign(subbuffer, subbuffer.size)
        Log.d("AAA", "sequence:" + boardCmdMsg.Command().toString() + "\tindex:" + iIndex.toString() + "\tmax:" + iMax)
        if (bFileDowning) {
            if (boardCmdMsg.Command() === vcMediaEvent) AddMediaCmd(boardCmdMsg) else CacheMsgList.add(boardCmdMsg)
            return
        }
        if (drawView!!.bLoadCacheMsg === false) {
            RunBoardSequenceMsg(boardCmdMsg)
            return
        }
        if (boardCmdMsg.Command() === vcMediaEvent) AddMediaCmd(boardCmdMsg) else CacheMsgList.add(boardCmdMsg)
        if (iIndex == iMax) {
            HandleCacheTcpMsg() //处理缓存的白板命令消息
            HandleCacheMediaCmd()
        }
    }

    private fun HandleCacheMediaCmd() {
        try {
            if (CacheMediaCmdList.size == 0) return
            //确定播放现场，正常只有1个或者2个命令
            val MediaCmdpacket = CacheMediaCmdList[0]
            bStartProcessMediaCache = true
            HandleMediaEvent(MediaCmdpacket)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun RunBoardSequenceMsg(boardCmdMsg: SeriallyPacket) {
        if (boardCmdMsg.Command() === vcServerMtFile) //下载文件的消息
        {
//            HandleDownLoadBoardResMsg(boardCmdMsg)
        } else if (boardCmdMsg.Command() === vcShareBoardTab) //白板多文档相关命令
        {
//            HandleBoardTabMsg(boardCmdMsg)
        } else if (boardCmdMsg.Command() === vcSetMtLayout) {
//            val ipubchatflag = boardCmdMsg.AsInt(0x04)
//            if (ipubchatflag > 0) {
//                if (ipubchatflag == 1) {
//                    publicChatForbidden = true
//                } else {
//                    publicChatForbidden = false
//                }
//                SetChatInput()
//            }
        } else if (boardCmdMsg.Command() === vcSetForbiddenUser) {
//            val iExternUserId = boardCmdMsg.AsInt(0x03)
//            if (meetingMyself.ExternID !== iExternUserId) return  //不是禁言自己，不理会
//            val iprivatechatflag = boardCmdMsg.AsInt(0x04)
//            if (iprivatechatflag == 1) privateChatForbidden = true else privateChatForbidden = false
//            SetChatInput()
        } else if (boardCmdMsg.Command() === vcMediaEvent) {
            HandleMediaEvent(boardCmdMsg)
        } else if (drawView != null) //白板命令绘制
        {
            drawView!!.RunActionCommand(boardCmdMsg)
        }
    }

    private fun HandleMediaEvent(packet: SeriallyPacket) {
        val eventtype = packet.AsInt(0x03)
        if (eventtype == MediaEvent.mtPlay.ordinal) {
            Log.d("mediaevent", "mtPlay")
            val mediaUrl = packet.AsString(0x04)
            val boardtool = BoardUtils()
            val mediaext = boardtool.getExtensionName(mediaUrl).toLowerCase()
            playerView!!.stop()
            if (mediaext == ".mp3") {
                drawView!!.visibility = View.GONE
                playerView!!.visibility = View.GONE
            } else {
                drawView!!.visibility = View.GONE
                playerView!!.visibility = View.VISIBLE
            }
            //TODO 设置暖场图片
            playerView!!.url = mediaUrl
            playerView!!.play()
        } else if (eventtype == MediaEvent.mtPause.ordinal) {
            Log.d("mediaevent", "mtPause")
            if (drawView!!.bLoadCacheMsg) {
                playerView!!.stop()
                //       drawView.setVisibility(View.VISIBLE);
                //      playerView.setVisibility(View.GONE);
            } else {
                val iPause = packet.AsInt(0x07)
                if (iPause == 0) {
                    playerView!!.pause()
                } else {
                    playerView!!.continueplay()
                }
            }
        } else if (eventtype == MediaEvent.mtClose.ordinal) {
            Log.d("mediaevent", "mtClose")
            run {
                playerView!!.stop()
                drawView!!.visibility = View.VISIBLE
                playerView!!.visibility = View.GONE
            }
        } else if (eventtype == MediaEvent.mtSeek.ordinal) {
            Log.d("mediaevent", "mtSeek")
            val playPosition = packet.AsInt(0x05) * 1000
            playerView!!.seekTo(playPosition)
            val ipause = packet.AsInt(0x07)
            if (ipause == 1) playerView!!.continueplay()
        }
    }

    private val boardList: HashMap<Int, BoardFile> = HashMap()
    private var boardLayout: BoardLayout? = null
    private var bStageReady = false

    private var currVotePaper: VotePaper? = null

    private fun initData() {
        appTmpPath = "$cacheDir/AppTmpPath"
        val path = File(appTmpPath)
        if (path.exists() && path.isDirectory) {
            BoardUtils.deleteDir(appTmpPath)
        }
        path.mkdir()
        drawView?.SetRootTmpPath(appTmpPath)
        drawView?.ResetBoard()
        drawView?.NewFile()
        //初始为拖动状态
        //初始为拖动状态
        drawView?.PrepareDraw(BoardConstants.WBShapeType.wbstDrag)
        //HandleThumbView();
        LoadMessageHandler(false)
        //初始的空白白板
        //初始的空白白板
        val boardFile = BoardFile()
        boardFile.Title = ""
        boardFile.Downloaded = true
        boardList[0] = boardFile
//        HandleMsgView() todo
        boardLayout = BoardLayout()
        bStageReady = false
        currVotePaper = VotePaper()
    }

    fun SendSeqCmd(packet: SeriallyPacket?) {
        val bigpacket = SeriallyPacket()
        bigpacket.SetCommand(vcAddSequenceAct)
        bigpacket.InsertData(0x01, mAliRtc.meetingMyself!!.UserID)
        bigpacket.InsertData(0x02, mAliRtc.mtOption!!.MeetingID)
        bigpacket.InsertSubData(0x05, packet)
        mAliRtc.sendTcpPacket(bigpacket)
    }

    fun LoadMessageHandler(bLoad: Boolean) {
        if (drawView == null) return
        if (bLoad == false) {
            drawView!!.notifyMsg = null
        } else {
            drawView!!.notifyMsg = object : NotifyMsg {
                override fun onAddObject(ObjId: Int) {
                    val Obj = drawView!!.getObj(ObjId) ?: return
                    if (!mAliRtc.isConnectMeet()) return
                    val packet = SeriallyPacket()
                    packet.SetCommand(vcAppendWBObject)
                    packet.InsertData(0x01, mAliRtc.meetingMyself!!.UserID)
                    packet.InsertDataU(0x03, Obj.ObjectID)
                    packet.InsertData(0x04, Obj.MainType.ordinal)
                    val buf = ByteArray(2048)
                    if (Obj.MainType === BoardConstants.WBMainType.wbmtShape) {
                        val shapeObj = Obj as CWBShape
                        val ilen = shapeObj.WriteToBuffer(buf)
                        if (ilen > 0) {
                            packet.InsertData(0x05, buf, ilen.toInt())
                            SendSeqCmd(packet)
                        }
                    } else if (Obj.MainType === BoardConstants.WBMainType.wbmtImage) {
                        val boardtool = BoardUtils()
                        val imageObj = Obj as CWBImage
                        var sExt = imageObj.GetFileName()
                        sExt = boardtool.getExtensionName(sExt)
                        //uploadfile
                        val sLocalfile = drawView!!.tmpPath.toString() + "/" + imageObj.ObjectID + "_" + drawView!!.BoardId + sExt
//                            UploadFile(sLocalfile)
                        val ilen = imageObj.WriteToBuffer(buf)
                        if (ilen > 0) {
                            packet.InsertData(0x05, buf, ilen.toInt())
                            packet.InsertData(0x06, sExt)
                            SendSeqCmd(packet)
                        }
                    }
                }

                override fun onDelObject(objId: Int) {
                    if (!mAliRtc.isConnectMeet()) return
                    val packet = SeriallyPacket()
                    packet.SetCommand(vcDeleteWBObject)
                    packet.InsertData(0x03, objId)
                    SendSeqCmd(packet)
                }

                override fun onAddPage(iPos: Int, bMode: Boolean) {
                    //修改缩略图
                    //     thumbAdapter.SetClickItem(iPos);
                    //    thumbAdapter.notifyDataSetChanged();
                    //发送消息
                    val pageid = drawView!!.GetPageID(iPos)
                    val page = drawView!!.GetPage(pageid.toInt())
                    if (!mAliRtc.isConnectMeet()) return
                    if (bMode) {
                        val packet = SeriallyPacket()
                        packet.SetCommand(vcInsertWBPage)
                        packet.InsertData(0x01, mAliRtc.meetingMyself!!.UserID)
                        packet.InsertData(0x03, pageid)
                        packet.InsertData(0x04, iPos)
                        packet.InsertData(0x05, page.Width)
                        packet.InsertData(0x06, page.Height)
                        packet.InsertData(0x08, drawView!!.CurrentPage.PageBackColor)
                        SendSeqCmd(packet)
                    }
                }

                override fun onDelPage(iPos: Int, bMode: Boolean) {
                    val pageid = drawView!!.GetPageID(iPos)
                    val page = drawView!!.GetPage(pageid)
                    if (!mAliRtc.isConnectMeet()) return
                    if (bMode) {
                        val packet = SeriallyPacket()
                        packet.SetCommand(vcDeleteWBPage)
                        packet.InsertData(0x01, mAliRtc.meetingMyself!!.UserID)
                        packet.InsertData(0x03, pageid)
                        packet.InsertData(0x04, iPos)
                        SendSeqCmd(packet)
                    }
                }

                override fun onSelectPage(iPos: Int, bMode: Boolean) {

                    //    thumbAdapter.SetClickItem(iPos);
                    //    thumbAdapter.notifyDataSetChanged();
                    val pageid = drawView!!.GetPageID(iPos)
                    val page = drawView!!.GetPage(pageid)
                    if (!mAliRtc.isConnectMeet()) return
                    if (bMode) {
                        val packet = SeriallyPacket()
                        packet.SetCommand(vcSelectWBPage)
                        packet.InsertData(0x01, mAliRtc.meetingMyself!!.UserID)
                        packet.InsertData(0x03, pageid)
                        packet.InsertData(0x04, iPos)
                        SendSeqCmd(packet)
                    }
                }

                override fun onClearScreen(iPos: Int, bMode: Boolean) {
                    val pageid = drawView!!.GetPageID(iPos)
                    val page = drawView!!.GetPage(pageid)
                    if (!mAliRtc.isConnectMeet()) return
                    if (bMode) {
                        val packet = SeriallyPacket()
                        packet.SetCommand(vcClearWBScreen)
                        packet.InsertData(0x01, mAliRtc.meetingMyself!!.UserID)
                        packet.InsertData(0x03, pageid)
                        packet.InsertData(0x04, drawView!!.GetUser())
                        SendSeqCmd(packet)
                    }
                }

                override fun onAdjustOver() {
                    Toast.makeText(applicationContext, "校正完成", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

private var bClassroomConnecting = false

/***
 * 从SocketService中接受消息
 */
@Subscribe(threadMode = ThreadMode.MAIN)
fun onEventMainThread(msg: EventMsg) {
    Log.i("AAA", "EventMsg =》》》 $msg")
    if (msg.Tag.equals(CONNET_SUCCESS)) {
        /*接收到这个消息说明连接成功*/
        Log.d("AAA", "\tCONNET_SUCCESS")
        bClassroomConnecting = true
    } else if (msg.Tag.equals(RECTCPPACKET)) {
        Log.d("AAA", "\tRECTCPPACKET")
        Toast.makeText(this, "收到数据", Toast.LENGTH_SHORT).show();
    } else if (msg.Tag.equals(CONNECT_FAIL)) { //连接失败
        Log.d("AAA", "\tCONNECT_FAIL")
        bClassroomConnecting = false
    } else if (msg.Tag.equals(Meeting_Msg)) { //
        HandleMeetingTcpMsg(msg.packet)
    }
}

private fun PrepareJointMt(pack: SeriallyPacket) {
    val pb = pack.AsBytes(0x03)
    mAliRtc.mtOption?.ParseFromBytes(pb, pb.size)
    val serverpacket = SeriallyPacket()
    serverpacket.Assign(pb, pb.size)
    val serverinfo = serverpacket.AsBytes(0x81)
    mAliRtc.mtOption?.MtServer?.ParseFromBytes(serverinfo, serverinfo.size)
    val packet = SeriallyPacket()
    packet.SetCommand(vcJoinMeeting)
    packet.InsertData(0x02, mAliRtc.mtOption!!.MeetingID)
    packet.InsertData(0x03, mAliRtc.mtOption!!.Password)
    packet.InsertData(0x04, 1) //audio
    packet.InsertData(0x05, mAliRtc.meetingMyself!!.Nick)
    packet.InsertData(0x06, 1) //video
    if (mAliRtc.mtOption!!.UserGuid.length > 0) packet.InsertData(0x07, mAliRtc.mtOption!!.UserGuid)
    packet.InsertData(0x08, mAliRtc.meetingMyself!!.ExternID)
    packet.InsertData(0x09, mAliRtc.mtOption!!.MtServer.ServerID)
    packet.InsertData(0x0C, 1) //android hd
    packet.InsertData(0x0B, 100) //version
    mAliRtc.sendTcpPacket(packet)
}

private fun VerifyJoinMt(packet: SeriallyPacket?) {
    Log.i("AAA", "VerifyJoinMt: " + (packet == null))
    if (packet == null) return
    var iv = packet.AsInt(0x02)
    if (iv != mAliRtc.mtOption!!.MeetingID.toInt()) return
    iv = packet.AsInt(0xFC)
    if (iv != 0) {
        val sMsg = "登录失败：" + packet.AsString(0xFB)
        Log.i("AAA", "VerifyJoinMt: 登录失败")
    } else {
        val sMsg = "登录成功：" + packet.AsString(0xFB)
        Log.i("AAA", "VerifyJoinMt: 登录成功")
        //       Toast.makeText(this, sMsg, Toast.LENGTH_SHORT).show();
    }

    val mtoptionpb = packet.AsBytes(0x03)
    mAliRtc.mtOption?.ParseFromBytes(mtoptionpb, mtoptionpb.size)
    //TextView editText = (TextView) findViewById(R.id.txtMeetingTitle);
    //editText.setText(mtOption.Name);
    val userpb = packet.AsBytes(0x04)
    mAliRtc.meetingMyself?.ParseFromBytes(userpb, userpb.size)
    //开启定时器，发送心跳
    Log.i("AAA", "VerifyJoinMt: 开启定时器，发送心跳")
    mAliRtc.sendBeatData()
    //发送准备完成的消息
    val mtStatuPacket = SeriallyPacket()
    mtStatuPacket.SetCommand(vcSetMtStatu)
    mtStatuPacket.InsertData(0x01, mAliRtc.meetingMyself!!.UserID)
    mtStatuPacket.InsertData(0x02, mAliRtc.mtOption!!.MeetingID)
    mtStatuPacket.InsertData(0x03, MeetingStatu.msStand.ordinal)
    Log.i("AAA", "VerifyJoinMt: 发送准备完成的消息")
    mAliRtc.sendTcpPacket(mtStatuPacket)
    mAliRtc.meetingMyself!!.Statu = MeetingStatu.msStand.ordinal

    //发送获取课堂成员列表请求
    val getMemberListpacket = SeriallyPacket()
    getMemberListpacket.SetCommand(vcGetMemberList)
    getMemberListpacket.InsertData(0x01, mAliRtc.meetingMyself!!.UserID)
    getMemberListpacket.InsertData(0x02, mAliRtc.mtOption!!.MeetingID)
    mAliRtc.sendTcpPacket(getMemberListpacket)
}

/***
 * 处理消息
 */
fun HandleMeetingTcpMsg(packet: SeriallyPacket) {
    Log.d("AAA", "处理消息 HandleMeetingTcpMsg")
    val iCmd = packet.Command()
    when (iCmd) {
        vcCfmMeetingInfo -> { //获取会议信息的回执
            Log.d("AAA", "处理消息---- vcCfmMeetingInfo  获取会议信息的回执")
            PrepareJointMt(packet)
        }
        vcCfmJoinMeeting -> {
            Log.d("AAA", "vcCfmJoinMeeting")
            VerifyJoinMt(packet)
        }
        vcSetSpeaker -> {
            Log.d("AAA", "设置发言人")
//                HandleSetSpeakMsg(packet)
        }
        vcSetManager -> {
            Log.d("AAA", "设置会议的管理者")
//                HandleSetManager(packet)
        }
        vcKickMember -> {
            Log.d("AAA", "踢出参会成员")
        }
        vcEndMeeting -> {
            Log.d("AAA", "结束会议")
//                Toast.makeText(this, "退出课堂", Toast.LENGTH_SHORT).show()
//                QuitMeeting()
        }
        vcMeetingCommand -> {
            Log.d("AAA", "会议命令　Member To Server")
        }
        vcCfmMemberList -> {
            Log.d("AAA", "会议成员列表")
            LoadMessageHandler(true)
        }
        vcJoinMeeting -> {
            Log.d("AAA", "加入会议")
        }
        vcQuitMeeting -> {
            Log.d("AAA", "退出会议")
        }
        vcVotePaper -> {
            Log.d("AAA", "发起问卷")
        }
        vcShowStatis -> {
            Log.d("AAA", "问卷回复")
//                HandleShowStatisMsg(packet)
        }
        vcResetBoard -> {
            Log.d("AAA", "会议室白板清场")
            val path = File(appTmpPath)
            if (path.exists() && path.isDirectory) {
                BoardUtils.deleteDir(appTmpPath)
            }
            path.mkdir()
            boardList.clear()
            //初始的空白白板
            val boardFile = BoardFile()
            boardFile.Title = ""
            boardFile.Downloaded = true
            boardList[0] = boardFile
            drawView!!.NewFile()
            drawView!!.BoardId = 0
        }
        vcMtPrivateChat -> { //私聊
//                ReceivePrivateMsg(packet)
        }
        vcAddSequenceAct -> { //添加白板序列命令
            Log.d("AAA", "添加白板序列命令")
            ReceiveBoardSeqenceMsg(packet)
        }
        else -> {

        }
    }
}


private fun initView() {
    mLocalView = findViewById(R.id.sf_local_view)
    drawView = findViewById(R.id.drawView)
    playerView = findViewById(R.id.playerview)
    SetPlayerViewListener()
}

private var bStartProcessMediaCache = false
private fun SetPlayerViewListener() {
    playerView!!.setOnVideoPlayingListener(object : PlaySurfaceView.OnVideoPlayingListener {
        override fun onVideoSizeChanged(vWidth: Int, vHeight: Int) {}
        override fun onPlaying(duration: Int, percent: Int) {}
        override fun onStart() {
            //已经准备好了
            if (bStartProcessMediaCache) {
                bStartProcessMediaCache = false
                HandleMediaStage()
            }
        }

        override fun onPlayOver() {}
        override fun onVideoSize(videoSize: Int) {}
    })
}

private val CacheMediaCmdList = ArrayList<SeriallyPacket>()
private fun HandleMediaStage() {
    try {
        bStartProcessMediaCache = false
        val iduration = playerView!!.mediaDuration
        var MediaCmdpacket: SeriallyPacket = CacheMediaCmdList.get(0)
        if (CacheMediaCmdList.size == 1) {
            val ilen = 0
            val sPlayTick = MediaCmdpacket.AsString(0x06)
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val playTick = format.parse(sPlayTick)
            val nowTick = Date()
            val ipos = (nowTick.time - playTick.time).toInt() //换算成秒为单位
            if (ipos < iduration) {
                playerView!!.seekTo(ipos)
                playerView!!.continueplay()
            } else playerView!!.stop()
        } else {
            MediaCmdpacket = CacheMediaCmdList.get(1)
            val ievent = MediaCmdpacket.AsInt(0x03)
            val ipos = MediaCmdpacket.AsInt(0x05) * 1000 //转为毫秒
            val sPlayTick = MediaCmdpacket.AsString(0x06)
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val playTick = format.parse(sPlayTick)
            val nowTick = Date()
            val iContinue = (nowTick.time - playTick.time).toInt()
            if (ievent == BoardConstants.MediaEvent.mtPause.ordinal) {
                val ipause = MediaCmdpacket.AsInt(0x07)
                if (ipause == 0) {
                    playerView!!.seekTo(ipos)
                    playerView!!.pause()
                } else if (ipause == 1) {
                    if (iContinue + ipos > iduration) playerView!!.stop() else {
                        playerView!!.seekTo(ipos + iContinue)
                        playerView!!.continueplay()
                    }
                }
            } else if (ievent == BoardConstants.MediaEvent.mtSeek.ordinal) {
                val ipause = MediaCmdpacket.AsInt(0x07)
                if (ipause == 0) {
                    playerView!!.seekTo(ipos)
                    playerView!!.pause()
                } else {
                    playerView!!.seekTo(ipos + iContinue)
                    playerView!!.continueplay()
                }
            }
        }
        CacheMediaCmdList.clear()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun initRTCEngineAndStartPreview() {
    Log.i("AAA", "Activity ---------------------> 初始化设置SDK相关")
    mAliRtc.initSdk(application)
    initLocalView()
    mAliRtc.startPreview()
    mAliRtc.openJoinChannelBeforeNeedParams()
    mAliRtc.joinChannel()
    Log.i("AAA", "Activity --------------------->  SDK 设置完成")
}

/**
 * 设置本地的预览视图的view
 */
private fun initLocalView() {
    mLocalView?.run {
        getHolder()?.setFormat(PixelFormat.TRANSLUCENT)
        setZOrderOnTop(false)
        setZOrderMediaOverlay(false)
        mAliRtc.readyAliVideoCanvas(this)
    }

}

private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode)
        return false
    }
    return true
}


override fun onRequestPermissionsResult(requestCode: Int,
                                        permissions: Array<String?>, grantResults: IntArray) {
    if (requestCode == PERMISSION_REQ_ID) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
            showToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                    "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE)
            finish()
            return
        } else {
//                initRtcLoginInfo()
//                mAliRtc.preInit(this,courseid,courn)
        }
    }
}

private fun showToast(msg: String) {
    runOnUiThread { Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show() }
}

override fun onDestroy() {
    EventBus.getDefault().unregister(this)
    super.onDestroy()
    mAliRtc.destory()
}

}