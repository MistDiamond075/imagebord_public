package com.ib.imagebord_test.service;

import com.ib.imagebord_test.entity.entUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class serviceSpringSecurity implements UserDetailsService {
    private final serviceUsers srvUsers;

    @Autowired
    public serviceSpringSecurity(serviceUsers srvUsers) {
        this.srvUsers = srvUsers;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        entUsers user = srvUsers.getUserByUsername(username);
        if (user == null) {
            return null;
        }
        if(user.getRole().equals("¯\_(ツ)_/¯") || user.getRole().equals("¯\_(ツ)_/¯")) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    true,
                    true,
                    true,
                    true,
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
                    );
        }
        return null;
    }
}
