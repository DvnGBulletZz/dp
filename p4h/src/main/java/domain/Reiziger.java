package domain;


import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reiziger")
public class Reiziger {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "reiziger_id")
    private int id;

    @Column (name = "voorletters")
    private String voorletters;

    @Column (name = "tussenvoegsel")
    private String tussenvoegsel;

    @Column (name = "achternaam")
    private String achternaam;

    @Column (name = "geboortedatum")
    private Date geboortedatum;


    @OneToOne(mappedBy = "reiziger", cascade = CascadeType.ALL)
    private Adres adres;

    @OneToMany(mappedBy = "reiziger", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OVChipkaart> ovChipkaarten = new ArrayList<>();


    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum, Adres adres, List<OVChipkaart> ovChipkaarten) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.adres = adres;
        this.ovChipkaarten = ovChipkaarten;

    }

    public Reiziger() {
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


    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void setOvChipkaarten(List<OVChipkaart> ovChipkaarten) {
        this.ovChipkaarten = ovChipkaarten;
    }


    public void addOvChipkaart(OVChipkaart ovChipkaart) {
        this.ovChipkaarten.add(ovChipkaart);
        ovChipkaart.setReiziger(this);
    }

    public void removeOvChipkaart(OVChipkaart ovChipkaart) {
        this.ovChipkaarten.remove(ovChipkaart);
        ovChipkaart.setReiziger(null);
    }

    @Override
    public String toString() {
        return String.format("Reiziger{id=%d, voorletters='%s', tussenvoegsel='%s', achternaam='%s', geboortedatum=%s, adres=%s, ovChipkaarten=%s}",
                id, voorletters, tussenvoegsel, achternaam, geboortedatum, (adres != null) ? adres.toString() : "null", ovChipkaarten);
    }
}
