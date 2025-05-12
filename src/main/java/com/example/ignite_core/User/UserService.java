package com.example.ignite_core.User;
import com.example.ignite_core.User.Model.UpdateUserRequest;
import com.example.ignite_core.User.Model.UserEntity;
import com.example.ignite_core.Utlility.InvalidUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public static final Logger log = LoggerFactory.getLogger(UserService.class);
    UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers(){
        try {
            log.info("Get All Users");
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Exception in UserService getAllUsers()", e);
        }
    }

    public Optional<UserEntity> findUserById(Long id){
        if(existingUser(id) == null){
            throw new InvalidUserException("Invalid User Exception With Id: "+id);
        }
        log.info("Find User By Id:  {}", id);
        return userRepository.findById(id);
    }

    public Optional<UserEntity> findByEmail(String email){

        log.info("Find User By Email:  {}", email);
        return userRepository.findByEmail(email);
    }

    public UserEntity updateUser(UpdateUserRequest user, Long id){

        UserEntity updatedUser = userRepository.findById(id)
                .map(existingUser -> updateExistingUser(existingUser,user))
                .orElseThrow(() -> new InvalidUserException("Invalid User Exception With Id: "+id));
        return userRepository.save(updatedUser);
    }

    public UserEntity updateExistingUser(UserEntity existingUser, UpdateUserRequest newUserDetails){
        existingUser.setName(newUserDetails.getName());
        existingUser.setAge(newUserDetails.getAge());
        existingUser.setSex(newUserDetails.isSex());
        existingUser.setHeight(newUserDetails.getHeight());
        existingUser.setWeight(newUserDetails.getWeight());

        return existingUser;
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public UserEntity existingUser(Long id){
       return userRepository.findById(id).orElseThrow(() -> new InvalidUserException("Invalid User Exception With Id: "+id));
    }

}
