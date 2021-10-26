package core.automat;

public enum State {

    INITIAL("q"),
    FINAL("r");

    State(String literal) {
        this.literal = literal;
    }

    private final String literal;

    public String getLiteral() {
        return literal;
    }

    public State turn() {
        return this == INITIAL
                ? FINAL
                : INITIAL;
    }
}
