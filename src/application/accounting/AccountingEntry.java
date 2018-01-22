package application.accounting;

import java.math.BigDecimal;

public class AccountingEntry {
    
    private int tag; //[1-360]
    private BigDecimal betrag; //in 100tel cent
    
    public AccountingEntry(int tag, BigDecimal betrag){
        this.tag = tag;
        this.betrag = betrag;
    }
    
    public int getTag() {
        return this.tag;
    }
    
    public BigDecimal getBetrag() {
        return this.betrag;
    }
}
