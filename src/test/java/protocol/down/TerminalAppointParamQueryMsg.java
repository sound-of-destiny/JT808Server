package protocol.down;

import java.util.ArrayList;

public class TerminalAppointParamQueryMsg {
    private int paramNumber;

    private ArrayList<Integer> paramIdList;

    @Override
    public String toString() {
        return "TerminalAppointParamQueryMsg{" +
                "paramNumber=" + paramNumber +
                ", paramIdList=" + paramIdList +
                '}';
    }

    public int getParamNumber() {
        return paramNumber;
    }

    public void setParamNumber(int paramNumber) {
        this.paramNumber = paramNumber;
    }

    public ArrayList<Integer> getParamIdList() {
        return paramIdList;
    }

    public void setParamIdList(ArrayList<Integer> paramIdList) {
        this.paramIdList = paramIdList;
    }
}
