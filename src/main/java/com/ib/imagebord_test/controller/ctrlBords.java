package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.entity.entBords;
import com.ib.imagebord_test.service.serviceBords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ctrlBords {
    private final serviceBords srvBords;

    @Autowired
    public ctrlBords(serviceBords srvBords) {
        this.srvBords = srvBords;
    }

    @GetMapping(path="/getBords")
    public @ResponseBody Iterable<entBords> getBords(){
        return srvBords.getBordsAll();
    }

    @GetMapping(path="/getBord/{id}")
    public @ResponseBody entBords getBordById(@PathVariable Long id){
        return srvBords.getBordById(id);
    }

    @PostMapping(path = "¯\_(ツ)_/¯")
    @Secured({"¯\_(ツ)_/¯"})
    public @ResponseBody entBords addBord(@RequestBody entBords bord){
        return srvBords.addBord(bord);
    }

    @DeleteMapping(path = "¯\_(ツ)_/¯")
    @Secured({"¯\_(ツ)_/¯"})
    public entBords delBord(@PathVariable Long id){
        return srvBords.deleteBordById(id);
    }
}
