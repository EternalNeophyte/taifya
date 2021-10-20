package core.chain;

import util.ListSafeAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ChainSequence implements ListSafeAccessor {

    private List<Chain> chains;

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

    public boolean hasSize(int size) {
        return chains.size() == size;
    }

    public boolean containsInOrder(ChainType... types) {
        if(types.length != chains.size()) {
            return false;
        }
        for(int i = 0; i < types.length; i++) {
            if(types[i].nothingAs(chains.get(i).getType())) {
                return false;
            }
        }
        return true;
    }
}
