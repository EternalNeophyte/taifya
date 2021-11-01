package core.automat;

import core.format.Regulars;
import core.grammar.Grammar;
import core.structure.ChainSequence;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static core.grammar.GrammarType.*;

public abstract class AbstractAutomat<T extends AbstractAutomat<T>> implements Regulars {

    Grammar grammar;
    ChainSequence magazine;
    AtomicInteger counter;
    State state;
    Stack<String> input;
    List<AutomatConfiguration> trace;
    boolean abortIncorrectInput;

    AbstractAutomat() {
        counter = new AtomicInteger(0);
        state = State.INITIAL;
        input = new Stack<>();
        trace = new LinkedList<>();
        abortIncorrectInput = false;
    }

    public T grammar(Grammar grammar) {
        this.grammar = grammar;
        if(grammar.classifiedAs(TYPE_0, CONTEXT_DEPENDANT)) {
            throw new InvalidGrammarException("Автомат не принимает грамматику с типом ниже КС (тип 2)");
        }
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

    public T abortIfInputIsIncorrect() {
        abortIncorrectInput = true;
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
        track(number, next, magazine.toString());
    }

    public void printTrace() {
        System.out.printf("[ %5s ][ %4s ][ %4s ][ %29s ]%n", "№", "Сост", "М", "Входная строка");
        trace.forEach(System.out::println);
    }

    public abstract T execute();
}
