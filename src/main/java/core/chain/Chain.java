package core.chain;

import java.util.Arrays;

public class Chain {

    private ChainType type;
    private String literal;

    private Chain(String input) {
        literal = input;
        type = ChainType.from(input);
    }

    public static Chain from(String input) {
        return new Chain(input);
    }

    public static boolean is(String input, ChainType type) {
        return ChainType.from(input).equals(type);
    }

    public boolean is(ChainType type) {
        return this.type.equals(type);
    }

    public boolean not(ChainType type) {
        return !is(type);
    }

    public boolean isAnyOf(ChainType... types) {
        return Arrays.stream(types)
                     .anyMatch(this::is);
    }

    public boolean notAnyOf(ChainType... types) {
        return !isAnyOf(types);
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return type.toString() + " " + literal;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        else if(!(o instanceof Chain)) {
            return false;
        }
        else {
            Chain ch = (Chain) o;
            return this.literal.equals(ch.literal);
        }
    }
}
