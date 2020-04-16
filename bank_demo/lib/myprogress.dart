import 'dart:math' as math;

import 'package:flutter/material.dart';

class MyProgress extends CustomPainter {
  var progress = 0;

  MyProgress({this.progress});

  @override
  void paint(Canvas canvas, Size size) {
    var paint = Paint()
      ..color = Color(0xff2B47E6)
      ..style=PaintingStyle.stroke
      ..strokeWidth = 10;
    var x = size.width / 2;
    var y = size.height / 2;
    var radius = math.min(size.width, size.height) / 2;

    var rect = Rect.fromCircle(center: Offset(x, y), radius: radius);

    var single = math.pi/180;
    canvas.drawArc(rect, 0, single*270, false, paint);
    paint.strokeCap=StrokeCap.round;
    paint.color=Color(0xff0AFFC8);
    var colors=[Color(0xff0AFFC8),Color(0xff00B78E)];

    paint.shader = SweepGradient(
      startAngle: 0,
      endAngle: 90,
      colors: colors,
    ).createShader(rect);

    canvas.drawArc(rect, single*270, single*90, false, paint);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return false;
  }
}
