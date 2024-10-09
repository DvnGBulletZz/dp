package domain;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ov_chipkaart")
public class OVChipkaart {
    @Id
    @Column(name = "kaart_nummer")
    private int kaartNummer;

    @Column(name = "geldig_tot")
    private Date geldigTot;

    @Column(name = "klasse")
    private int klasse;

    @Column(name = "saldo")
    private double saldo;

    @ManyToOne
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(
            name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "kaart_nummer"),
            inverseJoinColumns = @JoinColumn(name = "product_nummer"))
    private List<Product> producten = new ArrayList<>();

    public OVChipkaart(int kaartNummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
//        this.producten = new ArrayList<>();
    }

    public OVChipkaart() {}

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducts() {
        return producten;
    }

    public void setProducts(List<Product> producten) {
        this.producten = producten;
    }

    // Bidirectional relationship management methods
    public void addProduct(Product product) {
        if (producten == null) {
            producten = new ArrayList<>();
        }
        if (!producten.contains(product)) {
            producten.add(product);
            product.addOVChipkaart(this);  // Bidirectional relationship
        }
    }


    public void removeProduct(Product product) {
        if (producten.contains(product)) {
            producten.remove(product);
            product.removeOVChipkaart(this);
        }
    }

    // toString method
    @Override
    public String toString() {
        return "OVChipkaart{" +
                "kaartNummer=" + kaartNummer +
                ", geldigTot=" + geldigTot +
                ", klasse=" + klasse +
                ", saldo=" + saldo +
                ", reiziger=" + reiziger.getId() + // Avoid circular reference
                '}';
    }
}