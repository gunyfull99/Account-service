package com.account.repository;

import com.account.entity.Permission;
import com.account.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission,Long> {

    @Query(value = "SELECT  * from permission where id NOT IN (select permissions_id  from accounts_permissions where account_id" +
            " = :id)" +
            "",nativeQuery = true)
    Set<Permission> getUserNotPer(@Param("id") long id);

    @Query(value = "SELECT  * from permission where id  IN (select permissions_id  from accounts_permissions where account_id" +
            " = :id)" +
            "",nativeQuery = true)
    List<Permission> getUserHavePer(@Param("id") long id);

    @Query(value = "SELECT  * from permission where id  IN (select permissions_id  from roles_permissions where roles_id" +
            " = :id)" +
            "",nativeQuery = true)
    List<Permission> getRoleHavePer(@Param("id") long id);

    @Query(value = "SELECT  * from permission where id NOT IN (select permissions_id  from roles_permissions where roles_id" +
            " = :id)" +
            "",nativeQuery = true)
    Set<Permission> getRoleNotPer(@Param("id") long id);
}
