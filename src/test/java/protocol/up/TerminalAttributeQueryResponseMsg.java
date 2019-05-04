package protocol.up;

import cn.edu.sdu.JT808Server.protocol.MsgHeader;
import cn.edu.sdu.JT808Server.protocol.PackageData;

public class TerminalAttributeQueryResponseMsg extends PackageData {
    // 终端类型
    private TerminalType terminalType;
    // 制造商ID 5 个字节，终端制造商编码
    private String manufacturerId;
    // 终端型号 8个字节， 此终端型号 由制造商自行定义 位数不足八位的，补0x00
    private String terminalModel;
    // 终端ID 7个字节， 由大写字母 和数字组成， 此终端 ID由制造 商自行定义
    private String terminalId;
    // 终端 SIM 卡 ICCID
    private String ICCID;
    // 终端硬件版本号长度
    private int terminalHardwareVersionLength;
    // 终端硬件版本号
    private String terminalHardwareVersion;
    // 终端固件版本号长度
    private int terminalFirmwareVersionLength;
    // 终端固件版本号
    private String terminalFirmwareVersion;
    // GNSS 模块属性
    private GNSSAttribute gnssAttribute;
    // 通信模块属性
    private CommunicationModuleAttribute communicationModuleAttribute;

    public TerminalAttributeQueryResponseMsg() {}

    public TerminalAttributeQueryResponseMsg(PackageData packageData) {
        this.checkSum = packageData.getCheckSum();
        this.msgBody = packageData.getMsgBody();
        this.msgHeader = packageData.getMsgHeader();
    }

    @Override
    public String toString() {
        return "TerminalAttributeQueryResponseMsg{" +
                "terminalType=" + terminalType +
                ", manufacturerId='" + manufacturerId + '\'' +
                ", terminalModel='" + terminalModel + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", ICCID='" + ICCID + '\'' +
                ", terminalHardwareVersionLength=" + terminalHardwareVersionLength +
                ", terminalHardwareVersion='" + terminalHardwareVersion + '\'' +
                ", terminalFirmwareVersionLength=" + terminalFirmwareVersionLength +
                ", terminalFirmwareVersion='" + terminalFirmwareVersion + '\'' +
                ", gnssAttribute=" + gnssAttribute +
                ", communicationModuleAttribute=" + communicationModuleAttribute +
                '}';
    }

