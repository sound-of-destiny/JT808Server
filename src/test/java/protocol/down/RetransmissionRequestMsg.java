package protocol.down;

import java.util.ArrayList;

public class RetransmissionRequestMsg {
    // 对应要求补传的原始消息第一包的消息流水号
    private int originFlowId;
    // 重传包总数
    private int retransmissionNum;
    // 重传包 ID 列表重传包序号顺序排列,如“包 ID1 包 ID2...... 包 IDn”
    private ArrayList<Integer> retransmissionIdList;

    @Override
    public String toString() {
        return "RetransmissionRequestMsg{" +
                "originFlowId=" + originFlowId +
                ", retransmissionNum=" + retransmissionNum +
                ", retransmissionIdList=" + retransmissionIdList +
                '}';
    }

    public int getOriginFlowId() {
        return originFlowId;
    }

    public void setOriginFlowId(int originFlowId) {
        this.originFlowId = originFlowId;
    }

    public int getRetransmissionNum() {
        return retransmissionNum;
    }

    public void setRetransmissionNum(int retransmissionNum) {
        this.retransmissionNum = retransmissionNum;
    }

    public ArrayList<Integer> getRetransmissionIdList() {
        return retransmissionIdList;
    }

    public void setRetransmissionIdList(ArrayList<Integer> retransmissionIdList) {
        this.retransmissionIdList = retransmissionIdList;
    }
}
