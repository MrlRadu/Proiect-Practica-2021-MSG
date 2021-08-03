package msg.practica.ro.service;

import msg.practica.ro.exception.UserAlreadyExistException;
import msg.practica.ro.model.User;
import msg.practica.ro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserService implements IUserService{
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUserAccount(User user) {
        if (emailExists(user.getEmail())) {
            throw new UserAlreadyExistException(
                    "There is an account with the given email adress:" + user.getEmail());
        }
        User u = new User();
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());
        //encoding password
        u.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    private boolean emailExists(final String email) {
        return userRepo.findByEmail(email) != null;
    }
}
