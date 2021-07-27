package msg.practica.ro.model;

import javax.persistence.*;

@Entity(name = "Owners")
@Table(name = "Owners")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "url_chart", nullable = false, length = 1000)
    private String urlStatisticsChart;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUrlStatisticsChart() {
        return urlStatisticsChart;
    }

    public void setUrlStatisticsChart(String urlStatisticsChart) {
        this.urlStatisticsChart = urlStatisticsChart;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", urlStatisticsChart='" + urlStatisticsChart + '\'' +
                '}';
    }
}
