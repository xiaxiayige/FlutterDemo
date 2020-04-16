import 'package:bankdemo/myprogress.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'model.dart';

void main() => runApp(BankApp());

class BankApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "银行App",
      home: BanklyStats(),
    );
  }
}

class BanklyStats extends StatelessWidget {
  var color = Color(0xff6A75F9);

  var list = [
    StatsBean(
        icon: "food.png",
        title: "Food & Beverage",
        exceed: "Exceed \$ 45",
        desc: "Monthly average",
        mony: "1456"),
    StatsBean(
        icon: "clothing_icon.png",
        title: "Clothing",
        exceed: "",
        desc: "You have left 140 \n Monthly average",
        mony: "567"),
    StatsBean(
        icon: "food.png",
        title: "Travel",
        exceed: "Exceed \$ 45",
        desc: "Monthly average",
        mony: "100"),
  ];

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        backgroundColor: Color(0xffF5F5F5),
        body: ListView(
          children: <Widget>[
            Column(
              children: <Widget>[
                Padding(
                  padding: EdgeInsets.only(left: 24),
                  child: Row(
                    children: <Widget>[
                      Image.asset("welldone_icon.png"),
                      SizedBox(
                        width: 6,
                      ),
                      Text(
                        "Well Done !",
                        style: TextStyle(
                            color: Color(0xff546CF1),
                            fontWeight: FontWeight.bold,
                            fontSize: 42),
                      )
                    ],
                  ),
                ),
                SizedBox(
                  height: 12,
                ),
                Padding(
                  padding: EdgeInsets.only(left: 24),
                  child: RichText(
                    text: TextSpan(
                        text: "You saved ",
                        style: TextStyle(
                            color: color,
                            fontSize: 32,
                            fontWeight: FontWeight.w300),
                        children: [
                          TextSpan(
                              text: "\$ 200",
                              style: TextStyle(
                                  color: color,
                                  fontSize: 32,
                                  fontWeight: FontWeight.bold)),
                          TextSpan(
                            text: " in this week. Keep it up",
                            style: TextStyle(color: color, fontSize: 32),
                          )
                        ]),
                  ),
                ),
                SizedBox(
                  height: 20,
                ),
                Container(
                  padding: EdgeInsets.only(left: 16, right: 16),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: <Widget>[
                      Flexible(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: <Widget>[
                            Text("Budget in this Month",style: TextStyle(color: Colors.white,fontSize: 28),),
                            SizedBox(height: 16,),
                            Text("16 days left to complete",style: TextStyle(color: Colors.white,fontSize: 16),),
                          ],
                        ),
                      ),
                      Container(
                        height: 124,
                        width: 124,
                        child: CustomPaint(
                          painter: MyProgress(progress: 180),
                          child: Center(child: Text("25%",style: TextStyle(color: Colors.white,fontSize: 28,fontWeight: FontWeight.bold),)),
                        ),
                      )
                    ],
                  ),
                  margin: EdgeInsets.only(left: 12, right: 12),
                  height: 164,
                  decoration: BoxDecoration(
                      color: Color(0xff546cf1),
                      borderRadius: BorderRadius.circular(16)),
                ),
                SizedBox(
                  height: 16,
                ),
                ListView.builder(
                  itemBuilder: (context, index) {
                    return _buildCard(context, list[index]);
                  },
                  itemCount: list.length,
                  shrinkWrap: true,
                  physics: NeverScrollableScrollPhysics(),
                )
              ],
            )
          ],
        ),
      ),
    );
  }

  Widget _buildCard(BuildContext context, StatsBean bean) {
    return Container(
      padding: EdgeInsets.only(left: 26, top: 22, right: 30, bottom: 22),
      margin: EdgeInsets.only(left: 12, right: 12, bottom: 12),
      height: 142,
      decoration: BoxDecoration(
          color: Colors.white, borderRadius: BorderRadius.circular(16)),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            children: <Widget>[
              Image.asset(bean.icon),
              SizedBox(
                width: 6,
              ),
              Text(
                bean.title,
                style: TextStyle(
                    color: Color(0xff746A96),
                    fontSize: 28,
                    fontWeight: FontWeight.w300),
              )
            ],
          ),
          Expanded(
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.end,
              children: <Widget>[
                Column(
                  mainAxisAlignment: MainAxisAlignment.end,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    bean.exceed.length > 0
                        ? Row(
                            children: <Widget>[
                              Image.network(
                                  "https://api-lunacy.icons8.com/api/assets/23f1b21a-fd35-47c3-8c04-2c371572fee5/icons--Feather--Validations--Icons--Validation--Error.png"),
                              Text(
                                bean.exceed,
                                style: TextStyle(
                                    color: Color(0xffFF7070), fontSize: 16),
                              )
                            ],
                          )
                        : Text(""),
                    Text(
                      bean.desc,
                      style: TextStyle(color: Color(0xff746A96), fontSize: 16),
                    )
                  ],
                ),
                Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: <Widget>[
                    RichText(
                      text: TextSpan(
                          text: "\$ ",
                          style:
                              TextStyle(color: Color(0xff746A96), fontSize: 19),
                          children: [
                            TextSpan(
                                text: "${bean.mony}",
                                style: TextStyle(
                                    color: Color(0xff746A96),
                                    fontSize: 28,
                                    fontWeight: FontWeight.w400)),
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
            ),
          )
        ],
      ),
    );
  }
}
