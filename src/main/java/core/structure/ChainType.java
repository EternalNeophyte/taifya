package core.structure;

import core.format.Regulars;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public enum ChainType implements Regulars {

    AXIOM(S_EXP, "Аксиома грамматики"),
    TERMINAL(LOWERCASE_AND_DIGITS, "Терминал"),
    NON_TERMINAL(UPPERCASE_WITHOUT_S, "Нетерминал"),
    EMPTY(EPSILON_EXP, "Пустая цепочка"),
    UNKNOWN(NO_SPACE, "Неопознанный элемент");

    private final String regex;
    private final String definition;
    private List<ChainType> variants;

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

    public ChainType or(ChainType other) {
        if(Objects.isNull(variants)) {
            variants = new LinkedList<>();
            variants.add(this);
        }
        variants.add(other);
        return this;
    }

    public Stream<ChainType> variants() {
        return Objects.requireNonNullElse(variants, List.of(this))
                      .stream();
    }

    public boolean anyAs(ChainType other) {
        return variants().anyMatch(t -> t.sameAs(other));
    }

    public boolean nothingAs(ChainType other) {
        return variants().noneMatch(t -> t.sameAs(other));
    }

    public boolean sameAs(ChainType other) {
        return Objects.equals(this.regex, other.regex)
                && Objects.equals(this.definition, other.definition);
    }

    @Override
    public String toString() {
        return definition;
    }
}
