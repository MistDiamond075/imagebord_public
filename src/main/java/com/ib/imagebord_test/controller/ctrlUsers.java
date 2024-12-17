package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.entity.entUsers;
import com.ib.imagebord_test.service.serviceUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ADMIN","MAINADMIN"})
@RequestMapping("¯\_(ツ)_/¯")
public class ctrlUsers {
    private final serviceUsers srvUsers;

    @Autowired
    public ctrlUsers(serviceUsers srvUsers) {
        this.srvUsers = srvUsers;
    }

    @GetMapping(path="¯\_(ツ)_/¯")
    public @ResponseBody List<entUsers> getUsers(){
        return srvUsers.getUsersAll();
    }

    @GetMapping(path = "¯\_(ツ)_/¯")
    public @ResponseBody entUsers getUser(@PathVariable Long id){
        return srvUsers.getUserById(id);
    }

    @PostMapping(path="¯\_(ツ)_/¯")
    @Secured("MAINADMIN")
    public @ResponseBody entUsers addUser(@RequestBody entUsers newuser){
        return srvUsers.addUser(newuser);
    }

    @DeleteMapping(path="¯\_(ツ)_/¯")
    @Secured("MAINADMIN")
    public @ResponseBody entUsers delUserByUsername(@PathVariable String username){
        return srvUsers.deleteUserByUsername(username);
    }
}
