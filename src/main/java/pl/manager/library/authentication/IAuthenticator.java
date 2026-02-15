package pl.manager.library.authentication;

import pl.manager.library.model.User;

public interface IAuthenticator {
    User authenticate(User user);
}