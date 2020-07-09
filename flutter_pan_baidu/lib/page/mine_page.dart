import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutterpanbaidu/generated/r.dart';

/****
 * 我的
 */
class MinePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Container(
          height: 125,
          color: Colors.white,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: <Widget>[
              CircleAvatar(
                child: Image.asset(
                  R.img_mine_mine_head,
                  width: 80,
                  height: 80,
                ),
              ),
              Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: <Widget>[
                  Row(
                    children: <Widget>[
                      Text(
                        "哇—不公平嘎",
                        style: TextStyle(
                            fontSize: 20,
                            color: Colors.black,
                            fontWeight: FontWeight.bold),
                      ),
                      SizedBox(
                        width: 4,
                      ),
                      Image.asset(
                        R.img_mine_home_identity_common,
                        width: 18,
                        height: 16,
                      ),
                    ],
                  ),
                  SizedBox(
                    height: 8,
                  ),
                  Container(
                    width: 180,
                    height: 3,
                    child: LinearProgressIndicator(
                      backgroundColor: Colors.grey.withAlpha(50),
                      value: 0.2,
                    ),
                  ),
                  SizedBox(
                    height: 8,
                  ),
                  Row(
                    children: <Widget>[
                      Text(
                        "100G/2056G",
                        style:
                            TextStyle(color: Color(0xffAEAEAE), fontSize: 12),
                      ),
                      SizedBox(
                        width: 6,
                      ),
                      Text(
                        "管理空间",
                        style: TextStyle(color: Colors.red, fontSize: 12),
                      ),
                    ],
                  )
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Image.asset(
                    R.img_mine_personal_icon_point,
                    width: 30,
                    height: 60,
                  ),
                  Text(
                    "当前0积分",
                    style: TextStyle(color: Colors.orange, fontSize: 12),
                  )
                ],
              )
            ],
          ),
        ),
        Stack(
          children: <Widget>[
            Container(
              margin: EdgeInsets.only(left: 8, right: 8, top: 8),
              height: 68,
              decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(8),
                  color: Color(0xffECD9AE)),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  Row(
                    children: <Widget>[
                      Image.asset(
                        R.img_mine_personal_banner_membership,
                        height: 24,
                        width: 28,
                      ),
                      SizedBox(
                        width: 4,
                      ),
                      Text(
                        "开通超级会员",
                        style: TextStyle(
                            fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                    ],
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.end,
                    children: <Widget>[
                      Text(
                        "享极速下载,5T空间等特权",
                        style: TextStyle(
                            color: Colors.black, fontWeight: FontWeight.bold),
                      ),
                      SizedBox(
                        width: 4,
                      ),
                      SizedBox(
                        width: 16,
                        height: 16,
                        child: CircleAvatar(
                          backgroundColor: Colors.black,
                          child: Icon(
                            Icons.arrow_forward_ios,
                            color: Colors.white,
                            size: 12,
                          ),
                        ),
                      )
                    ],
                  )
                ],
              ),
            ),
            Positioned(
              left: 100,
              child: Image.asset(R.img_mine_sapi_sdk_sweep_light),
            )
          ],
        ),
        SizedBox(height: 4,),
        Container(
          margin: EdgeInsets.only(left: 8,right: 8),
          height: 200,
          color: Colors.white,
        )
      ],
    );
  }
}
