package com.account.controller;

import com.account.Dto.*;
import com.account.config.ResponseError;
import com.account.entity.*;
import com.account.exception.ResourceBadRequestException;
import com.account.exception.ResourceNotFoundException;
import com.account.repository.AccountRepository;
import com.account.service.AccountService;
import com.account.service.MyUserDetailsService;
import com.account.utils.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private ResponseError r;
    @Autowired
    private HttpSession session;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;


    // get detail account
    // http://localhost:8091/accounts/{id}
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @GetMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = Account.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})

    public ResponseEntity<AccountDto> getDetailUser(@Valid @PathVariable(name = "id") Long id) throws ResourceNotFoundException {
        Account account = accountService.findById(id);

        return ResponseEntity.ok().body(accountService.getAccById(account));
    }

    // Login
    // http://localhost:8091/accounts/login
    @PostMapping("/login")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Login success", response = JwtResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest, HttpSession session) throws ResourceNotFoundException, ResourceBadRequestException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new ResourceBadRequestException(new BaseResponse(r.notFound, "Wrong user or password"));
        }

        final UserDetails userDetails
                = myUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);
        AccountDto a = accountService.getAccByUsername(jwtRequest.getUsername());
        if (a.isActive() == false) {
            throw new ResourceBadRequestException(new BaseResponse(r.notFound, "Account is block."));
        }
        session.setAttribute("userId", a.getId());
        return new JwtResponse(token, a);
    }

    // Logout
    // http://localhost:8091/accounts/logout
    @PostMapping("/logout")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Logout success", response = String.class)})
    public String fetchSignoutSite(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "logout success !";
    }

    // Create account
    // http://localhost:8091/accounts
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PostMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Account.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody Account a) throws ResourceBadRequestException {

        Account account = accountService.getByUsername(a.getUsername());
        if (account != null) {
            throw new ResourceBadRequestException(new BaseResponse(r.isExist, "User is exist"));
        } else {
            return new ResponseEntity<AccountDto>(accountService.saveUser(a), HttpStatus.CREATED);
        }
    }

    // Update account
    // http://localhost:8091/accounts
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PutMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<AccountDto> updateAccount(@Valid @RequestBody Account a)
            throws ResourceNotFoundException, ResourceBadRequestException {

        Account accountRequest = accountService.getByUsername(a.getUsername());
        if (accountRequest == null) {
            throw new ResourceNotFoundException(new BaseResponse(r.notFound, "Not found for this id"));
        }
        accountRequest.setActive(a.getActive());
        accountRequest.setAddress(a.getAddress());
        accountRequest.setCompany(a.getCompany());
        accountRequest.setEmail(a.getEmail());
        accountRequest.setFullName(a.getFullName());
        accountRequest.setUserType(a.getUserType());
        Account account = accountService.save(accountRequest);
        return ResponseEntity.ok().body(accountService.updateUser(account));
    }

    // admin change pass
    // http://localhost:8091/accounts/admin/changepass
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PutMapping("/admin/changepass")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<AccountDto> adminChangePass(@Valid @RequestBody Account a)
            throws ResourceNotFoundException, ResourceBadRequestException {

        Account accountRequest = accountService.getByUsername(a.getUsername());
        if (accountRequest == null) {
            throw new ResourceNotFoundException(new BaseResponse(r.notFound, "Not found for this id"));
        }
        accountRequest.setPassword(a.getPassword());
        AccountDto account = accountService.saveUser(accountRequest);

        return ResponseEntity.ok().body(account);
    }

    // user change pass
    // http://localhost:8091/accounts/changepass
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PutMapping("/changepass")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<?> userChangePass(@Valid @RequestBody ChangePassForm form)
            throws ResourceNotFoundException, ResourceBadRequestException {
        accountService.UserChangePass(form);
        return ResponseEntity.ok().build();
    }

    // create role(ex:,ROLE_ADMIN,ROLE_USER,...)
    // http://localhost:8091/accounts/role/save
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PostMapping("/role/save")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Roles> createRole(@Valid @RequestBody Roles role) {

        return new ResponseEntity<Roles>(accountService.saveRole(role), HttpStatus.CREATED);
    }

    // add role to User
    // http://localhost:8091/accounts/role/addtoaccounts
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PostMapping("/role/addtoaccounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<?> addRoleToUser(@Valid @RequestBody RoleToUserForm form) {
        accountService.addRoleToUser(form.getUsername(), form.getRoleId());
        return ResponseEntity.ok().build();
    }


    // delete role to User
    // http://localhost:8091/accounts/role/deleteroleaccount
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @DeleteMapping("/role/deleteroleaccount")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<?> deleteRoleToUser(@Valid @RequestBody RoleToUserForm form) {
        accountService.removeRoleToUser(form.getUsername(), form.getRoleId());
        return ResponseEntity.ok().build();
    }

    // delete permission to User
    // http://localhost:8091/accounts/permission/deletepermissionaccount
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @DeleteMapping("/permission/deletepermissionaccount")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<?> deletePermissionUser(@Valid @RequestBody RoleToUserForm form) {
        accountService.removePermissionToUser(form.getUsername(), form.getRoleId());
        return ResponseEntity.ok().build();
    }

    // delete permission to role
    // http://localhost:8091/accounts/permission/deletepermissiontorole
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @DeleteMapping("/permission/deletepermissiontorole")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<?> deletePermissionRole(@Valid @RequestBody RolePermission form) {
        accountService.removePermissionToRole(form.getRoles_id(), form.getPermissions_id());
        return ResponseEntity.ok().build();
    }

    // create permission
    // http://localhost:8091/accounts/permission/save
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PostMapping("/permission/save")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Permission.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) {
        return new ResponseEntity<Permission>(accountService.savePermission(permission), HttpStatus.CREATED);
    }

    // get all permission by userid
    // http://localhost:8091/accounts/permission/2
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @GetMapping("/permission/{id}")
    public ResponseEntity<List<AccountPermission>> getPerByUser(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(accountService.findPerByUserId(id));
    }

    // add permission to User
    // http://localhost:8091/accounts/permission/addtoaccounts
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PostMapping("/permission/addtoaccounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<AccountPermission> addPerToUser(@Valid @RequestBody AccountPermission accountPermission) {
        accountService.addPer2User(accountPermission);
        return ResponseEntity.ok().build();
    }

    // add permission to role
    // http://localhost:8091/accounts/permission/addtorole
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PostMapping("/permission/addtorole")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<RolePermission> addPerToRole(@Valid @RequestBody RolePermission rolePermission) {
        accountService.addPer2Role(rolePermission);
        return ResponseEntity.ok().build();
    }

    // update permission to role
    // http://localhost:8091/accounts/permission/updatetorole
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PutMapping("/permission/updatetorole")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public String updatePerToRole(@Valid @RequestBody RolePermission rolePermission) {
        return accountService.updatePerInRole(rolePermission);

    }

    // update permission to user
    // http://localhost:8091/accounts/permission/updatetouser
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @PutMapping("/permission/updatetouser")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public String updatePerToUser(@Valid @RequestBody AccountPermission accountPermission) {
        return accountService.updatePerInUser(accountPermission);

    }

    // get role not in Account
    // http://localhost:8091/accounts/list/notrole/2
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/list/notrole/{id}")
    public ResponseEntity<Set<Roles>> getRoleNotInUser(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(accountService.getUserNotRole(id));
    }


    // get role not in Account
    // http://localhost:8091/accounts/list/haverole/2
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/list/haverole/{id}")
    public ResponseEntity<List<Roles>> getRoleHaveInUser(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(accountService.getUserHaveRole(id));
    }

    // get per  in Account
    // http://localhost:8091/accounts/list/havePer/2
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/list/havePer/{id}")
    public ResponseEntity<List<AccountPerForm>> getListPerInUser(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(accountService.getPerInUser(id));
    }

    // get per not in Account
    // http://localhost:8091/accounts/list/notPer/2
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/list/notPer/{id}")
    public ResponseEntity<Set<Permission>> getListPerNotInUser(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(accountService.getUserNotPer(id));
    }

    // get per not in role
    // http://localhost:8091/accounts/role/notPer/1
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/role/notPer/{id}")
    public ResponseEntity<Set<Permission>> getPerNotInRole(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(accountService.getRoleNotPer(id));
    }

    // get per  in role
    // http://localhost:8091/accounts/role/havePer/1
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/role/havePer/{id}")
    public ResponseEntity<List<RolePerForm>> getRoleHavePer(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(accountService.getPerInRole(id));
    }

    // get all Per
    // http://localhost:8091/account/per/list
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/per/list")
    public ResponseEntity<List<Permission>> getAllPer() {
        return ResponseEntity.ok().body(accountService.findAllPer());
    }

    // get detail per  in role
    // http://localhost:8091/accounts/role/per/1/1
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/role/per/{id}/{idP}")
    public ResponseEntity<RolePermission> getDetailPerInRole(@PathVariable(name = "id") long id, @PathVariable(name = "idP") long idP) {
        return ResponseEntity.ok().body(accountService.getDetailPerInRole(id, idP));
    }

    // get detail per  in user
    // http://localhost:8091/accounts/user/per/1/1
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/user/per/{id}/{idP}")
    public ResponseEntity<AccountPermission> getDetailPerInUser(@PathVariable(name = "id") long id, @PathVariable(name = "idP") long idP) {
        return ResponseEntity.ok().body(accountService.getDetailPerInUser(id, idP));
    }

    // get all Per
    // http://localhost:8091/account/role/list
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/role/list")
    public ResponseEntity<List<Roles>> getAllRole() {
        return ResponseEntity.ok().body(accountService.findAllRole());
    }

    // get all Account
    // http://localhost:8091/accounts/list
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/list")
    public ResponseEntity<List<AccountDto>> getAllAccount() {
        return ResponseEntity.ok().body(accountService.findAll());
    }

    // get can read from user
    // http://localhost:8091/accounts/canread/{perid}

    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @GetMapping("/canread/{username}/{perid}")
    public ResponseEntity<Boolean> getCanRead(@PathVariable(name = "username") String username,@PathVariable(name = "perid") long perId) {
        AccountDto a1 = accountService.getAccByUsername(username);
        AccountPermission ap = accountService.getDetailPerInUser(a1.getId(),perId);
        return ResponseEntity.ok().body(ap.isCanRead());
    }

    // get can create from user
    // http://localhost:8091/accounts/cancreate/{username}/{perid}
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @GetMapping("/cancreate/{username}/{perid}")
    public ResponseEntity<Boolean> getCanCreate(@PathVariable(name = "username") String username,@PathVariable(name = "perid") long perId) {
        AccountDto a1 = accountService.getAccByUsername(username);
        AccountPermission ap = accountService.getDetailPerInUser(a1.getId(),perId);
        return ResponseEntity.ok().body(ap.isCanCreate());
    }

    // get can update from user
    // http://localhost:8091/accounts/canupdate/{perid}
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @CrossOrigin(origins = "http://localhost:8091/accounts")
    @GetMapping("/canupdate/{username}/{perid}")
    public ResponseEntity<Boolean> getCanUpdate(@PathVariable(name = "username") String username,@PathVariable(name = "perid") long perId) {
        AccountDto a1 = accountService.getAccByUsername(username);
        AccountPermission ap = accountService.getDetailPerInUser(a1.getId(),perId);
        return ResponseEntity.ok().body(ap.isCanUpdate());
    }

}
