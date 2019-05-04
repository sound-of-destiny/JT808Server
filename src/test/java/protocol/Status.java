package protocol;

public class Status {
    // 0:ACC 关;1: ACC 开
    private boolean ACC;
    // 0:未定位;1:定位
    private boolean isLocation;
    // 0:北纬;1:南纬
    private boolean latitude;
    // 0:东经;1:西经
    private boolean longitude;
    // 0:运营状态;1:停运状态
    private boolean isRunning;
    // 0:经纬度未经保密插件加密;1:经纬度已经保密插件加密
    private boolean encrypt;
    // 00:空车;01:半载;10:保留;11:满载 (可用于客车的空、重车及货车的空载、满载状态表示,人工输入或传感器获取)
    private int goodsStatus;
    // 0:车辆油路正常;1:车辆油路断开
    private boolean vehicleOil;
    // 0:车辆电路正常;1:车辆电路断开
    private boolean vehicleCircut;
    // 0:车门解锁;1:车门加锁
    private boolean doorLock;
    // 0:门关; 1:门开(前门)
    private boolean frontDoorOpen;
    // 0:门关; 1:门开(中门)
    private boolean middleDoorOpen;
    // 0:门关; 1:门开(后门)
    private boolean endDoorOpen;
    // 0:门关; 1:门开(驾驶席门)
    private boolean driverDoorOpen;
    // 0:门关; 1:门开(自定义)
    private boolean otherDoorOpen;
    // 0:未使用 GPS 卫星进行定位;1:使用 GPS 卫星进行定位
    private boolean GPS;
    // 0:未使用北斗卫星进行定位;1:使用北斗卫星进行定位
    private boolean beidou;
    // 0:未使用 GLONASS 卫星进行定位;1:使用 GLONASS 卫星进行定位
    private boolean GLONASS;
    // 0:未使用 Galileo 卫星进行定位;1:使用 Galileo 卫星进行定位
    private boolean Galileo;

    @Override
    public String toString() {
        return "{ \"ACC\" : " + ACC + ", \"isLocation\" : " + isLocation + ", \"latitude\" : "
                + latitude + ", \"longitude\" : " + longitude + ", \"isRunning\" : "
                + isRunning + ", \"encrypt\" : " + encrypt + ", \"goodsStatus\" : "
                + goodsStatus + ", \"vehicleOil\" : " + vehicleOil + ", \"vehicleCircut\" : "
                + vehicleCircut + ", \"doorLock\" : " + doorLock + ", \"frontDoorOpen\" : "
                + frontDoorOpen + ", \"middleDoorOpen\" : " + middleDoorOpen + ", \"endDoorOpen\" : "
                + endDoorOpen + ", \"driverDoorOpen\" : " + driverDoorOpen + ", \"otherDoorOpen\" : "
                + otherDoorOpen + ", \"GPS\" : " + GPS + ", \"beidou\" : " + beidou + ", \"GLONASS\" : "
                + GLONASS + ", \"Galileo\" : " + Galileo + "  }";
    }

    public boolean isACC() {
        return ACC;
    }

    public void setACC(boolean ACC) {
        this.ACC = ACC;
    }

    public boolean isLocation() {
        return isLocation;
    }

    public void setLocation(boolean location) {
        isLocation = location;
    }

    public boolean isLatitude() {
        return latitude;
    }

    public void setLatitude(boolean latitude) {
        this.latitude = latitude;
    }

    public boolean isLongitude() {
        return longitude;
    }

    public void setLongitude(boolean longitude) {
        this.longitude = longitude;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public boolean isVehicleOil() {
        return vehicleOil;
    }

    public void setVehicleOil(boolean vehicleOil) {
        this.vehicleOil = vehicleOil;
    }

    public boolean isVehicleCircut() {
        return vehicleCircut;
    }

    public void setVehicleCircut(boolean vehicleCircut) {
        this.vehicleCircut = vehicleCircut;
    }

    public boolean isDoorLock() {
        return doorLock;
    }

    public void setDoorLock(boolean doorLock) {
        this.doorLock = doorLock;
    }

    public boolean isFrontDoorOpen() {
        return frontDoorOpen;
    }

    public void setFrontDoorOpen(boolean frontDoorOpen) {
        this.frontDoorOpen = frontDoorOpen;
    }

    public boolean isMiddleDoorOpen() {
        return middleDoorOpen;
    }

    public void setMiddleDoorOpen(boolean middleDoorOpen) {
        this.middleDoorOpen = middleDoorOpen;
    }

    public boolean isEndDoorOpen() {
        return endDoorOpen;
    }

    public void setEndDoorOpen(boolean endDoorOpen) {
        this.endDoorOpen = endDoorOpen;
    }

    public boolean isDriverDoorOpen() {
        return driverDoorOpen;
    }

    public void setDriverDoorOpen(boolean driverDoorOpen) {
        this.driverDoorOpen = driverDoorOpen;
    }

    public boolean isOtherDoorOpen() {
        return otherDoorOpen;
    }

    public void setOtherDoorOpen(boolean otherDoorOpen) {
        this.otherDoorOpen = otherDoorOpen;
    }

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
