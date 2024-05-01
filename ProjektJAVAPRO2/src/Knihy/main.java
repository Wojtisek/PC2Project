package Knihy;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        databaze mojeDatabaze = new databaze();
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
            System.out.println("7 .. Vyhladanie knih podla autora");
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
                default:
                    System.out.println("Neplatná volba. Zadejte číslo od 1 do ");
            }
        }
    }
}