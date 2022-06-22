package com.quiz.service;

import com.quiz.Dto.BaseResponse;
import com.quiz.Dto.CategoryEditRequest;
import com.quiz.Dto.CategoryRequest;
import com.quiz.entity.Category;
import com.quiz.service.repository.CategoryRepository;
import com.quiz.service.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CategoryRepository categoryRepository;
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    public BaseResponse createCategory(CategoryRequest category) {
        logger.info("Receive infor of category {} to create", category.getName());
        if (category.getName() == null || category.getName().trim().equals("")) {
            return new BaseResponse(400, "Chủ đề không được để trống ");
        }
        if (categoryRepository.findByNameIgnoreCase(category.getName()) != null) {
            logger.error("this category was existed !!!");
            throw new RuntimeException("Chủ đề đã tồn tại !!!");
        }
        Category categoryEntity = new Category();
        categoryEntity.setName(category.getName());
        categoryEntity.setActive(category.isActive());
        categoryRepository.save(categoryEntity);
        return new BaseResponse(200, "Tạo chủ đề thành công ");
    }


    public void editCategory(CategoryEditRequest request) {
        logger.info("Receive infor of category {} to edit", request.getName());

        Category categoryEntity = categoryRepository.getById(request.getId());
        if (categoryEntity == null) {
            logger.error("this category was existed !!!");

            throw new RuntimeException("this category not exist!!!");
        }
        categoryEntity.setName(request.getName());
        categoryEntity.setActive(request.isActive());
        categoryRepository.save(categoryEntity);
    }

    public List<CategoryRequest> getAllCategory() {
        logger.info("get all category");

        List<Category> categoryEntities = categoryRepository.findAll();
        List<CategoryRequest> categoryRequests = new ArrayList<>();
        for (Category categoryEntity : categoryEntities) {
            CategoryRequest categoryRequest = new CategoryRequest();
            categoryRequest.setName(categoryEntity.getName());
            categoryRequest.setActive(categoryEntity.isActive());
            categoryRequests.add(categoryRequest);
        }
        return categoryRequests;
    }

    public List<Category> getAllCate() {
        logger.info("get all category");

        return categoryRepository.findAll();
    }
}
