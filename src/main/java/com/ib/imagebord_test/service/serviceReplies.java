package com.ib.imagebord_test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ib.imagebord_test.configuration_app.confPropsCookies;
import com.ib.imagebord_test.configuration_app.confPropsPaths;
import com.ib.imagebord_test.content_management.ImageMGMT;
import com.ib.imagebord_test.content_management.VideoMGMT;
import com.ib.imagebord_test.entity.entBords;
import com.ib.imagebord_test.entity.entPostfiles;
import com.ib.imagebord_test.entity.entReplies;
import com.ib.imagebord_test.repository.repBords;
import com.ib.imagebord_test.repository.repReplies;
import com.ib.imagebord_test.entity.entThread;
import com.ib.imagebord_test.repository.repThread;
import jakarta.servlet.http.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@EnableAsync
public class serviceReplies {
    private final repReplies rReplies;
    private final repThread rThread;
    private final repBords rBords;
    private final servicePostfiles srvPostfiles;
    private final confPropsPaths apppaths;
    private final confPropsCookies appcookies;
    private final serviceDbDataSaver srvDbDataSaver;
    private final serviceBanlist srvBanlist;
    private final serviceTextAutoFormat srvAFT;
    private final serviceAuditJournal srvAuditJournal=new serviceAuditJournal();
    private final ImageMGMT imageMGMT=new ImageMGMT();
    private final VideoMGMT videoMGMT=new VideoMGMT();
    private final ObjectMapper objectMapper=new ObjectMapper();
    private final Safelist text_format_safelist=new Safelist().addTags("span").addAttributes("span", "class", "style");//.addTags("a").addAttributes("a", "href");


    @Autowired
    public serviceReplies(repReplies rReplies, repThread rThread, repBords rBords, servicePostfiles srvPostfiles, confPropsPaths apppaths, confPropsCookies appcookies, serviceDbDataSaver srvDbDataSaver, serviceBanlist srvBanlist, serviceTextAutoFormat srvAFT) {
        this.rReplies = rReplies;
        this.rThread = rThread;
        this.rBords = rBords;
        this.srvPostfiles = srvPostfiles;
        this.apppaths = apppaths;
        this.appcookies = appcookies;
        this.srvDbDataSaver = srvDbDataSaver;
        this.srvBanlist = srvBanlist;
        this.srvAFT = srvAFT;
    }

    public entReplies getReplyById(Long id){
        return rReplies.findById(id).orElseThrow(()->new RuntimeException("reply not found when getting by id"));
    }

