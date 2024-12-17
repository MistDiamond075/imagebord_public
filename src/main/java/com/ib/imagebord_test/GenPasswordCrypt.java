package com.ib.imagebord_test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenPasswordCrypt {
    public String generateCryptedPassword(String password) {
		//here should be algorithm for encryption
        String hashedPassword=password;
        return hashedPassword;
    }
}
