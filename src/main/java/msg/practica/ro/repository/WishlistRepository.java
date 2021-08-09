package msg.practica.ro.repository;

import msg.practica.ro.model.Apartment;
import msg.practica.ro.model.User;
import msg.practica.ro.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findAll();

    @Query("select w.apartment from Wishlist w where w.user.id = ?1")
    List<Apartment> findAllByUserId(Long userId);

    Wishlist findByUserAndApartment(User user, Apartment apartment);
}
