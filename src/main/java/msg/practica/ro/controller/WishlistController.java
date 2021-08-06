package msg.practica.ro.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
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
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist", description = "CRUD Operations for Wishlist")
public class WishlistController {
    @Autowired
    private WishlistRepository wishlistRepo;
    @Autowired
    private ApartmentRepository apartmentRepository;

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
    @GetMapping("/{id}")
    public List<Wishlist> findAllApartments(@PathVariable Long id) {
        return wishlistRepo.findAllByUserId(id);
    }

    @PostMapping("")
    @Operation(summary = "Add new wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "the wishlist was persisted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Wishlist.class))}),
            @ApiResponse(responseCode = "400", description = "the wishlist was NOT persisted",
                    content = @Content),})
//    public Wishlist createWishlist(Long userId, Long apartmentId){
//
//        Wishlist wishlist = new Wishlist(userId, apartmentId);
//        System.out.println(wishlist.getUser());
//        System.out.println(wishlist.getApartment());
//        return wishlistRepo.save(wishlist);
//    }
    public Wishlist createWishlist(@RequestBody @Valid Wishlist wishlist) {
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
    public String deleteWishlist(@PathVariable Long id) {
        Optional<Wishlist> w = wishlistRepo.findById(id);
        if (w.isPresent()) {
            wishlistRepo.delete(w.get());
            return "Wishlist with id " + id + " was successfully deleted";
        } else
            throw new RuntimeException("Wishlist with id " + id + " not found");

    }
    @ResponseBody
    @RequestMapping(value = "/pdf", headers="Accept=*/*", method = RequestMethod.GET, produces = "application/pdf")
//    @GetMapping("/pdf")
    public ByteArrayInputStream generatePdf() throws DocumentException, IOException {
        Font details = FontFactory.getFont(FontFactory.TIMES, 14, Font.BOLD, BaseColor.BLACK);
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, new FileOutputStream("KeepITsimple-Imobiliare.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);
        int current=1;
        for (Apartment a : apartmentRepository.findAll()) {

            if(current!=1){
                Paragraph chunk = new Paragraph("........................................................................\n", font);
                document.add(chunk);
            }
            Paragraph paragraph0 = new Paragraph("Apartment "+current+" Details", details);
            Paragraph paragraph1 = new Paragraph(a.toString(), font);
            String imageUrl = a.getPictures().get(0).getUrl();
            Image image2 = Image.getInstance(new URL(imageUrl));
            image2.scaleAbsolute(250, 200);
            if (current==6){
            image2.scaleAbsolute(150, 100);
            }
            Paragraph paragraph3 = new Paragraph("Owner Details", details);
            Paragraph paragraph2 = new Paragraph(a.getOwner().toString(), font);
            current++;


            document.add(paragraph0);
            document.add(paragraph1);
            document.add(image2);
            document.add(paragraph3);
            document.add(paragraph2);

        }
        document.close();

//        HttpHeaders responseHeaders = new HttpHeaders();
//        InputStreamResource inputStreamResource = new InputStreamResource(document);
//        responseHeaders.setContentType(MediaType.valueOf("application/pdf"));

        return new ByteArrayInputStream(out.toByteArray());
    }
}
