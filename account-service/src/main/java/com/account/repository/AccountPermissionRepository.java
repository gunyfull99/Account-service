package com.account.repository;

import com.account.entity.AccountPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface AccountPermissionRepository extends JpaRepository<AccountPermission,Long> {

    @Query(value = "select * FROM accounts_permissions WHERE account_id = :id", nativeQuery = true)
    List<AccountPermission> findPerByUserId(@Param("id") long id);
}
