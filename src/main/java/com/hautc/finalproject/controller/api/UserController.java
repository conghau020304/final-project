package com.hautc.finalproject.controller.api;

import com.hautc.finalproject.model.User;
import com.hautc.finalproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/user")
    public List<User> getAllUser(@RequestParam(value = "tk", name = "tk", required = false) String tk) {
        if(tk != null)
            return userService.findByUsername(tk);
        return userService.getAllActiveUserInfo();
    }

    @PostMapping("/user")
    public User addUser(@RequestBody User userRecord) {
        return userService.addUser(userRecord);
    }

    @PutMapping("/user/{id}")
    public User updateUser(@RequestBody User userRecord, @PathVariable Integer id) {
        return userService.updateUser(id,userRecord);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User userInfo = userService.getUserInfoById(id);
        if (userInfo == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}
