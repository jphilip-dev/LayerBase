package com.jphilips.userdetails.service;

import com.jphilips.shared.exceptions.errorcode.UserDetailsErrorCode;
import com.jphilips.userdetails.entity.UserDetails;
import com.jphilips.userdetails.exceptions.customs.OwnershipMismatchException;
import com.jphilips.userdetails.exceptions.customs.UserDetailsNotFoundException;
import com.jphilips.userdetails.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserDetailsManager {

    private final UserDetailsRepository userDetailsRepository;

    public UserDetails save(UserDetails userDetails){
        return userDetailsRepository.save(userDetails);
    }

    public void delete(UserDetails userDetails){
        userDetailsRepository.delete(userDetails);
    }

    public UserDetails validateById(Long id){
        return userDetailsRepository.findById(id)
                .orElseThrow(() -> new UserDetailsNotFoundException(UserDetailsErrorCode.NOT_FOUND));
    }

    public void ownershipCheck(Long headerUserId, Long userDetailsId){
        if(!Objects.equals(headerUserId, userDetailsId)){
            throw new OwnershipMismatchException(UserDetailsErrorCode.OWNERSHIP_MISMATCH);
        }
    }
}
