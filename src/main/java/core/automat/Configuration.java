package core.automat;

public class Configuration {

    private final int number;
    private final String recordedState;
    private final String inputString;
    private final String magazine;

    public Configuration(int number, String recordedState, String inputString, String magazine) {
        this.number = number;
        this.recordedState = recordedState;
        this.inputString = inputString;
        this.magazine = magazine;
    }

    public int getNumber() {
        return number;
    }

    public String getRecordedState() {
        return recordedState;
    }

    public String getInputString() {
        return inputString;
    }

    public String getMagazine() {
        return magazine;
    }
}
