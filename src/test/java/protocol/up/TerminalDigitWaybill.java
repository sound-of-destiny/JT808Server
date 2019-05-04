package protocol.up;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

public class TerminalDigitWaybill extends PackageData {
    // 电子运单长度
    private int digitWaybillLength;
    // 电子运单内容
    private String digitWaybillData;

    public TerminalDigitWaybill(){}

    public TerminalDigitWaybill(PackageData packageData) {
        this.msgHeader = packageData.getMsgHeader();
    }

    @Override
    public String toString() {
        return "TerminalDigitWaybill{" +
                "digitWaybillLength=" + digitWaybillLength +
                ", digitWaybillData='" + digitWaybillData + '\'' +
                '}';
    }

    public int getDigitWaybillLength() {
        return digitWaybillLength;
    }

    public void setDigitWaybillLength(int digitWaybillLength) {
        this.digitWaybillLength = digitWaybillLength;
    }

    public String getDigitWaybillData() {
        return digitWaybillData;
    }

    public void setDigitWaybillData(String digitWaybillData) {
        this.digitWaybillData = digitWaybillData;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

}
