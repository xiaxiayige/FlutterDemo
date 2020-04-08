import 'dart:core';

import 'package:flutter/cupertino.dart';

class HabitModle {
  HabitModle({this.icon, this.lable});

  String icon;
  String lable;
}

/***
 * 卡片信息
 */
class CardInfo{

  String leftIcon;
  String rightIcon;
  String desc;
  String cardNumber;
  int money;
  bool isSelect=false;

  CardInfo({this.leftIcon, this.rightIcon, this.desc, this.cardNumber,
    this.money, this.isSelect=false});
}
