package cn.edu.sdu.JT808Server.protocol.upMsg;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;
import cn.edu.sdu.JT808Server.util.JT808Const;

import java.util.Arrays;

/**
 * 
 * 终端鉴权消息
 *
 */

public class TerminalAuthenticationMsg extends PackageData {

	private String authCode;

	public TerminalAuthenticationMsg(PackageData packageData) {
		this.msgHeader = packageData.getMsgHeader();
		this.authCode = new String(packageData.getMsgBody(), JT808Const.string_charset);
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getAuthCode() {
		return authCode;
	}

	public MsgHeader getMsgHeader() {
		return msgHeader;
	}

	@Override
	public String toString() {
		return "[authCode=" + authCode + ", msgHeader=" + msgHeader + ", msgBodyBytes="
				+ Arrays.toString(msgBody) + ", checkSum=" + checkSum + "]";
	}

}
