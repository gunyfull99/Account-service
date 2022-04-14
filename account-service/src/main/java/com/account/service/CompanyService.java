package com.account.service;

import com.account.entity.Company;
import com.account.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;


    private static final Logger logger = LoggerFactory.getLogger(CompanyService.class);


    public List<Company> findAll() {
        logger.info("get all company");
        return companyRepository.findAll();
    }

    public Company getById(Long id) {
        logger.info("get company by id");
        return companyRepository.getById(id);
    }

    public Company findCompany(Long id) {
        logger.info("find ComPany By Id");
        return companyRepository.findComPanyById(id);
    }

    public Company save(Company company) {
        logger.info("receive info to save for company {}", company.getName());

        return companyRepository.save(company);
    }

    public void deleteById(Long id) {
        logger.info("delete company by id");
        companyRepository.deleteById(id);
    }

}
