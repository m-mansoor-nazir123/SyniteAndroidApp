package com.example.firebasechatapp.Notification;

public class Data {

    private String user,title,body,sent;
    private Integer icon;

    public Data(String myUid, String email, String message, String new_message, String hisUid, int chaticon) {
    }

    public Data(String user, String title, String body, String sent, Integer icon) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.sent = sent;
        this.icon = icon;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }
}
