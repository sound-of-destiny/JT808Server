package cn.edu.sdu.JT808Server.protocol;

import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalRegisterMsg;
import io.netty.channel.Channel;

import java.time.LocalDateTime;

public class Session {

	private Channel channel;
	private String terminalPhone;
	private String authenticationCode;
	// 客户端上次的连接时间，该值改变的情况:
	// 1. terminal --> server 心跳包
	// 2. terminal --> server 数据包
	private LocalDateTime localDateTime;
	private TerminalRegisterMsg terminalRegisterMsg;

	public static Channel setNowChannel(Channel channel) {
		return channel;
	}

	public static Session buildSession(Channel channel) {
		return buildSession(channel, null, null, null);
	}

	public static Session buildSession(Channel channel, String phone, String authenticationCode, TerminalRegisterMsg terminalRegisterMsg) {
		Session session = new Session();
		session.setChannel(setNowChannel(channel));
		session.setTerminalPhone(phone);
		session.setAuthenticationCode(authenticationCode);
		session.setLocalDateTime(LocalDateTime.now());
		session.setTerminalRegisterMsg(terminalRegisterMsg);
		return session;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getTerminalPhone() {
		return terminalPhone;
	}

	public void setTerminalPhone(String terminalPhone) {
		this.terminalPhone = terminalPhone;
	}

	public String getAuthenticationCode() {
		return authenticationCode;
	}

	public void setAuthenticationCode(String authenticationCode) {
		this.authenticationCode = authenticationCode;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public TerminalRegisterMsg getTerminalRegisterMsg() {
		return terminalRegisterMsg;
	}

	public void setTerminalRegisterMsg(TerminalRegisterMsg terminalRegisterMsg) {
		this.terminalRegisterMsg = terminalRegisterMsg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((channel == null) ? 0 : channel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Session other = (Session) obj;
		if (channel == null) {
			if (other.channel != null)
				return false;
		} else if (!channel.equals(other.channel))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Session [channel= " + channel + ", terminalPhone=" + terminalPhone + "　authenticationCode=" + authenticationCode
		+ " localDateTime=" + getLocalDateTime() + "]";
	}

}