package com.example.flutter_app

import android.app.ActivityManager
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.alivc.rtc.*
import com.example.flutter_app.alirtc.util.AliRtcWebUtils
import com.example.flutter_app.alirtc.util.ParserJsonUtils
import com.example.flutter_app.alirtc.util.ThreadUtils
import com.example.flutter_app.bean.RTCAuthInfo
import com.example.flutter_app.meetingboard.*
import com.example.flutter_app.meetingboard.BoardConstants.INTENT_IP
import com.example.flutter_app.meetingboard.BoardConstants.INTENT_PORT
import com.example.flutter_app.meetingboard.BoardMessage.vcCfmMeetingOnline
import com.example.flutter_app.meetingboard.service.SocketService
import org.json.JSONException
import org.json.JSONObject
import org.webrtc.sdk.SophonSurfaceView
import java.util.*

/***
 * alirtc
 */
class AliRtcImpl : IAliRtc {
    val SERVERINFO_URL = "http://soft1.guanxinqiao.com:7789/CallGXQMeeting/GetMeetingLocateInfo"
    val RTCSERVICE_URL = "http://soft1.guanxinqiao.com:8091/app/v1/login"

    private val TAG = "AliRtcImpl"
    var mtOption: MeetingOption? = null
    var meetingMyself: MeetingMember? = null
    private val mEngine: AliRtcEngineImpl by lazy { AliRtcEngineImpl(App.getInstance()) }
    val mAliRtcEngineNotify = object : AliRtcEngineNotify() {}
    private var mContext: Context? = null
    val mAliRtcEngineEventListener = object : AliRtcEngineEventListener() {
        override fun onJoinChannelResult(result: Int) {
            super.onJoinChannelResult(result)
            if (result == 0) {
                Log.i(TAG, "加入频道成功")
            }
        }

        /***
         * uid	String	用户ID
        result	int	0表示订阅成功，非0表示失败
        vt	AliRtcVideoTrack	订阅成功的视频流
        at	AliRtcAudioTrack	订阅成功的音频流
         */
        override fun onSubscribeResult(uid: String?, result: Int, vt: AliRtcEngine.AliRtcVideoTrack?, at: AliRtcEngine.AliRtcAudioTrack?) {
            super.onSubscribeResult(uid, result, vt, at)
            if (result == 0) {
                Log.i(TAG, "订阅成功 uid = $uid")
            }
        }

        override fun onUnsubscribeResult(result: Int, userId: String?) {
            super.onUnsubscribeResult(result, userId)
            if (result == 0) {
                Log.i(TAG, "取消订阅成功 uid = $userId")
            }
        }

        override fun onOccurError(error: Int) {
            super.onOccurError(error)
            Log.i(TAG, "onOccurError: $error")
        }

    }
    private var sc: ServiceConnection? = null
    private var socketService: SocketService? = null
    private var isConnectMeeting = false
    private var preInitCallback: IPreInitResultCallBack? = null
    private var rtcAuthInfo: RTCAuthInfo? = null

    fun isConnectMeet() = isConnectMeeting

    /**
     * 初始化aliRtcaSdk
     */
    override fun initSdk(application: Application) {
        //默认不开启兼容H5
//        AliRtcEngine.setH5CompatibleMode(0)
        mEngine.setRtcEngineEventListener(mAliRtcEngineEventListener)
        mEngine.setRtcEngineNotify(mAliRtcEngineNotify)
    }

    /**
     * 预初始化，准备所需要的数据，开启本地 service
     *
     */
    fun preInit(context: Context, courseId: String, courseName: String, userName: String, userId: Long, iPreInitResultCallBack: IPreInitResultCallBack) {
        mContext = context
        meetingMyself = MeetingMember().apply {
            Nick = userName
            ExternID = userId
        }
        mtOption = MeetingOption().apply {
            UserGuid = userId.toString()
        }
        preInitCallback = iPreInitResultCallBack
        getServerInfo(courseId, courseName)
    }


