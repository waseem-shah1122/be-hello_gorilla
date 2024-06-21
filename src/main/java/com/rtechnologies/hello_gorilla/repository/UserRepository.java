package com.rtechnologies.hello_gorilla.repository;

import com.rtechnologies.hello_gorilla.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByNumber(String number);
}
