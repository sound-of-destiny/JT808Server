package cn.edu.sdu.JT808Server.protocol;

public class MsgHeader {

    // 消息ID byte[0-1]
    private int msgId;

    /**
     * 消息体属性
     */
    // byte[2-3]
    private int msgBodyPropsField;
    // 消息体长度 bit[0-9]
    private int msgBodyLength;
    // 数据加密方式 bit[10-12]
    private int encryptionType;
    // 是否分包,true==>有消息包封装项 bit[13]
    private boolean hasSubPackage;
    // 保留位 bit[14-15]
    private int reservedBit;

    // 终端手机号 byte[4-9]
    private String terminalPhone;

    // 流水号 byte[10-11]
    private int flowId;

    /**
     * 消息包封装项
     */
    // byte[12-15]
    private int packageInfoField;
    // 消息包总数(word(16bit))
    private int totalSubPackage;
    // 包序号(word(16bit))这次发送的这个消息包是分包中的第几个消息包, 从 1 开始
    private int subPackageSeq;


    /**
     * getter and setter
     */
    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getTerminalPhone() {
        return terminalPhone;
    }

    public void setTerminalPhone(String terminalPhone) {
        this.terminalPhone = terminalPhone;
    }

    public int getFlowId() {
        return flowId;
    }

    public void setFlowId(int flowId) {
        this.flowId = flowId;
    }

    public int getMsgBodyPropsField() {
        return msgBodyPropsField;
    }

    public void setMsgBodyPropsField(int msgBodyPropsField) {
        this.msgBodyPropsField = msgBodyPropsField;
    }

    public int getMsgBodyLength() {
        return msgBodyLength;
    }

    public void setMsgBodyLength(int msgBodyLength) {
        this.msgBodyLength = msgBodyLength;
    }

    public int getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(int encryptionType) {
        this.encryptionType = encryptionType;
    }

    public boolean isHasSubPackage() {
        return hasSubPackage;
    }

    public void setHasSubPackage(boolean hasSubPackage) {
        this.hasSubPackage = hasSubPackage;
    }

    public int getReservedBit() {
        return reservedBit;
    }

    public void setReservedBit(int reservedBit) {
        this.reservedBit = reservedBit;
    }

    public int getPackageInfoField() {
        return packageInfoField;
    }

    public void setPackageInfoField(int packageInfoField) {
        this.packageInfoField = packageInfoField;
    }

    public int getTotalSubPackage() {
        return totalSubPackage;
    }

    public void setTotalSubPackage(int totalSubPackage) {
        this.totalSubPackage = totalSubPackage;
    }

    public int getSubPackageSeq() {
        return subPackageSeq;
    }

    public void setSubPackageSeq(int subPackageSeq) {
        this.subPackageSeq = subPackageSeq;
    }

    @Override
    public String toString() {
        return "MsgHeader [msgId=" + msgId + ", msgBodyPropsField=" + msgBodyPropsField + ", msgBodyLength="
                + msgBodyLength + ", encryptionType=" + encryptionType + ", hasSubPackage=" + hasSubPackage
                + ", reservedBit=" + reservedBit + ", terminalPhone=" + terminalPhone + ", flowId=" + flowId
                + ", packageInfoField=" + packageInfoField + ", totalSubPackage=" + totalSubPackage
                + ", subPackageSeq=" + subPackageSeq + "]";
    }
}
