package cn.edu.sdu.JT808Server.protocol;

public class CommandParam {
    // 连接控制 0:切换到指定监管平台服务器,连接到该服务器后即进入应急状态,
    //　此状态下仅有下发控制指令的监管平台可发送包括短信在内的控制指令;1:切换回原缺省监控平台服务器,并恢复正常状态。
    private int linkControl;
    // 拨号点名称
    private String APNname;
    // 拨号用户名
    private String APNusername;
    // 拨号密码
    private String APNpassword;
    // 地址 服务器地址,IP 或域名
    private String serverHost;
    // TCP 端口
    private int TCPport;
    // UDP 端口
    private int UDPport;
    // 制造商 ID
    private String manufacturerId;
    // 监管平台鉴权码 监管平台下发的鉴权码,仅用于终端连接到监管平台之后的鉴权,终端连接回原监控平台还用原鉴权码
    private String platformAuthenticationCode;
    // 终端硬件版本号
    private String terminalHardwareVersion;
    // 终端固件版本号
    private String terminalFirmwareVersion;
    // URL 地址
    private String URLaddress;
    // 连接到指定服务器时限单位:分(min),值非 0 表示在终端接收到升级或连接指定服务器指令
    //后的有效期截止前,终端应连回原地址。若值为 0,则表示一直连接指器时限定服务器
    private int serverTimeout;


    @Override
    public String toString() {
        return "CommandParam{" +
                "linkControl=" + linkControl +
                ", APNname='" + APNname + '\'' +
                ", APNusername='" + APNusername + '\'' +
                ", APNpassword='" + APNpassword + '\'' +
                ", serverHost='" + serverHost + '\'' +
                ", TCPport=" + TCPport +
                ", UDPport=" + UDPport +
                ", manufacturerId='" + manufacturerId + '\'' +
                ", platformAuthenticationCode='" + platformAuthenticationCode + '\'' +
                ", terminalHardwareVersion='" + terminalHardwareVersion + '\'' +
                ", terminalFirmwareVersion='" + terminalFirmwareVersion + '\'' +
                ", URLaddress='" + URLaddress + '\'' +
                ", serverTimeout=" + serverTimeout +
                '}';
    }

    public int getLinkControl() {
        return linkControl;
    }

    public void setLinkControl(int linkControl) {
        this.linkControl = linkControl;
    }

    public String getAPNname() {
        return APNname;
    }

    public void setAPNname(String APNname) {
        this.APNname = APNname;
    }

    public String getAPNusername() {
        return APNusername;
    }

    public void setAPNusername(String APNusername) {
        this.APNusername = APNusername;
    }

    public String getAPNpassword() {
        return APNpassword;
    }

    public void setAPNpassword(String APNpassword) {
        this.APNpassword = APNpassword;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getTCPport() {
        return TCPport;
    }

    public void setTCPport(int TCPport) {
        this.TCPport = TCPport;
    }

    public int getUDPport() {
        return UDPport;
    }

    public void setUDPport(int UDPport) {
        this.UDPport = UDPport;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getPlatformAuthenticationCode() {
        return platformAuthenticationCode;
    }

    public void setPlatformAuthenticationCode(String platformAuthenticationCode) {
        this.platformAuthenticationCode = platformAuthenticationCode;
    }

    public String getTerminalHardwareVersion() {
        return terminalHardwareVersion;
    }

    public void setTerminalHardwareVersion(String terminalHardwareVersion) {
        this.terminalHardwareVersion = terminalHardwareVersion;
    }

    public String getTerminalFirmwareVersion() {
        return terminalFirmwareVersion;
    }

    public void setTerminalFirmwareVersion(String terminalFirmwareVersion) {
        this.terminalFirmwareVersion = terminalFirmwareVersion;
    }

    public String getURLaddress() {
        return URLaddress;
    }

    public void setURLaddress(String URLaddress) {
        this.URLaddress = URLaddress;
    }

    public int getServerTimeout() {
        return serverTimeout;
    }

    public void setServerTimeout(int serverTimeout) {
        this.serverTimeout = serverTimeout;
    }
}
