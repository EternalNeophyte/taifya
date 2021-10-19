package core.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created on 19.10.2021 by
 *
 * @author alexandrov
 */
public class ChainSequence {

    private List<Chain> chains;

    private ChainSequence() {
        chains = new ArrayList<>();
    }

    public static ChainSequence create() {
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
        return chains.get(index);
    }

    public boolean hasSize(int size) {
        return chains.size() == size;
    }

    public boolean consistsAnyOf(ChainType[]... types) {
        if(types.length != chains.size()) {
            return false;
        }
        for(int i = 0; i < types.length; i++) {
            if(chains.get(i).notAnyOf(types[i])) {
                return false;
            }
        }
        return true;
    }
}
