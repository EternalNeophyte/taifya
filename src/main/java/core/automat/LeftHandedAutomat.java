package core.automat;

import core.format.Regulars;
import core.grammar.Grammar;

import java.util.concurrent.atomic.AtomicReference;

public class LeftHandedAutomat extends AbstractAutomat<LeftHandedAutomat> {

    public LeftHandedAutomat() {
        super();
    }

    private void process(AtomicReference<String> next) {

    }

    @Override
    public LeftHandedAutomat execute() {
        boolean matchFound;
        AtomicReference<String> next = new AtomicReference<>(/*input.peek()*/);
        while(!input.empty()) {
            next.set(input.pop());
            matchFound = grammar.lookupLeft(next.get(),
                                            Grammar.INITIAL_SEQUENCE,
                                            cs -> {
                                                magazine = cs;
                                                track(counter.incrementAndGet(), next.get());
                                            });
            //ToDo нетерминалы проскакивают через соответствие - фикс
            //ToDo Некорректный возврат boolean из lookupLeft - фикс
            /*if(!matchFound) {
                printTrace();
                throw new InputAbortedException("Автомат не нашел соответствий для '" +
                        next.get() +
                        "' и преждевременно завершил работу");

            }*/
        }
        track(counter.incrementAndGet(), EPSILON, EPSILON);
        return this;
    }



    @Override
    public void terminate(State state) {

    }
}
