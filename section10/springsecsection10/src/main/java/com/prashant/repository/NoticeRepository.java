package com.prashant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.prashant.model.Notice;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long> {

    // using @Query annotation we can write custom query
    @Query(value = "from Notice n where CURDATE() BETWEEN noticBegDt AND noticEndDt")  // according to this query data is updated not according to prefix
    List<Notice> findAllActiveNotices();

}