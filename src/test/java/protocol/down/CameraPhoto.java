package protocol.down;

public class CameraPhoto {
    // 通道 ID
    private int channelId;
    // 拍摄命令 0 表示停止拍摄;0xFFFF 表示录像;其它表示拍照张数
    private int photoCommand;
    // 拍照间隔/录像时间 秒, 0 表示按最小间隔拍照或一直录像
    private int cameraTime;
    // 保存标志 1:保存; 0:实时上传
    private int saveFlag;
    // 分辨率 0x01:320*240;0x02:640*480;0x03:800*600;0x04:1024*768;0x05:176*144;[Qcif];0x06:352*288;[Cif];0x07:704*288;[HALF D1];0x08:704*576;[D1];
    private int resolving;
    // 图像/视频质量 1-10,1 代表质量损失最小,10 表示压缩比最大
    private int cameraQuality;
    // 亮度
    private int light;
    // 对比度
    private int contrast;
    // 饱和度
    private int saturation;
    // 色度
    private int color;

    @Override
    public String toString() {
        return "CameraPhoto{" +
                "channelId=" + channelId +
                ", photoCommand=" + photoCommand +
                ", cameraTime=" + cameraTime +
                ", saveFlag=" + saveFlag +
                ", resolving=" + resolving +
                ", cameraQuality=" + cameraQuality +
                ", light=" + light +
                ", contrast=" + contrast +
                ", saturation=" + saturation +
                ", color=" + color +
                '}';
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getPhotoCommand() {
        return photoCommand;
    }

    public void setPhotoCommand(int photoCommand) {
        this.photoCommand = photoCommand;
    }

    public int getCameraTime() {
        return cameraTime;
    }

    public void setCameraTime(int cameraTime) {
        this.cameraTime = cameraTime;
    }

    public int getSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(int saveFlag) {
        this.saveFlag = saveFlag;
    }

    public int getResolving() {
        return resolving;
    }

    public void setResolving(int resolving) {
        this.resolving = resolving;
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
}
