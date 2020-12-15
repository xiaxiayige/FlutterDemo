//Tcp 发送和接收服务
//心跳包定时发送
//重连
package com.example.flutter_app.meetingboard.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;


import com.example.flutter_app.meetingboard.BoardConstants;
import com.example.flutter_app.meetingboard.EventMsg;
import com.example.flutter_app.meetingboard.SeriallyPacket;
import com.example.flutter_app.meetingboard.TcpBuffer;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.flutter_app.meetingboard.BoardConstants.CONNECT_FAIL;
import static com.example.flutter_app.meetingboard.BoardConstants.CONNET_SUCCESS;
import static com.example.flutter_app.meetingboard.BoardConstants.Meeting_Msg;


/**
 * socket连接服务
 */
//会议中的tcp长连接
public class SocketService extends Service {

    /*socket*/
    private String TAG = "SocketService";
    private Socket socket;
    /*连接线程*/
    private Thread connectThread;
    private Thread sendThread;
    private Thread rcvThread;

    private OutputStream outputStream;
    private InputStream inputStream;

    private SocketBinder sockerBinder = new SocketBinder();
    private String ip;
    private String port;


    List<byte[]> SendPacketList = Collections.synchronizedList((new ArrayList<byte[]>()));


    /*默认重连*/
    private boolean isReConnect = true;
    private boolean bConnecting = false;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Override
    public IBinder onBind(Intent intent) {
        return sockerBinder;
    }


    public class SocketBinder extends Binder {

        /*返回SocketService 在需要的地方可以通过ServiceConnection获取到SocketService  */
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            /*拿到传递过来的ip和端口号*/
            ip = intent.getStringExtra(BoardConstants.INTENT_IP);
            port = intent.getStringExtra(BoardConstants.INTENT_PORT);
            /*初始化socket*/
            initSocket();
        }

        return super.onStartCommand(intent, flags, startId);
    }


    /*初始化socket*/
    private void initSocket() {
        // if (socket == null && connectThread == null)
        if (connectThread != null) connectThread = null;
        connectThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!bConnecting) {
                    try {
                        if (socket != null) socket = null;
                        Socket worksocket = new Socket();
                        /*超时时间为2秒*/
                        worksocket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 2000);
                        /*连接成功的话  发送心跳包*/
                        if (worksocket.isConnected()) {
                            Log.i("AAA", "SocketService ----> 连接成功, 发送心跳包");
                            bConnecting = true;
                            socket = worksocket;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (e instanceof SocketTimeoutException) {
                            //  toastMsg("连接超时，正在重连");
                            //      releaseSocket();
                            EventMsg msg = new EventMsg();
                            msg.Tag = CONNECT_FAIL;
                            EventBus.getDefault().post(msg);
                        } else if (e instanceof NoRouteToHostException) {
                            toastMsg("该地址不存在，请检查");
                            //  stopSelf();
                        } else if (e instanceof ConnectException) {
                            //  toastMsg("连接异常或被拒绝，请检查");
                            // stopSelf();
                            //    releaseSocket();
                            Log.i("AAA", "SocketService ----> 连接异常或被拒绝，请检查 ");
                            EventMsg msg = new EventMsg();
                            msg.Tag = CONNECT_FAIL;
                            EventBus.getDefault().post(msg);
                        }
                    }
                    SystemClock.sleep(1000);
                }

                /*发送连接成功的消息*/
                EventMsg msg = new EventMsg();
                msg.Tag = CONNET_SUCCESS;
                EventBus.getDefault().post(msg);
                Log.i("AAA", "SocketService ----> 发送连接成功的消息 ");

                /*发送心跳数据*/
                receiveData();
            }
        });
        /*启动连接线程*/
        connectThread.start();
        if (sendThread != null) sendThread = null;
        if (sendThread == null) sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if ((bConnecting) && (socket != null) && (socket.isConnected()) && SendPacketList.size() > 0) {
                        try {
                            if (SendPacketList.size() <= 0) return;
                            byte[] buf = SendPacketList.remove(0);
                            outputStream = socket.getOutputStream();
                            if (outputStream != null) {
                                Log.i("AAA", "发送完成");
                                outputStream.write((buf));
                                outputStream.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            bConnecting = false;
                        }
                    }

                }
            }
        });
        sendThread.start();

    }

    /*因为Toast是要运行在主线程的   所以需要到主线程哪里去显示toast*/
    private void toastMsg(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*发送数据*/
    public void sendOrder(final byte[] order) {
        //复制数据，加入到发送队列
        Log.i("AAA", "SocketService ----> 复制数据，加入到发送队列 ");

        byte[] newdata = order.clone();
        SendPacketList.add(order);
    }

    //接收数据
    private void receiveData() {
        //return;
        rcvThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TcpBuffer tcpBuffer = new TcpBuffer(4096 * 10);

                    byte[] recBuf = new byte[4096 * 10];
                    while (true) {
                        if (socket == null) break;
                        if (bConnecting == false) break;
                        inputStream = socket.getInputStream();
                        int irec = inputStream.read(recBuf);
                        tcpBuffer.AddBuf(recBuf, irec);
                        SeriallyPacket recPacket = null;
                        do {
                            recPacket = tcpBuffer.ReadPacket();
                            Log.d("AAA", "tcpBuffer.RcvLen = > " + tcpBuffer.RcvLen);
                            if (recPacket != null) {
                                EventMsg msg = new EventMsg();
                                msg.Tag = Meeting_Msg;
                                msg.packet = recPacket;
                                Log.i("AAA", "post Msg ===> " + "rec meeting_msg");
                                EventBus.getDefault().post(msg);
                            }
                        } while ((recPacket != null) && (socket != null));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    bConnecting = false;
                }
            }
        });
        rcvThread.start();
    }


    /*释放资源*/
    public void releaseSocket() {

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
            socket = null;
        }

        if (connectThread != null) {
            connectThread = null;
        }
        if (sendThread != null) {
            sendThread = null;
        }

        bConnecting = false;
        // initSocket();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SocketService", "onDestroy");
        isReConnect = false;
        releaseSocket();
    }
}
