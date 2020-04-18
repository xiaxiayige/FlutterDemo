import 'dart:core';

class HabitModle {
  HabitModle({this.icon, this.lable});

  String icon;
  String lable;
}

/***
 * 卡片信息
 */
class CardInfo {
  String leftIcon;
  String rightIcon;
  String desc;
  String cardNumber;
  int money;
  bool isSelect = false;

  CardInfo(
      {this.leftIcon,
        this.rightIcon,
        this.desc,
        this.cardNumber,
        this.money,
        this.isSelect = false});
}

class StatsBean {
  String icon;
  String title;
  String exceed;
  String desc;
  String mony;

  StatsBean({this.icon, this.title, this.exceed, this.desc, this.mony});
}
