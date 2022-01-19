package com.account.repository;

import com.account.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository  extends JpaRepository<RolePermission,Long> {
}
