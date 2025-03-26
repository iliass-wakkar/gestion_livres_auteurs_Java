package Modules;

import java.util.ArrayList;
import java.util.List;

public class Acteur {
private  int id;
private String nom;
private String nationalite;
private ArrayList<Livre> livres = null;

    public Acteur(int id, String nom, String nationalite) {
        this.id = id;
        this.nom = nom;
        this.nationalite = nationalite;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }
}
