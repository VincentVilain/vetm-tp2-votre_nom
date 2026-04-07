package personnel;

import utiles.Pause;
import service.History;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Member {

    private String name, firstName, address, type;
    private int id, maxNumberBorrow, maxBorrowDuration, borrowNumber = 0, borrowNumberTotal =0;
    private boolean late = false;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";


    public Member(){
    }

    public Member(int id, String name, String firstName, String address, String type, int maxNumberBorrow, int maximumBorrowDuration) {
        this.id = id;
        this.name = name;
        this.firstName = firstName;
        this.address = address;
        this.type = type;
        this.maxNumberBorrow = maxNumberBorrow;
        this.maxBorrowDuration = maximumBorrowDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxNumberBorrow() {
        return maxNumberBorrow;
    }

    public void setMaxNumberBorrow(int maxNumberBorrow) {
        this.maxNumberBorrow = maxNumberBorrow;
    }

    public int getMaxBorrowDuration() {
        return maxBorrowDuration;
    }

    public void setMaxBorrowDuration(int maxBorrowDuration) {
        this.maxBorrowDuration = maxBorrowDuration;
    }

    public int getBorrowNumber() {
        return borrowNumber;
    }

    public void setBorrowNumber(int borrowNumber) {
        this.borrowNumber = borrowNumber;
    }

    public boolean isLate() {
        return late;
    }

    public void setLate(boolean late) {
        this.late = late;
    }

    public int getBorrowNumberTotal() {
        return borrowNumberTotal;
    }

    public void setBorrowNumberTotal(int borrowNumberTotal) {
        this.borrowNumberTotal = borrowNumberTotal;
    }

    public String toString() {
        String ch= type + " : [ " + id + ", " + name.toUpperCase() + ", " + firstName + ", adresse : " + address + ", situation : ";
        if (late)
            ch += "retardataire";
        else
            ch += "n'est pas retardataire";
        ch += " ]";
        return ch;
    }

    public void displayMember(){
        System.out.printf("|%11s", getType());
        System.out.printf("|%4d", getId());
        System.out.printf("|%20s", getName());
        System.out.printf("|%20s", getFirstName());
        System.out.printf("|%30s", getAddress());
        if (getBorrowNumber() == getMaxNumberBorrow()){
            System.out.print("|"+ANSI_RED);
            System.out.printf("%8d", getBorrowNumber());
            System.out.print(ANSI_RESET);
        }else
            System.out.printf("|%8d", getBorrowNumber());
        if (isLate()){
            System.out.print("|"+ANSI_RED);
            System.out.printf("%8s", "OUI");
            System.out.print(ANSI_RESET);
        }
        else
            System.out.printf("|%8s", "NON");
        System.out.printf("|%13d", getBorrowNumberTotal());
        System.out.println("|");
    }

    public void modifyMember(Scanner sc, ArrayList<History> history){
        System.out.println("\t Que voulez vous modifier de " + getFirstName() + " " + getName());
        String choice, newName, newFirstName, newAddress;
        Pause p = new Pause();
        do {
            System.out.println("\t 0) Sortire");
            System.out.println("\t 1) Nom (" + getName() + ")");
            System.out.println("\t 2) Prenom (" + getFirstName() + ")");
            System.out.println("\t 3) Adresse (" + getAddress() + ")");
            System.out.println("\t 4) Type ("+getType()+")");
            choice = sc.nextLine();
            switch (choice) {
                case "0": return;
                case "1":
                    System.out.println("\t Entrer nouveau nom");
                    newName = sc.nextLine();
                    history.add(new History(new Date(), "Member", "Modification", getId(), getFirstName() + " " + getName() + " par " + getFirstName() + " " + newName));
                    setName(newName);
                    break;
                case "2":
                    System.out.println("\t Entrer nouveau prenom");
                    newFirstName = sc.nextLine();
                    history.add(new History(new Date(), "Member", "Modification", getId(), getFirstName() + " " + getName() + " par " + newFirstName + " " + getName()));
                    setFirstName(newFirstName);
                    break;
                case "3":
                    System.out.println("\t Entrer nouvelle adresse");
                    newAddress = sc.nextLine();
                    history.add(new History(new Date(), "Member", "Modification", getId(), "Adresse de " + getFirstName() + " " + getName() + " de " + getAddress() + " a " + newAddress));
                    setAddress(newAddress);
                    break;
                case "4":
                    String ancienType = getType();
                    enterMemberType(sc);
                    history.add(new History(new Date(), "Member", "Modification", getId(), getFirstName() + " " + getName() + " de " + ancienType + " a " + getType()));
                    break;
                default: System.out.println("\t Erreur! Taper 0 pour sortire"); p.pause(500); break;
            }
        }while (true);
    }

    public void enterMemberType(Scanner sc){
        String typeChoice;
        Pause p = new Pause();
        do {
            System.out.println("\t Veillez selectionner un de ces types");
            System.out.println("\t 1) Etudiant");
            System.out.println("\t 2) Enseignant");
            System.out.println("\t 3) Visiteur");
            typeChoice = sc.nextLine();
            switch (typeChoice){
                case "1":
                    setType("Etudiant");
                    setMaxNumberBorrow(2);
                    setMaxBorrowDuration(7);
                    return;
                case "2":
                    setType("Enseignant");
                    setMaxNumberBorrow(4);
                    setMaxBorrowDuration(21);
                    return;
                case "3":
                    setType("Visiteur");
                    setMaxNumberBorrow(1);
                    setMaxBorrowDuration(7);
                    return;
                default:
                    System.out.println("\t Choix du type adherent est non valide veillez réessayer");
                    p.pause(500);
                    break;
            }
        }while (true);
    }

}
