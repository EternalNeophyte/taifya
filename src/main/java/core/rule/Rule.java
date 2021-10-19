package core.rule;

import core.chain.Chain;
import core.chain.ChainSequence;
import core.format.Formatting;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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

    public ChainSequence right(int index) {
        return right.get(index);
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

    public boolean leftSideHasOneChain() {
        return left.containsInOrder(AXIOM.or(NON_TERMINAL));
    }

    public boolean isAlignedLeft() {
        return leftSideHasOneChain()
                && right(0).containsInOrder(AXIOM.or(NON_TERMINAL), TERMINAL)
                && right(1).containsInOrder(TERMINAL);

    }

    public boolean isAlignedRight() {
        return leftSideHasOneChain()
                && right(0).containsInOrder(TERMINAL)
                && right(1).containsInOrder(AXIOM.or(NON_TERMINAL), TERMINAL);
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
