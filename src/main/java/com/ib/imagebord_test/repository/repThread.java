package com.ib.imagebord_test.repository;


import com.ib.imagebord_test.entity.entThread;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface repThread  extends CrudRepository<entThread,Long> {
     Optional<entThread> findByBordidId(Long bord_id);
     List<entThread> findAllByBordidName(String bordshortname);
}
