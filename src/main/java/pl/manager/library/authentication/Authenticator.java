package pl.manager.library.authentication;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import pl.manager.library.database.IUserRepository;
import pl.manager.library.model.User;

@Component
@RequiredArgsConstructor
public class Authenticator implements IAuthenticator {
    private final IUserRepository usersRepository;

    @Override
    public User authenticate(User user) {
        User foundUser = usersRepository.getUserByLogin(user.getLogin());

        if (foundUser != null && foundUser.getPassword().equals(DigestUtils.md5Hex(user.getPassword()))) {
            return foundUser;
        }

        return null;
    }
}