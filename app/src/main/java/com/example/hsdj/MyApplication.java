package com.example.hsdj;

/*用户全局变量*/
import android.app.Application;

public class MyApplication extends Application {
    //用户登录名
    private String username;
    //用户id
    private String userId;
    //用户昵称（真实姓名）
    private String userdesc;

    public MenuApplication getMenuApplication() {
        return menuApplication;
    }

    public void setMenuApplication(MenuApplication menuApplication) {
        this.menuApplication = menuApplication;
    }

    private MenuApplication menuApplication;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //token
    private String token;


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserdesc() {
        return userdesc;
    }

    public void setUserdesc(String userdesc) {
        this.userdesc = userdesc;
    }
}
