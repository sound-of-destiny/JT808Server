package protocol.up;

public class TerminalExtraLocationMsg {

    // 里程
    private int mileage;
    // 油量
    private int oilQuantity;
    // 行驶记录功能获取的速度
    private int carSpeed;
    // 需要人工确认报警事件的 ID
    private int warningId;

    private SpeedingExtraData speedingExtraData;
    private LocationExtraData locationExtraData;
    private LocationInfoExtraData locationInfoExtraData;
    private ExtraCarState extraCarState;
    private IOState ioState;

    // 模拟量,bit0-15,AD0;bit16-31,AD1
    private int simulation;
    // 无线通信网络信号强度
    private int wirelessIntensity;
    // GNSS 定位卫星数
    private int satellitesNum;

    @Override
    public String toString() {
        return "{ \"mileage\" : " + mileage + ", \"oilQuantity\" : " + oilQuantity
                + ", \"carSpeed\" : " + carSpeed + ", \"warningId\" : " + warningId + ", \"speedingExtraData\" : "
                + speedingExtraData + ", \"locationExtraData\" : " + locationExtraData + ", \"locationInfoExtraData\" : "
                + locationInfoExtraData + ", \"extraCarState\" : " + extraCarState + ", \"ioState\" : " + ioState
                + ", \"simulation\" : " + simulation + ", \"wirelessIntensity\" : " + wirelessIntensity
                + ", \"satellitesNum\" : " + satellitesNum + " }";
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getOilQuantity() {
        return oilQuantity;
    }

    public void setOilQuantity(int oilQuantity) {
        this.oilQuantity = oilQuantity;
    }

    public int getCarSpeed() {
        return carSpeed;
    }

    public void setCarSpeed(int carSpeed) {
        this.carSpeed = carSpeed;
    }

    public int getWarningId() {
        return warningId;
    }

    public void setWarningId(int warningId) {
        this.warningId = warningId;
    }

    public SpeedingExtraData getSpeedingExtraData() {
        return speedingExtraData;
    }

    public void setSpeedingExtraData(SpeedingExtraData speedingExtraData) {
        this.speedingExtraData = speedingExtraData;
    }

    public LocationExtraData getLocationExtraData() {
        return locationExtraData;
    }

    public void setLocationExtraData(LocationExtraData locationExtraData) {
        this.locationExtraData = locationExtraData;
    }

    public LocationInfoExtraData getLocationInfoExtraData() {
        return locationInfoExtraData;
    }

    public void setLocationInfoExtraData(LocationInfoExtraData locationInfoExtraData) {
        this.locationInfoExtraData = locationInfoExtraData;
    }

    public ExtraCarState getExtraCarState() {
        return extraCarState;
    }

    public void setExtraCarState(ExtraCarState extraCarState) {
        this.extraCarState = extraCarState;
    }

    public IOState getIoState() {
        return ioState;
    }

    public void setIoState(IOState ioState) {
        this.ioState = ioState;
    }

    public int getSimulation() {
        return simulation;
    }

    public void setSimulation(int simulation) {
        this.simulation = simulation;
    }

    public int getWirelessIntensity() {
        return wirelessIntensity;
    }

    public void setWirelessIntensity(int wirelessIntensity) {
        this.wirelessIntensity = wirelessIntensity;
    }

    public int getSatellitesNum() {
        return satellitesNum;
    }

    public void setSatellitesNum(int satellitesNum) {
        this.satellitesNum = satellitesNum;
    }

    // 超速报警附加信息消息体数据格式
    public static class SpeedingExtraData {
        // 位置类型
        private int locationType;
        // 区域或路段 ID
        private String locationId;

        public int getLocationType() {
            return locationType;
        }

        public void setLocationType(int locationType) {
            this.locationType = locationType;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        @Override
        public String toString() {
            return "{ \"locationType=\" : " + locationType + ", \"locationId=\" : " + locationId + " }";
        }
    }

    // 进出区域/路线报警附加信息消息体数据格式
    public static class LocationExtraData {
        // 位置类型
        private int locationType;
        // 区域或线路 ID
        private String locationId;
        // 方向
        private int direction;

        public int getLocationType() {
            return locationType;
        }

        public void setLocationType(int locationType) {
            this.locationType = locationType;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "{ \"locationType=\" : " + locationType + ", \"locationId=\" : " + locationId + ", \"direction=\" : " + direction+ " }";
        }
    }

    // 路线行驶时间不足/过长报警附加信息消息体数据格式
    public static class LocationInfoExtraData {
        // 路段 ID
        private String roadId;
        // 路段行驶时间
        private int runTime;
        // 结果
        private int result;

        public String getRoadId() {
            return roadId;
        }

        public void setRoadId(String roadId) {
            this.roadId = roadId;
        }

        public int getRunTime() {
            return runTime;
        }

        public void setRunTime(int runTime) {
            this.runTime = runTime;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "{ \"roadId=\" : " + roadId + ", \"runTime=\" : " + runTime + ", \"result=\" : "
                    + result + " }";
        }
    }

    // 扩展车辆信号状态位
    public static class ExtraCarState {
        // 1:近光灯信号
        private boolean lowLightSignal;
        // 1:远光灯信号
        private boolean farLightSignal;
        //　1:右转向灯信号
        private boolean rightLightSignal;
        //　1:左转向灯信号
        private boolean leftLightSignal;
        //　1:制动信号
        private boolean brakingSignal;
        //　1:倒档信号
        private boolean reverseSignal;
        //　1:雾灯信号
        private boolean fogLampSignal;
        //　1:示廓灯
        private boolean outlineLamp;
        //　1:喇叭信号
        private boolean hornSignal;
        //　1:空调状态
        private boolean airCondition;
        //　1:空挡信号
        private boolean neutralSignal;
        //　1:缓速器工作
        private boolean retarderOperation;
        //　1:ABS 工作
        private boolean ABSOperation;
        //　1:加热器工作
        private boolean heaterOperation;
        //　1:离合器状态
        private boolean clutchState;

        @Override
        public String toString() {
            return "{ \"owLightSignal\" : " + lowLightSignal + ", \"farLightSignal\" : " + farLightSignal + ", \"rightLightSignal\" : "
                    + rightLightSignal + ", \"leftLightSignal\" : " + leftLightSignal + ", \"brakingSignal\" : " + brakingSignal
                    + ", \"reverseSignal\" : " + reverseSignal + ", \"fogLampSignal\" : " + fogLampSignal + ", \"outlineLamp\" : " +
                    outlineLamp + ", \"hornSignal\" : " + hornSignal + ", \"airCondition\" : " + airCondition
                    + ", \"neutralSignal\" : " + neutralSignal + ", \"retarderOperation\" : " + retarderOperation + ", \"ABSOperation\" : "
                    + ABSOperation + ", \"heaterOperation\" : " + heaterOperation + ", \"clutchState\" : " + clutchState + " }";
        }

        public boolean isLowLightSignal() {
            return lowLightSignal;
        }

        public void setLowLightSignal(boolean lowLightSignal) {
            this.lowLightSignal = lowLightSignal;
        }

        public boolean isFarLightSignal() {
            return farLightSignal;
        }

        public void setFarLightSignal(boolean farLightSignal) {
            this.farLightSignal = farLightSignal;
        }

        public boolean isRightLightSignal() {
            return rightLightSignal;
        }

        public void setRightLightSignal(boolean rightLightSignal) {
            this.rightLightSignal = rightLightSignal;
        }

        public boolean isLeftLightSignal() {
            return leftLightSignal;
        }

        public void setLeftLightSignal(boolean leftLightSignal) {
            this.leftLightSignal = leftLightSignal;
        }

        public boolean isBrakingSignal() {
            return brakingSignal;
        }

        public void setBrakingSignal(boolean brakingSignal) {
            this.brakingSignal = brakingSignal;
        }

        public boolean isReverseSignal() {
            return reverseSignal;
        }

        public void setReverseSignal(boolean reverseSignal) {
            this.reverseSignal = reverseSignal;
        }

        public boolean isFogLampSignal() {
            return fogLampSignal;
        }

        public void setFogLampSignal(boolean fogLampSignal) {
            this.fogLampSignal = fogLampSignal;
        }

        public boolean isOutlineLamp() {
            return outlineLamp;
        }

        public void setOutlineLamp(boolean outlineLamp) {
            this.outlineLamp = outlineLamp;
        }

        public boolean isHornSignal() {
            return hornSignal;
        }

        public void setHornSignal(boolean hornSignal) {
            this.hornSignal = hornSignal;
        }

        public boolean isAirCondition() {
            return airCondition;
        }

        public void setAirCondition(boolean airCondition) {
            this.airCondition = airCondition;
        }

        public boolean isNeutralSignal() {
            return neutralSignal;
        }

        public void setNeutralSignal(boolean neutralSignal) {
            this.neutralSignal = neutralSignal;
        }

        public boolean isRetarderOperation() {
            return retarderOperation;
        }

        public void setRetarderOperation(boolean retarderOperation) {
            this.retarderOperation = retarderOperation;
        }

        public boolean isABSOperation() {
            return ABSOperation;
        }

        public void setABSOperation(boolean ABSOperation) {
            this.ABSOperation = ABSOperation;
        }

        public boolean isHeaterOperation() {
            return heaterOperation;
        }

        public void setHeaterOperation(boolean heaterOperation) {
            this.heaterOperation = heaterOperation;
        }

        public boolean isClutchState() {
            return clutchState;
        }

        public void setClutchState(boolean clutchState) {
            this.clutchState = clutchState;
        }
    }

    public static class IOState {
        // 1:深度休眠状态
        private boolean deepDormancy;
        // 1:休眠状态
        private boolean dormancy;

        public boolean isDeepDormancy() {
            return deepDormancy;
        }

        public void setDeepDormancy(boolean deepDormancy) {
            this.deepDormancy = deepDormancy;
        }

        public boolean isDormancy() {
            return dormancy;
        }

        public void setDormancy(boolean dormancy) {
            this.dormancy = dormancy;
        }

        @Override
        public String toString() {
            return "{ \"deepDormancy=\" : " + deepDormancy + ", \"dormancy=\" : " + dormancy + " }";
        }
    }
}
