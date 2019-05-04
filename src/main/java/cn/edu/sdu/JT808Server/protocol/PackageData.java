package cn.edu.sdu.JT808Server.protocol;

import java.util.Arrays;

public class PackageData {

    //消息头 byte[0-15]
    protected MsgHeader msgHeader;

    //消息体
    protected byte[] msgBody;

    //校验码 1byte
    protected int checkSum;

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public byte[] getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(byte[] msgBody) {
        this.msgBody = msgBody;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    /*public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }*/

    public PackageData() {
    }

    public PackageData(MsgHeader msgHeader, byte[] msgBody, int checkSum) {
        this.msgHeader = msgHeader;
        this.msgBody = msgBody;
        this.checkSum = checkSum;
    }

    @Override
    public String toString() {
        return "PackageData [msgHeader=" + msgHeader + ", msgBodyBytes=" + Arrays.toString(msgBody) + ", checkSum="
                + checkSum /*+ ", address=" + channel */+ "]";
    }

}
