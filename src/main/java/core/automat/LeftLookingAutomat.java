package core.automat;

import core.grammar.Grammar;

import java.util.concurrent.atomic.AtomicReference;

public class LeftLookingAutomat extends AbstractAutomat<LeftLookingAutomat> {

    LeftLookingAutomat() {
        super();
    }

    @Override
    public LeftLookingAutomat execute() {
        boolean matchFound;
        AtomicReference<String> next = new AtomicReference<>(/*input.peek()*/);
        while(!input.empty()) {
            next.set(input.pop());
            matchFound = grammar.lookupLeft(next.get(),
                                            Grammar.INITIAL_CHAIN,
                                            cs -> {
                                                magazine = cs;
                                                track(counter.incrementAndGet(), next.get());
                                            });
            //ToDo нетерминалы проскакивают через соответствие - фикс
            //ToDo Некорректный возврат boolean из lookupLeft - фикс
            //ToDo Бесконечная рекурсия без isNotRecursive, а с этим методом проходит только 1 правило
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