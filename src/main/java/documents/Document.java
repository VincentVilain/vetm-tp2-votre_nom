package documents;

import service.History;
import service.BorrowDocument;

import java.util.ArrayList;
import java.util.Scanner;

public abstract class Document {

    protected String title, location;
    //numberCopies est le nombre d'exemplaires totale inclue les documents qui sont en cours de pret
    protected int id, numberCopies, numberTotalBorrow =0;
    protected ArrayList<BorrowDocument> borrowList = new ArrayList<>();
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public Document(){

    }

    public Document(int id, String title, String location, int numberCopies) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.numberCopies = numberCopies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return location;
    }

    public void setRegion(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberCopies() {
        return numberCopies;
    }

    public void setNumberCopies(int numberCopies) {
        this.numberCopies = numberCopies;
    }

    public ArrayList<BorrowDocument> getBorrowList() {
        return borrowList;
    }

    public int getNumberTotalBorrow() {
        return numberTotalBorrow;
    }

    public void setNumberTotalBorrow(int numberTotalBorrow) {
        this.numberTotalBorrow = numberTotalBorrow;
    }

    public String toString() {
        return id + ", T: " + title + ", N.E: " + numberCopies + ", L: " + location;
    }

    public abstract void displayDocument(int disponible, int nb);

    public abstract void modifyDocument(Scanner sc, ArrayList<History> historique);
}
