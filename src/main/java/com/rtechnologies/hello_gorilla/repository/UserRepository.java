package com.rtechnologies.hello_gorilla.repository;

import com.rtechnologies.hello_gorilla.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByNumber(String number);

//    @Query(value = "SELECT *, (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(latitude)))) AS distance FROM UserEntity HAVING distance < 5", nativeQuery = true)
//    List<UserEntity> findUsersWithinRadius(Double latitude, Double longitude);
}
