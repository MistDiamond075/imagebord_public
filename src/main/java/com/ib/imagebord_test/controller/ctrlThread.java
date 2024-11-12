package com.ib.imagebord_test.controller;

import com.ib.imagebord_test.entity.entThread;
import com.ib.imagebord_test.service.serviceQueue;
import com.ib.imagebord_test.service.serviceThread;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.concurrent.CompletableFuture;


@Controller
@EnableAsync
public class ctrlThread {
    private final serviceThread srvThread;
    private final serviceQueue srvQueue;

    @Autowired
    public ctrlThread(serviceThread srvThread, serviceQueue srvQueue) {
        this.srvThread = srvThread;
        this.srvQueue = srvQueue;
    }

    @GetMapping(path = "¯\_(ツ)_/¯")
    @Secured({"¯\_(ツ)_/¯"})
    public @ResponseBody Iterable<entThread> getThread(){
        return srvThread.getThreadAll();
    }

    @GetMapping(path="¯\_(ツ)_/¯")
    @Secured({"¯\_(ツ)_/¯"})
    public @ResponseBody entThread getThreadById(@PathVariable Long id){
        return srvThread.getThreadById(id);
    }

    @PostMapping(path = "addThread/{bord_id}")
    public @ResponseBody CompletableFuture<entThread> addThread(@RequestPart(value = "files",required = false)MultipartFile[] attached_files, @RequestPart(value = "threadentitydata") entThread thread, @PathVariable Long bord_id, HttpServletRequest request){
            String addr=request.getRemoteAddr();
            Cookie[] cookie=request.getCookies();
            return srvQueue.addTask(()-> srvThread.addThread(thread,attached_files,bord_id,cookie,addr));
    }

    @DeleteMapping(path="¯\_(ツ)_/¯")
    @Secured({"¯\_(ツ)_/¯"})
    public @ResponseBody entThread delThread(@PathVariable Long id){
            return srvThread.deleteThreadById(id);
    }

    @PatchMapping("¯\_(ツ)_/¯")
    @Secured("¯\_(ツ)_/¯")
    public @ResponseBody entThread pinThread(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long id,@PathVariable boolean pinned){
        return srvThread.updateThreadPinned(id,pinned,userDetails);
    }

    @PatchMapping("¯\_(ツ)_/¯")
    @Secured("¯\_(ツ)_/¯")
    public @ResponseBody entThread lockThread(@AuthenticationPrincipal UserDetails userDetails,@PathVariable Long id,@PathVariable boolean locked){
        return srvThread.updateThreadLocked(id,locked,userDetails);
    }

    @GetMapping("goToThread/{id}")
    public String goToThread(@PathVariable Integer id){
        return "redirect:"+id.toString();
    }
}
