package pl.comarch.camp.it.book.store.controllers;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.comarch.camp.it.book.store.dao.IUserDAO;
import pl.comarch.camp.it.book.store.exceptions.UserAlreadyExistException;
import pl.comarch.camp.it.book.store.exceptions.UserValidationException;
import pl.comarch.camp.it.book.store.model.User;
import pl.comarch.camp.it.book.store.model.dto.RegisterUserDTO;
import pl.comarch.camp.it.book.store.session.SessionObject;
import pl.comarch.camp.it.book.store.validators.UserValidator;

@Controller
public class AuthenticationController {

    private final IUserDAO userDAO;

    /*@Resource
    SessionObject sessionObject;*/

    public AuthenticationController(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestParam String login, @RequestParam String password,
                        HttpSession httpSession) {
        try {
            UserValidator.validateLogin(login);
            UserValidator.validatePassword(password);
        } catch (UserValidationException e) {
            return "redirect:/login";
        }
        User user = this.userDAO.getByLogin(login);
        if(user != null && user.getPassword().equals(DigestUtils.md5Hex(password))) {
            user.setPassword(null);
            httpSession.setAttribute("user", user);
            return "redirect:/main";
        }
        return "redirect:/login";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return "redirect:/main";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("userModel", new RegisterUserDTO());
        return "register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String cos(@ModelAttribute RegisterUserDTO userDTO) {
        try {
            UserValidator.validateUserDto(userDTO);
            this.userDAO.save(map(userDTO));
        } catch(UserValidationException | UserAlreadyExistException e) {
            return "redirect:/register";
        }
        return "redirect:/main";
    }

    private User map(RegisterUserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setLogin(userDTO.getLogin());
        user.setPassword(DigestUtils.md5Hex(userDTO.getPassword()));
        user.setRole("USER");

        return user;
    }
}
