import 'package:dio/dio.dart';
import 'package:flutter_app/api/api_service.dart';
import 'package:flutter_app/bean/login_result_bean.dart';
import 'package:flutter_app/bean/responsebody.dart';
import 'package:flutter_app/constant.dart';
import 'package:flutter_app/net/dio_request.dart';
import 'package:rxdart/rxdart.dart';

class Api {
  RequesUtils requesUtils = RequesUtils();
  /***
   * 登录
   */
  Future<Result<LoginResultBean>> login(Map<String, dynamic> queryParameters) {
    return requesUtils.get<LoginResultBean>(API_SERVICE.USER_LOGIN, queryParameters);
  }

}
