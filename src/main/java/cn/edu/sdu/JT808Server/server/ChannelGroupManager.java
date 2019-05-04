package cn.edu.sdu.JT808Server.server;

import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelGroupManager {

    private static volatile ChannelGroupManager instance = null;

    private Map<String, ChannelGroup> channelGroupMap;

    private ChannelGroupManager() {
        this.channelGroupMap = new ConcurrentHashMap<>();
    }

    public static ChannelGroupManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new ChannelGroupManager();
                }
            }
        }
        return instance;
    }


    public synchronized void put(String channelGroupName, ChannelGroup channelGroup) {
        channelGroupMap.put(channelGroupName, channelGroup);
    }

    public ChannelGroup findByChannelGroupName(String channelGroupName) {
        return channelGroupMap.get(channelGroupName);
    }

}
