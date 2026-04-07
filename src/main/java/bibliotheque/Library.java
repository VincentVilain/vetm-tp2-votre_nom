package bibliotheque;

import documents.Article;
import documents.Book;
import documents.Document;
import documents.Magazine;
import personnel.Member;
import service.History;
import service.BorrowDocument;
import utiles.Pause;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Library {
    private final Pause pause = new Pause();
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<Document> documents = new ArrayList<>();
    private ArrayList<History> history = new ArrayList<>();
    private Scanner sc;
    private int memberId = 100, documentId = 100, totalBorrowNumber =0;
    public static final String Y = "\u001B[33m";
    public static final String RES = "\u001B[0m";

    public Library(Scanner sc) {
        this.sc = sc;
    }

    public void init(){
        Date todayDate = new Date();
        documents.forEach(d -> {
            for (BorrowDocument borrowDocument:d.getBorrowList()){
                long borrowDuration = todayDate.getTime()/(24*60*60*1000) - borrowDocument.getBorrowingDate().getTime() / (24 * 60 * 60 * 1000) - 1;
                if (borrowDuration>borrowDocument.getMember().getMaxBorrowDuration()){
                    borrowDocument.getMember().setLate(true);
                    history.add(new History(new Date(), "Member", "Retard", borrowDocument.getMember().getId(), borrowDocument.getMember().getFirstName() + " " + borrowDocument.getMember().getName() + " est retardataire : n'a pas rendu le document: " + d.getTitle()));
                }
            }
        });
    }

    public void menu(){
        String choice;
        do {
            System.out.println("\n\t **********\tLE MENU PRINCIPAL DE LA BIBLIOTHEQUE\t**********");
            System.out.println("\t Veillez choisir le numero correspondant à votre choix");
            System.out.println("\t 0 ) Quitter l'application");
            System.out.println("\t 1 ) Gestion des Adherents");
            System.out.println("\t 2 ) Gestion des documents");
            System.out.println("\t 3 ) Gestion des prêts");
            System.out.println("\t 4 ) Afficher tout l'historique\n");
            choice = sc.nextLine();
            switch (choice){
                case "0": System.out.println("\t Fermeture en cours ...");
                    pause.pause(1000); System.exit(0); break;
                case "1": memberManagement(); break;
                case "2": documentManagement(); break;
                case "3": borrowManagement(); break;
                case "4": allHistory(); break;
                default: System.out.println("\t Choix invalide, veillez réessayer");
                    pause.pause(1000); break;
            }
        }while (true);
    }

    public void memberManagement(){
        String memberChoice;
        do {
            System.out.println("\n\t *****\tGestion Adhérents\t*****");
            System.out.println("\t Veillez choisir le numero correspondant à votre choix");
            System.out.println("\t 0 ) Retour au menu principale");
            System.out.println("\t 1 ) Afficher les adhérent disponible");
            System.out.println("\t 2 ) Ajouter un nouvelle adherent");
            System.out.println("\t 3 ) Modifier un adherent par id");
            System.out.println("\t 3 ) Supprimer un adherent par id");
            System.out.println("\t 5 ) Rechercher un client et afficher ces informations");
            System.out.println("\t 6 ) Afficher les adherents retardataires");
            System.out.println("\t 7 ) Renouveler un adherent");
            memberChoice = sc.nextLine();
            switch (memberChoice){
                case "0":   menu(); break;
                case "1":   displayMember(); break;
                case "2":   addMember(); break;
                case "3":   modifyMember(); break;
                case "4":   deleteMember(); break;
                case "5":   findMemberInfos(); break;
                case "6":   displayMemberLate(); break;
                case "7":   renewMemberLate(); break;
                default:    System.out.println("\t Choix invalide! veillez réessayer");
                    pause.pause(1000); break;
            }
        }while (!memberChoice.equals("0"));
    }

    public void displayMember(){
        System.out.println("\n\t *****\tLes adherent de la bibliotheque\t*****");
        int nb =0;
        if (members.isEmpty())
            System.out.println("\t Il n'y a pas encore d'adherent enregistrer dans cette bibliothéque");
        else{
            memberHeader();
            for (Member a: members){
                a.displayMember();
                memberFrame();
                nb++;
            }
        }
        System.out.println("\n\t "+nb+" Member trouvé! Appuyez sur entrer pour continuer...");
        sc.nextLine();
    }

    public void addMember(){
        System.out.println("\n\t *****\tAjouter un nouveau adherent a la bibliotheque\t*****");
        Member a = new Member();
        a.enterMemberType(sc);
        a.setId(memberId);
        System.out.println("\t Nom de l'adherent a ajouter : ");
        a.setName(sc.nextLine());
        System.out.println("\t Prenom de l'adherent : ");
        a.setFirstName(sc.nextLine());
        System.out.println("\t Adresse de l'adherent : ");
        a.setAddress(sc.nextLine());
        this.members.add(a);
        this.memberId++;
        history.add(new History(new Date(), "Member", "Ajout", a.getId(), a.getType() + " sous le nom de " + a.getFirstName() + " " + a.getName()));
        System.out.println("\t Member " + a.getFirstName() + " " + a.getName() + " ajouté avec succées.");
        pause.pause(1000);
    }

    public void modifyMember(){
        System.out.println("\n\t *****\tModifier un adherent par id\t*****");
        Member a;
        a = findMemberById();
        a.modifyMember(sc, history);
    }

    public void deleteMember(){
        System.out.println("\n\t *****\tSuppression d'Member\t*****");
        Member a;
        a = findMemberById();
//        VOIR SI L'ADHERENT A UN PRET EN COURS
        for (Document d: documents)
            for (BorrowDocument pretDocument:d.getBorrowList())
                if (pretDocument.getMember() == a){
                    System.out.println("\t Suppression impossible! Cet adhérent a encore des pret en cours...");
                    pause.pause(1000);
                    return;
                }
        System.out.println("\t Vous êtes sur de supprimer l'adhérent : " + a.getFirstName() + " " + a.getName() + " ?");
        System.out.println("\t 1 ) Continuer");
        if (sc.nextLine().equals("1")){
//           SUPPRIMER ADHERENT
            history.add(new History(new Date(), "Member", "Suppression", a.getId(), a.getType() + ": " + a.getFirstName() + " " + a.getName()));
            members.remove(a);
            System.out.println("\t Member supprimer avec succees...");
            pause.pause(1000);
            return;
        }else
            System.out.println("\t Annulation...");
        pause.pause(1000);
    }

    public void findMemberInfos(){
        System.out.println("\n\t *****\tChercher toutes les information d'un adherent");
        System.out.println("\t Chercher l'adherent : ");
        ArrayList<Member> listAdherentTrouver = new ArrayList<>();
        searchMemberByIdOrNameOrAddress(listAdherentTrouver);
        memberHeader();
        for (Member a:listAdherentTrouver){
            a.displayMember();
            memberFrame();
        }
        pause.pause(500);
        if (listAdherentTrouver.size() == 1)
            System.out.println("\n\t Les livres en cours de prêt de " + listAdherentTrouver.get(0).getFirstName() + " " + listAdherentTrouver.get(0).getName() + " : \n");
        else
            System.out.println("\n\t Les livres en cours de prêt de ces adherents : \n");
        borrowHeader();
        for (Member a:listAdherentTrouver)
            for (Document d: documents)
                for (BorrowDocument pretDocument:d.getBorrowList())
                    if (pretDocument.getMember() == a){
                        pretDocument.displayBorrow(d);
                        borrowFrame();
                    }
        System.out.println("\t Appuyez sur entrer pour continuer...");
        sc.nextLine();
    }

    public void displayMemberLate(){
        System.out.println("\n\t *****\tLes adherent retardataires !\t*****\n");
        int nb=0;
        for (Member a: members)
            if (a.isLate()) {
                if (nb==0){
                    memberHeader();
                }
                a.displayMember();
                memberFrame();
                nb++;
            }
        if (nb==0)
            System.out.println("\t Il n'y a pas encore d'adherent retardataire");
        System.out.println("\n\t "+nb+" Member retardataire! Appuyez sur entrer pour continuer...");
        sc.nextLine();
    }

    public void renewMemberLate(){
        System.out.println("\n\t *****\t Renouveler un adherent retard\t*****");
        int id;
        String choice;
        do {
            System.out.println("\t 0 ) Retour");
            System.out.println("\t 1 ) afficher tout les adherents retard");
            System.out.println("\t 2 ) Renouveller adherent par ID");
            System.out.println("\t Veillez entrer votre choix");
            choice = sc.nextLine();
            switch (choice){
                case "0":   memberManagement(); break;
                case "1":   displayMemberLate(); break;
                case "2":
                    System.out.println("\t Entrer ID : ");
                    id = checkId();
                    sc.nextLine();
                    for (Member a: members){
                        if (a.getId() == id){
//                            ADHERENT TROUVER
                            if (a.isLate()){
                                for (Document d: documents){
                                    for (BorrowDocument borrowDocument:d.getBorrowList()){
                                        if (borrowDocument.getMember() == a){
                                            System.out.println("\t Cette adherent n'a pas encore rendu le document "+d.getTitle());
                                            System.out.println("\t Il faut rendre le document d'abord! Merci");
                                            pause.pause(1000);
                                            return;
                                        }
                                    }
                                }
                                a.setLate(false);
                                history.add(new History(new Date(), "Member", "Renouvellement", a.getId(), a.getFirstName() + " " + a.getName()));
                                System.out.println("\t Renouvellement réussie avec succées!");
                                pause.pause(500);
                                memberManagement();
                                return;
                            }
                            System.out.println("\t Cette adherent n'est pas retardataire");
                            pause.pause(500);
                            return;
                        }
                    }
                    System.out.println("\t Member introuvable! Veillez vérifier l'ID et réessayer...");
                    pause.pause(500);
                    break;
                default:
                    System.out.println("\t Choix invalide! Veillez réessayer");
                    break;
            }
        }while (!choice.equals("0"));
    }

    public void documentManagement(){
        String choixdocument;
        do {
            System.out.println("\n\t *****\tGestion Documents\t*****");
            System.out.println("\t Veillez choisir le numero correspondant à votre choix");
            System.out.println("\t 0 ) Retour au menu principale");
            System.out.println("\t 1 ) Afficher les Documents disponible");
            System.out.println("\t 2 ) Ajouter un nouveau document");
            System.out.println("\t 3 ) Modifier un document par id");
            System.out.println("\t 4 ) Supprimer un document par id");
            System.out.println("\t 5 ) Chercher un document");
            choixdocument = sc.nextLine();
            switch (choixdocument){
                case "0": menu(); break;
                case "1": displayDocument(); break;
                case "2": addDocument(); break;
                case "3": modifyDocument(); break;
                case "4": deleteDocument(); break;
                case "5": searchDocument(); break;
                default: System.out.println("\t Choix invalide! veillez réessayer");
                    pause.pause(1000); break;
            }
        }while (!choixdocument.equals("0"));
    }

    public void displayDocument(){
        System.out.println("\n\t *****\tLes documents de la bibliotheque\t*****");
        int  nb=0;
        if (documents.isEmpty())
            System.out.println("\t Il n'y a pas encore de document enregistrer dans cette bibliotheque");
        else{
            documentHeader();
            for (Document d: documents){
                int disponible = d.getNumberCopies() - d.getBorrowList().size();
                d.displayDocument(disponible, totalBorrowNumber);
                documentFrame();
                nb++;
            }
        }
        System.out.println("\n\t "+nb+" Document disponible! Appuyez sur entrer pour continuer...");
        sc.nextLine();
    }

    public void addDocument(){
        System.out.println("\n\t *****\tAjouter un nouveau document a la bibliotheque\t*****");
        String choixType, str;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("\t Veillez selectionner le type du document a ajouter");
        do {
            System.out.println("\t 0) Annuler");
            System.out.println("\t 1) Article Scientifique");
            System.out.println("\t 2) Book");
            System.out.println("\t 3) Magazine Scientifique");
            choixType = sc.nextLine();
            switch (choixType){
                case "0": documentManagement(); break;
                case "1":
                    Article a = new Article();
                    documentFilling(a);
                    System.out.println("\t Entrer Nom d'auteur");
                    a.setAuthorName(sc.nextLine());
                    System.out.println("\t Entrer la date de publication de l'article sous forme (DD/MM/YYYY)");
                    str = sc.nextLine();
                    boolean verifier = false;
                    do {
                        try {
                            a.setPublicationDate(sdf.parse(str));
                            verifier = true;
                        }catch (ParseException e){
                            System.out.println("\t Forme non respecter!");
                            System.out.println("\t Entrer la date de publication de l'article sous forme (DD/MM/YYYY)");
                            str = sc.nextLine();
                        }
                    }while (!verifier);
                    this.documents.add(a);
                    history.add(new History(new Date(), "Document", "Ajout", a.getId(), "Article : " + a.getTitle() + " par " + a.getAuthorName()));
                    this.documentId++;
                    System.out.println("\n\t Article ajouté avec succées.\n");
                    pause.pause(1000);
                    return;
                case "2":
                    Book l = new Book();
                    documentFilling(l);
                    System.out.println("\t Entrer Nom d'auteur");
                    l.setAuthorName(sc.nextLine());
                    System.out.println("\t Entrer Nom éditeur");
                    l.setEditorName(sc.nextLine());
                    System.out.println("\t Entrer la date de l'édition du livre sous forme (DD/MM/YYY)");
                    str = sc.nextLine();
                    verifier = false;
                    do {
                        try {
                            l.setPublicationDate(sdf.parse(str));
                            verifier = true;
                        }catch (ParseException e){
                            System.out.println("\t Forme non respecter!");
                            System.out.println("\t Entrer la date de l'édition du livre sous forme (DD/MM/YYY)");
                            str = sc.nextLine();
                        }
                    }while (!verifier);
                    this.documents.add(l);
                    history.add(new History(new Date(), "Document", "Ajout", l.getId(), "Book : " + l.getTitle() + " par " + l.getAuthorName()));
                    this.documentId++;
                    System.out.println("\n\t Book ajouté avec succées.\n");
                    pause.pause(1000);
                    return;
                case "3":
                    Magazine m = new Magazine();
                    verifier = false;
                    documentFilling(m);
                    System.out.println("\t Entree la fréquence de parution du magazin");
                    do {
                        try {
                            m.setFrequency(sc.nextInt());
                            verifier = true;
                        }
                        catch (Exception e){
                            System.out.println("\t Erreur! Veillez entrer des chiffres...");
                            System.out.println("\t Entree la fréquence de parution du magazin");
                            pause.pause(500);
                            sc.nextLine();
                        }
                    }while (!verifier);
                    sc.nextLine();
                    this.documents.add(m);
                    history.add(new History(new Date(), "Document", "Ajout", m.getId(), "Magazine : " + m.getTitle()));
                    this.documentId++;
                    System.out.println("\n\t Magazine ajouté avec succées.\n");
                    pause.pause(1000);
                    return;
                default:
                    System.out.println("\t Choix du type Document est non valide veillez réessayer");
                    pause.pause(1000);
                    break;
            }
        }while (!choixType.equals("0"));
    }

    public void modifyDocument(){
        System.out.println("\n\t *****\tModifier un document par id\t*****");
        Document d;
        d = searchDocumentById();
        d.modifyDocument(sc, history);
    }

    public void deleteDocument(){
        System.out.println("\n\t *****\tSuppression de document\t*****");
        Document d;
        d = searchDocumentById();
        System.out.println("\t Vous êtes sur de supprimer le document : " + d.getTitle() + " ?");
        if (!d.getBorrowList().isEmpty())
            System.out.println("\n\t !! Ce document possède des pret en cours, ca suppression évoquera la suppression de tout ces prets !!");
        System.out.println("\t 1 ) Continuer");
        if (sc.nextLine().equals("1")){
            history.add(new History(new Date(), "Document", "Suppression", d.getId(), d.getTitle()));
            documents.remove(d);
            System.out.println("\t Document supprimer avec succees...");
            pause.pause(1000);
            return;
        }else
            System.out.println("\t Annulation...");
        pause.pause(1000);
    }

    public void searchDocument(){
        System.out.println("\n\t *****\tChercher un document et afficher ces prets\t*****");
        ArrayList<Document> foundDocuments = new ArrayList<>();
        findDocumentByIdOrTitle(foundDocuments);
        documentHeader();
        for (Document d:foundDocuments){
            int available = d.getNumberCopies() - d.getBorrowList().size();
            d.displayDocument(available, totalBorrowNumber);
            documentFrame();
        }
        pause.pause(500);
        if (foundDocuments.size() == 1)
            System.out.println("\n\t Les prêts en cours de " + foundDocuments.get(0).getTitle() + " : \n");
        else
            System.out.println("\n\t Les prêts en cours de ces documents : \n");
        borrowHeader();
        for (Document d:foundDocuments)
            for (BorrowDocument borrowDocument:d.getBorrowList()){
                borrowDocument.displayBorrow(d);
                borrowFrame();
            }
        System.out.println("\t Appuyez sur entrer pour continuer...");
        sc.nextLine();
    }

    public void borrowManagement(){
        String borrowChoice;
        do {
            System.out.println("\n\t *****\tGestion Des Prets\t*****");
            System.out.println("\t Veillez choisir le numero correspondant à votre choix");
            System.out.println("\t 0 ) Retour au menu principale");
            System.out.println("\t 1 ) Afficher tout Prêts disponible");
            System.out.println("\t 2 ) Ajouter un nouveau prêt");
            System.out.println("\t 3 ) Modifier un prêt");
            System.out.println("\t 4 ) Rendre un document");
            borrowChoice = sc.nextLine();
            switch (borrowChoice){
                case "0": menu(); break;
                case "1": displayBorrow(); break;
                case "2": addBorrow(); break;
                case "3": modifyBorrow(); break;
                case "4": returnBorrow(); break;
                default: System.out.println("\t Choix invalide! veillez réessayer");
                    pause.pause(1000); break;
            }
        }while (!borrowChoice.equals("0"));
    }

    public void displayBorrow(){
        int nb=0;
        System.out.println("\n\t ******\tTout les prêts en cours\t*****");
//        VERIFIER SI IL EXISTE UN PRET EN COURS
        for (Document d: documents)
            for (BorrowDocument pretDocument:d.getBorrowList()) {
                if (nb==0){
                    borrowHeader();
                }
                pretDocument.displayBorrow(d);
                borrowFrame();
                nb++;
            }
        if (nb==0)
            System.out.println("\t Il n'y a aucune pret en cours!");
        System.out.println("\n\t "+nb+" pret disponible! Appuyez sur entrer pour continuer...");
        sc.nextLine();
    }

    public void addBorrow(){
        System.out.println("\n\t *****\tAjouter un prêt\t*****");
        System.out.println("\t Selectionnez l'adherent");
        Member a;
        a = findMemberById();
        System.out.println("\t Selectionnez le document");
        Document d;
        d = searchDocumentById();
//          VOIR S'IL A ATTEINT NOMBRE DE PRET LIMITE
        if (a.getMaxNumberBorrow() > a.getBorrowNumber()){
//              TOUTES LES CONTRAINTES SUR L'ADHERENT SONT VERIFIER & VOIR SI DOCUMENT EXISTE ENCORE DANS LA BIBLIOTHEQUE
            if (d.getBorrowList().size() < d.getNumberCopies()){
//                  TOUT EST VERIFIER & MAINTENANT NE RESTE QUE L'AFFECTATION
                BorrowDocument pretDocument = new BorrowDocument();
                pretDocument.setBorrowingDate(new Date());
                pretDocument.setMember(a);
                pretDocument.calculateReturnDate();
//                    AJOUTER LE PRET
                d.getBorrowList().add(pretDocument);
                a.setBorrowNumber(a.getBorrowNumber() + 1);
                d.setNumberTotalBorrow(d.getNumberTotalBorrow() + 1);
                a.setBorrowNumberTotal(a.getBorrowNumberTotal() + 1);
                history.add(new History(new Date(), "Pret", "Ajout", a.getId(), a.getType() + ": " + a.getFirstName() + " " + a.getName() + " a pris le document : " + d.getTitle()));
                System.out.println("\t Prêt du document effectuée avec succées! MERCI.");
                totalBorrowNumber++;
                pause.pause(1000);
                return;
            }
            System.out.println("\t Ce document n'est plus disponible!");
            pause.pause(1000);
            return;
        }
        System.out.println("\t Cette adherent a atteint ces emprunts limites");
        pause.pause(1000);
    }

    public void modifyBorrow(){
        System.out.println("\n\t *****\tModification d'un prêt\t*****");
        String choice;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("\t Selectionnez l'adherent");
        Member a;
        a = findMemberById();
        System.out.println("\t Selectionnez le document");
        Document d;
        d = searchDocumentById2(a);
        for (BorrowDocument pretDocument:d.getBorrowList()){
            if (pretDocument.getMember() == a){
//                MODIFIER
                do {
                    System.out.println("\t Veillez choisir le numero correspondant à votre choix");
                    System.out.println("\t 0 ) Annuler");
                    System.out.println("\t 1 ) Modifier le document (" + d.getTitle() + ")");
                    System.out.println("\t 2 ) Modifier l'adherent (" + a.getFirstName() + " " + a.getName() + ")");
                    System.out.println("\t 3 ) Modifier la date du pret (" + sdf.format(pretDocument.getBorrowingDate()) + ")");
                    choice = sc.nextLine();
                    switch (choice){
                        case "0": borrowManagement(); break;
                        case "1":
                            Document newDocument;
                            System.out.println("\t Chercher le nouveau document");
                            newDocument = searchDocumentById();
//                        VERIFIER SI LE NOUVEAU DOCUMENT EXISTE
                            if (newDocument.getBorrowList().size() < newDocument.getNumberCopies()){
//                              TOUT EST VERIFIER & MAINTENANT NE RESTE QUE L'AFFECTATION
                                newDocument.getBorrowList().add(pretDocument);
                                d.getBorrowList().remove(pretDocument);
                                newDocument.setNumberTotalBorrow(newDocument.getNumberTotalBorrow() + 1);
                                d.setNumberTotalBorrow(d.getNumberTotalBorrow() - 1);
                                history.add(new History(new Date(), "Pret", "Modification", 0, "Document: Member " + a.getFirstName() + " " + a.getName() + " change le document" + d.getTitle() + " par " + newDocument.getTitle()));
                                d = newDocument;
                                System.out.println("\t Modification Réusite.");
                                pause.pause(500);
                                break;
                            }
                            System.out.println("\t Ce nouveau document n'est plus disponible!");
                            pause.pause(500);
                            break;
                        case "2":
                            Member newAdherent;
                            System.out.println("\t Chercher le nouveau adherent");
                            newAdherent = findMemberById();
//                            TESTER SI ADHERENT RETARDATAIRE OU NE PEUX PLUS PRENDRE DE DOCUMENTS
                            if (!newAdherent.isLate())
                                if (newAdherent.getMaxNumberBorrow() > newAdherent.getBorrowNumber()){
                                    pretDocument.setMember(newAdherent);
                                    newAdherent.setBorrowNumber(newAdherent.getBorrowNumber() + 1);
                                    a.setBorrowNumber(a.getBorrowNumber() - 1);
                                    newAdherent.setBorrowNumberTotal(newAdherent.getBorrowNumberTotal() + 1);
                                    a.setBorrowNumberTotal(a.getBorrowNumberTotal() - 1);
                                    history.add(new History(new Date(), "Pret", "Modification", 0, "Member: " + a.getType() + ": " + a.getFirstName() + " " + a.getName() + " par " + newAdherent.getType() + ": " + newAdherent.getFirstName() + " " + newAdherent.getName()));
                                    a = newAdherent;
                                    System.out.println("\t Modification réussite.");
                                    pause.pause(500);
                                    break;
                                }
                            System.out.println("\t Cet Member ne peut pas emprunter un document!");
                            pause.pause(500);
                            break;
                        case "3":
//                            MODIFICATION DE LA DATE DE PRET
                            String str, ancienneDate;
                            ancienneDate = sdf.format(pretDocument.getBorrowingDate()) ;
                            System.out.println("\t Entrer la date de publication de l'article sous forme (DD/MM/YYYY)");
                            str = sc.nextLine();
                            try {
                                pretDocument.setBorrowingDate(sdf.parse(str));
                                pretDocument.calculateReturnDate();
                                history.add(new History(new Date(), "Pret", "Modification", 0, "Date: de " + ancienneDate + " a " + sdf.format(pretDocument.getBorrowingDate())));
                                System.out.println("\t Modification réussite.");
                                pause.pause(500);
                            }catch (ParseException e){
                                System.out.println("\t Forme non respecter! ");
                                pause.pause(500);
                            }
                            break;
                        default:
                            System.out.println("\t Choix non valide veillez réessayer");
                            pause.pause(500);
                            break;
                    }
                }while (!choice.equals("0"));
            }
        }
        System.out.println("\t Cette adherent n'a pas emprunté ce document");
    }

    public void returnBorrow(){
        System.out.println("\n\t *****\tRendre un document\t*****");
        System.out.println("\t Chercher l'adherent a rendre le document");
        Member a;
        a = findMemberById();
        System.out.println("\t Chercher le document a rendre");
        Document d;
        d = searchDocumentById2(a);
        for (BorrowDocument pretDocument:d.getBorrowList()){
            if (pretDocument.getMember() == a){
                history.add(new History(new Date(), "Pret", "Suppression", a.getId(), "l 'adherent: " + a.getFirstName() + " " + a.getName() + " a rendu le document: " + d.getTitle()));
                d.getBorrowList().remove(pretDocument);
                a.setBorrowNumber(a.getBorrowNumber() - 1);
                System.out.println("\t Document rendu avec succées. Merci!");
                pause.pause(500);
                return;
            }
        }
        System.out.println("\t Cette adherent n'a pas emprunté ce document");
    }

    public void allHistory(){
        System.out.println("\n\t ***\tHISTORIQUE\t*****");
        String choice;
        do {
            System.out.println("\t Veillez choisir le numero correspondant à votre choix");
            System.out.println("\t 0 ) Retour au menu principale");
            System.out.println("\t 1 ) Afficher tout l'historique disponible");
            System.out.println("\t 2 ) Afficher historique des adhérents");
            System.out.println("\t 3 ) Afficher historique des documents");
            System.out.println("\t 4 ) Afficher historique des prets");
            System.out.println("\t 5 ) Afficher historique des modifications");
            System.out.println("\t 6 ) Afficher historique des ajout");
            System.out.println("\t 7 ) Afficher historique des suppressions");
            System.out.println("\t 8 ) Afficher historique d'une date entrer");
            System.out.println("\t 9 ) Afficher historique par ID");
            choice = sc.nextLine();
            switch (choice){
                case "0": menu(); break;
                case "1": displayHistory(); break;
                case "2": displayHistoryByClassName("Member"); break;
                case "3": displayHistoryByClassName("Document"); break;
                case "4": displayHistoryByClassName("Pret"); break;
                case "5": displayHistoryByType("Modification"); break;
                case "6": displayHistoryByType("Ajout"); break;
                case "7": displayHistoryByType("Suppression"); break;
                case "8": displayHistoryByDate(); break;
                case "9": displayHistoryById(); break;
                default: System.out.println("\t Choix invalide! veillez réessayer");
                    pause.pause(1000); break;
            }
        }while (!choice.equals("0"));
    }

    public void displayHistory(){
        int count = history.size() - 1;
        int i;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        historyHeader();
        outerloop:
        while (count>=0) {
            i=0;
            while (i < 10) {
                if (count >= 0) {
                    history.get(count).afficherCetHistorique();
                    i++;
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public void displayHistoryByClassName(String str){
        int count = history.size() - 1;
        int i;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        historyHeader();
        outerloop:
        while (count>=0) {
            i=0;
            while (i < 10) {
                if (count >= 0) {
                    if (history.get(count).getClasse().equals(str)){
                        history.get(count).afficherCetHistorique();
                        i++;
                    }
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public void displayHistoryByType(String str){
        int count = history.size() - 1;
        int i=0;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        historyHeader();
        outerloop:
        while (count>=0) {
            while (i < 10) {
                i=0;
                if (count >= 0) {
                    if (history.get(count).getType().equals(str)){
                        history.get(count).afficherCetHistorique();
                        i++;
                    }
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public void displayHistoryByDate(){
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
        String str;
        int i;
        System.out.println("\t Entrer une date sous forme (dd/MM/yyyy) : ");
        str = sc.nextLine();
        try {
            sdf.parse(str);
        }catch (ParseException e){
            System.out.println("\t Forme non respecter...");
            pause.pause(500);
            return;
        }
        int count = history.size() - 1;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        historyHeader();
        outerloop:
        while (count>=0) {
            i=0;
            while (i < 10) {
                if (count >= 0) {
                    if (sdf.format(history.get(count).getAddedDate()).equals(str)){
                        history.get(count).afficherCetHistorique();
                        i++;
                    }
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public void displayHistoryById(){
        Member a;
        a = findMemberById();
        int i;
        int count = history.size() - 1;
        System.out.println("\n\t *****\tHISTORIQUE DE LA BIBLIOTHEQUE\t*****\n");
        historyHeader();
        outerloop:
        while (count>=0) {
            i=0;
            while (i < 10) {
                if (count >= 0) {
                    if (history.get(count).getId() == a.getId() && history.get(count).getClasse().equals("Member")){
                        history.get(count).afficherCetHistorique();
                        i++;
                    }
                    count--;
                } else
                    break outerloop;
            }
            if (count >=0){
                System.out.println("\t entrer pour continuer, 0 pour sortire...");
                if (sc.nextLine().equals("0"))
                    return;
            }
        }
        System.out.println("\n\t Appuyer sur entrer pour retourner...");
        sc.nextLine();
    }

    public Member findMemberById(){
        int id;
        String choice;
        Member member = new Member();
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Lister les adhérents");
            System.out.println("\t 2 ) Entree ID adherent");
            System.out.println("\t Veillez choisir une option : ");
            choice = sc.nextLine();
            switch (choice){
                case "0": memberManagement(); break;
                case "1": displayMember(); break;
                case "2":
                    System.out.println("\t Entrer l'ID de l'adherent : ");
                    id = checkId();
                    sc.nextLine();
                    for (Member a: members)
                        if (a.getId() == id)
                            return a;
                    System.out.println("\t Member introuvable! veillez réssayer");
                    pause.pause(500);
                    break;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause.pause(500);
                    break;
            }
        }while (!choice.equals("0"));
        return member;
    }

    public void searchMemberByIdOrNameOrAddress(ArrayList<Member> foundMembers){
        String choice, name, firstName, address;
        int id;
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Chercher adherent par ID");
            System.out.println("\t 2 ) Chercher adherent par NOM");
            System.out.println("\t 3 ) Chercher adherent par PRENOM");
            System.out.println("\t 4 ) Chercher adherent par ADRESSE");
            System.out.println("\t Veillez choisir une option : ");
            choice = sc.nextLine();
            switch (choice){
                case "0": memberManagement(); return;
                case "1":
                    System.out.println("\t Entrer l'ID de l'adherent : ");
                    id = checkId();
                    sc.nextLine();
                    for (Member a: members)
                        if (a.getId() == id){
                            foundMembers.add(a);
                            return;
                        }
                    System.out.println("\t Member introuvable! veillez réssayer");
                    pause.pause(500);
                    break;
                case "2":
                    System.out.println("\t Entrer NOM adherent");
                    name = sc.nextLine();
                    for (Member a: members)
                        if (a.getName().equalsIgnoreCase(name))
                            foundMembers.add(a);
                    if (foundMembers.isEmpty()){
                        System.out.println("\t Il n'y a aucun adherent trouver avec ce nom...");
                        pause.pause(500);
                        break;
                    }
                    return;
                case "3":
                    System.out.println("\t Entrer PRENOM adherent");
                    firstName = sc.nextLine();
                    for (Member a: members)
                        if (a.getFirstName().equalsIgnoreCase(firstName))
                            foundMembers.add(a);
                    if (foundMembers.isEmpty()){
                        System.out.println("\t Il n'y a aucun adherent trouver avec ce prenom...");
                        pause.pause(500);
                        break;
                    }
                    return;
                case "4":
                    System.out.println("\t Entrer ADRESSE adherent");
                    address = sc.nextLine();
                    for (Member a: members)
                        if (a.getAddress().equalsIgnoreCase(address))
                            foundMembers.add(a);
                    if (foundMembers.isEmpty()){
                        System.out.println("\t Il n'y a aucun adherent trouver avec cette adresse...");
                        pause.pause(500);
                        break;
                    }
                    return;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause.pause(500);
                    break;
            }
        }while (true);
    }

    public void findDocumentByIdOrTitle(ArrayList<Document> foundDocuments){
        int id;
        String choix, title;
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Chercher Document par ID");
            System.out.println("\t 2 ) Chercher Document par Titre");
            System.out.println("\t Veillez choisir une option : ");
            choix = sc.nextLine();
            switch (choix){
                case "0": documentManagement(); return;
                case "1":
                    System.out.println("\t Entrer ID : ");
                    id = checkId();
                    sc.nextLine();
                    for (Document d: documents)
                        if (d.getId() == id){
                            foundDocuments.add(d);
                            return;
                        }
                    System.out.println("\t Document introuvable! veillez réssayer");
                    pause.pause(500);
                    break;
                case "2":
                    System.out.println("\t Entrer Titre : ");
                    title = sc.nextLine();
                    for (Document d: documents)
                        if (d.getTitle().equalsIgnoreCase(title))
                            foundDocuments.add(d);
                    if (foundDocuments.isEmpty()){
                        System.out.println("\t Il n'y a aucun Document trouver avec ce titre...");
                        pause.pause(500);
                        break;
                    }
                    return;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause.pause(500);
                    break;
            }
        }while (true);
    }

    public Document searchDocumentById(){
        int id;
        String choice;
        Document document = new Article();
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Lister les documents disponible");
            System.out.println("\t 2 ) Entree ID document");
            System.out.println("\t Veillez choisir une option : ");
            choice = sc.nextLine();
            switch (choice){
                case "0": documentManagement(); break;
                case "1": displayDocument(); break;
                case "2":
                    System.out.println("\t Entrer l'ID du document : ");
                    id = checkId();
                    sc.nextLine();
                    for (Document d: documents){
                        if (d.getId() == id)
                            return d;
                    }
                    System.out.println("\t Document introuvable! veillez réssayer");
                    pause.pause(500);
                    break;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause.pause(500);
                    break;
            }
        }while (!choice.equals("0"));
        return document;
    }

    public Document searchDocumentById2(Member a){
        int id, nb=0;
        String choice;
        Document document = new Article();
        boolean found = false;
        do {
            System.out.println("\t 0 ) Annuler");
            System.out.println("\t 1 ) Lister les documents disponible pour " + a.getFirstName() + " " + a.getName());
            System.out.println("\t 2 ) Entree ID document");
            System.out.println("\t Veillez choisir une option : ");
            choice = sc.nextLine();
            switch (choice){
                case "0": borrowManagement(); break;
                case "1":
                    documentHeader();
                    for (Document d: documents)
                        for (BorrowDocument borrowDocument:d.getBorrowList())
                            if (borrowDocument.getMember() == a){
                                int available = d.getNumberCopies() - d.getBorrowList().size();
                                d.displayDocument(available, totalBorrowNumber);
                                documentFrame();
                                found = true;
                                nb++;
                            }
                    if (!found)
                        System.out.println("\t Cette adherent n'a pas de document en cours de pret");
                    else
                        System.out.println("\t " + nb + " Document preté pour " + a.getFirstName() + " " + a.getName());
                    pause.pause(500);
                    break;
                case "2":
                    System.out.println("\t Entrer l'ID du document : ");
                    id = checkId();
                    sc.nextLine();
                    for (Document d: documents){
                        if (d.getId() == id)
                            return d;
                    }
                    System.out.println("\t Document introuvable! veillez réssayer");
                    pause.pause(500);
                    break;
                default:
                    System.out.println("\t Choix invalide! veillez réessayer...");
                    pause.pause(500);
                    break;
            }
        }while (!choice.equals("0"));
        return document;
    }

    public int checkId() {
        int id;
        do {
            try{
                id = sc.nextInt();
                return id;
            } catch (Exception e){
                System.out.println("\t Erreur! Veillez entrer des chiffres...");
                System.out.println("\t Entrer l'ID une autre fois : ");
                pause.pause(500);
                sc.nextLine();
            }
        }while (true);
    }

    public void documentFilling(Document d){
        d.setId(documentId);
        System.out.println("\t Entrer le titre");
        d.setTitle(sc.nextLine());
        System.out.println("\t Entrer localisation (Salle/Rayon)");
        d.setLocation(sc.nextLine());
        System.out.println("\t Entrer nombre exemplaires");
        boolean verified = false;
        do {
            try{
                d.setNumberCopies(sc.nextInt());
                verified = true;
            } catch (Exception e){
                System.out.println("\t Erreur! Veillez entrer des chiffres...");
                System.out.println("\t Entrer nombre exemplaires");
                pause.pause(500);
                sc.nextLine();
            }
        }while (!verified);
        sc.nextLine();
    }

    public void memberFrame(){
        System.out.print("+-----------");
        System.out.print("+----");
        for (int i=0; i<2; i++)
            System.out.print("+--------------------");
        System.out.print("+------------------------------");
        for (int i=0; i<2; i++)
            System.out.print("+--------");
        System.out.print("+-------------");
        System.out.println("+");
    }

    public void memberHeader(){
        memberFrame();
        System.out.println("|   "+ Y +"TYPE"+ RES +"    | "+ Y +"ID"+ RES +
                " |        "+ Y +"NOM"+ RES +"         |       "+ Y +"PRENOM"+ RES +
                "       |           "+ Y +"ADRESSE"+ RES +"            |" +
                " "+ Y +"PRETS"+ RES +"  | "+ Y +"RETARD"+ RES +" | "+ Y +"PRET TOTALE"+ RES +" |");
        memberFrame();
    }

    public void documentFrame(){
        System.out.print("+---------");
        System.out.print("+----");
        System.out.print("+------------------------------------");
        for (int i=0; i<2; i++)
            System.out.print("+--------------------");
        System.out.print("+----------");
        for (int i=0; i<4; i++)
            System.out.print("+------------");
        System.out.print("+-------------");
        System.out.print("+------");
        System.out.println("+");
    }

    public void documentHeader(){
        documentFrame();
        System.out.println("|  "+ Y +"TYPE"+ RES +"   | "+ Y +"ID"+ RES +" |               "+ Y +
                "TITRE"+ RES +"                |     "+ Y +"Nom Auteur"+ RES +"     |    "+ Y +"NOM EDITEUR"+ RES +
                "     |   "+ Y +"DATE"+ RES +"   |  "+ Y +"FREQUENCE"+ RES +" |"+ Y +"LOCALISATION"+ RES +
                "| "+ Y +"EXEMPLAIRES"+ RES +"| "+ Y +"DISPONIBLE"+ RES +" | "+ Y +"PRET TOTALE"+ RES +" |  "+ Y +"%"+ RES +"   |");
        documentFrame();
    }

    public void borrowFrame(){
        System.out.print("+---------");
        for (int i=0; i<2; i++)
            System.out.print("+----------------------------------------");
        for (int i=0; i<3; i++)
            System.out.print("+-------------");
        System.out.println("+");
    }

    public void borrowHeader(){
        borrowFrame();
        System.out.println("| "+Y+"TYPE DOC"+RES+"|                 "+Y+"TITRE"+RES+"                  |                "+Y+
                "ADHERENT"+RES+"                |   "+Y+"TYPE AD"+RES+"   |    "+Y+"PRIS LE"+RES+
                "  | "+Y+"A RENDRE LE"+RES+" |");
        borrowFrame();
    }

    public void historyHeader(){
        System.out.println("+-------------------------+--------+------+---------------+--------------------------------------------------------------------------------+");
        System.out.println("|          "+Y+"DATE"+RES+"           |  "+Y+"CLASS"+RES+" |  "+Y+"ID"+RES+"  |      "+Y+
                "TYPE"+RES+"     |                                    "+Y+"INFO"+RES+"                                        |");
        System.out.println("+-------------------------+--------+------+---------------+--------------------------------------------------------------------------------+");
    }

    public void seeds(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        AJOUT AUTOMATIQUE DE 5 ADHERENT
        Member a1 = new Member(95, "slama", "khairi", "Borjine, sousse", "Etudiant", 2, 7);
        Member a2 = new Member(96, "slama", "khalil", "Borjine, sousse", "Visiteur", 1, 7);
        Member a3 = new Member(97, "zaatir", "Mounir", "Bohsina, sousse", "Enseignant", 4, 21);
        Member a4 = new Member(98, "rouis", "houssem", "M'saken, sousse", "Etudiant", 2, 7);
        Member a5 = new Member(99, "smida", "ghada", "Chott mariem, sousse", "Etudiant", 2, 7);
        members.add(a1);
        members.add(a2);
        members.add(a3);
        members.add(a4);
        members.add(a5);
//        AJOUT AUTOMATIQUE DE 5 DOCUMENTS
        Document d1 = new Article(95, "Le corona-virus", "S1/F100", 1, "Information Center", new Date());
        Document d2 = new Article(96, "La nature", "S1/F321", 1, "Nat Geo", new Date());
        Document d3 = new Book(97, "Harry Potter livre 1", "S1/L112", 2, "J.K Rowling", "Gallimard Jeunesse", new Date());
        Document d4 = new Book(98, "Le seigneur des anneaux", "S1/L538", 2, "J.R.R Tolkien", "Allen & Unwin", new Date());
        Document d5 = new Magazine(99, "Elle", "S1/M019", 1, 500);
        documents.add(d1);
        documents.add(d2);
        documents.add(d3);
        documents.add(d4);
        documents.add(d5);
//        AJOUT DE PRETS EN RETARD D'ARTICLE 1 AU 5EME ADHERENT
        BorrowDocument borrowedDocument = new BorrowDocument();
        borrowedDocument.setMember(a5);
        try{
            borrowedDocument.setBorrowingDate(sdf.parse("11/6/2020"));
        }catch (ParseException e){
            System.out.println("rien");
        }
        borrowedDocument.calculateReturnDate();
        d1.getBorrowList().add(borrowedDocument);
        a5.setBorrowNumber(1);
        a5.setBorrowNumberTotal(3);
        totalBorrowNumber++;
        d1.setNumberTotalBorrow(1);
//        ON INITIALISE LE 4EME ADHERENT RETARDATAIRE PAR DEFAUT
        a4.setLate(true);
        a4.setBorrowNumberTotal(1);
//        ON AJOUT UN PREDOC A L'ADHERENT 1 ET DOC 3
        BorrowDocument pretDocument2 = new BorrowDocument();
        pretDocument2.setMember(a1);
        try{
            pretDocument2.setBorrowingDate(sdf.parse("24/6/2020"));
        }catch (ParseException e){
            System.out.println("rien");
        }
        pretDocument2.calculateReturnDate();
        d3.getBorrowList().add(pretDocument2);
        a1.setBorrowNumber(1);
        a1.setBorrowNumberTotal(12);
        d3.setNumberTotalBorrow(1);
        totalBorrowNumber++;
    }
}
