package domain;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;


@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "product_nummer")
    private int productNummer;

    @Column(name = "naam")
    private String productNaam;

    @Column(name = "beschrijving")
    private String beschrijving;

    @Column(name = "prijs")
    private double prijs;

    @ManyToMany(mappedBy = "producten")
    private List<OVChipkaart> ovChipkaarten = new ArrayList<>() ;

    public Product(int productNummer, String productNaam, String beschrijving, double prijs) {
        this.productNummer = productNummer;
        this.productNaam = productNaam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public Product() {}

    public int getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(int productNummer) {
        this.productNummer = productNummer;
    }

    public String getProductNaam() {
        return productNaam;
    }

    public void setProductNaam(String productNaam) {
        this.productNaam = productNaam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        if (!ovChipkaarten.contains(ovChipkaart)) {
            ovChipkaarten.add(ovChipkaart);
            ovChipkaart.addProduct(this);  // Maintain bidirectional relationship
        }
    }

    public void removeOVChipkaart(OVChipkaart ovChipkaart) {
        if (ovChipkaarten.contains(ovChipkaart)) {
            ovChipkaarten.remove(ovChipkaart);
            ovChipkaart.removeProduct(this);
        }
    }


    // toString method
    @Override
    public String toString() {
        return "Product{" +
                "productNummer=" + productNummer +
                ", naam='" + productNaam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                '}';
    }

}