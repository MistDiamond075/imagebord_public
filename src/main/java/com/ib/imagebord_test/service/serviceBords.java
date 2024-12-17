package com.ib.imagebord_test.service;

import com.ib.imagebord_test.entity.entBords;
import com.ib.imagebord_test.repository.repBords;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class serviceBords {
    private final repBords rBords;
    private Set<entBords> bordList= ConcurrentHashMap.newKeySet();
    private final serviceDbDataSaver srvDbDataSaver;

    @Autowired
    public serviceBords(repBords rBords, serviceDbDataSaver srvDbDataSaver) {
        this.rBords = rBords;
        this.srvDbDataSaver = srvDbDataSaver;
    }

    @PostConstruct
    public void init(){
        bordList.addAll(getBordsAll());
    }
    
    public List<entBords> getBordsAll(){
        try{
            List<entBords> bords=(List<entBords>) rBords.findAll();
            for(entBords bord:bords){
                bord.setPostcount(srvDbDataSaver.getPostcountByBordId(bord.getId()));
                bord.setActiveThreads(srvDbDataSaver.getActiveThreadsByBordId(bord.getId()));
            }
        return bords;
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }
    
    public entBords getBordById(Long id){
        entBords bord=rBords.findById(id).orElseThrow(()->new RuntimeException("bord not found when getting by id "+id));
        bord.setPostcount(srvDbDataSaver.getPostcountByBordId(bord.getId()));
        bord.setActiveThreads(srvDbDataSaver.getActiveThreadsByBordId(bord.getId()));
        return bord;
    }

    public entBords getBordByShotname(String shortname){
        entBords bord=rBords.findAllByName(shortname).orElseThrow(()->new RuntimeException("bord not found when getting by shortname "+shortname));
        bord.setPostcount(srvDbDataSaver.getPostcountByBordId(bord.getId()));
        bord.setActiveThreads(srvDbDataSaver.getActiveThreadsByBordId(bord.getId()));
        return bord;
    }

    @Transactional
    public entBords addBord(entBords new_bord){
        try{
        new_bord.setActiveThreads(0);
        rBords.save(new_bord);
        bordList.add(new_bord);
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
         return new_bord;
    }

    @Transactional
    public entBords deleteBordById(Long id){
        entBords deletedBord=rBords.findById(id).orElseThrow(() -> new RuntimeException("bord not found when deleting by id"));
        if(deletedBord!=null) {
            try{
            rBords.delete(deletedBord);
            bordList.remove(deletedBord);
            }catch(RuntimeException e){
                e.printStackTrace();
                return null;
            }
        }
        return deletedBord;
    }

    @Transactional
    public entBords updateBord(entBords bord){
        entBords oldbord=rBords.findById(bord.getId()).orElseThrow(()->new RuntimeException("bord not found when updating"));
        rBords.save(bord);
        bordList.remove(oldbord);
        bordList.add(bord);
        return bord;
    }

    public Set<entBords> getBordList(){
        return bordList;
    }
}
