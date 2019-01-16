package com.zlsk.zTool.model.other;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IceWang on 2018/6/25.
 */

public class MessageModel {
    private String gid;
    @SerializedName("msg_name")
    private String title;
    @SerializedName("msg_info")
    private String content;
    @SerializedName("sendtime")
    private String time;
    @SerializedName("isread")
    private String isread;
    @SerializedName("isdeal")
    private String isdeal;
    @SerializedName("gcid")
    private String gcid;
    /**
     * 消息跳转的详情id
     */
    @SerializedName("extend")
    private String detailGid;
    private String msg_code;
    private String funid;
    private String activityName;
    private String image;
    private String activityKey;
    private String msgId;

    public boolean Isdeal() {
        return isdeal.equals("1");
    }

    public void setIsdeal(String isdeal) {
        this.isdeal = isdeal;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getDetailGid() {
        return detailGid;
    }

    public void setDetailGid(String detailGid) {
        this.detailGid = detailGid;
    }

    public String getGcid() {
        return gcid;
    }

    public void setGcid(String gcid) {
        this.gcid = gcid;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(String msg_code) {
        this.msg_code = msg_code;
    }

    public String getFunid() {
        return funid;
    }

    public void setFunid(String funid) {
        this.funid = funid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return msg_code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isReadState() {
        return isread.equals("1");
    }

    public String getIsread() {
        return isread;
    }

    public void setIsread(String isread) {
        this.isread = isread;
    }
}
