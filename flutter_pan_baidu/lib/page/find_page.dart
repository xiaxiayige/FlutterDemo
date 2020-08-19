import 'package:flutter/material.dart';
import 'package:flutterpanbaidu/generated/r.dart';

/****
 * 发现
 */
class FindPage extends StatefulWidget {
  @override
  _FindPageState createState() => _FindPageState();
}

class _FindPageState extends State<FindPage>
    with SingleTickerProviderStateMixin {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [_buildTitle(), _buildTabBar(), _buildBody()],
    );
  }

  Widget _buildTabBar() {
    return Container(
      height: 40,
      child: TabBar(
        tabs: <Widget>[
          for (var i in tags)
            Text(
              i,
            )
        ],
        controller: _tabController,
        labelColor: Colors.black,
        indicatorColor: Colors.blue,
        isScrollable: true,
        indicatorSize: TabBarIndicatorSize.label,
      ),
    );
  }

  PageController _pageController;
  TabController _tabController;

  @override
  void initState() {
    _pageController = PageController(initialPage: 0);
    _tabController =
        TabController(length: tags.length, initialIndex: 0, vsync: this);
  }

  /***
   * 标题栏部分
   */
  Widget _buildTitle() {
    return Container(
      margin: EdgeInsets.only(left: 12, top: 8),
      height: 50,
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Flexible(
            flex: 6,
            child: Container(
              height: 40,
              padding: EdgeInsets.only(left: 4),
              decoration: BoxDecoration(
                  color: Color(0xffF5F5F5),
                  borderRadius: BorderRadius.circular(25)),
              child: Row(
                children: <Widget>[],
              ),
            ),
          ),
          SizedBox(
            width: 10,
          ),
          Flexible(
              flex: 1,
              child: Stack(
                alignment: Alignment.center,
                children: <Widget>[
                  Image.asset(
                    R.img_find_tradeplatform_homepage_title_button_personal,
                    width: 40,
                    height: 40,
                  ),
                  Positioned(
                      top: 5,
                      right: 0,
                      child: Container(
                        child: null,
                        width: 10,
                        height: 10,
                        decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(5),
                            color: Colors.red),
                      ))
                ],
              )),
          SizedBox(
            width: 10,
          ),
          Flexible(
              flex: 1,
              child: Image.asset(
                R.img_find_tradeplatform_homepage_palying_black_10,
                width: 40,
                height: 40,
              ))
        ],
      ),
    );
  }

  var tags = [
    "关注",
    "推荐",
    "会员",
    "教育",
    "热门影视",
    "休闲娱乐",
    "小说",
    "外语",
    "模板素材",
    "情感生活",
    "人文历史",
    "商业财经"
  ];

  Widget _buildBody() {
    return Expanded(
      child:
        TabBarView(
          controller: _tabController,
          children: <Widget>[
            for (var i in tags)
              Center(child:Text(
                i,
              ))
          ],
        )
//      PageView.builder(
//        itemBuilder: (context, index) {
//          return Center(child: Text(tags[index]));
//        },
//        controller: _pageController,
//        scrollDirection: Axis.horizontal,
//        itemCount: tags.length,
//      ),
    );
  }
}
