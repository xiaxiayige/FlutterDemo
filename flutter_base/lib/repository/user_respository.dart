import 'package:flutter_app/api/api_impl.dart';
import 'package:flutter_app/api/result_callback.dart';
import 'package:flutter_app/bean/login_result_bean.dart';
import 'package:flutter_app/bean/responsebody.dart';

typedef FunctionResult<T>(
  T t,
);

/**
 * 用户相关
 */
class UserRespository {

  static Future login(Map<String, dynamic> queryParameters,ResultOk<LoginResultBean> resultOk, ResultFail resultFail) {
    Api().login(queryParameters).then((value) {
      if (value.data != null) {
        resultOk(value.data);
      } else {
        resultFail(value.errCode.toString(), value.errMsg);
      }
    });
  }


  static Stream<Result<LoginResultBean>> login2(Map<String, dynamic> queryParameters) {
    return Api().login(queryParameters).asStream();
  }
}
