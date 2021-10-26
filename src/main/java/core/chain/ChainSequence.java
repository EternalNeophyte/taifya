package core.chain;

import util.ListSafeAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    public Stream<Chain> chains() {
        return chains.stream();
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

    public boolean startsWith(String literal) {
        return getAtIndexOrElse(chains, 0, Chain.empty())
                .getLiteral()
                .equals(literal);
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
}
