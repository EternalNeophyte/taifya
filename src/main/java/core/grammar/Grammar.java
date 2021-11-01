package core.grammar;

import core.structure.Chain;
import core.format.Formatting;
import core.structure.ChainSequence;
import core.structure.Rule;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static core.structure.ChainType.NON_TERMINAL;
import static core.structure.ChainType.TERMINAL;
import static core.grammar.GrammarType.*;

public class Grammar implements Formatting {

    public final static ChainSequence INITIAL_SEQUENCE = ChainSequence.empty().chain("S");

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

    public List<Rule> getRules() {
        return rules;
    }

    public Grammar formulate() {
        classify();
        terminals = literalsSelectedBy(c -> c.is(TERMINAL));
        nonterminals = literalsSelectedBy(c -> c.is(NON_TERMINAL));
        return this;
    }

    public boolean classifiedAs(GrammarType... types) {
        classify();
        return Arrays.stream(types).anyMatch(t -> t.equals(type));
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

    public Stream<Rule> rules() {
        return rules.stream();
    }

    public boolean any(Predicate<Rule> predicate) {
        return rules().anyMatch(predicate);
    }

    public boolean all(Predicate<Rule> predicate) {
        return rules().allMatch(predicate);
    }

    private List<Rule> findRulesByLiteralsIn(ChainSequence chain, Function<Rule, Stream<ChainSequence>> ruleMapper) {
        return rules().filter(rule -> ruleMapper.apply(rule)
                                                .anyMatch(each -> each.containsSameAs(chain)))
                      .collect(Collectors.toUnmodifiableList());
    }

    private List<Rule> findRulesWithLeft(ChainSequence chain) {
        return findRulesByLiteralsIn(chain, rule -> Stream.of(rule.left()));
    }

    private List<Rule> findRulesWithRight(ChainSequence chain) {
        return findRulesByLiteralsIn(chain, Rule::rightSequences);
    }

    public boolean lookupLeft(String input, List<Rule> checked, ChainSequence sequence, Consumer<ChainSequence> consumer) {
        for(Rule rule : findRulesWithLeft(sequence)) {
            for(ChainSequence rightSequence : rule.right()) {
                consumer.accept(rightSequence);
                if(rightSequence.startsSameAs(input)) {
                    return true;
                }
                if(rule.isNotRecursive() && !checked.contains(rule)) {
                    checked.add(rule);
                    return lookupLeft(input, checked, rightSequence, consumer);
                }
            }
        }
        return false;
    }

    public boolean lookupRight(String input, List<Rule> checked, ChainSequence sequence, Consumer<ChainSequence> consumer) {
        consumer.accept(sequence);
        for(Rule rule : findRulesWithRight(sequence)) {
            ChainSequence leftSequence = rule.left();
                if(leftSequence.equals(INITIAL_SEQUENCE)) {
                    consumer.accept(leftSequence);
                    return true;
                }
                if(rule.isNotRecursive() && !checked.contains(rule)) {
                    checked.add(rule);
                    return lookupRight(input, checked, leftSequence, consumer);
            }
        }
        return false;
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
