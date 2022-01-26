package com.account.repository;

import com.account.entity.Account;
import com.account.entity.AccountPermission;
import com.account.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT  * from accounts where username= :username", nativeQuery = true)
    Account findByUsername(@Param("username") String username);



    @Query(value = "insert into accounts_permissions VALUES(:acId,:canCreate,:canRead,:canUpdate,:perId)", nativeQuery = true)
    void addPermission2User(@Param("acId") Long acId, @Param("canCreate") boolean canCreate,
                            @Param("canRead") boolean canRead,
                            @Param("canUpdate") boolean canUpdate,
                            @Param("perId") Long perId);

    @Query(value = "SELECT  full_name from accounts where id= :id", nativeQuery = true)
    String findNameByUserId(@Param("id") long id);
}
