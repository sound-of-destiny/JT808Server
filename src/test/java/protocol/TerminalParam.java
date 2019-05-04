package protocol;

import java.util.ArrayList;

public class TerminalParam {
    // 终端心跳发送间隔,单位为秒(s)
    private int beatInterval;
    // TCP 消息应答超时时间,单位为秒(s)
    private int TCPTimeout;
    // TCP 消息重传次数
    private int TCPretransTimes;
    // UDP 消息应答超时时间,单位为秒(s)
    private int UDPTimeout;
    // UDP 消息重传次数
    private int UDPretransTimes;
    // SMS 消息应答超时时间,单位为秒(s)
    private int SMSTimeout;
    // SMS 消息重传次数
    private int SMSretransTimes;
    // 主服务器 APN
    private String mainServerAPN;
    // 主服务器无线通信拨号用户名
    private String mainServerUsername;
    // 主服务器无线通信拨号密码
    private String mainServerPassword;
    // 主服务器地址,IP 或域名
    private String mainServerHost;
    // 备份服务器 APN
    private String backupServerAPN;
    // 备份服务器无线通信拨号用户名
    private String backupServerUsername;
    // 备份服务器无线通信拨号密码
    private String backupServerPassword;
    // 备份服务器地址,IP 或域名
    private String backupServerHost;
    // 服务器 TCP 端口
    private int serverTCPport;
    // 服务器 UDP 端口
    private int serverUDPport;
    // 道路运输证 IC 卡认证主服务器 IP 地址或域名
    private String ICverifyMainServerHost;
    // 道路运输证 IC 卡认证主服务器 TCP 端口
    private int ICverifyServerTCPport;
    // 道路运输证 IC 卡认证主服务器 UDP 端口
    private int ICverifyServerUDPport;
    // 道路运输证 IC 卡认证备份服务器 IP 地址或域名
    private String ICverifyBackupServerHost;
    // 位置汇报策略,0:定时汇报;1:定距汇报;2:定时和定距汇报
    private int locatonInfoStrategy;
    // 位置汇报方案,0:根据 ACC 状态; 1:根据登录状态和 ACC 状态,先判断登录状态,若登录再根据 ACC 状态
    private int locatonInfoPlan;
    // 驾驶员未登录汇报时间间隔,单位为秒(s),>0
    private int unloginTimeInterval;
    // 休眠时汇报时间间隔,单位为秒(s),>0
    private int sleepTimeInterval;
    // 紧急报警时汇报时间间隔,单位为秒(s),>0
    private int warningTimeInterval;
    // 缺省时间汇报间隔,单位为秒(s),>0
    private int defaultInterval;
    // 缺省距离汇报间隔,单位为米(m),>0
    private int defaultDistanceInterval;
    // 驾驶员未登录汇报距离间隔,单位为米(m),>0
    private int unloginDistanceInterval;
    // 休眠时汇报距离间隔,单位为米(m),>0
    private int sleepDistanceInterval;
    // 紧急报警时汇报距离间隔,单位为米(m),>0
    private int warningDistanceInterval;
    // 拐点补传角度,<180
    private int retransmissionAngle;
    // 电子围栏半径(非法位移阈值),单位为米
    private int electronicFenceRadius;
    // 监控平台电话号码
    private String platformPhoneNum;
    // 复位电话号码,可采用此电话号码拨打终端电话让终端复位
    private String resetPhoneNum;
    // 恢复出厂设置电话号码,可采用此电话号码拨打终端电话让终端恢复出厂设置
    private String restorePhoneNum;
    // 监控平台 SMS 电话号码
    private String platformSMSphoneNum;
    // 接收终端 SMS 文本报警号码
    private String alarmSMSphoneNum;
    // 终端电话接听策略,0:自动接听;1:ACC ON 时自动接听,OFF 时手动接听
    private int phoneStrategy;
    // 每次最长通话时间,单位为秒(s),0 为不允许通话,0xFFFFFFFF 为不限制
    private int longestPhoneTime;
    // 当月最长通话时间,单位为秒(s),0 为不允许通话,0xFFFFFFFF 为不限制
    private int monthLongestPhoneTime;
    // 监听电话号码
    private String monitorPhone;
    // 监管平台特权短信号码
    private String platformPrivilegeSMS;
    // 报警屏蔽字,与位置信息汇报消息中的报警标志相对应,相应位为 1则相应报警被屏蔽
    private int alarmShieldingWord;
    // 报警发送文本 SMS 开关,与位置信息汇报消息中的报警标志相对应,相应位为 1 则相应报警时发送文本 SMS
    private int alarmSMS;
    // 报警拍摄开关,与位置信息汇报消息中的报警标志相对应,相应位为1 则相应报警时摄像头拍摄
    private int alarmPhoto;
    // 报警拍摄存储标志,与位置信息汇报消息中的报警标志相对应,相应位为 1 则对相应报警时拍的照片进行存储,否则实时上传
    private int alarmPhotoSave;
    // 关键标志,与位置信息汇报消息中的报警标志相对应,相应位为 1 则对相应报警为关键报警
    private int keyFlag;
    // 最高速度,单位为公里每小时(km/h)
    private int highestSpeed;
    // 超速持续时间,单位为秒(s)
    private int speedingTime;
    // 连续驾驶时间门限,单位为秒(s)
    private int driverTimeLimit;
    // 当天累计驾驶时间门限,单位为秒(s)
    private int todayDriverTime;
    // 最小休息时间,单位为秒(s)
    private int leastRestTime;
    // 最长停车时间,单位为秒(s)
    private int longestPortTime;
    // 超速报警预警差值,单位为 1/10Km/h
    private int speedingWarningDifferenceValue;
    // 疲劳驾驶预警差值,单位为秒(s),>0
    private int tiredDriveWarningDifferenceValue;
    // 碰撞报警参数设置:b7-b0: 碰撞时间,单位 4ms;b15-b8:碰撞加速度,单位 0.1g,设置范围在:0-79 之间,默认为10 TODO

