package application.accounting;
import java.util.*;

public class Depositor {

    private String nummer;
    private String nachname;
    private String vorname;
    private long startguthaben;
    private List<AccountingEntry> einzahlungen;
    private static double zinsen;
    
    public Depositor(String nummer, String nachname, String vorname, long startguthaben, List<AccountingEntry> einzahlungen){
        this.nummer = nummer;
        this.nachname = nachname;
        this.vorname = vorname;
        this.startguthaben = startguthaben;
        this.einzahlungen = einzahlungen;
    }
    
    public void einzahlen(int tag, long betrag){
        AccountingEntry tmp = new AccountingEntry(tag,betrag);
        einzahlungen.add(tmp);
    }
    
    public static void setzeZinsen(double zinssatz){
        zinsen = zinssatz;
    }
    
    public double berechneGuthaben(){
        this.startguthaben = (long) (startguthaben * (zinsen/100 +1));
        while(!this.einzahlungen.isEmpty()){
            AccountingEntry tmp = einzahlungen.get(0);
            startguthaben += (long) (tmp.getBetrag() * ((zinsen/100) * ((360-tmp.getTag())/360.0) +1) );
            einzahlungen.remove(0);
        }
        return Math.round(startguthaben/100.0)/100.0; // 1tes /100 fuer 100tel cent, 2tes fuer umwandlung in euro
    }
    
    public String getNummer(){
        return this.nummer;
    }
    
    public String getNachname(){
        return this.nachname;
    }
    
    public String getVorname(){
        return this.vorname;
    }
    
    public double getZinsen(){
        return this.zinsen;
    }
    
}
