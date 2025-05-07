package com.example.apisample.category.repository;

import com.example.apisample.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByName(String name);

    Page<Category> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

}
