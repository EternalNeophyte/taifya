package util;

import core.automat.Automats;
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

    public static void lab5() {
        System.out.println("Введите строку для распознавания МП-автоматами");
        //Например, bccE
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        scanner.close();
        Grammar grammar = Grammar.describe()
                .rule("S -> AB")  //| ε
                .rule("A -> Aa | S | a")
                .rule("B -> bD | bS | b")
                .rule("D -> ccD")
                .rule("E -> eE |e");
        Automats.newLeftLooking()
                .grammar(grammar)
                .input(line)
                //.abortIfInputIsIncorrect()
                .execute()
                .printTrace();
        Automats.newExtendedRightLooking()
                .grammar(grammar)
                .marquer("#")
                .input(line)
                .execute()
                .printTrace();
    }
}
