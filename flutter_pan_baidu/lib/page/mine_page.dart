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

const SCROLL_MAX_VALUE = 50; //滑动最大距离

class _MinePageState extends State<MinePage>
    with TickerProviderStateMixin {
  List<CardInfo> toolscards = [
    CardInfo(R.img_service_album_icon, "相册备份", ""),
    CardInfo(R.img_service_album_icon, "回收站", ""),
    CardInfo(R.img_service_album_icon, "我的会员", ""),
    CardInfo(R.img_service_album_icon, "活动中心", ""),
    CardInfo(R.img_service_album_icon, "小程序", ""),
    CardInfo(R.img_service_album_icon, "我的分享", ""),
    CardInfo(R.img_service_album_icon, "智能设备", ""),
    CardInfo(R.img_service_album_icon, "文件清理", ""),
  ];

  List<CardInfo> moreService = [
    CardInfo(R.img_service_album_icon, "免费问医生", ""),
    CardInfo(R.img_service_album_icon, "借现金", ""),
    CardInfo(R.img_service_album_icon, "冲印商城", ""),
    CardInfo(R.img_service_album_icon, "搜一搜", ""),
    CardInfo(R.img_service_album_icon, "免流量卡", ""),
    CardInfo(R.img_service_album_icon, "领无限空间", ""),
  ];

  double progress = 0;
  double opacity = 1;
  int shadowopacity = 0;
  Animation<double> tween;
  var titleHeight = 50;
  double margingTitleTop = 0;
  AnimationController _animationController;
  ScrollController scrollController;
  Widget sweep_light = Image.asset(R.img_mine_sapi_sdk_sweep_light);

  AnimationController sildeAnimationController;
  Animation<Offset> sideAnimation;
  Animation<double> scalAnimation;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
        duration: Duration(milliseconds: 1200), vsync: this);
    tween = Tween<double>(begin: 0, end: 800).animate(_animationController);
    _animationController.repeat();

    scrollController = ScrollController();
    scrollController.addListener(() {
      _onScroll(scrollController.offset);
    });

    sildeAnimationController = AnimationController(duration: Duration(milliseconds: 1200), vsync: this);
    sideAnimation = Tween<Offset>(begin: Offset(0, 0), end: Offset(0, 0.1))
        .animate(CurvedAnimation(
      parent: sildeAnimationController,
      curve: Curves.elasticIn,
    ));
    sildeAnimationController.repeat(reverse: true);

    scalAnimation = Tween<double>(begin: 1,end: 1.2)
        .animate(CurvedAnimation(
      parent: sildeAnimationController,
      curve: Curves.elasticIn,
    ));
  }

  @override
  void dispose() {
    _animationController.dispose();
    sildeAnimationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[_buildBody(), _buildTitleBar()],
    );
  }

  Widget _buildTitleBar() {
    return Container(
      height: 50,
      decoration: BoxDecoration(boxShadow: [
        BoxShadow(
            color: Colors.grey.withAlpha(shadowopacity), offset: Offset(0, 2)),
        BoxShadow(
          color: Colors.white,
        )
      ], color: Colors.white),
      child: Stack(
        children: <Widget>[
          Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              SizedBox(
                width: 12,
              ),
              Image.asset(
                R.img_mine_bg_dn_my_scan,
                width: 30,
              ),
              SizedBox(
                width: 8,
              ),
              Text(
                "扫一扫",
                style: TextStyle(
                    color: Colors.black,
                    fontSize: 14,
                    fontWeight: FontWeight.bold),
              ),
              Flexible(
                child: Align(
                  alignment: Alignment.centerRight,
                  child: Padding(
                      padding: EdgeInsets.only(right: 12),
                      child: Image.asset(
                        R.img_mine_bg_dn_my_setting,
                        width: 30,
                      )),
                ),
              )
            ],
          ),
          Align(
              alignment: Alignment.center,
              child: Text(
                "哇—不公平嘎",
                style: TextStyle(
                    color: Colors.black.withOpacity(opacity),
                    fontSize: 18,
                    fontWeight: FontWeight.bold),
              ))
        ],
      ),
    );
  }

  Widget _buildCard(String title, bool isMore, List<CardInfo> cards) {
    return Container(
      padding: EdgeInsets.all(16),
      margin: EdgeInsets.only(left: 8, right: 8),
      height: 210,
      decoration: BoxDecoration(boxShadow: [
        BoxShadow(blurRadius: 8, color: Colors.grey.withAlpha(80))
      ], borderRadius: BorderRadius.circular(8), color: Colors.white),
      child: Column(
        children: <Widget>[
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Text(
                title,
                style: TextStyle(
                    fontSize: 20,
                    color: Colors.black,
                    fontWeight: FontWeight.bold),
              ),
              isMore
                  ? TextIcon(
                      text: Text("更多"),
                      icon: Icon(
                        Icons.arrow_forward_ios,
                        size: 12,
                        color: Colors.grey,
                      ),
                      icon_direction: TextIcon.TO_RIGHT,
                      padding: 3,
                    )
                  : Text("")
            ],
          ),
          Flexible(
            child: Container(
              child: GridView.builder(
                gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 4, mainAxisSpacing: 8, crossAxisSpacing: 8),
                itemBuilder: (context, index) {
                  return buildItemCard(cards[index]);
                },
                itemCount: cards.length,
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
        Image.asset(
          cardInfo.img,
          width: 35,
          height: 35,
        ),
        SizedBox(
          height: 4,
        ),
        Text(
          cardInfo.title,
          style: TextStyle(
              color: Colors.black, fontSize: 16, fontWeight: FontWeight.bold),
        ),
        SizedBox(
          height: 2,
        ),
      ],
    );
  }

  Widget buildItemCard(CardInfo card) {
    return Container(
      margin: EdgeInsets.only(top: 16),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Image.asset(
            card.img,
            width: 30,
            height: 40,
          ),
          Text(card.title)
        ],
      ),
    );
  }

  Widget _buildBody() {
    return SingleChildScrollView(
      controller: scrollController,
      padding: EdgeInsets.only(bottom: 8),
      child: Column(
        children: <Widget>[
          Container(
            margin: EdgeInsets.only(top: 50),
            height: 80,
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
                    _buildJiFenWidget(),
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
                animation: tween,
                builder: (BuildContext context, Widget child) {
                  return Positioned(
                    left: tween.value,
                    child: sweep_light,
                  );
                },
              )
            ],
          ),
          SizedBox(
            height: 6,
          ),
          _buildCard("网盘功能", true, toolscards),
          SizedBox(height: 6),
          _buildCard("更多服务", false, moreService),
          Container(
            padding: EdgeInsets.all(16),
            margin: EdgeInsets.all(8),
            height: 140,
            decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.all(Radius.circular(8)),
                boxShadow: [
                  BoxShadow(blurRadius: 8, color: Colors.grey.withAlpha(80))
                ]),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: <Widget>[
                    Text(
                      "主题模式",
                      style: TextStyle(
                          color: Colors.black,
                          fontWeight: FontWeight.bold,
                          fontSize: 18),
                    ),
                    TextIcon(
                      text: Text(
                        "银翼白",
                        style: TextStyle(color: Colors.grey, fontSize: 16),
                      ),
                      icon: Icon(
                        Icons.arrow_forward_ios,
                        size: 12,
                        color: Colors.grey.withAlpha(90),
                      ),
                      padding: 4,
                      icon_direction: TextIcon.TO_RIGHT,
                    )
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: <Widget>[
                    Text(
                      "设置",
                      style: TextStyle(
                          color: Colors.black,
                          fontWeight: FontWeight.bold,
                          fontSize: 18),
                    ),
                    TextIcon(
                      text: Text(
                        "",
                        style: TextStyle(color: Colors.grey, fontSize: 16),
                      ),
                      icon: Icon(
                        Icons.arrow_forward_ios,
                        size: 12,
                        color: Colors.grey.withAlpha(90),
                      ),
                      padding: 4,
                      icon_direction: TextIcon.TO_RIGHT,
                    )
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: <Widget>[
                    Text(
                      "用户反馈",
                      style: TextStyle(
                          color: Colors.black,
                          fontWeight: FontWeight.bold,
                          fontSize: 18),
                    ),
                    TextIcon(
                      text: Text(
                        "",
                        style: TextStyle(color: Colors.grey, fontSize: 16),
                      ),
                      icon: Icon(
                        Icons.arrow_forward_ios,
                        size: 12,
                        color: Colors.grey.withAlpha(90),
                      ),
                      padding: 4,
                      icon_direction: TextIcon.TO_RIGHT,
                    )
                  ],
                )
              ],
            ),
          ),
          _buildExitButton()
        ],
      ),
    );
  }

  Container _buildExitButton() {
    return Container(
      margin: EdgeInsets.only(left: 8, right: 8),
      decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.all(Radius.circular(8))),
      height: 50,
      child: Center(
          child: Text(
        "退出登录",
        style: TextStyle(
            color: Colors.redAccent, fontSize: 16, fontWeight: FontWeight.bold),
      )),
    );
  }

  void _onScroll(offset) {
    double alpha = offset / SCROLL_MAX_VALUE;
    if (alpha < 0) {
      alpha = 0;
    } else if (alpha > 1) {
      alpha = 1;
    }

    if (offset > 0) {
      shadowopacity = 50;
    } else {
      shadowopacity = 0;
    }

    setState(() {
      opacity = alpha;
    });
  }

  _buildJiFenWidget() {
    return SlideTransition(
      position: sideAnimation,
      child: ScaleTransition(
        scale: scalAnimation,
        child: Image.asset(
          R.img_mine_personal_icon_point,
          width: 30,
          height: 60,
        ),
      ),
    );
  }
}
