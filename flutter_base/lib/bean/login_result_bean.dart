import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';

/// token : "NTI5NjkuMTYwOTA1ODQ1Mi5kZDk0OTNkODk3MjZhOTYyOTU0NThmMWFlZmY5ZWFkYQ"
/// userinfo : {"uid":52969,"username":"csxuesheng07","realname":"小七","portrait":"http://www.guanxinqiao.com/images/portrait.png","sex":2,"school":{"schoolid":1,"schoolname":"关心桥网校"},"classroom":{"roomid":462,"roomname":"2班"}}

class LoginResultBean with ChangeNotifier  {
 static LoginResultBean instance = LoginResultBean();

  void update(LoginResultBean bean){
    _token = bean.token;
    _userinfo = bean.userinfo;
    notifyListeners();
  }
  void clear(){
    _token = null;
    _userinfo = null;
    notifyListeners();
  }

  String _token;
  Userinfo _userinfo;

  String get token => _token;
  Userinfo get userinfo => _userinfo;

  LoginResultBean({
      String token, 
      Userinfo userinfo}){
    _token = token;
    _userinfo = userinfo;
}

  LoginResultBean.fromJson(Map<String,dynamic> json) {
    _token = json["token"];
    _userinfo = json["userinfo"] != null ? Userinfo.fromJson(json["userinfo"]) : null;
  }

  Map<String, dynamic> toJson() {
    var map = <String, dynamic>{};
    map["token"] = _token;
    if (_userinfo != null) {
      map["userinfo"] = _userinfo.toJson();
    }
    return map;
  }

}

/// uid : 52969
/// username : "csxuesheng07"
/// realname : "小七"
/// portrait : "http://www.guanxinqiao.com/images/portrait.png"
/// sex : 2
/// school : {"schoolid":1,"schoolname":"关心桥网校"}
/// classroom : {"roomid":462,"roomname":"2班"}

class Userinfo with ChangeNotifier{
  int _uid;
  String _username;
  String _realname;
  String _portrait;
  int _sex;
  School _school;
  Classroom _classroom;

  int get uid => _uid;
  String get username => _username;
  String get realname => _realname;
  String get portrait => _portrait;
  int get sex => _sex;
  School get school => _school;
  Classroom get classroom => _classroom;

  Userinfo({
      int uid, 
      String username, 
      String realname, 
      String portrait, 
      int sex, 
      School school, 
      Classroom classroom}){
    _uid = uid;
    _username = username;
    _realname = realname;
    _portrait = portrait;
    _sex = sex;
    _school = school;
    _classroom = classroom;
}

  Userinfo.fromJson(dynamic json) {
    _uid = json["uid"];
    _username = json["username"];
    _realname = json["realname"];
    _portrait = json["portrait"];
    _sex = json["sex"];
    _school = json["school"] != null ? School.fromJson(json["school"]) : null;
    _classroom = json["classroom"] != null ? Classroom.fromJson(json["classroom"]) : null;
  }

  Map<String, dynamic> toJson() {
    var map = <String, dynamic>{};
    map["uid"] = _uid;
    map["username"] = _username;
    map["realname"] = _realname;
    map["portrait"] = _portrait;
    map["sex"] = _sex;
    if (_school != null) {
      map["school"] = _school.toJson();
    }
    if (_classroom != null) {
      map["classroom"] = _classroom.toJson();
    }
    return map;
  }

}

/// roomid : 462
/// roomname : "2班"

class Classroom {
  int _roomid;
  String _roomname;

  int get roomid => _roomid;
  String get roomname => _roomname;

  Classroom({
      int roomid, 
      String roomname}){
    _roomid = roomid;
    _roomname = roomname;
}

  Classroom.fromJson(dynamic json) {
    _roomid = json["roomid"];
    _roomname = json["roomname"];
  }

  Map<String, dynamic> toJson() {
    var map = <String, dynamic>{};
    map["roomid"] = _roomid;
    map["roomname"] = _roomname;
    return map;
  }

}

/// schoolid : 1
/// schoolname : "关心桥网校"

class School {
  int _schoolid;
  String _schoolname;

  int get schoolid => _schoolid;
  String get schoolname => _schoolname;

  School({
      int schoolid, 
      String schoolname}){
    _schoolid = schoolid;
    _schoolname = schoolname;
}

  School.fromJson(dynamic json) {
    _schoolid = json["schoolid"];
    _schoolname = json["schoolname"];
  }

  Map<String, dynamic> toJson() {
    var map = <String, dynamic>{};
    map["schoolid"] = _schoolid;
    map["schoolname"] = _schoolname;
    return map;
  }

}