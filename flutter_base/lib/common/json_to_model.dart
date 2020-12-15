import 'package:flutter_app/bean/login_result_bean.dart';

abstract class JsonToBean {
  static T converToBean<T>(Map<String,dynamic> json) {
    // print("map convert to bean , bean map = $json");
    if (T == LoginResultBean) {
      return LoginResultBean.fromJson(json) as T;
    }
  }
}
