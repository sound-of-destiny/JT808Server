package cn.edu.sdu.JT808Server.service.handler.web;

import cn.edu.sdu.JT808Server.protobuf.ClientData;
import cn.edu.sdu.JT808Server.protocol.Session;
import cn.edu.sdu.JT808Server.server.BusinessManager;
import cn.edu.sdu.JT808Server.server.SessionManager;
import cn.edu.sdu.JT808Server.util.BitOperator;
import cn.edu.sdu.JT808Server.util.CountTool;
import cn.edu.sdu.JT808Server.util.JT808Const;
import cn.edu.sdu.JT808Server.util.JT808ProtocolUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class JT808ServerWebHandler extends SimpleChannelInboundHandler<ClientData.Protocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ClientData.Protocol msg) throws Exception {

        log.info("【收到消息】　" + msg.getProtoType());

        String terminalPhone = msg.getTerminalPhone();
        Session session = SessionManager.getInstance().findByTerminalPhone(terminalPhone);
        if (session == null) return;
        CountTool countTool = new CountTool();

        switch (msg.getProtoType()) {

            // 补传分包请求 TODO
            case JT808Const.cmd_reload_message_request:
                log.info("【补传分包请求】");
                break;

            // 设置终端参数 TODO
            case JT808Const.cmd_terminal_param_settings:
                log.info("【设置终端参数】");
                setTerminalParam(ctx, session.getChannel(), terminalPhone, msg.getSettingTerminalParamMsg());
                break;

            // 查询终端参数
            case JT808Const.cmd_terminal_param_query:
                log.info("【查询终端参数】");
                sendToTerminalHeader(ctx, session.getChannel(), terminalPhone,
                        JT808Const.cmd_terminal_param_query, countTool);
                break;

            // 查询指定终端参数 TODO
            case JT808Const.cmd_terminal_appoint_param_query:
                log.info("【查询指定终端参数】");
                queryTerminalAppointParam(ctx, session.getChannel(), terminalPhone, msg.getTerminalAppointParamQueryMsg());
                break;

            // 查询终端属性
            case JT808Const.cmd_terminal_attribute_query:
                log.info("【查询终端属性】");
                sendToTerminalHeaderNoReplyFlowId(ctx, session.getChannel(), terminalPhone,
                        JT808Const.cmd_terminal_attribute_query);
                break;

            // 终端控制
            case JT808Const.cmd_terminal_control:
                log.info("【终端控制】");
                terminalControl(ctx, session.getChannel(), terminalPhone, msg.getTerminalControlMsg());
                break;

            // 位置信息查询
            case JT808Const.cmd_terminal_location_query:
                log.info("【位置信息查询】");
                sendToTerminalHeader(ctx, session.getChannel(), terminalPhone,
                        JT808Const.cmd_terminal_location_query, countTool);
                break;

            // 临时位置跟踪控制
            case JT808Const.cmd_temp_location_control:
                log.info("【临时位置跟踪控制】");
                tempLocationTrack(ctx, session.getChannel(), terminalPhone, msg.getTempLocationTrackMsg());
                break;

            // 车辆控制
            case JT808Const.cmd_car_control:
                log.info("【车辆控制】");
                carControl(ctx, session.getChannel(), terminalPhone, msg.getCarControl());
                break;

            // 人工确认报警消息
            case JT808Const.cmd_human_verify_warning_msg:
                log.info("【人工确认报警消息】");
                manualConfirmAlarm(ctx, session.getChannel(), terminalPhone, msg.getManualConfirmAlarmMsg());
                break;

            // 文本信息下发 TODO
            case JT808Const.cmd_text_download:
                log.info("【文本信息下发】");
                textMsg(ctx, session.getChannel(), terminalPhone, msg.getTextMsg());
                break;

            // 事件设置 TODO
            case JT808Const.cmd_event_setting:
                log.info("【事件设置】");
                break;

            // 提问下发 TODO
            case JT808Const.cmd_ask_download:
                log.info("【提问下发】");
                break;

            // 信息点播菜单设置 TODO
            case JT808Const.cmd_message_request_setting:
                log.info("【信息点播菜单设置】");
                break;

            // 行驶记录仪数据采集命令 TODO
            case JT808Const.cmd_recorder_data_request:
                log.info("【行驶记录仪数据采集命令】");
                break;

            // 行驶记录仪参数下传命令 TODO
            case JT808Const.cmd_recorder_param_down:
                log.info("【行驶记录仪参数下传命令】");
                break;

            // 上报驾驶员身份信息请求 TODO
            case JT808Const.cmd_Identity_information_request:
                log.info("【上报驾驶员身份信息请求】 本消息有误");
                sendToTerminalHeaderNoReplyFlowId(ctx, session.getChannel(), terminalPhone, JT808Const.cmd_Identity_information_request);
                break;

            // 摄像头立即拍摄命令
            case JT808Const.cmd_camera_photo:
                log.info("【摄像头立即拍摄】");
                cameraPhoto(ctx, session.getChannel(), terminalPhone, msg.getCameraPhoto());
                break;

            // 录音开始
            case JT808Const.cmd_record_start:
                log.info("【录音开始】");
                voiceRecord(ctx, session.getChannel(), terminalPhone, msg.getVoiceRecord());
                break;

            default:
                log.info("【未识别命令】");
        }
    }

    private void sendToTerminalHeader(ChannelHandlerContext ctx, Channel channel, String terminalPhone, int code, CountTool countTool) throws Exception {
        BusinessManager businessManager = BusinessManager.getInstance();
        int replyFlowId = businessManager.currentFlowId();
        businessManager.putByReplyFlowId(replyFlowId, ctx.channel());
        ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(JT808ProtocolUtils.sendToTerminalHeader(replyFlowId, terminalPhone, code)));
        future.addListener((ChannelFutureListener) futureListener -> futureListener.channel().eventLoop().schedule(() -> {
            int count = countTool.getN();
            int retransmissionTime = countTool.getRetransmissionTime();
            if (futureListener.isSuccess() && (BusinessManager.getInstance().findChannelByReplyFlowId(replyFlowId) == null)) {
                log.info("【发送成功】");
            } else {
                if (count < JT808Const.retransmission) {
                    try {
                        log.info("【发送失败】　第 " +  count + " 次尝试");
                        sendToTerminalHeader(ctx, channel, terminalPhone, code, countTool);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                count++;
                retransmissionTime *= count;
                countTool.setN(count);
                countTool.setRetransmissionTime(retransmissionTime);

            }
        }, countTool.getRetransmissionTime(), TimeUnit.SECONDS));
    }

    private void sendToTerminal(byte[] msgBody, ChannelHandlerContext ctx, Channel channel, String terminalPhone, int code, CountTool countTool) throws Exception {
        BusinessManager businessManager = BusinessManager.getInstance();
        int replyFlowId = businessManager.currentFlowId();
        businessManager.putByReplyFlowId(replyFlowId, ctx.channel());
        ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(JT808ProtocolUtils.sendToTerminal(msgBody, replyFlowId, terminalPhone, code)));
        future.addListener((ChannelFutureListener) futureListener -> futureListener.channel().eventLoop().schedule(() -> {
            int count = countTool.getN();
            int retransmissionTime = countTool.getRetransmissionTime();
            if (futureListener.isSuccess() && (BusinessManager.getInstance().findChannelByReplyFlowId(replyFlowId) == null)) {
                log.info("【发送成功】");
            } else {
                if (count < JT808Const.retransmission) {
                    try {
                        log.info("【发送失败】　第 " +  count + " 次尝试");
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
        }, countTool.getRetransmissionTime(), TimeUnit.SECONDS));
    }

    private void sendToTerminalHeaderNoReplyFlowId(ChannelHandlerContext ctx, Channel channel, String terminalPhone, int code) throws Exception {
        BusinessManager businessManager = BusinessManager.getInstance();
        int replyFlowId = businessManager.currentFlowId();
        businessManager.putByTerminalPhone(terminalPhone, ctx.channel());
        channel.writeAndFlush(Unpooled.copiedBuffer(JT808ProtocolUtils.sendToTerminalHeader(replyFlowId, terminalPhone, code)));
    }

    // TODO 太复杂
    private void setTerminalParam(ChannelHandlerContext ctx, Channel channel, String terminalPhone, ClientData.SettingTerminalParamMsg settingTerminalParamMsg) throws Exception {
        //JT808Const.cmd_terminal_param_settings
    }

    private void queryTerminalAppointParam(ChannelHandlerContext ctx, Channel channel, String terminalPhone, ClientData.TerminalAppointParamQueryMsg terminalAppointParamQueryMsg) throws Exception {
        int num = terminalAppointParamQueryMsg.getParamNumber();
        byte[] data = new byte[num * 4 + 1];
        data[0] = BitOperator.integerTo1Byte(num);
        for (int i = 0; i < num; i++) {
            System.arraycopy(BitOperator.integerTo4Bytes(terminalAppointParamQueryMsg.getParamIdListList().get(i)), 0, data, i * 4 + 1, 4);
        }
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_terminal_appoint_param_query, countTool);
    }

    // TODO 有bug
    private void terminalControl(ChannelHandlerContext ctx, Channel channel, String terminalPhone, ClientData.TerminalControlMsg terminalControlMsg) throws Exception {
        int command = terminalControlMsg.getCommand();
        CountTool countTool = new CountTool();
        if (command == 1 || command == 2) {
            byte[] data = BitOperator.concatAll(BitOperator.integerTo1Bytes(command),terminalControlMsg.getCommandParam().getBytes());
            sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_terminal_control, countTool);
        } else {
            byte[] data = BitOperator.integerTo1Bytes(command);
            sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_terminal_control, countTool);
        }
    }

    private void tempLocationTrack(ChannelHandlerContext ctx, Channel channel, String terminalPhone, ClientData.TempLocationTrackMsg tempLocationTrackMsg) throws Exception {
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo2Bytes(tempLocationTrackMsg.getInterval()),
                BitOperator.integerTo2Bytes(tempLocationTrackMsg.getLocationTrackValidTerm())
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_temp_location_control, countTool);
    }

    private void manualConfirmAlarm(ChannelHandlerContext ctx, Channel channel, String terminalPhone, ClientData.ManualConfirmAlarmMsg manualConfirmAlarmMsg) throws Exception {
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo2Bytes(manualConfirmAlarmMsg.getAlarmFlowId()),
                BitOperator.integerTo2Bytes(manualConfirmAlarmMsg.getManualConfirmAlarmType())
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_human_verify_warning_msg, countTool);
    }

    private void textMsg(ChannelHandlerContext ctx, Channel channel, String terminalPhone, ClientData.TextMsg txtMsg) throws Exception {
        byte[] data = BitOperator.concatAll(BitOperator.integerTo2Bytes(txtMsg.getFlag()), txtMsg.getTextMsg().getBytes());
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_text_download, countTool);
    }

    private void cameraPhoto(ChannelHandlerContext ctx, Channel channel, String terminalPhone, ClientData.CameraPhoto cameraPhoto) throws Exception {
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(cameraPhoto.getChannelId()),
                BitOperator.integerTo2Bytes(cameraPhoto.getPhotoCommand()),
                BitOperator.integerTo2Bytes(cameraPhoto.getCameraTime()),
                BitOperator.integerTo1Bytes(cameraPhoto.getSaveFlag()),
                BitOperator.integerTo1Bytes(cameraPhoto.getResolving()),
                BitOperator.integerTo1Bytes(cameraPhoto.getCameraQuality()),
                BitOperator.integerTo1Bytes(cameraPhoto.getLight()),
                BitOperator.integerTo1Bytes(cameraPhoto.getContrast()),
                BitOperator.integerTo1Bytes(cameraPhoto.getSaturation()),
                BitOperator.integerTo1Bytes(cameraPhoto.getColor())
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_camera_photo, countTool);
    }

    private void voiceRecord(ChannelHandlerContext ctx, Channel channel, String terminalPhone, ClientData.VoiceRecord voiceRecord) throws Exception {
        byte[] data = BitOperator.concatAll(
                BitOperator.integerTo1Bytes(voiceRecord.getVoiceRecordCommand()),
                BitOperator.integerTo2Bytes(voiceRecord.getVoiceRecordTime()),
                BitOperator.integerTo1Bytes(voiceRecord.getSaveFlag()),
                BitOperator.integerTo1Bytes(voiceRecord.getAudioSamplingRate())
        );
        CountTool countTool = new CountTool();
        sendToTerminal(data, ctx, channel, terminalPhone, JT808Const.cmd_record_start, countTool);
    }

    private void carControl(ChannelHandlerContext ctx, Channel channel, String terminalPhone, ClientData.CarControl carControl) throws Exception {
        CountTool countTool = new CountTool();
        sendToTerminal(BitOperator.integerTo1Bytes(carControl.getControlFlag()), ctx, channel, terminalPhone, JT808Const.cmd_car_control, countTool);
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel());
    }
}
