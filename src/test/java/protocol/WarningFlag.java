package protocol;

public class WarningFlag {
    // 0:紧急报警,触动报警开关后触发
    private boolean warningFlag;
    // 1:超速报警
    private boolean overSpeeding;
    // 2:疲劳驾驶
    private boolean overTired;
    // 3:危险预警
    private boolean dangerous;
    // 4:GNSS 模块发生故障
    private boolean GNSSFault;
    // 5:GNSS 天线未接或被剪断
    private boolean GNSSAntennaFault;
    // 6:GNSS 天线短路
    private boolean GNSSAntennaShortCircuit;
    // 7:终端主电源欠压
    private boolean terminalMainPowerUnderVoltage;
    // 8:终端主电源掉电
    private boolean terminalMainPowerFailure;
    // 9:终端 LCD 或显示器故障
    private boolean TerminalLCDFault;
    // 10:TTS 模块故障
    private boolean TTSFault;
    // 11:摄像头故障
    private boolean cameraFault;
    // 12:道路运输证 IC 卡模块故障
    private boolean ICCardFault;
    // 13:超速预警
    private boolean speeding;
    // 14:疲劳驾驶预警
    private boolean tired;
    // 18:当天累计驾驶超时
    private boolean driveTimeout;
    // 19:超时停车
    private boolean parkingOvertime;
    // 20:进出区域
    private boolean throughArea;
    // 21:进出路线
    private boolean throughRoad;
    // 22:路段行驶时间不足/过长
    private boolean roadTimeout;
    // 23:路线偏离报警
    private boolean roadFault;
    // 24:车辆 VSS 故障
    private boolean VSSFault;
    // 25:车辆油量异常
    private boolean vehicleOilException;
    // 26:车辆被盗(通过车辆防盗器)
    private boolean vehicleTheft;
    // 27:车辆非法点火
    private boolean vehicleIllegalIgnition;
    // 28:车辆非法位移
    private boolean vehicleIllegalShift;
    // 29:碰撞预警
    private boolean collisionWarning;
    // 30:侧翻预警
    private boolean rolloverWarning;
    // 31:非法开门报警(终端未设置区域时,不判断非法开门)
    private boolean illegalOpenDoor;

    @Override
    public String toString() {
        return "{ \"waringflag\" : " + warningFlag + ", \"overSpeeding\" : " + overSpeeding + ", \"overTired\" : "
                + overTired + ", \"dangeous\" : " + dangerous + ", \"GNSSFault\" : "
                + GNSSFault + ", \"GNSSAntennaFault\" : " + GNSSAntennaFault + ", \"GNSSAntennaShortCircuit\" : "
                + GNSSAntennaShortCircuit + ", \"terminalMainPowerUndervoltage\" : " + terminalMainPowerUnderVoltage
                + ", \"terminalMainPowerFailure\" : " + terminalMainPowerFailure + ", \"TerminalLCDFault\" : "
                + TerminalLCDFault + ", \"TTSFault\" : " + TTSFault + ", \"cameraFault\" : " + cameraFault
                + ", \"ICCardFault\" : " + ICCardFault + ", \"speeding\" : " + speeding + ", \"tired\" : "
                + tired + ", \"driveTimeout\" : " + driveTimeout + ", \"parkingOvertime\" : " + parkingOvertime
                + ", \"throughArea\" : " + throughArea + ", \"throughRoad\" : " + throughRoad
                + ", \"roadTimeout\" : " + roadTimeout + ", \"roadFault\" : " + roadFault + ", \"VSSFault\" : "
                + VSSFault + ", \"vehicleOilException\" : " + vehicleOilException + ", \"vehicleTheft\" : "
                + vehicleTheft + ", \"vehicleIllegalIgnition\" : " + vehicleIllegalIgnition + ", \"vehicleIllegalShift\" : "
                + vehicleIllegalShift + ", \"collisionWarning\" : " + collisionWarning + ", \"rolloverWarning\" : "
                + rolloverWarning + ", \"illegalOpenDoor\" : " + illegalOpenDoor + "  }";
    }

    public boolean isWarningfFlag() {
        return warningFlag;
    }

    public void setWarningFlag(boolean warningFlag) {
        this.warningFlag = warningFlag;
    }

    public boolean isOverSpeeding() {
        return overSpeeding;
    }

    public void setOverSpeeding(boolean overSpeeding) {
        this.overSpeeding = overSpeeding;
    }

    public boolean isOverTired() {
        return overTired;
    }

    public void setOverTired(boolean overTired) {
        this.overTired = overTired;
    }

    public boolean isDangerous() {
        return dangerous;
    }

    public void setDangerous(boolean dangerous) {
        this.dangerous = dangerous;
    }

    public boolean isGNSSFault() {
        return GNSSFault;
    }

    public void setGNSSFault(boolean GNSSFault) {
        this.GNSSFault = GNSSFault;
    }

