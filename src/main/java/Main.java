import bibliotheque.Library;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        Library l = new Library(sc);
        l.seeds();
        l.init();
        l.menu();
    }
}
