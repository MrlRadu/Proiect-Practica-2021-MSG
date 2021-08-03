package msg.practica.ro.model;

import javax.persistence.*;

@Entity(name="Pictures")
@Table(name="Pictures")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="picture_url", nullable = false, length = 2000)
    private String url;

    @JoinColumn(name = "APARTMENT_ID")
    @ManyToOne(cascade = {CascadeType.MERGE})
    private Apartment apartment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", apartment=" + apartment +
                '}';
    }
}
