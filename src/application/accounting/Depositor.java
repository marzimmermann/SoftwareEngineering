package application.accounting;
import java.util.*;
import java.math.BigDecimal;
import java.math.MathContext;

public class Depositor {

    private String nummer;
    private String nachname;
    private String vorname;
    private BigDecimal startguthaben;
    private List<AccountingEntry> einzahlungen;
    private static BigDecimal zinsen;
    
    public Depositor(String nummer, String nachname, String vorname, BigDecimal startguthaben, List<AccountingEntry> einzahlungen){
        this.nummer = nummer;
        this.nachname = nachname;
        this.vorname = vorname;
        this.startguthaben = startguthaben;
        this.einzahlungen = einzahlungen;
    }
    
    public void einzahlen(int tag, BigDecimal betrag){
        AccountingEntry tmp = new AccountingEntry(tag,betrag);
        einzahlungen.add(tmp);
    }
    
    public static void setzeZinsen(BigDecimal zinssatz){
        zinsen = zinssatz;
    }
    
    public BigDecimal berechneGuthaben(){
        this.startguthaben = startguthaben.multiply( (zinsen.divide(new BigDecimal (100)).add(new BigDecimal (1)))) ;
        while(!this.einzahlungen.isEmpty()){
            AccountingEntry tmp = einzahlungen.get(0);
            BigDecimal zwischen = tmp.getBetrag().multiply( ((zinsen.divide(new BigDecimal (100))).multiply(new BigDecimal((360-tmp.getTag())/360.0))).add(new BigDecimal (1)) );
            startguthaben= startguthaben.add(zwischen) ;
            einzahlungen.remove(0);
        }
        startguthaben = (startguthaben.divide( new BigDecimal(100)));
        
        return startguthaben; // 1tes /100 fuer 100tel cent, 2tes fuer umwandlung in euro
    }
    
    public BigDecimal interestAmount(BigDecimal betrag, BigDecimal zinssatz, int tage){
       return null; 
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
    
    public BigDecimal getZinsen(){
        return this.zinsen;
    }
    
}
