package com.example.flutter_app.meetingboard;

public class MtPurview {
    private int value;

    public MtPurview(int purviewvalue) {
        value = purviewvalue;
    }
    public MtPurview()
    {
        value=0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int purviewvalue)
    {
        value=purviewvalue;
    }

    public boolean getAudio()
    {
        int k=0;
        return ( value>>k &0x01 )==1;
    }
    public void setAudio(boolean bEnabled)
    {
        int k=0;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);
    }
    public boolean getVideo()
    {
        int k=1;
        return ( value>>k &0x01 )==1;
    }
    public void setVideo(boolean bEnabled)
    {
        int k=1;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);
    }
    public boolean getBoard()
    {
        int k=2;
        return ( value>>k &0x01 )==1;
    }
    public void setBoard(boolean bEnabled)
    {
        int k=2;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);
    }
    public boolean getManager()
    {
        int k=3;
        return ( value>>k &0x01 )==1;
    }
    public void setManager(boolean bEnabled)
    {
        int k=3;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);

    }

    public boolean getAdvMng()
    {
        int k=3;
        return ( value>>k &0x01 )==1;
    }
    public void setAdvMng(boolean bEnabled)
    {
        int k=3;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);

    }

}
