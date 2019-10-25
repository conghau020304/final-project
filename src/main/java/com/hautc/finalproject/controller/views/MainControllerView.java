package com.hautc.finalproject.controller.views;

import com.hautc.finalproject.dto.ChangePassDTO;
import com.hautc.finalproject.model.User;
import com.hautc.finalproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class MainControllerView {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }

    @GetMapping(value = {"/login", "/"})
    public String getLogin() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/user/change-password", method = RequestMethod.GET)
    public String showFormChangePassword(){
        return "changePass";
    }

    @PostMapping("/user/action-change-password")
    @ResponseBody
    public String actionChangePass(@RequestBody ChangePassDTO user, Principal principal){
        User u = userService.findByUsername(principal.getName());
        if(!passwordEncoder.matches(user.getOldPassword(), u.getPassword())){
            return "error";
        }
        userService.changePassword(passwordEncoder.encode(user.getNewPassword()),principal.getName());
        return "ok";
    }
}
