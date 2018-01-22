package application.accounting;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;

public class Accounting{
    public String applicationVersion = "Id: Accounting.java, version 862c397 of 2017-12-04 16:27:51 +0100 by se110409";
    
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
    
    public static void main(String[] args) throws FileNotFoundException, IOException{
        List<Depositor>deps = new ArrayList<>();
        String dateiname=null, output=null;
        BigDecimal zinssatz;
        
        //ohne ArgParser
        if(args.length == 0){
            Scanner sc = new Scanner(System.in);
            dateiname = sc.nextLine();
            zinssatz =  new BigDecimal( sc.nextLine());
            sc.close();
        }
        //mit Argparser
        else{
            ArgParser ap = new ArgParser(args);
            dateiname = ap.getInputFilename();
            output = ap.getOutputFilename();
            zinssatz = new BigDecimal (ap.getNonOptions());
            //System.setOut(new PrintStream(new FileOutputStream(output)));
        }
        
        BufferedReader bf = new BufferedReader(new FileReader(dateiname));
        String zeile;
        
        //datei einlesen
        while((zeile=bf.readLine()) != null) {
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
                BigDecimal startguthaben =  new BigDecimal (guth);
                
                List<AccountingEntry> einzahlungen = new ArrayList<>();
                for(int i=4; i< eintrag.length; i++){
                    int tag = Integer.parseInt(eintrag[i]);
                    i++;
                    String[] bet = eintrag[i].split(",");
                    if(bet[1].length() ==1){
                        bet[1] += "0";
                    }
                    String betr = bet[0]+bet[1];
                    BigDecimal betrag = new BigDecimal (betr);
                    AccountingEntry tmp = new AccountingEntry(tag, betrag);
                    einzahlungen.add(tmp);
                }
                Depositor d = new Depositor(nummer, nachname, vorname, startguthaben, einzahlungen);
                deps.add(d);
            }
        }
        
        Depositor.setzeZinsen(zinssatz);
        for(int k=0; k < deps.size(); k++){
            System.out.println(deps.get(k).getNummer() +";" +deps.get(k).getNachname() +";" + deps.get(k).getVorname() +";"+deps.get(k).berechneGuthaben());
        }
    }
}
