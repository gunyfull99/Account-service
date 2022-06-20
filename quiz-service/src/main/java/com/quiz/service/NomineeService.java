package com.quiz.service;

import com.quiz.Dto.NomineeEditRequest;
import com.quiz.Dto.NomineeRequest;
import com.quiz.entity.Nominee;
import com.quiz.service.repository.NomineeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NomineeService {

    @Autowired
    NomineeRepository nomineeRepository;

    private static final Logger logger = LoggerFactory.getLogger(NomineeService.class);


    public void createNominee(NomineeRequest nomineeRequest) {
        Nominee nomineeEntity = new Nominee();
        nomineeEntity.setName(nomineeRequest.getName());
        nomineeEntity.setStatus(nomineeRequest.isStatus());
        nomineeRepository.save(nomineeEntity);
    }

    public List<Nominee> getAll() {
        logger.info("get all nominee");
        return nomineeRepository.findAll();
    }

    public void editNominee(NomineeEditRequest nomineeRequest) {
        logger.info("Receive infor of nominee {} to edit", nomineeRequest.getName());
        Nominee nomineeEntity = nomineeRepository.getById(nomineeRequest.getId());
        nomineeEntity.setName(nomineeRequest.getName());
        nomineeEntity.setStatus(nomineeRequest.isStatus());
        nomineeRepository.save(nomineeEntity);
    }

    public void deleteNominee(Long id) {
        logger.info("Receive id of nominee {} to delete", id);

        Nominee nomineeEntity = nomineeRepository.getById(id);
        nomineeEntity.setStatus(false);
    }
}
