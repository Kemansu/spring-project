package com.example.demo.repository;

import com.example.demo.model.Account;
import com.example.demo.model.Animal;
import com.example.demo.model.AnimalType;
import com.example.demo.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    boolean existsByLocation(Location location);

    boolean existsByAccount(Account account);
}
