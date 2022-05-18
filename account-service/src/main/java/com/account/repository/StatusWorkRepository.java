package com.account.repository;

import com.account.entity.AccountPermission;
import com.account.entity.StatusWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusWorkRepository extends JpaRepository<StatusWork,Long> {

}
