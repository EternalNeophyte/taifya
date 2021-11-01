package core.structure;

import core.format.Regulars;

import java.util.Arrays;

public class Chain implements Regulars {

    private final ChainType type;
    private final String literal;

    private Chain(String input) {
        literal = input;
        type = ChainType.from(input);
    }

    public static Chain empty() {
        return new Chain(EPSILON);
    }

    public static Chain axiom() {
        return new Chain(S);
    }

    public static Chain from(String input) {
        return new Chain(input);
    }

    public ChainType getType() {
        return type;
    }

    public String getLiteral() {
        return literal;
    }

    public static boolean is(String input, ChainType type) {
        return ChainType.from(input).equals(type);
    }

    public boolean is(ChainType type) {
        return this.type.equals(type);
    }

    public boolean isNot(ChainType type) {
        return !is(type);
    }

    public boolean isAnyOf(ChainType... types) {
        return Arrays.stream(types)
                     .anyMatch(this::is);
    }

    public boolean isNotAnyOf(ChainType... types) {
        return !isAnyOf(types);
    }

    public boolean literalEquals(Chain other) {
        return literal.equals(other.literal);
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
            return literalEquals(ch) && this.type.sameAs(ch.type);
        }
    }

    @Override
    public String toString() {
        return type.toString() + " " + literal;
    }
}
