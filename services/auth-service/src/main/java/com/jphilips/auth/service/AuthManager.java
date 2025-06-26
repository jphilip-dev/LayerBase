package com.jphilips.auth.service;

import com.jphilips.auth.entity.User;
import com.jphilips.auth.exceptions.custom.EmailAlreadyExistException;
import com.jphilips.auth.exceptions.custom.OwnerMismatchException;
import com.jphilips.auth.exceptions.custom.UserInactiveException;
import com.jphilips.auth.exceptions.custom.UserNotFoundException;
import com.jphilips.auth.repository.UserRepository;
import com.jphilips.shared.exceptions.errorcode.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthManager {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public User validateUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(AuthErrorCode.USER_NOT_FOUND));
    }

    public User validateUser(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(AuthErrorCode.USER_NOT_FOUND));
    }

    public void checkEmailAvailability(String email){
        if(userRepository.findByEmail(email.toLowerCase()).isPresent()){
            throw new EmailAlreadyExistException(AuthErrorCode.EMAIL_EXISTS);
        }
    }

    public void checkOwnership(Long headerUserId, Long userId){
        if (!Objects.equals(headerUserId, userId)){
            throw new OwnerMismatchException(AuthErrorCode.OWNERSHIP_MISMATCH);
        }
    }

    public void validateStatus(User user){
        if (!user.getIsActive()){
            throw new UserInactiveException(AuthErrorCode.USER_INACTIVE);
        }
    }
}
