package com.example.hsdj;

import android.app.Application;

import java.util.List;

public class MenuApplication extends Application {

    private int menu_id;
    private Object menu_seq;
    private int parent_id;
    private String text;
    private Object url;
    private String authcode;
    private String optcode;
    private String accessKey;

    @Override
    public String toString() {
        return "MenuApplication{" +
                "menu_id=" + menu_id +
                ", menu_seq=" + menu_seq +
                ", parent_id=" + parent_id +
                ", text='" + text + '\'' +
                ", url=" + url +
                ", authcode='" + authcode + '\'' +
                ", optcode='" + optcode + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", appname=" + appname +
                ", children=" + children +
                '}';
    }

    private Object appname;
    private List<MenuApplication> children;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public int getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public Object getMenu_seq() {
        return menu_seq;
    }

    public void setMenu_seq(Object menu_seq) {
        this.menu_seq = menu_seq;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public String getOptcode() {
        return optcode;
    }

    public void setOptcode(String optcode) {
        this.optcode = optcode;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Object getAppname() {
        return appname;
    }

    public void setAppname(Object appname) {
        this.appname = appname;
    }

    public List<MenuApplication> getChildren() {
        return children;
    }

    public void setChildren(List<MenuApplication> children) {
        this.children = children;
    }
}
