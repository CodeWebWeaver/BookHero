package com.parkhomovsky.bookstore.repository.category;

import com.parkhomovsky.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
