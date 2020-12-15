package com.example.flutter_app.meetingboard;

import android.content.Context;

import java.util.Properties;

public class SoftConfig {
    private String m_loginServiceUrl;
    private String m_rtcServiceUrl;
    private String m_ggwApiUrl;
    private String m_ggwLessonsUrl;
    private Context m_context;

    public SoftConfig(Context context) {
        m_context = context;
    }

    public void LoadConfig(String configFileame) {
        Properties properties = new Properties();
        try {
            properties.load(m_context.getAssets().open(configFileame));
            m_loginServiceUrl = properties.getProperty("LoginServiceUrl", "");
            m_rtcServiceUrl = properties.getProperty("RtcServiceUrl", "");
            m_ggwApiUrl=properties.getProperty("GgwApiUrl","");
            m_ggwLessonsUrl=properties.getProperty("GgwLessonsUrl","");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLoginServiceUrl() {
        return m_loginServiceUrl;
    }
    public String getRtcServiceUrl()
    {
        return m_rtcServiceUrl;
    }
    public String getGgwApiUrl()
    {
        return m_ggwApiUrl;
    }
    public String getLessonsUrl()
    {
        return m_ggwLessonsUrl;
    }
}
