package com.sinse.electroshop.controller.store;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @GetMapping("/store/product/registform")
    public String registForm() {
        return "store/product/regist";
    }
}
