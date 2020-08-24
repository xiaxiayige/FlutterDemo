class CardInfo {
  String img;
  String title;
  String desc;
  CardInfo(this.img, this.title, this.desc);
}

class FileFolderBean {
  String fileName; //文件名
  bool isSystem;
  String updateTime; //文件夹创建/更新时间
  FileFolderBean({this.fileName, this.isSystem=false,this.updateTime=""}); //是否系统文件夹


}
