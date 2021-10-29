package core.automat;

import core.chain.Chain;
import core.format.Regulars;
import core.grammar.Grammar;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractAutomat<T extends AbstractAutomat<T>> implements Regulars {

    Grammar grammar;
    Chain magazine;
    AtomicInteger counter;
    State state;
    Stack<String> input;
    List<AutomatConfiguration> trace;

    public AbstractAutomat() {
        counter = new AtomicInteger(0);
        state = State.INITIAL;
        input = new Stack<>();
        trace = new LinkedList<>();
    }

    public T grammar(Grammar grammar) {
        this.grammar = grammar;
        return (T) this;
    }

    public T input(String line) {
        String[] symbols = new StringBuilder(line)
                                .reverse()
                                .toString()
                                .split("");
        Arrays.stream(symbols).forEach(input::push);
        return (T) this;
    }

    void track(int number, String next, String magazineContent) {
        trace.add(new AutomatConfiguration(number,
                                           next,
                                           input,
                                           magazineContent,
                                           state.getLiteral()));
    }

    void track(int number, String next) {
        track(number, next, magazine.getLiteral());
    }

    public void printTrace() {
        System.out.println("Список конфигураций автомата");
        trace.forEach(System.out::println);
    }

    public abstract T execute();

    public abstract void terminate(State state);
}