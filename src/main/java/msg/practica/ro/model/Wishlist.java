package msg.practica.ro.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table
public class Wishlist {

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    Apartment apartment;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Wishlist() {}

//    public Wishlist(Long userId, Long apartmentId) {
//        UserController userController = new UserController();
//        UserRepository userRepository = userController.getUserRepo();
//
//        ApartmentController apartmentController = new ApartmentController();
//        ApartmentRepository apartmentRepository = apartmentController.getApartmentRepo();
//
//        this.user = userRepository.findUserById(userId);
//        this.apartment = apartmentRepository.findApartmentById(apartmentId);
//    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "id=" + id +
                ", user=" + user +
                ", apartment=" + apartment +
                '}';
    }
}
