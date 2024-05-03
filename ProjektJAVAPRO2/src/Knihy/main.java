package Knihy;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        databaze mojeDatabaze = new databaze();
        mojeDatabaze.importFromSQLite();
        
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("Chyba při načítání JDBC ovladace: " + e.getMessage());
        }
        int volba;
        boolean run = true;
        while (run) {
            System.out.println("Vyberte pozadovanou cinnost:");
            System.out.println("1 .. Vložení nové knihy");
            System.out.println("2 .. Upravenie knihy");
            System.out.println("3 .. Zmazanie knihy");
            System.out.println("4 .. Zmena dostupnosti");
            System.out.println("5 .. Výpis knih");
            System.out.println("6 .. Vyhladanie knihy");
            System.out.println("7 .. Výpis podla autora");
            System.out.println("8 .. V7pis podla typu knihy");
            System.out.println("9 .. Výpis požičaných knih");
            System.out.println("10 .. Uloženie info o knihe do suboru");
            System.out.println("11 .. Načítanie info o knihe zo suboru");
            System.out.println("12 .. Skončenie programu");
            volba = mojeDatabaze.pouzeCelaCisla(sc);
            switch (volba) {
                case 1:
                    mojeDatabaze.setKniha(sc);
                    break;
                case 2:
                    mojeDatabaze.upravitKniha(sc);
                    break;
                case 3:
                    mojeDatabaze.smazatKniha(sc);
                    break;
                case 4:
                    mojeDatabaze.zmenaDostupnosti(sc);
                    break;
                case 5:
                    mojeDatabaze.vypisKnihy();
                    break;
                case 6:
                    mojeDatabaze.vyhladanieKnihy();
                    break;
                case 7:
                    mojeDatabaze.vypisPodlaAutora(sc);
                    break;
                case 8:
                    mojeDatabaze.vypisPodleTypuKnihy(sc);
                    break;
                case 9:
                    mojeDatabaze.vypisVypujcenychKnih();
                    break;
                case 10:
                	sc.nextLine();
                    System.out.println("Zadajte názov knihy, ktorú chcete uložiť:");
                    String nazovKnihy = sc.nextLine();
                    mojeDatabaze.ulozenieDoSuboru(nazovKnihy);
                    break;
                case 11:
                	sc.nextLine();
                    System.out.println("Zadajte názov súboru pre načítanie údajov o knihe:");
                    String nazovSuboru = sc.nextLine();
                    mojeDatabaze.nacitanieZoSuboru(nazovSuboru);
                    break;
                case 12:
                	mojeDatabaze.exportToSQLite();
                	run = false;
                	System.out.println("Program sa skočil");
                	break;
                default:
                    System.out.println("Neplatná volba. Zadejte číslo od 1 do 11");
            }
        }
    }
}