import 'dart:ui';

import 'package:bankdemo/model.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

void main() => runApp(BankApp());

class BankApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: SafeArea(
        child: Scaffold(
          backgroundColor: Color(0xfff5f5f5),
          body: BodyPage(),
        ),
      ),
    );
  }
}

class BodyPage extends StatelessWidget {
  var habits = [
    HabitModle(icon: "shopping_icon.png", lable: "Shopping"),
    HabitModle(icon: "travel_icon.png", lable: "Travel"),
    HabitModle(icon: "health_icon.png", lable: "Health"),
    HabitModle(icon: "food_icon.png", lable: "Food"),
    HabitModle(icon: "utili_icon.png", lable: "Utili")
  ];

  var bankCards = [
    CardInfo(
        leftIcon: "left_circle_logo.png",
        rightIcon: "circle_logo.png",
        desc: "Jenny Barnes",
        cardNumber: "**** 5868",
        money: 1234,
        isSelect: true),
    CardInfo(
        leftIcon: "citibank_logo.png",
        rightIcon: "visa_inc_logo.png",
        desc: "Jenny Barnes",
        cardNumber: "**** 1234",
        money: 1234),
    CardInfo(
        leftIcon: "capital_one_logo.png",
        rightIcon: "circle_logo.png",
        desc: "Jenny Barnes",
        cardNumber: "**** 8024",
        money: 1234),
  ];

  @override
  Widget build(BuildContext context) {
    return ListView(
      children: <Widget>[
        Container(
          margin: EdgeInsets.symmetric(horizontal: 12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Text(
                "Good Morning\nJenny",
                style: TextStyle(
                    color: Color(0xffFF3D00),
                    fontSize: 48,
                    fontWeight: FontWeight.w200),
              ),
              SizedBox(
                height: 12,
              ),
              _buildCardWidget(),
              SizedBox(
                height: 24,
              ),
              Text(
                "Spending Habits",
                style: TextStyle(
                    color: Color(0xff746A96),
                    fontSize: 24,
                    fontWeight: FontWeight.w300),
                textAlign: TextAlign.left,
              ),
              SizedBox(
                height: 24,
              ),
              Container(
                height: 100,
                child: ListView.builder(
                  itemBuilder: (context, index) {
                    return habitsWidget(context, habits[index]);
                  },
                  itemCount: habits.length,
                  scrollDirection: Axis.horizontal,
                ),
              ),
              SizedBox(
                height: 24,
              ),
              Text(
                "Mostly Paying with",
                style: TextStyle(
                    color: Color(0xff746A96),
                    fontSize: 24,
                    fontWeight: FontWeight.w300),
                textAlign: TextAlign.left,
              ),
              SizedBox(
                height: 24,
              ),
              ListView.builder(
                shrinkWrap: true,
                physics: NeverScrollableScrollPhysics(),
                itemBuilder: (context, index) {
                  return _buildBankCard(context, bankCards[index]);
                },
                itemCount: bankCards.length,
              )
            ],
          ),
        )
      ],
    );
  }

//中间的卡片
  _buildCardWidget() {
    return Container(
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(16.0),
        color: Color(0xffFF3D00),
      ),
      height: 149,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Row(
            children: <Widget>[
              Padding(
                padding: EdgeInsets.only(left: 10, top: 10),
                child: Align(
                  alignment: Alignment.topLeft,
                  child: Image.asset("assets/onilne_shopping.png"),
                ),
              ),
              Expanded(
                child: Text(
                  "We have nice offers for your favorite Brands",
                  style: TextStyle(color: Colors.white, fontSize: 24),
                  textAlign: TextAlign.left,
                ),
              )
            ],
          ),
          Align(
            alignment: Alignment.bottomRight,
            child: Container(
              width: 122,
              height: 46,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: <Widget>[
                  Padding(
                    child: Text(
                      "View",
                      style: TextStyle(color: Colors.white, fontSize: 24),
                    ),
                    padding: EdgeInsets.only(left: 12),
                  ),
                  Icon(
                    Icons.arrow_forward_ios,
                    color: Colors.white,
                  )
                ],
              ),
              decoration: BoxDecoration(
                  color: Color(0x33ffffff),
                  borderRadius: BorderRadius.only(
                      topLeft: Radius.circular(24),
                      bottomRight: Radius.circular(16))),
            ),
          )
        ],
      ),
    );
  }

  Widget habitsWidget(BuildContext context, HabitModle habitModle) {
    return Padding(
      padding: EdgeInsets.only(right: 18),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Container(
            width: 64,
            height: 72,
            decoration: BoxDecoration(boxShadow: [
              BoxShadow(
                  color: Color(0x33979797),
                  offset: Offset(0.0, 2.0),
                  blurRadius: 4)
            ], color: Colors.white, borderRadius: BorderRadius.circular(16)),
            child: Image.asset(habitModle.icon),
          ),
          SizedBox(
            height: 12,
          ),
          Text(
            habitModle.lable,
            style: TextStyle(color: Color(0xff746A96), fontSize: 12),
          )
        ],
      ),
    );
  }

  Widget _buildBankCard(BuildContext context, CardInfo bankCard) {
    return Container(
      margin: EdgeInsets.only(top: 23),
      height: 160,
      decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(16), color: Colors.white),
      child: Padding(
        padding: EdgeInsets.all(12),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: <Widget>[
                Image.asset(
                  bankCard.leftIcon,
                ),
                Expanded(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.end,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      Text(
                        "Selected",
                        style: TextStyle(
                            color: bankCard.isSelect
                                ? Color(0xff36E4B2)
                                : Colors.white,
                            fontSize: 16),
                      ),
                      Text(
                        bankCard.desc,
                        style:
                            TextStyle(color: Color(0xff746A96), fontSize: 16),
                      ),
                      Text(
                        bankCard.cardNumber,
                        style:
                            TextStyle(color: Color(0xff746A96), fontSize: 16),
                      ),
                    ],
                  ),
                ),
              ],
            ),
            Column(
              children: <Widget>[
                Image.asset(
                  bankCard.rightIcon,
                ),
                Expanded(
                  child: Align(
                    alignment: Alignment.bottomRight,
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: <Widget>[
                        RichText(
                          text: TextSpan(
                              text: "\$",
                              style:
                              TextStyle(color: Color(0xff746A96), fontSize: 21),
                              children: [
                                TextSpan(
                                    text: "${bankCard.money}",
                                    style: TextStyle(
                                        color: Color(0xff746A96), fontSize: 29,fontWeight: FontWeight.w800)),
                              ]),
                        ),
                        Text(" 00",
                          style: TextStyle(
                              color: Color(0xff746A96), fontSize: 21,fontWeight: FontWeight.w300),)
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
