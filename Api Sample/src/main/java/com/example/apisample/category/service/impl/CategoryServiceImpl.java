package com.example.apisample.category.service.impl;

import com.example.apisample.category.entity.Category;
import com.example.apisample.category.exception.CategoryAlreadyExistsException;
import com.example.apisample.category.exception.CategoryNotFoundException;
import com.example.apisample.category.model.dto.CategoryRequestDTO;
import com.example.apisample.category.model.dto.CategoryResponseDTO;
import com.example.apisample.category.model.dto.CategoryUpdateRequestDTO;
import com.example.apisample.category.model.mapper.CategoryMapper;
import com.example.apisample.category.repository.CategoryRepository;
import com.example.apisample.category.service.CategoryService;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void createCategory(CategoryRequestDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new CategoryAlreadyExistsException();
        }

        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void updateCategory(Integer id, CategoryUpdateRequestDTO dto)  {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        if (dto.getName() != null) category.setName(dto.getName());
        if (dto.getDescription() != null) category.setDescription(dto.getDescription());

        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id)  {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        categoryRepository.deleteById(category.getId());
    }

    @Override
    public CategoryResponseDTO getCategoryById(Integer id)  {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
        return CategoryMapper.categoryToDTO(category);
    }

    @Override
    public APIPageableResponseDTO<CategoryResponseDTO> getAllCategories(int pageNo, int pageSize, String search, String sort) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Category> page = categoryRepository.findAll(pageable);
        Page<CategoryResponseDTO> dtoPage = page.map(CategoryMapper::categoryToDTO);

        return new APIPageableResponseDTO<>(dtoPage);
    }
}
