package Knihy;

public class Kniha {
    private String nazev;
    private String autor;
    private int rokVydani;
    private boolean dostupnost;
    private String typKnihy;


    public Kniha(String nazev, String autor, int rokVydani, boolean dostupnost, String typKnihy) {
        this.nazev = nazev;
        this.autor = autor;
        this.rokVydani = rokVydani;
        this.dostupnost = dostupnost;
        this.typKnihy = typKnihy;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getRokVydani() {
        return rokVydani;
    }

    public boolean isDostupnost() {
        return dostupnost;
    }

    public void setDostupnost(boolean dostupnost) {
        this.dostupnost = dostupnost;
    }
    
    public String getTypKnihy() {
        return typKnihy;
    }

    public void setTypKnihy(String typKnihy) {
        this.typKnihy = typKnihy;
    }
}
