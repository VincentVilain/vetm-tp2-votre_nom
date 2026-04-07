package documents;

import utiles.Pause;
import service.History;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Magazine extends Document {

    int frequency;

    public Magazine(){

    }

    public Magazine(int id, String title, String location, int numberCopies, int frequency) {
        super(id, title, location, numberCopies);
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String toString() {
        return "Magazine [ " + super.toString() + ", F: " + frequency + " ]";
    }

    public void displayDocument(int available, int number){
        double k;
        if (number==0)
            k=0;
        else
            k= Math.ceil(((double) getNumberTotalBorrow() / number) * 100);
        System.out.printf("|%9s", getClass().getSimpleName());
        System.out.printf("|%4d", getId());
        System.out.printf("|%36s", getTitle());
        System.out.print("|    ------------    ");
        System.out.print("|    ------------    ");
        System.out.print("|  ------  ");
        System.out.printf("|%12d", getFrequency());
        System.out.printf("|%12s", getRegion());
        System.out.printf("|%12d", getNumberCopies());
        if (available==0){
            System.out.print("|"+ANSI_RED);
            System.out.printf("%12d", available);
            System.out.print(ANSI_RESET);
        }else
            System.out.printf("|%12d", available);
        System.out.printf("|%13d", getNumberTotalBorrow());
        if (k>30){
            System.out.print("|"+ANSI_GREEN);
            System.out.printf("%5s", k);
            System.out.print("%");
            System.out.print(ANSI_RESET);
        }else{
            System.out.printf("|%5s", k);
            System.out.print("%");
        }
            System.out.printf("|%5s", k);
        System.out.print("%");
        System.out.println("|");
    }

    public void modifyDocument(Scanner sc, ArrayList<History> history){
        String choice;
        Pause p = new Pause();
        System.out.println("\t Que voulez vous modifier de ce Magazin : ");
        do {
            System.out.println("\t 0) Sortire");
            System.out.println("\t 1) Titre (" + getTitle() + ")");
            System.out.println("\t 2) Localisation (" + getRegion() + ")");
            System.out.println("\t 3) Nombre Exemplaires (" + getNumberCopies() + ")");
            System.out.println("\t 4) Fréquence de parution (" + getFrequency() + ")");
            choice = sc.nextLine();
            switch (choice){
                case "0": return;
                case "1":
                    String ancienTitre = getTitle();
                    System.out.println("\t Entrer nouveau titre");
                    setTitle(sc.nextLine());
                    history.add(new History(new Date(), "Document", "Modification", getId(), "Titre de " + ancienTitre + " par " + getTitle()));
                    System.out.println("\t Modification avec succees.");
                    p.pause(1000);
                    break;
                case "2":
                    String loc = getRegion();
                    System.out.println("\t Entrer nouvelle localisation (Salle/Rayon)");
                    setRegion(sc.nextLine());
                    System.out.println("\t Modification avec succees.");
                    history.add(new History(new Date(), "Document", "Modification", getId(), "Localisation de " + loc + " par " + getRegion()));
                    p.pause(1000);
                    break;
                case "3":
                    System.out.println("\t Entrer nouveau nombre d'exemplaires");
                    boolean verified = false;
                    int nb = getNumberCopies();
                    do {
                        try{
                            setNumberCopies(sc.nextInt());
                            verified = true;
                        } catch (Exception e){
                            System.out.println("\t Erreur! Veillez entrer des chiffres...");
                            System.out.println("\t Entrer nouveau nombre d'exemplaires");
                            p.pause(500);
                            sc.nextLine();
                        }
                    }while (!verified);
                    sc.nextLine();
                    history.add(new History(new Date(), "Document", "Modification", getId(), "Nb exemplaires de " + nb + " a " + getNumberCopies() + " exemplaires en totale"));
                    System.out.println("\t Modification avec succees.");
                    p.pause(1000);
                    break;
                case "4":
                    System.out.println("\t Entrer nouvelle fréquence ");
                    verified=false;
                    int freq = getFrequency();
                    do {
                        try{
                            setFrequency(sc.nextInt());
                            verified = true;
                        } catch (Exception e){
                            System.out.println("\t Erreur! Veillez entrer des chiffres...");
                            System.out.println("\t Entrer nouvelle fréquence ");
                            p.pause(500);
                            sc.nextLine();
                        }
                    }while (!verified);
                    sc.nextLine();
                    history.add(new History(new Date(), "Document", "Modification", getId(), "frequence de " + freq + " par " + getFrequency()));
                    System.out.println("\t Modification avec succees.");
                    p.pause(1000);
                    break;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer");
                    p.pause(500);
                    break;
            }
        }while (true);
    }
}
