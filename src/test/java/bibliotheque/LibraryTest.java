package bibliotheque;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

class LibraryTest {

    Library l;
    ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        //Save the current PrintStream in a special variable
        PrintStream consoleStream = System.out;
        //Create a dynamic array
        outputStream = new ByteArrayOutputStream();
        //Create an adapter for the PrintStream class
        PrintStream stream = new PrintStream(outputStream);
        //Set it as the current System.out
        System.setOut(stream);

        Scanner sc = new Scanner(System.in);
//        Scanner sc = mock(Scanner.class);


        l = new Library(sc);
        l.seeds();
        l.init();
        //b.menu();
    }

    @Test
    void test() {
        l.displayMember();

        String result = outputStream.toString();
        int a =2;
    }
}