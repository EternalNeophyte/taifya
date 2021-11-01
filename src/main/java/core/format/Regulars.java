package core.format;

public interface Regulars {

    String EPSILON = "ε";
    String EPSILON_EXP = EPSILON + "|eps";
    String S = "S";
    String S_EXP = "^S$";
    String LOWERCASE_AND_DIGITS = "^[a-z0-9]$";
    String UPPERCASE_WITHOUT_S = "^[A-RT-Z]$";
    String NO_SPACE = "";
    String RULE_SPLITTER = "\\s*=\\s*|\\s*->\\s*|\\s*\\|\\s*";
}
