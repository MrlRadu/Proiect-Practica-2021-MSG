package msg.practica.ro.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Getter
@Setter
public class UserDTO{
    private String firstName;

    private String lastName;

    private String email;

    private String token;

    private String fullName;

    public UserDTO() {
    }

    public UserDTO(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
