package com.riska;

/**
 * Created by Anastasia on 05.01.2016.
 */
public class LoginInfo
{
    public String Site;
    public String Login;
    public String Password;

    public String ProxyHost;
    public int ProxyPort = -1;

    public LoginInfo(String site, String login, String password)
    {
        Site = site;
        Login = login;
        Password = password;
    }

    public LoginInfo(String site, String login, String password, String proxyHost, int proxyPort)
    {
        this(site, login, password);

        ProxyHost = proxyHost;
        ProxyPort = proxyPort;
    }

    public boolean hasProxy()
    {
        return ProxyHost != null && !ProxyHost.isEmpty() && ProxyPort != -1;
    }
}