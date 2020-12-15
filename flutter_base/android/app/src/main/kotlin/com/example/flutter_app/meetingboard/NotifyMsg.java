package com.example.flutter_app.meetingboard;

public interface NotifyMsg {
    public void onAddObject(int ObjId);
    public void onDelObject(int ObjId);
    public void onAddPage(int iPos, boolean bMode);
    public void onDelPage(int iPos, boolean bMode);
    public void onSelectPage(int iPos, boolean bMode);
    public void onClearScreen(int iPos, boolean bMode);
    public void onAdjustOver();

}
