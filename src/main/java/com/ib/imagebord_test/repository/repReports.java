package com.ib.imagebord_test.repository;

import com.ib.imagebord_test.entity.entReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface repReports extends JpaRepository<entReports,Long> {
    List<entReports> findAllByStatus(entReports.Status status);
    entReports findByStatus(entReports.Status status);
    void deleteAllByStatus(entReports.Status status);
}
