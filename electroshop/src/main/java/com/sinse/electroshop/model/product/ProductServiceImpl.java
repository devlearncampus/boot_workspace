package com.sinse.electroshop.model.product;

import com.sinse.electroshop.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    @Override
    public List getList() {
        return productDAO.selectAll();
    }

    @Override
    public Product getDetail(int product_id) {
        return productDAO.selectById(product_id);
    }
}
