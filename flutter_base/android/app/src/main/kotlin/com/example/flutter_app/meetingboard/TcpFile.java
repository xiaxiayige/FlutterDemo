package com.example.flutter_app.meetingboard;

public class TcpFile {
    private String Root;
    public String Host;
    public int  Port;
    public int  Rev;
    public String HttpPre;

    public void SetRoot(String ARoot)
    {
        String sv=ARoot;
        if(sv.equals("")==false)
        {
            if ((sv.substring(sv.length()-1).equals("/")==false) &&
            ((sv.substring(sv.length()-1).equals("\\")==false)))  sv=sv+"/";



        }
        Root=sv;
    }
    public String GetRoot()
    {
        return Root;
    }
}
