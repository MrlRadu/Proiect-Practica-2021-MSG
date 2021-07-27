package msg.practica.ro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import msg.practica.ro.model.Apartment;
import msg.practica.ro.repository.ApartmentRepository;
import msg.practica.ro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@Tag(name = "Apartments", description = "CRUD Operations for Apartments")
public class ApartmentController {
    @Autowired
    private ApartmentRepository apartmentRepo;

    @Operation(summary = "Get all apartments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the list of apartments",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Apartment.class)))}),
            @ApiResponse(responseCode = "404", description = "List not found",
                    content = @Content)})
    @GetMapping("")
    public List<Apartment> findAllApartments() {
        return apartmentRepo.findAll();
    }

}