    public boolean isGNSSAntennaFault() {
        return GNSSAntennaFault;
    }

    public void setGNSSAntennaFault(boolean GNSSAntennaFault) {
        this.GNSSAntennaFault = GNSSAntennaFault;
    }

    public boolean isGNSSAntennaShortCircuit() {
        return GNSSAntennaShortCircuit;
    }

    public void setGNSSAntennaShortCircuit(boolean GNSSAntennaShortCircuit) {
        this.GNSSAntennaShortCircuit = GNSSAntennaShortCircuit;
    }

    public boolean isTerminalMainPowerUnderVoltage() {
        return terminalMainPowerUnderVoltage;
    }

    public void setTerminalMainPowerUnderVoltage(boolean terminalMainPowerUnderVoltage) {
        this.terminalMainPowerUnderVoltage = terminalMainPowerUnderVoltage;
    }

    public boolean isTerminalMainPowerFailure() {
        return terminalMainPowerFailure;
    }

    public void setTerminalMainPowerFailure(boolean terminalMainPowerFailure) {
        this.terminalMainPowerFailure = terminalMainPowerFailure;
    }

    public boolean isTerminalLCDFault() {
        return TerminalLCDFault;
    }

    public void setTerminalLCDFault(boolean terminalLCDFault) {
        TerminalLCDFault = terminalLCDFault;
    }

    public boolean isTTSFault() {
        return TTSFault;
    }

    public void setTTSFault(boolean TTSFault) {
        this.TTSFault = TTSFault;
    }

    public boolean isCameraFault() {
        return cameraFault;
    }

    public void setCameraFault(boolean cameraFault) {
        this.cameraFault = cameraFault;
    }

    public boolean isICCardFault() {
        return ICCardFault;
    }

    public void setICCardFault(boolean ICCardFault) {
        this.ICCardFault = ICCardFault;
    }

    public boolean isSpeeding() {
        return speeding;
    }

    public void setSpeeding(boolean speeding) {
        this.speeding = speeding;
    }

    public boolean isTired() {
        return tired;
    }

    public void setTired(boolean tired) {
        this.tired = tired;
    }

    public boolean isDriveTimeout() {
        return driveTimeout;
    }

    public void setDriveTimeout(boolean driveTimeout) {
        this.driveTimeout = driveTimeout;
    }

    public boolean isParkingOvertime() {
        return parkingOvertime;
    }

    public void setParkingOvertime(boolean parkingOvertime) {
        this.parkingOvertime = parkingOvertime;
    }

    public boolean isThroughArea() {
        return throughArea;
    }

    public void setThroughArea(boolean throughArea) {
        this.throughArea = throughArea;
    }

    public boolean isThroughRoad() {
        return throughRoad;
    }

    public void setThroughRoad(boolean throughRoad) {
        this.throughRoad = throughRoad;
    }

    public boolean isRoadTimeout() {
        return roadTimeout;
    }

    public void setRoadTimeout(boolean roadTimeout) {
        this.roadTimeout = roadTimeout;
    }

    public boolean isRoadFault() {
        return roadFault;
    }

    public void setRoadFault(boolean roadFault) {
        this.roadFault = roadFault;
    }

    public boolean isVSSFault() {
        return VSSFault;
    }

    public void setVSSFault(boolean VSSFault) {
        this.VSSFault = VSSFault;
    }

    public boolean isVehicleOilException() {
        return vehicleOilException;
    }

    public void setVehicleOilException(boolean vehicleOilException) {
        this.vehicleOilException = vehicleOilException;
    }

    public boolean isVehicleTheft() {
        return vehicleTheft;
    }

    public void setVehicleTheft(boolean vehicleTheft) {
        this.vehicleTheft = vehicleTheft;
    }

    public boolean isVehicleIllegalIgnition() {
        return vehicleIllegalIgnition;
    }

    public void setVehicleIllegalIgnition(boolean vehicleIllegalIgnition) {
        this.vehicleIllegalIgnition = vehicleIllegalIgnition;
    }

    public boolean isVehicleIllegalShift() {
        return vehicleIllegalShift;
    }

    public void setVehicleIllegalShift(boolean vehicleIllegalShift) {
        this.vehicleIllegalShift = vehicleIllegalShift;
    }

    public boolean isCollisionWarning() {
        return collisionWarning;
    }

    public void setCollisionWarning(boolean collisionWarning) {
        this.collisionWarning = collisionWarning;
    }

    public boolean isRolloverWarning() {
        return rolloverWarning;
    }

    public void setRolloverWarning(boolean rolloverWarning) {
        this.rolloverWarning = rolloverWarning;
    }

    public boolean isIllegalOpenDoor() {
        return illegalOpenDoor;
    }

    public void setIllegalOpenDoor(boolean illegalOpenDoor) {
        this.illegalOpenDoor = illegalOpenDoor;
    }

}
