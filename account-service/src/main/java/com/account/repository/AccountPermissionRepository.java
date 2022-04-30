package com.account.repository;

import com.account.entity.AccountPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface AccountPermissionRepository extends JpaRepository<AccountPermission,Long> {

    @Query(value = "select * FROM accounts_permissions WHERE account_id = :id", nativeQuery = true)
    List<AccountPermission> findPerByUserId(@Param("id") long id);

    @Modifying
    @Query(value = "UPDATE accounts_permissions SET can_create = :create,can_update= :update,can_read= :read WHERE account_id = :user and permissions_id = :per",nativeQuery = true)
    void updatePerInUser(@Param("create") String create,@Param("update") String update,@Param("read") String read,
                         @Param("user") long user,@Param("per") long per);


    @Query(value = "select * from accounts_permissions WHERE account_id = :id and permissions_id = :idP", nativeQuery = true)
    AccountPermission getDetailPerInUser(@Param("id") long id,@Param("idP") long idP);
}
