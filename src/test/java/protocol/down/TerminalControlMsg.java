package protocol.down;

public class TerminalControlMsg {
    // 命令字
    private int command;
    // 命令参数
    private String commandParam;

    @Override
    public String toString() {
        return "TerminalControlMsg{" +
                "command=" + command +
                ", commandParam='" + commandParam + '\'' +
                '}';
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public String getCommandParam() {
        return commandParam;
    }

    public void setCommandParam(String commandParam) {
        this.commandParam = commandParam;
    }
}
