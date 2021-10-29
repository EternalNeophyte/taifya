package core.automat;

import java.util.Stack;

public class AutomatConfiguration {

    private final int number;
    private final String inputContent;
    private final String magazineContent;
    private final String recordedState;

    public AutomatConfiguration(int number, String next, Stack<String> stack,
                                String magazineContent, String recordedState) {
        this.number = number;
        this.inputContent = next +
                new StringBuilder(stack.toString()
                                       .replaceAll("\\Q[\\E|\\Q]\\E|,| ", "")).reverse();
        this.magazineContent = magazineContent;
        this.recordedState = recordedState;
    }

    public int getNumber() {
        return number;
    }

    public String getRecordedState() {
        return recordedState;
    }

    public String getInputContent() {
        return inputContent;
    }

    public String getMagazineContent() {
        return magazineContent;
    }

    @Override
    public String toString() {
        return String.format("| %5s | %5s | %5s | %30s", number, recordedState, magazineContent, inputContent);
    }
}
