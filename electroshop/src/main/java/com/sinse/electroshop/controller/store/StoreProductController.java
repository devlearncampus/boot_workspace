package com.sinse.electroshop.controller.store;

import com.sinse.electroshop.domain.Product;
import com.sinse.electroshop.model.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreProductController {
    private final ProductService productService;

    @GetMapping("/product/registform")
    public String registForm() {
        return "store/product/regist";
    }

    @PostMapping("/product/regist")
    @ResponseBody
    public ResponseEntity<String> regist(Product product) {

        productService.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).body("등록성공");
    }

    @GetMapping("/product/list")
    public String getList(Model model){
        List productList=productService.getList();
        model.addAttribute("productList", productList);

        return "store/product/list";
    }

    @GetMapping("/product/listbystore")
    public String getListByStore(Model model,  int storeId){
        List productList = productService.getListByStoreId(storeId);
        model.addAttribute("productList", productList);
        return "store/product/list";
    }
}
