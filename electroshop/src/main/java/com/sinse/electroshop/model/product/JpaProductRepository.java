package com.sinse.electroshop.model.product;

import com.sinse.electroshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaProductRepository extends JpaRepository<Product, Integer> {
    //모든 상품 가져오기
    public List<Product> findAll();

    //상품 한건 가져오기
    public Product findById(int product_id);
}
