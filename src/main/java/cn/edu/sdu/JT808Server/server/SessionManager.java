package cn.edu.sdu.JT808Server.server;

import cn.edu.sdu.JT808Server.protocol.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class SessionManager {

	// netty生成的sessionID和Session的对应关系
	private Map<String, Session> sessionIdMap;
	// 终端手机号和netty生成的sessionID的对应关系
	private Map<String, String> phoneMap;
	// 终端鉴权码和netty生成的sessionID的对应关系
	private Map<String, String> authenticationCodeMap;

	public static SessionManager getInstance() {
		return Singleton.INSTANCE.getSingleton();
	}

	private SessionManager() {
		this.sessionIdMap = new ConcurrentHashMap<>();
		this.phoneMap = new ConcurrentHashMap<>();
		this.authenticationCodeMap = new ConcurrentHashMap<>();
	}

	private enum Singleton {
		INSTANCE;
		private SessionManager singleton;
		Singleton() {
			singleton = new SessionManager();
		}
		public SessionManager getSingleton() {
			return singleton;
		}
	}

	public Session findBySessionId(String id) {
		return sessionIdMap.get(id);
	}

	public Session findByAuthenticationCode(String authenticationCode) {
		String sessionId = this.authenticationCodeMap.get(authenticationCode);
		if (sessionId == null)
			return null;
		return this.findBySessionId(sessionId);
	}

	public Session findByTerminalPhone(String phone) {
		String sessionId = phoneMap.get(phone);
		if (sessionId == null)
			return null;
		return this.findBySessionId(sessionId);
	}

	public synchronized void put(String key, Session value) {
		if (value.getTerminalPhone() != null && !"".equals(value.getTerminalPhone().trim())) {
			phoneMap.put(value.getTerminalPhone(), value.getChannel().id().asLongText());
		}
		if (value.getAuthenticationCode() != null && !"".equals(value.getAuthenticationCode().trim())) {
			authenticationCodeMap.put(value.getAuthenticationCode(), value.getChannel().id().asLongText());
		}
		sessionIdMap.put(key, value);
	}

	public synchronized void removeBySessionId(String sessionId) {
		if (sessionId == null)
			return;
		Session session = sessionIdMap.remove(sessionId);
		if (session == null)
			return;
		if (session.getTerminalPhone() != null)
			phoneMap.remove(session.getTerminalPhone());
		if (session.getAuthenticationCode() != null)
			phoneMap.remove(session.getAuthenticationCode());
	}

	public Set<String> keySet() {
		return sessionIdMap.keySet();
	}

	public void forEach(BiConsumer<? super String, ? super Session> action) {
		sessionIdMap.forEach(action);
	}

	public Set<Map.Entry<String, Session>> entrySet() {
		return sessionIdMap.entrySet();
	}

	public List<Session> toList() {
		return new ArrayList<>(sessionIdMap.values());
	}

}