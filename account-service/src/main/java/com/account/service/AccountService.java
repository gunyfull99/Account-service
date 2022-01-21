package com.account.service;


import com.account.Dto.BaseResponse;
import com.account.Dto.ChangePassForm;
import com.account.entity.*;
import com.account.exception.ResourceBadRequestException;
import com.account.exception.ResourceNotFoundException;
import com.account.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AccountService {


    private static final int notFound = 80915;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountPermissionRepository accountPermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Account save(Account entity) {
        return accountRepository.save(entity);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long aLong) {
        return accountRepository.findById(aLong);
    }

    public List<AccountPermission> findAllAccountPermission() {
        return accountPermissionRepository.findAll();
    }

    public Account getById(Long id) {
        return accountRepository.getById(id);
    }

    public Account saveUser(Account user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return accountRepository.save(user);
    }

    public Account UserChangePass(ChangePassForm form) {
        Account user = accountRepository.findByUsername(form.getUsername());
        boolean match=passwordEncoder.matches(form.getOldPass(),user.getPassword());

        if(!match){
            throw new ResourceBadRequestException(new BaseResponse(notFound, "Old pass  is wrong  "));
        }else if(!form.getNewPass().equals(form.getReNewPass())){
            System.out.println(form.getNewPass() +" zz "+ form.getReNewPass());
            throw new ResourceBadRequestException(new BaseResponse(notFound, "Re-NewPass not equal new pass  "));
        }else {
            user.setPassword(passwordEncoder.encode(form.getNewPass()));
        }
        return accountRepository.save(user);
    }


    public Roles saveRole(Roles role) {
        return roleRepository.save(role);
    }

    public Permission savePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Account getByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public List<AccountPermission> findPerByUserId(long id) {
        return accountPermissionRepository.findPerByUserId(id);
    }


    public void addRoleToUser(String username, long roleId) throws ResourceNotFoundException {

        Account user = accountRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this username "));
        }

        Roles role = roleRepository.getById(roleId);
        if (role == null) {
            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this Role Name "));
        }
        // accountRepository.addRole2User(user.getId(), role.getId());
        user.getRoles().add(role);
    }
    public void removeRoleToUser(String username, long roleId) throws ResourceNotFoundException {

        Account user = accountRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this username "));
        }
        Set<Roles> userRole=user.getRoles();
        user.getRoles().removeIf(x->x.getId()==roleId);
        accountRepository.save(user);
    }

    public void removePermissionToUser(String username, long perId) throws ResourceNotFoundException {

        Account user = accountRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this username "));
        }
        Set<Permission> userPer=user.getPermissions();
        user.getPermissions().removeIf(x->x.getId()==perId);
        accountRepository.save(user);
    }

    public Set<Roles> getUserNotRole(Long id){
        return  roleRepository.getUserNotRole(id);
    }

    public Set<Permission> getUserNotPer(Long id){
        return  permissionRepository.getUserNotPer(id);
    }

    public Set<Permission> getUserHavePer(Long id){
        return  permissionRepository.getUserHavePer(id);
    }

    public Set<Permission> getRoleNotPer(Long id){
        return  permissionRepository.getRoleNotPer(id);
    }

    public Set<Permission> getRoleHavePer(Long id){
        return  permissionRepository.getRoleHavePer(id);
    }
    public Set<Roles> getUserHaveRole(Long id){
        return  roleRepository.getUserHaveRole(id);
    }


    public void addPer2User(AccountPermission accountPermission) {
        accountPermissionRepository.save(accountPermission);
    }

    public void addPer2Role(RolePermission rolePermission) {
        rolePermissionRepository.save(rolePermission);
    }
}
