package com.ib.imagebord_test.misc;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenPasswordCrypt {
    public String generateCryptedPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //System.out.println(hashedPassword);
        return encoder.encode(password);
    }
}
