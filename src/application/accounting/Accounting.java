package application.accounting;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Accounting{
    
    public static String Kommadarstellung(double betrag){
        String betr = String.valueOf(betrag);
        String aus = "";
        for(int i=0; i< betr.length(); i++){
            if(i == betr.length()-3){ //entspricht '.'
                aus += ',';
                continue;
            }
            aus += betr.charAt(i);
        }
        return aus;
    }
    
    public static void main(String[] argv){
        List<Depositor>deps = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        
        //datei einlesen
        while(sc.hasNextLine()) {
            String zeile = sc.nextLine();
            char s = zeile.charAt(0);
            if(s == '#'){
                continue;
            }
            else if(s == '0' || s == '1' || s == '2' || s == '3' || s == '4' || s == '5' || s == '6' || s == '7' || s == '8' || s == '9'){ //Zeile mit Information
                String[]eintrag = zeile.split(";");
                String nummer = eintrag[0];
                String nachname = eintrag[1];
                String vorname = eintrag[2];
                
                String[] gut = eintrag[3].split(",");
                if(gut[1].length() ==1){
                    gut[1] += "0";
                }
                String guth = gut[0]+gut[1];
                long startguthaben = Long.parseLong(guth) *100;
                
                List<AccountingEntry> einzahlungen = new ArrayList<>();
                for(int i=4; i< eintrag.length; i++){
                    int tag = Integer.parseInt(eintrag[i]);
                    i++;
                    String[] bet = eintrag[i].split(",");
                    if(bet[1].length() ==1){
                        bet[1] += "0";
                    }
                    String betr = bet[0]+bet[1];
                    long betrag = Long.parseLong(betr) *100;
                    AccountingEntry tmp = new AccountingEntry(tag, betrag);
                    einzahlungen.add(tmp);
                }
                Depositor d = new Depositor(nummer, nachname, vorname, startguthaben, einzahlungen);
                deps.add(d);
            }
        }
        
        Depositor.setzeZinsen(0.05);
        
        for(int k=0; k < deps.size(); k++){
            System.out.println(deps.get(k).getNummer() +";" +deps.get(k).getNachname() +";" + deps.get(k).getVorname() +";" +Kommadarstellung(deps.get(k).berechneGuthaben()));
        }
    }
}
