package com.account.controller;

import com.account.Dto.BaseResponse;
import com.account.config.ResponseError;
import com.account.entity.Account;
import com.account.entity.Company;
import com.account.exception.ResourceBadRequestException;
import com.account.exception.ResourceNotFoundException;
import com.account.repository.CompanyRepository;
import com.account.service.CompanyService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ResponseError r;


    // Create company
    // http://localhost:8091/company
    @PostMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Company.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company c) throws ResourceBadRequestException {
        return new ResponseEntity<Company>(companyService.save(c), HttpStatus.CREATED);
    }

    // Update company
    // http://localhost:8091/company
    @PutMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Company.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company c)
            throws ResourceNotFoundException, ResourceBadRequestException {

        Company company = companyService.getById(c.getId());
        if (company == null) {
            throw new ResourceNotFoundException(new BaseResponse(r.notFound, "Not found for this id"));
        }
        company.setEmail(c.getEmail());
        company.setActive(c.getActive());
        company.setAddress(c.getAddress());
        company.setPhone(c.getPhone());
        company.setName(c.getName());
        return ResponseEntity.ok().body(companyService.save(company));
    }

    // delete company
    // http://localhost:8091/company/2
//    @DeleteMapping("/{id}")
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Company.class),
//            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
//            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
//            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
//            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
//    public ResponseEntity<String> delete(@PathVariable("id") long id) throws ResourceNotFoundException {
//        Company company =  companyService.findCompany(id);
//        if (company == null) {
//            throw new ResourceNotFoundException(new BaseResponse(notFound, "Not found for this id"));
//        }
//        companyService.deleteById(id);
//        return new ResponseEntity<String>("Company deleted successfully!.", HttpStatus.OK);
//    }

    // get all company
    // http://localhost:8091/company/list
    @GetMapping("/list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Company.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<Company>> getAllCompany() {
        return ResponseEntity.ok().body(companyService.findAll());
    }
}
