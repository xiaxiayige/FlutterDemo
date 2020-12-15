import 'package:flutter_app/common/json_to_model.dart';
import 'package:flutter_app/constant.dart';

class Result<T> {
  dynamic errCode;
  String errMsg;
  T data;

  Result({this.errCode, this.errMsg, this.data});

  Result.fromJson(Map<String, dynamic> resultData) {
    errCode = resultData['errCode'];
    errMsg = resultData['errMsg'];
    if(errCode.toString() ==REQUEST_OK){
      var json2 = resultData["data"];
      // data = JsonToBean.converToBean<T>(json2);
      data = json2;
    }else{
      Result.error(errCode.toString(), errMsg);
    }
  }

  Result.error(String code, String message) {
    errCode= code;
    errMsg = message;
  }

  @override
  String toString() {
    return 'Result{errCode: $errCode, errMsg: $errMsg, data: $data}';
  }
}
