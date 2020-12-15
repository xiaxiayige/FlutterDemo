import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_app/bean/login_result_bean.dart';
import 'package:flutter_app/bloc/user_bloc.dart';
import 'package:flutter_app/repository/user_respository.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:oktoast/oktoast.dart';
import 'package:provider/provider.dart';


void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(
          create: (_) => Userinfo(),
        ),
        ChangeNotifierProvider(
          create: (_) => LoginResultBean(),
        ),
      ],
      child: MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          primarySwatch: Colors.blue,
          visualDensity: VisualDensity.adaptivePlatformDensity,
        ),
        home: OKToast(
          child: Body(),
        ),
      ),
    );
  }
}

class Body extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<Body> {
  MethodChannel _methodChannel = new MethodChannel(
    "test-Method",
  );
  var count = 0;
  @override
  void initState() {
    super.initState();
  }
  @override
  Widget build(BuildContext context) {
    count++;
    return SafeArea(
      child: Scaffold(
        appBar: AppBar(
          title: Text("Title $count"),
        ),
        body: Column(
          children: [
            RaisedButton(
                onPressed: () {
                  _methodChannel.invokeMethod("test-Method");
                  setState(() {

                  });
                },
                child: Text("看视频")),
            RaisedButton(
              child: Text("HttpRequest"),
              onPressed: () {
              },
            ),
            SizedBox(
              height: 20,
            ),
            // StreamBuilder<Result<LoginResultBean>>(builder: (BuildContext context, AsyncSnapshot<Result<LoginResultBean>> snapshot){
            //   if(snapshot.hasData){
            //     return Text("loading end...${snapshot.data.data.token}");
            //   }else{
            //     return Text("Loading...");
            //   }
            // },
            //   stream: UserRespository.login2({"username": "csxuesheng06", "password": 123456})
            // ),
            MyUserInfo(),
          ],
        ),
      ),
    );
  }




}

class MyUserInfo extends StatelessWidget {
  String userName;
  var count=0;
  LoginBloc loginBloc =LoginBloc();

  MyUserInfo({this.userName});
  @override
  Widget build(BuildContext context) {
    count++;
    LoginResultBean userInfo = Provider.of<LoginResultBean>(context);
    return Column(
      children: [
        RaisedButton(
          onPressed: (){
            _login(context,userInfo);
          },
          child: Text("Click Me"),
        ),
        Consumer<LoginResultBean>(
          builder: (BuildContext context, LoginResultBean value, Widget child){
            return Text("userName =  ${value?.userinfo?.username} $count");
          },
        ),
        Text("userName =  ${userInfo?.userinfo?.username} $count"),
        RaisedButton(
          child: Text("login3"),
          onPressed: (){
            // Provider.of<LoginResultBean>(context, listen: false).clear();
            loginBloc.login3();
          },
        ),
        StreamBuilder<String>(
          builder: (BuildContext context, AsyncSnapshot<String> snapshot){
            if(!snapshot.hasData){
              return Text("loading ...");
            }
            return Text("login 3 userName = ${snapshot.data}");
          },
          stream:loginBloc.loginUserNameObservable,
        ),

        StreamBuilder<String>(
          builder: (BuildContext context, AsyncSnapshot<String> snapshot){
            return Text("login 2 userName = ${snapshot.data}");
          },
          stream:loginBloc.login2(),
        ),

      ],
    );
  }

  /***
   * 登录方法
   */
  void _login(BuildContext context,LoginResultBean userInfo) {
    UserRespository.login({"username": "csxuesheng06", "password": 123456},
            (data) {
          print("ok 登录成功");
          userInfo.update(data);
        }, (code, msg) {
          Fluttertoast.showToast(msg: msg, toastLength: Toast.LENGTH_SHORT);
        });
  }
}

