package core.automat;

import core.grammar.Grammar;
import core.structure.Chain;

import java.util.LinkedList;

public class LeftLookingAutomat extends AbstractAutomat<LeftLookingAutomat> {

    LeftLookingAutomat() {
        super();
    }

    @Override
    public void printTrace() {
        System.out.println("\nМП-автомат с левосторонним разбором");
        super.printTrace();
    }

    @Override
    public LeftLookingAutomat execute() {
        boolean matchFound;
        String copyOfNext;
        magazine = Grammar.INITIAL_SEQUENCE;
        track(counter.incrementAndGet(), "");
        while(!input.empty()) {
            String next = input.pop();
            copyOfNext = next;
            matchFound = grammar.lookupLeft(next,
                                            new LinkedList<>(),
                                            magazine,
                                            cs -> {
                                                magazine = cs;
                                                track(counter.incrementAndGet(), next);
                                            });
            magazine.removeFirstOrElse(Chain.empty());
            if(!matchFound && abortIncorrectInput) {
                printTrace();
                throw new InputAbortedException("Автомат не нашел соответствий для '" +
                                                copyOfNext +
                                                "' и преждевременно завершил работу");

            }
        }
        track(counter.incrementAndGet(), EPSILON, EPSILON);
        return this;
    }
}
