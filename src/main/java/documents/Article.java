package documents;

import utiles.Pause;
import service.History;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Article extends Document {

    private String authorName;
    private Date publicationDate;

    public Article() {

    }

    public Article(int id, String title, String localisation, int numberCopies, String authorName, Date publicationDate) {
        super(id, title, localisation, numberCopies);
        this.authorName = authorName;
        this.publicationDate = publicationDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Article [ " + super.toString() + ", N.A: " + authorName + ", D.P: " + sdf.format(publicationDate) + " ]";
    }

    public void displayDocument(int available, int number){
        double k;
        if (number==0)
            k=0;
        else
            k= Math.ceil(((double) getNumberTotalBorrow() / number) * 100);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.printf("|%9s", getClass().getSimpleName());
        System.out.printf("|%4d", getId());
        System.out.printf("|%36s", getTitle());
        System.out.printf("|%20s", getAuthorName());
        System.out.print("|    ------------    ");
        System.out.printf("|%10s", sdf.format(getPublicationDate()));
        System.out.print("|   ------   ");
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
        System.out.println("|");
    }

    public void modifyDocument(Scanner sc, ArrayList<History> history){
        String choix, str;
        Pause p = new Pause();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("\t Que voulez vous modifier de cet Article : ");
        do {
            System.out.println("\t 0) Sortire");
            System.out.println("\t 1) Titre (" + getTitle() + ")");
            System.out.println("\t 2) Localisation (" + getRegion() + ")");
            System.out.println("\t 3) Nombre Exemplaires (" + getNumberCopies() + ")");
            System.out.println("\t 4) Nom auteur (" + getAuthorName() + ")");
            System.out.println("\t 5) Date publication (" + sdf.format(getPublicationDate()) + ")");
            choix = sc.nextLine();
            switch (choix){
                case "0": return;
                case "1":
                    String oldTitle = getTitle();
                    System.out.println("\t Entrer nouveau titre");
                    setTitle(sc.nextLine());
                    history.add(new History(new Date(), "Document", "Modification", getId(), "Titre de " + oldTitle + " par " + getTitle()));
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
                    boolean varified = false;
                    int nb = getNumberCopies();
                    do {
                        try{
                            setNumberCopies(sc.nextInt());
                            varified = true;
                        } catch (Exception e){
                            System.out.println("\t Erreur! Veillez entrer des chiffres...");
                            System.out.println("\t Entrer nouveau nombre d'exemplaires");
                            p.pause(500);
                            sc.nextLine();
                        }
                    }while (!varified);
                    sc.nextLine();
                    history.add(new History(new Date(), "Document", "Modification", getId(), "Nb exemplaires de " + nb + " a " + getNumberCopies() + " exemplaires en totale"));
                    System.out.println("\t Modification avec succees.");
                    p.pause(1000);
                    break;
                case "4":
                    String oldAuthorName = getAuthorName();
                    System.out.println("\t Entrer nouveau nom d'auteur");
                    setAuthorName(sc.nextLine());
                    history.add(new History(new Date(), "Document", "Modification", getId(), "Nom auteur de " + oldAuthorName + " par " + getAuthorName()));
                    System.out.println("\t Modification avec succees.");
                    p.pause(1000);
                    break;
                case "5":
                    String oldPublicationDate = sdf.format(getPublicationDate());
                    System.out.println("\t Entrer nouvelle date de publication sous forme (DD/MM/YYYY)");
                    str = sc.nextLine();
                    try {
                        setPublicationDate(sdf.parse(str));
                        System.out.println("\t Modification avec succees.");
                        history.add(new History(new Date(), "Document", "Modification", getId(), "Date publication de " + oldPublicationDate + " par " + sdf.format(
                                getPublicationDate())));
                        p.pause(1000);
                    }catch (ParseException e){
                        System.out.println("\t Forme non respecter!");
                        p.pause(1000);
                    }
                    break;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer");
                    p.pause(500);
                    break;
            }
        }while (true);
    }
}
