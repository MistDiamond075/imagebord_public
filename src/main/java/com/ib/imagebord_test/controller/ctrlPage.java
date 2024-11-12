package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.entity.entBords;
import com.ib.imagebord_test.entity.entReplies;
import com.ib.imagebord_test.entity.entThread;
import com.ib.imagebord_test.service.serviceBords;
import com.ib.imagebord_test.service.serviceReplies;
import com.ib.imagebord_test.service.serviceThread;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @Secured({"¯\_(ツ)_/¯"})
    public String adminrulespage(){return "adminrules";}

    @GetMapping(path = {"¯\_(ツ)_/¯"})
    public String adminloginpage(){return "adminlogin";}

    @Secured({"¯\_(ツ)_/¯"})
    @GetMapping(path ={ "¯\_(ツ)_/¯","¯\_(ツ)_/¯"})
    public String adminpage(){return "adminmenu";}

    @GetMapping({"/{bordname}","/{bordname}/"})
    public String bredpage(Model model, @PathVariable String bordname){
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
        return "samplebord";
    }

    @GetMapping(path={"/{bordname}/thread/{id}","/{bordname}/thread/{id}/"})
    public String bredpageth(@PathVariable Long id,Model model) {
        List<entBords> bords=srvBords.getBordsAll();
        List<entReplies> replies=srvReplies.setRepliesDisplayedData(id);
        model.addAttribute("replies",replies);
        model.addAttribute("bordlist",bords);
        return "samplepage";
    }
}
