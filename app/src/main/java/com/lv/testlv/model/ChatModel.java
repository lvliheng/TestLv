package com.lv.testlv.model;

import com.avos.avoscloud.im.v2.AVIMConversation;

import java.io.Serializable;

/**
 * Created by lvliheng on 15/6/4.
 */
public class ChatModel implements Serializable {
    private AVIMConversation avimConversation;

    public AVIMConversation getAvimConversation() {
        return avimConversation;
    }

    public void setAvimConversation(AVIMConversation avimConversation) {
        this.avimConversation = avimConversation;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "avimConversation=" + avimConversation +
                '}';
    }
}
