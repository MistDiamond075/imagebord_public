package com.ib.imagebord_test.repository;

import com.ib.imagebord_test.entity.entUsers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface repUsers  extends CrudRepository<entUsers,Long> {
    Optional<entUsers> findByUsernameAndPassword(String username,String password);
    Optional<entUsers> findByUsername(String username);
    void deleteByUsername(String username);
}
