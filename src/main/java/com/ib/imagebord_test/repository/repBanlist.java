package com.ib.imagebord_test.repository;

import com.ib.imagebord_test.entity.entBanlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface repBanlist extends CrudRepository<entBanlist,Long> {
    Optional<entBanlist> findByIp(String ip);
}
