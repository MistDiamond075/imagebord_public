package com.ib.imagebord_test.service;

import com.ib.imagebord_test.entity.entReports;
import com.ib.imagebord_test.repository.repReports;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class serviceReports {
    private final repReports rReports;
    private final serviceAuditJournal srvAuditJournal=new serviceAuditJournal();

    public serviceReports(repReports rReports){
        this.rReports=rReports;
    }

    public List<entReports> getReportsAll(){
        try {
            return rReports.findAll();
        }catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    public entReports getReportById(Long id){
        try {
            return rReports.findById(id).orElseThrow(() -> new RuntimeException("report not found when getting by id"));
        }catch (RuntimeException e){
                e.printStackTrace();
                return null;
            }
    }

    public List<entReports> getReportsByStatus(entReports.Status status){
        try {
            Enum.valueOf(entReports.Status.class, status.toString());
            return rReports.findAllByStatus(status);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public entReports deleteReportById(Long id){
        try {
            entReports report = getReportById(id);
            if (report != null) {
                rReports.delete(report);
            }
            return report;
        }catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public void deleteReportsByStatus(entReports.Status status){
        try {
            Enum.valueOf(entReports.Status.class, status.toString());
            rReports.deleteAllByStatus(status);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Transactional
    public entReports addReport(entReports report){
        try {
            rReports.save(report);
            return report;
        }catch (RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public entReports updateReportStatus(Long id, entReports.Status status, UserDetails adminuser){
        Optional<entReports> report_opt=rReports.findById(id);
        if(report_opt.isPresent()){
            try {
                Enum.valueOf(entReports.Status.class, status.toString());
                entReports report = report_opt.get();
                report.setStatus(status);
                rReports.save(report);
                srvAuditJournal.addReportActivityToLog(adminuser.getUsername(),String.valueOf(report.getId()),status.toString());
                return report;
            }catch (IllegalArgumentException e){
                e.printStackTrace();
                return null;
            }
        }else {
            throw new RuntimeException("report not found when updating report status");
        }
    }
}
