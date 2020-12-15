package com.example.flutter_app.meetingboard;

import android.graphics.Rect;

public class BoardByteBuf {

    private byte[] m_buffer;
    private int readpos;
    private int writepos;

    public int GetReadPos() {
        return readpos;
    }

    public int GetWritePos() {
        return writepos;
    }

    public boolean AssignReadBuf(byte[] sourcebuf) {
        int startpos = 0;
        return AssignReadBuf(sourcebuf, startpos);
    }

    public boolean AssignReadBuf(byte[] sourcebuf, int startpos) {
        if (startpos >= sourcebuf.length) return false;
        m_buffer = sourcebuf;
        readpos = startpos;
        return true;
    }

    public boolean AssignWriteBuf(byte[] sourcebuf) {
        int startpos = 0;
        return AssignWriteBuf(sourcebuf, startpos);
    }

    public boolean AssignWriteBuf(byte[] sourcebuf, int startpos) {
        if (startpos >= sourcebuf.length) return false;
        m_buffer = sourcebuf;
        writepos = startpos;
        return true;
    }


    public short ReadUByte() {
        short ret = 0;
        if (readpos < m_buffer.length) ret = (short) (m_buffer[readpos] & 0xff);
        readpos++;
        return ret;
    }

    public short ReadShort(){
        short ret = 0;
        if (readpos + 1 < m_buffer.length) {
            byte b0 = m_buffer[readpos];
            byte b1 = m_buffer[readpos + 1];
            short s0=(short)(b0 & 0xff);
            short s1=(short)(b1 & 0xff);
            ret = (short)( s1<<8 | s0);
        }
        readpos += 2;
        return ret;

    }

    public int ReadUShort() {
        int ret = 0;
        if (readpos + 1 < m_buffer.length) {
            byte b0 = m_buffer[readpos];
            byte b1 = m_buffer[readpos + 1];
            short s0 = (short) (b0 & 0xff);
            short s1 = (short) (b1 & 0xff);
            ret =(int) ((s1 << 8) | s0);
        }
        readpos += 2;
        return ret;
    }

    public int ReadInt() {
        int ret = 0;
        if (readpos + 3 < m_buffer.length) {
            byte b0 = m_buffer[readpos];
            byte b1 = m_buffer[readpos + 1];
            byte b2 = m_buffer[readpos + 2];
            byte b3 = m_buffer[readpos + 3];
            short s0 = (short) (b0 & 0xff);
            short s1 = (short) (b1 & 0xff);
            short s2 = (short) (b2 & 0xff);
            short s3 = (short) (b3 & 0xff);
            ret =(int)((s3 << 24) | (s2 << 16) | (s1 << 8) | s0);
        }
        readpos += 4;
        return ret;
    }

    public long ReadLong() {
        long ret = 0;
        if (readpos + 7 < m_buffer.length) {
            byte b0 = m_buffer[readpos];
            byte b1 = m_buffer[readpos + 1];
            byte b2 = m_buffer[readpos + 2];
            byte b3 = m_buffer[readpos + 3];
            byte b4 = m_buffer[readpos + 4];
            byte b5 = m_buffer[readpos + 5];
            byte b6 = m_buffer[readpos + 6];
            byte b7 = m_buffer[readpos + 7];

            short s0 = (short) (b0 & 0xff);
            short s1 = (short) (b1 & 0xff);
            short s2 = (short) (b2 & 0xff);
            short s3 = (short) (b3 & 0xff);
            short s4 = (short) (b4 & 0xff);
            short s5 = (short) (b5 & 0xff);
            short s6 = (short) (b6 & 0xff);
            short s7 = (short) (b7 & 0xff);

            ret = (long)((s7 << (7 * 8)) | (s6 << (6 * 8)) | (s5 << (5 * 8)) | (s4 << (4 * 8)) | (s3 << 24) | (s2 << 16) | (s1 << 8) | s0);
        }
        readpos += 8;
        return ret;
    }

    public long ReadUInt() {
        long  ret = 0;
        if (readpos + 3 < m_buffer.length) {
            byte b0 = m_buffer[readpos];
            byte b1 = m_buffer[readpos + 1];
            byte b2 = m_buffer[readpos + 2];
            byte b3 = m_buffer[readpos + 3];
            short s0 = (short) (b0 & 0xff);
            short s1 = (short) (b1 & 0xff);
            short s2 = (short) (b2 & 0xff);
            short s3 = (short) (b3 & 0xff);
            ret =(long) ((s3 << 24) | (s2 << 16) | (s1 << 8) | s0);
        }
        readpos += 4;
        return ret;
    }

    public Rect ReadRect() {
        Rect ret = new Rect(0, 0, 0, 0);
        if (readpos + 15 < m_buffer.length) {
            ret.left = (int) ReadUInt();
            ret.top = (int) ReadUInt();
            ret.right = (int) ReadUInt();
            ret.bottom = (int) ReadUInt();
        }

        return ret;
    }

