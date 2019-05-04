package protocol.up;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;
import protocol.Status;
import protocol.WarningFlag;

/**
 *
 * 位置信息汇报消息
 *
 */

public class TerminalLocationMsg extends PackageData {
    //电话
    private String terminalPhone;

    //报警标志 byte[0-3]
    private WarningFlag warningFlag;

    //状态 byte[4-7]
    private Status status;

    //纬度 byte[8-11]
    private double latitude;

    //经度 byte[12-15]
    private double longitude;

    //高程 byte[16-17]
    private int elevation;

    //速度 byte[18-19]
    private double speed;

    //方向 byte[20-21]
    private int direction;

    //时间(BCD[6] YY-MM-DD-hh-mm-ss) byte[22-27]
    private String time;

    // 位置附加信息项
    private TerminalExtraLocationMsg terminalExtraLocationMsg;

    public TerminalLocationMsg(){}

    public TerminalLocationMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
        this.terminalPhone = packageData.getMsgHeader().getTerminalPhone();
    }

    public String getTerminalPhone() {
        return terminalPhone;
    }

    public void setTerminalPhone(String terminalPhone) {
        this.terminalPhone = terminalPhone;
    }

    public WarningFlag getWarningFlag() {
        return warningFlag;
    }

    public void setWarningFlag(WarningFlag warningFlag) {
        this.warningFlag = warningFlag;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TerminalExtraLocationMsg getTerminalExtraLocationMsg() {
        return terminalExtraLocationMsg;
    }

    public void setTerminalExtraLocationMsg(TerminalExtraLocationMsg terminalExtraLocationMsg) {
        this.terminalExtraLocationMsg = terminalExtraLocationMsg;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    @Override
    public String toString() {
        return "{ \"warningFlag\" : " + warningFlag + ", \"status\" : " + status + ", \"latitude\" : "
                + latitude + ", \"longitude\" : " + longitude + ", \"elevation\" : " + elevation
                + ", \"speed\" : " + speed + ", \"direction\" : " + direction + ", \"time\" : \"" + time
                + "\", \"terminalPhone\" : \"" + terminalPhone + "\", \"terminalExtraLocationMsg\" : "
                + terminalExtraLocationMsg +" }";
    }
}
