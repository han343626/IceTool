package com.ice.icetool.model;

/**
 * Created by IceWang on 2019/1/4.
 */

public class MessageModel {
    public String title;
    public String content;
    public String time;
    public boolean read = false;

    public MessageModel(String title, String content, String time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public MessageModel(String title, String content, String time, boolean read) {
        this.title = title;
        this.content = content;
        this.time = time;
        this.read = read;
    }
}
