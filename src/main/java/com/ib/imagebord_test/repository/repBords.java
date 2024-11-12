package com.ib.imagebord_test.repository;

import com.ib.imagebord_test.entity.entBords;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface repBords extends CrudRepository<entBords,Long> {
    Optional<entBords> findAllByName(String name);
}
