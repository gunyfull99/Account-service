package com.account.service;


import com.account.Dto.*;
import com.account.config.ResponseError;
import com.account.entity.*;
import com.account.exception.ResourceBadRequestException;
import com.account.exception.ResourceForbiddenRequestException;
import com.account.exception.ResourceNotFoundException;
import com.account.repository.*;
import com.account.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Period;
import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

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
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountPermissionRepository accountPermissionRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private StatusWorkRepository statusWorkRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);


    public Account save(Account entity) {
        logger.info("save info of account {}", entity.getFullName());

        return accountRepository.save(entity);
    }

    public List<Account> listAllAccount() {

        logger.info("find all account");

        return accountRepository.findAll();
    }

    public List<StatusWork> listAllStatusWork() {
        logger.info("find all statuswork");
        return statusWorkRepository.findAll();
    }

    public void blockListUser(List<Long> listUser) {
        logger.info("Block list user");

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

        logger.info("search user");

        List<Account> list = null;
        if (name == null || name.trim().equals("")) {
            list = accountRepository.findAll();
        } else {
            list = accountRepository.searchUser(name);
        }
        List<AccountDto> a1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AccountDto aDto = updateUser(list.get(i));
            a1.add(aDto);
        }
        return a1;
    }

    public List<Long> getListUserId(String name) {
        logger.info("list user id by name");

        List<Long> list = accountRepository.getListUserId(name);
        return list;
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
        Account acc = accountRepository.save(a);
        List<Permission> listPer = permissionRepository.findAll();
        for (int i = 0; i < listPer.size(); i++) {
            AccountPermission ap = new AccountPermission();
            ap.setAccount_id(acc.getId());
            ap.setPermissions_id(listPer.get(i).getId());
            ap.setCan_create("false");
            ap.setCan_read("false");
            ap.setCan_update("false");
            addPer2User(ap);
        }
        ModelMapper mapper = new ModelMapper();
        AccountDto accd = mapper.map(a, AccountDto.class);
        return accd;
    }

    public BaseResponse createAccount(CreateAccountDto a) {
        logger.info("save user {}", a.getFullName());
        if (a.getBirthDay().getTime() >= System.currentTimeMillis()) {
            return new BaseResponse(400, "Ngày sinh không hợp lệ ");
        }

        a.setPassword(passwordEncoder.encode(a.getPassword()));
        Set<Roles> roles = new HashSet<>();
        roles.add(roleRepository.findById(a.getRole()).get());
        ModelMapper mapper = new ModelMapper();
        Account acc = mapper.map(a, Account.class);
        acc.setCompany(companyRepository.findComPanyById(a.getCompany()));
        acc.setRoles(roles);
        acc.setActive(true);
        acc.setUsername(a.getUsername().toLowerCase());
        acc = accountRepository.save(acc);
        List<Permission> listPer = permissionRepository.findAll();
        for (int i = 0; i < listPer.size(); i++) {
            AccountPermission ap = new AccountPermission();
            ap.setAccount_id(acc.getId());
            ap.setPermissions_id(listPer.get(i).getId());
            ap.setCan_create("false");
            ap.setCan_read("false");
            ap.setCan_update("false");
            addPer2User(ap);
        }

        return new BaseResponse(200, "Tạo tài khoản thành công ");
    }

    public Account convertAccount(Account acc, AccountDto a) {
        acc.setPhone(a.getPhone());
        acc.setAddress(a.getAddress());
        acc.setBirthDay(a.getBirthDay());
        acc.setUserType(a.getUserType());
        acc.setStartDay(a.getStartDay());
        acc.setEmail(a.getEmail());
        acc.setFullName(a.getFullName());
        acc.setCompany(companyRepository.findComPanyById(a.getCompanyId()));
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

            throw new ResourceBadRequestException(new BaseResponse(400, "Sai mật khẩu cũ"));
        } else if (!form.getNewPass().equals(form.getReNewPass())) {
            logger.error("Re-NewPass not equal new pass");

            throw new ResourceBadRequestException(new BaseResponse(400, "2 mật khẩu không khớp"));
        } else {
            user.setPassword(passwordEncoder.encode(form.getNewPass()));
        }
        return accountRepository.save(user);
    }


    public BaseResponse saveRole(Roles role) {
        logger.info("receive info to save for role {}", role.getName());
        Roles roles = roleRepository.save(role);
        List<Permission> listPer = permissionRepository.findAll();
        for (int i = 0; i < listPer.size(); i++) {
            RolePermission rp = new RolePermission();
            rp.setRoles_id(roles.getId());
            rp.setPermissions_id(listPer.get(i).getId());
            rp.setCan_create("false");
            rp.setCan_update("false");
            rp.setCan_read("false");
            addPer2Role(rp);
        }
        return new BaseResponse(200, "Create role " + role.getName() + " successful");
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


    public void addRoleToUser(String username, long roleId) throws ResourceBadRequestException {
        logger.info("add Role To User {}", username);

        Account user = accountRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy tài khoản "));
        }

        Roles role = roleRepository.getById(roleId);
        if (role == null) {
            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy role name "));
        }
        // accountRepository.addRole2User(user.getId(), role.getId());
        user.getRoles().add(role);
        accountRepository.save(user);

    }

    public void removeRoleToUser(String username, long roleId) throws ResourceBadRequestException {
        logger.info("remove Role To User {}", username);

        Account user = accountRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy tài khoản"));
        }
        Set<Roles> userRole = user.getRoles();
        user.getRoles().removeIf(x -> x.getId() == roleId);
        accountRepository.save(user);
    }

    public void removePermissionToUser(String username, long perId) throws ResourceBadRequestException {
        logger.info("remove Permission To User {}", username);

        Account user = accountRepository.findByUsername(username);
        if (user == null) {
            logger.error("Not found for this username {}", username);

            throw new ResourceBadRequestException(new BaseResponse(400, "Không tìm thấy tài khoản"));
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
            list.add(new RolePerForm(roleId, p.get(i).getId(), getDetailPerInRole(roleId, p.get(i).getId()).getCan_read(),
                    getDetailPerInRole(roleId, p.get(i).getId()).getCan_update(), getDetailPerInRole(roleId, p.get(i).getId()).getCan_create(),
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
            throw new ResourceForbiddenRequestException(new BaseResponse(r.forbidden, "Bạn không có quyền truy cập "));
        }
        return a;
    }

    public List<AccountPerForm> getPerInUser(long userId) {
        logger.info("get Permission In User");

        List<Permission> p = getUserHavePer(userId);
        List<AccountPerForm> list = new ArrayList<>();
        for (int i = 0; i < p.size(); i++) {
            list.add(new AccountPerForm(userId, p.get(i).getId(), getDetailPerInUser(userId, p.get(i).getId()).getCan_read(),
                    getDetailPerInUser(userId, p.get(i).getId()).getCan_update(), getDetailPerInUser(userId, p.get(i).getId()).getCan_create(),
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

    public BaseResponse updatePerInRole(RolePermission rolePermission) {
        logger.info("update Permission In Role");
        String canRead = "true";
        String canUpdate = "true";
        String canCreate = "true";
        RolePermission rp = rolePermissionRepository.getDetailPerInRole(rolePermission.getRoles_id(), rolePermission.getPermissions_id());

        canRead = rolePermission.getCan_read() == null ? canCreate = rp.getCan_read() : rolePermission.getCan_read();
        canUpdate = rolePermission.getCan_update() == null ? canCreate = rp.getCan_update() : rolePermission.getCan_update();
        canCreate = rolePermission.getCan_create() == null ? canCreate = rp.getCan_create() : rolePermission.getCan_create();

        rolePermissionRepository.updatePerInRole(canCreate, canUpdate, canRead, rolePermission.getRoles_id(), rolePermission.getPermissions_id());
        return new BaseResponse(200, "Update success!");
    }

    public BaseResponse updatePerInUser(AccountPermission accountPermission) {
        logger.info("update Permission In User");
        String canRead = "true";
        String canUpdate = "true";
        String canCreate = "true";
        AccountPermission rp = accountPermissionRepository.getDetailPerInUser(accountPermission.getAccount_id(), accountPermission.getPermissions_id());

        canRead = accountPermission.getCan_read() == null ? canCreate = rp.getCan_read() : accountPermission.getCan_read();
        canUpdate = accountPermission.getCan_update() == null ? canCreate = rp.getCan_update() : accountPermission.getCan_update();
        canCreate = accountPermission.getCan_create() == null ? canCreate = rp.getCan_create() : accountPermission.getCan_create();
        accountPermissionRepository.updatePerInUser(canCreate, canUpdate, canRead, accountPermission.getAccount_id(), accountPermission.getPermissions_id());
        return new BaseResponse(200, "Update success!");
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

    public Page<Account> searchUserWithPaging(AccountPaging accountPaging) {
        logger.info("Search user");

        Page<Account> a = null;
        Pageable pageable = PageRequest.of(accountPaging.getPage() - 1, accountPaging.getLimit(), Sort.by("id").descending());

        // a=accountRepository.filter(accountPaging.getSearch(),Long.parseLong(accountPaging.getRole()),accountPaging.getUserType(),pageable);

        if (accountPaging.getRole() == null || accountPaging.getRole().trim().equals("")) {
            a = accountRepository.filterWhereNoRole(accountPaging.getSearch(),
                    accountPaging.getUserType() == null || accountPaging.getUserType().trim().equals("") ? "%%" : accountPaging.getUserType(),
                    pageable);
        } else if (accountPaging.getRole() != null && (accountPaging.getUserType() != null && !accountPaging.getUserType().trim().equals(""))) {
            a = accountRepository.filterWhereHaveRoleAndType(accountPaging.getSearch(),
                    Long.parseLong(accountPaging.getRole()),
                    accountPaging.getUserType(),
                    pageable);
        } else if (accountPaging.getUserType() == null || accountPaging.getUserType().trim().equals("")) {
            a = accountRepository.findAllByFullNameContainingIgnoreCaseAndRolesIdAndIsActive(accountPaging.getSearch(),
                    Long.parseLong(accountPaging.getRole()), true,
                    pageable);
        }
        return a;
    }

    public void sendHtmlMail(DataMailDTO dataMail, String templateName) throws MessagingException {
        logger.info("Send mail");

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        Context context = new Context();
        context.setVariables(dataMail.getProps());

        String html = templateEngine.process(templateName, context);

        helper.setTo(dataMail.getTo());
        helper.setSubject(dataMail.getSubject());
        helper.setText(html, true);

        mailSender.send(message);
    }

    public BaseResponse sendMailPassWord(ClientSdi sdi) throws ResourceNotFoundException {
        logger.info("Send mail");

        try {
            Account a = accountRepository.findByEmail(sdi.getEmail());
            if (a == null) {
                throw new ResourceNotFoundException(new BaseResponse(r.notFound, "Email không tồn tại "));
            }
            String newPass = DataUtils.generateTempPwd(6);
            DataMailDTO dataMail = new DataMailDTO();
            dataMail.setTo(sdi.getEmail());
            dataMail.setSubject("Gửi lại mật khẩu ");
            Map<String, Object> props = new HashMap<>();
            props.put("password", newPass);
            dataMail.setProps(props);
            sendHtmlMail(dataMail, "client");
            a.setPassword(newPass);
            AccountDto account = saveUserWithPassword(a);
            return new BaseResponse(200, "Gửi mail thành công");
        } catch (MessagingException exp) {
            exp.printStackTrace();
        }
        return new BaseResponse(200, "Gửi mail thất bại");
    }
}
