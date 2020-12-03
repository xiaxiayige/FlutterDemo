import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutterpanbaidu/page/file_page.dart';
import 'package:flutterpanbaidu/page/find_page.dart';
import 'package:flutterpanbaidu/page/home_page.dart';
import 'package:flutterpanbaidu/page/mine_page.dart';
import 'package:flutterpanbaidu/page/share_page.dart';
import 'package:flutterpanbaidu/costom_extension.dart';
import 'generated/r.dart';

void main(){
  ErrorWidget.builder = (FlutterErrorDetails details){
         bool inDebug = true;
         assert(() { inDebug = true; return true; }());
         // In debug mode, use the normal error widget which shows
         // the error message:
         if (inDebug)
           return ErrorWidget(details.exception);
         // In release builds, show a yellow-on-blue message instead:
         return Container(
           alignment: Alignment.center,
           child: Text(
             'Error!',
             style: TextStyle(color: Colors.yellow),
             textDirection: TextDirection.ltr,
           ),
         );
  };


  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle(
      statusBarColor: Colors.transparent,
      statusBarIconBrightness: Brightness.dark,
      statusBarBrightness: Platform.isAndroid ? Brightness.dark : Brightness.light,
      systemNavigationBarColor: Colors.white,
      systemNavigationBarDividerColor: Colors.grey,
      systemNavigationBarIconBrightness: Brightness.dark,
    ));
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
//        scaffoldBackgroundColor: Colors.white
      ),
      home: FragmentContainer(),
    );
  }
}

class FragmentContainer extends StatefulWidget {
  @override
  _FragmentContainerState createState() => _FragmentContainerState();
}


class _FragmentContainerState extends State<FragmentContainer> {
  var currentPosition = 0;
  Widget bodyWidget;
  HomePage homePage;
  FindPage findPage;
  MinePage minePage;
  SharePage sharePage;
  FilePage filePage;

  @override
  void initState() {
    homePage = HomePage();
    bodyWidget = homePage;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: SafeArea(
        top: false,
        bottom: false,
        child: Scaffold(
          backgroundColor: Color(0xffFBFBFB),
          body: bodyWidget,
          bottomNavigationBar: BottomNavigationBar(
            items: [
              BottomNavigationBarItem(
                  icon: Image.asset(
                    currentPosition == 0
                        ? R.img_bottombar_btn_home_pre
                        : R.img_bottombar_icon_home_nor,
                    width: 25,
                    height: 25,
                  ),
                  title: Text("首页")),
              BottomNavigationBarItem(
                  icon: Image.asset(
                    currentPosition == 1
                        ? R.img_bottombar_btn_file_pre
                        : R.img_bottombar_icon_file_nor,
                    width: 25,
                    height: 25,
                  ),
                  title: Text("文件")),
              BottomNavigationBarItem(
                  icon: Image.asset(
                    currentPosition == 2
                        ? R.img_bottombar_icon_shared_pre
                        : R.img_bottombar_icon_shared_nor,
                    width: 25,
                    height: 25,
                  ),
                  title: Text("共享")),
              BottomNavigationBarItem(
                  icon: Image.asset(
                    currentPosition == 3
                        ? R.img_bottombar_btn_discovery_pre
                        : R.img_bottombar_icon_discovery_nor,
                    width: 25,
                    height: 25,
                  ),
                  title: Text("发现")),
              BottomNavigationBarItem(
                  icon: Icon(
                    Icons.accessibility,
                    color: currentPosition == 4 ? Colors.blue : Colors.grey,
                  ),
                  title: Text("我的")),
            ],
            onTap: (position) {
              currentPosition = position;
              bodyWidget = getWidget(currentPosition);
              setState(() {});
            },
            type: BottomNavigationBarType.fixed,
            selectedItemColor: Colors.blue,
            unselectedItemColor: Colors.grey,
            showSelectedLabels: true,
            showUnselectedLabels: true,
            currentIndex: currentPosition,
            backgroundColor: Colors.white,
            elevation: 0,
          ),
        ),
      ),
    );
  }

  Widget getWidget(int currentPosition) {
    switch (currentPosition) {
      case 0:
        {
          if (homePage == null) {
            homePage = HomePage();
          }
          return homePage;
        }
        break;
      case 1:
        {
          if (filePage == null) {
            filePage = FilePage();
          }
          return filePage;
        }
        break;
      case 2:
        {
          if (sharePage == null) {
            sharePage = SharePage();
          }
          return sharePage;
        }
        break;
      case 3:
        {
          if (findPage == null) {
            findPage = FindPage();
          }
          return findPage;
        }
        break;
      case 4:
        {
          if (minePage == null) {
            minePage = MinePage();
          }
          return minePage;
        }
        break;
    }
  }
}
