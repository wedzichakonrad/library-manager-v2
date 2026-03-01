package pl.manager.library.authentication;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;
import pl.manager.library.database.IUserRepository;
import pl.manager.library.model.User;

@Component
@RequiredArgsConstructor
public class Authenticator implements IAuthenticator {
    private final IUserRepository usersRepository;

    @Override
    public User authenticate(User userFromGui) {
        String login = userFromGui.getLogin().trim();
        String password = userFromGui.getPassword().trim();

        User foundUser = usersRepository.getUserByLogin(login);

        if (foundUser == null) {
            System.out.println("User not found: '" + login + "'");
            return null;
        }

        if (BCrypt.checkpw(password, foundUser.getPassword())) {
            return foundUser;
        }

        System.out.println("Wrong password");
        return null;
    }
}