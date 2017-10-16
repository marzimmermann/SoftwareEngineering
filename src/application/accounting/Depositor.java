package application.accounting;
import java.util.List;

public class Depositor {
	private String nummer, nachname, vorname;
	private long startguthaben;
	private List<AccountingEntry> einzahlungen;
	protected static double zinsInProzent = 0.05;
	
	public Depositor(String nummer, String nachname, String vorname, long startguthaben, List<AccountingEntry> einzahlungen) {
		this.nummer = nummer;
		this.nachname = nachname;
		this.vorname = vorname;
		this.startguthaben = startguthaben;
		this.einzahlungen = einzahlungen;
	}
	
	public void einzahlen(int tag, long betrag){
		this.einzahlungen.add(new AccountingEntry(tag, betrag));
	}
	
	public static void setzeZinsen(double zinssatz){
		zinsInProzent = zinssatz;
	}
	
	public static double getZinssatz(){
		return zinsInProzent;
	}
	
	public double berechneGuthaben(double zinssatz){
        long guthaben = (long)(this.startguthaben *((zinssatz/100.) +1.)) ;
		for(int i = 0; i < einzahlungen.size(); i++){
            guthaben+= (long)(this.einzahlungen.get(i).getBetrag() * (((zinsInProzent/100.)*(360-
                this.einzahlungen.get(i).getTag())/360.)+1.));
		}
		return ((guthaben+50)/100)/100.;
	}

	public String getNummer() {
		return nummer;
	}

	public String getNachname() {
		return nachname;
	}

	public String getVorname() {
		return vorname;
	}
	
}
