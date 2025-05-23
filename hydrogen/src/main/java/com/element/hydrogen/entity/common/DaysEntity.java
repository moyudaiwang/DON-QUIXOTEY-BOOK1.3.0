package com.element.hydrogen.entity.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class DaysEntity implements Serializable {
    private String openId;
    private String daysId;
    //每天的标题
    private String title;
    //代办事项的数量
    private int itemNumber;
    //日程
    private String date;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getDaysId() {
        return daysId;
    }

    public void setDaysId(String daysId) {
        this.daysId = daysId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DaysEntity{" +
                "openId='" + openId + '\'' +
                ", daysId='" + daysId + '\'' +
                ", title='" + title + '\'' +
                ", itemNumber=" + itemNumber +
                ", date='" + date + '\'' +
                '}';
    }
}