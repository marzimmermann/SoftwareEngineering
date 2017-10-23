package application.accounting;

public class AccountingEntry {
    
    private int tag; //[1-360]
    private long betrag; //in 100tel cent
    
    public AccountingEntry(int tag, long betrag){
        this.tag = tag;
        this.betrag = betrag;
    }
    
    public int getTag() {
        return this.tag;
    }
    
    public long getBetrag() {
        return this.betrag;
    }
}
