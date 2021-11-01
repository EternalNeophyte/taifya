package core.structure;

import core.format.Formatting;
import util.ListSafeAccessor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static core.structure.ChainType.*;

public class Rule implements Formatting, ListSafeAccessor {

    private ChainSequence left;
    private List<ChainSequence> right;

    private Rule() {
        left = ChainSequence.empty();
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
                                    ChainSequence sequence = ChainSequence.empty();
                                    list.forEach(sequence::chain);
                                    right.add(sequence);
                    });
    }

    public ChainSequence left() {
        return left;
    }

    public Stream<Chain> leftChains() {
        return left.chains();
    }

    public List<ChainSequence> right() {
        return right;
    }

    public ChainSequence right(int index) {
        return getAtIndexOrElse(right, index, ChainSequence.empty());
    }

    public Stream<ChainSequence> rightSequences() {
        return right.stream();
    }

    public Stream<Chain> rightChains() {
        return rightSequences().flatMap(ChainSequence::chains);
    }

    public Stream<Chain> mergedChains() {
        return Stream.concat(leftChains(), rightChains());
    }

    public boolean hasEmptyChain() {
        return mergedChains().anyMatch(c -> c.is(EMPTY));
    }

    public boolean leftSideHasOneChain() {
        return left.hasSize(1) && left.containsInOrder(AXIOM.or(NON_TERMINAL));
    }

    public boolean rightSideHasSize(int size) {
        return right.size() == size;
    }

    public boolean isAlignedLeft() {
        return leftSideHasOneChain()
                && rightSideHasSize(2)
                && right(0).containsInOrder(AXIOM.or(NON_TERMINAL), TERMINAL)
                && right(1).containsInOrder(TERMINAL);

    }

    public boolean isAlignedRight() {
        return leftSideHasOneChain()
                && rightSideHasSize(2)
                && right(0).containsInOrder(TERMINAL, AXIOM.or(NON_TERMINAL))
                && right(1).containsInOrder(TERMINAL);
    }

    public boolean isAligned() {
        return isAlignedLeft() || isAlignedRight();
    }

    public boolean isNotRecursive() {
        return rightChains().noneMatch(right -> leftChains().anyMatch(right::literalEquals));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n\tПравило {")
                                .append("\n\t\t").append("Левая часть = [");
        left.chains().forEach(c -> perComma(sb, c.toString()));
        replaceLast(sb, COMMA, SQUARE_BRACKET);
        sb.append("\n\t\t").append("Правая часть = [");
        right.forEach(sequence -> {
                    sequence.chains().forEach(c -> perComma(sb, c.toString()));
                    replaceLast(sb, COMMA, DELIMITER);
                });
        replaceLast(sb, DELIMITER, SQUARE_BRACKET);
        return sb.append("\n\t}").toString();
    }
}
