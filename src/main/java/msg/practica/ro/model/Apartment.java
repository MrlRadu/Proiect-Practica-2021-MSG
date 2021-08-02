package msg.practica.ro.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "Apartments")
@Table(name = "Apartments")
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 200)
    private String titleApart;

    @Column(nullable = false, length = 30)
    private String transactionType;

    @Column(nullable = false, length = 30)
    private String propertyType;

    @Column(nullable = false, length = 30)
    private String city;

    @Column(nullable = false, length = 30)
    private String neighbourhood;

    @Column(nullable = false, length = 60)
    private Integer price;

    @Column(nullable = false, length = 10)
    private Integer nrRooms;

    @Column(nullable = false, length = 20)
    private Integer surface;

    @Column(nullable = false, length = 20)
    private Integer yearConstruction;

    @Column(nullable = false, length = 1000)
    private String description;

    @JoinColumn(name = "OWNER_ID")
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Owner owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleApart() {
        return titleApart;
    }

    public void setTitleApart(String titleApart) {
        this.titleApart = titleApart;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getNrRooms() {
        return nrRooms;
    }

    public void setNrRooms(Integer nrRooms) {
        this.nrRooms = nrRooms;
    }

    public Integer getSurface() {
        return surface;
    }

    public void setSurface(Integer surface) {
        this.surface = surface;
    }

    public Integer getYearConstruction() {
        return yearConstruction;
    }

    public void setYearConstruction(Integer yearConstruction) {
        this.yearConstruction = yearConstruction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", titleApart='" + titleApart + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", city='" + city + '\'' +
                ", neighbourhood='" + neighbourhood + '\'' +
                ", price=" + price +
                ", nrRooms=" + nrRooms +
                ", surface=" + surface +
                ", yearConstruction=" + yearConstruction +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                '}';
    }
}