    public TerminalType getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(TerminalType terminalType) {
        this.terminalType = terminalType;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getTerminalModel() {
        return terminalModel;
    }

    public void setTerminalModel(String terminalModel) {
        this.terminalModel = terminalModel;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getICCID() {
        return ICCID;
    }

    public void setICCID(String ICCID) {
        this.ICCID = ICCID;
    }

    public int getTerminalHardwareVersionLength() {
        return terminalHardwareVersionLength;
    }

    public void setTerminalHardwareVersionLength(int terminalHardwareVersionLength) {
        this.terminalHardwareVersionLength = terminalHardwareVersionLength;
    }

    public String getTerminalHardwareVersion() {
        return terminalHardwareVersion;
    }

    public void setTerminalHardwareVersion(String terminalHardwareVersion) {
        this.terminalHardwareVersion = terminalHardwareVersion;
    }

    public int getTerminalFirmwareVersionLength() {
        return terminalFirmwareVersionLength;
    }

    public void setTerminalFirmwareVersionLength(int terminalFirmwareVersionLength) {
        this.terminalFirmwareVersionLength = terminalFirmwareVersionLength;
    }

    public String getTerminalFirmwareVersion() {
        return terminalFirmwareVersion;
    }

    public void setTerminalFirmwareVersion(String terminalFirmwareVersion) {
        this.terminalFirmwareVersion = terminalFirmwareVersion;
    }

    public GNSSAttribute getGnssAttribute() {
        return gnssAttribute;
    }

    public void setGnssAttribute(GNSSAttribute gnssAttribute) {
        this.gnssAttribute = gnssAttribute;
    }

    public CommunicationModuleAttribute getCommunicationModuleAttribute() {
        return communicationModuleAttribute;
    }

    public void setCommunicationModuleAttribute(CommunicationModuleAttribute communicationModuleAttribute) {
        this.communicationModuleAttribute = communicationModuleAttribute;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }


    public static class TerminalType {
        // bit0,0:不适用客运车辆,1:适用客运车辆;
        private boolean passengerVehicles;
        // bit1,0:不适用危险品车辆,1:适用危险品车辆;
        private boolean dangerousGoodsVehicles;
        // bit2,0:不适用普通货运车辆,1:适用普通货运车辆;
        private boolean commonFreightVehicle;
        // bit3,0:不适用出租车辆,1:适用出租车辆;
        private boolean taxi;
        // bit6,0:不支持硬盘录像,1:支持硬盘录像;
        private boolean supportingHardDiskVideo;
        // bit7,0:一体机,1:分体机
        private boolean integratedMachine;

        public boolean isPassengerVehicles() {
            return passengerVehicles;
        }

        public void setPassengerVehicles(boolean passengerVehicles) {
            this.passengerVehicles = passengerVehicles;
        }

        public boolean isDangerousGoodsVehicles() {
            return dangerousGoodsVehicles;
        }

        public void setDangerousGoodsVehicles(boolean dangerousGoodsVehicles) {
            this.dangerousGoodsVehicles = dangerousGoodsVehicles;
        }

        public boolean isCommonFreightVehicle() {
            return commonFreightVehicle;
        }

        public void setCommonFreightVehicle(boolean commonFreightVehicle) {
            this.commonFreightVehicle = commonFreightVehicle;
        }

        public boolean isTaxi() {
            return taxi;
        }

        public void setTaxi(boolean taxi) {
            this.taxi = taxi;
        }

        public boolean isSupportingHardDiskVideo() {
            return supportingHardDiskVideo;
        }

        public void setSupportingHardDiskVideo(boolean supportingHardDiskVideo) {
            this.supportingHardDiskVideo = supportingHardDiskVideo;
        }

        public boolean isIntegratedMachine() {
            return integratedMachine;
        }

        public void setIntegratedMachine(boolean integratedMachine) {
            this.integratedMachine = integratedMachine;
        }
    }

    public static class GNSSAttribute {
        // bit0,0:不支持 GPS 定位, 1:支持 GPS 定位
        private boolean GPS;
        // bit1,0:不支持北斗定位, 1:支持北斗定位
        private boolean beidou;
        // bit2,0:不支持 GLONASS 定位, 1:支持 GLONASS 定位
        private boolean GLONASS;
        // bit3,0:不支持 Galileo 定位, 1:支持 Galileo 定位
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
    }

    public static class CommunicationModuleAttribute {
        // bit0,0:不支持GPRS通信, 1:支持GPRS通信
        private boolean GPSCommunication;
        // bit1,0:不支持CDMA通信, 1:支持CDMA通信
        private boolean CDMA;
        // bit2,0:不支持TD-SCDMA通信, 1:支持TD-SCDMA通信
        private boolean TDSCMA;
        // bit3,0:不支持WCDMA通信, 1:支持WCDMA通信
        private boolean WCDMA;
        // bit4,0:不支持CDMA2000通信, 1:支持CDMA2000通信
        private boolean CDMA2000;
        // bit5,0:不支持TD-LTE通信, 1:支持TD-LTE通信
        private boolean TDLTE;
        // bit7,0:不支持其他通信方式, 1:支持其他通信方式
        private boolean otherCommunication;

        public boolean isGPSCommunication() {
            return GPSCommunication;
        }

        public void setGPSCommunication(boolean GPSCommunication) {
            this.GPSCommunication = GPSCommunication;
        }

        public boolean isCDMA() {
            return CDMA;
        }

        public void setCDMA(boolean CDMA) {
            this.CDMA = CDMA;
        }

        public boolean isTDSCMA() {
            return TDSCMA;
        }

        public void setTDSCMA(boolean TDSCMA) {
            this.TDSCMA = TDSCMA;
        }

        public boolean isWCDMA() {
            return WCDMA;
        }

        public void setWCDMA(boolean WCDMA) {
            this.WCDMA = WCDMA;
        }

        public boolean isCDMA2000() {
            return CDMA2000;
        }

        public void setCDMA2000(boolean CDMA2000) {
            this.CDMA2000 = CDMA2000;
        }

        public boolean isTDLTE() {
            return TDLTE;
        }

        public void setTDLTE(boolean TDLTE) {
            this.TDLTE = TDLTE;
        }

        public boolean isOtherCommunication() {
            return otherCommunication;
        }

        public void setOtherCommunication(boolean otherCommunication) {
            this.otherCommunication = otherCommunication;
        }
    }
}
