import 'package:bankdemo/model.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

void main() => runApp(BankApp());

class BankApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "银行App",
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatelessWidget {
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
    return SafeArea(
      child: Scaffold(
        backgroundColor: Color(0xfff5f5f5),
        appBar: AppBar(
          leading: Icon(
            Icons.arrow_back_ios,
            color: Colors.red,
          ),
          backgroundColor: Colors.white,
        ),
        body: ListView(
          children: <Widget>[
            Container(
              padding: EdgeInsets.all(20.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Text(
                    "Good Morning\nKangKang",
                    style: TextStyle(
                        color: Color(0xffff3d00),
                        fontSize: 48,
                        fontWeight: FontWeight.w300),
                  ),
                  SizedBox(
                    height: 20,
                  ),
                  _buildTopCard(),
                  SizedBox(
                    height: 20,
                  ),
                  Text(
                    "Spending Habits",
                    style: TextStyle(
                        color: Color(0xff746A96),
                        fontSize: 24,
                        fontWeight: FontWeight.w300),
                  ),
                  SizedBox(
                    height: 20,
                  ),
                  Container(
                    height: 115,
                    child: ListView.builder(
                      itemBuilder: (context, index) {
                        return _buildCenterCard(context, habits[index]);
                      },
                      itemCount: habits.length,
                      scrollDirection: Axis.horizontal,
                    ),
                  ),
                  SizedBox(
                    height: 20,
                  ),
                  Text(
                    "Mostly Paying with",
                    style: TextStyle(
                        color: Color(0xff746A96),
                        fontSize: 24,
                        fontWeight: FontWeight.w300),
                  ),
                  SizedBox(
                    height: 20,
                  ),
                  ListView.builder(
                    itemBuilder: (context, index) {
                      return _buildButtomCard(context, bankCards[index]);
                    },
                    itemCount: bankCards.length,
                    physics: NeverScrollableScrollPhysics(),
                    shrinkWrap: true,
                  )
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  //构建顶部的卡片
  _buildTopCard() {
    return Container(
      height: 149,
      decoration: BoxDecoration(
          color: Color(0xffff3d00), borderRadius: BorderRadius.circular(16)),
      child: Column(
        children: <Widget>[
          Padding(
            padding: EdgeInsets.only(left: 8, top: 14, right: 8),
            child: Row(
              children: <Widget>[
                Image.asset("onilne_shopping.png"),
                Expanded(
                    child: Text(
                  "We have nice offersWe have nice offers",
                  style: TextStyle(
                      fontSize: 24,
                      color: Colors.white,
                      fontWeight: FontWeight.w300),
                ))
              ],
            ),
          ),
          Expanded(
            child: Align(
              alignment: Alignment.bottomRight,
              child: Container(
                width: 124,
                height: 47,
                decoration: BoxDecoration(
                    borderRadius: BorderRadius.only(
                        bottomRight: Radius.circular(16),
                        topLeft: Radius.circular(18)),
                    color: Color(0x4dffffff)),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Text(
                      "View",
                      style: TextStyle(color: Colors.white, fontSize: 20),
                    ),
                    Icon(Icons.arrow_forward_ios, color: Colors.white)
                  ],
                ),
              ),
            ),
          )
        ],
      ),
    );
  }

  //构建中间的列表卡片
  Widget _buildCenterCard(BuildContext context, HabitModle habit) {
    return Padding(
      padding: EdgeInsets.only(right: 16),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Container(
            height: 72,
            width: 64,
            decoration: BoxDecoration(boxShadow: [
              BoxShadow(
                  color: Color(0xffffff), blurRadius: 4, offset: Offset(0, 2))
            ], borderRadius: BorderRadius.circular(16), color: Colors.white),
            child: Image.asset(habit.icon),
          ),
          SizedBox(
            height: 8,
          ),
          Text(
            habit.lable,
            style: TextStyle(
                color: Color(0xff746A96), fontWeight: FontWeight.w400),
          )
        ],
      ),
    );
  }

  _buildButtomCard(BuildContext context, CardInfo cardInfo) {
    return Container(
      height: 160,
      padding: EdgeInsets.all(20),
      margin: EdgeInsets.only(top: 20),
      decoration: BoxDecoration(
          color: Colors.white, borderRadius: BorderRadius.circular(16)),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              Image.asset(cardInfo.leftIcon),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: <Widget>[
                    Text(
                      "Select",
                      style: TextStyle(
                        fontSize: 16,
                          fontWeight: FontWeight.bold,
                          color:
                              cardInfo.isSelect ? Color(0xff36E4B2) : Colors.white),
                    ),
                    Text(cardInfo.desc,style: TextStyle(color: Color(0xff746A96),fontSize: 16),)
                    ,SizedBox(height: 8,)
                    ,Text(cardInfo.cardNumber,style: TextStyle(color: Color(0xff746A96),fontSize: 16),)
                  ],
                ),
              )
            ],
          ),
         Column(
           mainAxisAlignment: MainAxisAlignment.spaceBetween,
           children: <Widget>[
             Image.asset(cardInfo.rightIcon),
             Row(
               crossAxisAlignment: CrossAxisAlignment.start,
               children: <Widget>[
                 RichText(
                   text: TextSpan(
                       text: "\$",
                       style:
                       TextStyle(color: Color(0xff746A96), fontSize: 21),
                       children: [
                         TextSpan(
                             text: "${cardInfo.money}",
                             style: TextStyle(
                                 color: Color(0xff746A96), fontSize: 29,fontWeight: FontWeight.w800)),
                       ]),
                 ),
                 Text(" 00",
                   style: TextStyle(
                       color: Color(0xff746A96), fontSize: 21,fontWeight: FontWeight.w300),)
               ],
             )
           ],
         )
        ],
      ),
    );
  }
}
