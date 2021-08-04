package msg.practica.ro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import msg.practica.ro.dto.UserDTO;
import msg.practica.ro.exception.UserNotFoundException;
import msg.practica.ro.login.JwtTokenUtil;
import msg.practica.ro.model.JwtRequest;
import msg.practica.ro.model.User;
import msg.practica.ro.repository.UserRepository;
import msg.practica.ro.service.CustomUserDetails;
import msg.practica.ro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "CRUD Operations for Users")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @Autowireddsa//sterg + userservice
//    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the list of users",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))}),
            @ApiResponse(responseCode = "404", description = "List not found",
                    content = @Content)})
    @GetMapping("")
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    @Operation(summary = "Get an user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public User findById(@Parameter(description = "id of user to be searched")
                         @PathVariable long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @CrossOrigin(origins = "http://localhost:4200") //solved Cors errors
    @PostMapping("/register")
    @Operation(summary = "Add new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User persisted successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "User was NOT persisted successfully",
                    content = @Content),})

    public UserDTO registerUserAccount(@RequestBody @Valid User user) throws IOException {
        String siteURL = "http://localhost:4200";
        return service.registerNewUserAccount(user, siteURL);
    }

    //return registered;


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (service.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @PutMapping("")
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "User not successfully updated",
                    content = @Content),})
    public User updateUser(@RequestBody final User u) {
        return userRepo.save(u);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user with certain id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "User not successfully deleted",
                    content = @Content),})
    public String deleteUser(@PathVariable Long id) {
        Optional<User> u = userRepo.findById(id);
        if (u.isPresent()) {
            userRepo.delete(u.get());
            return "User with id " + id + " was successfully deleted";
        } else
            throw new RuntimeException("User with id " + id + " not found");

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final CustomUserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        final UserDTO currentDTO =  new UserDTO();
        currentDTO.setEmail(userDetails.getUsername());
        currentDTO.setToken(token);
        currentDTO.setFullName(userDetails.getFullName());
        //userdto.set(token)
        //returnez userdto
//        return ResponseEntity.ok(new JwtResponse(token));
        return ResponseEntity.ok(currentDTO);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

//    @CrossOrigin(origins = "http://localhost:4200")
//    @RequestMapping("/login")
//    public boolean login(@RequestBody User user) {
//        System.out.println("aici" + user.toString());
//        return
//                user.getEmail().equals("user") && user.getPassword().equals("password");
//    }
//
//    @RequestMapping("/user")
//    public Principal user(HttpServletRequest request) {
//        String authToken = request.getHeader("Authorization")
//                .substring("Basic".length()).trim();
//        return () -> new String(Base64.getDecoder()
//                .decode(authToken)).split(":")[0];
//    }
}
