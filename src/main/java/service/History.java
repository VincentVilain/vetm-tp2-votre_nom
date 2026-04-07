package service;

import java.text.SimpleDateFormat;
import java.util.Date;

public class History {

    private String info;
    private String classe;
    private String type;
    int id;
    private Date addedDate;

    public History(Date dateAjout, String classe, String type, int id , String info) {
        this.info = info;
        this.classe = classe;
        this.type = type;
        this.id = id;
        this.addedDate = dateAjout;
    }

    public String getInfo() {
        return info;
    }

    public String getType() {
        return type;
    }

    public String getClasse() {
        return classe;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public int getId() { return id; }

    public void afficherCetHistorique(){
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy k:m:s");
        System.out.printf("|%25s", sdf.format(getAddedDate()));
        System.out.printf("|%8s", getClasse());
        System.out.printf("|%6d", getId());
        System.out.printf("|%15s", getType());
        System.out.printf("|%80s", getInfo());
        System.out.println("|\n+-------------------------+--------+------+---------------+--------------------------------------------------------------------------------+");
    }
}
