import 'package:flutter/material.dart';
import 'package:flutterpanbaidu/bean/bean.dart';
import 'package:flutterpanbaidu/generated/r.dart';
import 'package:flutterpanbaidu/tools/common_widget.dart';

/****
 * 文件
 */
class FilePage extends StatefulWidget {


  @override
  _FilePageState createState() => _FilePageState();
}

class _FilePageState extends State<FilePage> {

  List<FileFolderBean> dataList = [
    FileFolderBean(fileName: "盘下来的资源", isSystem: true),
    FileFolderBean(fileName: "我的卡包", isSystem: true),
    FileFolderBean(fileName: "隐藏空间", isSystem: true),
  ];

  @override
  void initState() {
    super.initState();
    initData();
  }

  void initData() {
    for (int i = 0; i < 10; i++) {
      dataList.add(
          FileFolderBean(fileName: "用户文件夹$i", updateTime: "2020-08-24:14:00"));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        _buildTitleBar(),
        _buildBody(dataList)
      ],
    );
  }

  /***
   * titlebar
   */
  Widget _buildTitleBar() {
    return Container(
      height: 50,
      color: Colors.white,
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Align(
            child: Row(
              children: <Widget>[
                SizedBox(
                  width: 12,
                ),
                TextIcon(
                    text: Text("分类",
                        style: TextStyle(
                            fontSize: 18, fontWeight: FontWeight.bold)),
                    icon: Icon(Icons.arrow_drop_down),
                    icon_direction: TextIcon.TO_RIGHT)
              ],
            ),
            alignment: Alignment.centerLeft,
          ),
          Align(
            alignment: Alignment.centerRight,
            child: Row(
              children: <Widget>[
                Image.asset(
                  R.img_bg_dn_common_titlebar_btn_add,
                  width: 35,
                  height: 35,
                ),
                SizedBox(
                  width: 8,
                ),
                Image.asset(
                  R.img_bg_dn_common_titlebar_btn_transfer_small,
                  width: 35,
                  height: 35,
                ),
                SizedBox(
                  width: 8,
                ),
                Image.asset(
                  R.img_common_titlebar_icon_more,
                  width: 35,
                  height: 35,
                ),
                SizedBox(
                  width: 8,
                ),
              ],
            ),
          )
        ],
      ),
    );
  }

  /***
   * 内容部分
   */
  Widget _buildBody(List<FileFolderBean> dataList) {
    return Expanded(
      child: DecoratedBox(
        decoration: BoxDecoration(color: Colors.white),
        child: ListView(
          children: <Widget>[
            SizedBox(height: 12,),
          Container(
            margin: EdgeInsets.only(left: 12,right: 12),
            padding: EdgeInsets.only(left: 12,right: 12),
            height: 30,
            decoration: BoxDecoration(borderRadius: BorderRadius.circular(15),color:Color(0xffF8F8F8) ),
            child: Row(
              children: <Widget>[
                Icon(Icons.search,size: 16,color: Color(0xff8D8C93),),
                SizedBox(width: 4,),
                Text("搜照片:地点,事务",style: TextStyle(fontSize: 14,color: Color(0xff8D8C93)),)
              ],
            ),
          ),

            for (var value in dataList)
            _buildItem(value)
          ],
        ),
      )
    );
  }

  Widget _buildItem(FileFolderBean bean) {
    return Container(
      height: 68,
      color: Colors.white,
      child: Stack(
        children: <Widget>[
          Center(
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                SizedBox(width: 12,),
                Image.asset(
                  R.img_file_icon_list_folder,
                  width: 25,height: 25,
                ),
                SizedBox(
                  width: 20,
                ),
                Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: <Widget>[
                    Text(bean.fileName,style: TextStyle(fontSize: 16,fontWeight: FontWeight.bold),),
                    SizedBox(height: 6,),
                    if(!bean.isSystem) Text(bean.updateTime,style: TextStyle(color: Colors.grey,fontSize: 12),)
                  ],
                ),
                Expanded(child: Align(
                    alignment: Alignment.centerRight,
                    child: Padding(
                        padding: EdgeInsets.only(right: 12),
                        child: Image.asset(R.img_file_radio_button_un_checked,width: 20,height: 20,))))
              ],
            ),
          ),
          Align(
              alignment: Alignment.bottomCenter,
              child: Padding(
                  padding: EdgeInsets.only(left: 12,right: 12),
                  child: Divider(height: 1,color: Colors.grey.withAlpha(80),)))
        ],
      )
    );
  }
}
