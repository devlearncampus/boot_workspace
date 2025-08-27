package com.sinse.electroshop.controller.store;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
public class StoreProductController {

    @GetMapping("/product/registform")
    public String registForm() {
        return "store/product/regist";
    }
}
