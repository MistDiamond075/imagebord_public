package com.ib.imagebord_test.service;

import com.ib.imagebord_test.misc.GenPasswordCrypt;
import com.ib.imagebord_test.entity.entUsers;
import com.ib.imagebord_test.repository.repUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class serviceUsers {
    private final repUsers rUsers;
    private final GenPasswordCrypt pwdgenerator=new GenPasswordCrypt();

    @Autowired
    public serviceUsers(repUsers rUsers) {
        this.rUsers = rUsers;
    }

    public List<entUsers> getUsersAll(){
        try{
        return (List<entUsers>) rUsers.findAll();
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public entUsers getUserById(Long id){
        return rUsers.findById(id).orElseThrow(()->new RuntimeException("user not found when getting by id"));
    }

    public entUsers getUserByUsername(String username){
        return rUsers.findByUsername(username).orElseThrow(()->new RuntimeException("user not found when getting by username"));
    }

    @Transactional
    public entUsers addUser(entUsers new_user){
        try{
        String newpass=pwdgenerator.generateCryptedPassword(Objects.requireNonNull(new_user.getPassword()));
        new_user.setPassword(newpass);
        rUsers.save(new_user);
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
        return new_user;
    }

    @Transactional
    public entUsers deleteUserByUsername(String username){
        entUsers user=rUsers.findByUsername(username).orElseThrow(()->new RuntimeException("user not found when deleting by username"));
        if(user!=null) {
            try{
            rUsers.delete(user);
            }catch(RuntimeException e){
                e.printStackTrace();
                return null;
            }
        }
        return user;
    }
}
