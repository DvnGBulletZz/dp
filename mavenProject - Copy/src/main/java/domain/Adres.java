package domain;
public class Adres {

    private int id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private Integer reizigerId; 

    public Adres(int id, String postcode, String huisnummer, String straat, String woonplaats, int reizigerId){
        this.id = id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reizigerId = reizigerId;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getPostcode(){
        return postcode;
    }

    public void setPostcode(String postcode){
        this.postcode = postcode;
    }

    public String getHuisnummer(){
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer){
        this.huisnummer = huisnummer;
    }

    public String getStraat(){
        return straat;
    }

    public void setStraat(String straat){
        this.straat = straat;
    }

    public String getWoonplaats(){
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats){
        this.woonplaats = woonplaats;
    }


    public int getReizigerId() {
        return reizigerId;
    }
    
    public void setReizigerid(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    
    public String toString() {
        return String.format("Adres{id=%d, postcode='%s', huisnummer='%s', straat='%s', woonplaats='%s', reiziger_id=%d}",
                id, postcode, huisnummer, straat, woonplaats, reizigerId);
    }
    





}
