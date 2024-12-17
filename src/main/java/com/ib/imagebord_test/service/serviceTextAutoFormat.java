package com.ib.imagebord_test.service;

import com.ib.imagebord_test.entity.entBords;
import com.ib.imagebord_test.repository.repBords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@EnableAsync
public class serviceTextAutoFormat {
    private final repBords rBords;
    private final serviceAuditJournal srvAuditJournal=new serviceAuditJournal();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ConcurrentHashMap<String, Boolean> bord_textautoformat_enabled = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, List<String>> bord_textautoformat_lists = new ConcurrentHashMap<>();

    @Autowired
    serviceTextAutoFormat(repBords rBords) {
        this.rBords = rBords;
        List<entBords> bordsList = (List<entBords>) this.rBords.findAll();
        for (entBords bord : bordsList) {
            initTextautoformatmaps(bord.getName());
        }
    }

    public boolean getBordTextAutoFormatState(String bordshortname) {
        String af_bordname = bordshortname.substring(bordshortname.indexOf('/') + 1);
        return bord_textautoformat_enabled.getOrDefault(af_bordname, false);
    }

    public boolean setBordTextAutoFormatState(boolean textautoformat, String bordshortname, UserDetails userDetails) {
        String af_bordname = bordshortname.substring(bordshortname.indexOf('/') + 1);
        bord_textautoformat_enabled.put(af_bordname, textautoformat);
        String userrole="";
        if(userDetails!=null) {
            userrole = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }
        String username="";
        if(userDetails!=null){
            username=userDetails.getUsername();
        }
        srvAuditJournal.addTextAutoFormatActivityToLog(username,userrole,bordshortname ,textautoformat,1);
        return bord_textautoformat_enabled.get(af_bordname);
    }

    public void setBordTextAutoFormatList(String bordshortname, List<String> new_aft_list,UserDetails userDetails) {
        lock.writeLock().lock();
        String af_bordname = bordshortname.substring(bordshortname.indexOf('/') + 1);
        File file = new File("src/main/files/" + af_bordname + "_textautoformat.txt");
        try (FileWriter writer = new FileWriter(file, false)) {
            for (String str : new_aft_list) {
                writer.write(str + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
        bord_textautoformat_lists.put(af_bordname, new_aft_list);
        String userrole="";
        if(userDetails!=null) {
            userrole = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }
        String username="";
        if(userDetails!=null){
            username=userDetails.getUsername();
        }
        srvAuditJournal.addTextAutoFormatActivityToLog(username,userrole,bordshortname ,false,2);
    }

    public List<String> getBordTextAutoFormatList(String bordshortname) {
        String af_bordname = bordshortname.substring(bordshortname.indexOf('/') + 1);
        return bord_textautoformat_lists.getOrDefault(af_bordname,null);

    }

    private void initTextautoformatmaps(String bordshortname) {
        ArrayList<String> aft_list = new ArrayList<>();
        String af_bordname = bordshortname.substring(bordshortname.indexOf('/') + 1);
        if (Files.exists(Path.of("src/main/files/" + af_bordname + "_textautoformat.txt"))) {
            lock.readLock().lock();
            try (FileReader reader = new FileReader("src/main/files/" + af_bordname + "_textautoformat.txt");
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("|") && line.indexOf('|') != line.length() - 1) {
                        aft_list.add(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.readLock().unlock();
            }
            if (!aft_list.isEmpty()) {
                bord_textautoformat_lists.put(af_bordname, aft_list);
                bord_textautoformat_enabled.put(af_bordname,false);
            }
        }
    }
}