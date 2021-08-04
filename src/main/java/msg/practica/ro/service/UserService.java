package msg.practica.ro.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import msg.practica.ro.dto.UserDTO;
import msg.practica.ro.exception.UserAlreadyExistException;
import msg.practica.ro.mapper.UserMapper;
import msg.practica.ro.model.User;
import msg.practica.ro.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private boolean emailExists(final String email) {
        return userRepo.findByEmail(email) != null;
    }


    public UserDTO registerNewUserAccount(User user, String siteURL) throws IOException {
        //http://localhost:8080/api/users/verify?code=
        if (emailExists(user.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with the given email adress:" + user.getEmail());
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setVerified(false);
        userRepo.save(user);
        sendVerificationEmail(user, siteURL);
        return UserMapper.convertEntitytoDTO(user);
    }

    public boolean verify(String verificationCode) {
        User user = userRepo.findByVerificationCode(verificationCode);

        if (user == null || user.isVerified()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setVerified(true);
            userRepo.save(user);

            return true;
        }
    }

    private void sendVerificationEmail(User user, String siteURL) throws IOException {
        Email fromEmail = new Email();
        fromEmail.setName("KeepITsimpleImobiliare");
        fromEmail.setEmail("gabriel.dan22.99@gmail.com");
        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.setTemplateId("d-a0b902c8d5e94a689ace788b3b4a924e");
        Personalization personalization=new Personalization();
        personalization.addDynamicTemplateData("first_name",user.getFirstName());
        personalization.addDynamicTemplateData("last_name",user.getLastName());

        //http://localhost:8080/api/users/verify?code=
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
        System.out.println(verifyURL);

        personalization.addDynamicTemplateData("link",verifyURL);
        personalization.addTo(new Email(user.getEmail()));
        mail.addPersonalization(personalization);
        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user);
    }

}
