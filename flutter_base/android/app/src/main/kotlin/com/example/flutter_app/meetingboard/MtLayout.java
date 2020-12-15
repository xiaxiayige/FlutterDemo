package com.example.flutter_app.meetingboard;

public   class MtLayout {
    private int value;

    public MtLayout(int mtlayoutvalue) {
        value = mtlayoutvalue;
    }
    public MtLayout()
    {
        value=0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int mtlayoutvalue)
    {
        value=mtlayoutvalue;
    }

    public boolean getLock()
    {
        int k=0;
        return ( value>>k &0x01 )==1;
    }
    public void setLock(boolean bEnabled)
    {
        int k=0;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);
    }
    public boolean getShowLeft()
    {
        int k=1;
        return ( value>>k &0x01 )==1;
    }
    public void setShowLeft(boolean bEnabled)
    {
        int k=1;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);
    }
    public boolean getShowRight()
    {
        int k=2;
        return ( value>>k &0x01 )==1;
    }
    public void setShowRight(boolean bEnabled)
    {
        int k=2;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);
    }
    public boolean getHideChat()
    {
        int k=3;
        return ( value>>k &0x01 )==1;
    }
    public void setHideChat(boolean bEnabled)
    {
        int k=3;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);

    }


    public boolean getHideTop()
    {
        int k=4;
        return ( value>>k &0x01 )==1;
    }
    public void setHideTop(boolean bEnabled)
    {
        int k=4;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);
    }


    public boolean getVideoSync()
    {
        int k=5;
        return ( value>>k &0x01 )==1;
    }
    public void setVideoSync(boolean bEnabled)
    {
        int k=5;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);
    }


    public boolean getFullScreen()
    {
        int k=6;
        return ( value>>k &0x01 )==1;
    }
    public void setFullScreen(boolean bEnabled)
    {
        int k=6;
        if (bEnabled) value |=(1<<k);
        else value &=~(1<<k);
    }


    public int  getVPanel()
    {
        return ( value>>8 &0x0F ) ;
    }
    public void setVPanel(int videovalue)
    {
        value |= 8<<videovalue;
    }

    public int  getPanelIndex()
    {
        return ( value>>12 &0x0F ) ;
    }
    public void setPanelIndex(int videovalue)
    {
        value |= 12<<videovalue;
    }
}