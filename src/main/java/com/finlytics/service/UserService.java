package com.finlytics.service;

import com.finlytics.dto.UserReqDTO;
import com.finlytics.dto.UserResDTO;
import com.finlytics.entity.User;
import com.finlytics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResDTO createUser(UserReqDTO userReqDTO) {
        User user = new User();
        user.setUsername(userReqDTO.getUsername());
        user.setPassword(userReqDTO.getPassword());
        user.setEmail(userReqDTO.getEmail());
        user.setRole(userReqDTO.getRole());
        user.setPhoneNumber(userReqDTO.getPhoneNumber());

        User savedUser = userRepository.save(user);
        return mapToUserResDTO(savedUser);
    }

    private UserResDTO mapToUserResDTO(User savedUser) {
        UserResDTO userResDTO = new UserResDTO();
        userResDTO.setUsername(savedUser.getUsername());
        userResDTO.setEmail(savedUser.getEmail());
        userResDTO.setRole(savedUser.getRole());
        return userResDTO;
    }

    public List<UserResDTO> getAllUsers(){

        return userRepository.findAll()
                .stream().map(this::mapToUserResDTO).toList();
    }

    public UserResDTO getUserByEmail(UserReqDTO userReqDTO){
        UserResDTO userResDTO = new UserResDTO();
       Optional<User> user = userRepository.findByEmail(userReqDTO.getEmail());
       if (!user.isEmpty()){ //if (user.isPresent())
           userResDTO.setUsername(user.get().getUsername());
           userResDTO.setEmail(user.get().getEmail());
           userResDTO.setRole(user.get().getRole());
       }
       return userResDTO;
    }
}
