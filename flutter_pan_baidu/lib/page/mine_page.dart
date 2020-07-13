import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutterpanbaidu/bean/bean.dart';
import 'package:flutterpanbaidu/generated/r.dart';
import 'package:flutterpanbaidu/tools/common_widget.dart';

/****
 * 我的
 */
class MinePage extends StatefulWidget {

  @override
  _MinePageState createState() => _MinePageState();
}

class _MinePageState extends State<MinePage> with SingleTickerProviderStateMixin {
  List<CardInfo> toolscards=[
    CardInfo(R.img_service_album_icon,"相册备份",""),
    CardInfo(R.img_service_album_icon,"回收站",""),
    CardInfo(R.img_service_album_icon,"我的会员",""),
    CardInfo(R.img_service_album_icon,"活动中心",""),
    CardInfo(R.img_service_album_icon,"小程序",""),
    CardInfo(R.img_service_album_icon,"我的分享",""),
    CardInfo(R.img_service_album_icon,"只能设备",""),
    CardInfo(R.img_service_album_icon,"文件清理",""),
  ];

  List<CardInfo> moreService=[
    CardInfo(R.img_service_album_icon,"免费问医生",""),
    CardInfo(R.img_service_album_icon,"借现金",""),
    CardInfo(R.img_service_album_icon,"冲印商城",""),
    CardInfo(R.img_service_album_icon,"搜一搜",""),
    CardInfo(R.img_service_album_icon,"免流量卡",""),
    CardInfo(R.img_service_album_icon,"领无限空间",""),
  ];

  double progress=0;

  Animation<double>  tween ;
  AnimationController _animationController;
  
  @override
  void initState() {
    super.initState();
    _animationController=AnimationController(duration: Duration(milliseconds: 1000),vsync: this);
    tween = Tween<double>(begin: 0,end: 350).animate(_animationController);
    _animationController.repeat();
  }

  @override
  void dispose() {
    // TODO: implement dispose
    _animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: EdgeInsets.only(bottom: 8),
      child: Column(
        children: <Widget>[
          Container(
            height: 125,
            color: Colors.white,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: <Widget>[
                CircleAvatar(
                  child: Image.asset(
                    R.img_mine_mine_head,
                    width: 80,
                    height: 80,
                  ),
                ),
                Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    Row(
                      children: <Widget>[
                        Text(
                          "哇—不公平嘎",
                          style: TextStyle(
                              fontSize: 20,
                              color: Colors.black,
                              fontWeight: FontWeight.bold),
                        ),
                        SizedBox(
                          width: 4,
                        ),
                        Image.asset(
                          R.img_mine_home_identity_common,
                          width: 18,
                          height: 16,
                        ),
                      ],
                    ),
                    SizedBox(
                      height: 8,
                    ),
                    Container(
                      width: 180,
                      height: 3,
                      child: LinearProgressIndicator(
                        backgroundColor: Colors.grey.withAlpha(50),
                        value: 0.2,
                      ),
                    ),
                    SizedBox(
                      height: 8,
                    ),
                    Row(
                      children: <Widget>[
                        Text(
                          "100G/2056G",
                          style:
                              TextStyle(color: Color(0xffAEAEAE), fontSize: 12),
                        ),
                        SizedBox(
                          width: 6,
                        ),
                        Text(
                          "管理空间",
                          style: TextStyle(color: Colors.red, fontSize: 12),
                        ),
                      ],
                    )
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Image.asset(
                      R.img_mine_personal_icon_point,
                      width: 30,
                      height: 60,
                    ),
                    Text(
                      "当前0积分",
                      style: TextStyle(color: Colors.orange, fontSize: 12),
                    )
                  ],
                )
              ],
            ),
          ),
          Stack(
            children: <Widget>[
              Container(
                margin: EdgeInsets.only(left: 8, right: 8, top: 8),
                height: 68,
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8),
                    color: Color(0xffECD9AE)),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    Row(
                      children: <Widget>[
                        Image.asset(
                          R.img_mine_personal_banner_membership,
                          height: 24,
                          width: 28,
                        ),
                        SizedBox(
                          width: 4,
                        ),
                        Text(
                          "开通超级会员",
                          style: TextStyle(
                              fontSize: 18, fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: <Widget>[
                        Text(
                          "享极速下载,5T空间等特权",
                          style: TextStyle(
                              color: Colors.black, fontWeight: FontWeight.bold),
                        ),
                        SizedBox(
                          width: 4,
                        ),
                        SizedBox(
                          width: 16,
                          height: 16,
                          child: CircleAvatar(
                            backgroundColor: Colors.black,
                            child: Icon(
                              Icons.arrow_forward_ios,
                              color: Colors.white,
                              size: 12,
                            ),
                          ),
                        )
                      ],
                    )
                  ],
                ),
              ),
              AnimatedBuilder(
              animation:tween,
                builder:(BuildContext context, Widget child){
                  return Positioned(
                    left: tween.value,
                    child: Image.asset(R.img_mine_sapi_sdk_sweep_light),
                  );
                } ,
              )
            ],
          ),
          SizedBox(height: 6,),
          _buildCard("网盘功能",true,toolscards),
          SizedBox(height: 6),
          _buildCard("更多服务",false,toolscards),
        ],
      ),
    );
  }

  Widget _buildCard(String title,bool isMore,List<CardInfo> cards){
      return Container(
        padding: EdgeInsets.all(16),
        margin: EdgeInsets.only(left: 8,right: 8),
        height: 210,
        decoration: BoxDecoration(
            boxShadow: [BoxShadow(blurRadius: 8,color: Colors.grey.withAlpha(80))],borderRadius: BorderRadius.circular(8),color: Colors.white),
        child: Column(
          children: <Widget>[
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
              Text(title,style: TextStyle(fontSize: 20,color: Colors.black,fontWeight: FontWeight.bold),),
              isMore ? TextIcon(text: Text("更多"),icon: Icon(Icons.arrow_forward_ios,size: 12,color: Colors.grey,),icon_direction: TextIcon.TO_RIGHT,padding: 3,):Text("")
              ],),
            Flexible(
              child: Container(
                child:  GridView.builder(
                  gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 4, mainAxisSpacing: 8, crossAxisSpacing: 8),
                  itemBuilder: (context, index) {
                    return buildCard(cards[index]);
                  },
                  itemCount: 6,
                  shrinkWrap: false,
                  physics: NeverScrollableScrollPhysics(),
                ),
              ),
            )
          ],
        ),
      );
  }

  Widget buildCardWidget(CardInfo cardInfo) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        Image.asset(cardInfo.img,width: 35,height: 35,),
        SizedBox(height: 4,),
        Text(cardInfo.title,style: TextStyle(color: Colors.black,fontSize: 16,fontWeight: FontWeight.bold),),
        SizedBox(height: 2,),
      ],
    );
  }

  Widget buildCard(CardInfo card) {
    return Container(
      margin: EdgeInsets.only(top: 16),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Image.asset(card.img,width: 30,height: 40,),
          Text(card.title)
        ],
      ),
    );
  }
}