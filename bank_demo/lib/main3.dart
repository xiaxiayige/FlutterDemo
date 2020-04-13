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
        backgroundColor: Color(0xffF5F5F5),
        body:Stack(
          children: <Widget>[

            ListView(
              children: <Widget>[
                Container(
                  width: double.infinity,
                  height: 272,
                  decoration: BoxDecoration(
                      color: Color(0xff1CE1AC),
                      borderRadius: BorderRadius.only(
                          bottomLeft: Radius.circular(24),
                          bottomRight: Radius.circular(24))),
                  child: Padding(
                    padding: EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: <Widget>[

                        SizedBox(
                          height: 24,
                        ),
                        Row(
                          children: <Widget>[
                            Image.asset("sparkle_icon.png"),
                            SizedBox(
                              width: 16,
                            ),
                            Text(
                              "Big Save !",
                              style: TextStyle(
                                  color: Colors.white,
                                  fontWeight: FontWeight.bold,
                                  fontSize: 48),
                            )
                          ],
                        ),
                        SizedBox(
                          height: 16,
                        ),
                        RichText(
                          text: TextSpan(
                              text: "You can save ",
                              style: TextStyle(
                                  color: Colors.white,
                                  fontSize: 32,
                                  fontWeight: FontWeight.w300),
                              children: [
                                TextSpan(
                                    text: " \$",
                                    style: TextStyle(
                                        color: Colors.white, fontSize: 32)),
                                TextSpan(
                                    text: " 42",
                                    style: TextStyle(
                                        color: Colors.white,
                                        fontWeight: FontWeight.bold)),
                                TextSpan(
                                    text: " by paying with Bankly",
                                    style: TextStyle(
                                        color: Colors.white,
                                        fontWeight: FontWeight.w300))
                              ]),
                        )
                      ],
                    ),
                  ),
                ),
                SizedBox(height: 16,),
                Padding(
                    padding: EdgeInsets.only(left: 16),
                    child: Text("Available Credit Cards",style: TextStyle(fontSize:16,color: Color(0xff8175a3),fontWeight: FontWeight.w300),)),
                SizedBox(height: 16,),
                ListView.builder(
                    shrinkWrap: true,
                    physics: NeverScrollableScrollPhysics(),
                    itemCount: bankCards.length,
                    itemBuilder: (context,index){
                      return _buildButtomCard(context, bankCards[index]);
                    })
              ],
            ),
            SizedBox(
              height: 50,
              width: 50,
              child: Icon(
                Icons.arrow_back_ios,
                color: Colors.white,
              ),
            ),
            Align(
              alignment: Alignment.bottomRight,
              child: Container(
                width: 190,
                height: 70,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Text("Continue",style: TextStyle(color: Colors.white,fontSize: 24),)
                    ,SizedBox(width: 8,)
                    ,Icon(Icons.arrow_forward_ios,color: Colors.white,)
                  ],
                ),
                decoration: BoxDecoration(
                    color: Color(0xff1CE1AC),
                    borderRadius: BorderRadius.only(topLeft: Radius.circular(55.5),)),
              ),
            )
          ],
        ),
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
                          color: cardInfo.isSelect
                              ? Color(0xff36E4B2)
                              : Colors.white),
                    ),
                    Text(
                      cardInfo.desc,
                      style: TextStyle(color: Color(0xff746A96), fontSize: 16),
                    ),
                    SizedBox(
                      height: 8,
                    ),
                    Text(
                      cardInfo.cardNumber,
                      style: TextStyle(color: Color(0xff746A96), fontSize: 16),
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
    );
  }
}