    // 侧翻报警参数设置:侧翻角度,单位 1 度,默认为 30 度
    private int rolloverParam;
    // 定时拍照控制,见 表 13
    private TimingPhotoControlBit timingPhotoControlBit;
    // 定距拍照控制,见 表 14
    private FixedPictureControlBit fixedPictureControlBit;
    // 图像/视频质量,1-10,1 最好
    private int cameraQuality;
    // 亮度,0-255
    private int light;
    // 对比度,0-127
    private int contrast;
    // 饱和度,0-127
    private int saturation;
    // 色度,0-255
    private int color;
    // 车辆里程表读数,1/10km
    private int mileage;
    // 车辆所在的省域 ID
    private int provinceId;
    // 车辆所在的市域 ID
    private int cityId;
    // 公安交通管理部门颁发的机动车号牌
    private String licensePlate;
    // 车牌颜色,按照 JT/T415-2006 的 5.4.12
    private int licensePlateColor;
    // GNSS 定位模式,定义如下:
    // bit0,0:禁用 GPS 定位, 1:启用 GPS 定位;
    // bit1,0:禁用北斗定位, 1:启用北斗定位;
    // bit2,0:禁用 GLONASS 定位, 1:启用 GLONASS 定位;
    // bit3,0:禁用 Galileo 定位, 1:启用 Galileo 定位。
    private GNSS gnss;
    // GNSS 波特率,定义如下:
    // 0x00:4800;0x01:9600;
    // 0x02:19200;0x03:38400;
    // 0x04:57600;0x05:115200。
    private int GNSSbaudRatio;
    // GNSS 模块详细定位数据输出频率,定义如下:
    // 0x00:500ms;0x01:1000ms(默认值);
    // 0x02:2000ms;0x03:3000ms;
    // 0x04:4000ms。
    private int GNSSdataOutputFrequency;
    // GNSS 模块详细定位数据采集频率,单位为秒,默认为 1
    private int GNSSdataFrequency;
    // GNSS 模块详细定位数据上传方式:
    // 0x00,本地存储,不上传(默认值);
    // 0x01,按时间间隔上传;
    // 0x02,按距离间隔上传;
    // 0x0B,按累计时间上传,达到传输时间后自动停止上传;
    // 0x0C,按累计距离上传,达到距离后自动停止上传;
    // 0x0D,按累计条数上传,达到上传条数后自动停止上传。

    // GNSS 模块详细定位数据上传设置:
    // 上传方式为 0x01 时,单位为秒;
    // 上传方式为 0x02 时,单位为米;
    // 上传方式为 0x0B 时,单位为秒;
    // 上传方式为 0x0C 时,单位为米;
    // 上传方式为 0x0D 时,单位为条。

    // CAN 总线通道 1 采集时间间隔(ms),0 表示不采集

    // CAN 总线通道 1 上传时间间隔(s),0 表示不上传

    // CAN 总线通道 2 采集时间间隔(ms),0 表示不采集

    // CAN 总线通道 2 上传时间间隔(s),0 表示不上传

    // CAN 总线 ID 单独采集设置:
    // bit63-bit32 表示此 ID 采集时间间隔(ms),0 表示不采集;
    // bit31 表示 CAN 通道号,0:CAN1,1:CAN2;
    // bit30 表示帧类型,0:标准帧,1:扩展帧;
    // bit29 表示数据采集方式,0:原始数据,1:采集区间的计算值;
    // bit28-bit0 表示 CAN 总线 ID。

    //

    // 音视频部分
    // 音视频参数设置
    private MediaParam mediaParam;
    // 音视频通道列表设置
    private MediaChannelList mediaChannelList;
    // 单独视频通道参数设置
    // public VideoChannelParam videoChannelParam;
    // 特殊报警录像参数设置
    // public UniqueAlarmVideoParam uniqueAlarmVideoParam;
    // 视频相关报警屏蔽字
    private int videoSheildBits;
    // 图像分析报警参数设置
    // public PictureAlarmingParam pictureAlarmingParam;
    // 终端休眠唤醒模式设置
    // public TerminalWakeUpModel terminalWakeUpModel;


    public MediaParam getMediaParam() {
        return mediaParam;
    }

    public void setMediaParam(MediaParam mediaParam) {
        this.mediaParam = mediaParam;
    }

    public MediaChannelList getMediaChannelList() {
        return mediaChannelList;
    }

    public void setMediaChannelList(MediaChannelList mediaChannelList) {
        this.mediaChannelList = mediaChannelList;
    }

    public int getVideoSheildBits() {
        return videoSheildBits;
    }

    public void setVideoSheildBits(int videoSheildBits) {
        this.videoSheildBits = videoSheildBits;
    }

    public static class MediaParam {
        // 实时流编码模式
        private int realTimeStreamCode;
        // 实时流分辨率
        private int realTimeStreamResovling;
        // 实时流关键帧间隔
        private int realTimeKeyFrameInterval;
        // 实时流目标帧率
        private int realTimeStreamGoalFrameRate;
        // 实时流目标码率
        private int realTimeStreamGoalCodeRate;
        // 存储流编码模式
        private int savedStreamCode;
        // 存储流分辨率
        private int savedStreamResovling;
        // 存储流关键帧间隔
        private int savedKeyFrameInterval;
        // 存储流目标帧率
        private int savedStreamGoalFrameRate;
        // 存储流目标码率
        private int savedStreamGoalCodeRate;
        // OSD字幕叠加设置
        private int OSDSubtitle;
        // 是否启用音频
        private int openVoice;

        public int getRealTimeStreamCode() {
            return realTimeStreamCode;
        }

        public void setRealTimeStreamCode(int realTimeStreamCode) {
            this.realTimeStreamCode = realTimeStreamCode;
        }

        public int getRealTimeStreamResovling() {
            return realTimeStreamResovling;
        }

        public void setRealTimeStreamResovling(int realTimeStreamResovling) {
            this.realTimeStreamResovling = realTimeStreamResovling;
        }

