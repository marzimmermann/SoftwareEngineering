package application.accounting;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Accounting {
	
	public static void main(String ars[]){
		Scanner sc = new Scanner(System.in);
		String line = new String();
		String splitted [];
		while(sc.hasNext()){
			line = sc.nextLine();
           // System.out.println(line);
			splitted = line.split(";");
			if(splitted[0].startsWith("#") || splitted.length < 4){
				for(int j = 0; j < splitted.length; j++){
					System.out.print(splitted[j]);
				}
				System.out.println();
				continue;
			}
			else{
				/* 0:nummer, 1:nachname, 2:vorname, 3:startsumme, 4-?: einzahlunggen */
				List<AccountingEntry> einzahlungen = new ArrayList<>();
				Depositor sparer = new Depositor(splitted[0], splitted[1], splitted[2],
						Math.round((Double.valueOf(splitted[3]))*10000.), einzahlungen);
				for(int  j = 4; j < splitted.length; j+=2){
					try{
						sparer.einzahlen(Integer.valueOf(splitted[j]), Math.round(Double.valueOf(splitted[j+1])*10000.));
					}
					catch(IndexOutOfBoundsException e){
						System.out.println("Fehler in dieser Zeile! Angaben Fehlen");
						continue;
					}
				}
				System.out.println(sparer.getNummer()+";"+sparer.getNachname()+";"+sparer.getVorname()+
						";"+sparer.berechneGuthaben(Depositor.getZinssatz()));
			}
		}
	}
}
