package application.accounting;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;

public class Accounting{
public String applicationVersion = "Id: Accounting.java, version 862c397 of 2017-12-04 16:27:51 +0100 by se110409";
    
    //Logger
    private static final Logger logger = Logger.getLogger(Accounting.class.getName());
    
    //Resource Bundle
    private static String baseName = "Accounting";
    private static ResourceBundle rb = ResourceBundle.getBundle(baseName);
    
    private static void beende(String msg){
        logger.warning(msg);
        System.out.println("Ausfuehrung beendet (siehe Logdatei): ");
        System.out.println(msg);
        System.exit(1);
    }
    
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
        File file = new File("./dist/data/lang");
        rb = null;
        try{
            URL[] urls = {file.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            rb = ResourceBundle.getBundle(baseName, Locale.getDefault(), loader);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        List<Depositor>deps = new ArrayList<>();
        String dateiname=null, output=null, log="";
        BigDecimal zinssatz;
        
        //ohne ArgParser
        if(args.length == 0){
            Scanner sc = new Scanner(System.in);
            System.out.println("Warte auf args");
            dateiname = sc.nextLine();
            zinssatz =  new BigDecimal( sc.nextLine());
            sc.close();
        }
        //mit Argparser
        else{
            ArgParser ap = new ArgParser(args);
            dateiname = ap.getInputFilename();
            log = ap.getLogFilename();
            output = ap.getOutputFilename();
            zinssatz = new BigDecimal (ap.getNonOptions());
            //System.setOut(new PrintStream(new FileOutputStream(output)));
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
                        
                    if(nummer.length() == 0){
                        beende("Fehlerhafte Id");
                    }
                        
                    if(nachname.length() == 0 || vorname.length() == 0){
                        beende("Fehler beim Namen");
                    }
                        
                    String[] gut = eintrag[3].split(",");
                        
                    try{    
                        if(gut[1].length() ==1){
                            gut[1] += "0";
                        }
                    }catch(Exception e){
                        logger.warning("Falsches Startguthaben");
                        beende(e.getMessage());
                    }
                    String guth = gut[0]+gut[1];
                    BigDecimal startguthaben =  new BigDecimal (guth);
                        
                    
                List<AccountingEntry> einzahlungen = new ArrayList<>();
                for(int i=4; i< eintrag.length; i++){
                    int tag = Integer.parseInt(eintrag[i]);
                    i++;
                    String[] bet = eintrag[i].split(",");
                    try{
                        if(bet[1].length() ==1){
                            bet[1] += "0";
                        }
                        String betr = bet[0]+bet[1];
                        BigDecimal betrag = new BigDecimal (betr);
                        AccountingEntry tmp = new AccountingEntry(tag, betrag);
                        einzahlungen.add(tmp);
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        logger.warning("Falsche Eingabe");
                        beende(e.getMessage());
                    }
                }
                Depositor d = new Depositor(nummer, nachname, vorname, startguthaben, einzahlungen);
                deps.add(d);
            }
        }
        }
        catch (IOException e){
            logger.warning("Ein-/Ausgabefehler (Datei " +dateiname +" )");
            throw e;
        }
        
        Depositor.setzeZinsen(zinssatz);
        for(int k=0; k < deps.size(); k++){
            System.out.println(deps.get(k).getNummer() +";" +deps.get(k).getNachname() +";" + deps.get(k).getVorname() +";"+deps.get(k).berechneGuthaben());
        }
    }
}
