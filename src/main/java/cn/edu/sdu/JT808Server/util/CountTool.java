package cn.edu.sdu.JT808Server.util;

public class CountTool {

    private int n;
    private int retransmissionTime;

    public CountTool() {
        this.n = 1;
        this.retransmissionTime = JT808Const.responseTimeout;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getRetransmissionTime() {
        return retransmissionTime;
    }

    public void setRetransmissionTime(int retransmissionTime) {
        this.retransmissionTime = retransmissionTime;
    }
}
