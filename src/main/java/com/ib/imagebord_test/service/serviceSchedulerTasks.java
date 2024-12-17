package com.ib.imagebord_test.service;

import com.ib.imagebord_test.entity.entBanlist;
import com.ib.imagebord_test.entity.entBords;
import com.ib.imagebord_test.entity.entReports;
import com.ib.imagebord_test.entity.entThread;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

@Service
@EnableAsync
@EnableScheduling
public class serviceSchedulerTasks {
    private final serviceBords srvBords;
    private final serviceDbDataSaver srvDbDataSaver;
    private final  serviceThread srvThread;
    private final serviceBanlist srvBanlist;
    private final serviceReports srvReports;
    private final servicePostfiles srvPostfiles;
    private final serviceTextAutoFormat srvAFT;
    private final serviceAuditJournal srvAuditJournal=new serviceAuditJournal();
    private final String time_pattern="dd.MM.yyyy HH:mm:ss";
    private final String ban_period_time_pattern="yyyy-MM-dd";
    private final int active_threads_cap=7;

    @Autowired
    public serviceSchedulerTasks(serviceBords srvBords, serviceDbDataSaver srvDbDataSaver, serviceThread srvThread, serviceBanlist srvBanlist, serviceReports srvReports, servicePostfiles srvPostfiles, serviceTextAutoFormat srvAFT) {
        this.srvBords = srvBords;
        this.srvDbDataSaver=srvDbDataSaver;
        this.srvThread=srvThread;
        this.srvBanlist = srvBanlist;
        this.srvReports = srvReports;
        this.srvPostfiles = srvPostfiles;
        this.srvAFT = srvAFT;
    }

    @Async
    @Scheduled(cron = "${scheduled.timer.cron.clearreports}")
    public void removeDoneReports(){
        srvReports.deleteReportsByStatus(entReports.Status.DONE);
    }

    @Async
    @Scheduled(cron="${scheduled.timer.cron.checkbans}")
    public void checkOutdatedBans(){
        List<entBanlist> bans=srvBanlist.getBanlistAll();
        try{
            for(entBanlist ban:bans) {
                LocalDate current_date = LocalDate.parse(getCurrentDate(ban_period_time_pattern), DateTimeFormatter.ISO_DATE);
                LocalDate ban_end_date = LocalDate.parse(ban.getPeriod(), DateTimeFormatter.ISO_DATE);
                if(current_date.isEqual(ban_end_date)){
                   srvBanlist.removeBan(ban);
                }
            }
        }catch (DateTimeException e) {
            e.printStackTrace();
        }
    }

    @Async
    @Scheduled(cron = "${scheduled.timer.cron.postfilesidcheck}")
    public void checkPostfilesIdInDb(){
        if(srvPostfiles.getMaxId()>=4290967000L){
            srvAuditJournal.addPostfilesIdWarning();
        }
    }

    @Async
    @Scheduled(cron = "${scheduled.timer.cron.clearauditjournal}")
    public void clearAuditJournal(){
        srvAuditJournal.clearJournal();
    }

    @Async
    @Scheduled(fixedRateString = "${scheduled.timer.updpostcount}")
    public void updateBordsPostcount() {
        List<entBords> bords = srvBords.getBordsAll();
        for (entBords bord : bords) {
            if (bord != null){
                bord.setPostcount(srvDbDataSaver.getPostcountByBordId(bord.getId()));
                srvBords.updateBord(bord);
            }
        }
    }

    @Async
    @Scheduled(fixedRateString = "${scheduled.timer.updactivethreads}")
    public void updateBordsActiveThreadsCount(){
        List<entBords> bords = srvBords.getBordsAll();
        for (entBords bord : bords) {
            if (bord != null){
                bord.setActiveThreads(srvDbDataSaver.getActiveThreadsByBordId(bord.getId()));
                srvBords.updateBord(bord);
            }
        }
    }

    @Async
    @Scheduled(fixedRateString = "${scheduled.timer.updthreadpostcount}")
    public void updateThreadPostcount(){
        List<entThread> threads=srvThread.getThreadAll();
        for(entThread thread:threads){
            if(thread!=null){
                thread.setPostcount(srvDbDataSaver.getThreadPostcountByThreadId(thread.getId()));
                srvThread.updateThread(thread);
            }
        }
    }

    @Async
    @Scheduled(fixedRateString = "${scheduled.timer.clroldthreadsbpage}")
    public void deleteOldThreadsFromBpage(){
        List<entThread> threads=srvThread.getThreadAll();
        try {
            DateTimeFormatter formatter=new DateTimeFormatterBuilder()
                    .appendPattern(time_pattern)
                    .toFormatter();
        for (entThread thread:threads){
            if(!thread.getPinned()) {
                    LocalDateTime current_date = LocalDateTime.parse(getCurrentDate(time_pattern),formatter);
                    LocalDateTime thread_date = LocalDateTime.parse(thread.getDate(),formatter);
                   Duration different = Duration.between(thread_date, current_date);
                    if (different.toDays() >= 2) {
                        if (!thread.getPinned() && srvDbDataSaver.getActiveThreadsByBordId(thread.getBordid().getId())>active_threads_cap) {
                             srvThread.deleteThreadById(thread.getId());
                        }
                    }
                }
            }
        } catch (DateTimeException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void onShutdown(){
        updateBordsPostcount();
        updateBordsActiveThreadsCount();
        updateThreadPostcount();
    }

    private String getCurrentDate(String pattern){
        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
}
