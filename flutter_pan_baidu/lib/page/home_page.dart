import 'package:flutter/material.dart';
import 'package:flutterpanbaidu/bean/bean.dart';
import 'package:flutterpanbaidu/generated/r.dart';

/****
 * 首页
 */
class HomePage extends StatelessWidget {
  List<CardInfo> cardWidget = [
    CardInfo(R.img_service_album_icon, "相册", "轻松备份照片不丢失"),
    CardInfo(R.img_service_video_icon, "视频", ""),
    CardInfo(R.img_service_transfer_assistant_cion, "传输助手", ""),
    CardInfo(R.img_service_file_manager_icon, "文件管理", ""),
    CardInfo(R.img_service_note_icon, "笔记", ""),
    CardInfo(R.img_service_more_icon, "更多", "")
  ];

  Widget buildCardWidget(CardInfo cardInfo) {
    return Container(
      decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(8), color: Colors.white),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Image.asset(cardInfo.img,width: 35,height: 35,),
          SizedBox(height: 4,),
          Text(cardInfo.title,style: TextStyle(color: Colors.black,fontSize: 16,fontWeight: FontWeight.bold),),
          SizedBox(height: 2,),
          Text(cardInfo.desc,style: TextStyle(color: Color(0xffd0d0d0,),fontSize: 10),)

        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Padding(
          padding:
              const EdgeInsets.only(left: 16, top: 16, right: 16, bottom: 16),
          child: Row(
            children: <Widget>[
              Expanded(
                child: Container(
                  height: 40,
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(20),
                      color: Color(0xffF2F2F2)),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: <Widget>[
                      SizedBox(
                        width: 16,
                      ),
                      Icon(
                        Icons.search,
                        color: Color(0xffC3C3C3),
                      ),
                      SizedBox(
                        width: 8,
                      ),
                      Text(
                        "搜照片:地点,事务",
                        style: TextStyle(
                            color: Color(0xff949494),
                            fontWeight: FontWeight.w600),
                      )
                    ],
                  ),
                ),
              ),
              SizedBox(
                width: 10,
              ),
              Image.asset(
                R.img_bg_dn_common_titlebar_btn_transfer_small,
                width: 40,
                height: 40,
              ),
              SizedBox(
                width: 12,
              ),
              Image.asset(
                R.img_bg_dn_common_titlebar_btn_add,
                width: 40,
                height: 40,
              )
            ],
          ),
        ),
        SizedBox(
          height: 12,
        ),
        Expanded(
          child: SingleChildScrollView(
            child: Column(children: <Widget>[
              Padding(
                padding: const EdgeInsets.only(left: 16, right: 12,bottom: 12),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    Text(
                      "常用服务",
                      style: TextStyle(
                          color: Colors.black,
                          fontSize: 18,
                          fontWeight: FontWeight.bold),
                    ),
                    Row(
                      children: <Widget>[
                        Text(
                          "查看全部",
                          style: TextStyle(
                            color: Colors.grey,
                          ),
                        ),
                        SizedBox(
                          width: 4,
                        ),
                        Image.asset(
                          R.img_bg_dn_audio_album_null_new_enter_nor,
                          width: 12,
                          height: 12,
                        )
                      ],
                    )
                  ],
                ),
              ),
              SizedBox(
                height: 8,
              ),
              Container(
                margin: EdgeInsets.only(left: 12, right: 16),
                height: 230,
                child: GridView.builder(
                  gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 3, mainAxisSpacing: 8, crossAxisSpacing: 8),
                  itemBuilder: (context, index) {
                    return buildCardWidget(cardWidget[index]);
                  },
                  itemCount: 6,
                  shrinkWrap: false,
                  physics: NeverScrollableScrollPhysics(),
                ),
              ),
              Container(margin: EdgeInsets.only(left: 12,right: 16),
                width: double.infinity,
                height: 50,
                color: Colors.white,
                child: Align( alignment:Alignment.bottomLeft,
                    child: Text("照片定制新品上线",style: TextStyle(color: Colors.black,
                        fontSize: 18,
                        fontWeight: FontWeight.bold),)),
              ),
              Stack(
                children: <Widget>[
                  Container(
                    height: 300,
                    margin: EdgeInsets.only(left: 12,right: 16,top: 16),
                    color: Colors.orange,
                  )
                ],
              ),
              SizedBox(height: 4,)
            ],),
          ),
        ),
      ],
    );
  }
}
