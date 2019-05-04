package cn.edu.sdu.JT808Server.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PackageManager {
    private Map<String, Map> terminalPhoneMap;

    private static volatile PackageManager instance = null;

    public static PackageManager getInstance() {
        if (instance == null) {
            synchronized (BusinessManager.class){
                if (instance == null) {
                    instance = new PackageManager();
                }
            }
        }
        return instance;
    }

    private PackageManager() {
        this.terminalPhoneMap = new ConcurrentHashMap<>();
    }

    public synchronized void putByTerminalPhone(String terminalPhone, Map map) {
        terminalPhoneMap.put(terminalPhone, map);
    }

    public synchronized Map findByTerminalPhone(String terminalPhone) {
        return terminalPhoneMap.get(terminalPhone);
    }

    public synchronized void removeByTerminalPhone(String terminalPhone) {
        terminalPhoneMap.remove(terminalPhone);
    }
}
