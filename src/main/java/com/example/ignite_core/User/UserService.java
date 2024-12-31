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

    public Optional<UserEntity> findUserById(Long id){
       return userRepository.findById(id);
    }

    public Optional<UserEntity> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserEntity updateUser(UserEntity user, Long id){

        UserEntity updatedUser = userRepository.findById(id)
                .map(existingUser -> updateExistingUser(existingUser,user))
                .orElseThrow(() -> new InvalidUserException("Invalid User Exception With Id: "+id));
        return userRepository.save(user);
    }

    public UserEntity updateExistingUser(UserEntity existingUser, UserEntity newUserDetails){
        existingUser.setName(newUserDetails.getName());
        existingUser.setEmail(newUserDetails.getEmail());
        existingUser.setPassword(newUserDetails.getPassword());
        existingUser.setAge(newUserDetails.getAge());
        existingUser.setSex(newUserDetails.getSex());
        existingUser.setHeight(newUserDetails.getHeight());
        existingUser.setWeight(newUserDetails.getWeight());
        existingUser.setAllergies(newUserDetails.getAllergies());

        return newUserDetails;
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public void addAllergies(List<String> allergies, Long id){
        UserEntity user = existingUser(id);
        for (String allergy : allergies){
            user.getAllergies().add(allergy);
        }
        userRepository.save(user);
    }

    public void deleteAllergies(Long id){
        UserEntity user = existingUser(id);
        user.setAllergies(null);
    }

    public UserEntity existingUser(Long id){
       return userRepository.findById(id).orElseThrow(() -> new InvalidUserException("Invalid User Exception With Id: "+id));
    }

}
