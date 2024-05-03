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
	private static final String DB_URL = "jdbc:sqlite:C:/Users/matko/Desktop/Java/ProjektJAVAPRO2/moja_databaza.db";
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
            System.out.println("Chyba pri vytvarani tabulky knihy: " + e.getMessage());
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
            System.out.println("Údaje boli úspešne exportované do SQLite databázy");
        } catch (SQLException e) {
            System.out.println("Chyba pri exportovaní údajov do SQLite databázy: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("");
        }
    }

    public void importFromSQLite() {
    	vytvoritTabulku();
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            String selectQuery = "SELECT * FROM knihy";
            var resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String nazov = resultSet.getString("nazov");
                String autor = resultSet.getString("autor");
                int rokVydania = resultSet.getInt("rok_vydania");
                boolean dostupnost = resultSet.getBoolean("dostupnost");
                String typKnihy = resultSet.getString("typ_knihy");

                prvkyDatabaze[posledniKniha++] = new Kniha(nazov, autor, rokVydania, dostupnost, typKnihy);
            }
            System.out.println("Údaje boli úspešne načítané z SQL databázy\n");
        } catch (SQLException e) {
            System.out.println("Chyba pri načítavaní údajov zo SQLite databázy: " + e.getMessage());
        }
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
                for (int i = 0; i < prvkyDatabaze.length; i++) {
                    if (prvkyDatabaze[i] == nalezenaKniha) {
                        for (int j = i; j < posledniKniha - 1; j++) {
                            prvkyDatabaze[j] = prvkyDatabaze[j + 1];
                        }
                        prvkyDatabaze[posledniKniha - 1] = null;
                        posledniKniha--;
                        System.out.println("Kniha byla úspěšně smazána.");
                        return;
                    }
                }
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
    
    
    public void vypisPodlaAutora(Scanner sc) {
        	sc.nextLine();
	    	System.out.println("Zadejte autora, ktor0ho knihz chcete vypísat:");
	        String autor = sc.nextLine();
            List<Kniha> knihyAutora = new ArrayList<>();
            for (Kniha kniha : prvkyDatabaze) {
                if (kniha != null && kniha.getAutor().equalsIgnoreCase(autor)) {
                    knihyAutora.add(kniha);
                }
            }
            Collections.sort(knihyAutora, (kniha1, kniha2) -> kniha1.getRokVydani() - kniha2.getRokVydani());
            System.out.println("Knihy autora " + autor + " v chronologickém pořadí:");
            for (Kniha kniha : knihyAutora) {
                System.out.println("Název: " + kniha.getNazev());
                System.out.println("Rok vydání: " + kniha.getRokVydani());
                System.out.println("Typ knihy: " + kniha.getTypKnihy());
                System.out.println("Dostupnost: " + (kniha.isDostupnost() ? "K dispozici" : "Půjčená"));
                System.out.println();
            }
        }
    
    
    public void vypisPodleTypuKnihy(Scanner sc) {
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
                System.out.println("Neplatná volba typu knihy.");
                return;
        }

        if (!knihyPodleTypu.isEmpty()) {
            System.out.println("Knihy patřící do kategorie " + (volbaTypu == 1 ? typRomanu : typUcebnice) + ":");
            for (Kniha kniha : knihyPodleTypu) {
                System.out.println("Název: " + kniha.getNazev());
                System.out.println("Autor: " + kniha.getAutor());
                System.out.println("Rok vydání: " + kniha.getRokVydani());
                System.out.println("Dostupnost: " + (kniha.isDostupnost() ? "K dispozici" : "Půjčená"));
                System.out.println();
            }
        } else {
            System.out.println("Žádné knihy patřící do kategorie " + (volbaTypu == 1 ? typRomanu : typUcebnice) + " nebyly nalezeny.");
        }
    }
    
    
    public void vypisVypujcenychKnih() {
        boolean existujiVypujceneKnihy = false;

        for (Kniha kniha : prvkyDatabaze) {
            if (kniha != null && !kniha.isDostupnost()) {
                existujiVypujceneKnihy = true;
                System.out.println("Název: " + kniha.getNazev());
                System.out.println("Typ knihy: " + kniha.getTypKnihy());
                System.out.println();
            }
        }

        if (!existujiVypujceneKnihy) {
            System.out.println("Momentálně nejsou půjčené žádné knihy.");
        }
    }
    
    public void ulozenieDoSuboru(String nazovKnihy) {
        try {
            System.out.println("Zadajte názov súboru pre uloženie údajov:");
            String nazovSuboru = sc.nextLine();
            FileWriter writer = new FileWriter(nazovSuboru);

            for (Kniha kniha : prvkyDatabaze) {
                if (kniha != null && kniha.getNazev().equalsIgnoreCase(nazovKnihy)) {
                    writer.write(kniha.getNazev() + "," + kniha.getAutor() + "," + kniha.getRokVydani() + "," + kniha.isDostupnost() + "," + kniha.getTypKnihy() + "\n");
                    break;
                }
            }
            writer.close();
            System.out.println("Údaje o knihe " + nazovKnihy + " boli úspešne uložené do súboru.");
        } catch (IOException e) {
            System.out.println("Chyba pri zápise do súboru: " + e.getMessage());
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
                System.out.println("Údaje boli úspešne načítané zo súboru pre knihu " + nazov + ".");
            } else {
                System.out.println("Súbor je prázdny.");
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Chyba pri čítaní zo súboru: " + e.getMessage());
        }
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