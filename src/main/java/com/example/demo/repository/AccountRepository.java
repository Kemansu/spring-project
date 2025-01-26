package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByEmail(String email);

    boolean existsById(int id);

    boolean existsByEmail(String email);

    @Query("SELECT a FROM account a " +
            "WHERE (:firstName IS NULL OR (LOWER(a.firstName) LIKE CONCAT('%', LOWER(CAST(:firstName AS string)), '%')))" +
            "AND  (:lastName IS NULL OR (LOWER(a.lastName) LIKE CONCAT('%', LOWER(CAST(:lastName AS string)), '%')))" +
            "AND  (:email IS NULL OR (LOWER(a.email) LIKE CONCAT('%', LOWER(CAST(:email AS string)), '%')))" +
            "ORDER BY a.id"
    )
    List<Account> findAccountsByParams(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email
    );
}