    public byte[] ReadByteArr(int ilen) {
        byte[] ret = null;
        if (readpos + ilen - 1 < m_buffer.length) {
            ret = new byte[ilen];
            System.arraycopy(m_buffer, readpos, ret, 0, ilen);
            readpos += ilen;
        }
        return ret;
    }

    //先读取字符数，在根据字符数返回字符串
    public String ReadString() {
        String ret = "";
        try {
            int iLen = ReadUByte();
            if (iLen > 0) {
                byte[] buffer = ReadByteArr(iLen);
                if (buffer != null) {
                    ret = new String(buffer, "GB2312");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public double ReadDouble() {
        long lvalue = ReadLong();
        double dread = Double.longBitsToDouble(lvalue);
        return dread;
    }

    public boolean WriteDouble(double dvalue) {
        if (writepos + 7 < m_buffer.length) {
            long lvalue = Double.doubleToLongBits(dvalue);
            WriteLong(lvalue);
            return true;
        }
        return false;
    }

    public boolean WriteBool(boolean bv) {
        short sValue = (short) (bv ? 1 : 0);
        return WriteUByte(sValue);
    }

    public boolean WriteUByte(short value) {
        if (writepos < m_buffer.length) {
            m_buffer[writepos] = (byte) ((value) & 0xff);
            writepos++;
            return true;
        }
        return false;
    }

    public boolean WriteUShort(int value) {
        if (writepos + 1 < m_buffer.length) {
            m_buffer[writepos] = (byte) ((value) & 0xff);
            m_buffer[writepos + 1] = (byte) ((value >> 8) & 0xff);
            writepos += 2;
            return true;
        }
        return false;
    }

    public boolean WriteShort(short value) {
        if (writepos + 1 < m_buffer.length) {
            m_buffer[writepos] = (byte) ((value) & 0xff);
            m_buffer[writepos + 1] = (byte) ((value >> 8) & 0xff);
            writepos += 2;
            return true;
        }
        return false;
    }

    public boolean WriteInt(int value) {
        if (writepos + 3 < m_buffer.length) {
            m_buffer[writepos] = (byte) ((value) & 0xff);
            m_buffer[writepos + 1] = (byte) ((value >> 8) & 0xff);
            m_buffer[writepos + 2] = (byte) ((value >> 16) & 0xff);
            m_buffer[writepos + 3] = (byte) ((value >> 24) & 0xff);
            writepos += 4;
            return true;
        }
        return false;
    }

    public boolean WriteUint(long value) {
        if (writepos + 3 < m_buffer.length) {
            m_buffer[writepos] = (byte) ((value) & 0xff);
            m_buffer[writepos + 1] = (byte) ((value >> 8) & 0xff);
            m_buffer[writepos + 2] = (byte) ((value >> 16) & 0xff);
            m_buffer[writepos + 3] = (byte) ((value >> 24) & 0xff);
            writepos += 4;
            return true;
        }
        return false;
    }

    public boolean WriteLong(long value) {
        if (writepos + 7 < m_buffer.length) {
            m_buffer[writepos] = (byte) ((value) & 0xff);
            m_buffer[writepos + 1] = (byte) ((value >> 8) & 0xff);
            m_buffer[writepos + 2] = (byte) ((value >> 8 * 2) & 0xff);
            m_buffer[writepos + 3] = (byte) ((value >> 8 * 3) & 0xff);
            m_buffer[writepos + 4] = (byte) ((value >> 8 * 4) & 0xff);
            m_buffer[writepos + 5] = (byte) ((value >> 8 * 5) & 0xff);
            m_buffer[writepos + 6] = (byte) ((value >> 8 * 6) & 0xff);
            m_buffer[writepos + 7] = (byte) ((value >> 8 * 7) & 0xff);
            writepos += 8;
            return true;
        }
        return false;
    }

    public boolean WriteByteArr(byte[] value) {
        if (writepos + value.length - 1 < m_buffer.length) {
            System.arraycopy(value, 0, m_buffer, writepos, value.length);
            writepos += value.length;
            return true;
        }
        return false;
    }

    public boolean WriteString(String value) {
        try {
            byte[] originBuf = value.getBytes("GB2312");
            int icharcount = originBuf.length;
            byte[] sbuff = new byte[icharcount + 1];
            System.arraycopy(originBuf, 0, sbuff, 0, icharcount);
         /*   int len = sbuff.length;
            if (WriteUByte((short) len) == false) return false;*/

            return WriteByteArr(sbuff);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //先保存字符串长度，再保存字符串
    // 字符串保存时，带\0结束，兼容c++
    public boolean WriteStringZeroTail(String value)
    {
        try {
            byte[] originBuf = value.getBytes("GB2312");
            int icharcount = originBuf.length;
            byte[] sbuff = new byte[icharcount + 1];
            System.arraycopy(originBuf, 0, sbuff, 0, icharcount);
            int len = sbuff.length;
              WriteUByte((short) len);
            return WriteByteArr(sbuff);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean WriteRect(Rect rect) {
        if (writepos + 15 < m_buffer.length) {
            WriteUint(rect.left);
            WriteUint(rect.top);
            WriteUint(rect.right);
            WriteUint(rect.bottom);

            return true;

        }
        return false;
    }

}
