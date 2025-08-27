package com.sinse.electroshop.model.product;

import com.sinse.electroshop.domain.Product;

import java.util.List;

public interface ProductService {
    public List getList();
    public Product getDetail(int product_id);
    public Product save(Product product);
}
