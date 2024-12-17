package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.entity.entBords;
import com.ib.imagebord_test.entity.entReplies;
import com.ib.imagebord_test.entity.entThread;
import com.ib.imagebord_test.service.serviceBords;
import com.ib.imagebord_test.service.serviceReplies;
import com.ib.imagebord_test.service.serviceThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@Controller
public class ctrlPage {
    private final serviceReplies srvReplies;
    private final serviceThread srvThread;
    private final serviceBords srvBords;

    @Autowired
    public ctrlPage(serviceReplies srvReplies, serviceThread srvThread, serviceBords srvBords) {
        this.srvReplies = srvReplies;
        this.srvThread = srvThread;
        this.srvBords = srvBords;
    }

    @GetMapping(path="/")
    public String mainpage(Model model){
        Set<entBords> bords=srvBords.getBordList();
        model.addAttribute("bordlist",bords);
        return "index";
    }

    @GetMapping(path = "/rules")
    public String rulespage(){return "rules";}

    @GetMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public String adminrulespage(){return "adminrules";}

    @GetMapping(path = {"¯\_(ツ)_/¯"})
    public String adminloginpage(){return "adminlogin";}

    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    @GetMapping(path ={ "¯\_(ツ)_/¯","¯\_(ツ)_/¯"})
    public String adminpage(Model model){
        model.addAttribute("bords",srvBords.getBordList());
        return "adminmenu";}

    @GetMapping({"/{bordname}","/{bordname}/"})
    public String bredpage(Model model, @PathVariable String bordname, @AuthenticationPrincipal UserDetails userDetails){
        if (bordname.equals("favicon.png")||bordname.equals("favicon.ico")) {
            System.out.println("favicon");
        return null;
        }
        List<entThread> threads=srvThread.getThreadsForBordPage("/"+bordname);
        Set<entBords> bords=srvBords.getBordList();
        entBords bord=srvBords.getBordByShotname("/"+bordname);
        model.addAttribute("threads",threads);
        model.addAttribute("bord",bord);
        model.addAttribute("bordlist",bords);
        String userrole=null;
        if(userDetails!=null) {
            userrole = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }
        model.addAttribute("user",userrole);
        return "samplebord";
    }

    @GetMapping(path={"/{bordname}/thread/{id}","/{bordname}/thread/{id}/"})
    public String bredpageth(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<entBords> bords=srvBords.getBordsAll();
        List<entReplies> replies=srvReplies.setRepliesDisplayedData(id);
        model.addAttribute("replies",replies);
        model.addAttribute("bordlist",bords);
        String userrole=null;
        if(userDetails!=null) {
            userrole = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }
        model.addAttribute("user",userrole);
        return "samplepage";
    }
}
