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
        body: Stack(
          children: <Widget>[
            ListView(
              children: <Widget>[
                Column(
                  children: <Widget>[
                    Container(
                        child: Padding(
                          padding: EdgeInsets.only(left: 28, top: 75),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: <Widget>[
                              Row(
                                children: <Widget>[
                                  Image.asset("sparkle_icon.png"),
                                  SizedBox(
                                    width: 8,
                                  ),
                                  Text(
                                    "Big Save !",
                                    style: TextStyle(
                                        color: Colors.white,
                                        fontSize: 48,
                                        fontWeight: FontWeight.w600),
                                  )
                                ],
                              ),
                              SizedBox(
                                height: 12,
                              ),
                              RichText(
                                text: TextSpan(
                                    text: "You can save \$ ",
                                    style: TextStyle(
                                        color: Colors.white,
                                        fontSize: 32,
                                        fontWeight: FontWeight.w300),
                                    children: [
                                      TextSpan(
                                          text: "42",
                                          style: TextStyle(
                                              color: Colors.white,
                                              fontSize: 32,
                                              fontWeight: FontWeight.bold)),
                                      TextSpan(
                                          text: " by paying with Bankly",
                                          style: TextStyle(
                                              color: Colors.white,
                                              fontSize: 32,
                                              fontWeight: FontWeight.w300))
                                    ]),
                              )
                            ],
                          ),
                        ),
                        height: 272,
                        decoration: BoxDecoration(
                          color: Color(0xff1cE1ac),
                          borderRadius: BorderRadius.only(
                              bottomLeft: Radius.circular(32),
                              bottomRight: Radius.circular(32)),
                        )),
                    SizedBox(
                      height: 16,
                    ),
                    Align(
                        alignment: Alignment.topLeft,
                        child: Padding(
                            padding: EdgeInsets.only(left: 16),
                            child: Text(
                              "Available Credit Cards",
                              style: TextStyle(
                                  color: Color(0xff8175A3),
                                  fontSize: 24,
                                  fontWeight: FontWeight.w300),
                            ))),
                    SizedBox(
                      height: 16,
                    ),
                    ListView.builder(
                      physics: NeverScrollableScrollPhysics(),
                      shrinkWrap: true,
                      itemBuilder: (context, index) {
                        return _buildButtomCard(context, bankCards[index]);
                      },
                      itemCount: bankCards.length,
                    )
                  ],
                )
              ],
            ),
            Padding(
                padding: EdgeInsets.only(top: 12, left: 16),
                child: Icon(
                  Icons.arrow_back_ios,
                  color: Colors.white,
                )),
            Align (
              alignment: Alignment.bottomRight,
              child: Container(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Text(
                      "Continue",
                      style: TextStyle(
                          color: Colors.white,
                          fontSize: 24,
                          fontWeight: FontWeight.bold),
                    ),
                    SizedBox(
                      width: 8,
                    ),
                    Icon(
                      Icons.arrow_forward_ios,
                      color: Colors.white,
                    )
                  ],
                ),
                width: 200,
                height: 75,
                decoration: BoxDecoration(
                    color: Color(0xff1CE1AC),
                    borderRadius:
                        BorderRadius.only(topLeft: Radius.circular(50))),
              ),
            )
          ],
        ),
      ),
    );
  }

  _buildButtomCard(BuildContext context, CardInfo cardInfo) {
    return Padding(
      padding: EdgeInsets.only(left: 16, right: 16),
      child: Container(
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
                            color: cardInfo.isSelect
                                ? Color(0xff36E4B2)
                                : Colors.white),
                      ),
                      Text(
                        cardInfo.desc,
                        style:
                            TextStyle(color: Color(0xff746A96), fontSize: 16),
                      ),
                      SizedBox(
                        height: 8,
                      ),
                      Text(
                        cardInfo.cardNumber,
                        style:
                            TextStyle(color: Color(0xff746A96), fontSize: 16),
                      )
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
                                    color: Color(0xff746A96),
                                    fontSize: 29,
                                    fontWeight: FontWeight.w800)),
                          ]),
                    ),
                    Text(
                      " 00",
                      style: TextStyle(
                          color: Color(0xff746A96),
                          fontSize: 21,
                          fontWeight: FontWeight.w300),
                    )
                  ],
                )
              ],
            )
          ],
        ),
      ),
    );
  }
}
