package com.tecsup.app.micro.user.controller;

import com.tecsup.app.micro.user.dto.CreateRequestUser;
import com.tecsup.app.micro.user.dto.User;
import com.tecsup.app.micro.user.exception.DuplicateEmailException;
import com.tecsup.app.micro.user.exception.NotFoundUser;
import com.tecsup.app.micro.user.mapper.UserMapper;
import com.tecsup.app.micro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id){
        log.info("REST  request  to get  user by  id :{}",id);
        // return ResponseEntity.ok(userService.getUserById(id));

        if (id == null || id.isBlank() || !id.matches("\\d+")) { // Solo números
            return ResponseEntity
                    .badRequest()
                    .body(" bad ID");
        }
        try{
            Long idReal = Long.parseLong(id);
            User user = userService.getUserById(idReal);
            return ResponseEntity.ok(user);
        }catch (NotFoundUser e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers(){
        log.info("REST  request  to get all  user ");
         return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<?> createUser( @RequestBody CreateRequestUser createRequestUser) {
        User savedEntity=null;
        try {
            if (createRequestUser == null
                    || createRequestUser.getName() == null
                    || createRequestUser.getEmail() == null
                    || createRequestUser.getPhone() == null
                    || createRequestUser.getAddress() == null)
            {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("A field is missing ");
            }
            User newUser=userMapper.toDomainFromInput(createRequestUser);
            savedEntity = userService.saveUser(newUser);
        }catch (DuplicateEmailException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error make the customer");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (id == null || id.isBlank() || !id.matches("\\d+")) { // Solo números
            return ResponseEntity
                    .badRequest()
                    .body(" bad ID ");
        }
        try{
            Long idReal = Long.parseLong(id);
            User existingUser = userService.getUserById(idReal);
            userService.deleteUser(existingUser);
            return ResponseEntity.ok("User Deleted with ID"+idReal);
        }catch (NotFoundUser e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }




}
