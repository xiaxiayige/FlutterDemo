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
        body: Center(

          child: CircularProgressIndicator(backgroundColor: Colors.red,),
        ),
      ),
    );
  }

}
