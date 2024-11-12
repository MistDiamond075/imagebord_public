package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.service.serviceAuditJournal;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("¯\_(ツ)_/¯")
@Secured({"¯\_(ツ)_/¯"})
public class ctrlAuditJournal {
    private final serviceAuditJournal srvAuditJournal=new serviceAuditJournal();

    @GetMapping("¯\_(ツ)_/¯")
    public @ResponseBody String getAuditJournal(){
        return srvAuditJournal.getAdminLog();
    }
}
