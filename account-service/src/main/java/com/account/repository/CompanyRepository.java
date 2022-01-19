package com.account.repository;

import com.account.entity.Account;
import com.account.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Query(value = "select * from company where id = :id",nativeQuery = true)
    Company findComPanyById(@Param("id") Long id);
}
