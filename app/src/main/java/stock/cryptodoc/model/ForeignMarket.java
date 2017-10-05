package stock.cryptodoc.model;

/**
 * Created by Administrator on 17-09-2017.
 */

public class ForeignMarket {
    String LASTMARKET,PRICE,CHANGEPCT24HOUR,VOLUME24HOURTO,MARKETNAME;
    String IMAGE;

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public ForeignMarket(String MARKETNAME) {
        this.MARKETNAME = MARKETNAME;
    }

    public String getMARKETNAME() {
        return MARKETNAME;
    }

    public void setMARKETNAME(String MARKETNAME) {
        this.MARKETNAME = MARKETNAME;
    }

    public ForeignMarket() {
    }

    public ForeignMarket(String LASTMARKET, String PRICE, String CHANGEPCT24HOUR, String VOLUME24HOURTO) {
        this.LASTMARKET = LASTMARKET;
        this.PRICE = PRICE;
        this.CHANGEPCT24HOUR = CHANGEPCT24HOUR;
        this.VOLUME24HOURTO = VOLUME24HOURTO;
    }

    public String getLASTMARKET() {
        return LASTMARKET;
    }

    public void setLASTMARKET(String LASTMARKET) {
        this.LASTMARKET = LASTMARKET;
    }

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getCHANGEPCT24HOUR() {
        return CHANGEPCT24HOUR;
    }

    public void setCHANGEPCT24HOUR(String CHANGEPCT24HOUR) {
        this.CHANGEPCT24HOUR = CHANGEPCT24HOUR;
    }

    public String getVOLUME24HOURTO() {
        return VOLUME24HOURTO;
    }

    public void setVOLUME24HOURTO(String VOLUME24HOURTO) {
        this.VOLUME24HOURTO = VOLUME24HOURTO;
    }
}
