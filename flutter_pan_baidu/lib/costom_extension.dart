import 'package:flutter/material.dart';

extension costom_widget on Widget{
  Widget center(){
    return Center(
      child: this,
    );
  }
}

extension  on String{
  Color toColor(){
    return Color(int.parse("0xff${this}"));
  }
}
