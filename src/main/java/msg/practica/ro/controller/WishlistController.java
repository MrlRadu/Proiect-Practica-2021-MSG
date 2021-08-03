package msg.practica.ro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import msg.practica.ro.model.Apartment;
import msg.practica.ro.model.Wishlist;
import msg.practica.ro.repository.ApartmentRepository;
import msg.practica.ro.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist", description = "CRUD Operations for Wishlist")
public class WishlistController {
    @Autowired
    private WishlistRepository wishlistRepo;

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

    @PostMapping("")
    @Operation(summary = "Add new wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the wishlist was persisted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "the wishlist was NOT persisted",
                    content = @Content),})
    public Wishlist createWishlist(@RequestBody @Valid Wishlist wishlist){

        return wishlistRepo.save(wishlist);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete wishlist with certain id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wishlist successfully deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "Wishlist not successfully deleted",
                    content = @Content),})
    public String deleteWishlist(@PathVariable Long id){
        Optional<Wishlist> w = wishlistRepo.findById(id);
        if(w.isPresent()){
            wishlistRepo.delete(w.get());
            return "Wishlist with id " + id + " was successfully deleted";
        }
        else
            throw new RuntimeException("Wishlist with id " + id + " not found");

    }
}