        public int getRealTimeKeyFrameInterval() {
            return realTimeKeyFrameInterval;
        }

        public void setRealTimeKeyFrameInterval(int realTimeKeyFrameInterval) {
            this.realTimeKeyFrameInterval = realTimeKeyFrameInterval;
        }

        public int getRealTimeStreamGoalFrameRate() {
            return realTimeStreamGoalFrameRate;
        }

        public void setRealTimeStreamGoalFrameRate(int realTimeStreamGoalFrameRate) {
            this.realTimeStreamGoalFrameRate = realTimeStreamGoalFrameRate;
        }

        public int getRealTimeStreamGoalCodeRate() {
            return realTimeStreamGoalCodeRate;
        }

        public void setRealTimeStreamGoalCodeRate(int realTimeStreamGoalCodeRate) {
            this.realTimeStreamGoalCodeRate = realTimeStreamGoalCodeRate;
        }

        public int getSavedStreamCode() {
            return savedStreamCode;
        }

        public void setSavedStreamCode(int savedStreamCode) {
            this.savedStreamCode = savedStreamCode;
        }

        public int getSavedStreamResovling() {
            return savedStreamResovling;
        }

        public void setSavedStreamResovling(int savedStreamResovling) {
            this.savedStreamResovling = savedStreamResovling;
        }

        public int getSavedKeyFrameInterval() {
            return savedKeyFrameInterval;
        }

        public void setSavedKeyFrameInterval(int savedKeyFrameInterval) {
            this.savedKeyFrameInterval = savedKeyFrameInterval;
        }

        public int getSavedStreamGoalFrameRate() {
            return savedStreamGoalFrameRate;
        }

        public void setSavedStreamGoalFrameRate(int savedStreamGoalFrameRate) {
            this.savedStreamGoalFrameRate = savedStreamGoalFrameRate;
        }

        public int getSavedStreamGoalCodeRate() {
            return savedStreamGoalCodeRate;
        }

        public void setSavedStreamGoalCodeRate(int savedStreamGoalCodeRate) {
            this.savedStreamGoalCodeRate = savedStreamGoalCodeRate;
        }

        public int getOSDSubtitle() {
            return OSDSubtitle;
        }

        public void setOSDSubtitle(int OSDSubtitle) {
            this.OSDSubtitle = OSDSubtitle;
        }

        public int getOpenVoice() {
            return openVoice;
        }

        public void setOpenVoice(int openVoice) {
            this.openVoice = openVoice;
        }

        @Override
        public String toString() {
            return "MediaParam{" +
                    "realTimeStreamCode=" + realTimeStreamCode +
                    ", realTimeStreamResovling=" + realTimeStreamResovling +
                    ", realTimeKeyFrameInterval=" + realTimeKeyFrameInterval +
                    ", realTimeStreamGoalFrameRate=" + realTimeStreamGoalFrameRate +
                    ", realTimeStreamGoalCodeRate=" + realTimeStreamGoalCodeRate +
                    ", savedStreamCode=" + savedStreamCode +
                    ", savedStreamResovling=" + savedStreamResovling +
                    ", savedKeyFrameInterval=" + savedKeyFrameInterval +
                    ", savedStreamGoalFrameRate=" + savedStreamGoalFrameRate +
                    ", savedStreamGoalCodeRate=" + savedStreamGoalCodeRate +
                    ", OSDSubtitle=" + OSDSubtitle +
                    ", openVoice=" + openVoice +
                    '}';
        }
    }


    public static class MediaChannelList {
        // 音视频通道总数
        private int mediaChannelNum;
        // 音频通道总数
        private int voiceChannelNum;
        // 视频通道总数
        private int vedioChannelNum;
        // 音视频通道对照表
        private ArrayList<MediaChannel> mediaChannelList;

        public static class MediaChannel {
            // 物理通道号
            private int physicalChannelId;
            // 逻辑通道号
            private int logicalChannelId;
            // 通道类型
            private int channelType;
            // 是否连接云台
            private int linkTable;

            public int getPhysicalChannelId() {
                return physicalChannelId;
            }

            public void setPhysicalChannelId(int physicalChannelId) {
                this.physicalChannelId = physicalChannelId;
            }

            public int getLogicalChannelId() {
                return logicalChannelId;
            }

            public void setLogicalChannelId(int logicalChannelId) {
                this.logicalChannelId = logicalChannelId;
            }

            public int getChannelType() {
                return channelType;
            }

            public void setChannelType(int channelType) {
                this.channelType = channelType;
            }

            public int getLinkTable() {
                return linkTable;
            }

            public void setLinkTable(int linkTable) {
                this.linkTable = linkTable;
            }

            @Override
            public String toString() {
                return "MediaChannel{" +
                        "physicalChannelId=" + physicalChannelId +
                        ", logicalChannelId=" + logicalChannelId +
                        ", channelType=" + channelType +
                        ", linkTable=" + linkTable +
                        '}';
            }
        }

        public int getMediaChannelNum() {
            return mediaChannelNum;
        }

        public void setMediaChannelNum(int mediaChannelNum) {
            this.mediaChannelNum = mediaChannelNum;
        }

        public int getVoiceChannelNum() {
            return voiceChannelNum;
        }

        public void setVoiceChannelNum(int voiceChannelNum) {
            this.voiceChannelNum = voiceChannelNum;
        }

        public int getVedioChannelNum() {
            return vedioChannelNum;
        }

        public void setVedioChannelNum(int vedioChannelNum) {
            this.vedioChannelNum = vedioChannelNum;
        }

        public ArrayList<MediaChannel> getMediaChannelList() {
            return mediaChannelList;
        }

        public void setMediaChannelList(ArrayList<MediaChannel> mediaChannelList) {
            this.mediaChannelList = mediaChannelList;
        }

