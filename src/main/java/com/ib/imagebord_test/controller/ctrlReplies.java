package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.entity.entReplies;
import com.ib.imagebord_test.service.serviceQueue;
import com.ib.imagebord_test.service.serviceReplies;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@EnableAsync
public class ctrlReplies {
    private final serviceReplies srvReplies;
    private final serviceQueue srvQueue;

    @Autowired
    public ctrlReplies(serviceReplies srvReplies, serviceQueue srvQueue) {
        this.srvReplies = srvReplies;
        this.srvQueue = srvQueue;
    }

    @GetMapping(path="¯\_(ツ)_/¯")
    @Secured({"¯\_(ツ)_/¯"})
    public @ResponseBody List<entReplies> getReplies(){
        return srvReplies.getReplyAll();
    }

    @GetMapping(path="¯\_(ツ)_/¯")
    @Secured({"¯\_(ツ)_/¯"})
    public @ResponseBody entReplies getReplyById(@PathVariable Long id){
        return srvReplies.getReplyById(id);
    }

    @GetMapping("¯\_(ツ)_/¯")
    @Secured({"¯\_(ツ)_/¯"})
    public @ResponseBody List<entReplies> getRepliesByThreadId(@PathVariable Long id){
        return srvReplies.getRepliesByThreadId(id);
    }

    @PostMapping(path="addReply/{thread_id}")
    public @ResponseBody CompletableFuture<entReplies> addReply(@RequestPart(value = "files",required = false) MultipartFile[] attached_files, @RequestPart(value = "replyentitydata") entReplies reply, @PathVariable Long thread_id, @RequestParam(value = "isOP") Boolean isOP, HttpServletRequest request){
            String ipaddr=request.getRemoteAddr();
            Cookie[] cookie=request.getCookies();
            return srvQueue.addTask(()-> srvReplies.addReply(reply,attached_files,thread_id,isOP,cookie,ipaddr));
    }

    @DeleteMapping(path="¯\_(ツ)_/¯")
    @Secured({"¯\_(ツ)_/¯"})
    public @ResponseBody entReplies delReply(@PathVariable Long id){
        return srvReplies.deleteReplyById(id);
    }

}
