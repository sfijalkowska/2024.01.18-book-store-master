package pl.comarch.camp.it.book.store.validators;

import pl.comarch.camp.it.book.store.exceptions.UserValidationException;
import pl.comarch.camp.it.book.store.model.dto.RegisterUserDTO;

public class UserValidator {
    public static void validateLogin(String login) {
        if(login.length() < 5) {
            throw new UserValidationException("Login too short");
        }
    }

    public static void validateName(String name) {
        if(name.length() < 3 || !Character.isUpperCase(name.charAt(0))) {
            throw new UserValidationException("Name incorrect");
        }
    }

    public static void validateSurname(String surname) {
        if(surname.length() < 2 || !Character.isUpperCase(surname.charAt(0))) {
            throw new UserValidationException("Surname incorrect");
        }
    }

    public static void validatePassword(String password) {
        if(password.length() < 5) {
            throw new UserValidationException("Password too short");
        }
    }

    public static void validatePasswordsEquality(String pass1, String pass2) {
        if(!pass1.equals(pass2)) {
            throw new UserValidationException("Passwords not equal");
        }
    }

    public static void validateUserDto(RegisterUserDTO userDTO) {
        UserValidator.validateName(userDTO.getName());
        UserValidator.validateSurname(userDTO.getSurname());
        UserValidator.validateLogin(userDTO.getLogin());
        UserValidator.validatePassword(userDTO.getPassword());
        UserValidator.validatePasswordsEquality(userDTO.getPassword(), userDTO.getPassword2());
    }
}
