package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.entity.entReports;
import com.ib.imagebord_test.service.serviceReports;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@EnableAsync
public class ctrlReports {
    private final serviceReports srvReports;

    public ctrlReports(serviceReports srvReports){
        this.srvReports=srvReports;
    }

    @GetMapping(path = {"¯\_(ツ)_/¯","¯\_(ツ)_/¯"})
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody List<entReports> getReports(){return srvReports.getReportsAll();}

    @GetMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody List<entReports> getReportsByStatus(@PathVariable entReports.Status status){
        return srvReports.getReportsByStatus(status);
    }

    @GetMapping(path = "¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody entReports getReportById(@PathVariable Long id){
        return srvReports.getReportById(id);
    }

    @PatchMapping(path = "¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody entReports updateReportStatus(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @PathVariable entReports.Status status){
        return srvReports.updateReportStatus(id,status,userDetails);
    }

    @PostMapping(path = "/addReport")
    @Async
    public @ResponseBody CompletableFuture<entReports> addReport(@RequestBody entReports report){
        return CompletableFuture.supplyAsync(()-> srvReports.addReport(report));
    }
}
