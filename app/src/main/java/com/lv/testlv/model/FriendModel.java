package com.lv.testlv.model;

/**
 * Created by lvliheng on 15/6/6.
 */
public class FriendModel {

    private String name;
    private String isInGroup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsInGroup() {
        return isInGroup;
    }

    public void setIsInGroup(String isInGroup) {
        this.isInGroup = isInGroup;
    }

    @Override
    public String toString() {
        return "FriendModel{" +
                "name='" + name + '\'' +
                ", isInGroup='" + isInGroup + '\'' +
                '}';
    }
}
