package com.finlytics.service;

import com.finlytics.config.SecurityConfig;
import com.finlytics.dto.UserReqDTO;
import com.finlytics.dto.UserResDTO;
import com.finlytics.entity.User;
import com.finlytics.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder; //encode the password before saving to database(encodePassword in SecurityConfig class is mandatory)

    public UserResDTO createUser(UserReqDTO userReqDTO) {
        User user = new User();
        user.setUsername(userReqDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userReqDTO.getPassword()));
        user.setEmail(userReqDTO.getEmail());
        user.setRole(userReqDTO.getRole());
        user.setPhoneNumber(userReqDTO.getPhoneNumber());

        User savedUser = userRepository.save(user); //saved in database
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

    public boolean validateUser(UserReqDTO userReqDTO){
        Optional<User> user = userRepository.findByEmail(userReqDTO.getEmail());
        boolean matchPassword = false;
        boolean matchEmail = false;
        if(user.isPresent()) {
            matchPassword = passwordEncoder.matches(userReqDTO.getPassword(), user.get().getPassword());
            matchEmail = userReqDTO.getEmail().equals(user.get().getEmail());

        }
        return matchPassword && matchEmail;
    }

    public UserResDTO modifyUser(UserReqDTO userReqDTO){

        Optional<User> userDetails = userRepository.findByEmail(userReqDTO.getEmail());
        return userDetails.map(userExist -> {
            if(userReqDTO.getPhoneNumber()!=null && !userReqDTO.getPhoneNumber().isEmpty()){
                userExist.setPhoneNumber(userReqDTO.getPhoneNumber());
            }
            if(userReqDTO.getUsername()!=null && !userReqDTO.getUsername().isEmpty()){
                userExist.setUsername(userReqDTO.getUsername());
            }
            if(userReqDTO.getRole()!=null && !userReqDTO.getRole().isEmpty()){
                userExist.setRole(userReqDTO.getRole());
            }
            if(userReqDTO.getPassword()!=null && !userReqDTO.getPassword().isEmpty()){
                userExist.setPassword(passwordEncoder.encode(userReqDTO.getPassword()));
            }

            User updatedUser = userRepository.save(userExist);

            return mapToUserResDTO(updatedUser);
        }).orElse(null);
    }
}
