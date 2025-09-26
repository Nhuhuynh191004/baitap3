package com.example.baitap3.repository;

import com.example.baitap3.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {


    // lấy sản phẩm theo category
    List<Product> findByCategoryId(Integer categoryId);

    // query động cho search
    @Query("SELECT p FROM Product p " +
            "WHERE (:name IS NULL OR p.name LIKE %:name%) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<Product> searchProducts(@Param("name") String name,
                                 @Param("categoryId") Integer categoryId,
                                 @Param("status") Integer status,
                                 Pageable pageable);
}
