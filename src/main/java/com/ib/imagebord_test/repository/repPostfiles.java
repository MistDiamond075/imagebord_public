package com.ib.imagebord_test.repository;

import com.ib.imagebord_test.entity.entPostfiles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface repPostfiles extends CrudRepository<entPostfiles,Long> {
    List<entPostfiles> findAllByRepliesidId(Long reply_id);
    Optional<Long> findTopByOrderByIdDesc();
}
