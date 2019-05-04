package cn.edu.sdu.JT808Server.service.handler.browser;

import cn.edu.sdu.JT808Server.protocol.Session;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import cn.edu.sdu.JT808Server.server.ChannelGroupManager;
import cn.edu.sdu.JT808Server.server.SessionManager;
import cn.edu.sdu.JT808Server.util.BitOperator;
import cn.edu.sdu.JT808Server.util.CountTool;
import cn.edu.sdu.JT808Server.util.JT808Const;
import cn.edu.sdu.JT808Server.util.JT808ProtocolUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class JT808ServerTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        System.out.println("================================================================================");

        System.out.println("【收到消息】　" + msg.text());
        String receivedMsg = msg.text();
        if(receivedMsg.startsWith("0x")||receivedMsg.startsWith("0X")){
            receivedMsg = receivedMsg.substring(2);
        }
        String code = receivedMsg.substring(0, 4);
        String terminalPhone = receivedMsg.substring(4);
        Session session = SessionManager.getInstance().findByTerminalPhone(terminalPhone);
        switch (Integer.valueOf(code, 16)) {

            // 补传分包请求
            case JT808Const.cmd_reload_message_request:
                System.out.println("【补传分包请求】");
                break;

            // 设置终端参数
            case JT808Const.cmd_terminal_param_settings:
                System.out.println("【设置终端参数】");
                if (session != null) {
                    setTerminalParam(ctx, session.getChannel(), terminalPhone);
                }
                break;

            // 查询终端参数
            case JT808Const.cmd_terminal_param_query:
                System.out.println("【查询终端参数】");
                if (session != null) {
                    CountTool countTool = new CountTool();
                    sendToTerminalHeader(ctx, session.getChannel(), terminalPhone, JT808Const.cmd_terminal_param_query, countTool);
                }
                break;

            // 终端控制
            case JT808Const.cmd_terminal_control:
                System.out.println("【终端控制】");
                break;

            // 位置信息查询
            case JT808Const.cmd_terminal_location_query:
                System.out.println("【位置信息查询】");
                if (session != null) {
                    CountTool countTool = new CountTool();
                    sendToTerminalHeader(ctx, session.getChannel(), terminalPhone, JT808Const.cmd_terminal_location_query, countTool);
                }
                break;

            // 临时位置跟踪控制
            case JT808Const.cmd_temp_location_control:
                System.out.println("【临时位置跟踪控制】");
                break;

            // 查询终端属性
            case JT808Const.cmd_terminal_attribute_query:
                System.out.println("【查询终端属性】");
                if (session != null) {
                    sendToTerminalHeaderNoReplyFlowId(ctx, session.getChannel(), terminalPhone,
                            JT808Const.cmd_terminal_attribute_query);
                }
                break;

            // 文本信息下发
            case JT808Const.cmd_text_download:
                System.out.println("【文本信息下发】");
                break;

            // 事件设置
            case JT808Const.cmd_event_setting:
                System.out.println("【事件设置】");
                break;

            // 提问下发
            case JT808Const.cmd_ask_download:
                System.out.println("【提问下发】");
                break;

            // 信息点播菜单设置
            case JT808Const.cmd_message_request_setting:
                System.out.println("【信息点播菜单设置】");
                break;

            // 行驶记录仪数据采集命令
            case JT808Const.cmd_recorder_data_request:
                System.out.println("【行驶记录仪数据采集命令】");
                break;

            // 行驶记录仪参数下传命令
            case JT808Const.cmd_recorder_param_down:
                System.out.println("【行驶记录仪参数下传命令】");
                break;

            // 上报驾驶员身份信息请求
            case JT808Const.cmd_Identity_information_request:
                System.out.println("【上报驾驶员身份信息请求】");
                //sendToTerminalDriver(session.getChannel(), terminalPhone, JT808Const.cmd_Identity_information_request);
                break;

            // 多媒体数据上传应答
            case JT808Const.cmd_media_upload_resp:
                System.out.println("【多媒体数据上传应答】");
                break;

            // 存储多媒体数据检索
            case JT808Const.cmd_saved_media_request:
                System.out.println("【存储多媒体数据检索】");
                if (session != null) {
                    savedMediaResourcesQuery(ctx, session.getChannel(), terminalPhone);
                }
                break;

            // 查询终端音视频属性
            case JT808Const.cmd_Terminal_media_attribute_query:
                System.out.println("【查询终端音视频属性】");
                if (session != null) {
                    sendToTerminalHeaderNoReplyFlowId(ctx, session.getChannel(), terminalPhone, JT808Const.cmd_Terminal_media_attribute_query);
                }
                break;

            // 实时音视频传输请求
            case JT808Const.cmd_real_time_media_transmission_request:
                System.out.println("【实时音视频传输请求】");
                if (session != null) {
                    realTimeMediaTransmissionRequest(ctx, session.getChannel(), terminalPhone);
                }
                break;

            // 音视频实时传输控制
            case JT808Const.cmd_real_time_media_transmission_control:
                System.out.println("【音视频实时传输控制】");
                if (session != null) {
                    realTimeMediaTransmissionControl(ctx, session.getChannel(), terminalPhone);
                }
                break;

            // 实时音视频传输状态通知
            case JT808Const.cmd_real_time_media_transmission_state:
                System.out.println("【实时音视频传输状态通知】");
                if (session != null) {
                    for (int i = 0; i < 32; i++) {
                        realTimeMediaTransmissionState(i, ctx, session.getChannel(), terminalPhone);
                    }

                }
                break;

            // 查询资源列表
            case JT808Const.cmd_media_resources_query:
                System.out.println("【查询资源列表】");
                if (session != null) {
                    mediaResourcesQuery(ctx, session.getChannel(), terminalPhone);
                }
                break;

            // 摄像头立即拍摄命令
            case JT808Const.cmd_camera_photo:
                System.out.println("【摄像头立即拍摄命令】");
                if (session != null) {
                    cameraPhoto(ctx, session.getChannel(), terminalPhone);
                }
                break;

            // 录音开始命令
            case JT808Const.cmd_record_start:
                System.out.println("【录音开始命令】");
                if (session != null) {
                    recordStart(ctx, session.getChannel(), terminalPhone);
                }
                break;

            default:
                System.out.println("【未识别命令】");
        }



    }

    private void setTerminalParam(ChannelHandlerContext ctx, Channel channel, String terminalPhone) throws Exception {
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(2),

               /* BitOperator.integerTo4Bytes(0x0013),
                BitOperator.integerTo1Bytes(15),
                "202.194.014.071".getBytes(),*/

                BitOperator.integerTo4Bytes(0x0075),
                BitOperator.integerTo1Bytes(21),        // 长度
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(5),
                BitOperator.integerTo2Bytes(500),
                BitOperator.integerTo1Bytes(30),
                BitOperator.integerTo4Bytes(10000),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(5),
                BitOperator.integerTo2Bytes(500),
                BitOperator.integerTo1Bytes(30),
                BitOperator.integerTo4Bytes(10000),
                BitOperator.integerTo2Bytes(0),         // OSD
                BitOperator.integerTo1Bytes(0),

                BitOperator.integerTo4Bytes(0x0076),
                BitOperator.integerTo1Bytes(19),
                BitOperator.integerTo1Bytes(4),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(4),
                BitOperator.integerTo1Bytes(1),         // 通道对照表
                BitOperator.integerTo1Bytes(1),
                BitOperator.integerTo1Bytes(2),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(2),         // 通道对照表
                BitOperator.integerTo1Bytes(2),
                BitOperator.integerTo1Bytes(2),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(3),         // 通道对照表
                BitOperator.integerTo1Bytes(3),
                BitOperator.integerTo1Bytes(2),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(4),         // 通道对照表
                BitOperator.integerTo1Bytes(4),
                BitOperator.integerTo1Bytes(2),
                BitOperator.integerTo1Bytes(0)
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_terminal_param_settings, countTool);
    }

    private void recordStart(ChannelHandlerContext ctx, Channel channel, String terminalPhone) throws Exception {
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(0x01),                 // channel
                BitOperator.integerTo2Bytes(1),
                BitOperator.integerTo1Bytes(0),                 // 保存标志
                BitOperator.integerTo1Bytes(0)
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_camera_photo, countTool);
    }

    private void cameraPhoto(ChannelHandlerContext ctx, Channel channel, String terminalPhone) throws Exception {
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(5),                 // channel
                BitOperator.integerTo2Bytes(1),
                BitOperator.integerTo2Bytes(0),
                BitOperator.integerTo1Bytes(0),                 // 保存标志
                BitOperator.integerTo1Bytes(0x08),
                BitOperator.integerTo1Bytes(1),
                BitOperator.integerTo1Bytes(128),
                BitOperator.integerTo1Bytes(64),
                BitOperator.integerTo1Bytes(64),
                BitOperator.integerTo1Bytes(128)
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_camera_photo, countTool);
    }

    private void savedMediaResourcesQuery(ChannelHandlerContext ctx, Channel channel, String terminalPhone) throws Exception {
        String time = "000000000000";
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(0),
                time.getBytes(),
                time.getBytes()
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_saved_media_request, countTool);
    }

    private void mediaResourcesQuery(ChannelHandlerContext ctx, Channel channel, String terminalPhone) throws Exception {
        String time = "000000000000";
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(0),
                time.getBytes(),
                time.getBytes(),
                new byte[] {0, 0, 0, 0, 0, 0, 0, 0},
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(0)
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_media_resources_query, countTool);
    }

    private void realTimeMediaTransmissionRequest(ChannelHandlerContext ctx, Channel channel, String terminalPhone) throws Exception {
        String ip = "211.087.225.206";
        System.out.println(ip.getBytes().length);
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(ip.getBytes().length),
                ip.getBytes(),
                BitOperator.integerTo2Bytes(10003),
                BitOperator.integerTo2Bytes(0),
                BitOperator.integerTo1Bytes(1),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(0)
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_real_time_media_transmission_request, countTool);
    }

    private void realTimeMediaTransmissionControl(ChannelHandlerContext ctx, Channel channel, String terminalPhone) throws Exception {
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(1),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(0)
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_real_time_media_transmission_control, countTool);
    }

    private void realTimeMediaTransmissionState(int i, ChannelHandlerContext ctx, Channel channel, String terminalPhone) throws Exception {
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(i),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(0),
                BitOperator.integerTo1Bytes(0)
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_real_time_media_transmission_control, countTool);
    }

    private void sendToTerminal(byte[] msgBody, ChannelHandlerContext ctx, Channel channel, String terminalPhone, int code, CountTool countTool) throws Exception {
        BusinessManager businessManager = BusinessManager.getInstance();
        int replyFlowId = businessManager.currentFlowId();
        System.out.println(replyFlowId + " " + channel);
        businessManager.putByReplyFlowId(replyFlowId, ctx.channel());

        channel.writeAndFlush(Unpooled.copiedBuffer(JT808ProtocolUtils.sendToTerminal(msgBody, replyFlowId, terminalPhone, code)));
        /*future.addListener((ChannelFutureListener) futureListener -> futureListener.channel().eventLoop().schedule(() -> {
            int count = countTool.getN();
            int retransmissionTime = countTool.getRetransmissionTime();
            if (futureListener.isSuccess() && (BusinessManager.getInstance().findChannelByReplyFlowId(replyFlowId) == null)) {
                System.out.println("【发送成功】");
            } else {
                if (count < JT808Const.retransmission) {
                    try {
                        System.out.println("【发送失败】　第 " +  count + " 次尝试");
                        sendToTerminal(msgBody, ctx, channel, terminalPhone, code, countTool);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                count++;
                retransmissionTime *= count;
                countTool.setN(count);
                countTool.setRetransmissionTime(retransmissionTime);

            }
        }, countTool.getRetransmissionTime(), TimeUnit.SECONDS));*/
    }

    private void sendToTerminalHeader(ChannelHandlerContext ctx, Channel channel, String terminalPhone, int code, CountTool countTool) throws Exception {
        BusinessManager businessManager = BusinessManager.getInstance();
        int replyFlowId = businessManager.currentFlowId();
        System.out.println(replyFlowId + " " + channel);
        businessManager.putByReplyFlowId(replyFlowId, ctx.channel());
        ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(JT808ProtocolUtils.sendToTerminalHeader(
                replyFlowId,
                terminalPhone,
                code)));
        future.addListener((ChannelFutureListener) futureListener -> futureListener.channel().eventLoop().schedule(() -> {
            int count = countTool.getN();
            int retransmissionTime = countTool.getRetransmissionTime();
            if (futureListener.isSuccess() && (BusinessManager.getInstance().findChannelByReplyFlowId(replyFlowId) == null)) {
                System.out.println("【发送成功】");
            } else {
                if (count < JT808Const.retransmission) {
                    try {
                        System.out.println("【发送失败】　第 " +  count + " 次尝试");
                        sendToTerminalHeader(ctx, channel, terminalPhone, code, countTool);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                retransmissionTime = retransmissionTime*(count + 1);
                count++;
                countTool.setN(count);
                countTool.setRetransmissionTime(retransmissionTime);

            }
        }, countTool.getRetransmissionTime(), TimeUnit.SECONDS));
    }

    private void sendToTerminalHeaderNoReplyFlowId(ChannelHandlerContext ctx, Channel channel, String terminalPhone, int code) throws Exception {
        BusinessManager businessManager = BusinessManager.getInstance();
        int replyFlowId = businessManager.currentFlowId();
        businessManager.putByTerminalPhone(terminalPhone, ctx.channel());
        channel.writeAndFlush(Unpooled.copiedBuffer(JT808ProtocolUtils.sendToTerminalHeader(replyFlowId, terminalPhone, code)));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("【有监控终端接入】   " + channel);
        if (ChannelGroupManager.getInstance().findByChannelGroupName("LocationMsg") == null)
            ChannelGroupManager.getInstance().put("LocationMsg", channelGroup);
        ChannelGroupManager.getInstance().findByChannelGroupName("LocationMsg").add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("【有监控终端断开】");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("【异常发生】");
        cause.printStackTrace();
        ctx.close();
    }
}
