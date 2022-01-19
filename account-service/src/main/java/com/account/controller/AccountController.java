package com.account.controller;

import com.account.Dto.*;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private static final int notFound = 80915;
    private static final int isExist = 80916;
    private static final int usernameEmpty = 80911;
    private static final int passwordEmpty = 80912;
    @Autowired
    private HttpSession session;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;


    // @Autowired
    // private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

//    @Autowired
//    private ModelMapper modelMapper;
//    @GetMapping
//    public List<PostDto> getAllPosts() {
//
//        return postService.getAllPosts().stream().map(post -> modelMapper.map(post, PostDto.class))
//                .collect(Collectors.toList());
//    }

    // get detail account
    // http://localhost:8091/accounts/{id}
    @GetMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success", response = Account.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})

    public ResponseEntity<Account> getAccountById(@Valid @PathVariable(name = "id") Long id) throws ResourceNotFoundException {
        Account account = accountService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this id")));

        // convert entity to DTO
        // AccountDto postResponse = modelMapper.map(account, AccountDto.class);


        return ResponseEntity.ok().body(account);
    }

    // Login
    // http://localhost:8091/accounts/login
    @PostMapping("/login")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Login success", response = JwtResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws ResourceNotFoundException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new ResourceNotFoundException(new BaseResponse(notFound, "Wrong user or password"));
        }

        final UserDetails userDetails
                = myUserDetailsService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);
        Account a = accountService.getByUsername(jwtRequest.getUsername());
        return new JwtResponse(token, a);
    }

    // Logout
    // http://localhost:8091/accounts/logout
    @PostMapping("/logout")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Logout success", response = String.class)})
    public String logout() {
        return "Logout success";
    }

    // Create account
    // http://localhost:8091/accounts
    @PostMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Account.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account a) throws ResourceBadRequestException {

        Account account = accountService.getByUsername(a.getUsername());
        if (account != null) {
            throw new ResourceBadRequestException(new BaseResponse(isExist, "User is exist"));
        } else {
            return new ResponseEntity<Account>(accountService.saveUser(a), HttpStatus.CREATED);
        }
        // convert DTO to entity
        //   Account postRequest = modelMapper.map(accountDto, Account.class);
        // convert entity to DTO
        //AccountDto postResponse = modelMapper.map(account, AccountDto.class);
    }

    // Update account
    // http://localhost:8091/accounts
    @PutMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Account> updateAccount(@Valid @RequestBody Account a)
            throws ResourceNotFoundException, ResourceBadRequestException {

        Account accountRequest = accountService.getByUsername(a.getUsername());
        if (accountRequest == null) {
            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this id"));
        }
        //  accountRequest = modelMapper.map(accountDto, Account.class);
        accountRequest.setPassword(a.getPassword());
        accountRequest.setActive(a.getActive());
        accountRequest.setAddress(a.getAddress());
        accountRequest.setCompany(a.getCompany());
        accountRequest.setEmail(a.getEmail());
        accountRequest.setFullName(a.getFullName());
        accountRequest.setUserType(a.getUserType());
        Account account = accountService.saveUser(accountRequest);
        //AccountDto accountResponse = modelMapper.map(account, AccountDto.class);

        return ResponseEntity.ok().body(account);
    }

    // create role(ex:,ROLE_ADMIN,ROLE_USER,...)
    // http://localhost:8091/accounts/role/save
    @PostMapping("/role/save")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Roles> saveRole(@Valid @RequestBody Roles role) {

        return new ResponseEntity<Roles>(accountService.saveRole(role), HttpStatus.CREATED);
    }

    // add role to User
    // http://localhost:8091/accounts/role/addtoaccounts
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

    // create permission
    // http://localhost:8091/accounts/permission/save
    @PostMapping("/permission/save")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Permission.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Permission> savePermission(@Valid @RequestBody Permission permission) {
        return new ResponseEntity<Permission>(accountService.savePermission(permission), HttpStatus.CREATED);
    }

    // get all permission by userid
    // http://localhost:8091/accounts/permission/2
    @GetMapping("/permission/{id}")
    public ResponseEntity<List<AccountPermission>> getPerByUser(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(accountService.findPerByUserId(id));
    }

    // add permission to User
    // http://localhost:8091/accounts/permission/addtoaccounts
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
    // http://localhost:8091/accounts/permission/addtoaccounts
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


    // get role not in Account
    // http://localhost:8091/accounts/list/notrole/2
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/list/notrole/{id}")
    public ResponseEntity<Set<Roles>> getRoleNotInUser(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok().body(accountService.getUserNotRole(id));
    }

    // get all Account
    // http://localhost:8091/accounts/list
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Account.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    @GetMapping("/list")
    public ResponseEntity<List<Account>> getAllAccount() {
        return ResponseEntity.ok().body(accountService.findAll());
    }
}
