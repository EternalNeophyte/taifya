package core.grammar;

import core.chain.Chain;
import core.chain.ChainSequence;
import core.format.Formatting;
import core.rule.Rule;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static core.chain.ChainType.NON_TERMINAL;
import static core.chain.ChainType.TERMINAL;
import static core.grammar.GrammarType.*;

public class Grammar implements Formatting {

    public final static Chain INITIAL_CHAIN = Chain.from("S");

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
        terminals = literalsSelectedBy(c -> c.is(TERMINAL));
        nonterminals = literalsSelectedBy(c -> c.is(NON_TERMINAL));
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

    private List<String> literalsSelectedBy(Predicate<Chain> predicate) {
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

    public Stream<Rule> rules() {
        return rules.stream();
    }

    public boolean any(Predicate<Rule> predicate) {
        return rules().anyMatch(predicate);
    }

    public boolean all(Predicate<Rule> predicate) {
        return rules().allMatch(predicate);
    }

    private List<Rule> findRulesBy(Predicate<Rule> predicate) {
        return rules()
                .filter(predicate)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Rule> findRulesWithLeft(Chain sequence) {
        return findRulesBy(rule -> rule.left().chains().anyMatch(cs -> cs.getLiteral().startsWith(sequence.getLiteral())));
    }

    private List<Rule> findRulesWithRight(Chain sequence) {
        return findRulesBy(rule -> rule.rightChains()
                                       .anyMatch(cs -> cs.getLiteral().startsWith(sequence.getLiteral())));
    }

    public boolean lookupLeft(String input, Chain sequence, Consumer<Chain> consumer) {
        if(sequence.equals(INITIAL_CHAIN)) {
            consumer.accept(sequence);
        }
        for(Rule rule : findRulesWithLeft(sequence)) {
            if(rule.isRecursive()) {
                break;
            }


            for(Chain ch : rule.rightChains().collect(Collectors.toList())) {
                consumer.accept(ch);
                if(input.startsWith(ch.getLiteral())) {

                    return true;

                }

                lookupLeft(input, ch, consumer);
            }


        }
        return false;
    }

    public void lookupRight(String input, List<Rule> rules, Consumer<Chain> consumer) {
        /*for(Rule rule : rules) {
            rule.rightaChins()
                .filter(cs -> cs.startsSameAs(input))
                .findFirst()
                .ifPresent(consumer);
            Chain left = rule.left();
            consumer.accept(left);
            if(left.equals(INITIAL_CHAIN)) {
                break;
            }
            lookupRight(input, findRulesWithRight(left), consumer);
        }*/
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
