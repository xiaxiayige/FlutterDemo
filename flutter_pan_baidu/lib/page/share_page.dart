
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
            Container(
              margin: EdgeInsets.only(left: 16,right: 16,top: 70),
              padding: EdgeInsets.only(left: 8,right: 8),
              height: 40,
              decoration: BoxDecoration(color: Color(0xffF8F8F8),borderRadius: BorderRadius.circular(25)
              ,),
              child:Row(
                children: <Widget>[
                  Icon(Icons.search,size: 18,color: Color(0xffd9d9d9),),
                  SizedBox(width: 6,),
                  Text("用户/群/标签",style: TextStyle(color: Color(0xff999999)),)
                ],
              ),
            ),
            SizedBox(height: 20,),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: <Widget>[
                Column(
                  children: <Widget>[
                    Image.asset(R.img_share_share_list_guide_item_own_share_icon,width: 30,height: 30,),
                    SizedBox(height: 4,),
                    Text("创建的共享",style: TextStyle(color: Colors.black),),
                  ],
                ),
                Column(
                  children: <Widget>[
                    Image.asset(R.img_share_share_list_guide_item_own_joined_share,width: 30,height: 30,),
                    SizedBox(height: 4,),
                    Text("加入的共享",style: TextStyle(color: Colors.black),),
                  ],
                ),
                Column(
                  children: <Widget>[
                    Image.asset(R.img_share_share_list_guide_item_own_transfer_helper,width: 30,height: 30,),
                    SizedBox(height: 4,),
                    Text("传输助手",style: TextStyle(color: Colors.black),),
                  ],
                ),
              ],
            ),
            SizedBox(height: 6,),
            Expanded(
              child: Container(
                color: Colors.white,
                padding: EdgeInsets.all(16),
                child:Column(
                  children: <Widget>[
                      Container(
                        height: 50,
                        decoration:
                        BoxDecoration(borderRadius: BorderRadius.circular(12),color: Color(0xffF5F5F5)),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: <Widget>[
                            Padding(
                                padding: EdgeInsets.only(left: 12),
                                child: Text("打开通知，第一时间获取好资源")),
                            Row(
                              children: <Widget>[
                                Container(
                                    padding: EdgeInsets.only(left: 6,right: 6,top: 4,bottom: 4),
                                    decoration: BoxDecoration(borderRadius: BorderRadius.circular(50),color: Color(0xff06A7FF),),
                                    child: Text("去开启",style: TextStyle(color: Colors.white),)),
                                SizedBox(width: 4,),
                                Icon(Icons.close,size: 16,color: Color(0xffBDBDBD),),
                                SizedBox(width: 12,)
                              ],
                            )
                          ],
                        ),
                      ),
                    Expanded(
                      child: ListView(
                        shrinkWrap: true,
                        physics: NeverScrollableScrollPhysics(),
                        children: <Widget>[
                          _buildItem(R.img_share_share_link_guide_icon,"管理我的分享","查看已发出的分享详情","06-04 15:14"),
                          _buildItem(R.img_share_youa_logo,"一刻相册","恭喜你获得永久无限空间","06-04 15:14"),
                          _buildItem(R.img_share_item_new_follow_icon,"新好友","平安秘诀网huaw、ml加你为好友了","06-04 15:14"),
                          _buildItem(R.img_share_item_share_guide_icon_normal,"给朋友分享文件","立即体验全新百度网盘","06-04 15:14"),
                      ],),
                    )
                  ],
                ),
              ),
            )
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

  Widget _buildItem(String iconStr, String title, String desc, String time) {
  return Padding(
    padding: EdgeInsets.only(top: 12,bottom: 12),
    child: Row(
      children: <Widget>[
        Image.asset(iconStr,width: 45,height: 45,),
        SizedBox(width: 8,),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text(title,style: TextStyle(fontSize: 18),),
            SizedBox(height: 6,),
            Text(desc,style: TextStyle(color: Color(0xffD9D9D9),fontSize: 14),)
          ],
        ),
        Expanded(
            child: Align(
                alignment: Alignment.topRight,
                child: Text(time,style: TextStyle(color:Color(0xffD9D9D9),fontSize: 12 ),)))
      ],
    ),
  );
}
