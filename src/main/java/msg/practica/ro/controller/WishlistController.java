package msg.practica.ro.controller;

import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import msg.practica.ro.model.Apartment;
import msg.practica.ro.model.User;
import msg.practica.ro.model.Wishlist;
import msg.practica.ro.repository.UserRepository;
import msg.practica.ro.repository.WishlistRepository;
import msg.practica.ro.service.GeneratePdfReport;
import msg.practica.ro.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist", description = "CRUD Operations for Wishlist")
@Transactional
public class WishlistController {
    @Autowired
    private WishlistRepository wishlistRepo;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Get all wishlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the list of wishlists",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Wishlist.class)))}),
            @ApiResponse(responseCode = "404", description = "List not found",
                    content = @Content)})
    @GetMapping("")
    public List<Wishlist> findAllApartments() {
        return wishlistRepo.findAll();
    }

    @Operation(summary = "Get the wishlist from a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the wishlist",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Wishlist.class)))}),
            @ApiResponse(responseCode = "404", description = "List not found",
                    content = @Content)})
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public List<Apartment> findAllApartments(@PathVariable Long id) {
        return wishlistRepo.findAllByUserId(id);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/{userId}/{apartmentId}")
    @Operation(summary = "Add new wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the wishlist was persisted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "the wishlist was NOT persisted",
                    content = @Content),})
    public void createWishlistWithQuery(@PathVariable Long userId, @PathVariable Long apartmentId) {
        wishlistService.insertIntoWishlist(userId, apartmentId);

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete wishlist with certain id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist successfully deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "Wishlist not successfully deleted",
                    content = @Content),})
    public String deleteWishlist(@PathVariable Long id) {
        Optional<Wishlist> w = wishlistRepo.findById(id);
        if (w.isPresent()) {
            wishlistRepo.delete(w.get());
            return "Wishlist with id " + id + " was successfully deleted";
        } else
            throw new RuntimeException("Wishlist with id " + id + " not found");

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "generate pdf with apartments from wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "pdf successfully generated",
                    content = {@Content(mediaType = "application/pdf",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "pdf not successfully generated",
                    content = @Content),})
    @RequestMapping(value = "/pdf/{email}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> wishlistReport(@PathVariable String email) throws DocumentException, IOException {

        User user = userRepository.findByEmail(email);
        List<Apartment> apartments = wishlistRepo.findAllByUserId(user.getId());
        System.out.println(email);
        ByteArrayInputStream bis = GeneratePdfReport.generatePdf(apartments);

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=KeepITsimple-Imobiliare.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }


    //delete din wishlist -> where user=... and apart=...
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/user/{userId}/apartment/{apId}")
    @Operation(summary = "Delete apartment from user wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted from wishlist",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "Not successfully deleted from wishlist",
                    content = @Content),})
    public void deleteFromWishlist(@PathVariable String apId, @PathVariable String userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Wishlist> criteriaDelete = cb.createCriteriaDelete(Wishlist.class);
        Root root = criteriaDelete.from(Wishlist.class);
        Predicate apart = cb.equal(root.get("apartment").get("id"), apId);
        Predicate user = cb.equal(root.get("user").get("id"), userId);
        criteriaDelete.where(apart, user);

        Query q = entityManager.createQuery(criteriaDelete);
        q.executeUpdate();

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/statistics/{apId}")
    @Operation(summary = "get how many times a apartment is in the wishlist of any user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully received the number",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "Not successfully deleted from wishlist",
                    content = @Content),})
    public String ApartmentInWishlist(@PathVariable Long apId) {
        return wishlistRepo.countDistinctByApartment_Id(apId).toString();
    }

    //with native query

//    public String deleteFromWishlist(@RequestBody @Valid Wishlist wishlist){
//        entityManager.persist(wishlist);
//        entityManager.flush();
//        entityManager.clear();
//
//
//        //not a good solution
//        entityManager.createNativeQuery("DELETE FROM Wishlist w WHERE w.apartment_id = :apart_id AND w.user_id= :user_id") // and w.user.email = :user_email
//                .setParameter("apart_id",wishlist.getApartment().getId())
//                .setParameter("user_id",wishlist.getUser().getId())
//                .executeUpdate();
//
//        return "Deleted from wishlist";// where apartmentId = " + apartment.getId() + " and userEmail = " + user.getEmail();
//    }

}
