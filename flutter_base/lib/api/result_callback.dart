//成功
typedef ResultOk<T> = void Function(T t);
//失败
typedef ResultFail = void Function(String errorCode, String errMsg);