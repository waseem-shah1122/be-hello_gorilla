package com.rtechnologies.hello_gorilla.repository;

import com.rtechnologies.hello_gorilla.entity.MeetUpUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetUpUserRepository extends JpaRepository<MeetUpUserEntity,Long> {
}
