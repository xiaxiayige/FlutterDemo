import 'package:dio/dio.dart';

class LogInterceptors extends Interceptor{

  @override
  Future onRequest(RequestOptions options) {
    print("http:request start");
    print("http:request url              ===> ${options.uri.toString()}");
    print("http:request requestBody      ===> ${options.data}");
    return super.onRequest(options);
  }

  @override
  Future onResponse(Response response) {
    print("http:response statusCode      ===> ${response.statusCode}");
    print("http:response data            ===> ${response.data}");
    print("http:response end");
    return super.onResponse(response);
  }

  @override
  Future onError(DioError err) {
    print("request error => ${err.toString()}");
    return super.onError(err);
  }
}