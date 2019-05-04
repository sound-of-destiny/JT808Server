package cn.edu.sdu.JT808Server.util;

import cn.edu.sdu.JT808Server.protocol.Session;
import cn.edu.sdu.JT808Server.protocol.upMsg.TerminalRegisterMsg;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import cn.edu.sdu.JT808Server.server.SessionManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

public class JT808ProtocolUtils {

	private static final Logger log = LoggerFactory.getLogger(JT808ProtocolUtils.class);
	/**
	 * 发送到终端
	 */
	public static byte[] sendToTerminal(byte[] msgBody, int flowId, String terminalPhone, int rspType) throws Exception {
		int msgBodyProps = JT808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
		byte[] msgHeader = JT808ProtocolUtils.generateMsgHeader(terminalPhone, rspType, msgBodyProps, flowId);
		byte[] headerAndBody = BitOperator.concatAll(msgHeader, msgBody);
		return getBytes(headerAndBody);
	}

	/**
	 * 只发送头部
	 */
	public static byte[] sendToTerminalHeader(int flowId, String terminalPhone, int rspType) throws Exception {
		int msgBodyProps = JT808ProtocolUtils.generateMsgBodyProps(0, 0b000, false, 0);
		byte[] msgHeader = JT808ProtocolUtils.generateMsgHeader(terminalPhone, rspType, msgBodyProps, flowId);
		return getBytes(msgHeader);
	}

	private static byte[] getBytes(byte[] msg) throws Exception {
		int checkSum = BitOperator.getCheckSum4JT808(msg, 0, msg.length);

		byte[] pkg = BitOperator.concatAll(Arrays.asList(
				new byte[] { JT808Const.pkg_delimiter }, 				// 0x7e
				msg, 													// 消息
				BitOperator.integerTo1Bytes(checkSum), 					// 校验码
				new byte[] { JT808Const.pkg_delimiter }					// 0x7e
		));

		return doEscape4Send(pkg, 1, pkg.length - 2);
	}

	public static void sessionUpdate(ChannelHandlerContext ctx, TerminalRegisterMsg msg, String authenticationCode, String terminalPhone) {
		SessionManager sessionManager = SessionManager.getInstance();
		Session phoneSession = sessionManager.findByTerminalPhone(terminalPhone);
		if (phoneSession != null) {
			sessionManager.removeBySessionId(phoneSession.getChannel().id().asLongText());
			phoneSession.getChannel().close();
		}
		Session authenticationSession = sessionManager.findByAuthenticationCode(authenticationCode);
		if (authenticationSession != null) {
			sessionManager.removeBySessionId(authenticationSession.getChannel().id().asLongText());
			authenticationSession.getChannel().close();
		}
		Session session = Session.buildSession(ctx.channel(), terminalPhone, authenticationCode, msg);
		session.setLocalDateTime(LocalDateTime.now());
		sessionManager.put(session.getChannel().id().asLongText(), session);
	}

	// 通用应答
	public static void commonResponse(ChannelHandlerContext ctx, int replyFlowId, int responseCode, int replyCode) throws Exception {
		byte[] msgBody = BitOperator.concatAll(Arrays.asList(
				BitOperator.integerTo2Bytes(replyFlowId),  						// 应答流水号
				BitOperator.integerTo2Bytes(responseCode),      				// 应答ID,对应的终端消息的ID
				BitOperator.integerTo1Bytes(replyCode)                  		// 结果
		));

		Session session = SessionManager.getInstance().findBySessionId(ctx.channel().id().asLongText());
		log.info("【通用应答】");
		ctx.writeAndFlush(Unpooled.copiedBuffer(sendToTerminal(msgBody, BusinessManager.getInstance().currentFlowId(),
				session.getTerminalPhone(), JT808Const.cmd_common_resp)));
	}

