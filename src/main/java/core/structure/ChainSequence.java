package core.structure;

import util.ListSafeAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static core.structure.ChainType.NON_TERMINAL;
import static core.structure.ChainType.TERMINAL;

public class ChainSequence implements ListSafeAccessor {

    private final List<Chain> chains;

    private ChainSequence() {
        chains = new ArrayList<>();
    }

    public static ChainSequence empty() {
        return new ChainSequence();
    }

    public ChainSequence chain(String input) {
        chains.add(Chain.from(input));
        return this;
    }

    public ChainSequence chaining(List<Chain> chain) {
        chains.addAll(chain);
        return this;
    }

    public static ChainSequence of(List<Chain> chain) {
        return empty().chaining(chain);
    }

    public List<Chain> getChains() {
        return chains;
    }

    public Stream<Chain> chains() {
        return chains.stream();
    }

    private Stream<Chain> chainsOfType(ChainType type) {
        return chains.stream().filter(ch -> ch.is(type));
    }

    public Stream<Chain> terminals() {
        return chainsOfType(TERMINAL);
    }

    public Stream<Chain> nonterminals() {
        return chainsOfType(NON_TERMINAL);
    }

    public void removeFirst() {
        if(size() > 0) {
            chains.remove(0);
        }
    }

    public void removeFirstOrElse(Chain other) {
        if(hasSize(0)) {
            chains.add(other);
        }
        chains.remove(0);
    }

    public Chain at(int index) {
        return getAtIndexOrElse(chains, index, Chain.empty());
    }

    public int size() {
        return chains.size();
    }

    public boolean hasSize(int size) {
        return chains.size() == size;
    }

    public boolean startsSameAs(String input) {
        String literal = at(0).getLiteral();
        return input.startsWith(literal);
    }

    public boolean startsSameAs(ChainSequence other) {
        return other.at(0).literalEquals(at(0));
    }

    public boolean containsSameAs(ChainSequence other) {
        return chains().anyMatch(ch -> other.chains().anyMatch(ch::literalEquals));
    }

    public boolean containsInOrder(ChainType... types) {
        int size = size();
        return types.length == size && IntStream.range(0, size)
                                                .allMatch(i -> types[i].anyAs(at(i).getType()));
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof ChainSequence)) {
            return false;
        }
        ChainSequence other = (ChainSequence) o;
        int size = this.size();
        return size == other.size()
                && IntStream.range(0, size)
                            .allMatch(i -> this.at(i).equals(other.at(i)));
    }

    @Override
    public String toString() {
        return chains()
                .map(Chain::getLiteral)
                .collect(Collectors.joining());
    }
}
