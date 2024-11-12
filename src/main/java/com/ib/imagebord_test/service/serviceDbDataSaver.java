package com.ib.imagebord_test.service;

import com.ib.imagebord_test.entity.entBanlist;
import com.ib.imagebord_test.entity.entBords;
import com.ib.imagebord_test.entity.entThread;
import com.ib.imagebord_test.repository.repBanlist;
import com.ib.imagebord_test.repository.repBords;
import com.ib.imagebord_test.repository.repThread;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class serviceDbDataSaver {
    private final repBords rBords;
    private final repThread rThread;
    private final repBanlist rBanlist;
    private final ConcurrentHashMap<Long,Long> bord_post_count=new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long,Integer> bord_active_threads= new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long,Integer> thread_post_count=new ConcurrentHashMap<>();

    @Autowired
    public serviceDbDataSaver(repBords rBords, repThread rThread, repBanlist rBanlist) {
        this.rBords = rBords;
        this.rThread = rThread;
        this.rBanlist = rBanlist;
    }

    @PostConstruct
    public void init(){
        List<entBords> bordsList=(List<entBords>) rBords.findAll();
        List<entThread> threadList=(List<entThread>) rThread.findAll();
        initBordPostCount(bordsList);
        initBordActiveThreads(bordsList);
        initThreadsPostCount(threadList);
    }

    public Integer getActiveThreadsByBordId(Long bord_id){
        return bord_active_threads.getOrDefault(bord_id,0);
    }

    public void updateActiveThreadsByBordId(Long bord_id,boolean increase){
        if(bord_id!=null && bord_active_threads.containsKey(bord_id)) {
            if (increase) {
                bord_active_threads.merge(bord_id, 1, Integer::sum);
            } else {
                bord_active_threads.merge(bord_id, -1, Integer::sum);
            }
        }
    }

    public  Integer getThreadPostcountByThreadId(Long thread_id){
            return thread_post_count.getOrDefault(thread_id, 0);
    }

    public  void updateThreadPostcountByThreadId(Long thread_id,boolean increase){
        if(thread_id!=null && bord_active_threads.containsKey(thread_id)) {
            if (increase) {
                thread_post_count.merge(thread_id, 1, Integer::sum);
            } else {
                thread_post_count.merge(thread_id, -1, Integer::sum);
            }
        }
    }

    public  Long getPostcountByBordId(Long bord_id){
        return bord_post_count.getOrDefault(bord_id,0L);
    }

    public  void updatePostcountByBordId(Long bord_id){
        if(bord_id!=null && bord_active_threads.containsKey(bord_id)) {
            bord_post_count.merge(bord_id, 1L, Long::sum);
        }
    }

    public void deleteThreadFromThreadPostcountMap(entThread deletedThread){
        if(deletedThread!=null) {
            thread_post_count.remove(deletedThread.getId());
        }
    }

    public void addThreadToThreadPostcountMap(Long thread_id,Integer postcount){
        if(thread_id!=null) {
            thread_post_count.put(thread_id, 0);
        }
    }

    private void initBordPostCount(List<entBords> bordsList){
        for(entBords bord:bordsList){
            bord_post_count.put(bord.getId(),bord.getPostcount());
        }
    }

    private void initBordActiveThreads(List<entBords> bordsList){
        for(entBords bord:bordsList){
            bord_active_threads.put(bord.getId(),bord.getActiveThreads());
        }
    }

    private void initThreadsPostCount(List<entThread> threadList){
        for(entThread thread:threadList){
            thread_post_count.put(thread.getId(),thread.getPostcount());
        }
    }
}
