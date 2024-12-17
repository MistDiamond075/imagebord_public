package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.entity.entBanlist;
import com.ib.imagebord_test.service.serviceBanlist;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ADMIN","MODERATOR","MAINADMIN"})
@RequestMapping(path = "¯\_(ツ)_/¯")
public class ctrlBanlist {
    private final serviceBanlist srvBanlist;

    public ctrlBanlist(serviceBanlist srvBanlist) {
        this.srvBanlist = srvBanlist;
    }

    @GetMapping(path = "¯\_(ツ)_/¯")
    public @ResponseBody List<entBanlist> getBanlist(){
        return srvBanlist.getBanlistAll();
    }

    @PostMapping(path = "¯\_(ツ)_/¯")
    @Secured({"ADMIN","MAINADMIN"})
    public @ResponseBody entBanlist addBan(@AuthenticationPrincipal UserDetails userDetails, @RequestBody entBanlist ban){return srvBanlist.addBan(ban,userDetails);}

    @DeleteMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MAINADMIN"})
    public @ResponseBody entBanlist deleteBanlist(@AuthenticationPrincipal UserDetails userDetails,@RequestBody entBanlist ban){return srvBanlist.removeBan(ban);}
}
