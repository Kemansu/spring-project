package com.example.demo.repository;

import com.example.demo.enums.Gender;
import com.example.demo.enums.LifeStatus;
import com.example.demo.model.Account;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalType;
import com.example.demo.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    boolean existsByLocation(Location location);

    boolean existsByAccount(Account account);

    @Query("SELECT a FROM Animal a " +
            "WHERE (:chipperId IS NULL OR a.account.id = :chipperId)" +
            "AND (:startDateTime IS NULL OR a.chippingDateTime >= :startDateTime)" +
            "AND (:endDateTime IS NULL OR a.chippingDateTime <= :endDateTime)" +
            "AND (:chippingLocationId IS NULL OR a.location.id = :chippingLocationId)" +
            "AND (:lifeStatus IS NULL OR a.lifeStatus = :lifeStatus)" +
            "AND (:gender IS NULL OR a.gender = :gender)" +
            "ORDER BY a.id"
    )
    List<Animal> findAnimalsByParams(
            @Param("startDateTime") String startDateTime,
            @Param("endDateTime") String endDateTime,
            @Param("chipperId") Long chipperId,
            @Param("chippingLocationId") Long chippingLocationId,
            @Param("lifeStatus") LifeStatus lifeStatus,
            @Param("gender") Gender gender
    );
}
