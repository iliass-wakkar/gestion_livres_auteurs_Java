package Modules;

public class Livre {
    private int id;
    private String titre;
    private int id_auteur;

    public Livre(int id, String titre, int id_auteur) {
        this.id = id;
        this.titre = titre;
        this.id_auteur = id_auteur;
    }

    public Livre(String titre, int id_auteur) {
        this.titre = titre;
        this.id_auteur = id_auteur;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getId_auteur() {
        return id_auteur;
    }

    public void setId_auteur(int id_auteur) {
        this.id_auteur = id_auteur;
    }
}
