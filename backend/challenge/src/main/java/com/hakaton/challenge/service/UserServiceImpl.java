package com.hakaton.challenge.service;
import com.hakaton.challenge.api.User;
import com.hakaton.challenge.domain.UserEntity;
import com.hakaton.challenge.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements  UserService{
    private final UserRepository userRepository;
    public User saveUser(User u)
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(u.getEmail());
        userEntity.setFirstName(u.getFirstname());
        userEntity.setLastName(u.getLastname());
        userEntity  = userRepository.save(userEntity);

        u.setId(userEntity.getId());

        return u;
    }

}
