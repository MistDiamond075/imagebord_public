package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.entity.entReplies;
import com.ib.imagebord_test.service.serviceQueue;
import com.ib.imagebord_test.service.serviceReplies;
import com.ib.imagebord_test.service.serviceTextAutoFormat;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@EnableAsync
public class ctrlReplies {
    private final serviceReplies srvReplies;
    private final serviceQueue srvQueue;
    private final serviceTextAutoFormat srvAFT;

    @Autowired
    public ctrlReplies(serviceReplies srvReplies, serviceQueue srvQueue, serviceTextAutoFormat srvAFT) {
        this.srvReplies = srvReplies;
        this.srvQueue = srvQueue;
        this.srvAFT = srvAFT;
    }

    @GetMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody List<entReplies> getReplies(){
        return srvReplies.getReplyAll();
    }

    @GetMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody entReplies getReplyById(@PathVariable Long id){
        return srvReplies.getReplyById(id);
    }

    @GetMapping("¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody List<entReplies> getRepliesByThreadId(@PathVariable Long id){
        return srvReplies.getRepliesByThreadId(id);
    }

    @PostMapping(path="addReply/{thread_id}")
    public @ResponseBody CompletableFuture<entReplies> addReply(@RequestPart(value = "files",required = false) MultipartFile[] attached_files, @RequestPart(value = "replyentitydata") entReplies reply, @PathVariable Long thread_id, @RequestParam(value = "isOP") Boolean isOP, HttpServletRequest request,@AuthenticationPrincipal UserDetails userDetails){
            String ipaddr=request.getRemoteAddr();
            Cookie[] cookie=request.getCookies();
            return srvQueue.addTask(()-> srvReplies.addReply(reply,attached_files,thread_id,isOP,cookie,ipaddr,userDetails));
    }

    @DeleteMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody entReplies delReply(@PathVariable Long id){
        return srvReplies.deleteReplyById(id);
    }

    @PostMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MAINADMIN"})
    public @ResponseBody String setTextAutoformat(@RequestParam boolean isAutoformat,@PathVariable String bordshortname,@AuthenticationPrincipal UserDetails userDetails){
        bordshortname='/'+bordshortname;
        return "Textautoformat set "+srvAFT.setBordTextAutoFormatState(isAutoformat,bordshortname,userDetails);
    }

    @PostMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MAINADMIN"})
    public @ResponseBody List<String> setTextAutoformatList(@PathVariable String bordshortname, @RequestBody List<String> wordlist,@AuthenticationPrincipal UserDetails userDetails){
        bordshortname='/'+bordshortname;
         srvAFT.setBordTextAutoFormatList(bordshortname,wordlist,userDetails);
         return srvAFT.getBordTextAutoFormatList(bordshortname);
    }

    @GetMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody List<String> getTextAutoFormatList(@PathVariable String bordshortname){
        bordshortname='/'+bordshortname;
        return srvAFT.getBordTextAutoFormatList(bordshortname);
    }

    @GetMapping(path="¯\_(ツ)_/¯")
    @Secured({"ADMIN","MODERATOR","MAINADMIN"})
    public @ResponseBody boolean getTextAutoFormat(@PathVariable String bordshortname){
        bordshortname='/'+bordshortname;
        return srvAFT.getBordTextAutoFormatState(bordshortname);
    }
}
