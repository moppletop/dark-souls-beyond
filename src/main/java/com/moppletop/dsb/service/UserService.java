package com.moppletop.dsb.service;

import com.moppletop.dsb.domain.dto.user.User;
import com.moppletop.dsb.domain.entity.UserEntity;
import com.moppletop.dsb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String name, String password) {
        UserEntity userEntity = new UserEntity();

        userEntity.setName(name);
        userEntity.setPassword(password);

        userEntity = userRepository.save(userEntity);

        return new User(userEntity.getId(), userEntity.getName(), passwordEncoder.encode(userEntity.getPassword()));
    }

    public Optional<User> findUserByName(String name) {
        return userRepository.findByName(name)
                .map(userEntity -> new User(
                        userEntity.getId(),
                        userEntity.getName(),
                        passwordEncoder.encode(userEntity.getPassword())
                ));
    }

}
