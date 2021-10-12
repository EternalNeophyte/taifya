package core.format;

public interface Formatting extends Regulars {

    String COMMA = ", ";
    String DELIMITER = " | ";
    String SQUARE_BRACKET = "]";
    String FLOATING_BRACKET = "}";

    default void replaceLast(StringBuilder sb, String removed, String placed) {
        sb.replace(sb.lastIndexOf(removed), sb.length(), placed);
    }

    default void perComma(StringBuilder sb, String statement) {
        sb.append(statement).append(COMMA);
    }
}
