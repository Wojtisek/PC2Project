package Knihy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



public class databaze {
	private static final String DB_URL = "jdbc:sqlite:databazaSQLite.db";
    private Scanner sc;
    private Kniha[] prvkyDatabaze;
    private int posledniKniha;

    public databaze() {
        prvkyDatabaze = new Kniha[10];
        sc = new Scanner(System.in);
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private void vytvoritTabulku() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS knihy (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nazov TEXT," +
                "autor TEXT," +
                "rok_vydania INTEGER," +
                "dostupnost BOOLEAN," +
                "typ_knihy TEXT)";
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
        } catch (SQLException e) {
            System.out.println("Chyba pri vytvareni tabulky knihy: " + e.getMessage());
        }
    }
    
    public void exportToSQLite() {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
        	
            String deleteQuery = "DELETE FROM knihy";
            statement.executeUpdate(deleteQuery);

            for (Kniha kniha : prvkyDatabaze) {
                String insertQuery = String.format("INSERT INTO knihy (nazov, autor, rok_vydania, dostupnost, typ_knihy) VALUES ('%s', '%s', %d, %b, '%s')",
                        kniha.getNazev(), kniha.getAutor(), kniha.getRokVydani(), kniha.isDostupnost(), kniha.getTypKnihy());
                statement.executeUpdate(insertQuery);
            }
            System.out.println("Udaje byly uspesne exportovane do SQLite databaze");
        } catch (SQLException e) {
            System.out.println("Chyba pri exportovani udaju do SQLite databaze: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("");
        }
    }

