package com.example.ignite_core.User;
import com.example.ignite_core.Utlility.InvalidUserException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void addAllergies(ArrayList<String> allergies, Long id){
        UserEntity user = existingUser(id);
        for (String allergy : allergies){
            if(!user.getAllergies().contains(allergy)){
                user.getAllergies().add(allergy);
            }
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteAllergies(ArrayList<String> allergies, Long id){
        UserEntity user = existingUser(id);
        List<String> userAllergies = new ArrayList<>(user.getAllergies());

        userAllergies.removeIf(allergy -> allergies.stream()
                .anyMatch(a -> a.trim().equalsIgnoreCase(allergy.trim())));

        System.out.println("alllergies: "+ userAllergies );
        user.setAllergies(userAllergies);
        userRepository.save(user);
    }

    public void updateAllergies(ArrayList<String> allergies, Long id){
        UserEntity user = existingUser(id);

        user.setAllergies(new ArrayList<>());

        for (String allergy : allergies) {
            user.getAllergies().add(allergy);
        }

        userRepository.save(user);
    }

    public UserEntity existingUser(Long id){
       return userRepository.findById(id).orElseThrow(() -> new InvalidUserException("Invalid User Exception With Id: "+id));
    }

}