    private fun getServerInfo(courseid: String, coursename: String) {
        Log.i("AAA", "AliRtcImpl 开始获取服务信息")
        val apiurl: String = SERVERINFO_URL
        val sJsonBody = "{\"CourseId\":\"$courseid\",\"CourseName\":\"$coursename\"}"
        AliRtcWebUtils.getInstance().doPostStringResponse(apiurl, sJsonBody, object : AliRtcWebUtils.HttpCallBack {
            override fun onError(error: String?) {
                Log.i("AAA", "AliRtcImpl 获取服务信息失败")
//                runOnUiThread { Toast.makeText(applicationContext, "调用服务器新失败", Toast.LENGTH_SHORT).show() }
                preInitCallback?.preInitResult(IPreInitResultCallBack.STATUS_ERROR, "调用服务器新失败")

            }

            override fun onSuccess(result: String?) {

                try {
                    val jsonObject = JSONObject(result)
                    val iMeetingId = jsonObject.getInt("MeetingId")
                    val iPort = jsonObject.getInt("ServerPort")
                    val sServerIp = jsonObject.getString("ServerIp")
                    val sAddress = "$sServerIp:$iPort"
                    //make key
                    var sKey: String = BoardConstants.md5PrivateKey
                    sKey = BoardUtils.md5(sKey).toUpperCase()

                    mtOption?.MeetingID = iMeetingId.toLong()
                    mtOption?.SetServer(sAddress)
                    mtOption?.Password = sKey
                    Log.i("AAA", "AliRtcImpl 获取服务信息成功,准备开启本地服务(SocketService)")
                    startLocalService()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    /***
     * 开启本地服务
     */
    private fun startLocalService() {
        if (isServiceRunning("com.example.flutter_app.meetingboard.service.SocketService") == false) {
            /*启动service*/
            val meetingAddressIp: String = BoardUtils.intToIp(mtOption!!.MtServer.IP)
            val meetingAddressport = java.lang.String.valueOf(mtOption!!.MtServer.Port)
            val intent = Intent(mContext, SocketService::class.java)
            intent.putExtra(INTENT_IP, meetingAddressIp)
            intent.putExtra(INTENT_PORT, meetingAddressport)
            intent.action = "android.intent.action.RESPOND_VIA_MESSAGE"
            /*通过binder拿到service*/
            sc = object : ServiceConnection {
                override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                    val binder: SocketService.SocketBinder = iBinder as SocketService.SocketBinder
                    Log.i("AAA", "AliRtcImpl 启动服务完成。。。。准备初始化登录信息")
                    socketService = binder.getService()
                    isConnectMeeting = true
                    //启动完成，连接videoChannel
                    initRtcLoginInfo()
                    sendJoinMtRequire()
                }

                override fun onServiceDisconnected(componentName: ComponentName) {
                    isConnectMeeting = false
                }
            }
            mContext?.startService(intent)
            mContext?.bindService(intent, sc, BIND_AUTO_CREATE)
        }
    }


    private fun sendJoinMtRequire() {
        val packet = SeriallyPacket()
        //读取课堂详细信息
        packet.SetCommand(BoardMessage.vcGetMeetingInfo)
        packet.InsertData(0x01, meetingMyself!!.ExternID)
        packet.InsertData(0x02, mtOption!!.MeetingID)
        packet.InsertData(0xFE, BoardConstants.SocketSource.ssJoinMeeting.ordinal)
        Log.i("AAA", "AliRtcImpl  读取课堂(会议)详细信息 ， sendJoinMtRequire")
        sendTcpPacket(packet)
    }


    fun sendTcpPacket(packet: SeriallyPacket) {
        Log.i("AAA", "AliRtcImpl 发送数据包")
        try {
            if (isConnectMeeting == false) return
            val databuf = ByteArray(packet.GetLen() + 4)
            val boardByteBuf = BoardByteBuf()
            boardByteBuf.AssignWriteBuf(databuf)
            boardByteBuf.WriteInt(packet.GetLen())
            boardByteBuf.WriteByteArr(packet.Data())
            if (socketService != null)
                socketService?.sendOrder(databuf)
        } catch (e: java.lang.Exception) {
            Log.i("AAA", "AliRtcImpl 发送数据包 异常")
            e.printStackTrace()
        }
    }

    private fun initRtcLoginInfo() {
        Log.i("AAA", "AliRtcImpl 开始初始化登录信息。。。。")
//        mRtcChannelId = "9679" //房间号
//        mRtcUserName = "1" //用户
        val hashMap = HashMap<String, String>()
        hashMap["user"] = meetingMyself?.ExternID.toString()
        hashMap["room"] = mtOption?.MeetingID.toString()
        hashMap["passwd"] = "12345678"
        val base: String = RTCSERVICE_URL //AliRtcConstants.GSLB_TEST;
        AliRtcWebUtils.getInstance().doGet(base, hashMap, object : AliRtcWebUtils.HttpCallBack {
            override fun onError(error: String?) {
                ThreadUtils.runOnUiThread(Runnable { /*showProgressDialog(false)*/ })
                Log.i("AAA", "AliRtcImpl 初始化登录信息。。。。失败")
            }

            override fun onSuccess(result: String?) {
                Log.i("AAA", "AliRtcImpl 初始化登录信息。。。。成功")
                rtcAuthInfo = ParserJsonUtils.parserLoginJson(result)
                if (rtcAuthInfo != null) {
//                    initRTCEngineAndStartPreview(rtcAuthInfo)
                    preInitCallback?.preInitResult(IPreInitResultCallBack.STATUS_SUCCESS)
                }
            }
        })
    }


    /**
     * 判断服务是否运行
     */
    private fun isServiceRunning(className: String): Boolean {
        val activityManager = mContext?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = activityManager.getRunningServices(Int.MAX_VALUE)
        if (info == null || info.size == 0) return false
        for (aInfo in info) {
            if (className == aInfo.service.className) return true
        }
        return false
    }


    /***
     * 销毁
     */
    fun destory() {
        mEngine.destroy()
    }

    fun readyAliVideoCanvas(view: SophonSurfaceView) {
        val aliVideoCanvas = AliRtcEngine.AliVideoCanvas()
        aliVideoCanvas.view = view
        aliVideoCanvas.renderMode = AliRtcEngine.AliRtcRenderMode.AliRtcRenderModeAuto
        if (mEngine != null) {
            mEngine.setLocalViewConfig(aliVideoCanvas, AliRtcEngine.AliRtcVideoTrack.AliRtcVideoTrackCamera)
        }
    }

    fun openJoinChannelBeforeNeedParams() {
        Log.i("AAA", "AliRtcImpl SDK openJoinChannelBeforeNeedParams ")
        mEngine.startAudioCapture()
        mEngine.startAudioPlayer()
    }

    fun joinChannel() {
        Log.i("AAA", "AliRtcImpl SDK 连接Channel ")
        if (mEngine == null) {
            return
        }
        val userInfo: AliRtcAuthInfo = AliRtcAuthInfo().apply {
            appid = rtcAuthInfo?.data?.appid
            nonce = rtcAuthInfo?.data?.nonce
            timestamp = rtcAuthInfo?.data?.timestamp ?: 0
            userId = rtcAuthInfo?.data?.userid
            gslb = rtcAuthInfo?.data?.gslb
            token = rtcAuthInfo?.data?.token
            conferenceId = mtOption?.MeetingID.toString()
        }
        /*
         *设置自动发布和订阅，只能在joinChannel之前设置
         *参数1    true表示自动发布；false表示手动发布
         *参数2    true表示自动订阅；false表示手动订阅
         */
        mEngine.setAutoPublishSubscribe(false, true)
        // 加入频道，参数1:鉴权信息 参数2:用户名
        mEngine.joinChannel(userInfo, meetingMyself?.ExternID.toString())
    }


    fun startPreview() {
        Log.i("AAA", "AliRtcImpl SDK startPreview ")
        try {
            mEngine.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var timer: Timer? = null
    private var onlineTask: TimerTask? = null

    fun sendBeatData() {
        if (timer == null) {
            timer = Timer()
        }
        if (onlineTask == null) {
            onlineTask = object : TimerTask() {
                override fun run() {
                    try {
                        val onlinepacket = SeriallyPacket()
                        onlinepacket.SetCommand(vcCfmMeetingOnline)
                        onlinepacket.InsertData(0x01, meetingMyself!!.UserID)
                        onlinepacket.InsertData(0x02, mtOption!!.MeetingID)
                        sendTcpPacket(onlinepacket)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        timer?.schedule(onlineTask, 0, 5000)
    }



    /**
     * 预初始化准备结果
     */
    interface IPreInitResultCallBack {
        companion object {
            val STATUS_SUCCESS = 0
            val STATUS_ERROR = -1
        }

        fun preInitResult(initStatus: Int, errorMessage: String? = "")
    }




}