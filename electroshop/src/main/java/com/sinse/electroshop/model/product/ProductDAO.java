package com.sinse.electroshop.model.product;

import com.sinse.electroshop.domain.Product;

import java.util.List;

public interface ProductDAO {
    public List selectAll();
    public Product selectById(int product_id);
    public Product regist(Product product);
}
