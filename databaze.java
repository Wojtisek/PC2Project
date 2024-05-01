package Knihy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class databaze {
    private Scanner sc;
    private Kniha[] prvkyDatabaze;
    private int posledniKniha;

    public databaze() {
        prvkyDatabaze = new Kniha[10];
        sc = new Scanner(System.in);
    }

    public void setKniha(Scanner sc) {
        System.out.println("Vyberte typ knihy:");
        System.out.println("1 .. Román");
        System.out.println("2 .. Učebnica");
        int volbaTypu = pouzeCelaCisla(sc);

        String typKnihy = "";
        switch (volbaTypu) {
            case 1:
                typKnihy = vyberTypRomanu(sc);
                break;
            case 2:
                typKnihy = vyberTypUcebnice(sc);
                break;
            default:
                System.out.println("Neplatná voľba typu knihy.");
                return;
        }
        sc.nextLine();
        System.out.println("Zadajte názov knihy:");
        String nazov = sc.nextLine();

        System.out.println("Zadajte autora knihy:");
        String autor = sc.nextLine();

        System.out.println("Zadajte rok vydania knihy:");
        int rokVydania = pouzeCelaCisla(sc);

        boolean dostupnost = zadatDostupnost(sc);
        		
        sc.nextLine();
        prvkyDatabaze[posledniKniha++] = new Kniha(nazov, autor, rokVydania, dostupnost, typKnihy);
    }

    private String vyberTypRomanu(Scanner sc) {
        System.out.println("Vyberte typ románu:");
        System.out.println("1 .. Historický");
        System.out.println("2 .. Fiktívny");
        System.out.println("3 .. Psychologický");
        System.out.println("4 .. Pre mládež");
        System.out.println("5 .. Iné");
        int volba = pouzeCelaCisla(sc);
        switch (volba) {
            case 1:
                return "Historický";
            case 2:
                return "Fiktívny";
            case 3:
                return "Psychologický";
            case 4:
                return "Pre mládež";
            case 5:
                return "Iné";
            default:
                System.out.println("Neplatná voľba typu románu.");
                return "";
        }
    }

    private String vyberTypUcebnice(Scanner sc){
        System.out.println("Zadajte ročník učebnice Vysokej školy:");
        System.out.println("1 .. 1.rocnik");
        System.out.println("2 .. 2.rocnik");
        System.out.println("3 .. 3.rocnik");
        System.out.println("4 .. 4.rocnik");
        System.out.println("5 .. 5.rocnik");
        int rocnik = pouzeCelaCisla(sc);
        switch (rocnik) {
	        case 1:
	            return "1.rocnik";
	        case 2:
	            return "2.rocnik";
	        case 3:
	            return "3.rocnik";
	        case 4:
	            return "4.rocnik";
	        case 5:
	            return "5.rocnik";
	        default:
	            System.out.println("Neplatná voľba typu románu.");
	            return"";
        }
    }
    
    public Kniha najdiKnihaPodleNazvu(String nazev) {
        for (Kniha kniha : prvkyDatabaze) {
            if (kniha != null && kniha.getNazev().equalsIgnoreCase(nazev)) {
                return kniha;
            }
        }
        return null;
    }

    public void upravitKniha(Scanner sc) {
        sc.nextLine();
        System.out.println("Zadejte název knihy, kterou chcete upravit:");
        String nazev = sc.nextLine();
        Kniha nalezenaKniha = najdiKnihaPodleNazvu(nazev);
        if (nalezenaKniha != null) {
            System.out.println("Kniha nalezena:");
            System.out.println("Název: " + nalezenaKniha.getNazev());
            System.out.println("Autor: " + nalezenaKniha.getAutor());
            System.out.println("Rok vydání: " + nalezenaKniha.getRokVydani());
            System.out.println("Typ knihy: " + nalezenaKniha.getTypKnihy());
            System.out.println("Dostupnost: " + (nalezenaKniha.isDostupnost() ? "K dispozici" : "Půjčená"));
            System.out.println("Zvolte, co chcete upravit:");
            System.out.println("1 .. Název knihy");
            System.out.println("2 .. Autor knihy");
            System.out.println("3 .. Dostupnost knihy");
            int volba = pouzeCelaCisla(sc);
            switch (volba) {
                case 1:
                    System.out.println("Zadejte nový název knihy:");
                    sc.nextLine();
                    String novyNazev = sc.nextLine();
                    nalezenaKniha.setNazev(novyNazev);
                    System.out.println("Název knihy byl úspěšně změněn na: " + novyNazev);
                    break;
                case 2:
                    System.out.println("Zadejte nového autora knihy:");
                    sc.nextLine();
                    String novyAutor = sc.nextLine();
                    nalezenaKniha.setAutor(novyAutor);
                    System.out.println("Autor knihy byl úspěšně změněn na: " + novyAutor);
                    break;
                case 3:
                    boolean dostupnost = zadatDostupnost(sc);
                    nalezenaKniha.setDostupnost(dostupnost);
                    System.out.println("Dostupnost knihy byla úspěšně změněna.");
                    break;
                default:
                    System.out.println("Neplatná volba.");
            }
        } else {
            System.out.println("Kniha s názvem '" + nazev + "' nebyla nalezena.");
        }
    }

    public void smazatKniha(Scanner sc) {
        sc.nextLine();
        System.out.println("Zadejte název knihy, kterou chcete smazat:");
        String nazev = sc.nextLine();
        Kniha nalezenaKniha = najdiKnihaPodleNazvu(nazev);
        if (nalezenaKniha != null) {
            System.out.println("Nalezena kniha:");
            System.out.println("Název: " + nalezenaKniha.getNazev());
            System.out.println("Autor: " + nalezenaKniha.getAutor());
            System.out.println("Rok vydání: " + nalezenaKniha.getRokVydani());
            System.out.println("Typ knihy: " + nalezenaKniha.getTypKnihy());
            System.out.println("Dostupnost: " + (nalezenaKniha.isDostupnost() ? "K dispozici" : "Půjčená"));

            System.out.println("Opravdu chcete tuto knihu smazat? (ano/ne)");
            String potvrzeni = sc.nextLine();
            if (potvrzeni.equalsIgnoreCase("ano")) {
                System.out.println("Kniha byla úspěšně smazána.");
            } else {
                System.out.println("Operace smazání knihy zrušena.");
            }
        } else {
            System.out.println("Kniha s názvem '" + nazev + "' nebyla nalezena.");
        }
    }
    
    public void zmenaDostupnosti(Scanner sc) {
    	 sc.nextLine();
         System.out.println("Zadejte název knihy pro zmenu dostupnosti:");
         String nazev = sc.nextLine();
         Kniha nalezenaKniha = najdiKnihaPodleNazvu(nazev);
         if (nalezenaKniha != null) {
        	 System.out.println("Název: " + nalezenaKniha.getNazev());
        	 System.out.println("Dostupnost: " + (nalezenaKniha.isDostupnost() ? "K dispozici" : "Půjčená"));
        	 boolean dostupnost = zadatDostupnost(sc);
             nalezenaKniha.setDostupnost(dostupnost);
             System.out.println("Dostupnost knihy byla úspěšně změněna.");
         } else {
             System.out.println("Kniha s názvem '" + nazev + "' nebyla nalezena.");
         }
     }

    
    public void vypisKnihy() {
    	List<Kniha> seznamKnih = new ArrayList<>();
        for (Kniha kniha : prvkyDatabaze) {
            if (kniha != null) {
                seznamKnih.add(kniha);
            }
        }
        Collections.sort(seznamKnih, (kniha1, kniha2) -> kniha1.getNazev().compareToIgnoreCase(kniha2.getNazev()));
        System.out.println("Seznam knih v abecedním pořadí:");
        for (Kniha kniha : seznamKnih) {
            System.out.println();
            System.out.println("Název: " + kniha.getNazev());
            System.out.println("Autor: " + kniha.getAutor());
            System.out.println("Rok vydání: " + kniha.getRokVydani());
            System.out.println("Typ knihy: " + kniha.getTypKnihy());
            System.out.println("Dostupnost: " + (kniha.isDostupnost() ? "K dispozici" : "Půjčená"));
            System.out.println();
        }
    }
    
    public void vyhladanieKnihy() {
         System.out.println("Zadejte název knihy, kterou chcete vzhladat:");
         String nazev = sc.nextLine();
         Kniha nalezenaKniha = najdiKnihaPodleNazvu(nazev);
         if (nalezenaKniha != null) {
             System.out.println("Nalezena kniha:");
             System.out.println("Název: " + nalezenaKniha.getNazev());
             System.out.println("Autor: " + nalezenaKniha.getAutor());
             System.out.println("Rok vydání: " + nalezenaKniha.getRokVydani());
             System.out.println("Typ knihy: " + nalezenaKniha.getTypKnihy());
             System.out.println("Dostupnost: " + (nalezenaKniha.isDostupnost() ? "K dispozici" : "Půjčená"));
         } else {
             System.out.println("Kniha s názvem '" + nazev + "' nebyla nalezena.");
         }  
    }
    
    
    public void vypisPodlaAutora(){
    	
    }
    

    public int pouzeCelaCisla(Scanner sc) {
        int cislo = 0;
        try {
            cislo = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Nastala výnimka typu " + e.toString());
            System.out.println("Zadajte prosím celé číslo ");
            sc.nextLine();
            cislo = pouzeCelaCisla(sc);
        }
        return cislo;
        
    }
    
    private boolean zadatDostupnost(Scanner sc) {
        boolean dostupnost = false;
        boolean opakovat = true;
        while (opakovat) {
            System.out.println("Zadajte dostupnost knihy:");
            System.out.println("1 .. K dispozícií");
            System.out.println("2 .. Požičané");
            int volba = pouzeCelaCisla(sc);
            switch (volba) {
                case 1:
                    dostupnost = true;
                    opakovat = false;
                    break;
                case 2:
                    dostupnost = false;
                    opakovat = false;
                    break;
                default:
                    System.out.println("Neplatná volba dostupnosti. Zkuste to znovu.");
            }
        }
        return dostupnost;
    }
}