        @Override
        public String toString() {
            return "MediaChannelList{" +
                    "mediaChannelNum=" + mediaChannelNum +
                    ", voiceChannelNum=" + voiceChannelNum +
                    ", vedioChannelNum=" + vedioChannelNum +
                    ", mediaChannelList=" + mediaChannelList +
                    '}';
        }
    }




    public int getBeatInterval() {
        return beatInterval;
    }

    public void setBeatInterval(int beatInterval) {
        this.beatInterval = beatInterval;
    }

    public int getTCPTimeout() {
        return TCPTimeout;
    }

    public void setTCPTimeout(int TCPTimeout) {
        this.TCPTimeout = TCPTimeout;
    }

    public int getTCPretransTimes() {
        return TCPretransTimes;
    }

    public void setTCPretransTimes(int TCPretransTimes) {
        this.TCPretransTimes = TCPretransTimes;
    }

    public int getUDPTimeout() {
        return UDPTimeout;
    }

    public void setUDPTimeout(int UDPTimeout) {
        this.UDPTimeout = UDPTimeout;
    }

    public int getUDPretransTimes() {
        return UDPretransTimes;
    }

    public void setUDPretransTimes(int UDPretransTimes) {
        this.UDPretransTimes = UDPretransTimes;
    }

    public int getSMSTimeout() {
        return SMSTimeout;
    }

    public void setSMSTimeout(int SMSTimeout) {
        this.SMSTimeout = SMSTimeout;
    }

    public int getSMSretransTimes() {
        return SMSretransTimes;
    }

    public void setSMSretransTimes(int SMSretransTimes) {
        this.SMSretransTimes = SMSretransTimes;
    }

    public String getMainServerAPN() {
        return mainServerAPN;
    }

    public void setMainServerAPN(String mainServerAPN) {
        this.mainServerAPN = mainServerAPN;
    }

    public String getMainServerUsername() {
        return mainServerUsername;
    }

    public void setMainServerUsername(String mainServerUsername) {
        this.mainServerUsername = mainServerUsername;
    }

    public String getMainServerPassword() {
        return mainServerPassword;
    }

    public void setMainServerPassword(String mainServerPassword) {
        this.mainServerPassword = mainServerPassword;
    }

    public String getMainServerHost() {
        return mainServerHost;
    }

    public void setMainServerHost(String mainServerHost) {
        this.mainServerHost = mainServerHost;
    }

    public String getBackupServerAPN() {
        return backupServerAPN;
    }

    public void setBackupServerAPN(String backupServerAPN) {
        this.backupServerAPN = backupServerAPN;
    }

    public String getBackupServerUsername() {
        return backupServerUsername;
    }

    public void setBackupServerUsername(String backupServerUsername) {
        this.backupServerUsername = backupServerUsername;
    }

    public String getBackupServerPassword() {
        return backupServerPassword;
    }

    public void setBackupServerPassword(String backupServerPassword) {
        this.backupServerPassword = backupServerPassword;
    }

    public String getBackupServerHost() {
        return backupServerHost;
    }

    public void setBackupServerHost(String backupServerHost) {
        this.backupServerHost = backupServerHost;
    }

    public int getServerTCPport() {
        return serverTCPport;
    }

    public void setServerTCPport(int serverTCPport) {
        this.serverTCPport = serverTCPport;
    }

    public int getServerUDPport() {
        return serverUDPport;
    }

    public void setServerUDPport(int serverUDPport) {
        this.serverUDPport = serverUDPport;
    }

    public String getICverifyMainServerHost() {
        return ICverifyMainServerHost;
    }

    public void setICverifyMainServerHost(String ICverifyMainServerHost) {
        this.ICverifyMainServerHost = ICverifyMainServerHost;
    }

    public int getICverifyServerTCPport() {
        return ICverifyServerTCPport;
    }

    public void setICverifyServerTCPport(int ICverifyServerTCPport) {
        this.ICverifyServerTCPport = ICverifyServerTCPport;
    }

    public int getICverifyServerUDPport() {
        return ICverifyServerUDPport;
    }

    public void setICverifyServerUDPport(int ICverifyServerUDPport) {
        this.ICverifyServerUDPport = ICverifyServerUDPport;
    }

    public String getICverifyBackupServerHost() {
        return ICverifyBackupServerHost;
    }

    public void setICverifyBackupServerHost(String ICverifyBackupServerHost) {
        this.ICverifyBackupServerHost = ICverifyBackupServerHost;
    }

    public int getLocatonInfoStrategy() {
        return locatonInfoStrategy;
    }

    public void setLocatonInfoStrategy(int locatonInfoStrategy) {
        this.locatonInfoStrategy = locatonInfoStrategy;
    }

    public int getLocatonInfoPlan() {
        return locatonInfoPlan;
    }

    public void setLocatonInfoPlan(int locatonInfoPlan) {
        this.locatonInfoPlan = locatonInfoPlan;
    }

    public int getUnloginTimeInterval() {
        return unloginTimeInterval;
    }

    public void setUnloginTimeInterval(int unloginTimeInterval) {
        this.unloginTimeInterval = unloginTimeInterval;
    }

    public int getSleepTimeInterval() {
        return sleepTimeInterval;
    }

    public void setSleepTimeInterval(int sleepTimeInterval) {
        this.sleepTimeInterval = sleepTimeInterval;
    }

    public int getWarningTimeInterval() {
        return warningTimeInterval;
    }

    public void setWarningTimeInterval(int warningTimeInterval) {
        this.warningTimeInterval = warningTimeInterval;
    }

    public int getDefaultInterval() {
        return defaultInterval;
    }

    public void setDefaultInterval(int defaultInterval) {
        this.defaultInterval = defaultInterval;
    }

    public int getDefaultDistanceInterval() {
        return defaultDistanceInterval;
    }

    public void setDefaultDistanceInterval(int defaultDistanceInterval) {
        this.defaultDistanceInterval = defaultDistanceInterval;
    }

    public int getUnloginDistanceInterval() {
        return unloginDistanceInterval;
    }

