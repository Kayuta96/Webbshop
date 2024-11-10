package com.example.webbshop.repository;

import com.example.webbshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by name containing a keyword (e.g., for search functionality)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Find products by category ID
    List<Product> findByCategoryId(Long categoryId);

    // Find products within a specific price range
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Find all available (in-stock) products
    List<Product> findByQuantityGreaterThan(int quantity);
}