    public List<entReplies> getReplyAll(){
        try{
        return (List<entReplies>) rReplies.findAll();
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<entReplies> getRepliesByThreadId(Long id){
        try{
        return rReplies.findAllByThreadid_Id(id);
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public entReplies getReplyFirstByThreadId(Long thread_id){
        return rReplies.findFirstByThreadidId(thread_id).orElseThrow(()->new RuntimeException("reply not found when getting first by thread id"));
    }

    public List<entReplies> getReplyLast3ByThreadId(Long thread_id){
        try{
        return rReplies.findTop3ByThreadidIdOrderByDateDesc(thread_id);
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public entReplies getReplyByThreadIdAndNumber(Long thread_id, String number){
        return rReplies.findByThreadidIdAndNumber(thread_id,Long.parseLong(number)).orElseThrow(()->new RuntimeException("reply not found when getting by thread id and number"));
    }

    @Transactional
    public entReplies deleteReplyById(Long id){
        entReplies delreply=rReplies.findById(id).orElseThrow(()->new RuntimeException("reply not found when deleting by id"));
            rReplies.delete(delreply);
            srvDbDataSaver.updateThreadPostcountByThreadId(delreply.getThreadid().getId(),false);
            deleteFiles(apppaths.getResources() + apppaths.getThreadfilesPath() + rBords.findById(delreply.getThreadid().getBordid().getId()).orElseThrow(()->new RuntimeException("bord not found when deleting reply by id")).getName() + "/" + delreply.getThreadid().getId(), delreply.getId() + "_");
            return delreply;
    }

    @Transactional
    public List<entReplies> deleteRepliesByThreadId(Long id){
        List<entReplies> deletedreplies= rReplies.findAllByThreadid_Id(id);
        entThread thread=rThread.findById(id).orElseThrow(()-> new RuntimeException("thread not found when deleting replies by thread id"));
        if(thread!=null) {
            //rReplies.deleteAllByThreadid(thread);
            }
        return deletedreplies;
    }

    @Transactional
    public entReplies addReply(entReplies new_reply, MultipartFile[] attached_files, Long thread_id, Boolean op, Cookie[] cookie, String ipaddr, UserDetails userDetails){
        try {
            if(srvBanlist.checkIp(ipaddr)){
                return null;
            }
            entThread thread = rThread.findById(thread_id).orElseThrow(()->new RuntimeException("thread not found when adding reply"));
            entBords bord = rBords.findById(thread.getBordid().getId()).orElseThrow(()->new RuntimeException("bord not found when adding reply"));
            boolean isCap = srvDbDataSaver.getThreadPostcountByThreadId(thread_id) + 1 >= thread.getCap();
            boolean isLocked=thread.getLocked();
            if(isLocked) {
                if (userDetails != null) {
                    String userrole = userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .findFirst()
                            .orElse(null);
                    String convertedrole = parseUserRole(userDetails);
                    if (convertedrole!=null && !convertedrole.equals("Аноним ")) {
                        isLocked = false;
                        srvAuditJournal.addThreadActivityToLog(userDetails.getUsername(),userrole,thread_id.toString(),3);
                    }
                }
            }
            if (new_reply != null && !isCap && !isLocked) {
                String addr="";
                if(cookie!=null) {
                    for(Cookie cookie1:cookie) {
                        if (appcookies.getCookie_id().equals(cookie1.getName())) {
                            addr=cookie1.getValue();
                        }
                    }
                }
                new_reply.setNumber(srvDbDataSaver.getPostcountByBordId(bord.getId()));
                srvDbDataSaver.updateThreadPostcountByThreadId(thread_id,true);
                srvDbDataSaver.updatePostcountByBordId(bord.getId());
                new_reply.setIpreplying(addr);
                new_reply.setThreadid(thread);
                new_reply.setOp(false);
                if(op!=null && cookie!=null) {
                            if (op && thread.getIpcreator().equals(addr)){
                                new_reply.setOp(true);
                            }
                }
                new_reply.setIp(ipaddr);
                if(new_reply.getReceiver()!=null){
                    List<String> receiverlist = objectMapper.readValue(new_reply.getReceiver(), new TypeReference<>() {});
                    for(String receiver:receiverlist) {
                        entReplies recv_reply = getReplyByThreadIdAndNumber(thread_id, receiver);
                        if (recv_reply != null && recv_reply.getNumber()==Long.parseLong(receiver)) {
                            List<String> new_repliers = objectMapper.readValue(recv_reply.getRepliers(), new TypeReference<>() {
                            });
                            new_repliers.add(String.valueOf(new_reply.getNumber()));
                            recv_reply.setRepliers(objectMapper.writeValueAsString(new_repliers));
                            rReplies.save(recv_reply);
                            new_repliers.clear();
                        }
                    }
                    new_reply.setReceiver_list(receiverlist);
                }
                new_reply.setRepliers(objectMapper.writeValueAsString(new ArrayList<String>()));
                new_reply.setText(textFormat(new_reply.getText(), bord.getName()));
                if(!new_reply.getPostername().equals("false")) {
                    new_reply.setPostername(parseUserRole(userDetails));
                }else{
                    new_reply.setPostername("Аноним ");
                }
                rReplies.save(new_reply);
                if(attached_files!=null){
                    List<String> imgpath=new ArrayList<>();
                    List<MultipartFile> imgfiles=new ArrayList<>();
                    List<MultipartFile> vidfiles=new ArrayList<>();
                    for(MultipartFile files:attached_files){
                        if(files.getContentType()!=null) {
                            if (files.getContentType().startsWith("image")){
                                imgfiles.add(files);
                            }
                             else if(files.getContentType().startsWith("video")){
                                 vidfiles.add(files);
                            }
                        }
                    }
                    String filepath= apppaths.getResources()+ "/static"+apppaths.getThreadfilesPath()+bord.getName()+"/" +thread_id+"/";
                    if(!imgfiles.isEmpty()) {
                        imgpath = imageMGMT.saveImages(filepath, imgfiles, new_reply.getId(), srvDbDataSaver.getThreadPostcountByThreadId(thread_id), apppaths.getThreadfilesPath() + bord.getName() + "/" + thread_id);
                    }
                    if(!vidfiles.isEmpty()){
                        List<String> vidpath;
                        vidpath=videoMGMT.saveVideos(filepath,vidfiles, new_reply.getId(), srvDbDataSaver.getThreadPostcountByThreadId(thread_id),apppaths.getThreadfilesPath() + bord.getName() + "/" + thread_id);
                        if(vidpath!=null && !vidpath.isEmpty()){
                            imgpath.addAll(vidpath);
                        }
                    }
                    for(String filepaths: imgpath){
                        entPostfiles postfile=new entPostfiles(null,filepaths,filepaths.substring(filepaths.indexOf('.')),new_reply);
                        srvPostfiles.addPostfile(postfile);
                    }
                    new_reply.setImg_paths(imgpath);
                }
                return new_reply;
            }
        }catch (RuntimeException | JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

   public List<entReplies> setRepliesDisplayedData(Long thread_id){
        try {
            List<entReplies> replies=getRepliesByThreadId(thread_id);
            for (entReplies reply : replies) {
                List<entPostfiles> postfiles=srvPostfiles.getAllPostfilesByReplyId(reply.getId());
                List<String> imgpaths=new ArrayList<>();
                if (postfiles != null) {
                    for(entPostfiles files:postfiles) {
                        String paths = files.getPath();
                        imgpaths.add(paths);
                    }
                        reply.setImg_paths(imgpaths);
                }
                if(reply.getRepliers()!=null) {
                    String repliers = reply.getRepliers();
                    List<String> replierslist = objectMapper.readValue(repliers, new TypeReference<>() {});
                    reply.setRepliers_list(replierslist);
                }
                if(reply.getReceiver()!=null){
                    String receiver= reply.getReceiver();
                    List<String> receiverlist=objectMapper.readValue(receiver, new TypeReference<>() {});
                    reply.setReceiver_list(receiverlist);
                }
            }
            return replies;
        }catch(JsonProcessingException | RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    private void deleteFiles(String path,String prefix){
        try {
            Path imgdir = Path.of(path);
            if (Files.exists(imgdir) && Files.isDirectory(imgdir)) {
                Files.walkFileTree(imgdir, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        if (file.getFileName().toString().startsWith(prefix)) {
                            Files.delete(file);
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private String textFormat(String text,String bordshortname){
        Document.OutputSettings settings=new Document.OutputSettings().prettyPrint(false);
        text=Jsoup.clean(text,"", text_format_safelist,settings);
        if(text.contains("&gt;")){
            text=text.replaceAll("(&gt;.*)", "<span class=\"txt-greentext\">$1</span>");
        }
        String newtext= text.replaceAll("\\[strong]", "<span class =\"txt-strong\">")
                .replaceAll("\\[/strong]", "</span>")
                .replaceAll("\\[spoiler]", "<span class=\"txt-spoiler\">")
                .replaceAll("\\[/spoiler]", "</span>")
                .replaceAll("\\[cursive]", "<span class=\"txt-cursive\">")
                .replaceAll("\\[/cursive]", "</span>")
                .replaceAll("(https?://\\\\S+)","<a href=\"$1\" target=\"_blank\">$1</a>");
        Document doc=Jsoup.parse(newtext);
        doc.outputSettings().prettyPrint(false);
        for (Element strongTag : doc.select("strong")) {
            strongTag.tagName("span");
            strongTag.addClass("txt-strong");
        }
        for (Element strongTag : doc.select("spoiler")) {
            strongTag.tagName("span");
            strongTag.addClass("txt-spoiler");
        }
        for (Element strongTag : doc.select("cursive")) {
            strongTag.tagName("span");
            strongTag.addClass("txt-cursive");
        }
        String str_ret=doc.body().html();
        String af_bordname=bordshortname.substring(bordshortname.indexOf('/')+1);
        if(srvAFT.getBordTextAutoFormatState(af_bordname)){
            List<String> patterns=srvAFT.getBordTextAutoFormatList(af_bordname);
            if(patterns!=null){
                    for (String pattern:patterns) {
                        str_ret = str_ret.replace(pattern.substring(0, pattern.indexOf('|')), "<span class=\"txt-replaced\">" + pattern.substring(pattern.indexOf('|') + 1) + "</span>");
                    }
                }
        }
        return str_ret;
    }

    private String parseUserRole(UserDetails userDetails) {
        String role =userDetails!=null ? userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null) : "";
            switch (Objects.requireNonNull(role)) {
                case "ROLE_MAINADMIN" -> {
                    role = "<span class=\"poster-name-madmin\">Главный врач </span>";
                    return role;
                }
                case "ROLE_ADMIN" -> {
                    role = "<span class=\"poster-name-admin\">Врач </span>";
                    return role;
                }
                case "ROLE_MODERATOR" -> {
                    role = "<span class=\"poster-name-moderator\">Санитар </span>";
                    return role;
                }
            }
        role = "Аноним ";
        return role;
    }
}