    public void setUnloginDistanceInterval(int unloginDistanceInterval) {
        this.unloginDistanceInterval = unloginDistanceInterval;
    }

    public int getSleepDistanceInterval() {
        return sleepDistanceInterval;
    }

    public void setSleepDistanceInterval(int sleepDistanceInterval) {
        this.sleepDistanceInterval = sleepDistanceInterval;
    }

    public int getWarningDistanceInterval() {
        return warningDistanceInterval;
    }

    public void setWarningDistanceInterval(int warningDistanceInterval) {
        this.warningDistanceInterval = warningDistanceInterval;
    }

    public int getRetransmissionAngle() {
        return retransmissionAngle;
    }

    public void setRetransmissionAngle(int retransmissionAngle) {
        this.retransmissionAngle = retransmissionAngle;
    }

    public int getElectronicFenceRadius() {
        return electronicFenceRadius;
    }

    public void setElectronicFenceRadius(int electronicFenceRadius) {
        this.electronicFenceRadius = electronicFenceRadius;
    }

    public String getPlatformPhoneNum() {
        return platformPhoneNum;
    }

    public void setPlatformPhoneNum(String platformPhoneNum) {
        this.platformPhoneNum = platformPhoneNum;
    }

    public String getResetPhoneNum() {
        return resetPhoneNum;
    }

    public void setResetPhoneNum(String resetPhoneNum) {
        this.resetPhoneNum = resetPhoneNum;
    }

    public String getRestorePhoneNum() {
        return restorePhoneNum;
    }

    public void setRestorePhoneNum(String restorePhoneNum) {
        this.restorePhoneNum = restorePhoneNum;
    }

    public String getPlatformSMSphoneNum() {
        return platformSMSphoneNum;
    }

    public void setPlatformSMSphoneNum(String platformSMSphoneNum) {
        this.platformSMSphoneNum = platformSMSphoneNum;
    }

    public String getAlarmSMSphoneNum() {
        return alarmSMSphoneNum;
    }

    public void setAlarmSMSphoneNum(String alarmSMSphoneNum) {
        this.alarmSMSphoneNum = alarmSMSphoneNum;
    }

    public int getPhoneStrategy() {
        return phoneStrategy;
    }

    public void setPhoneStrategy(int phoneStrategy) {
        this.phoneStrategy = phoneStrategy;
    }

    public int getLongestPhoneTime() {
        return longestPhoneTime;
    }

    public void setLongestPhoneTime(int longestPhoneTime) {
        this.longestPhoneTime = longestPhoneTime;
    }

    public int getMonthLongestPhoneTime() {
        return monthLongestPhoneTime;
    }

    public void setMonthLongestPhoneTime(int monthLongestPhoneTime) {
        this.monthLongestPhoneTime = monthLongestPhoneTime;
    }

    public String getMonitorPhone() {
        return monitorPhone;
    }

    public void setMonitorPhone(String monitorPhone) {
        this.monitorPhone = monitorPhone;
    }

    public String getPlatformPrivilegeSMS() {
        return platformPrivilegeSMS;
    }

    public void setPlatformPrivilegeSMS(String platformPrivilegeSMS) {
        this.platformPrivilegeSMS = platformPrivilegeSMS;
    }

    public int getAlarmShieldingWord() {
        return alarmShieldingWord;
    }

    public void setAlarmShieldingWord(int alarmShieldingWord) {
        this.alarmShieldingWord = alarmShieldingWord;
    }

    public int getAlarmSMS() {
        return alarmSMS;
    }

    public void setAlarmSMS(int alarmSMS) {
        this.alarmSMS = alarmSMS;
    }

    public int getAlarmPhoto() {
        return alarmPhoto;
    }

    public void setAlarmPhoto(int alarmPhoto) {
        this.alarmPhoto = alarmPhoto;
    }

    public int getAlarmPhotoSave() {
        return alarmPhotoSave;
    }

    public void setAlarmPhotoSave(int alarmPhotoSave) {
        this.alarmPhotoSave = alarmPhotoSave;
    }

    public int getKeyFlag() {
        return keyFlag;
    }

    public void setKeyFlag(int keyFlag) {
        this.keyFlag = keyFlag;
    }

    public int getHighestSpeed() {
        return highestSpeed;
    }

    public void setHighestSpeed(int highestSpeed) {
        this.highestSpeed = highestSpeed;
    }

    public int getSpeedingTime() {
        return speedingTime;
    }

    public void setSpeedingTime(int speedingTime) {
        this.speedingTime = speedingTime;
    }

    public int getDriverTimeLimit() {
        return driverTimeLimit;
    }

    public void setDriverTimeLimit(int driverTimeLimit) {
        this.driverTimeLimit = driverTimeLimit;
    }

    public int getTodayDriverTime() {
        return todayDriverTime;
    }

    public void setTodayDriverTime(int todayDriverTime) {
        this.todayDriverTime = todayDriverTime;
    }

    public int getLeastRestTime() {
        return leastRestTime;
    }

    public void setLeastRestTime(int leastRestTime) {
        this.leastRestTime = leastRestTime;
    }

    public int getLongestPortTime() {
        return longestPortTime;
    }

    public void setLongestPortTime(int longestPortTime) {
        this.longestPortTime = longestPortTime;
    }

    public int getSpeedingWarningDifferenceValue() {
        return speedingWarningDifferenceValue;
    }

    public void setSpeedingWarningDifferenceValue(int speedingWarningDifferenceValue) {
        this.speedingWarningDifferenceValue = speedingWarningDifferenceValue;
    }

    public int getTiredDriveWarningDifferenceValue() {
        return tiredDriveWarningDifferenceValue;
    }

    public void setTiredDriveWarningDifferenceValue(int tiredDriveWarningDifferenceValue) {
        this.tiredDriveWarningDifferenceValue = tiredDriveWarningDifferenceValue;
    }

    public int getRolloverParam() {
        return rolloverParam;
    }

    public void setRolloverParam(int rolloverParam) {
        this.rolloverParam = rolloverParam;
    }

