import 'package:flutter/material.dart';
import 'package:flutterpanbaidu/generated/r.dart';

/****
 * 发现
 */
class FindPage extends StatefulWidget {
  @override
  _FindPageState createState() => _FindPageState();
}

class _FindPageState extends State<FindPage> {
  @override
  Widget build(BuildContext context) {
    return Column(
      children:[
        _buildTitle(),
        _buildBody()
      ],
    );
  }

  PageController _pageController;

  @override
  void initState() {
    _pageController = PageController(initialPage: 0);
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

  Widget _buildBody() {
    return Expanded(
      child: PageView.builder(
        itemBuilder: (context, index) {
          return Center(child: Text("page $index"));
        },
        controller: _pageController,
        itemCount: 7,
      ),
    );
  }
}
