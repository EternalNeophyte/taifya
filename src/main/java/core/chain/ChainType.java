package core.chain;

import core.format.Regulars;

import java.util.Arrays;
import java.util.regex.Pattern;

public enum ChainType implements Regulars {

    AXIOM(S, "Аксиома грамматики"),
    TERMINAL(LOWERCASE_AND_DIGITS, "Терминал"),
    NON_TERMINAL(UPPERCASE_WITHOUT_S, "Нетерминал"),
    EMPTY(EPSILONS, "Пустая цепочка"),
    UNKNOWN(NO_SPACE, "Неопознанный элемент");

    private final String regex;
    private final String definition;

    ChainType(String regex, String definition) {
        this.regex = regex;
        this.definition = definition;
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

    @Override
    public String toString() {
        return definition;
    }
}
