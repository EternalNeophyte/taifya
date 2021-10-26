package util;

import core.grammar.Grammar;

import java.util.Scanner;
import java.util.function.Consumer;

public class Labs {

    private static void printLegendForLab1(String stopLine) {
        StringBuilder legend = new StringBuilder();
        legend.append("Опишите правила грамматики через 'Enter', используя\n")
                .append("• заглавные латинские буквы для нетерминалов;\n")
                .append("• строчные латинские буквы и цифры для терминалов;\n")
                .append("• '->' или '=' - переход внутри правила;\n")
                .append("• 'eps' или 'ε' - пустая цепочка;\n")
                .append("Введите '")
                .append(stopLine)
                .append("', чтобы закончить");
        System.out.println(legend);
    }

    private static void scanWith(String stopLine, Consumer<String> consumer) {
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            String nextLine = scanner.nextLine();
            if(nextLine.equals(stopLine)) {
                break;
            }
            consumer.accept(nextLine);
        }
        scanner.close();
    }

    public static void lab1() {
        final String stopLine = "@";
        Grammar grammar = Grammar.describe();
        printLegendForLab1(stopLine);
        scanWith(stopLine, grammar::rule);
        grammar.formulate().print();
    }
}
