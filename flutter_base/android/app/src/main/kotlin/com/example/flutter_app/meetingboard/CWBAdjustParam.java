package com.example.flutter_app.meetingboard;

import android.graphics.Point;

public class CWBAdjustParam {

    private int adjustStep;
    public   Point[]  standPoint;
    private Point[] adjustPoint;

    public CWBAdjustParam() {

        standPoint = new Point[4];
        adjustPoint = new Point[4];
        adjustStep = 0;
        standPoint[0]=new Point();
        standPoint[0].x = 75;
        standPoint[0].y = 75;
        standPoint[1]=new Point();
        standPoint[1].x = 992-75;
        standPoint[1].y = 75;
        standPoint[2]=new Point();
        standPoint[2].x = 75;
        standPoint[2].y = 1404-75;
        standPoint[3]=new Point();
        standPoint[3].x = 992-75;
        standPoint[3].y = 1404-75;

    }

    public void AddAdjustPoint(Point pt) {

        if (adjustStep < 4) {
            adjustPoint[adjustStep]  = pt ;
            adjustStep++;
        }
    }

    public CWBPenParam GetAdjustPenParam() {
        CWBPenParam ret = new CWBPenParam();
        try {
            if (adjustStep != 4) return ret;
            ret.xSlope = ((standPoint[0].x + standPoint[2].x) - (standPoint[1].x + standPoint[3].x))*1.0f /
                    ((adjustPoint[0].x + adjustPoint[2].x) - (adjustPoint[1].x + adjustPoint[3].x));
            ret.xOffset = standPoint[0].x - adjustPoint[0].x * ret.xSlope;

            ret.ySlope = ((standPoint[0].y + standPoint[1].y) - (standPoint[2].y + standPoint[3].y))*1.0f /
                    ((adjustPoint[0].y + adjustPoint[1].y) - (adjustPoint[2].y + adjustPoint[3].y));
            ret.yOffset = standPoint[0].y - adjustPoint[0].y * ret.ySlope;
            return  ret;

        } catch (Exception e) {
            return ret;
        }
    }

    public int GetAdjustStep() {
        return adjustStep;
    }


}
