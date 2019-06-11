package cn.edu.sdu.JT808Server.server;

import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelGroupManager {

    private Map<String, ChannelGroup> channelGroupMap;

    private ChannelGroupManager() {
        this.channelGroupMap = new ConcurrentHashMap<>();
    }

    public static ChannelGroupManager getInstance() {
        return Singleton.INSTANCE.getSingleton();
    }

    private enum Singleton {
        INSTANCE;
        private ChannelGroupManager singleton;
        Singleton() {
            singleton = new ChannelGroupManager();
        }
        public ChannelGroupManager getSingleton() {
            return  singleton;
        }
    }


    public synchronized void put(String channelGroupName, ChannelGroup channelGroup) {
        channelGroupMap.put(channelGroupName, channelGroup);
    }

    public ChannelGroup findByChannelGroupName(String channelGroupName) {
        return channelGroupMap.get(channelGroupName);
    }

}
