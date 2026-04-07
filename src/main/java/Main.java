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
//        modification d'un adherent en pret, test si retard ou si il ne peut plus prendre
//        modification d'un document en pret, test si document existe
//        teste saisie sur tout les "int" et les "date"
//        affiche que des document de l'adhérent souhaiter dans rendre pret ou modifier pret
//        Affichage sous forme de tableau et avec des couleurs en utilisant le codage ANSI
//        id_adherent et id_document auto_increment
//        avant suppression d'un adherent il faut qu'il rend tout ces document
//        fonction init au lancement du programme pour chercher les adherents retard
//        renouvellement d'un adherent retard s'effectue que s'il a rendu ces pret
//        confirmation de suppression pour adherent && document et affiche message d'alert si document en cours de pret
//        recharche par nom ou prenom ou adresse et ignore la majuscule et minuscule
//        table historique avec un affichage de la derniaire modification et avec 10 par foie
//        CHERCHER DANS HISTORIQUE PAR ID OU NOM DE CLASSE OU TYPE OU DATE
//        Member : nombre des emprunts effectués, le nombre des emprunts en cours et le nombre des emprunts dépassés.
//        Document : Nombre effectuer pour chaque document et statestique (%) par rapport a la demande
//        recherche document et afficher ca liste pret