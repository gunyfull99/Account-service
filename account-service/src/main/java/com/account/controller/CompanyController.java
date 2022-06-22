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
import com.account.service.FileService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import static org.springframework.http.MediaType.parseMediaType;

@Log4j2
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FileService fileService;

    @Autowired
    private ResponseError r;
    private static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));

    public CompanyController() {
    }

    @Value("${file.upload-dir}")
    String FILE_DIRECTORY;


    // Create company
    // http://localhost:8091/company


    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:8091/company")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Company.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Company> createCompany(@RequestParam String name, @RequestParam String phone,
                                                 @RequestParam String email, @RequestParam String shortCutName, @RequestParam String address,
                                                 @RequestParam MultipartFile image) throws ResourceBadRequestException, IOException {


        Company company = new Company();
//        File myFile= new File(FILE_DIRECTORY+image.getOriginalFilename());
//        myFile.createNewFile();
//        FileOutputStream fos= new FileOutputStream(myFile);
//        fos.write(image.getBytes());


//
//        Attachment attachment = null;
//        attachment = attachmentService.saveAttachment(file);
//        String  downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/getImage/")
//                .path(attachment.getId())
//                .toUriString();

//        Path staticPath = Paths.get("static");
//        Path imagePath = Paths.get("images");
//        if (!Files.exists(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))) {
//            Files.createDirectories(CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
//        }
//        Path file = CURRENT_FOLDER.resolve(staticPath)
//                .resolve(imagePath).resolve(image.getOriginalFilename());
//        try (OutputStream os = Files.newOutputStream(file)) {
//            os.write(image.getBytes());
//        }
        company.setEmail(email);
        company.setPhone(phone);
        company.setShortCutName(shortCutName);
        company.setName(name);
        company.setAddress(address);
//        byte[] bytes = image.getBytes();
//        String base64 = Base64.getEncoder().encodeToString(bytes);
        company.setLogo(fileService.storeFile(image).toString());
        // fos.close();

        return new ResponseEntity<Company>(companyService.save(company), HttpStatus.CREATED);
    }

    @GetMapping("/getImage/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable("filename") String filename, HttpServletRequest request) throws IOException {
        Resource fileResource = fileService.getFile(filename);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(fileResource.getFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        byte[] image = new byte[0];
        try {
            image = FileUtils.readFileToByteArray(new File("uploads/" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().contentType(parseMediaType(contentType)).body(image);
    }

    // Update company
    // http://localhost:8091/company
    @CrossOrigin(origins = "http://localhost:8091/company")
    @PutMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Update success", response = Company.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Company> updateCompany(@RequestParam long id, @RequestParam String name, @RequestParam String phone,
                                                 @RequestParam String email, @RequestParam String shortCutName, @RequestParam String address,
                                                 @RequestParam MultipartFile image)
            throws ResourceNotFoundException, ResourceBadRequestException, IOException {

        Company company = companyService.getById(id);


        company.setEmail(email);
        company.setPhone(phone);
        company.setShortCutName(shortCutName);
        company.setName(name);
        company.setAddress(address);
        company.setLogo(fileService.storeFile(image).toString());

        return new ResponseEntity<Company>(companyService.save(company), HttpStatus.CREATED);
    }


    // get all company
    // http://localhost:8091/company/list
    @CrossOrigin(origins = "http://localhost:8091/company")
    @GetMapping("/list")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "get success", response = Company.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<Company>> getAllCompany() {
        return ResponseEntity.ok().body(companyService.findAll());
    }
}
