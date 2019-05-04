package protocol.up;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

public class TerminalDriverIdentityMsg extends PackageData {
    // 状态
    // 0x01:从业资格证 IC 卡插入(驾驶员上班)
    // 0x02:从业资格证 IC 卡拔出(驾驶员下班)
    private int state;
    // 时间
    // 插卡/拔卡时间,YY-MM-DD-hh-mm-ss;
    // 以下字段在状态为 0x01 时才有效并做填充
    private String time;
    // IC 卡读取结果
    // 0x00:IC 卡读卡成功;
    // 0x01:读卡失败,原因为卡片密钥认证未通过;
    // 0x02:读卡失败,原因为卡片已被锁定;
    // 0x03:读卡失败,原因为卡片被拔出;
    // 0x04:读卡失败,原因为数据校验错误。
    // 以下字段在 IC 卡读取结果等于 0x00 时才有效
    private int ICCardInfo;
    // 驾驶员姓名长度
    private int driverNameLength;
    // 驾驶员姓名
    private String driverName;
    // 从业资格证编码
    // 长度 20 位,不足补 0x00
    private String qualificationCode;
    // 发证机构名称长度
    private int authorityNameLength;
    // 发证机构名称
    private String authorityName;
    // 证件有效期 BCD[4] YYYYMMDD
    private String cardValidityTerm;

    public TerminalDriverIdentityMsg(){}

    public TerminalDriverIdentityMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    @Override
    public String toString() {
        return "TerminalDriverIdentityMsg{" +
                "state=" + state +
                ", time='" + time + '\'' +
                ", ICCardInfo=" + ICCardInfo +
                ", driverNameLength=" + driverNameLength +
                ", driverName='" + driverName + '\'' +
                ", qualificationCode='" + qualificationCode + '\'' +
                ", authorityNameLength=" + authorityNameLength +
                ", authorityName='" + authorityName + '\'' +
                ", cardValidityTerm='" + cardValidityTerm + '\'' +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getICCardInfo() {
        return ICCardInfo;
    }

    public void setICCardInfo(int ICCardInfo) {
        this.ICCardInfo = ICCardInfo;
    }

    public int getDriverNameLength() {
        return driverNameLength;
    }

    public void setDriverNameLength(int driverNameLength) {
        this.driverNameLength = driverNameLength;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getQualificationCode() {
        return qualificationCode;
    }

    public void setQualificationCode(String qualificationCode) {
        this.qualificationCode = qualificationCode;
    }

    public int getAuthorityNameLength() {
        return authorityNameLength;
    }

    public void setAuthorityNameLength(int authorityNameLength) {
        this.authorityNameLength = authorityNameLength;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getCardValidityTerm() {
        return cardValidityTerm;
    }

    public void setCardValidityTerm(String cardValidityTerm) {
        this.cardValidityTerm = cardValidityTerm;
    }

    @Override
    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

}
