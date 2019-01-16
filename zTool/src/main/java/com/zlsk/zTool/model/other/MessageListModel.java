package com.zlsk.zTool.model.other;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by IceWang on 2018/6/25.
 */

public class MessageListModel {
    @SerializedName("getMe")
    private List<MessageModel> messageModelList;
    private int total;

    public List<MessageModel> getMessageModelList() {
        return messageModelList;
    }

    public void setMessageModelList(List<MessageModel> messageModelList) {
        this.messageModelList = messageModelList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
