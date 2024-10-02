package domain;

import jakarta.persistence.*;
@Entity
@Table(name = "adres")
public class Adres {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adres_id")
    private Integer id;

    @Column(name = "postcode")
    private String postcode;

    @Column(name = "huisnummer")
    private String huisnummer;

    @Column(name = "straat")
    private String straat;

    @Column(name = "woonplaats")
    private String woonplaats;

    @OneToOne
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;


    public Adres(int id, String postcode, String huisnummer, String straat, String woonplaats, Reiziger reiziger){
        this.id = id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger= reiziger;
    }

    public Adres() {

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


    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger (Reiziger reiziger) {
        this.reiziger = reiziger;
    }


    public String toString() {
        return String.format("Adres{id=%d, postcode='%s', huisnummer='%s', straat='%s', woonplaats='%s', reiziger_id=%d}",
                id, postcode, huisnummer, straat, woonplaats, reiziger.getId());
    }






}
