package nl.hu.dp.ovchip.model.domein.domain;

import java.sql.Date;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;  // Association with Adres

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum, Adres adres) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.adres = adres;
    }
    

    
    public Reiziger() {
        //TODO Auto-generated constructor stub
    }



    public int getId() {
        return id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }


    public Adres getAdres() {
        return adres;
    }


    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    @Override
    public String toString() {
        return String.format("Reiziger{id=%d, voorletters='%s', tussenvoegsel='%s', achternaam='%s', geboortedatum=%s, adres=%s}",
                id, voorletters, tussenvoegsel, achternaam, geboortedatum, (adres != null) ? adres.toString() : "null");
    }
}
