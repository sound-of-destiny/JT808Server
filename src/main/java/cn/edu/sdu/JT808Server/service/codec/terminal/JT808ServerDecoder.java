package cn.edu.sdu.JT808Server.service.codec.terminal;

import cn.edu.sdu.JT808Server.protobuf.ServerData;
import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;
import cn.edu.sdu.JT808Server.protocol.Session;
import cn.edu.sdu.JT808Server.protocol.upMsg.*;
import cn.edu.sdu.JT808Server.server.ChannelGroupManager;
import cn.edu.sdu.JT808Server.server.PackageManager;
import cn.edu.sdu.JT808Server.server.SessionManager;
import cn.edu.sdu.JT808Server.util.*;
import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class JT808ServerDecoder extends ByteToMessageDecoder {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final String QUEUE_NAME = "JT808Server_OriginData_Queue";

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
                log.info("【终端断开】 " + ctx.channel());
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("【终端连接】 " + ctx.channel());
        Session session = SessionManager.getInstance().findBySessionId(ctx.channel().id().asLongText());
        if(session != null) {
            session.setLocalDateTime(LocalDateTime.now());
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws  Exception {

        PackageData pkg = new PackageData();
        byte[] b = new byte[in.readableBytes()];
        in.readBytes(b);
        byte[] bs = JT808ProtocolUtils.doEscape4Receive(b, 0, b.length - 1);

        MQUtil.toQueue(QUEUE_NAME, bs);

        // 1. 消息头 16byte 或 12byte
        MsgHeader msgHeader = parseMsgHeaderFromBytes(bs);
        pkg.setMsgHeader(msgHeader);

        // 2. 消息体 有子包信息,消息体起始字节后移四个字节:消息包总数(word(16))+包序号(word(16))
        int msgBodyStartIndex = 12;
        if (msgHeader.isHasSubPackage()) {
            msgBodyStartIndex = 16;
        }

        // 3. 将数据存入消息体
        byte[] tmp = new byte[msgHeader.getMsgBodyLength()];
        System.arraycopy(bs, msgBodyStartIndex, tmp, 0, tmp.length);
        pkg.setMsgBody(tmp);

        // ４. 去掉分隔符之后，最后一位就是校验码
        int checkSumInPkg = bs[bs.length - 1];
        int calculatedCheckSum = BitOperator.getCheckSum4JT808(bs, 0, bs.length - 1);
        pkg.setCheckSum(checkSumInPkg);
        if (checkSumInPkg != calculatedCheckSum) {
            log.error("【检验码不一致】");
        }

        final int msgHeaderId = msgHeader.getMsgId();

        if (msgHeaderId == JT808Const.msg_id_terminal_authentication) {
            // 终端鉴权
            TerminalAuthenticationMsg authenticationMsg = toTerminalAuthenticationMsg(pkg);
            out.add(authenticationMsg);
            return;
        } else if(msgHeaderId == JT808Const.msg_id_terminal_register) {
            // 终端注册
            TerminalRegisterMsg registerMsg = toTerminalRegisterMsg(pkg);
            out.add(registerMsg);
            return;
        }

        ChannelGroupManager channelGroupManager = ChannelGroupManager.getInstance();
        ChannelGroup channelGroupTerminal = channelGroupManager.findByChannelGroupName("Terminal");
        if (channelGroupTerminal == null)
            channelGroupManager.put("Terminal", channelGroup);
        else if (channelGroupTerminal.find(ctx.channel().id()) == null) {
            log.info("【非法连接】 ！！！！！！！！！！");
            return;
        }

        switch (msgHeaderId) {

            // 终端通用应答
            case JT808Const.msg_id_terminal_common_resp:
                TerminalCommonResponseMsg terminalCommonResponseMsg = toTerminalCommonResponseMsg(pkg);
                out.add(terminalCommonResponseMsg);
                break;

            // 终端心跳
            case JT808Const.msg_id_terminal_heart_beat:
                out.add(msgHeader);
                break;

            // 终端注销
            case JT808Const.msg_id_terminal_log_out:
                log.info("【终端注销】");
                out.add(msgHeader);
                break;

            // 查询终端参数应答
            case JT808Const.msg_id_terminal_param_query_resp:
                ServerData.TerminalParamQueryResponseMsg terminalParamQueryResponseMsg = toTerminalParamQueryResponseMsg(pkg);
                out.add(terminalParamQueryResponseMsg);
                break;

            // 查询终端属性应答
            case JT808Const.msg_id_terminal_attribute_query_resp:
                ServerData.TerminalAttributeQueryResponseMsg terminalAttributeQueryResponseMsg = toTerminalAttributeQueryResponseMsg(pkg);
                out.add(terminalAttributeQueryResponseMsg);
                break;

            // 位置信息
            case JT808Const.msg_id_terminal_location:
                ServerData.TerminalLocationMsg terminalLocationMsg = toTerminalLocationMsg(pkg);
                out.add(terminalLocationMsg);
                break;

            // 位置信息查询应答
            case JT808Const.msg_id_terminal_location_query_resp:
                TerminalLocationRequestMsg terminalLocationRequestMsg = toTerminalLocationRequestMsg(pkg);
                out.add(terminalLocationRequestMsg);
                break;

            // 事件报告
            case JT808Const.msg_id_event_report:
                log.info("【事件报告】");
                final byte[] pkgBodyEvent = pkg.getMsgBody();
                int eventId = parseIntFromBytes(pkgBodyEvent, 0, 1);
                out.add(eventId);
                break;

            // 提问应答
            case JT808Const.msg_id_ask_answer:
                log.info("【提问应答】");
                final byte[] pkgBodyAnswer = pkg.getMsgBody();
                int replyFlowIdAnswer = parseIntFromBytes(pkgBodyAnswer, 0, 2);
                int answer = parseIntFromBytes(pkgBodyAnswer, 2, 1);
                out.add(answer);
                break;

            // 信息点播/取消
            case JT808Const.msg_id_message_request_cancel:
                log.info("【信息点播/取消】");
                TerminalMessageRequestMsg terminalMessageRequestMsg = toTerminalMessageRequestMsg(pkg);
                out.add(terminalMessageRequestMsg);
                break;

            // 定位数据批量上传
            case JT808Const.msg_id_bulk_location_upload:
                log.info("【定位数据批量上传】");
                ServerData.TerminalBulkLocationMsg terminalBulkLocationMsg = toTerminalBulkLocationMsg(pkg);
                out.add(terminalBulkLocationMsg);
                break;

            // 车辆控制应答
            case JT808Const.msg_id_car_control_resp:
                log.info("【车辆控制应答】");
                TerminalCarControlResponseMsg terminalCarControlResponseMsg = toTerminalCarControlResponseMsg(pkg);
                out.add(terminalCarControlResponseMsg);
                break;

            // 行驶记录仪数据上传 todo GBT 19056
            case JT808Const.msg_id_recorder_data_upload:
                log.info("【行驶记录仪数据上传】");
                //out.add(msgHeader);
                break;

            // 电子运单上报
            case JT808Const.msg_id_digital_data_upload:
                ServerData.TerminalDigitWaybill terminalDigitWaybill = toTerminalDigitWaybill(pkg);
                out.add(terminalDigitWaybill);
                break;

            // 驾驶员身份信息采集上报
            case JT808Const.msg_id_Identity_information_upload:
                ServerData.TerminalDriverIdentityMsg terminalDriverIdentityMsg = toTerminalDriverIdentityMsg(pkg);
                out.add(terminalDriverIdentityMsg);
                break;

            // 多媒体事件信息上传
            case JT808Const.msg_id_media_event_upload:
                log.info("【多媒体事件信息上传】");
                TerminalMediaEventMsg terminalMediaEventMsg = toTerminalMediaEventMsg(pkg);
                out.add(terminalMediaEventMsg);
                break;

            // 多媒体数据上传
            case JT808Const.msg_id_media_upload:
                log.info("【多媒体数据上传】");
                PackageManager packageManager = PackageManager.getInstance();
                Map map = packageManager.findByTerminalPhone(msgHeader.getTerminalPhone());
                if (map == null) {
                    map = new ConcurrentHashMap();
                    packageManager.putByTerminalPhone(msgHeader.getTerminalPhone(), map);
                }
                map.put(msgHeader.getSubPackageSeq(), pkg);
                if (map.keySet().size() == msgHeader.getTotalSubPackage()) {
                    byte[] body = ((PackageData)map.get(1)).getMsgBody();
                    for (int i = 2; i < map.keySet().size() + 1; i++) {
                        body = BitOperator.concatAll(
                                body,
                                ((PackageData)map.get(i)).getMsgBody()
                        );
                    }
                    PackageData npkg = new PackageData(((PackageData)map.get(1)).getMsgHeader(), body, (((PackageData) map.get(1)).getCheckSum()));
                    ServerData.TerminalMediaDataMsg terminalMediaDataMsg = toTerminalMediaDataMsg(npkg);
                    out.add(terminalMediaDataMsg);
                    packageManager.removeByTerminalPhone(msgHeader.getTerminalPhone());
                }
                break;

            // 摄像头立即拍摄命令应答
            case JT808Const.msg_id_camera_photo_response:
                log.info("【摄像头立即拍摄命令应答】");
                ServerData.TerminalCameraPhotoResponseMsg terminalCameraPhotoResponseMsg = toTerminalCameraPhotoResponseMsg(pkg);
                out.add(terminalCameraPhotoResponseMsg);
                break;

            // 存储多媒体数据检索应答
            case JT808Const.msg_id_saved_media_response:
                log.info("【存储多媒体数据检索应答】");
                ServerData.TerminalSavedMediaRetrievalResponseMsg terminalSavedMediaRetrievalResponseMsg = toTerminalSavedMediaRetrievalResponseMsg(pkg);
                out.add(terminalSavedMediaRetrievalResponseMsg);
                break;

            // CAN 总线数据上传
            case JT808Const.msg_id_CAN_data_upload:
                log.info("【CAN 总线数据上传】");
                break;

            // 数据上行透传
            case JT808Const.msg_id_data_upload:
                log.info("【数据上行透传】");
                break;

            // 数据压缩上报
            case JT808Const.msg_id_data_zip_upload:
                log.info("【数据压缩上报】");
                break;

            // 终端 RSA 公钥
            case JT808Const.msg_id_terminal_RSA:
                log.info("【终端 RSA 公钥】");
                break;



            // 终端上传音视频属性
            case JT808Const.msg_id_Terminal_upload_media_attribute:
                log.info("【终端上传音视频属性】");
                ServerData.TerminalUploadMediaAttributeMsg terminalUploadMediaAttributeMsg = toTerminalUploadMediaAttributeMsg(pkg);
                out.add(terminalUploadMediaAttributeMsg);
                break;

            // 终端上传音视频资源列表
            case JT808Const.msg_id_Terminal_upload_media_resourse_list:
                log.info("【终端上传音视频资源列表】");
                ServerData.TerminalUploadMediaResourcesMsg terminalUploadMediaResourcesMsg = toTerminalUploadMediaResourcesMsg(pkg);
                out.add(terminalUploadMediaResourcesMsg);
                break;

            default:
                log.info("【其他消息...】");

        }
    }

    private int parseIntFromBytes(byte[] data, int startIndex, int length) {
        return parseIntFromBytes(data, startIndex, length, 0);
    }

    private int parseIntFromBytes(byte[] data, int startIndex, int length, int defaultVal) {
        try {
            // 字节数大于4,从起始索引开始向后处理4个字节,其余超出部分丢弃
            final int len = length > 4 ? 4 : length;
            byte[] tmp = new byte[len];
            System.arraycopy(data, startIndex, tmp, 0, len);
            return BitOperator.byteToInteger(tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultVal;
        }
    }

    private String parseBCDStringFromBytes(byte[] data, int startIndex, int lenth) {
        return this.parseBCDStringFromBytes(data, startIndex, lenth, null);
    }

    private String parseBCDStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
        try {
            byte[] tmp = new byte[lenth];
            System.arraycopy(data, startIndex, tmp, 0, lenth);
            return BCD8421Operator.bcd2String(tmp);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultVal;
        }
    }

    private boolean parseBooleanFromBytes(byte[] data, int startIndex, int length, int index) {
        byte[] tmp = new byte[length];
        System.arraycopy(data, startIndex, tmp, 0, length);
        return BitOperator.byteToBit(tmp).charAt(index) == '1';
    }

    private int parseIntFrom2Bit(byte[] data, int startIndex, int length, int start, int end) {
        byte[] tmp = new byte[length];
        System.arraycopy(data, startIndex, tmp, 0, length);
        return Integer.parseInt(BitOperator.byteToBit(tmp).substring(start, end));
    }

    /**
     * 解析消息头
     */
    private MsgHeader parseMsgHeaderFromBytes(byte[] data) {
        MsgHeader msgHeader = new MsgHeader();

        // 1. 消息ID word(16)
        msgHeader.setMsgId(parseIntFromBytes(data, 0, 2));

        // 2. 消息体属性 word(16)
        int msgBodyProps = parseIntFromBytes(data, 2, 2);
        msgHeader.setMsgBodyPropsField(msgBodyProps);
        // [0-9] 0000,0011,1111,1111(3FF)(消息体长度)
        msgHeader.setMsgBodyLength(msgBodyProps & 0x3ff);
        // [10-12] 0001,1100,0000,0000(1C00)(加密类型)
        msgHeader.setEncryptionType((msgBodyProps & 0x1c00) >> 10);
        // [ 13 ] 0010,0000,0000,0000(2000)(是否有子包)
        msgHeader.setHasSubPackage(((msgBodyProps & 0x2000) >> 13) == 1);
        // [14-15] 1100,0000,0000,0000(C000)(保留位)
        msgHeader.setReservedBit(((msgBodyProps & 0xc000) >> 14));

        // 3. 终端手机号 bcd[6]
        msgHeader.setTerminalPhone(parseBCDStringFromBytes(data, 4, 6));

        // 4. 消息流水号 word(16) 按发送顺序从 0 开始循环累加
        msgHeader.setFlowId(parseIntFromBytes(data, 10, 2));

        // 5. 消息包封装项
        // 有子包信息
        if (msgHeader.isHasSubPackage()) {
            // 消息包封装项字段
            msgHeader.setPackageInfoField(parseIntFromBytes(data, 12, 4));
            // byte[0-1] 消息包总数(word(16))
            msgHeader.setTotalSubPackage(parseIntFromBytes(data, 12, 2));
            // byte[2-3] 包序号(word(16)) 从 1 开始
            msgHeader.setSubPackageSeq(parseIntFromBytes(data, 14, 2));
        }
        return msgHeader;
    }

    private TerminalCommonResponseMsg toTerminalCommonResponseMsg(PackageData packageData) {
        TerminalCommonResponseMsg ret = new TerminalCommonResponseMsg(packageData);
        final byte[] data = packageData.getMsgBody();
        ret.setReplyFlowId(parseIntFromBytes(data, 0, 2));
        ret.setReplyId(parseIntFromBytes(data, 2, 2));
        ret.setReplyCode(parseIntFromBytes(data, 4, 1));
        return ret;
    }

    /**
     * 解析终端注册
     */
    private TerminalRegisterMsg toTerminalRegisterMsg(PackageData packageData) {
        TerminalRegisterMsg ret = new TerminalRegisterMsg(packageData);
        final byte[] data = packageData.getMsgBody();

        // 1. byte[0-1] 省域ID(WORD)
        // 设备安装车辆所在的省域，省域ID采用GB/T2260中规定的行政区划代码6位中前两位
        // 0保留，由平台取默认值
        ret.setProvinceId(parseIntFromBytes(data, 0, 2));

        // 2. byte[2-3] 设备安装车辆所在的市域或县域,市县域ID采用GB/T2260中规定的行 政区划代码6位中后四位
        // 0保留，由平台取默认值
        ret.setCityId(parseIntFromBytes(data, 2, 2));

        // 3. byte[4-8] 制造商ID(BYTE[5]) 5 个字节，终端制造商编码
        // byte[] tmp = new byte[5];
        ret.setManufacturerId(parseStringFromBytes(data, 4, 5));

        // 4. byte[9-16] 终端型号(BYTE[8]) 八个字节， 此终端型号 由制造商自行定义 位数不足八位的，补空格。
        ret.setTerminalType(parseStringFromBytes(data, 9, 8));

        // 5. byte[17-23] 终端ID(BYTE[7]) 七个字节， 由大写字母 和数字组成， 此终端 ID由制造 商自行定义
        ret.setTerminalId(parseStringFromBytes(data, 17, 7));

        // 6. byte[24] 车牌颜色(BYTE) 车牌颜 色按照JT/T415-2006 中5.4.12 的规定
        ret.setLicensePlateColor(parseIntFromBytes(data, 24, 1));

        // 7. byte[25-x] 车牌(STRING) 公安交 通管理部门颁 发的机动车号牌
        ret.setLicensePlate(parseStringFromBytes(data, 25, data.length - 25));

        return ret;
    }

    private String parseStringFromBytes(byte[] data, int startIndex, int lenth) {
        return this.parseStringFromBytes(data, startIndex, lenth, null);
    }

    private String parseStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
        try {
            byte[] tmp = new byte[lenth];
            System.arraycopy(data, startIndex, tmp, 0, lenth);
            return new String(tmp, JT808Const.string_charset);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultVal;
        }
    }

    /**
     * 解析鉴权信息
     */
    private TerminalAuthenticationMsg toTerminalAuthenticationMsg(PackageData packageData) {
        TerminalAuthenticationMsg ret = new TerminalAuthenticationMsg(packageData);
        final byte[] data = packageData.getMsgBody();
        // 设置鉴权信息
        ret.setAuthCode(new String(data));
        return ret;
    }

    /**
     * 解析位置信息
     */

    private ServerData.TerminalLocationMsg getTerminalLocationMsg(byte[] data, String terminalPhone, int flowId) throws Exception {
        ServerData.TerminalLocationMsg.Builder ret =  ServerData.TerminalLocationMsg.newBuilder();
        // 1. byte[0-3] 报警标志(DWORD(32))
        ServerData.TerminalLocationMsg.WarningFlag.Builder warningFlag = ServerData.TerminalLocationMsg.WarningFlag.newBuilder();
        warningFlag.setWarningFlag(parseBooleanFromBytes(data, 0, 4, 0));
        warningFlag.setOverSpeeding(parseBooleanFromBytes(data, 0, 4, 1));
        warningFlag.setOverTired(parseBooleanFromBytes(data, 0, 4, 2));
        warningFlag.setDangerous(parseBooleanFromBytes(data, 0, 4, 3));
        warningFlag.setGNSSFault(parseBooleanFromBytes(data, 0, 4, 4));
        warningFlag.setGNSSAntennaFault(parseBooleanFromBytes(data, 0, 4, 5));
        warningFlag.setGNSSAntennaShortCircuit(parseBooleanFromBytes(data, 0, 4, 6));
        warningFlag.setTerminalMainPowerUnderVoltage(parseBooleanFromBytes(data, 0, 4, 7));
        warningFlag.setTerminalMainPowerFailure(parseBooleanFromBytes(data, 0, 4, 8));
        warningFlag.setTerminalLCDFault(parseBooleanFromBytes(data, 0, 4, 9));
        warningFlag.setTTSFault(parseBooleanFromBytes(data, 0, 4, 10));
        warningFlag.setCameraFault(parseBooleanFromBytes(data, 0, 4, 11));
        warningFlag.setICCardFault(parseBooleanFromBytes(data, 0, 4, 12));
        warningFlag.setSpeeding(parseBooleanFromBytes(data, 0, 4, 13));
        warningFlag.setTired(parseBooleanFromBytes(data, 0, 4, 14));
        warningFlag.setDriveTimeout(parseBooleanFromBytes(data, 0, 4, 18));
        warningFlag.setParkingOvertime(parseBooleanFromBytes(data, 0, 4, 19));
        warningFlag.setThroughArea(parseBooleanFromBytes(data, 0, 4, 20));
        warningFlag.setThroughRoad(parseBooleanFromBytes(data, 0, 4, 21));
        warningFlag.setRoadTimeout(parseBooleanFromBytes(data, 0, 4, 22));
        warningFlag.setRoadFault(parseBooleanFromBytes(data, 0, 4, 23));
        warningFlag.setVSSFault(parseBooleanFromBytes(data, 0, 4, 24));
        warningFlag.setVehicleOilException(parseBooleanFromBytes(data, 0, 4, 25));
        warningFlag.setVehicleTheft(parseBooleanFromBytes(data, 0, 4, 26));
        warningFlag.setVehicleIllegalIgnition(parseBooleanFromBytes(data, 0, 4, 27));
        warningFlag.setVehicleIllegalShift(parseBooleanFromBytes(data, 0, 4, 28));
        warningFlag.setCollisionWarning(parseBooleanFromBytes(data, 0, 4, 29));
        warningFlag.setRolloverWarning(parseBooleanFromBytes(data, 0, 4, 30));
        warningFlag.setIllegalOpenDoor(parseBooleanFromBytes(data, 0, 4, 31));
        ret.setWarningFlag(warningFlag.build());
        // 2. byte[4-7] 状态(DWORD(32))
        ServerData.TerminalLocationMsg.Status.Builder status = ServerData.TerminalLocationMsg.Status.newBuilder();
        status.setACC(parseBooleanFromBytes(data, 4, 4, 0));
        status.setIsLocation(parseBooleanFromBytes(data, 4, 4, 1));
        status.setLatitude(parseBooleanFromBytes(data, 4, 4, 2));
        status.setLongitude(parseBooleanFromBytes(data, 4, 4, 3));
        status.setIsRunning(parseBooleanFromBytes(data, 4, 4, 4));
        status.setEncrypt(parseBooleanFromBytes(data, 4, 4, 5));
        status.setGoodsStatus(parseIntFrom2Bit(data, 4, 4, 6, 8));
        status.setVehicleOil(parseBooleanFromBytes(data, 4, 4, 10));
        status.setVehicleCircut(parseBooleanFromBytes(data, 4, 4, 11));
        status.setDoorLock(parseBooleanFromBytes(data, 4, 4, 12));
        status.setFrontDoorOpen(parseBooleanFromBytes(data, 4, 4, 13));
        status.setMiddleDoorOpen(parseBooleanFromBytes(data, 4, 4, 14));
        status.setEndDoorOpen(parseBooleanFromBytes(data, 4, 4, 15));
        status.setDriverDoorOpen(parseBooleanFromBytes(data, 4, 4, 16));
        status.setOtherDoorOpen(parseBooleanFromBytes(data, 4, 4, 17));
        status.setGPS(parseBooleanFromBytes(data, 4, 4, 18));
        status.setBeidou(parseBooleanFromBytes(data, 4, 4, 19));
        status.setGLONASS(parseBooleanFromBytes(data, 4, 4, 20));
        status.setGalileo(parseBooleanFromBytes(data, 4, 4, 21));
        ret.setStatus(status.build());
        // 3. byte[8-11] 纬度(DWORD(32)) 以度为单位的纬度值乘以10^6，精确到百万分之一度
        ret.setLatitude((double)parseIntFromBytes(data, 8, 4)/1000000);
        // 4. byte[12-15] 经度(DWORD(32)) 以度为单位的经度值乘以10^6，精确到百万分之一度
        ret.setLongitude((double)parseIntFromBytes(data, 12, 4)/1000000);
        // 5. byte[16-17] 高程(WORD(16)) 海拔高度，单位为米（ m）
        ret.setElevation(parseIntFromBytes(data, 16, 2));
        // byte[18-19] 速度(WORD) 1/10km/h
        ret.setSpeed((double)parseIntFromBytes(data, 18, 2));
        // byte[20-21] 方向(WORD) 0-359，正北为 0，顺时针
        ret.setDirection(parseIntFromBytes(data, 20, 2));
        // byte[22-x] 时间(BCD[6]) YY-MM-DD-hh-mm-ss
        // GMT+8 时间，本标准中之后涉及的时间均采用此时区
        String sd = parseBCDStringFromBytes(data,  22, 6);
        String time = String.format("20%s-%s-%s %s:%s:%s",sd.substring(0, 2), sd.substring(2, 4), sd.substring(4, 6),
                sd.substring(6, 8), sd.substring(8, 10), sd.substring(10, 12));
        ret.setTime(time);

        int index = 28;
        ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.Builder terminalExtraLocationMsg = ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.newBuilder();
        while (data.length > index) {
            int extraDataId = parseIntFromBytes(data, index, 1);
            int length = parseIntFromBytes(data, index + 1, 1);
            switch (extraDataId) {
                case 0x01:
                    terminalExtraLocationMsg.setMileage(parseIntFromBytes(data, index + 2, length));
                    break;
                case 0x02:
                    terminalExtraLocationMsg.setOilQuantity(parseIntFromBytes(data, index + 2, length));
                    break;
                case 0x03:
                    terminalExtraLocationMsg.setCarSpeed(parseIntFromBytes(data, index + 2, length));
                    break;
                case 0x04:
                    terminalExtraLocationMsg.setWarningId(parseIntFromBytes(data, index + 2, length));
                    break;
                case 0x11:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.SpeedingExtraData.Builder speedingExtraData =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.SpeedingExtraData.newBuilder();
                    int locationType = parseIntFromBytes(data, index + 2, 1);
                    speedingExtraData.setLocationType(locationType);
                    if (locationType != 0){
                        speedingExtraData.setLocationId(parseStringFromBytes(data, index + 3, length - 1));
                    }
                    terminalExtraLocationMsg.setSpeedingExtraData(speedingExtraData.build());
                    break;
                case 0x12:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.LocationExtraData.Builder locationExtraData =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.LocationExtraData.newBuilder();
                    locationExtraData.setLocationType(parseIntFromBytes(data, index + 2, 1));
                    locationExtraData.setLocationId(parseStringFromBytes(data, index + 3, 4));
                    locationExtraData.setDirection(parseIntFromBytes(data, index + 7, 1));
                    terminalExtraLocationMsg.setLocationExtraData(locationExtraData.build());
                    break;
                case 0x13:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.LocationInfoExtraData.Builder locationInfoExtraData =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.LocationInfoExtraData.newBuilder();
                    locationInfoExtraData.setRoadId(parseStringFromBytes(data, index + 2, 4));
                    locationInfoExtraData.setRunTime(parseIntFromBytes(data, index + 6, 2));
                    locationInfoExtraData.setResult(parseIntFromBytes(data, index + 8, 1));
                    terminalExtraLocationMsg.setLocationInfoExtraData(locationInfoExtraData.build());
                    break;
                case 0x14:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.VideoAlarm.Builder videoAlarm =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.VideoAlarm.newBuilder();
                    videoAlarm.setVideoLost(parseBooleanFromBytes(data, index + 2, 4, 0));
                    videoAlarm.setVideoShelter(parseBooleanFromBytes(data, index + 2, 4, 1));
                    videoAlarm.setStorageFault(parseBooleanFromBytes(data, index + 2, 4, 2));
                    videoAlarm.setOtherFault(parseBooleanFromBytes(data, index + 2, 4, 3));
                    videoAlarm.setOvercrowding(parseBooleanFromBytes(data, index + 2, 4, 4));
                    videoAlarm.setExceptBehavior(parseBooleanFromBytes(data, index + 2, 4, 5));
                    videoAlarm.setVideoStorageOver(parseBooleanFromBytes(data, index + 2, 4, 6));
                    terminalExtraLocationMsg.setVideoAlarm(videoAlarm);
                    break;
                case 0x15:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.VideoLostFlag.Builder videoLostFlag =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.VideoLostFlag.newBuilder();
                    videoLostFlag.setLogicChannel1(parseBooleanFromBytes(data, index + 2, 4, 0));
                    videoLostFlag.setLogicChannel2(parseBooleanFromBytes(data, index + 2, 4, 1));
                    videoLostFlag.setLogicChannel3(parseBooleanFromBytes(data, index + 2, 4, 2));
                    videoLostFlag.setLogicChannel4(parseBooleanFromBytes(data, index + 2, 4, 3));
                    videoLostFlag.setLogicChannel5(parseBooleanFromBytes(data, index + 2, 4, 4));
                    videoLostFlag.setLogicChannel6(parseBooleanFromBytes(data, index + 2, 4, 5));
                    videoLostFlag.setLogicChannel7(parseBooleanFromBytes(data, index + 2, 4, 6));
                    videoLostFlag.setLogicChannel8(parseBooleanFromBytes(data, index + 2, 4, 7));
                    videoLostFlag.setLogicChannel9(parseBooleanFromBytes(data, index + 2, 4, 8));
                    videoLostFlag.setLogicChannel10(parseBooleanFromBytes(data, index + 2, 4, 9));
                    videoLostFlag.setLogicChannel11(parseBooleanFromBytes(data, index + 2, 4, 10));
                    videoLostFlag.setLogicChannel12(parseBooleanFromBytes(data, index + 2, 4, 11));
                    videoLostFlag.setLogicChannel13(parseBooleanFromBytes(data, index + 2, 4, 12));
                    videoLostFlag.setLogicChannel14(parseBooleanFromBytes(data, index + 2, 4, 13));
                    videoLostFlag.setLogicChannel15(parseBooleanFromBytes(data, index + 2, 4, 14));
                    videoLostFlag.setLogicChannel16(parseBooleanFromBytes(data, index + 2, 4, 15));
                    videoLostFlag.setLogicChannel17(parseBooleanFromBytes(data, index + 2, 4, 16));
                    videoLostFlag.setLogicChannel18(parseBooleanFromBytes(data, index + 2, 4, 17));
                    videoLostFlag.setLogicChannel19(parseBooleanFromBytes(data, index + 2, 4, 18));
                    videoLostFlag.setLogicChannel20(parseBooleanFromBytes(data, index + 2, 4, 19));
                    videoLostFlag.setLogicChannel21(parseBooleanFromBytes(data, index + 2, 4, 20));
                    videoLostFlag.setLogicChannel22(parseBooleanFromBytes(data, index + 2, 4, 21));
                    videoLostFlag.setLogicChannel23(parseBooleanFromBytes(data, index + 2, 4, 22));
                    videoLostFlag.setLogicChannel24(parseBooleanFromBytes(data, index + 2, 4, 23));
                    videoLostFlag.setLogicChannel25(parseBooleanFromBytes(data, index + 2, 4, 24));
                    videoLostFlag.setLogicChannel26(parseBooleanFromBytes(data, index + 2, 4, 25));
                    videoLostFlag.setLogicChannel27(parseBooleanFromBytes(data, index + 2, 4, 26));
                    videoLostFlag.setLogicChannel28(parseBooleanFromBytes(data, index + 2, 4, 27));
                    videoLostFlag.setLogicChannel29(parseBooleanFromBytes(data, index + 2, 4, 28));
                    videoLostFlag.setLogicChannel30(parseBooleanFromBytes(data, index + 2, 4, 29));
                    videoLostFlag.setLogicChannel31(parseBooleanFromBytes(data, index + 2, 4, 30));
                    videoLostFlag.setLogicChannel32(parseBooleanFromBytes(data, index + 2, 4, 31));
                    terminalExtraLocationMsg.setVideoLostFlag(videoLostFlag.build());
                    break;
                case 0x16:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.VideoShelterFlag.Builder videoShelterFlag =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.VideoShelterFlag.newBuilder();
                    videoShelterFlag.setLogicChannel1(parseBooleanFromBytes(data, index + 2, 4, 0));
                    videoShelterFlag.setLogicChannel2(parseBooleanFromBytes(data, index + 2, 4, 1));
                    videoShelterFlag.setLogicChannel3(parseBooleanFromBytes(data, index + 2, 4, 2));
                    videoShelterFlag.setLogicChannel4(parseBooleanFromBytes(data, index + 2, 4, 3));
                    videoShelterFlag.setLogicChannel5(parseBooleanFromBytes(data, index + 2, 4, 4));
                    videoShelterFlag.setLogicChannel6(parseBooleanFromBytes(data, index + 2, 4, 5));
                    videoShelterFlag.setLogicChannel7(parseBooleanFromBytes(data, index + 2, 4, 6));
                    videoShelterFlag.setLogicChannel8(parseBooleanFromBytes(data, index + 2, 4, 7));
                    videoShelterFlag.setLogicChannel9(parseBooleanFromBytes(data, index + 2, 4, 8));
                    videoShelterFlag.setLogicChannel10(parseBooleanFromBytes(data, index + 2, 4, 9));
                    videoShelterFlag.setLogicChannel11(parseBooleanFromBytes(data, index + 2, 4, 10));
                    videoShelterFlag.setLogicChannel12(parseBooleanFromBytes(data, index + 2, 4, 11));
                    videoShelterFlag.setLogicChannel13(parseBooleanFromBytes(data, index + 2, 4, 12));
                    videoShelterFlag.setLogicChannel14(parseBooleanFromBytes(data, index + 2, 4, 13));
                    videoShelterFlag.setLogicChannel15(parseBooleanFromBytes(data, index + 2, 4, 14));
                    videoShelterFlag.setLogicChannel16(parseBooleanFromBytes(data, index + 2, 4, 15));
                    videoShelterFlag.setLogicChannel17(parseBooleanFromBytes(data, index + 2, 4, 16));
                    videoShelterFlag.setLogicChannel18(parseBooleanFromBytes(data, index + 2, 4, 17));
                    videoShelterFlag.setLogicChannel19(parseBooleanFromBytes(data, index + 2, 4, 18));
                    videoShelterFlag.setLogicChannel20(parseBooleanFromBytes(data, index + 2, 4, 19));
                    videoShelterFlag.setLogicChannel21(parseBooleanFromBytes(data, index + 2, 4, 20));
                    videoShelterFlag.setLogicChannel22(parseBooleanFromBytes(data, index + 2, 4, 21));
                    videoShelterFlag.setLogicChannel23(parseBooleanFromBytes(data, index + 2, 4, 22));
                    videoShelterFlag.setLogicChannel24(parseBooleanFromBytes(data, index + 2, 4, 23));
                    videoShelterFlag.setLogicChannel25(parseBooleanFromBytes(data, index + 2, 4, 24));
                    videoShelterFlag.setLogicChannel26(parseBooleanFromBytes(data, index + 2, 4, 25));
                    videoShelterFlag.setLogicChannel27(parseBooleanFromBytes(data, index + 2, 4, 26));
                    videoShelterFlag.setLogicChannel28(parseBooleanFromBytes(data, index + 2, 4, 27));
                    videoShelterFlag.setLogicChannel29(parseBooleanFromBytes(data, index + 2, 4, 28));
                    videoShelterFlag.setLogicChannel30(parseBooleanFromBytes(data, index + 2, 4, 29));
                    videoShelterFlag.setLogicChannel31(parseBooleanFromBytes(data, index + 2, 4, 30));
                    videoShelterFlag.setLogicChannel32(parseBooleanFromBytes(data, index + 2, 4, 31));
                    terminalExtraLocationMsg.setVideoShelterFlag(videoShelterFlag.build());
                    break;
                case 0x17:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.StorageFaultFlag.Builder storageFaultFlag =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.StorageFaultFlag.newBuilder();
                    storageFaultFlag.setStorage1(parseBooleanFromBytes(data, index + 2, 4, 0));
                    storageFaultFlag.setStorage2(parseBooleanFromBytes(data, index + 2, 4, 1));
                    storageFaultFlag.setStorage3(parseBooleanFromBytes(data, index + 2, 4, 2));
                    storageFaultFlag.setStorage4(parseBooleanFromBytes(data, index + 2, 4, 3));
                    storageFaultFlag.setStorage5(parseBooleanFromBytes(data, index + 2, 4, 4));
                    storageFaultFlag.setStorage6(parseBooleanFromBytes(data, index + 2, 4, 5));
                    storageFaultFlag.setStorage7(parseBooleanFromBytes(data, index + 2, 4, 6));
                    storageFaultFlag.setStorage8(parseBooleanFromBytes(data, index + 2, 4, 7));
                    storageFaultFlag.setStorage9(parseBooleanFromBytes(data, index + 2, 4, 8));
                    storageFaultFlag.setStorage10(parseBooleanFromBytes(data, index + 2, 4, 9));
                    storageFaultFlag.setStorage11(parseBooleanFromBytes(data, index + 2, 4, 10));
                    storageFaultFlag.setStorage12(parseBooleanFromBytes(data, index + 2, 4, 11));
                    storageFaultFlag.setBackupStorage1(parseBooleanFromBytes(data, index + 2, 4, 12));
                    storageFaultFlag.setBackupStorage2(parseBooleanFromBytes(data, index + 2, 4, 13));
                    storageFaultFlag.setBackupStorage3(parseBooleanFromBytes(data, index + 2, 4, 14));
                    storageFaultFlag.setBackupStorage4(parseBooleanFromBytes(data, index + 2, 4, 15));
                    terminalExtraLocationMsg.setStorageFaultFlag(storageFaultFlag.build());
                    break;
                case 0x18:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.ExceptBehavior.Builder exceptBehavior =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.ExceptBehavior.newBuilder();
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.ExceptBehavior.ExceptBehaviorType.Builder exceptBehaviorType =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.ExceptBehavior.ExceptBehaviorType.newBuilder();
                    exceptBehaviorType.setTired(parseBooleanFromBytes(data, index + 2, 2, 0));
                    exceptBehaviorType.setPhone(parseBooleanFromBytes(data, index + 2, 2, 1));
                    exceptBehaviorType.setSmoking(parseBooleanFromBytes(data, index + 2, 2, 2));
                    exceptBehavior.setExceptBehaviorType(exceptBehaviorType.build());
                    exceptBehavior.setTiredLevel(parseIntFromBytes(data, index + 4, 1));
                    terminalExtraLocationMsg.setExceptBehavior(exceptBehavior.build());
                    break;
                case 0x25:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.ExtraCarState.Builder extraCarState =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.ExtraCarState.newBuilder();
                    extraCarState.setLowLightSignal(parseBooleanFromBytes(data, index + 2 , 4, 0));
                    extraCarState.setFarLightSignal(parseBooleanFromBytes(data, index + 2 , 4, 1));
                    extraCarState.setRightLightSignal(parseBooleanFromBytes(data, index + 2 , 4, 2));
                    extraCarState.setLeftLightSignal(parseBooleanFromBytes(data, index + 2 , 4, 3));
                    extraCarState.setBrakingSignal(parseBooleanFromBytes(data, index + 2 , 4, 4));
                    extraCarState.setReverseSignal(parseBooleanFromBytes(data, index + 2 , 4, 5));
                    extraCarState.setFogLampSignal(parseBooleanFromBytes(data, index + 2 , 4, 6));
                    extraCarState.setOutlineLamp(parseBooleanFromBytes(data, index + 2 , 4, 7));
                    extraCarState.setHornSignal(parseBooleanFromBytes(data, index + 2 , 4, 8));
                    extraCarState.setAirCondition(parseBooleanFromBytes(data, index + 2 , 4, 9));
                    extraCarState.setNeutralSignal(parseBooleanFromBytes(data, index + 2 , 4, 10));
                    extraCarState.setRetarderOperation(parseBooleanFromBytes(data, index + 2 , 4, 11));
                    extraCarState.setABSOperation(parseBooleanFromBytes(data, index + 2 , 4, 12));
                    extraCarState.setHeaterOperation(parseBooleanFromBytes(data, index + 2 , 4,  13));
                    extraCarState.setClutchState(parseBooleanFromBytes(data, index + 2 , 4,  14));
                    terminalExtraLocationMsg.setExtraCarState(extraCarState.build());
                    break;
                case 0x2A:
                    ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.IOState.Builder ioState =
                            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.IOState.newBuilder();
                    ioState.setDeepDormancy(parseBooleanFromBytes(data, index + 2, 2, 0));
                    ioState.setDormancy(parseBooleanFromBytes(data, index + 2, 2, 1));
                    terminalExtraLocationMsg.setIoState(ioState.build());
                    break;
                case 0x2B:
                    terminalExtraLocationMsg.setSimulation(parseIntFromBytes(data, index + 2, length));
                    break;
                case 0x30:
                    terminalExtraLocationMsg.setWirelessIntensity(parseIntFromBytes(data, index + 2, length));
                    break;
                case 0x31:
                    terminalExtraLocationMsg.setSatellitesNum(parseIntFromBytes(data, index + 2, length));
                    break;
                default:
                    log.info("【附加位置信息保留信息】 " + Integer.toHexString(extraDataId));
            }
            index += length + 2;
        }
        ret.setTerminalExtraLocationMsg(terminalExtraLocationMsg.build());
        ret.setTerminalPhone(terminalPhone);
        ret.setFlowId(flowId);
        return ret.build();
    }

    private ServerData.TerminalLocationMsg toTerminalLocationMsg(PackageData packageData) throws Exception {
        final byte[] data = packageData.getMsgBody();
        return getTerminalLocationMsg(data, packageData.getMsgHeader().getTerminalPhone(), packageData.getMsgHeader().getFlowId());
    }

    /**
     * 解析查询参数应答消息
     */
    private ServerData.TerminalParamQueryResponseMsg toTerminalParamQueryResponseMsg(PackageData packageData) {
        ServerData.TerminalParamQueryResponseMsg.Builder ret = ServerData.TerminalParamQueryResponseMsg.newBuilder();
        final byte[] data = packageData.getMsgBody();
        ret.setFlowId(parseIntFromBytes(data, 0, 2));
        int num = parseIntFromBytes(data, 2, 1);
        ret.setReplyParamNumber(num);
        ServerData.TerminalParamQueryResponseMsg.TerminalParam.Builder terminalParam =
                ServerData.TerminalParamQueryResponseMsg.TerminalParam.newBuilder();
        int index = 3;
        for (int i = 0; i < num; i++) {
            int paramId = parseIntFromBytes(data, index, 4);
            index += 4;
            int paramLength = parseIntFromBytes(data, index, 1);
            index++;
            switch (paramId) {
                case 0x0001:
                    terminalParam.setBeatInterval(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0002:
                    terminalParam.setTCPTimeout(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0003:
                    terminalParam.setTCPretransTimes(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0004:
                    terminalParam.setUDPTimeout(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0005:
                    terminalParam.setUDPretransTimes(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0006:
                    terminalParam.setSMSTimeout(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0007:
                    terminalParam.setSMSretransTimes(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0010:
                    terminalParam.setMainServerAPN(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0011:
                    terminalParam.setMainServerUsername(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0012:
                    terminalParam.setMainServerPassword(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0013:
                    terminalParam.setMainServerHost(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0014:
                    terminalParam.setBackupServerAPN(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0015:
                    terminalParam.setBackupServerUsername(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0016:
                    terminalParam.setBackupServerPassword(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0017:
                    terminalParam.setBackupServerHost(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0018:
                    terminalParam.setServerTCPport(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0019:
                    terminalParam.setServerUDPport(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x001A:
                    terminalParam.setICverifyMainServerHost(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x001B:
                    terminalParam.setICverifyServerTCPport(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x001C:
                    terminalParam.setICverifyServerUDPport(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x001D:
                    terminalParam.setICverifyBackupServerHost(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0020:
                    terminalParam.setLocatonInfoStrategy(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0021:
                    terminalParam.setLocatonInfoPlan(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0022:
                    terminalParam.setUnloginTimeInterval(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0027:
                    terminalParam.setSleepTimeInterval(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0028:
                    terminalParam.setWarningTimeInterval(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0029:
                    terminalParam.setDefaultInterval(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x002C:
                    terminalParam.setDefaultDistanceInterval(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x002D:
                    terminalParam.setUnloginDistanceInterval(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x002E:
                    terminalParam.setSleepDistanceInterval(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x002F:
                    terminalParam.setWarningDistanceInterval(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0030:
                    terminalParam.setRetransmissionAngle(parseIntFromBytes(data, index, paramLength));
                    break;
                /*case 0x0031:
                    terminalParam.set(parseIntFromBytes(data, index, paramLength));*/
                case 0x0040:
                    terminalParam.setPlatformPhoneNum(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0041:
                    terminalParam.setResetPhoneNum(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0042:
                    terminalParam.setRestorePhoneNum(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0043:
                    terminalParam.setPlatformSMSphoneNum(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0044:
                    terminalParam.setAlarmSMSphoneNum(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0045:
                    terminalParam.setPhoneStrategy(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0046:
                    terminalParam.setLongestPhoneTime(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0047:
                    terminalParam.setMonthLongestPhoneTime(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0048:
                    terminalParam.setMonitorPhone(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0049:
                    terminalParam.setPlatformPrivilegeSMS(parseStringFromBytes(data, index, paramLength));
                    break;
                case 0x0050:
                    terminalParam.setAlarmShieldingWord(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0051:
                    terminalParam.setAlarmSMS(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0052:
                    terminalParam.setAlarmPhoto(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0053:
                    terminalParam.setAlarmPhotoSave(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0054:
                    terminalParam.setKeyFlag(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0055:
                    terminalParam.setHighestSpeed(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0056:
                    terminalParam.setSpeedingTime(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0057:
                    terminalParam.setDriverTimeLimit(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0058:
                    terminalParam.setTodayDriverTime(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x0059:
                    terminalParam.setLeastRestTime(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x005A:
                    terminalParam.setLongestPortTime(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x005B:
                    terminalParam.setSpeedingWarningDifferenceValue(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x005C:
                    terminalParam.setTiredDriveWarningDifferenceValue(parseIntFromBytes(data, index, paramLength));
                    break;
                case 0x005E:
                    terminalParam.setRolloverParam(parseIntFromBytes(data, index, paramLength));
                    break;
               /* case 0x0064:
                    terminalParam.setPlatformSMSphoneNum(parseStringFromBytes(data, index, paramLength));*/


                case 0x0075:
                    System.out.println("音视频参数设置");
                    break;
                case 0x0076:
                    System.out.println("音视频通道列表设置");
                    break;
                case 0x0077:
                    System.out.println("单独视频通道参数设置");
                    break;
                case 0x0079:
                    System.out.println(".....");
                    break;

                default:
                    System.out.println(Integer.toHexString(paramId));
            }
            index += paramLength;
        }
        ret.setTerminalParam(terminalParam);
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }

    /**
     * 解析定位数据批量上传
     */
    private ServerData.TerminalBulkLocationMsg toTerminalBulkLocationMsg(PackageData packageData) throws Exception {
        ServerData.TerminalBulkLocationMsg.Builder ret = ServerData.TerminalBulkLocationMsg.newBuilder();

        final byte[] data = packageData.getMsgBody();
        int num = parseIntFromBytes(data, 0, 2);
        ret.setDataNum(num);
        ret.setDataType(parseIntFromBytes(data, 2, 1));
        ArrayList<ServerData.TerminalLocationMsg> terminalLocationMsgs = new ArrayList<>();
        int outIndex = 3;
        for (int i = 0; i < num; i++) {
            ServerData.TerminalLocationMsg.Builder terminalLocationMsg =  ServerData.TerminalLocationMsg.newBuilder();
            int len = parseIntFromBytes(data, outIndex, 2);
            // 1. byte[0-3] 报警标志(DWORD(32))
            ServerData.TerminalLocationMsg.WarningFlag.Builder warningFlag = ServerData.TerminalLocationMsg.WarningFlag.newBuilder();
            warningFlag.setWarningFlag(parseBooleanFromBytes(data, outIndex + 2, 4, 0));
            warningFlag.setOverSpeeding(parseBooleanFromBytes(data, outIndex + 2, 4, 1));
            warningFlag.setOverTired(parseBooleanFromBytes(data, outIndex + 2, 4, 2));
            warningFlag.setDangerous(parseBooleanFromBytes(data, outIndex + 2, 4, 3));
            warningFlag.setGNSSFault(parseBooleanFromBytes(data, outIndex + 2, 4, 4));
            warningFlag.setGNSSAntennaFault(parseBooleanFromBytes(data, outIndex + 2, 4, 5));
            warningFlag.setGNSSAntennaShortCircuit(parseBooleanFromBytes(data, outIndex + 2, 4, 6));
            warningFlag.setTerminalMainPowerUnderVoltage(parseBooleanFromBytes(data, outIndex + 2, 4, 7));
            warningFlag.setTerminalMainPowerFailure(parseBooleanFromBytes(data, outIndex + 2, 4, 8));
            warningFlag.setTerminalLCDFault(parseBooleanFromBytes(data, outIndex + 2, 4, 9));
            warningFlag.setTTSFault(parseBooleanFromBytes(data, outIndex + 2, 4, 10));
            warningFlag.setCameraFault(parseBooleanFromBytes(data, outIndex + 2, 4, 11));
            warningFlag.setICCardFault(parseBooleanFromBytes(data, outIndex + 2, 4, 12));
            warningFlag.setSpeeding(parseBooleanFromBytes(data, outIndex + 2, 4, 13));
            warningFlag.setTired(parseBooleanFromBytes(data, outIndex + 2, 4, 14));
            warningFlag.setDriveTimeout(parseBooleanFromBytes(data, outIndex + 2, 4, 18));
            warningFlag.setParkingOvertime(parseBooleanFromBytes(data, outIndex + 2, 4, 19));
            warningFlag.setThroughArea(parseBooleanFromBytes(data, outIndex + 2, 4, 20));
            warningFlag.setThroughRoad(parseBooleanFromBytes(data, outIndex + 2, 4, 21));
            warningFlag.setRoadTimeout(parseBooleanFromBytes(data, outIndex + 2, 4, 22));
            warningFlag.setRoadFault(parseBooleanFromBytes(data, outIndex + 2, 4, 23));
            warningFlag.setVSSFault(parseBooleanFromBytes(data, outIndex + 2, 4, 24));
            warningFlag.setVehicleOilException(parseBooleanFromBytes(data, outIndex + 2, 4, 25));
            warningFlag.setVehicleTheft(parseBooleanFromBytes(data, outIndex + 2, 4, 26));
            warningFlag.setVehicleIllegalIgnition(parseBooleanFromBytes(data, outIndex + 2, 4, 27));
            warningFlag.setVehicleIllegalShift(parseBooleanFromBytes(data, outIndex + 2, 4, 28));
            warningFlag.setCollisionWarning(parseBooleanFromBytes(data, outIndex + 2, 4, 29));
            warningFlag.setRolloverWarning(parseBooleanFromBytes(data, outIndex + 2, 4, 30));
            warningFlag.setIllegalOpenDoor(parseBooleanFromBytes(data, outIndex + 2, 4, 31));
            terminalLocationMsg.setWarningFlag(warningFlag.build());
            // 2. byte[4-7] 状态(DWORD(32))
            ServerData.TerminalLocationMsg.Status.Builder status = ServerData.TerminalLocationMsg.Status.newBuilder();
            status.setACC(parseBooleanFromBytes(data, outIndex + 6, 4, 0));
            status.setIsLocation(parseBooleanFromBytes(data, outIndex + 6, 4, 1));
            status.setLatitude(parseBooleanFromBytes(data, outIndex + 6, 4, 2));
            status.setLongitude(parseBooleanFromBytes(data, outIndex + 6, 4, 3));
            status.setIsRunning(parseBooleanFromBytes(data, outIndex + 6, 4, 4));
            status.setEncrypt(parseBooleanFromBytes(data, outIndex + 6, 4, 5));
            status.setGoodsStatus(parseIntFrom2Bit(data, outIndex + 6, 4, 6, 8));
            status.setVehicleOil(parseBooleanFromBytes(data, outIndex + 6, 4, 10));
            status.setVehicleCircut(parseBooleanFromBytes(data, outIndex + 6, 4, 11));
            status.setDoorLock(parseBooleanFromBytes(data, outIndex + 6, 4, 12));
            status.setFrontDoorOpen(parseBooleanFromBytes(data, outIndex + 6, 4, 13));
            status.setMiddleDoorOpen(parseBooleanFromBytes(data, outIndex + 6, 4, 14));
            status.setEndDoorOpen(parseBooleanFromBytes(data, outIndex + 6, 4, 15));
            status.setDriverDoorOpen(parseBooleanFromBytes(data, outIndex + 6, 4, 16));
            status.setOtherDoorOpen(parseBooleanFromBytes(data, outIndex + 6, 4, 17));
            status.setGPS(parseBooleanFromBytes(data, outIndex + 6, 4, 18));
            status.setBeidou(parseBooleanFromBytes(data, outIndex + 6, 4, 19));
            status.setGLONASS(parseBooleanFromBytes(data, outIndex + 6, 4, 20));
            status.setGalileo(parseBooleanFromBytes(data, outIndex + 6, 4, 21));
            terminalLocationMsg.setStatus(status.build());
            // 3. byte[8-11] 纬度(DWORD(32)) 以度为单位的纬度值乘以10^6，精确到百万分之一度
            terminalLocationMsg.setLatitude((double)parseIntFromBytes(data, outIndex + 10, 4)/1000000);
            // 4. byte[12-15] 经度(DWORD(32)) 以度为单位的经度值乘以10^6，精确到百万分之一度
            terminalLocationMsg.setLongitude((double)parseIntFromBytes(data, outIndex + 14, 4)/1000000);
            // 5. byte[16-17] 高程(WORD(16)) 海拔高度，单位为米（ m）
            terminalLocationMsg.setElevation(parseIntFromBytes(data, outIndex + 18, 2));
            // byte[18-19] 速度(WORD) 1/10km/h
            terminalLocationMsg.setSpeed((double)parseIntFromBytes(data, outIndex + 20, 2));
            // byte[20-21] 方向(WORD) 0-359，正北为 0，顺时针
            terminalLocationMsg.setDirection(parseIntFromBytes(data, outIndex + 22, 2));
            // byte[22-x] 时间(BCD[6]) YY-MM-DD-hh-mm-ss
            // GMT+8 时间，本标准中之后涉及的时间均采用此时区
            String sd = parseBCDStringFromBytes(data, outIndex + 24, 6);
            String time = String.format("20%s-%s-%s %s:%s:%s",sd.substring(0, 2), sd.substring(2,4), sd.substring(4, 6),
                    sd.substring(6, 8), sd.substring(8, 10), sd.substring(10, 12));
            terminalLocationMsg.setTime(time);
            int index = outIndex + 30;
            ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.Builder terminalExtraLocationMsg = ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.newBuilder();
            while (len > index) {
                int extraDataId = parseIntFromBytes(data, index, 1);
                int length = parseIntFromBytes(data, index + 1, 1);
                index += 2;
                switch (extraDataId) {
                    case 0x01:
                        terminalExtraLocationMsg.setMileage(parseIntFromBytes(data, index, length));
                        break;
                    case 0x02:
                        terminalExtraLocationMsg.setOilQuantity(parseIntFromBytes(data, index, length));
                        break;
                    case 0x03:
                        terminalExtraLocationMsg.setCarSpeed(parseIntFromBytes(data, index, length));
                        break;
                    case 0x04:
                        terminalExtraLocationMsg.setWarningId(parseIntFromBytes(data, index, length));
                        break;
                    case 0x11:
                        ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.SpeedingExtraData.Builder speedingExtraData =
                                ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.SpeedingExtraData.newBuilder();
                        int locationType = parseIntFromBytes(data, index, 1);
                        speedingExtraData.setLocationType(locationType);
                        if (locationType != 0) {
                            speedingExtraData.setLocationId(parseStringFromBytes(data, index + 1,  4));
                        }
                        terminalExtraLocationMsg.setSpeedingExtraData(speedingExtraData.build());
                        break;
                    case 0x12:
                        ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.LocationExtraData.Builder locationExtraData =
                                ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.LocationExtraData.newBuilder();
                        locationExtraData.setLocationType(parseIntFromBytes(data, index, 1));
                        locationExtraData.setLocationId(parseStringFromBytes(data, index + 1, 4));
                        locationExtraData.setDirection(parseIntFromBytes(data, index + 5, 1));
                        terminalExtraLocationMsg.setLocationExtraData(locationExtraData.build());
                        break;
                    case 0x13:
                        ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.LocationInfoExtraData.Builder locationInfoExtraData =
                                ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.LocationInfoExtraData.newBuilder();
                        locationInfoExtraData.setRoadId(parseStringFromBytes(data, index, 4));
                        locationInfoExtraData.setRunTime(parseIntFromBytes(data, index + 4, 2));
                        locationInfoExtraData.setResult(parseIntFromBytes(data, index + 6, 1));
                        terminalExtraLocationMsg.setLocationInfoExtraData(locationInfoExtraData.build());
                        break;
                    case 0x25:
                        ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.ExtraCarState.Builder extraCarState =
                                ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.ExtraCarState.newBuilder();
                        extraCarState.setLowLightSignal(parseBooleanFromBytes(data, index, 4, 0));
                        extraCarState.setFarLightSignal(parseBooleanFromBytes(data, index, 4, 1));
                        extraCarState.setRightLightSignal(parseBooleanFromBytes(data, index, 4, 2));
                        extraCarState.setLeftLightSignal(parseBooleanFromBytes(data, index, 4, 3));
                        extraCarState.setBrakingSignal(parseBooleanFromBytes(data, index, 4, 4));
                        extraCarState.setReverseSignal(parseBooleanFromBytes(data, index, 4, 5));
                        extraCarState.setFogLampSignal(parseBooleanFromBytes(data, index, 4, 6));
                        extraCarState.setOutlineLamp(parseBooleanFromBytes(data, index, 4, 7));
                        extraCarState.setHornSignal(parseBooleanFromBytes(data, index, 4, 8));
                        extraCarState.setAirCondition(parseBooleanFromBytes(data, index, 4, 9));
                        extraCarState.setNeutralSignal(parseBooleanFromBytes(data, index, 4, 10));
                        extraCarState.setRetarderOperation(parseBooleanFromBytes(data, index, 4, 11));
                        extraCarState.setABSOperation(parseBooleanFromBytes(data, index, 4, 12));
                        extraCarState.setHeaterOperation(parseBooleanFromBytes(data, index, 4, 13));
                        extraCarState.setClutchState(parseBooleanFromBytes(data, index, 4, 14));
                        terminalExtraLocationMsg.setExtraCarState(extraCarState.build());
                        break;
                    case 0x2A:
                        ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.IOState.Builder ioState =
                                ServerData.TerminalLocationMsg.TerminalExtraLocationMsg.IOState.newBuilder();
                        ioState.setDeepDormancy(parseBooleanFromBytes(data, index, 2, 0));
                        ioState.setDormancy(parseBooleanFromBytes(data, index, 2, 1));
                        terminalExtraLocationMsg.setIoState(ioState.build());
                        break;
                    case 0x2B:
                        terminalExtraLocationMsg.setSimulation(parseIntFromBytes(data, index, length));
                        break;
                    case 0x30:
                        terminalExtraLocationMsg.setWirelessIntensity(parseIntFromBytes(data, index, length));
                        break;
                    case 0x31:
                        terminalExtraLocationMsg.setSatellitesNum(parseIntFromBytes(data, index, length));
                        break;
                    default:
                        log.info("【附加位置信息保留信息】 {}", extraDataId);
                }
                index += length;
            }
            terminalLocationMsg.setTerminalExtraLocationMsg(terminalExtraLocationMsg.build());
            outIndex += len + 2;
            ret.addTerminalLocationMsg(terminalLocationMsg);
        }
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }


    /**
    *  信息点播/取消
    */
    private TerminalMessageRequestMsg toTerminalMessageRequestMsg(PackageData packageData) {

        final byte[] data = packageData.getMsgBody();
        TerminalMessageRequestMsg ret = new TerminalMessageRequestMsg(packageData);
        ret.setMessageType(parseIntFromBytes(data, 0, 1));
        ret.setMessageFlag(parseIntFromBytes(data, 1, 1));
        return ret;
    }


    private TerminalCarControlResponseMsg toTerminalCarControlResponseMsg(PackageData packageData) throws Exception {
        TerminalCarControlResponseMsg ret = new TerminalCarControlResponseMsg(packageData);
        final byte[] data = packageData.getMsgBody();
        int replyFlowId = parseIntFromBytes(data, 0, 2);
        ret.setReplyFlowId(replyFlowId);
        byte[] newBody = new byte[data.length - 2];
        System.arraycopy(data, 2, newBody, 0,data.length - 2);
        packageData.setMsgBody(newBody);
        ServerData.TerminalLocationMsg terminalLocationMsgResp = toTerminalLocationMsg(packageData);
        ret.setTerminalLocationMsg(terminalLocationMsgResp);
        return ret;
    }

    private TerminalLocationRequestMsg toTerminalLocationRequestMsg(PackageData packageData) throws Exception {
        TerminalLocationRequestMsg ret = new TerminalLocationRequestMsg(packageData);
        final byte[] data = packageData.getMsgBody();
        int replyFlowId = parseIntFromBytes(data, 0, 2);
        ret.setReplyFlowId(replyFlowId);
        byte[] newBody = new byte[data.length - 2];
        System.arraycopy(data, 2, newBody, 0,data.length - 2);
        packageData.setMsgBody(newBody);
        ServerData.TerminalLocationMsg terminalLocationMsgResp = toTerminalLocationMsg(packageData);
        ret.setTerminalLocationMsg(terminalLocationMsgResp);
        return ret;
    }

    private ServerData.TerminalDigitWaybill toTerminalDigitWaybill(PackageData packageData) {
        ServerData.TerminalDigitWaybill.Builder ret = ServerData.TerminalDigitWaybill.newBuilder();
        final byte[] data = packageData.getMsgBody();
        ret.setDigitWaybillData(parseStringFromBytes(data, 4, data.length - 4));
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }

    private ServerData.TerminalDriverIdentityMsg toTerminalDriverIdentityMsg(PackageData packageData) {
        ServerData.TerminalDriverIdentityMsg.Builder ret = ServerData.TerminalDriverIdentityMsg.newBuilder();
        final byte[] data = packageData.getMsgBody();
        ret.setState(parseIntFromBytes(data, 0, 1));
        ret.setTime(parseBCDStringFromBytes(data, 1, 6));
        ret.setICCardInfo(parseIntFromBytes(data, 7, 1));
        int nameLength = parseIntFromBytes(data, 8, 1);
        ret.setDriverName(parseStringFromBytes(data, 9, nameLength));
        // ret.setQualificationCode(parseStringFromBytes(data, 9 + nameLength, 20)); TODO
        int authorityNameLength = parseIntFromBytes(data, 29 + nameLength, 1);
        ret.setAuthorityName(parseStringFromBytes(data, 30 + nameLength, authorityNameLength));
        ret.setCardValidityTerm(parseBCDStringFromBytes(data, 30 + nameLength + authorityNameLength, 4));
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }

    private TerminalMediaEventMsg toTerminalMediaEventMsg(PackageData packageData) {
        TerminalMediaEventMsg ret = new TerminalMediaEventMsg(packageData);
        final byte[] data = packageData.getMsgBody();
        ret.setMediaDataId(parseIntFromBytes(data, 0, 4));
        ret.setMediaType(parseIntFromBytes(data, 4, 1));
        ret.setMediaCode(parseIntFromBytes(data, 5, 1));
        ret.setEventCode(parseIntFromBytes(data, 6, 1));
        ret.setChannelIdId(parseIntFromBytes(data, 7, 1));
        return ret;
    }

    private ServerData.TerminalMediaDataMsg toTerminalMediaDataMsg(PackageData packageData) throws Exception {
        ServerData.TerminalMediaDataMsg.Builder ret = ServerData.TerminalMediaDataMsg.newBuilder();
        final byte[] data = packageData.getMsgBody();
        ret.setMediaId(parseIntFromBytes(data, 0, 4));
        ret.setMediaType(parseIntFromBytes(data, 4, 1));
        ret.setMediaCode(parseIntFromBytes(data, 5, 1));
        ret.setEventCode(parseIntFromBytes(data, 6, 1));
        ret.setChannelId(parseIntFromBytes(data, 7, 1));
        byte[] tmp = new byte[28];
        System.arraycopy(data, 8, tmp, 0, 28);
        packageData.setMsgBody(tmp);
        ServerData.TerminalLocationMsg terminalLocationMsg = toTerminalLocationMsg(packageData);
        ret.setTerminalLocationMsg(terminalLocationMsg);
        byte[] mediaData = new byte[data.length - 36];
        System.arraycopy(data, 36, mediaData, 0, data.length - 36);
        ret.setMediaData(ByteString.copyFrom(mediaData));
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }

    private ServerData.TerminalCameraPhotoResponseMsg toTerminalCameraPhotoResponseMsg(PackageData packageData) {
        ServerData.TerminalCameraPhotoResponseMsg.Builder ret = ServerData.TerminalCameraPhotoResponseMsg.newBuilder();
        final byte[] data = packageData.getMsgBody();
        ret.setReplyFlowId(parseIntFromBytes(data, 0, 2));
        ret.setResult(parseIntFromBytes(data, 2, 1));
        int mediaIdNum = parseIntFromBytes(data, 3, 2);
        ret.setMediaIdNum(mediaIdNum);
        int index = 5;
        for (int i = 0; i < mediaIdNum; i++) {
            ret.addMediaIdList(parseIntFromBytes(data, index, 4));
            index += 4;
        }
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }

    private ServerData.TerminalSavedMediaRetrievalResponseMsg toTerminalSavedMediaRetrievalResponseMsg(PackageData packageData) throws Exception {
        ServerData.TerminalSavedMediaRetrievalResponseMsg.Builder ret = ServerData.TerminalSavedMediaRetrievalResponseMsg.newBuilder();
        final byte[] data = packageData.getMsgBody();
        ret.setReplyFlowId(parseIntFromBytes(data, 0, 2));
        int mediaDataNum = parseIntFromBytes(data, 2, 2);
        ret.setMediaRetrievalNum(mediaDataNum);
        int index = 4;
        for (int i = 0; i < mediaDataNum; i++) {
            ServerData.TerminalSavedMediaRetrievalResponseMsg.TerminalRetrievalDataMsg.Builder terminalRetrievalDataMsg
                    = ServerData.TerminalSavedMediaRetrievalResponseMsg.TerminalRetrievalDataMsg.newBuilder();
            terminalRetrievalDataMsg.setMediaId(parseIntFromBytes(data, index, 4));
            terminalRetrievalDataMsg.setMediaType(parseIntFromBytes(data, index + 4, 1));
            terminalRetrievalDataMsg.setPassagewayId(parseIntFromBytes(data, index + 5, 1));
            terminalRetrievalDataMsg.setEventCode(parseIntFromBytes(data, index + 6, 1));
            byte[] tmp = new byte[28];
            System.arraycopy(data, index + 7, tmp, 0, 28);
            packageData.setMsgBody(tmp);
            ServerData.TerminalLocationMsg terminalLocationMsg = toTerminalLocationMsg(packageData);
            terminalRetrievalDataMsg.setTerminalLocationMsg(terminalLocationMsg);
            index += 35;
            ret.addTerminalRetrievalDataMsg(terminalRetrievalDataMsg);
        }
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }

    private ServerData.TerminalAttributeQueryResponseMsg toTerminalAttributeQueryResponseMsg(PackageData packageData) {
        ServerData.TerminalAttributeQueryResponseMsg.Builder ret = ServerData.TerminalAttributeQueryResponseMsg.newBuilder();
        final byte[] data = packageData.getMsgBody();
        ServerData.TerminalAttributeQueryResponseMsg.TerminalType.Builder terminalType =
                ServerData.TerminalAttributeQueryResponseMsg.TerminalType.newBuilder();
        terminalType.setPassengerVehicles(parseBooleanFromBytes(data, 0, 2, 0));
        terminalType.setDangerousGoodsVehicles(parseBooleanFromBytes(data, 0, 2, 1));
        terminalType.setCommonFreightVehicle(parseBooleanFromBytes(data, 0, 2, 2));
        terminalType.setTaxi(parseBooleanFromBytes(data, 0, 2, 3));
        terminalType.setSupportingHardDiskVideo(parseBooleanFromBytes(data, 0, 2, 6));
        terminalType.setIntegratedMachine(parseBooleanFromBytes(data, 0, 2, 7));
        ret.setTerminalType(terminalType);
        ret.setManufacturerId(parseStringFromBytes(data, 2, 5));
        ret.setTerminalModel(parseStringFromBytes(data, 7, 20));
        ret.setTerminalId(parseStringFromBytes(data, 27, 7));
        ret.setICCID(parseBCDStringFromBytes(data, 34, 10));
        int terminalHardwareVersionLength = parseIntFromBytes(data, 44, 1);
        ret.setTerminalHardwareVersion(parseStringFromBytes(data, 45, terminalHardwareVersionLength));
        int terminalFirewareVersionLength = parseIntFromBytes(data, 45 + terminalHardwareVersionLength, 1);
        ret.setTerminalFirmwareVersion(parseStringFromBytes(data, 46 + terminalHardwareVersionLength, terminalFirewareVersionLength));
        int index = 46 + terminalHardwareVersionLength + terminalFirewareVersionLength;
        ServerData.TerminalAttributeQueryResponseMsg.GNSSAttribute.Builder gnssAttribute = ServerData.TerminalAttributeQueryResponseMsg.GNSSAttribute.newBuilder();
        gnssAttribute.setGPS(parseBooleanFromBytes(data, index, 1, 0));
        gnssAttribute.setBeidou(parseBooleanFromBytes(data, index, 1, 1));
        gnssAttribute.setGLONASS(parseBooleanFromBytes(data, index, 1, 2));
        gnssAttribute.setGalileo(parseBooleanFromBytes(data, index, 1, 3));
        ret.setGnssAttribute(gnssAttribute.build());
        ServerData.TerminalAttributeQueryResponseMsg.CommunicationModuleAttribute.Builder communicationModuleAttribute
                = ServerData.TerminalAttributeQueryResponseMsg.CommunicationModuleAttribute.newBuilder();
        communicationModuleAttribute.setGPSCommunication(parseBooleanFromBytes(data, index + 1, 1, 0));
        communicationModuleAttribute.setCDMA(parseBooleanFromBytes(data, index + 1, 1, 1));
        communicationModuleAttribute.setTDSCMA(parseBooleanFromBytes(data, index + 1, 1, 2));
        communicationModuleAttribute.setWCDMA(parseBooleanFromBytes(data, index + 1, 1, 3));
        communicationModuleAttribute.setCDMA2000(parseBooleanFromBytes(data, index + 1, 1, 4));
        communicationModuleAttribute.setTDLTE(parseBooleanFromBytes(data, index + 1, 1, 5));
        communicationModuleAttribute.setOtherCommunication(parseBooleanFromBytes(data, index + 1, 1, 7));
        ret.setCommunicationModuleAttribute(communicationModuleAttribute);
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }

    private ServerData.TerminalUploadMediaAttributeMsg toTerminalUploadMediaAttributeMsg(PackageData packageData) {
        ServerData.TerminalUploadMediaAttributeMsg.Builder ret = ServerData.TerminalUploadMediaAttributeMsg.newBuilder();
        final byte[] data = packageData.getMsgBody();
        ret.setVoiceCode(parseIntFromBytes(data, 0, 1));
        ret.setVoiceChannelNum(parseIntFromBytes(data, 1, 1));
        ret.setVoiceSamplingRate(parseIntFromBytes(data, 2, 1));
        ret.setVoiceSamplingBit(parseIntFromBytes(data, 3, 1));
        ret.setVoiceFrameLength(parseIntFromBytes(data, 4, 2));
        ret.setVoiceOutput(parseIntFromBytes(data, 6, 1));
        ret.setVideoCode(parseIntFromBytes(data, 7, 1));
        ret.setMaxVoiceChannelNum(parseIntFromBytes(data, 8, 1));
        ret.setMaxVideoChannelNum(parseIntFromBytes(data, 9, 1));
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }


    private ServerData.TerminalUploadMediaResourcesMsg toTerminalUploadMediaResourcesMsg(PackageData packageData) {
        ServerData.TerminalUploadMediaResourcesMsg.Builder ret = ServerData.TerminalUploadMediaResourcesMsg.newBuilder();
        final byte[] data = packageData.getMsgBody();
        ret.setReplyFlowId(parseIntFromBytes(data, 0, 2));
        int num = parseIntFromBytes(data, 2, 4);
        ret.setMediaResourcesNum(num);
        int index = 6;
        for (int i = 0; i < num; i++) {
            ServerData.TerminalUploadMediaResourcesMsg.MediaResources.Builder mediaResources
                    = ServerData.TerminalUploadMediaResourcesMsg.MediaResources.newBuilder();
            mediaResources.setLogicChannelNum(parseIntFromBytes(data, index, 1));
            mediaResources.setStartTime(parseBCDStringFromBytes(data, index + 1, 6));
            mediaResources.setEndTime(parseBCDStringFromBytes(data, index + 7, 6));
            //mediaResources.setWarnningFlag(13, 8);
            mediaResources.setMediaResourcesType(parseIntFromBytes(data, index + 21, 1));
            mediaResources.setBitStreamType(parseIntFromBytes(data, index + 22, 1));
            mediaResources.setStorageTYpe(parseIntFromBytes(data, index + 23, 1));
            mediaResources.setFileSize(parseIntFromBytes(data, index + 24, 4));
            index += 28;
            ret.addMediaResources(mediaResources);
        }
        ret.setTerminalPhone(packageData.getMsgHeader().getTerminalPhone());
        ret.setFlowId(packageData.getMsgHeader().getFlowId());
        return ret.build();
    }

}
