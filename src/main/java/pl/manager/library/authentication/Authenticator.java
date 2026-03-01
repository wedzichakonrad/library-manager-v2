package pl.manager.library.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.manager.library.database.IUserRepository;
import pl.manager.library.model.User;

@Component
@RequiredArgsConstructor
public class Authenticator implements IAuthenticator {
    private final IUserRepository usersRepository;

    @Override
    public User authenticate(User userFromGui) {
        User foundUser = usersRepository.getUserByLogin(userFromGui.getLogin());

        if (foundUser == null) {
            System.out.println("User not found - " + userFromGui.getLogin());
            return null;
        }

        System.out.println("Wrong password");
        return null;
    }
}