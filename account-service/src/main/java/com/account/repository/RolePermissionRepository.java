package com.account.repository;

import com.account.entity.AccountPermission;
import com.account.entity.RolePermission;
import com.account.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface RolePermissionRepository  extends JpaRepository<RolePermission,Long> {

    @Modifying
    @Query(value = "UPDATE roles_permissions SET can_create = :create,can_update= :update,can_read= :read WHERE roles_id = :role and permissions_id = :per",nativeQuery = true)
    void updatePerInRole(@Param("create") String create,@Param("update") String update,@Param("read") String read,
                          @Param("role") long role,@Param("per") long per);

    @Query(value = "select * from roles_permissions WHERE roles_id = :id and permissions_id = :idP", nativeQuery = true)
    RolePermission getDetailPerInRole(@Param("id") long id,@Param("idP") long idP);
}
