import java.io.*;
import java.util.Scanner;
import java.util.Stack;

public class Lab2 {
    public static void main(String[] args) throws IOException {
        //Виконання функцій основного класу
        UCPP Postman = new UCPP();
    }
}
class UCPP {

    //Визначення ключових змінних
    int count = 0;
    int num = 0;
    int empty_row = 0;
    int empty_mtr2 = 0;
    double result = 0;
    Stack < Integer > S = new Stack < > ();
    Stack < Integer > C = new Stack < > ();
    Stack < Integer > M = new Stack < > ();

    UCPP() throws IOException {

        //n - розмірність читаємої матриці
        int n;
        //Оголошення матриці ваг
        double mtr[][];
        //Оголошення матриці зв'язності
        double mtr2[][];
        //Читання матриці з файлу
        File f = new File("c:\\matrix.txt");
        Scanner sc = new Scanner(f);
        n = sc.nextInt();
        mtr = new double[n][n];
        mtr2 = new double[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                mtr[i][j] = sc.nextDouble();
        //Закриття файлу
        sc.close();

        System.out.println("\nМатриця ваг:");
        for (int i = 0; i < mtr.length; i++) {
            for (int j = 0; j < mtr[i].length; j++) {
                System.out.printf("%4s\t", mtr[i][j]);
            }
            System.out.println();
        }

        //Заповнення матриці зв'язності
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mtr[i][j] != 0) {
                    mtr2[i][j] = 1;
                } else {
                    mtr2[i][j] = 0;
                }
            }
        }
        
        //Виведення матриці зв'язності
        System.out.println("\nМатриця зв'язності:");
        for (int i = 0; i < mtr2.length; i++) {
            for (int j = 0; j < mtr2[i].length; j++) {
                System.out.print(mtr2[i][j] + "    ");
            }
            System.out.println();
        }
        
        //Визначення вершин з непарною кількістю вихідних дуг
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (mtr[i][j] != 0) {
                    num++;
                }
            }
            if (num % 2 != 0) {
                M.push(i);
                num = 0;
            }
        }
        System.out.println("\nВершини з непарною степінню: " + M);
        
        //Дублювання шляхів ребер
        while (M.empty() == false) {
            int m = M.lastElement();
            M.pop();
            mtr2[M.lastElement()][m] = mtr2[M.lastElement()][m]+1;
            mtr2[m][M.lastElement()] =  mtr2[m][M.lastElement()]+1 ;
            M.pop();
        }
        
        //Вивід зміненої матриці зв'язності
        System.out.println("\nЗмінена матриця зв'язності:");
        for (int i = 0; i < mtr2.length; i++) {
            for (int j = 0; j < mtr2[i].length; j++) {
                System.out.print(mtr2[i][j] + "    ");
            }
            System.out.println();
        }
        
        //Підрахунок загальної ваги пройденого шляху
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result = result + mtr[i][j] * mtr2[i][j];
            }
        }

       //Внесення до стеку початкової вершини й знаходження першого суміжного ребра
        for (int j = 0; j < n; j++) {
            if (mtr2[0][j] != 0) {
                S.push(0);
                mtr2[0][j] = mtr2[0][j] - 1;
                mtr2[j][0] = mtr2[j][0] - 1;
                S.push(j);
                break;
            }
        }

        //Цикл діє поки в зміненій матриці зв'язності залишаються непройдені ребра
        while (empty_mtr2 == 0) {
        	//Цикл діє поки не зайшов в тупикову ситуацію
            while (empty_row == 0) {
                for (int j = 0; j < n; j++) {
                	//Пошук наступного суміжного ребра
                    if (mtr2[S.lastElement()][j] != 0) {
                        mtr2[S.lastElement()][j] = mtr2[S.lastElement()][j] - 1;
                        mtr2[j][S.lastElement()] = mtr2[j][S.lastElement()] - 1;
                        //Занесення вершини в стек
                        S.push(j);
                        //Нульове значення вказує що для вихідної вершини ще залишаються суміжні ребра
                        empty_row = 0;
                        break;
                    } else {
                    	//Одиниця вказує на тупикову ситуацію
                        empty_row = 1;
                    }
                }
            }
            //Перевіряємо чи лишилися непройдені ребра
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (mtr2[i][j] != 0) {
                        count++;
                    }
                }
            }
            //Якщо є непройдені ребра при тупиковій ситуації
            if (count > 0) {
            	//Переносимо останні пройдені вершини в інший стек, доки не знайдемо таку, що має суміжні ребра
                C.push(S.lastElement());
                S.pop();
                empty_row = 0;
                empty_mtr2 = 0;
                count = 0;
            //Якщо всі ребра пройдені то алгоритм завершено
            } else {
                empty_mtr2 = 1;
            }
        }
        //Переносимо весь вміст стеку S у стек C для отримання повного шляху
        while (S.empty() == false) {
            C.push(S.lastElement());
            S.pop();
        }
        
        //Покрокове виведення всього шляху разом з вагами для відповідних дуг
        int in = C.lastElement();
        int out = 0;
        C.pop();

        System.out.println("\nПокрокове виконання алгоритму:");
        while (C.empty() == false) {
            out = in ; in = C.lastElement();
            C.pop();
            System.out.println((out) + " -> " + ( in ) + ":  " + mtr[out][ in ]);
        }
        System.out.println("\nЗагальна вага пройденого шляху: " + (result / 2));

    }
}