package com.account.controller;

import com.account.Dto.BaseResponse;
import com.account.Dto.FileUploadUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ResponseError r;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    // Create company
    // http://localhost:8091/company


    @PostMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Company.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Company> createCompany(@RequestParam String name,@RequestParam String phone,
                                                  @RequestParam String email, @RequestParam String shortCutName,@RequestParam String address,
                                                  @RequestParam MultipartFile image) throws ResourceBadRequestException, IOException {


        Company company = new Company();

        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(image.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        company.setEmail(email);
        company.setPhone(phone);
        company.setShortCutName(shortCutName);
        company.setName(name);
        company.setAddress(address);
        System.out.println(imagePath.resolve(image.getOriginalFilename()));
        byte[] bytes=image.getBytes();
        String base64= Base64.getEncoder().encodeToString(bytes);
        company.setLogo(base64);

        return new ResponseEntity<Company>(companyService.save(company), HttpStatus.CREATED);
    }

    // Update company
    // http://localhost:8091/company
    @PutMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Company.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Company> updateCompany(@RequestParam long id, @RequestParam String name,@RequestParam String phone,
                                                 @RequestParam String email, @RequestParam String shortCutName,@RequestParam String address,
                                                 @RequestParam MultipartFile image)
            throws ResourceNotFoundException, ResourceBadRequestException, IOException {

        Company company = companyService.getById(id);

        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = CURRENT_FOLDER.resolve(staticPath)
                .resolve(imagePath).resolve(image.getOriginalFilename());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        company.setEmail(email);
        company.setPhone(phone);
        company.setShortCutName(shortCutName);
        company.setName(name);
        company.setAddress(address);
        System.out.println(imagePath.resolve(image.getOriginalFilename()));
        byte[] bytes=image.getBytes();
        String base64= Base64.getEncoder().encodeToString(bytes);
        company.setLogo(base64);

        return new ResponseEntity<Company>(companyService.save(company), HttpStatus.CREATED);
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
