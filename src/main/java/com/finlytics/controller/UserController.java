package com.finlytics.controller;

import com.finlytics.dto.UserReqDTO;
import com.finlytics.dto.UserResDTO;
import com.finlytics.service.UserService;
import com.finlytics.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController   {

    @Autowired
    private UserService userService;
    //MongoDB -> NoSQL don't need all values from entity should be in request body
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserReqDTO userReqDTO) {
        UserResDTO userDetail = userService.getUserByEmail(userReqDTO);

        if(userDetail.getEmail()!=null){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message","user already exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        UserResDTO createdUser = userService.createUser(userReqDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logicUser(@RequestBody UserReqDTO userReqDTO) {
        Map<String, String> message = new HashMap<>();
        UserResDTO userDetail = userService.getUserByEmail(userReqDTO);
        boolean isValidUser = userService.validateUser(userReqDTO);
        if(isValidUser){

            message.put("message","Welcome! "+userDetail.getUsername());
            message.put("jwtToken",JwtUtil.generateToken(userReqDTO.getEmail()));
            return ResponseEntity.ok(message);
        }
        message.put("error","Authentication failed. Invalid credentials.");
        return ResponseEntity.status(401).body(message);
    }

    @GetMapping
    public List<UserResDTO> getUsers() {

        return userService.getAllUsers();
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modifyUser(@RequestBody UserReqDTO userReqDTO) {

        UserResDTO userResDTO = userService.modifyUser(userReqDTO);
        if(userResDTO!=null){
            return ResponseEntity.ok(userResDTO);
        }
        Map<String, String> error = new HashMap<>();
        error.put("error","No User Found");
        return ResponseEntity.status(404).body(error);
    }

}
