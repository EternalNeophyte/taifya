import java.util.InputMismatchException;
import java.util.Scanner;

import static util.Labs.*;

public class Application {

    public static void main(String... args) {
        System.out.print("Введите номер лаб. работы: ");
        Scanner scanner = new Scanner(System.in);
        try {
            switch (scanner.nextInt()) {
                case 1:
                    lab1();
                    break;
                case 5:
                    lab5();
                    break;
                default:
                    throw new InputMismatchException();
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Ошибка. Повторите ввод");
            main(args);
        }
    }
}
