package application.accounting;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.*;
import java.io.*;

public class Accounting{
    //Logger
    private static final Logger logger = Logger.getLogger(Accounting.class.getName());
    
    //Resource Bundle
    private static String baseName = "Accounting";
    private static ResourceBundle rb = ResourceBundle.getBundle(baseName);
    
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
        String dateiname=null, output=null, log="";
        double zinssatz=0;
        
        //ohne ArgParser
        if(args.length == 0){
            Scanner sc = new Scanner(System.in);
            dateiname = sc.nextLine();
            zinssatz = sc.nextDouble();
            sc.close();
        }
        //mit Argparser
        else{
            ArgParser ap = new ArgParser(args);
            dateiname = ap.getInputFilename();
            log = ap.getLogFilename();
            output = ap.getOutputFilename();
            zinssatz = Double.parseDouble(ap.getNonOptions());
            if(output != null){
                System.setOut(new PrintStream(new FileOutputStream(output)));
            }
        }
        
        //Logger wird aktiviert
        if(log !=null && log.length() >0){
        try{
            boolean append =true;
            FileHandler fh = new FileHandler(log, append);
            fh.setFormatter(new Formatter(){
                public String format(LogRecord rec){
                    StringBuffer buf = new StringBuffer(1000);
                    buf.append(new java.util.Date()).append(' ');
                    buf.append(rec.getLevel()).append(' ');
                    buf.append(formatMessage(rec)).append('\n');
                    return buf.toString();
                }
            });
            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
        }
        catch(IOException e){
            logger.severe("Datei kann nicht geschrieben werden");
            e.printStackTrace();
        }
        }
        //datei einlesen
        try{
            BufferedReader bf = new BufferedReader(new FileReader(dateiname));
            String zeile;
            String readinput_msg = rb.getString("readinput_msg");
            logger.info(readinput_msg + ": " + dateiname);
            
            while((zeile=bf.readLine()) != null) {
                logger.info("gelesene Zeile: " + zeile);
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
        }
        catch (IOException e){
            logger.warning("IOException");
            throw e;
        }
        
        Depositor.setzeZinsen(zinssatz);
        logger.info("setze Zinssatz auf: " + zinssatz);
        for(int k=0; k < deps.size(); k++){
            System.out.println(deps.get(k).getNummer() +";" +deps.get(k).getNachname() +";" + deps.get(k).getVorname() +";"+Kommadarstellung(deps.get(k).berechneGuthaben()));
        }
    }
}
