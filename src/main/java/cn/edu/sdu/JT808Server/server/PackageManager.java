package cn.edu.sdu.JT808Server.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PackageManager {
    private Map<String, Map> terminalPhoneMap;

    public static PackageManager getInstance() {
        return Singleton.INSTANCE.getSingleton();
    }

    private PackageManager() {
        this.terminalPhoneMap = new ConcurrentHashMap<>();
    }

    private enum Singleton {
        INSTANCE;
        private PackageManager singleton;
        Singleton() {
            singleton = new PackageManager();
        }
        public PackageManager getSingleton() {
            return singleton;
        }
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