	/**
	 * 接收消息时转义<br>
	 * 
	 * <pre>
	 * 0x7d01 <====> 0x7d
	 * 0x7d02 <====> 0x7e
	 * </pre>
	 * 
	 * @param bs
	 *            要转义的字节数组
	 * @param start
	 *            起始索引
	 * @param end
	 *            结束索引
	 * @return 转义后的字节数组
	 * @throws Exception e
	 */
	public static byte[] doEscape4Receive(byte[] bs, int start, int end) throws Exception {
		if (start < 0 || end > bs.length)
			throw new ArrayIndexOutOfBoundsException("doEscape4Receive error : index out of bounds(start=" + start
					+ ",end=" + end + ",bytes length=" + bs.length + ")");
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			for (int i = 0; i < start; i++) {
				baos.write(bs[i]);
			}
			for (int i = start; i < end - 1; i++) {
				if (bs[i] == 0x7d && bs[i + 1] == 0x01) {
					baos.write(0x7d);
					i++;
				} else if (bs[i] == 0x7d && bs[i + 1] == 0x02) {
					baos.write(0x7e);
					i++;
				} else {
					baos.write(bs[i]);
				}
			}
			for (int i = end - 1; i < bs.length; i++) {
				baos.write(bs[i]);
			}
			return baos.toByteArray();
		}
	}

	/**
	 * 
	 * 发送消息时转义<br>
	 * 
	 * <pre>
	 *  0x7e <====> 0x7d02
	 * </pre>
	 * 
	 * @param bs
	 *            要转义的字节数组
	 * @param start
	 *            起始索引
	 * @param end
	 *            结束索引
	 * @return 转义后的字节数组
	 * @throws Exception e
	 */
	private static byte[] doEscape4Send(byte[] bs, int start, int end) throws Exception {
		if (start < 0 || end > bs.length)
			throw new ArrayIndexOutOfBoundsException("doEscape4Send error : index out of bounds(start=" + start
					+ ",end=" + end + ",bytes length=" + bs.length + ")");
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
			for (int i = 0; i < start; i++) {
				baos.write(bs[i]);
			}
			for (int i = start; i < end; i++) {
				if (bs[i] == 0x7e) {
					baos.write(0x7d);
					baos.write(0x02);
				} else {
					baos.write(bs[i]);
				}
			}
			for (int i = end; i < bs.length; i++) {
				baos.write(bs[i]);
			}
			return baos.toByteArray();
		}
	}

	public static int generateMsgBodyProps(int msgLen, int encryptionType, boolean isSubPackage, int reversed_14_15) {
		// [ 0-9 ] 0000,0011,1111,1111(03FF)(消息体长度)
		// [10-12] 0001,1100,0000,0000(1C00)(加密类型)
		// [ 13  ] 0010,0000,0000,0000(2000)(是否有子包)
		// [14-15] 1100,0000,0000,0000(C000)(保留位)
		if (msgLen >= 1024)
			log.error("The max value of msgLen is 1023, but " + msgLen);
		int subPkg = isSubPackage ? 1 : 0;
		int ret = (msgLen & 0x3FF) | ((encryptionType << 10) & 0x1C00) | ((subPkg << 13) & 0x2000)
				| ((reversed_14_15 << 14) & 0xC000);
		return ret & 0xffff;
	}

	private static byte[] generateMsgHeader(String phone, int msgType, int msgBodyProps, int flowId)
			throws Exception {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			// 1. 消息ID word(16)
			baos.write(BitOperator.integerTo2Bytes(msgType));
			// 2. 消息体属性 word(16)
			baos.write(BitOperator.integerTo2Bytes(msgBodyProps));
			// 3. 终端手机号 bcd[6]
			baos.write(BCD8421Operator.string2Bcd(phone));
			// 4. 消息流水号 word(16),按发送顺序从 0 开始循环累加
			baos.write(BitOperator.integerTo2Bytes(flowId));
			// 消息包封装项 此处不予考虑
			return baos.toByteArray();
		}
	}
}
