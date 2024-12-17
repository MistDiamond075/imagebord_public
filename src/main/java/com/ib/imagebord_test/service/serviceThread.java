package com.ib.imagebord_test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ib.imagebord_test.configuration_app.confPropsCookies;
import com.ib.imagebord_test.configuration_app.confPropsPaths;
import com.ib.imagebord_test.entity.entBords;
import com.ib.imagebord_test.entity.entPostfiles;
import com.ib.imagebord_test.entity.entReplies;
import com.ib.imagebord_test.entity.entThread;
import com.ib.imagebord_test.repository.repBords;
import com.ib.imagebord_test.repository.repThread;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class serviceThread {
    private final repThread rThread;
    private final repBords rBords;
    private final serviceReplies srvReplies;
    private final serviceDbDataSaver srvDbDataSaver;
    private final servicePostfiles srvPostfiles;
    private final serviceAuditJournal srvAuditJournal=new serviceAuditJournal();
    private final confPropsPaths apppaths;
    private final confPropsCookies appcookies;
    private final serviceBanlist srvBanlist;
    private final ObjectMapper objectMapper=new ObjectMapper();

    @Autowired
    public serviceThread(repThread rThread, repBords rBords, serviceReplies srvReplies, serviceDbDataSaver srvDbDataSaver, servicePostfiles srvPostfiles, confPropsPaths apppaths, confPropsCookies appcookies, serviceBanlist srvBanlist) {
        this.rThread = rThread;
        this.rBords = rBords;
        this.srvReplies = srvReplies;
        this.srvDbDataSaver = srvDbDataSaver;
        this.srvPostfiles = srvPostfiles;
        this.apppaths = apppaths;
        this.appcookies = appcookies;
        this.srvBanlist = srvBanlist;
    }

    public entThread getThreadById(Long id){
        entThread thread=rThread.findById(id).orElseThrow(()->new RuntimeException("thread not found when getting by id"));
        thread.setPostcount(srvDbDataSaver.getThreadPostcountByThreadId(thread.getId()));
        return thread;
    }

    public List<entThread> getThreadAll(){
        try{
            List<entThread> threads=(List<entThread>) rThread.findAll();
            for(entThread thread:threads){
                thread.setPostcount(srvDbDataSaver.getThreadPostcountByThreadId(thread.getId()));
            }
        return threads;
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public entThread getThreadByBordId(Long id){
        entThread thread=rThread.findByBordidId(id).orElseThrow(()->new RuntimeException("thread not found when getting by bord id"));
        thread.setPostcount(srvDbDataSaver.getThreadPostcountByThreadId(thread.getId()));
        return thread;
    }

    public List<entThread> getThreadAllByBordName(String bordshortname){
        try{
        return rThread.findAllByBordidName(bordshortname);
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<entThread> getThreadsForBordPage(String bordshortname){
        List<entThread> threads = rThread.findAllByBordidName(bordshortname);
        Collections.reverse(threads);
        threads=sortThreads(threads);

        return threads.stream().peek(thread -> {
            try {
                List<entPostfiles> postfiles;
                List<String> imgpaths=new ArrayList<>();
                entReplies firstReply = srvReplies.getReplyFirstByThreadId(thread.getId());
                postfiles=srvPostfiles.getAllPostfilesByReplyId(firstReply.getId());
                for(entPostfiles files:postfiles) {
                    String paths = files.getPath();
                    imgpaths.add(paths);
                }
                if(firstReply.getRepliers()!=null){
                    String repliers = firstReply.getRepliers();
                    List<String> replierslist = objectMapper.readValue(repliers, new TypeReference<>() {
                    });
                    firstReply.setRepliers_list(replierslist);
                }
                firstReply.setImg_paths(imgpaths);
            thread.setFirstReply(firstReply);
            List<entReplies> latestReplies = srvReplies.getReplyLast3ByThreadId(thread.getId());
            if(srvDbDataSaver.getThreadPostcountByThreadId(thread.getId())<=3){
                latestReplies.remove(latestReplies.size()-1);
            }
                for (entReplies reply : latestReplies) {
                    postfiles=srvPostfiles.getAllPostfilesByReplyId(reply.getId());
                    imgpaths=new ArrayList<>();
                    if (postfiles != null) {
                        for(entPostfiles files:postfiles) {
                            String paths = files.getPath();
                            imgpaths.add(paths);
                        }
                        reply.setImg_paths(imgpaths);
                    }
                    if(reply.getRepliers()!=null) {
                        String repliers = reply.getRepliers();
                        List<String> replierslist = objectMapper.readValue(repliers, new TypeReference<>() {
                        });
                        reply.setRepliers_list(replierslist);
                    }
                    if(reply.getReceiver()!=null){
                        String receiver= reply.getReceiver();
                        List<String> receiverlist=objectMapper.readValue(receiver, new TypeReference<>() {});
                        reply.setReceiver_list(receiverlist);
                    }
                }

            Collections.reverse(latestReplies);
            thread.setLastReplies(latestReplies);
            }catch(JsonProcessingException | RuntimeException e){
                e.printStackTrace();
            }
        }).collect(Collectors.toList());
    }

    @Transactional
    public entThread deleteThreadById(Long id){
        entThread deletedThread=rThread.findById(id).orElseThrow(()->new RuntimeException("thread not found when deleting by id"));
        entBords bord_id=rBords.findById(deletedThread.getBordid().getId()).orElseThrow(()->new RuntimeException("bord not found when deleting thread by id"));
        try{
        rThread.delete(deletedThread);
            srvDbDataSaver.updateActiveThreadsByBordId(bord_id.getId(),false);
            srvDbDataSaver.deleteThreadFromThreadPostcountMap(deletedThread);
            System.out.println("thread "+id+" deleted");
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
        deleteDirectory(apppaths.getResources()+"/static/"+apppaths.getThreadfilesPath()+bord_id.getName()+"/"+deletedThread.getId());
        return deletedThread;
    }

    @Transactional
    public entThread addThread(entThread new_thread, MultipartFile[] attached_files, Long bord_id, Cookie[] cookies,String ipaddr,UserDetails userDetails){
        try {
            if(srvBanlist.checkIp(ipaddr)){
                return null;
            }
            Long bord=srvDbDataSaver.getPostcountByBordId(bord_id);
            if (new_thread != null) {
                String addr="";
                if(cookies!=null) {
                    for(Cookie cookie:cookies) {
                        System.out.println(cookie.getName());
                        if (appcookies.getCookie_id().equals(cookie.getName())) {
                            new_thread.setIpcreator(cookie.getValue());
                            addr=cookie.getValue();
                        }
                    }
                }
                new_thread.setIpcreator(addr);
                new_thread.setBordid(rBords.findById(bord_id).orElseThrow(()->new RuntimeException("bord not found when adding thread")));
                rThread.save(new_thread);
                srvDbDataSaver.updateActiveThreadsByBordId(bord_id,true);
                srvDbDataSaver.addThreadToThreadPostcountMap(new_thread.getId(), 0);
                srvReplies.addReply(new entReplies(null, new_thread, new_thread.getFposttext(), new_thread.getDate(),addr, null, "send",
                        bord, false, objectMapper.writeValueAsString(new ArrayList<String>()),ipaddr),attached_files,new_thread.getId(),false,cookies,ipaddr,userDetails);
            }
        }catch (JsonProcessingException | RuntimeException e){
            e.printStackTrace();
        }
        return new_thread;
    }

    @Transactional
    public entThread updateThread(entThread updthread){
        entThread thread=getThreadById(updthread.getId());
        if(thread!=null) {
            rThread.save(updthread);
        }
        return updthread;
    }

    public entThread updateThreadPinned(Long id, boolean pinned, UserDetails adminuser){
        Optional<entThread> thread_opt=rThread.findById(id);
        if(thread_opt.isPresent()){
            try{
                entThread thread=thread_opt.get();
                thread.setPinned(pinned);
                rThread.save(thread);
                String userrole=adminuser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null);
                srvAuditJournal.addThreadActivityToLog(adminuser.getUsername(),userrole,String.valueOf(thread.getId()),1);
                return thread;
            }catch (RuntimeException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public entThread updateThreadLocked(Long id, boolean locked, UserDetails adminuser){
        Optional<entThread> thread_opt=rThread.findById(id);
        if(thread_opt.isPresent()){
            try{
                entThread thread=thread_opt.get();
                thread.setLocked(locked);
                rThread.save(thread);
                String userrole=adminuser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse(null);
                srvAuditJournal.addThreadActivityToLog(adminuser.getUsername(),userrole,String.valueOf(thread.getId()),2);
                return thread;
            }catch (RuntimeException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private void deleteDirectory(String path){
        try {
            Path imgdir=Path.of(path);
            if(Files.exists(imgdir) && Files.isDirectory(imgdir)) {
                Files.walkFileTree(imgdir, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path imgdir, IOException exc) throws IOException {
                        Files.delete(imgdir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    private List<entThread> sortThreads(List<entThread> threads){
        int j = 0;
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).getPinned()) {
                Collections.swap(threads, i, j);
                j++;
            }
        }
        return threads;
    }
}
