package core.grammar;

public enum GrammarType {

    TYPE_0("Тип 0"),
    CONTEXT_DEPENDANT("Контекстно-зависимая (Тип 1)"),
    CONTEXT_FREE("Контекстно-свободная (Тип 2)"),
    REGULAR("Регулярная (Тип 3)"),
    REGULAR_ALIGNED_LEFT("Регулярная, выровненная влево"),
    REGULAR_ALIGNED_RIGHT("Регулярная, выровненная вправо");

    private final String definition;

    GrammarType(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return definition;
    }
}
