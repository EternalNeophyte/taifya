package core.grammar;

import core.chain.Chain;
import core.format.Formatting;
import core.rule.Rule;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static core.chain.ChainType.*;
import static core.grammar.GrammarType.*;

public class Grammar implements Formatting {

    private GrammarType type;
    private List<Rule> rules;
    private List<String> terminals;
    private List<String> nonterminals;

    private Grammar() {
        rules = new LinkedList<>();
        terminals = new LinkedList<>();
        nonterminals = new LinkedList<>();
    }

    public static Grammar describe() {
        return new Grammar();
    }

    public Grammar rule(String ruleDefinition) {
        rules.add(Rule.from(ruleDefinition));
        return this;
    }

    public Grammar formulate() {
        classify();
        terminals = chainsSelectedBy(c -> c.is(TERMINAL));
        nonterminals = chainsSelectedBy(c -> c.is(NON_TERMINAL));
        return this;
    }

    public void classify() {
        if(any(Rule::hasEmptyChain)) {
            type = TYPE_0;
        }
        else if(all(Rule::isAlignedLeft)) {
            type = REGULAR_ALIGNED_LEFT;
        }
        else if(all(Rule::isAlignedRight)) {
            type = REGULAR_ALIGNED_RIGHT;
        }
        else if(all(Rule::isAligned)) {
            type = REGULAR;
        }
        else if(all(Rule::leftSideHasOneChain)) {
            type = CONTEXT_FREE;
        }
        else {
            type = CONTEXT_DEPENDANT;
        }
    }

    private List<String> chainsSelectedBy(Predicate<Chain> predicate) {
        return rules.stream()
                .flatMap(Rule::mergedChains)
                .filter(predicate)
                .map(Chain::getLiteral)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    public int numberOfRules() {
        return rules.size();
    }

    public Rule lastRule() {
        return rules.get(rules.size() - 1);
    }

    public boolean any(Predicate<Rule> predicate) {
        return rules.stream()
                    .anyMatch(predicate);
    }

    public boolean all(Predicate<Rule> predicate) {
        return rules.stream()
                    .allMatch(predicate);
    }

    public void print() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Грамматика вида G = { {");
        terminals.forEach(literal -> perComma(sb, literal));
        replaceLast(sb, COMMA, FLOATING_BRACKET);
        sb.append(", {");
        nonterminals.forEach(literal -> perComma(sb, literal));
        replaceLast(sb, COMMA, FLOATING_BRACKET);
        sb.append(", P, S } c правилами P = { ");
        rules.forEach(r -> sb.append("\t").append(r));
        sb.append("\n}\nимеет тип:\n").append(type.toString()).append("\n");
        return sb.toString();
    }
}
