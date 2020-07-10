import 'package:flutter/material.dart';

/***
 * 文本+icon
 */
class TextIcon extends StatelessWidget {
  static const String ICON_DIRECTION = "icon_direction";
  static const int TO_LEFT = 0;
  static const int TO_RIGHT = 1;
  static const int TO_TOP = 2;
  static const int TO_BUTTOM = 3;

  Text text;
  Widget icon; //有可能是 icon or image string
  int icon_direction; //图标放置方向
  double padding = 0;

  TextIcon({
    @required this.text,
    @required this.icon,
    @required this.icon_direction,
    this.padding = 0,
  });

  @override
  Widget build(BuildContext context) {
    return buildWidget(icon_direction, padding, icon, text);
  }

  Widget buildWidget(int direction, double padding, Widget icon, text) {
    if (direction == TO_LEFT) {
      return Row(
        children: <Widget>[
          icon,
          SizedBox(
            width: padding,
          ),
          text
        ],
      );
    } else if (direction == TO_RIGHT) {
      return Row(
        children: <Widget>[
          text,
          SizedBox(
            width: padding,
          ),
          icon,
        ],
      );
    } else if (direction == TO_TOP) {
      return Column(
        children: <Widget>[
          icon,
          SizedBox(
            height: padding,
          ),
          text,
        ],
      );
    } else {
      return Column(
        children: <Widget>[
          text,
          SizedBox(
            height: padding,
          ),
          icon,
        ],
      );
    }
  }
}
