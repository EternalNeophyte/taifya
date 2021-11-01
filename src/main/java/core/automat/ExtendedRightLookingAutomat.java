package core.automat;

import core.structure.ChainSequence;

import java.util.LinkedList;

public class ExtendedRightLookingAutomat extends AbstractAutomat<ExtendedRightLookingAutomat> {

    String marquer;

    ExtendedRightLookingAutomat() {
        super();
    }

    public ExtendedRightLookingAutomat marquer(String marquer) {
        this.marquer = marquer;
        return this;
    }

    @Override
    void track(int number, String next) {
        super.track(number, next, marquer + magazine.toString());
    }

    @Override
    public void printTrace() {
        System.out.println("\nРасширенный МП-автомат с правосторонним разбором");
        super.printTrace();
    }

    @Override
    public ExtendedRightLookingAutomat execute() {
        boolean matchFound;
        String copyOfNext;
        magazine = ChainSequence.empty();
        track(counter.incrementAndGet(), "");
        while(!input.empty()) {
            String next = input.pop();
            copyOfNext = next;
            magazine.chain(next);
            matchFound = grammar.lookupRight(next,
                    new LinkedList<>(),
                    magazine,
                    cs -> {
                        magazine = cs;
                        track(counter.incrementAndGet(), next);
                    });
            magazine.removeFirst();
            if(!matchFound && abortIncorrectInput) {
                printTrace();
                throw new InputAbortedException("Автомат не нашел соответствий для '" +
                        copyOfNext +
                        "' и преждевременно завершил работу");

            }
        }
        state = state.turn();
        track(counter.incrementAndGet(), EPSILON, marquer);
        return this;
    }
}
