package com.hautc.finalproject.controller.api;

import com.hautc.finalproject.dto.UserDTO;
import com.hautc.finalproject.exception.ResourceNotFoundException;
import com.hautc.finalproject.model.User;
import com.hautc.finalproject.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/api")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IUserService userService;

    @GetMapping("/user")
    public List<UserDTO> getAllUser(@RequestParam(value = "tk", name = "tk", required = false) String tk) {
        List<User> users;
        if (tk != null)
            users = userService.searchByUsername(tk);
        else {
            users = userService.getAllUserInfo();
        }
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @PostMapping("/user")
    public ResponseEntity addUser(@Valid @RequestBody UserDTO userRecord) {
        if (userService.findByUsername(userRecord.getUsername()) != null) {
            Map<String, String> error = new HashMap<>();
            error.put("username", userRecord.getUsername() + " duplicate in database!");
            return new ResponseEntity(error, HttpStatus.CONFLICT);
        }
        User user = convertToEntity(userRecord);
        return ResponseEntity.ok().body(convertToDto(userService.addUser(user)));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userRecord, @PathVariable Integer id) throws ResourceNotFoundException {

        User user = userService.getUserInfoById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid user Id:" + id + " !"));

        if (userService.checkDuplicateUsernameBeforeUpdate(user.getUsername(), userRecord.getUsername())) {
            Map<String, String> error = new HashMap<>();
            error.put("username", userRecord.getUsername() + " duplicate in database!");
            return new ResponseEntity(error, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok().body(convertToDto(userService.updateUser(id, convertToEntity(userRecord))));
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Integer id) throws ResourceNotFoundException {
        User userInfo = userService.getUserInfoById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid user Id:" + id + " !"));
        userService.deleteUser(id);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) throws ResourceNotFoundException {
        User userInfo = userService.getUserInfoById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid user Id:" + id + " !"));
        return ResponseEntity.ok().body(convertToDto(userInfo));
    }

    private UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
