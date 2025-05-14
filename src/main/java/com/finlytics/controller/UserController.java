package com.finlytics.controller;

import com.finlytics.dto.UserReqDTO;
import com.finlytics.dto.UserResDTO;
import com.finlytics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController   {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserReqDTO userReqDTO) {

        UserResDTO userDetail = userService.getUserByEmail(userReqDTO);
        if(userDetail!=null){
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message","user already exist");
            return ResponseEntity.badRequest().body(errorResponse);

        }
        UserResDTO createdUser = userService.createUser(userReqDTO);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    public List<UserResDTO> getUsers() {

        return userService.getAllUsers();
    }

}
