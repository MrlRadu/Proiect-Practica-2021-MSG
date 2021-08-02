package msg.practica.ro.repository;

import msg.practica.ro.model.Picture;
import msg.practica.ro.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findAll();
}
