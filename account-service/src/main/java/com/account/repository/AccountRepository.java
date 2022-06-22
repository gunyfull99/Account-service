package com.account.repository;

import com.account.entity.Account;
import com.account.entity.AccountPermission;
import com.account.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT  * from accounts where username= :username", nativeQuery = true)
    Account findByUsername(@Param("username") String username);

    @Query(value = "SELECT  * from accounts where id= :id", nativeQuery = true)
    Account selectById(@Param("id") Long id);

    @Query(value = "insert into accounts_permissions VALUES(:acId,:canCreate,:canRead,:canUpdate,:perId)", nativeQuery = true)
    void addPermission2User(@Param("acId") Long acId, @Param("canCreate") boolean canCreate,
                            @Param("canRead") boolean canRead,
                            @Param("canUpdate") boolean canUpdate,
                            @Param("perId") Long perId);

    @Query(value = "SELECT  full_name from accounts where id= :id", nativeQuery = true)
    String findNameByUserId(@Param("id") long id);

    @Query(value = "select * from accounts  where username   LIKE %:name% or full_name  LIKE %:name% ", nativeQuery = true)
    List<Account> searchUser(@Param("name") String name);

    @Query(value = "select id from accounts  where  full_name  LIKE %:name% ", nativeQuery = true)
    List<Long> getListUserId(@Param("name") String name);

    Account findByEmail(String email);

    Page<Account> findAllByRolesId(long id, Pageable p);

    Page<Account> findAllByUserType(String userType, Pageable p);

    Page<Account> findAllByFullNameContainingIgnoreCase(String name, Pageable p);

    Page<Account> findAllByFullNameContainingIgnoreCaseAndRolesIdAndIsActive(String name, long roleId,boolean isActive, Pageable p);

    Page<Account> findAllByFullNameContainingIgnoreCaseAndUserType(String name, String userType, Pageable p);

    Page<Account> findAllByFullNameContainingIgnoreCaseAndRolesIdAndUserType(String name, long roleId, String userType, Pageable p);

    // @Query(value = "SELECT * FROM accounts :role  :name  :type ", nativeQuery = true)
    @Query(value = "SELECT * FROM accounts \n" +
            "inner join accounts_roles on accounts.id= accounts_roles.account_id and accounts_roles.roles_id= :role \n" +
            " where lower(accounts.user_type) like :type and lower(accounts.full_name) like %:name%  and is_active=true  ", nativeQuery = true)
    Page<Account> filterWhereHaveRoleAndType(@Param("name") String name,
                                      @Param("role") long roleId
            , @Param("type") String type
            , Pageable pageable);



    @Query(value = "SELECT * FROM accounts \n" +
            " where lower(user_type) like :type and lower(full_name) like %:name%  and is_active=true  ", nativeQuery = true)
    Page<Account> filterWhereNoRole(@Param("name") String name,
             @Param("type") String type
            , Pageable pageable);

}
