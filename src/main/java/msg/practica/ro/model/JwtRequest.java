package msg.practica.ro.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String email;
    private String password;

    //need default constructor for JSON Parsing
    public JwtRequest() {

    }

    public JwtRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
