package com.ib.imagebord_test.service;

import com.ib.imagebord_test.entity.entBanlist;
import com.ib.imagebord_test.repository.repBanlist;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class serviceBanlist {
    private final repBanlist rBanlist;
    private final Set<String> banlist=ConcurrentHashMap.newKeySet();
    private final serviceAuditJournal srvAuditJournal=new serviceAuditJournal();

    @Autowired
    public serviceBanlist(repBanlist rBanlist){
        this.rBanlist=rBanlist;
    }

    @PostConstruct
    public void init(){
        for(entBanlist ban:getBanlistAll()) banlist.add(ban.getIp());
    }

    public List<entBanlist> getBanlistAll(){
        try {
            return (List<entBanlist>) rBanlist.findAll();
        }catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public entBanlist getBanByIp(String ip){
        return rBanlist.findByIp(ip).orElse(null);
    }

    @Transactional
    public entBanlist removeBan(entBanlist ban){
        try{
        rBanlist.delete(ban);
        deleteBanFromBanlist(ban.getIp());
        return ban;
        }catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public entBanlist addBan(entBanlist ban, UserDetails adminuser){
        try{
        rBanlist.save(ban);
        addBanToBanlist(ban.getIp());
        String userrole=adminuser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
        srvAuditJournal.addBanActivityToLog(adminuser.getUsername(),userrole,ban.getIp(), ban.getCause(), ban.getPeriod(),true);
        return ban;
        }catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    private void addBanToBanlist(String ip){
        banlist.add(ip);
    }

    public Set<String> getLocalBanlist(){
        return banlist;
    }

    public boolean checkIp(String ip){
        return banlist.contains(ip);
    }

    private void deleteBanFromBanlist(String ip){
        banlist.remove(ip);
    }

}
