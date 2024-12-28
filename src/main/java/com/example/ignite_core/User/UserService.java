package com.example.ignite_core.User;

import com.example.ignite_core.Utlility.InvalidUserException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers(){
        return userRepository.findAll();
    }

    public UserEntity createUser(UserEntity user){
        if(user == null){
            throw new InvalidUserException("Invalid User Exception");
        }
        return userRepository.save(user);
    }

    public Optional<UserEntity> findUserByName(String name){
       return userRepository.findByName(name);
    }

    public Optional<UserEntity> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserEntity updateUser(UserEntity user, Long id){

        return userRepository.findById(id)
                .map(existingUser -> updateExistingUser(existingUser,user))
                .orElseThrow(() -> new InvalidUserException("Invalid User Exception With Id: "+id));
    }

    public UserEntity updateExistingUser(UserEntity existingUser, UserEntity newUserDetails){
        existingUser.setName(newUserDetails.getName());
        existingUser.setEmail(newUserDetails.getEmail());
        existingUser.setPassword(newUserDetails.getPassword());
        existingUser.setAge(newUserDetails.getAge());
        existingUser.setSex(newUserDetails.getSex());

        return newUserDetails;
    }
}
