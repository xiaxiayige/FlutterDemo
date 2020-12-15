
import 'package:flutter_app/api/result_callback.dart';
import 'package:flutter_app/bean/login_result_bean.dart';
import 'package:flutter_app/repository/user_respository.dart';
import 'package:rxdart/rxdart.dart';


class LoginBloc{
  BehaviorSubject<LoginResultBean> _behaviorsubject;

  BehaviorSubject<String> _behaviorSubjectForUserName;

  BehaviorSubject<LoginResultBean> get loginObservable => _behaviorsubject;

  BehaviorSubject<String> get loginUserNameObservable => _behaviorSubjectForUserName;

  LoginBloc(){
    _behaviorsubject = BehaviorSubject<LoginResultBean>();
    _behaviorSubjectForUserName = BehaviorSubject<String>();
  }

  void login(){
     UserRespository.login({"username":"csxuesheng01","password":123456},(data){
       print("=====$data");
     },null);
  }

  void  login3(){
     UserRespository.login({"username":"csxuesheng03","password":123456},(data){
       _behaviorSubjectForUserName.sink.add(data.userinfo.username);
     },null);
  }

  Stream<String> login2(){
    return UserRespository.login2({"username":"csxuesheng02","password":123456}).map<String>((event) =>
    event.data.userinfo.username
    );
  }

  void disop(){
    _behaviorsubject.close();
  }
}