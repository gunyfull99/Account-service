package com.account.service;


import com.account.Dto.*;
import com.account.config.ResponseError;
import com.account.entity.*;
import com.account.exception.ResourceBadRequestException;
import com.account.exception.ResourceForbiddenRequestException;
import com.account.exception.ResourceNotFoundException;
import com.account.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {


    private static final int notFound = 80915;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private ResponseError r;
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

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);


    public Account save(Account entity) {
        logger.info("save info of account {}", entity.getFullName());

        return accountRepository.save(entity);
    }

    public void blockListUser(List<Long> listUser) {
        for (int i = 0; i < listUser.size(); i++) {
            Account a = accountRepository.selectById(listUser.get(i));
            a.setActive(false);
            accountRepository.save(a);
        }
    }

    public Page<Account> findAll(AccountPaging ap) {
        int offset = ap.getPage();
        if (offset < 0) {
            offset = 1;
        }
        logger.info("Get all account");
        Page<Account> a = accountRepository.findAll(PageRequest.of(offset - 1, ap.getLimit()));
        if (a.isEmpty()) {
            logger.error("no account exist !!!");
            throw new RuntimeException("no account exist !!!");
        }
        return a;
    }

    public List<AccountDto> convertAccount(List<Account> list) {
        List<AccountDto> a1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AccountDto aDto = updateUser(list.get(i));
            a1.add(aDto);
        }
        return a1;
    }

    public List<AccountDto> searchUser(String name) {
        List<Account> list = accountRepository.searchUser(name);
        List<AccountDto> a1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AccountDto aDto = updateUser(list.get(i));
            a1.add(aDto);
        }
        return a1;
    }

    public List<Roles> findAllRole() {

        logger.info("get all role");
        return roleRepository.findAll();
    }

    public List<Permission> findAllPer() {
        logger.info("get all Permission");
        return permissionRepository.findAll();
    }

    public Account findById(Long aLong) {
        logger.info("get account by id");
        return accountRepository.selectById(aLong);
    }

    public List<AccountPermission> findAllAccountPermission() {
        logger.info("get all Permission of account");
        return accountPermissionRepository.findAll();
    }

    public Account getById(Long id) {
        logger.info("get account by id");
        return accountRepository.getById(id);
    }

    public AccountDto saveUserWithPassword(Account a) {
        logger.info("save user {}", a.getFullName());

        a.setPassword(passwordEncoder.encode(a.getPassword()));
        accountRepository.save(a);
        ModelMapper mapper = new ModelMapper();
        AccountDto acc = mapper.map(a, AccountDto.class);
        return acc;
    }

    public Account convertAccount(Account a) {
        Account acc = new Account();
        acc.setActive(a.getActive());
        acc.setAddress(a.getAddress());
        acc.setCompany(a.getCompany());
        acc.setEmail(a.getEmail());
        acc.setFullName(a.getFullName());
        acc.setUserType(a.getUserType());
        acc.setBirthDay(a.getBirthDay());
        acc.setStartDay(a.getStartDay());
        return acc;
    }

    public AccountDto updateUser(Account a) {
        logger.info("update user {}", a.getFullName());
        ModelMapper mapper = new ModelMapper();
        AccountDto acc = mapper.map(a, AccountDto.class);
        return acc;
    }

    public Account UserChangePass(ChangePassForm form) {
        logger.info("change password for user {}", form.getUsername());

        Account user = accountRepository.findByUsername(form.getUsername());

        if (user == null) {
            logger.error("user not exist !!!");
            throw new RuntimeException("user not exist !!!");
        }
        boolean match = passwordEncoder.matches(form.getOldPass(), user.getPassword());

        if (!match) {
            logger.error("Old pass  is wrong");

            throw new ResourceBadRequestException(new BaseResponse(notFound, "Old pass  is wrong  "));
        } else if (!form.getNewPass().equals(form.getReNewPass())) {
            logger.error("Re-NewPass not equal new pass");

            throw new ResourceBadRequestException(new BaseResponse(notFound, "Re-NewPass not equal new pass  "));
        } else {
            user.setPassword(passwordEncoder.encode(form.getNewPass()));
        }
        return accountRepository.save(user);
    }


    public Roles saveRole(Roles role) {
        logger.info("receive info to save for role {}", role.getName());

        return roleRepository.save(role);
    }

    public Permission savePermission(Permission permission) {
        logger.info("receive info to save for role {}", permission.getName());
        return permissionRepository.save(permission);
    }

    public Account getByUsername(String username) {
        logger.info("get account By Username {}", username);

        return accountRepository.findByUsername(username);
    }

    public List<AccountPermission> findPerByUserId(long id) {
        logger.info("find Permission By UserId");
        return accountPermissionRepository.findPerByUserId(id);
    }


    public void addRoleToUser(String username, long roleId) throws ResourceNotFoundException {
        logger.info("add Role To User {}", username);

        Account user = accountRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this username "));
        }

        Roles role = roleRepository.getById(roleId);
        if (role == null) {
            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this Role Name "));
        }
        // accountRepository.addRole2User(user.getId(), role.getId());
        user.getRoles().add(role);
        accountRepository.save(user);

    }

    public void removeRoleToUser(String username, long roleId) throws ResourceNotFoundException {
        logger.info("remove Role To User {}", username);

        Account user = accountRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this username "));
        }
        Set<Roles> userRole = user.getRoles();
        user.getRoles().removeIf(x -> x.getId() == roleId);
        accountRepository.save(user);
    }

    public void removePermissionToUser(String username, long perId) throws ResourceNotFoundException {
        logger.info("remove Permission To User {}", username);

        Account user = accountRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this username "));
        }
        Set<Permission> userPer = user.getPermissions();
        user.getPermissions().removeIf(x -> x.getId() == perId);
        accountRepository.save(user);
    }

    public void removePermissionToRole(long roleId, long perId) {
        logger.info("remove Permission To Role");

        Roles roles = roleRepository.getById(roleId);
        if (roles == null) {
            logger.error("this role not exist !!!");
            throw new RuntimeException("this role not exist !!!");
        }
        Set<Permission> per = roles.getPermissions();
        roles.getPermissions().removeIf(x -> x.getId() == perId);
        roleRepository.save(roles);
    }


    public Set<Roles> getUserNotRole(Long id) {
        logger.info("get User Not Role");
        return roleRepository.getUserNotRole(id);
    }

    public Set<Permission> getUserNotPer(Long id) {
        logger.info("get User Not Permission");

        return permissionRepository.getUserNotPer(id);
    }

    public List<Permission> getUserHavePer(Long id) {
        logger.info("get User Have Permission");
        return permissionRepository.getUserHavePer(id);
    }

    public Set<Permission> getRoleNotPer(Long id) {
        logger.info("get Role Not Permission");
        return permissionRepository.getRoleNotPer(id);
    }

    public List<Permission> getRoleHavePer(Long id) {
        logger.info("get Role Have Permission");
        return permissionRepository.getRoleHavePer(id);
    }

    public List<RolePerForm> getPerInRole(long roleId) {
        logger.info("get Permission In Role ");

        List<Permission> p = getRoleHavePer(roleId);
        List<RolePerForm> list = new ArrayList<>();
        for (int i = 0; i < p.size(); i++) {
            list.add(new RolePerForm(roleId, p.get(i).getId(), getDetailPerInRole(roleId, p.get(i).getId()).isCanRead(),
                    getDetailPerInRole(roleId, p.get(i).getId()).isCanUpdate(), getDetailPerInRole(roleId, p.get(i).getId()).isCanCreate(),
                    p.get(i).getName()));
        }
        return list;
    }


    public List<Roles> getUserHaveRole(Long id) {

        logger.info("get User Have Role");
        return roleRepository.getUserHaveRole(id);
    }


    public void addPer2User(AccountPermission accountPermission) {
        logger.info("add Permission 2User");

        accountPermissionRepository.save(accountPermission);
    }

    public AccountPermission getDetailPerInUser(long id, long idP) throws ResourceNotFoundException {
        logger.info("get Detail Permission In User");
        AccountPermission a = accountPermissionRepository.getDetailPerInUser(id, idP);
        if (a == null) {
            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "You can't access "));
        }
        return a;
    }

    public List<AccountPerForm> getPerInUser(long userId) {
        logger.info("get Permission In User");

        List<Permission> p = getUserHavePer(userId);
        List<AccountPerForm> list = new ArrayList<>();
        for (int i = 0; i < p.size(); i++) {
            list.add(new AccountPerForm(userId, p.get(i).getId(), getDetailPerInUser(userId, p.get(i).getId()).isCanRead(),
                    getDetailPerInUser(userId, p.get(i).getId()).isCanUpdate(), getDetailPerInUser(userId, p.get(i).getId()).isCanCreate(),
                    p.get(i).getName()));
        }
        return list;
    }

    public RolePermission getDetailPerInRole(long id, long idP) {
        logger.info("get Detail Permission In Role");

        return rolePermissionRepository.getDetailPerInRole(id, idP);
    }

    public void addPer2Role(RolePermission rolePermission) {
        logger.info("add Permission 2Role");

        rolePermissionRepository.save(rolePermission);
    }

    public String updatePerInRole(RolePermission rolePermission) {
        logger.info("update Permission In Role");

        rolePermissionRepository.updatePerInRole(rolePermission.isCanCreate(), rolePermission.isCanUpdate(), rolePermission.isCanRead(), rolePermission.getRoles_id(), rolePermission.getPermissions_id());
        return "Update success!";
    }

    public String updatePerInUser(AccountPermission accountPermission) {
        logger.info("update Permission In User");

        accountPermissionRepository.updatePerInUser(accountPermission.isCanCreate(), accountPermission.isCanUpdate(), accountPermission.isCanRead(), accountPermission.getAccount_id(), accountPermission.getPermissions_id());
        return "Update success!";
    }

    public AccountDto getAccByUsername(String username) {
        logger.info("get Account By Username {}", username);

        ModelMapper mapper = new ModelMapper();
        Account a = accountRepository.findByUsername(username);
        AccountDto acc = mapper.map(a, AccountDto.class);
        return acc;
    }

    public AccountDto getAccById(Account a) {
        logger.info("get Account By Id ");
        ModelMapper mapper = new ModelMapper();
        AccountDto acc = mapper.map(a, AccountDto.class);
        return acc;
    }

    public Page<Account> searchUserWithPaging(String name, AccountPaging accountPaging) {
        Page<Account> a = null;
        Pageable pageable = PageRequest.of(accountPaging.getPage() - 1, accountPaging.getLimit());

        if (accountPaging.getRole() == null || !accountPaging.getRole().isEmpty()) {
            a = accountRepository.findAllByRolesId(Long.parseLong(accountPaging.getRole()), pageable);
        } else if (name.isEmpty() || name == null || name.trim().equals("")) {
            a = accountRepository.findAll(pageable);
        } else {
            a = accountRepository.findAllByFullNameContainingIgnoreCase(name, pageable);
        }
        return a;
    }


}
