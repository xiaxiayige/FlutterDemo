import 'dart:math' as math;

import 'package:flutter/material.dart';
//https://github.com/flutter/flutter/issues/23648
//https://stackoverflow.com/questions/50376727/flutter-drawarc-method-draws-full-circle-not-just-arc
//https://api.flutter.dev/flutter/vector_math/vector_math-library.html
//https://api.flutter.dev/flutter/dart-ui/Canvas/drawArc.html
class MyProgress extends CustomPainter {
  var progress = 0;

  MyProgress({this.progress});

  @override
  void paint(Canvas canvas, Size size) {
    var paint = Paint()
      ..color = Color(0xff2B47E6)
      ..style = PaintingStyle.stroke
      ..strokeWidth = 10
      ..isAntiAlias=true;



    var x = size.width / 2;
    var y = size.height / 2;
    var radius = math.min(size.width, size.height) / 2;

    var rect = Rect.fromCircle(center: Offset(x, y), radius: radius);

    var single = math.pi / 180;
    canvas.drawArc(rect, 0, single * 270, false, paint);
//    canvas.drawCircle(Offset(x, y), radius, paint);

    paint.color = Color(0xff0AFFC8);
    var colors = [Color(0xff0AFFC8),Color(0xff00B78E)];

    paint.shader = SweepGradient(
      startAngle: 0,
      endAngle: single*180 ,
      colors: colors,
    ).createShader(rect);

    canvas.save();
    paint.strokeCap = StrokeCap.round;
//    canvas.drawArc(rect, single*270*-1, single*180, false, paint);
    canvas.rotate(single*180);
    canvas.translate(-size.width, -size.height);
    canvas.drawArc(rect, single*270*-1, single*90, false, paint);
    canvas.restore();
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return false;
  }
}