    public TimingPhotoControlBit getTimingPhotoControlBit() {
        return timingPhotoControlBit;
    }

    public void setTimingPhotoControlBit(TimingPhotoControlBit timingPhotoControlBit) {
        this.timingPhotoControlBit = timingPhotoControlBit;
    }

    public FixedPictureControlBit getFixedPictureControlBit() {
        return fixedPictureControlBit;
    }

    public void setFixedPictureControlBit(FixedPictureControlBit fixedPictureControlBit) {
        this.fixedPictureControlBit = fixedPictureControlBit;
    }

    public int getCameraQuality() {
        return cameraQuality;
    }

    public void setCameraQuality(int cameraQuality) {
        this.cameraQuality = cameraQuality;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getLicensePlateColor() {
        return licensePlateColor;
    }

    public void setLicensePlateColor(int licensePlateColor) {
        this.licensePlateColor = licensePlateColor;
    }

    public GNSS getGnss() {
        return gnss;
    }

    public void setGnss(GNSS gnss) {
        this.gnss = gnss;
    }

    public int getGNSSbaudRatio() {
        return GNSSbaudRatio;
    }

    public void setGNSSbaudRatio(int GNSSbaudRatio) {
        this.GNSSbaudRatio = GNSSbaudRatio;
    }

    public int getGNSSdataOutputFrequency() {
        return GNSSdataOutputFrequency;
    }

    public void setGNSSdataOutputFrequency(int GNSSdataOutputFrequency) {
        this.GNSSdataOutputFrequency = GNSSdataOutputFrequency;
    }

    public int getGNSSdataFrequency() {
        return GNSSdataFrequency;
    }

    public void setGNSSdataFrequency(int GNSSdataFrequency) {
        this.GNSSdataFrequency = GNSSdataFrequency;
    }

    public static class GNSS {
        // 0:未使用 GPS 卫星进行定位;1:使用 GPS 卫星进行定位
        private boolean GPS;
        // 0:未使用北斗卫星进行定位;1:使用北斗卫星进行定位
        private boolean beidou;
        // 0:未使用 GLONASS 卫星进行定位;1:使用 GLONASS 卫星进行定位
        private boolean GLONASS;
        // 0:未使用 Galileo 卫星进行定位;1:使用 Galileo 卫星进行定位
        private boolean Galileo;

        public boolean isGPS() {
            return GPS;
        }

        public void setGPS(boolean GPS) {
            this.GPS = GPS;
        }

        public boolean isBeidou() {
            return beidou;
        }

        public void setBeidou(boolean beidou) {
            this.beidou = beidou;
        }

        public boolean isGLONASS() {
            return GLONASS;
        }

        public void setGLONASS(boolean GLONASS) {
            this.GLONASS = GLONASS;
        }

        public boolean isGalileo() {
            return Galileo;
        }

        public void setGalileo(boolean galileo) {
            Galileo = galileo;
        }

        @Override
        public String toString() {
            return "GNSS{" +
                    "GPS=" + GPS +
                    ", beidou=" + beidou +
                    ", GLONASS=" + GLONASS +
                    ", Galileo=" + Galileo +
                    '}';
        }
    }

    public static class TimingPhotoControlBit {
        // 摄像通道 1 定时拍照开关标志
        private boolean cameraChannelTiming1;
        // 摄像通道 2 定时拍照开关标志
        private boolean cameraChannelTiming2;
        // 摄像通道 3 定时拍照开关标志
        private boolean cameraChannelTiming3;
        // 摄像通道 4 定时拍照开关标志
        private boolean cameraChannelTiming4;
        // 摄像通道 5 定时拍照开关标志
        private boolean cameraChannelTiming5;
        // 摄像通道 1 定时拍照存储标志
        private boolean cameraChannelTimingSave1;
        // 摄像通道 2 定时拍照存储标志
        private boolean cameraChannelTimingSave2;
        // 摄像通道 3 定时拍照存储标志
        private boolean cameraChannelTimingSave3;
        // 摄像通道 4 定时拍照存储标志
        private boolean cameraChannelTimingSave4;
        // 摄像通道 5 定时拍照存储标志
        private boolean cameraChannelTimingSave5;
        // 定时时间单位
        private int TimingUnit;
        // 定时时间间隔
        private int TimingInterval;

        public boolean isCameraChannelTiming1() {
            return cameraChannelTiming1;
        }

        public void setCameraChannelTiming1(boolean cameraChannelTiming1) {
            this.cameraChannelTiming1 = cameraChannelTiming1;
        }

        public boolean isCameraChannelTiming2() {
            return cameraChannelTiming2;
        }

        public void setCameraChannelTiming2(boolean cameraChannelTiming2) {
            this.cameraChannelTiming2 = cameraChannelTiming2;
        }

        public boolean isCameraChannelTiming3() {
            return cameraChannelTiming3;
        }

        public void setCameraChannelTiming3(boolean cameraChannelTiming3) {
            this.cameraChannelTiming3 = cameraChannelTiming3;
        }

        public boolean isCameraChannelTiming4() {
            return cameraChannelTiming4;
        }

        public void setCameraChannelTiming4(boolean cameraChannelTiming4) {
            this.cameraChannelTiming4 = cameraChannelTiming4;
        }

        public boolean isCameraChannelTiming5() {
            return cameraChannelTiming5;
        }

        public void setCameraChannelTiming5(boolean cameraChannelTiming5) {
            this.cameraChannelTiming5 = cameraChannelTiming5;
        }

        public boolean isCameraChannelTimingSave1() {
            return cameraChannelTimingSave1;
        }

        public void setCameraChannelTimingSave1(boolean cameraChannelTimingSave1) {
            this.cameraChannelTimingSave1 = cameraChannelTimingSave1;
        }

        public boolean isCameraChannelTimingSave2() {
            return cameraChannelTimingSave2;
        }

        public void setCameraChannelTimingSave2(boolean cameraChannelTimingSave2) {
            this.cameraChannelTimingSave2 = cameraChannelTimingSave2;
        }

        public boolean isCameraChannelTimingSave3() {
            return cameraChannelTimingSave3;
        }

        public void setCameraChannelTimingSave3(boolean cameraChannelTimingSave3) {
            this.cameraChannelTimingSave3 = cameraChannelTimingSave3;
        }

        public boolean isCameraChannelTimingSave4() {
            return cameraChannelTimingSave4;
        }

        public void setCameraChannelTimingSave4(boolean cameraChannelTimingSave4) {
            this.cameraChannelTimingSave4 = cameraChannelTimingSave4;
        }

        public boolean isCameraChannelTimingSave5() {
            return cameraChannelTimingSave5;
        }

        public void setCameraChannelTimingSave5(boolean cameraChannelTimingSave5) {
            this.cameraChannelTimingSave5 = cameraChannelTimingSave5;
        }

        public int getTimingUnit() {
            return TimingUnit;
        }

        public void setTimingUnit(int timingUnit) {
            TimingUnit = timingUnit;
        }

        public int getTimingInterval() {
            return TimingInterval;
        }

        public void setTimingInterval(int timingInterval) {
            TimingInterval = timingInterval;
        }

        @Override
        public String toString() {
            return "TimingPhotoControlBit{" +
                    "cameraChannelTiming1=" + cameraChannelTiming1 +
                    ", cameraChannelTiming2=" + cameraChannelTiming2 +
                    ", cameraChannelTiming3=" + cameraChannelTiming3 +
                    ", cameraChannelTiming4=" + cameraChannelTiming4 +
                    ", cameraChannelTiming5=" + cameraChannelTiming5 +
                    ", cameraChannelTimingSave1=" + cameraChannelTimingSave1 +
                    ", cameraChannelTimingSave2=" + cameraChannelTimingSave2 +
                    ", cameraChannelTimingSave3=" + cameraChannelTimingSave3 +
                    ", cameraChannelTimingSave4=" + cameraChannelTimingSave4 +
                    ", cameraChannelTimingSave5=" + cameraChannelTimingSave5 +
                    ", TimingUnit=" + TimingUnit +
                    ", TimingInterval=" + TimingInterval +
                    '}';
        }
    }

    public static class FixedPictureControlBit {
        // 摄像通道 1 定拍照开关标志
        private boolean cameraChannelFixed1;
        // 摄像通道 2 定距拍照开关标志
        private boolean cameraChannelFixed2;
        // 摄像通道 3 定距拍照开关标志
        private boolean cameraChannelFixed3;
        // 摄像通道 4 定距拍照开关标志
        private boolean cameraChannelFixed4;
        // 摄像通道 5 定距拍照开关标志
        private boolean cameraChannelFixed5;
        // 摄像通道 1 定距拍照存储标志
        private boolean cameraChannelFixedSave1;
        // 摄像通道 2 定距拍照存储标志
        private boolean cameraChannelFixedSave2;
        // 摄像通道 3 定距拍照存储标志
        private boolean cameraChannelFixedSave3;
        // 摄像通道 4 定距拍照存储标志
        private boolean cameraChannelFixedSave4;
        // 摄像通道 5 定距拍照存储标志
        private boolean cameraChannelFixedSave5;
        // 定距距离单位
        private int FixedUnit;
        // 定距距离间隔
        private int FixedInterval;

        public boolean isCameraChannelFixed1() {
            return cameraChannelFixed1;
        }

        public void setCameraChannelFixed1(boolean cameraChannelFixed1) {
            this.cameraChannelFixed1 = cameraChannelFixed1;
        }

        public boolean isCameraChannelFixed2() {
            return cameraChannelFixed2;
        }

        public void setCameraChannelFixed2(boolean cameraChannelFixed2) {
            this.cameraChannelFixed2 = cameraChannelFixed2;
        }

        public boolean isCameraChannelFixed3() {
            return cameraChannelFixed3;
        }

        public void setCameraChannelFixed3(boolean cameraChannelFixed3) {
            this.cameraChannelFixed3 = cameraChannelFixed3;
        }

        public boolean isCameraChannelFixed4() {
            return cameraChannelFixed4;
        }

        public void setCameraChannelFixed4(boolean cameraChannelFixed4) {
            this.cameraChannelFixed4 = cameraChannelFixed4;
        }

        public boolean isCameraChannelFixed5() {
            return cameraChannelFixed5;
        }

        public void setCameraChannelFixed5(boolean cameraChannelFixed5) {
            this.cameraChannelFixed5 = cameraChannelFixed5;
        }

        public boolean isCameraChannelFixedSave1() {
            return cameraChannelFixedSave1;
        }

        public void setCameraChannelFixedSave1(boolean cameraChannelFixedSave1) {
            this.cameraChannelFixedSave1 = cameraChannelFixedSave1;
        }

        public boolean isCameraChannelFixedSave2() {
            return cameraChannelFixedSave2;
        }

        public void setCameraChannelFixedSave2(boolean cameraChannelFixedSave2) {
            this.cameraChannelFixedSave2 = cameraChannelFixedSave2;
        }

        public boolean isCameraChannelFixedSave3() {
            return cameraChannelFixedSave3;
        }

        public void setCameraChannelFixedSave3(boolean cameraChannelFixedSave3) {
            this.cameraChannelFixedSave3 = cameraChannelFixedSave3;
        }

        public boolean isCameraChannelFixedSave4() {
            return cameraChannelFixedSave4;
        }

        public void setCameraChannelFixedSave4(boolean cameraChannelFixedSave4) {
            this.cameraChannelFixedSave4 = cameraChannelFixedSave4;
        }

        public boolean isCameraChannelFixedSave5() {
            return cameraChannelFixedSave5;
        }

        public void setCameraChannelFixedSave5(boolean cameraChannelFixedSave5) {
            this.cameraChannelFixedSave5 = cameraChannelFixedSave5;
        }

        public int getFixedUnit() {
            return FixedUnit;
        }

        public void setFixedUnit(int fixedUnit) {
            FixedUnit = fixedUnit;
        }

        public int getFixedInterval() {
            return FixedInterval;
        }

        public void setFixedInterval(int fixedInterval) {
            FixedInterval = fixedInterval;
        }

        @Override
        public String toString() {
            return "FixedPictureControlBit{" +
                    "cameraChannelFixed1=" + cameraChannelFixed1 +
                    ", cameraChannelFixed2=" + cameraChannelFixed2 +
                    ", cameraChannelFixed3=" + cameraChannelFixed3 +
                    ", cameraChannelFixed4=" + cameraChannelFixed4 +
                    ", cameraChannelFixed5=" + cameraChannelFixed5 +
                    ", cameraChannelFixedSave1=" + cameraChannelFixedSave1 +
                    ", cameraChannelFixedSave2=" + cameraChannelFixedSave2 +
                    ", cameraChannelFixedSave3=" + cameraChannelFixedSave3 +
                    ", cameraChannelFixedSave4=" + cameraChannelFixedSave4 +
                    ", cameraChannelFixedSave5=" + cameraChannelFixedSave5 +
                    ", FixedUnit=" + FixedUnit +
                    ", FixedInterval=" + FixedInterval +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TerminalParam{" +
                "beatInterval=" + beatInterval +
                ", TCPTimeout=" + TCPTimeout +
                ", TCPretransTimes=" + TCPretransTimes +
                ", UDPTimeout=" + UDPTimeout +
                ", UDPretransTimes=" + UDPretransTimes +
                ", SMSTimeout=" + SMSTimeout +
                ", SMSretransTimes=" + SMSretransTimes +
                ", mainServerAPN='" + mainServerAPN + '\'' +
                ", mainServerUsername='" + mainServerUsername + '\'' +
                ", mainServerPassword='" + mainServerPassword + '\'' +
                ", mainServerHost='" + mainServerHost + '\'' +
                ", backupServerAPN='" + backupServerAPN + '\'' +
                ", backupServerUsername='" + backupServerUsername + '\'' +
                ", backupServerPassword='" + backupServerPassword + '\'' +
                ", backupServerHost='" + backupServerHost + '\'' +
                ", serverTCPport=" + serverTCPport +
                ", serverUDPport=" + serverUDPport +
                ", ICverifyMainServerHost='" + ICverifyMainServerHost + '\'' +
                ", ICverifyServerTCPport=" + ICverifyServerTCPport +
                ", ICverifyServerUDPport=" + ICverifyServerUDPport +
                ", ICverifyBackupServerHost='" + ICverifyBackupServerHost + '\'' +
                ", locatonInfoStrategy=" + locatonInfoStrategy +
                ", locatonInfoPlan=" + locatonInfoPlan +
                ", unloginTimeInterval=" + unloginTimeInterval +
                ", sleepTimeInterval=" + sleepTimeInterval +
                ", warningTimeInterval=" + warningTimeInterval +
                ", defaultInterval=" + defaultInterval +
                ", defaultDistanceInterval=" + defaultDistanceInterval +
                ", unloginDistanceInterval=" + unloginDistanceInterval +
                ", sleepDistanceInterval=" + sleepDistanceInterval +
                ", warningDistanceInterval=" + warningDistanceInterval +
                ", retransmissionAngle=" + retransmissionAngle +
                ", electronicFenceRadius=" + electronicFenceRadius +
                ", platformPhoneNum='" + platformPhoneNum + '\'' +
                ", resetPhoneNum='" + resetPhoneNum + '\'' +
                ", restorePhoneNum='" + restorePhoneNum + '\'' +
                ", platformSMSphoneNum='" + platformSMSphoneNum + '\'' +
                ", alarmSMSphoneNum='" + alarmSMSphoneNum + '\'' +
                ", phoneStrategy=" + phoneStrategy +
                ", longestPhoneTime=" + longestPhoneTime +
                ", monthLongestPhoneTime=" + monthLongestPhoneTime +
                ", monitorPhone='" + monitorPhone + '\'' +
                ", platformPrivilegeSMS='" + platformPrivilegeSMS + '\'' +
                ", alarmShieldingWord=" + alarmShieldingWord +
                ", alarmSMS=" + alarmSMS +
                ", alarmPhoto=" + alarmPhoto +
                ", alarmPhotoSave=" + alarmPhotoSave +
                ", keyFlag=" + keyFlag +
                ", highestSpeed=" + highestSpeed +
                ", speedingTime=" + speedingTime +
                ", driverTimeLimit=" + driverTimeLimit +
                ", todayDriverTime=" + todayDriverTime +
                ", leastRestTime=" + leastRestTime +
                ", longestPortTime=" + longestPortTime +
                ", speedingWarningDifferenceValue=" + speedingWarningDifferenceValue +
                ", tiredDriveWarningDifferenceValue=" + tiredDriveWarningDifferenceValue +
                ", rolloverParam=" + rolloverParam +
                ", timingPhotoControlBit=" + timingPhotoControlBit +
                ", fixedPictureControlBit=" + fixedPictureControlBit +
                ", cameraQuality=" + cameraQuality +
                ", light=" + light +
                ", contrast=" + contrast +
                ", saturation=" + saturation +
                ", color=" + color +
                ", mileage=" + mileage +
                ", provinceId=" + provinceId +
                ", cityId=" + cityId +
                ", licensePlate='" + licensePlate + '\'' +
                ", licensePlateColor=" + licensePlateColor +
                ", gnss=" + gnss +
                ", GNSSbaudRatio=" + GNSSbaudRatio +
                ", GNSSdataOutputFrequency=" + GNSSdataOutputFrequency +
                ", GNSSdataFrequency=" + GNSSdataFrequency +
                ", mediaParam=" + mediaParam +
                ", mediaChannelList=" + mediaChannelList +
                ", videoSheildBits=" + videoSheildBits +
                '}';
    }
}
