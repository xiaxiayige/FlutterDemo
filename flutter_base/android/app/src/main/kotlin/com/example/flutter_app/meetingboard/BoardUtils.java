package com.example.flutter_app.meetingboard;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Base64;


import org.apache.poi.poifs.filesystem.DocumentInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class BoardUtils {

    public boolean WriteBytesToStream(byte[] buffer, OutputStream outputStream) {
        try {
            outputStream.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  true;
    }

    public boolean WriteUByteToStream(short value, OutputStream outputStream)    //save BYTE   char
    {//判断是否越界
        try {
            int ByteWidth = 1;
            byte[] arr = new byte[ByteWidth];
            arr[0] = (byte) ((value) & 0xff);
            outputStream.write(arr);
        } catch (IOException e) {

        }
        return true;
    }

    public boolean WriteUShortToStream(int value, OutputStream outputStream)     //save WORD    unsigned short
    {//判断是否越界
        try {
            int ByteWidth = 2;
            byte[] arr = new byte[ByteWidth];
            arr[0] = (byte) ((value) & 0xff);
            arr[1] = (byte) ((value >> 8) & 0xff);
            outputStream.write(arr);
        } catch (IOException e) {

        }
        return true;
    }

    public boolean WriteColorToStream(int colorvalue, OutputStream outputStream) {
        try {
            int ByteWidth = 4;
            byte[] arr = new byte[ByteWidth];

            arr[0] = (byte) ((colorvalue) & 0xff);
            arr[1] = (byte) ((colorvalue >> 8) & 0xff);
            arr[2] = (byte) ((colorvalue >> 16) & 0xff);
            arr[3] = 0;//(byte) ((colorvalue >> 24) & 0xff);*/

            outputStream.write(arr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean WriteUIntToStream(long value, OutputStream outputStream)    //save DWORD  unsigned  long
    {//判断是否越界
        try {
            int ByteWidth = 4;

            byte[] arr = new byte[ByteWidth];
            arr[0] = (byte) ((value) & 0xff);
            arr[1] = (byte) ((value >> 8) & 0xff);
            arr[2] = (byte) ((value >> 16) & 0xff);
            arr[3] = (byte) ((value >> 24) & 0xff);
            outputStream.write(arr);
        } catch (IOException e) {

        }
        return true;
    }

    public boolean WriteStringToStream(String value, OutputStream outputStream) {
        //先保存字符个数，然后保存字符串
        try {
            byte[] originBuf = value.getBytes("GB2312");
            int icharcount = originBuf.length;
            byte[] sbuff = new byte[icharcount + 1];
            System.arraycopy(originBuf, 0, sbuff, 0, icharcount);
            int ilen = sbuff.length;
            WriteUShortToStream(ilen, outputStream);
            outputStream.write(sbuff);

        } catch (IOException e) {

        }
        return true;
    }

    //将文件写入到outputstream
    public boolean SaveFileToStream(String sFile, OutputStream outputStream) {
        try {
            File file = new File(sFile);
            if (!file.exists()) return false;
            FileInputStream fi = new FileInputStream(file);
            int iReadMax = 10 * 1024;
            byte[] readbuf = new byte[iReadMax];
            int iread = fi.read(readbuf);
            while (iread > 0) {
                outputStream.write(readbuf);
                iread = fi.read(readbuf);
            }
            fi.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //读stream写到文件中
    public boolean SaveStreamToFile(DocumentInputStream inputStream, String sFile) {

        try {
            File file = new File(sFile);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            int iReadMax = 10 * 1024;
            byte[] readbuf = new byte[iReadMax];
            int iread = inputStream.read(readbuf);
            while (iread > 0) {
                fos.write(readbuf, 0, iread);
                iread = inputStream.read(readbuf);
            }
            fos.close();

        } catch (IOException e) {
            return false;
        }
        return true;
    }


    //先读取长度，再获取字节数据，然后转换为字符串
    public String ReadStringFromStream(DocumentInputStream inputStream) {

        String sRet = "";
        try {
            int iLen = inputStream.readUShort();
            byte[] buffer = new byte[iLen];
            inputStream.read(buffer);
            sRet = new String(buffer);
            //删除字符串末尾的/0字符
            sRet = sRet.replaceAll("\0", "");
        } catch (IOException e) {
        }
        return sRet;
    }


    public Rect ReviseRect(Rect rs) {
        Rect rd = new Rect();
        rd.left = Math.min(rs.left, rs.right);
        rd.right = Math.max(rs.left, rs.right);
        rd.top = Math.min(rs.top, rs.bottom);
        rd.bottom = Math.max(rs.top, rs.bottom);
        return rd;
    }


    Rect OutlineRect(Rect rs, int offset) {
        Rect rd = new Rect(rs);
        if (rd != null) {
            rd.left = rd.left - offset;
            rd.top = rd.top - offset;
            rd.right = rd.right + offset;
            rd.bottom = rd.bottom + offset;
        }
        return rd;
    }

    //判断点是否在矩形区域内
    public boolean InRect(Rect rect, int x, int y) {
        boolean bret = false;
        if ((x >= rect.left) && (x <= rect.right) && (y >= rect.top) && (y <= rect.bottom)) {
            bret = true;
        }
        return bret;
    }

    //获取文件扩展名,带.
    public String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot);
            }
        }
        return "";
    }

    public String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }


    public String getFileNameWithSuffix(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        int start = path.lastIndexOf("/");
        if (start != -1) {
            return path.substring(start + 1);
        } else {
            return "";
        }
    }

    //判读文件是否存在
    public boolean FileExists(String sPath) {
        File testFile = new File(sPath);
        if (testFile.exists()) return true;
        else return false;
    }

    public boolean RenameFile(String originFile, String targetFile) {
        try {
            File file = new File(originFile);
            file.renameTo(new File(targetFile));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean CopyFile(String sourcename, String targetname) {
        boolean bRet = false;
        File sourceFile = new File(sourcename);
        if (!sourceFile.exists()) return false;
        File targetFile = new File(targetname);
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(sourceFile).getChannel();
            outputChannel = new FileOutputStream(targetFile).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            inputChannel.close();
            outputChannel.close();
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
            bRet = false;
        }
        return bRet;

    }

    public boolean DeleteFile(String szfile) {

        try {
            File file = new File(szfile);
            if (file.exists()) {
                file.delete();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除文件夹和文件夹里面的文件
    public  static  void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public int GetARGB(int rgbValue) {
        int alpha = 255;
        int red = (int) (rgbValue & (0xff));
        int green = (int) ((rgbValue >> 8) & (0xff));
        int blue = (int) ((rgbValue >> 16) & (0xff));
        return Color.argb(255, red, green, blue);

    }

    public int GetRGB(int argbValue) {
        int red = Color.red(argbValue);
        int green = Color.green(argbValue);
        int blue = Color.blue(argbValue);
        return blue << 16 | green << 8 | red;

    }


    //
    double determinant(double v1, double v2, double v3, double v4)  // 行列式
    {
        return (v1 * v3 - v2 * v4);
    }

    //判断两条线段是否有交点
    public boolean intersect3(Point aa, Point bb, Point cc, Point dd) {
        double delta = determinant(bb.x - aa.x, dd.x - cc.x, dd.y - cc.y, bb.y - aa.y);
        if (delta <= (1e-6) && delta >= -(1e-6))  // delta=0，表示两线段重合或平行
        {
            return false;
        }
        double namenda = determinant(dd.x - cc.x, aa.x - cc.x, aa.y - cc.y, dd.y - cc.y) / delta;
        if (namenda > 1 || namenda < 0) {
            return false;
        }
        double miu = determinant(bb.x - aa.x, aa.x - cc.x, aa.y - cc.y, bb.y - aa.y) / delta;
        if (miu > 1 || miu < 0) {
            return false;
        }
        return true;
    }

    public int ipToInt(String ipAddr) {
        byte[] iparr = ipToBytes(ipAddr);
        int iret = bytesToInt(iparr);
        return iret;
    }


    public byte[] ipToBytes(String ipAddr) {
        //初始化字节数组，定义长度为4
        byte[] ret = new byte[4];
        try {
            //使用关键字"." 分割字符串数组
            String[] ipArr = ipAddr.split("\\.");

            //将字符串数组依次写入字节数组
            ret[0] = (byte) (Integer.parseInt(ipArr[0]));
            ret[1] = (byte) (Integer.parseInt(ipArr[1]));
            ret[2] = (byte) (Integer.parseInt(ipArr[2]));
            ret[3] = (byte) (Integer.parseInt(ipArr[3]));
            return ret;
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid IP : " + ipAddr);
        }
    }

    /**
     * 根据位运算把 byte[] -> int
     * <p>
     * 原理：将每个字节强制转化为8位二进制码，然后依次左移8位，对应到Int变量的4个字节中
     *
     * @param bytes
     * @return int
     */
    public int bytesToInt(byte[] bytes) {
        int addr = 0;               //初始化Int变量addr=0
        addr |= (bytes[0] & 0xFF);  //强制转化为8位二进制码，比如原码是101，强转后00000101
        addr = addr << 8;           //左移8位，得到00000101 00000000，给下个字节的拼接创造环境（预留8位0，方便用|进行拼接）
        addr |= (bytes[1] & 0xFF);   //强制转化为8位二进制码，比如原码是10101，强转后00010101,和00000101 00000000进行或运算后得到00000101 00010101
        addr = addr << 8;           //左移8位，得到00000101 00010101 00000000
        addr |= (bytes[2] & 0xFF);  //强制转化为8位二进制码，比如原码是111，强转后00000111,和00000101 00010101 00000000进行或运算后得到00000101 00010101 00000111
        addr = addr << 8;           //左移8位，得到00000101 00010101 00000111 00000000
        addr |= ((bytes[3]) & 0xFF);//强制转化为8位二进制码，比如原码是1，强转后00000001,和00000101 00010101 00000111 00000000进行或运算后得到00000101 00010101 00000111 00000001
        return addr;                //拼接结束，返回int变量

//      优化之后的写法，原理相同，不过是先移位后直接强转的同时指定位数
//      int addr = bytes[3] & 0xFF;
//      addr |= ((bytes[2] << 8) & 0xFF00);
//      addr |= ((bytes[1] << 16) & 0xFF0000);
//      addr |= ((bytes[0] << 24) & 0xFF000000);
//      return addr;

    }


    /**
     * 把int->string地址
     *
     * @param ipInt
     * @return String
     */
    public static String intToIp(int ipInt) {
        return new StringBuilder()
                .append(((ipInt >> 24) & 0xFF)).append('.')   //右移3个字节（24位），得到IP地址的第一段也就是int变量的第一个字节（从左边算起）
                .append((ipInt >> 16) & 0xFF).append('.')     //右移2字节（16位），得到int变量的第一和第二个字节（从左边算起），经过&0xFF处理得到后8位也就是byte[1]
                .append((ipInt >> 8) & 0xFF).append('.')      //同理如上
                .append((ipInt & 0xFF))                       //同理如上
                .toString();
    }

    //md5 字符串md5
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String Base64Encode(String str) {
        String sRet = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
        return sRet;
    }

    public static String Base64Decode(String str) {
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
    }

    public static String Base64Decode(String str, String charsetName) {
        try {
            byte[] buffer = Base64.decode(str.getBytes(), Base64.DEFAULT);
            String sret = new String(buffer, "GB2312");
            return sret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

/*
    public byte[] ToBytes(boolean bValue) {
        byte[] retBuf = new byte[1];
        BoardByteBuf boardtool = new BoardByteBuf();
        boardtool.AssignWriteBuf(retBuf);
        short sValue = (short) (bValue ? 1 : 0);
        boardtool.WriteUByte(sValue);
        return retBuf;
    }

    public byte[] ToBytes(short sValue) {
        byte[] retBuf = new byte[1];
        BoardByteBuf boardtool = new BoardByteBuf();
        boardtool.AssignWriteBuf(retBuf);
        boardtool.WriteUByte(sValue);
        return retBuf;
    }

    public byte[] ToBytes(int iValue) {
        byte[] retBuf = new byte[2];
        BoardByteBuf boardtool = new BoardByteBuf();
        boardtool.AssignWriteBuf(retBuf);
        boardtool.WriteUShort(iValue);
        return retBuf;
    }

    public byte[] ToBytes(int dataType, long lValue) {
        int bufferSize = 0;
        byte[] retBuf;
        BoardByteBuf boardtool = new BoardByteBuf();
        switch (BoardConstants.ByteDataType.values()[dataType]) {
            case bdtBool:
            case bdtChar:
                bufferSize = 1;
                retBuf = new byte[bufferSize];
                boardtool.WriteUByte((short) lValue);
                break;
            case bdtInt16:
            case bdtUInt16:
                bufferSize = 2;
                retBuf = new byte[bufferSize];
                boardtool.WriteUShort((int) lValue);
                break;
            case bdtInt32:
            case bdtUInt32:
                bufferSize = 4;
                retBuf = new byte[bufferSize];
                boardtool.WriteUint(lValue);
                break;
            case bdtInt64:
            case bdtUInt64:
                bufferSize = 8;
                retBuf = new byte[bufferSize];
                boardtool.WriteLong(lValue);
                break;
            case bdtDouble:
                bufferSize = 8;
                retBuf = new byte[bufferSize];
                boardtool.WriteDouble(lValue);
                break;
            case bdtDateTime:
                bufferSize = 8;
                break;
            case bdtIP:
                buffersize = 6;
                break;
            default:
                break;
        }
        byte[] ret = new byte[buffersize];

    }*/
}
