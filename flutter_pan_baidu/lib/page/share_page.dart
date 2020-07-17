
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutterpanbaidu/generated/r.dart';

/****
 * 共享
 */
class SharePage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Stack(
      children: <Widget>[
        Column(
          children: <Widget>[

          ],
        ),
       Container(
         color: Colors.white,
         height: 50,
         padding: EdgeInsets.only(left: 8,right: 8),
         child:  Stack(
          children: <Widget>[
            Align(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: <Widget>[
                  Image.asset(R.img_bg_dn_friend_titlebar_icon_plane_normal,width: 30,height: 30,),
                  Row(
                    children: <Widget>[
                      Image.asset(R.img_bg_dn_common_titlebar_btn_address,width: 30,height: 30,),
                      SizedBox(width: 8,),
                      Image.asset(R.img_bg_dn_common_titlebar_btn_add,width: 30,height: 30,),
                    ],
                  )
                ],
              ),
              alignment: Alignment.center,
            ),
            Center(child: Text("共享",style: TextStyle(color: Colors.black,fontSize: 16,fontWeight: FontWeight.w700),),)
          ],
         ),
       )
      ],
    );
  }
}
