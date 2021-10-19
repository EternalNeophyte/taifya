package core.rule;

import core.chain.Chain;
import core.chain.ChainSequence;
import core.chain.ChainType;
import core.format.Formatting;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static core.chain.ChainType.*;

public class Rule implements Formatting {

    private ChainSequence left;
    private List<ChainSequence> right;

    private Rule() {
        left = ChainSequence.create();
        right = new LinkedList<>();
    }

    public static Rule from(String input) {
        Rule that = new Rule();
        that.initChainsFrom(input);
        return that;
    }

    private void initChainsFrom(String input) {
        List<List<String>> inputHolder = Arrays.stream(input.trim().split(RULE_SPLITTER))
                    .map(part -> Chain.is(part, EMPTY)
                                    ? List.of(EPSILON)
                                    : Arrays.asList(part.split("")))
                    .collect(Collectors.toUnmodifiableList());
        inputHolder.get(0)
                    .forEach(s -> left.chain(s));
        inputHolder.stream()
                    .skip(1)
                    .forEach(list -> {
                                    ChainSequence sequence = ChainSequence.create();
                                    list.forEach(sequence::chain);
                                    right.add(sequence);
                    });
    }

    public Stream<Chain> rightChains() {
        return right.stream()
                    .flatMap(ChainSequence::chains);
    }

    public Stream<Chain> mergedChains() {
        return Stream.concat(left.chains(), rightChains());
    }

    public boolean hasEmptyChain() {
        return mergedChains().anyMatch(c -> c.is(EMPTY));
    }

    public boolean leftSideHasOneLiteral() {
        return left.hasSize(1) && left.at(0).isAnyOf(AXIOM, NON_TERMINAL);
    }

    private boolean isAlignedBasically(ChainType[] typesOfFirst, ChainType[] typesOfSecond) {
        ChainSequence rightOne = right.get(0);
        ChainSequence rightTwo = right.get(1);
        return left.hasSize(1)
                && left.at(0).isAnyOf(AXIOM, NON_TERMINAL)
                && right.size() == 2
                && rightOne.hasSize(2)
                && rightOne.at(0).isAnyOf(typesOfFirst)
                && rightOne.at(1).isAnyOf(typesOfSecond)
                && rightTwo.hasSize(1)
                && rightTwo.at(0).is(TERMINAL);
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
        left.chains().forEach(c -> perComma(sb, c.toString()));
        replaceLast(sb, COMMA, SQUARE_BRACKET);
        sb.append("\n").append("Правая часть = [");
        right.forEach(sequence -> {
                    sequence.chains().forEach(c -> perComma(sb, c.toString()));
                    replaceLast(sb, COMMA, DELIMITER);
                });
        replaceLast(sb, DELIMITER, SQUARE_BRACKET);
        return sb.toString();
    }
}