    public void importFromSQLite() {
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            String selectQuery = "SELECT * FROM knihy";
            var resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String nazov = resultSet.getString("nazav");
                String autor = resultSet.getString("autor");
                int rokVydania = resultSet.getInt("rok_vydani");
                boolean dostupnost = resultSet.getBoolean("dostupnost");
                String typKnihy = resultSet.getString("typ_knihy");

                prvkyDatabaze[posledniKniha++] = new Kniha(nazov, autor, rokVydania, dostupnost, typKnihy);
            }
            System.out.println("Udaje byly uspesne nactene z SQL databaze\n");
        } catch (SQLException e) {
            System.out.println("Chyba pri nacteni udaju z SQLite databaze: " + e.getMessage());
        }
    }

   public void setKniha(Scanner sc) {
        int volbaTypu;
        String typKnihy = "";

        do {
            System.out.println("Vyberte typ knihy:");
            System.out.println("1 .. Roman");
            System.out.println("2 .. Ucebnice");
            volbaTypu = pouzeCelaCisla(sc);

            switch (volbaTypu) {
                case 1:
                    typKnihy = vyberTypRomanu(sc);
                    break;
                case 2:
                    typKnihy = vyberTypUcebnice(sc);
                    break;
                default:
                    System.out.println("Neplatna volba typu knihy. Zadejte prosim znovu.");
                    break;
            }
        } while (volbaTypu != 1 && volbaTypu != 2);

       
        sc.nextLine();
        System.out.println("Zadajte nazev knihy:");
        String nazov = sc.nextLine();

        System.out.println("Zadajte autora knihy:");
        String autor = sc.nextLine();

        System.out.println("Zadajte rok vydani knihy:");
        int rokVydania = pouzeCelaCisla(sc);

        boolean dostupnost = zadatDostupnost(sc);
        		
        sc.nextLine();
        prvkyDatabaze[posledniKniha++] = new Kniha(nazov, autor, rokVydania, dostupnost, typKnihy);
    }

    private String vyberTypRomanu(Scanner sc) {
        System.out.println("Vyberte typ romanu:");
        System.out.println("1 .. Historicky");
        System.out.println("2 .. Fiktivny");
        System.out.println("3 .. Psychologicky");
        System.out.println("4 .. Pre mladez");
        System.out.println("5 .. Jine");
        int volba = pouzeCelaCisla(sc);
        switch (volba) {
            case 1:
                return "Historicky";
            case 2:
                return "Fiktivny";
            case 3:
                return "Psychologicky";
            case 4:
                return "Pro mladez";
            case 5:
                return "Jine";
            default:
                System.out.println("Neplatna volba typu romanu. Zadejte prosim znovu.");
                return vyberTypRomanu(sc); 
        }
    }

    private String vyberTypUcebnice(Scanner sc){
        System.out.println("Zadajte rocnik ucebnice Vysoke skoly:");
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
                System.out.println("Neplatna volba typu ucebnice. Zadejte prosim znovu.");
                return vyberTypUcebnice(sc); 
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
        System.out.println("Zadejte nazev knihy, kterou chcete upravit:");
        String nazev = sc.nextLine();
        Kniha nalezenaKniha = najdiKnihaPodleNazvu(nazev);
        if (nalezenaKniha != null) {
            System.out.println("Kniha nalezena:");
            System.out.println("Nazev: " + nalezenaKniha.getNazev());
            System.out.println("Autor: " + nalezenaKniha.getAutor());
            System.out.println("Rok vydani: " + nalezenaKniha.getRokVydani());
            System.out.println("Typ knihy: " + nalezenaKniha.getTypKnihy());
            System.out.println("Dostupnost: " + (nalezenaKniha.isDostupnost() ? "K dispozici" : "Pujcena"));
            System.out.println("Zvolte, co chcete upravit:");
            System.out.println("1 .. Nazev knihy");
            System.out.println("2 .. Autor knihy");
            System.out.println("3 .. Dostupnost knihy");
            int volba = pouzeCelaCisla(sc);
            switch (volba) {
                case 1:
                    System.out.println("Zadejte novy nazev knihy:");
                    sc.nextLine();
                    String novyNazev = sc.nextLine();
                    nalezenaKniha.setNazev(novyNazev);
                    System.out.println("Nazev knihy byl uspesne zmenen na: " + novyNazev);
                    break;
                case 2:
                    System.out.println("Zadejte noveho autora knihy:");
                    sc.nextLine();
                    String novyAutor = sc.nextLine();
                    nalezenaKniha.setAutor(novyAutor);
                    System.out.println("Autor knihy byl uspesne zmenen na: " + novyAutor);
                    break;
                case 3:
                    boolean dostupnost = zadatDostupnost(sc);
                    nalezenaKniha.setDostupnost(dostupnost);
                    System.out.println("Dostupnost knihy byla uspesne zmenena.");
                    break;
                default:
                    System.out.println("Neplatna volba.");
            }
        } else {
            System.out.println("Kniha s nazvem '" + nazev + "' nebyla nalezena.");
        }
    }

    public void smazatKniha(Scanner sc) {
        sc.nextLine();
        System.out.println("Zadejte nazev knihy, kterou chcete smazat:");
        String nazev = sc.nextLine();
        Kniha nalezenaKniha = najdiKnihaPodleNazvu(nazev);
        if (nalezenaKniha != null) {
            System.out.println("Nalezena kniha:");
            System.out.println("Nazev: " + nalezenaKniha.getNazev());
            System.out.println("Autor: " + nalezenaKniha.getAutor());
            System.out.println("Rok vydani: " + nalezenaKniha.getRokVydani());
            System.out.println("Typ knihy: " + nalezenaKniha.getTypKnihy());
            System.out.println("Dostupnost: " + (nalezenaKniha.isDostupnost() ? "K dispozici" : "Pujcena"));

            System.out.println("Opravdu chcete tuto knihu smazat? (ano/ne)");
            String potvrzeni = sc.nextLine();
            if (potvrzeni.equalsIgnoreCase("ano")) {
                for (int i = 0; i < prvkyDatabaze.length; i++) {
                    if (prvkyDatabaze[i] == nalezenaKniha) {
                        for (int j = i; j < posledniKniha - 1; j++) {
                            prvkyDatabaze[j] = prvkyDatabaze[j + 1];
                        }
                        prvkyDatabaze[posledniKniha - 1] = null;
                        posledniKniha--;
                        System.out.println("Kniha byla uspesne smazana.");
                        return;
                    }
                }
            } else {
                System.out.println("Operace smazazani knihy zrusena.");
            }
        } else {
            System.out.println("Kniha s nazvem '" + nazev + "' nebyla nalezena.");
        }
    }
    
    public void zmenaDostupnosti(Scanner sc) {
    	 sc.nextLine();
         System.out.println("Zadejte nazev knihy pro zmenu dostupnosti:");
         String nazev = sc.nextLine();
         Kniha nalezenaKniha = najdiKnihaPodleNazvu(nazev);
         if (nalezenaKniha != null) {
        	 System.out.println("Nazev: " + nalezenaKniha.getNazev());
        	 System.out.println("Dostupnost: " + (nalezenaKniha.isDostupnost() ? "K dispozici" : "Pujcena"));
        	 boolean dostupnost = zadatDostupnost(sc);
             nalezenaKniha.setDostupnost(dostupnost);
             System.out.println("Dostupnost knihy byla uspesne zmenena.");
         } else {
             System.out.println("Kniha s nazvem '" + nazev + "' nebyla nalezena.");
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
        System.out.println("Seznam knih v abecednim poradi:");
        for (Kniha kniha : seznamKnih) {
            System.out.println();
            System.out.println("Nazev: " + kniha.getNazev());
            System.out.println("Autor: " + kniha.getAutor());
            System.out.println("Rok vydani: " + kniha.getRokVydani());
            System.out.println("Typ knihy: " + kniha.getTypKnihy());
            System.out.println("Dostupnost: " + (kniha.isDostupnost() ? "K dispozici" : "Pujcena"));
            System.out.println();
        }
    }
    
    public void vyhladanieKnihy() {
         System.out.println("Zadejte nazev knihy, kterou chcete vyhledat:");
         String nazev = sc.nextLine();
         Kniha nalezenaKniha = najdiKnihaPodleNazvu(nazev);
         if (nalezenaKniha != null) {
             System.out.println("Nalezena kniha:");
             System.out.println("Nazev: " + nalezenaKniha.getNazev());
             System.out.println("Autor: " + nalezenaKniha.getAutor());
             System.out.println("Rok vydani: " + nalezenaKniha.getRokVydani());
             System.out.println("Typ knihy: " + nalezenaKniha.getTypKnihy());
             System.out.println("Dostupnost: " + (nalezenaKniha.isDostupnost() ? "K dispozici" : "Pujcena"));
         } else {
             System.out.println("Kniha s nazvem '" + nazev + "' nebyla nalezena.");
         }  
    }
    
    
    public void vypisPodlaAutora(Scanner sc) {
        	sc.nextLine();
	    	System.out.println("Zadejte autora, ktereho knihy chcete vypsat:");
	        String autor = sc.nextLine();
            List<Kniha> knihyAutora = new ArrayList<>();
            for (Kniha kniha : prvkyDatabaze) {
                if (kniha != null && kniha.getAutor().equalsIgnoreCase(autor)) {
                    knihyAutora.add(kniha);
                }
            }
            Collections.sort(knihyAutora, (kniha1, kniha2) -> kniha1.getRokVydani() - kniha2.getRokVydani());
            System.out.println("Knihy autora " + autor + " v chronologickem poradi:");
            for (Kniha kniha : knihyAutora) {
                System.out.println("Nazev: " + kniha.getNazev());
                System.out.println("Rok vydani: " + kniha.getRokVydani());
                System.out.println("Typ knihy: " + kniha.getTypKnihy());
                System.out.println("Dostupnost: " + (kniha.isDostupnost() ? "K dispozici" : "Pujcena"));
                System.out.println();
            }
        }
    
    
    public Object vypisPodleTypuKnihy(Scanner sc) {
        System.out.println("Podle akeho typu knihy?");
        System.out.println("1. Roman:");
        System.out.println("2. Ucebnica:");

        int volbaTypu = pouzeCelaCisla(sc);

        List<Kniha> knihyPodleTypu = new ArrayList<>();
        String typRomanu = null;
		String typUcebnice = null;
		
		switch (volbaTypu) {
            case 1:
            	typRomanu = vyberTypRomanu(sc);
                for (Kniha kniha : prvkyDatabaze) {
                    if (kniha != null && kniha.getTypKnihy().equalsIgnoreCase(typRomanu)) {
                        knihyPodleTypu.add(kniha);
                    }
                }
                break;
            case 2:
            	typUcebnice = vyberTypUcebnice(sc);
                for (Kniha kniha : prvkyDatabaze) {
                    if (kniha != null && kniha.getTypKnihy().equalsIgnoreCase(typUcebnice)) {
                        knihyPodleTypu.add(kniha);
                    }
                }
                break;
            default:
                System.out.println("Neplatna volba typu knihy.");
                return vypisPodleTypuKnihy( sc);
        }

        if (!knihyPodleTypu.isEmpty()) {
            System.out.println("Knihy patrici do kategorie " + (volbaTypu == 1 ? typRomanu : typUcebnice) + ":");
            for (Kniha kniha : knihyPodleTypu) {
                System.out.println("Nazev: " + kniha.getNazev());
                System.out.println("Autor: " + kniha.getAutor());
                System.out.println("Rok vydani: " + kniha.getRokVydani());
                System.out.println("Dostupnost: " + (kniha.isDostupnost() ? "K dispozici" : "Pujcena"));
                System.out.println();
            }
        } else {
            System.out.println("Zadne knihy patrici do kategorie " + (volbaTypu == 1 ? typRomanu : typUcebnice) + " nebyly nalezeny.");
        }
		return typUcebnice;
    }
    
    
    public void vypisVypujcenychKnih() {
        boolean existujiVypujceneKnihy = false;

        for (Kniha kniha : prvkyDatabaze) {
            if (kniha != null && !kniha.isDostupnost()) {
                existujiVypujceneKnihy = true;
                System.out.println("Nazev: " + kniha.getNazev());
                System.out.println("Typ knihy: " + kniha.getTypKnihy());
                System.out.println();
            }
        }

        if (!existujiVypujceneKnihy) {
            System.out.println("Momentalne nejsou pujcene zadne knihy.");
        }
    }
    
    public void ulozenieDoSuboru(String nazovKnihy) {
        try {
            // Požiadaj používateľa o názov súboru
            System.out.println("Zadajte nazev souboru pro ulozeni udaju:");
            String nazovSuboru = sc.nextLine();
            FileWriter writer = new FileWriter(nazovSuboru);

            for (Kniha kniha : prvkyDatabaze) {
                if (kniha != null && kniha.getNazev().equalsIgnoreCase(nazovKnihy)) {
                    // Zapíšte údaje o knihe do súboru
                    writer.write(kniha.getNazev() + "," + kniha.getAutor() + "," + kniha.getRokVydani() + "," + kniha.isDostupnost() + "," + kniha.getTypKnihy() + "\n");
                    break; // ukončiť zápis po nájdení hľadanej knihy
                }
            }
            writer.close();
            System.out.println("Udaje o knize " + nazovKnihy + " byly uspesne ulozene do souboru.");
        } catch (IOException e) {
            System.out.println("Chyba pri zapise do souboru: " + e.getMessage());
        }
    }

    public void nacitanieZoSuboru(String nazovSuboru) {
        try {
            File subor = new File(nazovSuboru);
            BufferedReader reader = new BufferedReader(new FileReader(subor));
            String riadok;

            if ((riadok = reader.readLine()) != null) {
                String[] udaje = riadok.split(",");
                String nazov = udaje[0];
                String autor = udaje[1];
                int rokVydania = Integer.parseInt(udaje[2]);
                boolean dostupnost = Boolean.parseBoolean(udaje[3]);
                String typKnihy = udaje[4];
                prvkyDatabaze[posledniKniha++] = new Kniha(nazov, autor, rokVydania, dostupnost, typKnihy);
                System.out.println("Udaje byly uspesne nactene ze souboru pro knihu " + nazov + ".");
            } else {
                System.out.println("Soubor je prazdny.");
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Chyba pri cteni ze souboru: " + e.getMessage());
        }
    }
    
    public int pouzeCelaCisla(Scanner sc) {
        int cislo = 0;
        try {
            cislo = sc.nextInt();
        } catch (Exception e) {
            System.out.println("Nastala vyjimka typu " + e.toString());
            System.out.println("Zadajte prosim cele cislo ");
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
            System.out.println("1 .. K dispozici");
            System.out.println("2 .. Pujcene");
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
                    System.out.println("Neplatna volba dostupnosti. Zkuste to znovu.");
            }
        }
        return dostupnost;
    }
}
