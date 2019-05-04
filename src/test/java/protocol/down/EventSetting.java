package protocol.down;

import protocol.Event;

import java.util.ArrayList;

public class EventSetting {
    // 设置类型 0:删除终端现有所有事件,该命令后不带后继字节;1:更新事件;2:追加事件;3:修改事件;4:删除特定几项事件,之后事件项中无需带事件内容
    private int settingType;
    // 设置总数
    private int settingNum;
    // 事件项列表
    private ArrayList<Event> eventList;

    @Override
    public String toString() {
        return "EventSetting{" +
                "settingType=" + settingType +
                ", settingNum=" + settingNum +
                ", eventList=" + eventList +
                '}';
    }

    public int getSettingType() {
        return settingType;
    }

    public void setSettingType(int settingType) {
        this.settingType = settingType;
    }

    public int getSettingNum() {
        return settingNum;
    }

    public void setSettingNum(int settingNum) {
        this.settingNum = settingNum;
    }

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }
}
