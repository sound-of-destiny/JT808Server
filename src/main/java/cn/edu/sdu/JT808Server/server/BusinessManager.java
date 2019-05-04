package cn.edu.sdu.JT808Server.server;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BusinessManager {

    // 消息流水号 word(16) 按发送顺序从 0 开始循环累加
    private volatile int currentFlowId = 0;

    private Map<Integer, Channel> replyFlowIdMap;
    private Map<String, Channel> terminalPhoneMap;

    private static volatile BusinessManager instance = null;

    public static BusinessManager getInstance() {
        if (instance == null) {
            synchronized (BusinessManager.class){
                if (instance == null) {
                    instance = new BusinessManager();
                }
            }
        }
        return instance;
    }

    private BusinessManager() {
        this.replyFlowIdMap = new ConcurrentHashMap<>();
        this.terminalPhoneMap = new ConcurrentHashMap<>();
    }

    public Channel findChannelByReplyFlowId(int replyFlowId) {
        return replyFlowIdMap.get(replyFlowId);
    }

    public synchronized void putByReplyFlowId(int replyFlowId, Channel channel) {
        replyFlowIdMap.put(replyFlowId, channel);
    }
    public synchronized void removeByReplyFlowId(int replyFlowId) {
        replyFlowIdMap.remove(replyFlowId);
    }






    public Channel findChannelByTerminalPhone(String terminalPhone) {
        return terminalPhoneMap.get(terminalPhone);
    }

    public synchronized void putByTerminalPhone(String terminalPhone, Channel channel) {
        terminalPhoneMap.put(terminalPhone, channel);
    }

    public synchronized void removeByTerminalPhone(String terminalPhone) {
        terminalPhoneMap.remove(terminalPhone);
    }


    public synchronized int currentFlowId() {
        if (currentFlowId >= 0xffff)
            currentFlowId = 0;
        return currentFlowId++;
    }
}
