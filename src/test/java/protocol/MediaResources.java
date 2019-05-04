package protocol;

public class MediaResources {
    // 逻辑通道号
    private int logicChannelNum;
    // 开始时间
    private String startTime;
    // 结束时间
    private String endTime;
    // 报警标志
    private long alarmFlag;
    // 音视频资源类型
    private int mediaResourcesType;
    // 码流类型
    private int bitStreamType;
    // 存储器类型
    private int storageTYpe;
    // 文件大小
    private int fileSize;

    @Override
    public String toString() {
        return "MediaResources{" +
                "logicChannelNum=" + logicChannelNum +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", alarmFlag=" + alarmFlag +
                ", mediaResourcesType=" + mediaResourcesType +
                ", bitStreamType=" + bitStreamType +
                ", storageTYpe=" + storageTYpe +
                ", fileSize=" + fileSize +
                '}';
    }

    public int getLogicChannelNum() {
        return logicChannelNum;
    }

    public void setLogicChannelNum(int logicChannelNum) {
        this.logicChannelNum = logicChannelNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(long alarmFlag) {
        this.alarmFlag = alarmFlag;
    }

    public int getMediaResourcesType() {
        return mediaResourcesType;
    }

    public void setMediaResourcesType(int mediaResourcesType) {
        this.mediaResourcesType = mediaResourcesType;
    }

    public int getBitStreamType() {
        return bitStreamType;
    }

    public void setBitStreamType(int bitStreamType) {
        this.bitStreamType = bitStreamType;
    }

    public int getStorageTYpe() {
        return storageTYpe;
    }

    public void setStorageTYpe(int storageTYpe) {
        this.storageTYpe = storageTYpe;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
