package msg.practica.ro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import msg.practica.ro.exception.ApartmentNotFoundException;
import msg.practica.ro.model.Apartment;
import msg.practica.ro.repository.ApartmentRepository;
import msg.practica.ro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Get an apartment by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the apartment",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Apartment.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Apartment not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public Apartment findById(@Parameter(description = "id of apartment to be searched")
                              @PathVariable long id) {
        return apartmentRepo.findById(id)
                .orElseThrow(() -> new ApartmentNotFoundException(id));
    }


}
