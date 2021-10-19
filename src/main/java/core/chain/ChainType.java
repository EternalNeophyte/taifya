package core.chain;

import core.format.Regulars;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public enum ChainType implements Regulars {

    AXIOM(S, "Аксиома грамматики"),
    TERMINAL(LOWERCASE_AND_DIGITS, "Терминал"),
    NON_TERMINAL(UPPERCASE_WITHOUT_S, "Нетерминал"),
    EMPTY(EPSILONS, "Пустая цепочка"),
    UNKNOWN(NO_SPACE, "Неопознанный элемент");

    private final String regex;
    private final String definition;
    private List<ChainType> alternatives;

    ChainType(String regex, String definition) {
        this.regex = regex;
        this.definition = definition;
        this.alternatives = new LinkedList<>();
    }

    public static ChainType from(String input) {
        return Arrays.stream(values())
                .filter(t -> Pattern.compile(t.regex)
                                    .matcher(input)
                                    .find())
                .findFirst()
                .orElse(UNKNOWN);
    }

    public static ChainType[] of(ChainType... types) {
        return types;
    }

    public ChainType or(ChainType other) {
        alternatives.add(other);
        return this;
    }

    public boolean match(ChainType... types) {
// ToDo
        return false;
    }

    @Override
    public String toString() {
        return definition;
    }
}
