package core.rule;

import core.chain.Chain;
import core.chain.ChainType;
import core.format.Formatting;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static core.chain.ChainType.*;

public class Rule implements Formatting {

    private List<Chain> left;
    private List<List<Chain>> right;

    private Rule() {
        left = new LinkedList<>();
        right = new LinkedList<>();
    }

    public static Rule from(String input) {
        Rule that = new Rule();
        that.initChainsFrom(input);
        return that;
    }

    private void initChainsFrom(String input) {
        List<List<String>> inputHolder =
                Arrays.stream(input.trim().split(RULE_SPLITTER))
                    .map(part -> Chain.is(part, EMPTY)
                                    ? List.of(EPSILON)
                                    : Arrays.asList(part.split("")))
                    .collect(Collectors.toUnmodifiableList());
        inputHolder.get(0)
                    .forEach(s -> left.add(Chain.from(s)));
        inputHolder.stream()
                    .skip(1)
                    .forEach(list -> {
                                    List<Chain> chains = new LinkedList<>();
                                    list.forEach(s -> chains.add(Chain.from(s)));
                                    right.add(chains);
                    });
    }

    public Stream<Chain> mergedChains() {
        List<Chain> merged = new LinkedList<>(left);
        right.forEach(merged::addAll);
        return merged.stream();
    }

    public boolean hasEmptyChain() {
        return mergedChains().anyMatch(c -> c.is(EMPTY));
    }

    public boolean leftSideHasOneLiteral() {
        return left.size() == 1 && left.get(0).isAnyOf(AXIOM, NON_TERMINAL);
    }

    private boolean isAlignedBasically(ChainType[] typesOfFirst, ChainType[] typesOfSecond) {
        List<Chain> rightOne = right.get(0);
        List<Chain> rightTwo = right.get(1);
        return left.size() == 1
                && right.size() == 2
                && left.get(0).isAnyOf(AXIOM, NON_TERMINAL)
                && rightOne.size() == 2
                && rightOne.get(0).isAnyOf(typesOfFirst)
                && rightOne.get(1).isAnyOf(typesOfSecond)
                && rightTwo.size() == 1
                && rightTwo.get(0).is(TERMINAL);
    }

    public boolean isAlignedLeft() {
        return isAlignedBasically(ChainType.of(AXIOM, NON_TERMINAL),
                                  ChainType.of(TERMINAL));
    }

    public boolean isAlignedRight() {
        return isAlignedBasically(ChainType.of(TERMINAL),
                                  ChainType.of(AXIOM, NON_TERMINAL));
    }

    public boolean isAligned() {
        return isAlignedLeft() || isAlignedRight();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\nПравило");
        sb.append("\n").append("Левая часть = [");
        left.forEach(c -> perComma(sb, c.toString()));
        replaceLast(sb, COMMA, SQUARE_BRACKET);
        sb.append("\n").append("Правая часть = [");
        right.forEach(list -> {
                    list.forEach(c -> perComma(sb, c.toString()));
                    replaceLast(sb, COMMA, DELIMITER);
                });
        replaceLast(sb, DELIMITER, SQUARE_BRACKET);
        return sb.toString();
    }
}
