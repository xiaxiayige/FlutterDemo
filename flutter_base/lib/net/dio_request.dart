import 'package:dio/dio.dart';
import 'package:flutter_app/bean/login_result_bean.dart';
import 'package:flutter_app/bean/responsebody.dart';
import 'package:flutter_app/constant.dart';
import 'package:flutter_app/net/logging_interceptors.dart';

/**
 *
 * 网络请求-dio
 */
class RequesUtils {
  static final RequesUtils _singleton = RequesUtils._internal();

  static Dio _dio;

  factory RequesUtils() {
    return _singleton;
  }

  RequesUtils._internal() {
    _dio = Dio(
      BaseOptions(
        baseUrl: BASEURL,
        connectTimeout: 30000,
      ),
    )..interceptors.add(LogInterceptors());
  }

  Future<Result<T>> get<T>(String urlPath, Map<String, dynamic> map) async {
    Response response = await _dio.get(urlPath, queryParameters: map);
    if (response.statusCode == 200) {
      return Result<T>.fromJson(response.data);
    } else {
      return Result<T>.error(response.statusCode.toString(), response.statusMessage);
    }
  }

  Future<Result> post<T>(String urlPath, Map<String, dynamic> map) async {
    Response response = await _dio.post(urlPath, queryParameters: map);
    if (response.statusCode == 200) {
      return Result<T>.fromJson(response.data);
    } else {
      return Result.error(response.statusCode.toString(), response.statusMessage);
    }
  }

}
