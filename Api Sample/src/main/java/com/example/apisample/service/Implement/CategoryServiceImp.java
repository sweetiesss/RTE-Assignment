package com.example.apisample.service.Implement;

import com.example.apisample.entity.Category;
import com.example.apisample.exception.categoryservice.CategoryAlreadyExistsException;
import com.example.apisample.exception.categoryservice.CategoryNotFoundException;
import com.example.apisample.model.dto.category.CategoryRequestDTO;
import com.example.apisample.model.dto.category.CategoryResponseDTO;
import com.example.apisample.model.dto.category.CategoryUpdateRequestDTO;
import com.example.apisample.model.dto.pagination.APIPageableResponseDTO;
import com.example.apisample.model.mapper.CategoryMapper;
import com.example.apisample.repository.CategoryRepository;
import com.example.apisample.service.Interface.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void createCategory(CategoryRequestDTO dto) throws Exception {
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
    public void updateCategory(Integer id, CategoryUpdateRequestDTO dto) throws Exception {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        if (dto.getName() != null) category.setName(dto.getName());
        if (dto.getDescription() != null) category.setDescription(dto.getDescription());

        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Integer id) throws Exception {
        Category category = categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);

        categoryRepository.deleteById(category.getId());
    }

    @Override
    public CategoryResponseDTO getCategoryById(Integer id) throws Exception {
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
