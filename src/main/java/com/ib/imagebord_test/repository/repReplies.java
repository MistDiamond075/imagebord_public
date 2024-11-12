package com.ib.imagebord_test.repository;

import com.ib.imagebord_test.entity.entReplies;
import com.ib.imagebord_test.entity.entThread;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface repReplies extends CrudRepository<entReplies,Long> {
    void deleteAllByThreadid(entThread thread_id);
    List<entReplies> findAllByThreadid_Id(Long thread_id);
    Optional<entReplies> findFirstByThreadidId(Long thread_id);
    List<entReplies> findTop3ByThreadidIdOrderByDateDesc(Long thread_id);
    Optional<entReplies> findByThreadidIdAndNumber(Long thread_id, Long number);
}
